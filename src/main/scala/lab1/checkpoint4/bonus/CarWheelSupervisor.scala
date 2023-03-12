package org.marius
package lab1.checkpoint4.bonus

import lab1.checkpoint4.bonus.CarMainSupervisor.*

import akka.actor.typed.*
import akka.actor.typed.scaladsl.Behaviors

import scala.collection.mutable.ArrayBuffer

object CarWheelSupervisor {
  private val maxAllowedFailures = CarMainSupervisor.maxAllowedFailures

  def apply(): Behavior[Command] =
    Behaviors
      .supervise[Command] {
        Behaviors
          .setup[Command] { context =>
            var currentFailures = 0
            // Create an array buffer, spawn sensors, add them to the array buffer and watch them.
            val sensors: ArrayBuffer[ActorRef[Command]] = ArrayBuffer[ActorRef[Command]]()
            for (i <- 1 to 4 by 1) {
              val wheelSensor = context.spawn(CarWheelSensor(), s"wheel-sensor-$i")
              sensors.addOne(wheelSensor)
              context.watch(wheelSensor)
            }

            Behaviors
              .receiveMessagePartial[Command] {
                // Start the wheel sensors.
                case PerformMeasurement(replyTo) =>
                  sensors.foreach(sensor => {
                    sensor ! PerformMeasurement(context.self)
                  })
                  Behaviors.same
                // Restart a sensor if current failures are less than the maximum allowed failures,
                // otherwise stop the actor system.
                case RestartSensor(sensor) =>
                  context.log.info(s"An error occurred due to invalid measurements and `${sensor.path.name}` died.")
                  currentFailures += 1
                  if (currentFailures >= maxAllowedFailures) {
                    // Failures received from wheel sensors.
                    Behaviors.stopped
                  }
                  else {
                    context.log.info(s"Restarting sensor `${sensor.path.name}`.")
                    Thread.sleep(2000)
                    sensor ! PerformMeasurement(context.self)
                    Behaviors.same
                  }
              }
              .receiveSignal {
                case (_, Terminated(ref)) =>
                  Behaviors.same
              }
          }
      }.onFailure(SupervisorStrategy.restart)
}
