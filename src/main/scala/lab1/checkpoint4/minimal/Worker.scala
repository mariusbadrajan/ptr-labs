package org.marius
package lab1.checkpoint4.minimal

import lab1.checkpoint4.minimal.Worker.Message

import akka.actor.typed.*
import akka.actor.typed.scaladsl.Behaviors

//! When parent actor DIES (Stop / Restart), the child actors are also STOPPED.
object Worker {
  // Message - type of messages that this actor can receive.
  sealed trait Message

  // EchoMessage - message that will be sent to the worker.
  case class EchoMessage(message: String) extends Message

  // Kill - message that will be sent to the worker to kill it.
  case object Kill extends Message

  // apply - command to start the actor.
  // Behavior[Message] is the return type of the apply method.
  def apply(name: String): Behavior[Message] =
    // Behaviors.supervise - wraps the given behavior with a supervisor that will restart / resume / stop the actor in case of failure (exceptions thrown inside an actor).
    //! The default supervision strategy is to stop the actor if an exception is thrown.
    Behaviors
      .supervise[Message] {
        // Behaviors.setup - factory for a behavior.
        // .setup is inside .supervise => setup block will be run when the actor is first started and also when is restarted.
        // When .setup is inside .supervise and inside .setup is/are spawned one/some child actor/s and parent is restarted,
        // then child actor/s are stopped to avoid resource leaks of creating new child actor/s each time the parent is restarted.
        Behaviors
          .setup[Message] { context =>
            // Behaviors.receiveMessage - simplified version of Receive with only a single argument - the message to be handled.
            Behaviors
              .receiveMessage[Message] {
                // Handle any received message.
                case EchoMessage(message) =>
                  context.log.info(s"Worker `$name` received message: $message.")
                  // Return the same behavior.
                  Behaviors.same
                // Handle kill message.
                case Kill =>
                  context.log.info(s"Worker `$name` was killed.")
                  // Throws an exception.
                  throw new Exception(s"Worker '$name' was killed.")
              }
              // Behaviors.receiveSignal - construct an actor Behavior that can react to lifecycle signals only.
              .receiveSignal {
                // Before a supervised actor is stopped it is sent the PostStop signal giving it a chance to clean up resources it has created.
                // Lifecycle signal that is fired after this actor and all its child actors (transitively) have terminated.
                // The Terminated signal is only sent to registered watchers after this signal has been processed.
                case (context, PostStop) =>
                  context.log.info(s"Worker `$name` received stopped signal.")
                  // Behaviors.stopped is returned by PostStop signal.
                  Behaviors.stopped
                // Before a supervised actor is restarted it is sent the PreRestart signal giving it a chance to clean up resources it has created.
                // Lifecycle signal that is fired upon restart of the Actor before replacing the behavior with the fresh one.
                case (context, PreRestart) =>
                  context.log.info(s"Worker `$name` received restarted signal.")
                  // Behaviors.same is returned by PreRestart signal.
                  Behaviors.same

                //! ONLY FOR PARENT ACTORS context.watch(<child actor>) + case (context, Terminated(ref)) => {...}, otherwise DeathPactException.
                // 1. context.watch(<child actor>) WITHOUT case (context, Terminated(ref)) => {...} will throw DeathPactException.
                // I.E. If the parent in turn does not handle the Terminated message it will itself fail with an DeathPactException.
                // 2. case (context, Terminated(ref)) => {...} WITHOUT context.watch(<child actor>) will have no meaning (overkill feature).
                // Lifecycle signal that is fired when an Actor that was watched has terminated.
                // Watching is performed by invoking the akka.actor.typed.scaladsl.ActorContext.watch.
                //* case (context, Terminated(_)) =>
                  //* context.log.info("Worker received terminated signal from it's child/children.")
                  // Behavior to be returned (what should happen to current actor) when a child / one of children of the current actor - stopped.
                  //* Behaviors.same
              }
          }
      }
      // 1. SupervisorStrategy.resume - This strategy instructs the actor to ignore the failure and continue processing messages as if nothing happened.
      // 1.1. The failed actor will keep its current state and behavior, and will continue to receive messages as usual.
      // 1.2. This strategy is appropriate when the failure is not severe, and the actor is able to recover by itself.
      // 2. SupervisorStrategy.restart - This strategy instructs the actor to terminate itself and create a new instance with a fresh state and behavior.
      // 2.1. The new instance will be initialized with any necessary information by the actor itself before it starts processing messages.
      // 2.2. This strategy is appropriate when the failure is severe, but the actor is able to recover by resetting its state and starting over.
      // 3. SupervisorStrategy.stop - This strategy instructs the actor to terminate itself and stop processing messages.
      // 3.1. The failed actor will not be restarted, and any state it had will be lost.
      // 3.1. This strategy is appropriate when the failure is severe and cannot be safely ignored, or when the actor is unable to recover by itself.

      // By default all child actors are stopped when the parent actor is stopped.
      // But when .setup is inside .supervise, the .setup code block will be run once again when parent is restarted, therefore new child actors will be spawned.
      .onFailure(SupervisorStrategy.restart)
}
