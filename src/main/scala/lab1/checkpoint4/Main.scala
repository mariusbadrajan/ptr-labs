package org.marius
package lab1.checkpoint4

import lab1.checkpoint4.minimal.Supervisor.{ForwardToAllWorkers, ForwardToWorker, Kill as KillSupervisor}
import lab1.checkpoint4.minimal.Worker.{EchoMessage, Kill as KillChild}
import lab1.checkpoint4.minimal.{Worker, Supervisor as MinSupervisor}

import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorSystem, Behavior}

object Main extends App {
  // Minimal task
  // ActorSystem is a factory for creating and running top-level actors
  private val minimalSystem = ActorSystem(MinSupervisor(3), "minimal-pool")
  // send different types of messages to the actor
  minimalSystem ! ForwardToAllWorkers(EchoMessage("Hello World!"))
  Thread.sleep(1000)
  minimalSystem ! ForwardToWorker("worker-2", EchoMessage("Hello worker 2!"))
  Thread.sleep(1000)
  minimalSystem ! ForwardToWorker("worker-10", EchoMessage("Hello worker 10!"))
  Thread.sleep(1000)
  minimalSystem ! ForwardToWorker("worker-3", KillChild)
  Thread.sleep(1000)
  minimalSystem ! ForwardToAllWorkers(EchoMessage("Hello again!"))
  Thread.sleep(1000)
  minimalSystem ! KillSupervisor
  Thread.sleep(1000)
  minimalSystem ! ForwardToAllWorkers(EchoMessage("Hello again!"))
  // wait until all actors have executed their termination logic
  Thread.sleep(1000)
}
