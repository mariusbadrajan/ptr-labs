package org.marius
package lab1.checkpoint3.minimal

import lab1.checkpoint3.minimal.Actor
import lab1.checkpoint3.minimal.Actor.ActorStop

import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorRef, Behavior, PostStop, Terminated}

object ActorManager {
  // actor commands, which are the only messages an actor can receive
  sealed trait ActorManagerCommand

  // command to stop the inner actor
  case object ActorManagerStopped extends ActorManagerCommand

  // apply - command to start the actor
  // Behavior[ActorManagerCommand] is the return type of the apply method
  // Behaviors.setup is a factory for creating a behavior
  def apply[ActorManagerCommand](): Behavior[ActorManagerCommand] = Behaviors.setup { context =>
    // log that the actor has started
    context.log.info(s"Actor manager started.")

    // create the inner actor
    // context.spawn is a factory for creating a child actor and returns its ActorRef
    val child = context.spawn(Actor(), "Actor")
    // context.watch is a factory for creating a watch on the child actor
    context.watch(child)

    Behaviors
      // Behaviors.receiveMessage is a factory for creating a behavior that handles a single message
      .receiveMessage[ActorManagerCommand] {
        // handle the Stop command
        ActorManagerStopped =>
          // send the Stop command to the child actor
          child ! ActorStop
          // return the same behavior
          Behaviors.same
      }
      // Behaviors.receiveSignal is a factory for creating a behavior that handles a single signal
      .receiveSignal {
        // handle the Terminated signal, which is sent when the child actor is stopped
        case (_, Terminated(value)) =>
          // log the name of the terminated actor
          context.log.info(s"Actor \"${value.path.name}\" was terminated.")
          // stop the actor
          Behaviors.stopped
      }
  }
}
