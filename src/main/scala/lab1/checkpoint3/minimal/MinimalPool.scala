package org.marius
package lab1.checkpoint3.minimal

import lab1.checkpoint3.minimal.ActorManager.ActorManagerStopped

import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorSystem, Behavior}

object MinimalPool {
  // apply - command to start the actor
  // Behavior[Any] is the return type of the apply method
  // Behaviors.setup is a factory for creating a behavior
  def apply(): Behavior[Any] = Behaviors.setup { context =>
    // Behaviors.receiveMessage is a factory for creating a behavior that handles a single message
    Behaviors.receiveMessage {
      // handle any received message
      _ => {
        // context.spawn is a method that creates a child actor and returns its ActorRef
        val messagePrinter = context.spawn(MessagePrinter(), "message-printer")
        // send a message to the actor
        messagePrinter ! "Hello, world!"

        // context.spawn is a method that creates a child actor and returns its ActorRef
        val messageParser = context.spawn(MessageParser(), "message-parser")
        // send different messages to the actor (the actor will only handle Int and String messages)
        messageParser ! 10
        messageParser ! "Hello"
        messageParser ! Map {
          10 -> "Hello"
        }

        // context.spawn is a method that creates a child actor and returns its ActorRef
        val averageCounter = context.spawn(AverageCounter(), "average-counter")
        // send 3 messages to the actor
        averageCounter ! 10
        averageCounter ! 10
        averageCounter ! 10

        // context.spawn is a method that creates a child actor and returns its ActorRef
        val actorManager = context.spawn(ActorManager(), "actor-manager")
        // send a command to the actor 
        actorManager ! ActorManagerStopped

        // stop the actor
        Behaviors.stopped
      }
    }
  }
}
