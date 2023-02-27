package org.marius
package lab1.checkpoint3.bonus

import lab1.checkpoint3.bonus.RiskyWorker.PerformTask

import akka.actor.typed.*
import akka.actor.typed.scaladsl.Behaviors

import scala.util.Random

object RiskyScheduler {
  // actor commands, which are the only messages an actor can receive
  sealed trait SchedulerCommand

  // command to execute a task
  case class ExecuteTask(task: String) extends SchedulerCommand

  // command to show the result of a task
  case class TaskResult(result: String) extends SchedulerCommand

  // apply - command to start the actor
  // Behavior[SchedulerCommand] is the return type of the apply method
  // Behaviors.receive is a factory for creating a behavior from a function that takes a message and a context
  def apply(): Behavior[SchedulerCommand] = Behaviors.receive { (context, message) =>
    // message match is a pattern matching expression
    message match {
      // handle the message of type ExecuteTask, input from user
      case ExecuteTask(task) =>
        // create a new risky worker with a random name
        val worker = context.spawn(RiskyWorker(), s"worker-${Random.nextInt(1000)}")
        // send the task to the worker with the current actor as the sender (context.self)
        worker ! PerformTask(task, context.self)
        // return the same behavior
        Behaviors.same
      // handle the message of type TaskResult, output from worker
      case TaskResult(result) =>
        // log the result
        context.log.info(s"Task successful: $result")
        // return the same behavior
        Behaviors.same
      // handle all other messages
      case _ =>
        Behaviors.unhandled
    }
  }
}
