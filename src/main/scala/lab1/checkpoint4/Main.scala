package org.marius
package lab1.checkpoint4

import lab1.checkpoint4.bonus.CarMainSupervisor.TurnIgnitionOn
import lab1.checkpoint4.bonus.Examiner.StartExamination
import lab1.checkpoint4.bonus.{CarMainSupervisor, Examiner}
import lab1.checkpoint4.main.Supervisor as MainSupervisor
import lab1.checkpoint4.main.Supervisor.CleanString
import lab1.checkpoint4.minimal.Supervisor.{ForwardToAllWorkers, ForwardToWorker, Kill as KillSupervisor}
import lab1.checkpoint4.minimal.Worker.{EchoMessage, Kill as KillChild}
import lab1.checkpoint4.minimal.{Worker, Supervisor as MinSupervisor}

import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorSystem, Behavior}

object Main extends App {
  // Minimal task
  // ActorSystem is a factory for creating and running top-level actors.
  private val minimalSystem = ActorSystem(MinSupervisor(3), "minimal-pool")
  // Send different types of messages to the actor.
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
  // Wait until all actors have executed their termination logic.
  Thread.sleep(1000)

  // Main task
  // Can be implemented using two approaches:
  // 1. Passing parent reference to the child actors as constructor parameter or part of the message.
  //TODO 2. Using Receptionist (https://doc.akka.io/docs/akka/current/typed/actor-discovery.html).
  // ActorSystem is a factory for creating and running top-level actors.
  private val mainSystem = ActorSystem(MainSupervisor(), "main-pool")
  // Send a message to the actor.
  mainSystem ! CleanString("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Phasellus bibendum.")
  // Wait until all actors have executed their termination logic.
  Thread.sleep(1000)

  // Bonus task
  // ActorSystem is a factory for creating and running top-level actors.
  private val bonusSystem1 = ActorSystem(Examiner(15), "bonus-1-pool")
  // Send a message to the actor.
  bonusSystem1 ! StartExamination

  //TODO: Better approach: send a message to all actors to stop processing measurements, when max nr of allowed failures is reached,
  // because SOMETIMES when a supervisor dies, A/SOME child actor/s will be terminated only when it/they will complete processing current message and
  // it may happen that child actor/s will try to send a message to a DEAD supervisor and "dead letters" error will be thrown.
  // ActorSystem is a factory for creating and running top-level actors.
  private val bonusSystem2 = ActorSystem(CarMainSupervisor(), "bonus-2-pool")
  // Send a message to the actor.
  bonusSystem2 ! TurnIgnitionOn
}
