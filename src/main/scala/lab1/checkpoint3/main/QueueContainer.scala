package org.marius
package lab1.checkpoint3.main

import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorRef, Behavior}

import scala.collection.mutable.ArrayBuffer

object QueueContainer {
  // actor commands, which are the only messages an actor can receive
  sealed trait Command

  // command to enqueue an item
  case class Enqueue(item: Int, replyTo: ActorRef[QueueStatus]) extends Command
  // command to dequeue an item
  case class Dequeue(replyTo: ActorRef[QueueStatus]) extends Command

  // actor commands, which are the only messages an actor can receive
  sealed trait QueueStatus

  // command to show the status of the queue
  case class Status(message: Any) extends QueueStatus

  // apply - command to start the actor
  // Behavior[Command] is the return type of the apply method
  // Behaviors.receiveMessage is a factory for creating a behavior that handles a single message
  def apply(queue: ArrayBuffer[Int]): Behavior[Command] = Behaviors.receiveMessage[Command] {
    // handle the Enqueue command
    case Enqueue(item, replyTo) =>
      // add the item to the queue
      queue.addOne(item)
      // notify queueManager that the item was added with success
      replyTo ! Status("Ok.")
      // return the reference to the new queue actor
      QueueContainer(queue)
    // handle the Dequeue command
    case Dequeue(replyTo) =>
      // check if the queue is empty
      if (queue.isEmpty) {
        // notify queueManager that the queue is empty
        replyTo ! Status("Queue is empty.")
      // if the queue is not empty
      } else {
        // remove the first item from the queue
        val removedValue = queue(0)
        // remove the first item from the queue
        queue.remove(0)
        // notify queueManager that the item was removed with the value
        replyTo ! Status(removedValue)
      }
      // return the reference to the new queue actor
      QueueContainer(queue)
  }
}
