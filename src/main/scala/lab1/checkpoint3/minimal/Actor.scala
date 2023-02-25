package org.marius
package lab1.checkpoint3.minimal

import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorRef, Behavior}

object Actor {
  // actor commands, which are the only messages an actor can receive
  sealed trait ActorCommand

  // command to stop the actor
  case object ActorStop extends ActorCommand

  // apply - command to start the actor
  // Behavior[ActorCommand] is the return type of the apply method
  // Behaviors.setup is a factory for creating a behavior
  def apply(): Behavior[ActorCommand] = Behaviors.setup { context =>
    // log that the actor has started
    context.log.info(s"Actor started.")

    // Behaviors.receiveMessage is a factory for creating a behavior that handles a single message
    Behaviors.receiveMessage[ActorCommand] {
      // handle the Stop command
      ActorStop =>
        // log that the actor is stopping
        context.log.info("Actor is stopping...")
        // stop the actor
        Behaviors.stopped
    }
  }
}
