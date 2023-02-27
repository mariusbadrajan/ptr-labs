package org.marius
package lab1.checkpoint3.main

import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorRef, ActorSystem, Behavior}

import scala.collection.mutable.ArrayBuffer

object QueueManager {
  // queueManager - actor that receives messages from the queue actors
  private val queueManager: ActorRef[QueueContainer.QueueStatus] = ActorSystem(QueueManager(), "queue-manager")
  
  // apply - command to start the actor
  // Behavior[QueueContainer.QueueStatus] is the return type of the apply method
  // Behaviors.setup is a factory for creating a behavior
  private def apply(): Behavior[QueueContainer.QueueStatus] = Behaviors.setup { context =>
    // Behaviors.receiveMessage is a factory for creating a behavior that handles a single message
    Behaviors.receiveMessage[QueueContainer.QueueStatus] {
      // handle the QueueStatus message
      case QueueContainer.Status(message) =>
        // log the message
        context.log.info(s"$message")
        // continue processing messages
        Behaviors.same
    }
  }

  // create a new queue
  def newQueue(): ActorRef[QueueContainer.Command] = {
    // return the reference to the queue actor
    ActorSystem(QueueContainer(new ArrayBuffer[Int]()), "queue-container")
  }

  // push an item to the queue
  def push(queueRef: ActorRef[QueueContainer.Command], item: Int): Unit = {
    queueRef ! QueueContainer.Enqueue(item, queueManager)
  }

  // pop an item from the queue
  def pop(queueRef: ActorRef[QueueContainer.Command]): Unit = {
    queueRef ! QueueContainer.Dequeue(queueManager)
  }
}
