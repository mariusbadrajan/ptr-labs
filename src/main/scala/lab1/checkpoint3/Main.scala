package org.marius
package lab1.checkpoint3

import lab1.checkpoint3.bonus.BonusPool
import lab1.checkpoint3.main.{MainPool, QueueManager}
import lab1.checkpoint3.minimal.*

import akka.actor.typed.ActorSystem

object Main extends App {
  // Minimal task
  // ActorSystem is a factory for creating and running top-level actors
  private val minimalSystem = ActorSystem(MinimalPool(), "minimal-pool")
  // send a message to the actor
  minimalSystem ! "Start"
  // wait until all actors have executed their termination logic
  Thread.sleep(1000)
  // wait for the actor to terminate
  // minimalSystem.terminate()

  // Main task
  // ActorSystem is a factory for creating and running top-level actors
  private val mainSystem = ActorSystem(MainPool(), "main-pool")
  // send a message to the actor
  mainSystem ! "Start"
  // wait until all actors have executed their termination logic
  Thread.sleep(1000)

  // Bonus task
  // ActorSystem is a factory for creating and running top-level actors
  private val bonusSystem = ActorSystem(BonusPool(), "bonus-pool")
  // send a message to the actor
  bonusSystem ! "Start"
}
