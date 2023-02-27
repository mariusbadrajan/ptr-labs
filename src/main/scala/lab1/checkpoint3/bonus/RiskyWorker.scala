package org.marius
package lab1.checkpoint3.bonus

import lab1.checkpoint3.bonus.RiskyScheduler.{SchedulerCommand, TaskResult}

import akka.actor.typed.*
import akka.actor.typed.scaladsl.Behaviors

import scala.util.Random

object RiskyWorker {
  // actor commands, which are the only messages an actor can receive
  sealed trait WorkerCommand

  // command to perform a task
  case class PerformTask(task: String, replyTo: ActorRef[SchedulerCommand]) extends WorkerCommand

  // command to crash the worker
  case object Crash extends WorkerCommand

  // apply - command to start the actor
  // Behavior[WorkerCommand] is the return type of the apply method
  // Behaviors.receive is a factory for creating a behavior from a function that takes a message and a context
  def apply(): Behavior[WorkerCommand] = Behaviors.receive { (context, message) =>
    // message match is a pattern matching expression
    message match {
      // handle the message of type PerformTask, input from scheduler
      case PerformTask(task, replyTo) =>
        // if Random.nextBoolean() returns true, send the result (`Miau`) to the scheduler
        if (Random.nextBoolean()) {
          // send the result to the scheduler
          replyTo ! TaskResult("Miau")
        // if Random.nextBoolean() returns false, crash itself
        } else {
          // log the failure
          context.log.info(s"Task failed")
          // send the crash message to itself (context.self)
          context.self ! Crash
        }
        // return the same behavior
        Behaviors.same
      // handle the message of type Crash, input from worker (itself)
      case Crash =>
        // log the crash
        context.log.info(s"Worker crashed")
        // return the stopped behavior
        Behaviors.stopped
      // handle all other messages
      case _ =>
        Behaviors.unhandled
    }
  }
}
