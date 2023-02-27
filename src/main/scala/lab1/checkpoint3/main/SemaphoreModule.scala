package org.marius
package lab1.checkpoint3.main

import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorRef, ActorSystem, Behavior}

object SemaphoreModule {
  // actor commands, which are the only messages an actor can receive
  sealed trait Command

  // command to acquire a resource (decrement the counter of semaphore)
  case class Acquire() extends Command
  // command to release a resource (increment the counter of semaphore)
  case class Release() extends Command

  // maxValue - predefined max value of allowed resources to enter the critical section
  private val maxValue = 10

  // apply - command to start the actor
  // Behavior[Command] is the return type of the apply method
  // Behaviors.setup is a factory for creating a behavior
  private def apply(initialCount: Int, maxCount: Int = maxValue): Behavior[Command] = Behaviors.setup { context =>
    // Behaviors.receiveMessage is a factory for creating a behavior that handles a single message
    Behaviors.receiveMessage[Command] {
      // handle the Acquire command
      case Acquire() =>
        // check if there are available resources
        if (initialCount > 0) {
          // decrement the counter of semaphore
          val newValue = initialCount - 1
          // log that the resource was acquired
          context.log.info(s"Acquired. Remaining $newValue.")
          // return the new behavior
          SemaphoreModule(newValue, maxCount)
        // if there are no available resources, return the same behavior        
        } else {
          Behaviors.same
        }
      // handle the Release command
      case Release() =>
        // check if it is possible to release a resource
        if (initialCount < maxCount) {
          // increment the counter of semaphore
          val newValue = initialCount + 1
          // log that the resource was released
          context.log.info(s"Released. Remaining $newValue.")
          // return the new behavior
          SemaphoreModule(newValue, maxCount)
        // if it is not possible to release a resource, return the same behavior
        } else {
          Behaviors.same
        }
    }
  }

  // create a new semaphore
  def createSemaphore(initialCount: Int, maxCount: Int = maxValue): ActorRef[Command] = {
    // return the actor reference
    ActorSystem(SemaphoreModule(initialCount, maxCount), "semaphore-module")
  }

  // acquire a resource
  def acquire(semaphoreRef: ActorRef[Command]): Unit = {
    semaphoreRef ! Acquire()
  }

  // release a resource
  def release(semaphoreRef: ActorRef[Command]): Unit = {
    semaphoreRef ! Release()
  }
}
