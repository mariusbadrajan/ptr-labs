package org.marius
package lab1.checkpoint3.main

import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors

object MainPool {
  // apply - command to start the actor
  // Behavior[Any] is the return type of the apply method
  // Behaviors.setup is a factory for creating a behavior
  def apply(): Behavior[Any] = Behaviors.setup { * =>
    // Behaviors.receiveMessage is a factory for creating a behavior that handles a single message
    Behaviors.receiveMessage {
      // handle any received message
      _ => {
        // create a new queue
        val queue = QueueManager.newQueue()
        // push element 42 to the queue
        QueueManager.push(queue, 42)
        // pop an element from the queue
        QueueManager.pop(queue)
        // pop an element from the queue
        QueueManager.pop(queue)

        // create a new semaphore with initial value 1 and max value 2
        val semaphore = SemaphoreModule.createSemaphore(1, 2)
        // acquire the semaphore 2 times
        SemaphoreModule.acquire(semaphore)
        SemaphoreModule.acquire(semaphore)
        // release the semaphore 3 times
        SemaphoreModule.release(semaphore)
        SemaphoreModule.release(semaphore)
        SemaphoreModule.release(semaphore)

        // stop the actor
        Behaviors.stopped
      }
    }
  }
}
