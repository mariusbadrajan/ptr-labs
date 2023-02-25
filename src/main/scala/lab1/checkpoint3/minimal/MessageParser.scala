package org.marius
package lab1.checkpoint3.minimal

import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorRef, Behavior}

object MessageParser {
  // apply - command to start the actor
  // Behavior[T] is the return type of the apply method
  // Behaviors.receive is a factory for creating a behavior from a function that takes a message and a context
  def apply[T](): Behavior[T] = Behaviors.receive { (context, message) =>
    // message match is a pattern matching expression
    message match {
      // handle the message of type String
      case message: String =>
        // convert the message to lower case
        val lowerCaseMessage = message.toLowerCase()
        // log the message
        context.log.info(s"Received: $lowerCaseMessage")
        // return the same behavior
        Behaviors.same
      // handle the message of type Int
      case message: Int =>
        // increment the value of the message
        val incrementedMessage = message + 1;
        // log the message
        context.log.info(s"Received: $incrementedMessage")
        // return the same behavior
        Behaviors.same
      // handle all other messages
      case _: T =>
        // log that the message is not handled
        context.log.info(s"Received: I don`t know how to HANDLE this!")
        // return the same behavior
        Behaviors.same
    }
  }
}
