package org.marius
package lab1.checkpoint4.bonus

import lab1.checkpoint4.bonus.CarMainSupervisor.{Command, PerformMeasurement, RestartSensor}

import akka.actor.typed.*
import akka.actor.typed.scaladsl.Behaviors

import scala.util.Random
import scala.util.control.Breaks.break

object CarMotorSensor {
  def apply(): Behavior[Command] =
    Behaviors
      .supervise[Command] {
        Behaviors
          .setup[Command] { context =>
            val actorName = context.self.path.name
            var parent: ActorRef[Command] = null

            // Method to randomly take measurements / throw an exception.
            def takeMeasurements(): Unit = {
              Thread.sleep(3000)
              if (Random.nextBoolean()) {
                context.log.info(s"The `$actorName` has taken measurements successfully.")
                takeMeasurements()
              }
              else {
                // Comment this line and uncomment the next one (throw new ...) to send a PreRestart signal and restart the actor.
                parent ! RestartSensor(context.self)
                // throw new Exception("An error occurred.")
              }
            }

            Behaviors
              .receiveMessagePartial[Command] {
                // Start taking measurements.
                case PerformMeasurement(replyTo) =>
                  parent = replyTo
                  takeMeasurements()
                  Behaviors.same
              }
              .receiveSignal {
                case (context, PreRestart) =>
                  parent ! RestartSensor(context.self)
                  Behaviors.same
              }
          }
      }.onFailure(SupervisorStrategy.restart)
}
