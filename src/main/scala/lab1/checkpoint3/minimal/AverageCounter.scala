package org.marius
package lab1.checkpoint3.minimal

import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorRef, Behavior}

import scala.collection.mutable.ListBuffer

object AverageCounter {
  // current average - the average of all the messages received so far
  private var currentAverage: Double = 0

  // apply - command to start the actor
  // Behavior[Double] is the return type of the apply method
  // Behaviors.setup is a factory for creating a behavior
  def apply(): Behavior[Double] = Behaviors.setup { context =>
    // log the initial current average
    context.log.info(s"Current average is $currentAverage")

    // Behaviors.receiveMessage is a factory for creating a behavior that handles a single message
    Behaviors.receiveMessage {
      // handle the message of type Double
      case message: Double =>
        // update the current average
        currentAverage = (currentAverage + message) / 2
        // log the current average
        context.log.info(s"Current average is $currentAverage")
        // return the same behavior
        Behaviors.same
      // handle all other messages
      case _ => Behaviors.unhandled
    }
  }
}
