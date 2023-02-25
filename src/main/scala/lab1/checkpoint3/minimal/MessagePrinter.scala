package org.marius
package lab1.checkpoint3.minimal

import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorRef, Behavior}

object MessagePrinter {
  // apply - command to start the actor
  // Behavior[T] is the return type of the apply method
  // Behaviors.receive is a factory for creating a behavior from a function that takes a message and a context
  def apply[T](): Behavior[T] = Behaviors.receive { (context, message) =>
    // context.log.info is a method that logs a message
    context.log.info(s"$message")
    // Behaviors.same is a behavior that does not change the current behavior
    Behaviors.same
  }
}
