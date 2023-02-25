package org.marius
package lab1.checkpoint3

import lab1.checkpoint3.minimal.ActorManager.ActorManagerStopped
import lab1.checkpoint3.minimal.{ActorManager, AverageCounter, MessageParser, MessagePrinter, MinimalPool}

import akka.actor.typed.ActorSystem
import akka.actor.{PoisonPill, typed}

object Main extends App {
  // Minimal task
  // ActorSystem is a factory for creating and running top-level actors
  private val minimalSystem = ActorSystem(MinimalPool(), "minimal-pool")
  // send a message to the actor
  minimalSystem ! "Start"
  // wait for the actor to terminate
  // minimalSystem.terminate()
}
