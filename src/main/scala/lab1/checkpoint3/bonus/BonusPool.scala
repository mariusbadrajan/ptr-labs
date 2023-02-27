package org.marius
package lab1.checkpoint3.bonus

import lab1.checkpoint3.bonus.RiskyScheduler.ExecuteTask

import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorSystem, Behavior}

object BonusPool {
  // apply - command to start the actor
  // Behavior[Any] is the return type of the apply method
  // Behaviors.setup is a factory for creating a behavior
  def apply(): Behavior[Any] = Behaviors.setup { * =>
    // Behaviors.receiveMessage is a factory for creating a behavior that handles a single message
    Behaviors.receiveMessage {
      // handle any received message
      _ => {
        // create a new risky scheduler
        val scheduler = ActorSystem(RiskyScheduler(), "scheduler")
        // send 3 tasks to the scheduler and wait 500 ms between each task
        scheduler ! ExecuteTask("Hello")
        Thread.sleep(500)
        scheduler ! ExecuteTask("How are you")
        Thread.sleep(500)
        scheduler ! ExecuteTask("Risky business")

        // stop the actor
        Behaviors.stopped
      }
    }
  }
}
