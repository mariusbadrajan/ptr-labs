package org.marius
package lab2.checkpoint1

import Pool.Start

import akka.actor.typed.ActorSystem

object Main extends App {
  // Minimal, main, bonus tasks
  // ActorSystem is a factory for creating and running top-level actors.
  private val system = ActorSystem(Pool(), "pool")
  // Send a message to the actor.
  system ! Start
}
