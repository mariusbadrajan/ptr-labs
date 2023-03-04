package org.marius
package lab1.checkpoint4.minimal

import lab1.checkpoint4.minimal.Worker.{EchoMessage, Message}

import akka.actor.typed.*
import akka.actor.typed.scaladsl.{Behaviors, Routers}
import akka.routing.Broadcast

//! When parent actor DIES (Stop / Restart), the child actors are also STOPPED.
object Supervisor {
  // Command - type of messages that this actor can receive.
  sealed trait Command

  // ForwardToWorker - message that is sent to the supervisor to forward a message to a specific worker.
  case class ForwardToWorker(workerName: String, message: Message) extends Command

  // ForwardToAllWorkers - message that is sent to the supervisor to forward a message to all workers.
  case class ForwardToAllWorkers(message: Message) extends Command

  // WorkerTerminated - message that is sent to the supervisor when a worker is terminated.
  case class WorkerTerminated(ref: ActorRef[Message]) extends Command

  // Kill - message that is sent to the supervisor to kill it (testing purpose of .withStopChildren(false)).
  case object Kill extends Command

  // apply - command to start the actor.
  // Behavior[Command] is the return type of the apply method.
  def apply(numOfWorkers: Int): Behavior[Command] =
    // Behaviors.setup - factory for a behavior.
    // .setup is outside .supervise => setup block will be run ONLY when the actor is first started.
    // .supervise + .withStopChildren(false) inside .setup => child actors are not influenced when the parent actor is restarted.
    // The restarted parent instance will then have the same children as before the failure.
    Behaviors
      .setup[Command] { context =>
        // Spawn a number of workers.
        val mainWorkers: List[ActorRef[Message]] = (1 to numOfWorkers).map { i =>
          val worker = context.spawn(Worker(s"Worker $i"), s"worker-$i")
          //! ONLY FOR PARENT ACTORS context.watch(<child actor>) + case (context, Terminated(ref)) => {...}, otherwise DeathPactException.
          // Registers this actor as a Monitor for the provided ActorRef.
          // This actor will receive a Terminated(subject) message when watched actor is terminated.
          context.watch(worker)

          // Registers this actor as a Monitor for the provided ActorRef.
          // This actor will receive the specified message (WorkerTerminated(worker) - defined below) when watched actor is terminated.
          //* context.watchWith(worker, WorkerTerminated(worker))

          // Add spawned worker to the list of workers.
          worker
        // Convert the indexed sequence to a list.
        }.toList

        // Method to handle the received generic command.
        def receiveCommand(command: Command, workers: List[ActorRef[Message]]): Behavior[Command] = {
          // Match the received command.
          command match {
            // 1. Forward the received message to a specific worker.
            case ForwardToWorker(workerName, workerMessage) =>
              // Find the worker with the given name.
              workers.find(actorRef => actorRef.path.name == workerName) match {
                // If the worker was found, forward the message to it.
                case Some(worker) => worker ! workerMessage
                // If the worker was not found, log a warning.
                case None => context.log.warn(s"No worker was found with name `$workerName`.")
              }
              // Return the same behavior.
              Behaviors.same
            // 2. Forward the received message to all workers.
            case ForwardToAllWorkers(message) =>
              // If there are workers to forward the message to, forward the message to the first worker and call the method again with the rest of the workers.
              if (workers.length > 0) {
                // Forward the message to the first worker.
                val currentWorker = workers.head
                // Send the message to the first worker.
                currentWorker ! message
                // Send the message to rest of the workers.
                receiveCommand(command, workers.tail)
              }
              else {
                // Return the same behavior (no more workers to forward the message to).
                Behaviors.same
              }
            // Handle the received `kill` signal.
            case Kill =>
              throw new Exception("Supervisor was killed.")
            // 3. Handle the received `terminated` signal from a watched (context.watch(worker) - defined above) worker.
            // Instead of restarting the worker, spawn a new one with the same name - have implemented this, because SupervisorStrategy.restart was not working, now it is.
            // This strategy can be used when we want to stop an actor and start a new one with the same name with new state / behavior,
            // instead of restarting the actor with the same state / behavior which which could become damaged during the recovery stage.
            //* case WorkerTerminated(worker) =>
              // Spawn a new worker with the same name as the terminated worker.
              //* val newWorker = context.spawn(Worker(worker.path.name), worker.path.name)
              // Register this actor as a Monitor for the provided ActorRef.
              //* context.watchWith(newWorker, WorkerTerminated(newWorker))
              // Create a new list of workers, with the terminated worker removed from mainWorkers and the new worker added to mainWorkers.
              //* val updatedWorkers = workers.filterNot(_ == worker) :+ newWorker
              // Update the global list of workers.
              //* mainWorkers = updatedWorkers
              // Call the method again with command to forward a message (the faulty worker was restarted) to the new worker.
              //* receiveCommand(ForwardToWorker(newWorker.path.name, EchoMessage(s"Worker restarted: `${newWorker.path.name}`")), updatedWorkers)
          }
        }

        // Behaviors.supervise - wraps the given behavior with a supervisor that will restart / resume / stop the actor in case of failure (exceptions thrown inside an actor).
        //! The default supervision strategy is to stop the actor if an exception is thrown.
        Behaviors
          .supervise[Command] {
            // Behaviors.receiveMessagePartial - construct an actor Behavior from a partial message handler which treats undefined messages as unhandled,
            // i.e. no need to specify case _ => Behaviors.unhandled.
            Behaviors
              .receiveMessagePartial[Command] {
                // Handle the received generic command.
                case command: Command => receiveCommand(command, mainWorkers)
              }
              // Behaviors.receiveSignal - construct an actor Behavior that can react to lifecycle signals only.
              .receiveSignal {
                //* Same description as in Worker class
                case (context, PostStop) =>
                  context.log.info("Supervisor received stopped signal.")
                  Behaviors.stopped
                //* Same description as in Worker class
                case (context, PreRestart) =>
                  context.log.info("Supervisor received restarted signal.")
                  Behaviors.same
                //* Same description as in Worker class
                case (context, Terminated(ref)) =>
                  context.log.info(s"Supervisor received terminated signal from `${ref.path.name}`.")
                  Behaviors.same
              }
          }
          //* Same description as in Worker class
          
          // By default all child actors are stopped when the parent actor is stopped.
          // But when .setup is outside .supervise, the .setup code block will be run only when parent is first started and NOT when it is restarted,
          // therefore died child actors will remain stopped and if we will try to send a message to them through the parent, we will get a DeadLetter,
          // meaning that the message was sent to an actor that does not exist. So .withStopChildren(false) helps us to keep the child actors alive.
          .onFailure(SupervisorStrategy.restart.withStopChildren(false))
      }
}
