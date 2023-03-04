package org.marius
package lab1.checkpoint4.main

import lab1.checkpoint4.minimal.Supervisor.Command

import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorRef, Behavior, SupervisorStrategy, Terminated}

import scala.concurrent.duration.*

object Supervisor {
  sealed trait Command
  case class CleanString(message: String) extends Command
  case object SignalFromChild extends Command
  sealed trait Message
  case class Split(message: String, replyTo: ActorRef[Command]) extends Message
  case class LowerCaseAndSwap(message: List[String], replyTo: ActorRef[Command]) extends Message
  case class Join(message: List[String], replyTo: ActorRef[Command]) extends Message

  def apply(): Behavior[Command] =
    Behaviors
      .setup[Command] { context =>
        var currentMessage = ""

        // Spawn 3 workers for the 3 steps of the process and give as parameter the next worker in the chain.
        val thirdWorker = context.spawn(ThirdWorker(), s"ThirdWorker")
        val secondWorker = context.spawn(SecondWorker(thirdWorker), s"SecondWorker")
        val firstWorker = context.spawn(FirstWorker(secondWorker), s"FirstWorker")

        // Register the workers as monitors for the supervisor.
        context.watch(firstWorker)
        context.watch(secondWorker)
        context.watch(thirdWorker)

        Behaviors
          .supervise[Command] {
            Behaviors
              .receiveMessagePartial[Command] {
                // Inialized the process by sending the first message to the first worker.
                case CleanString(message) =>
                  currentMessage = message
                  firstWorker ! Split(message, context.self)
                  Behaviors.same
                // Restart the process if a problem occurs.
                case SignalFromChild =>
                  context.log.info("A problem occurred while processing the string. Restarting the process...")
                  Thread.sleep(1000)
                  firstWorker ! Split(currentMessage, context.self)
                  Behaviors.same
              }
          }.onFailure[Exception](SupervisorStrategy.restart)
      }
}
