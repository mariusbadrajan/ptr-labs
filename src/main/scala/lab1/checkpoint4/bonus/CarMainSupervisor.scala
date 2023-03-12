package org.marius
package lab1.checkpoint4.bonus

import akka.actor.typed.*
import akka.actor.typed.scaladsl.Behaviors

object CarMainSupervisor {
  val maxAllowedFailures = 5

  sealed trait Command

  case object TurnIgnitionOn extends Command

  case class RestartSensor(sensor: ActorRef[Command]) extends Command

  case class PerformMeasurement(parent: ActorRef[Command]) extends Command

  def apply(): Behavior[Command] =
    Behaviors
      .supervise[Command] {
        Behaviors
          .setup[Command] { context =>
            var currentFailures = 0
            // Spawn sensors.
            val cabinSensor = context.spawn(CarCabinSensor(), "cabin-sensor")
            val motorSensor = context.spawn(CarMotorSensor(), "motor-sensor")
            val chassisSensor = context.spawn(CarChassisSensor(), "chassis-sensor")
            val wheelSupervisor = context.spawn(CarWheelSupervisor(), "wheel-supervisor")

            // Add sensors to a list.
            val sensors: List[ActorRef[Command]] = List(cabinSensor, motorSensor, chassisSensor, wheelSupervisor)

            Behaviors
              .receiveMessagePartial[Command] {
                // Start the wheel supervisor, cabin sensor, motor sensor and chassis sensor.
                case TurnIgnitionOn =>
                  // sensors.foreach(_ ! PerformMeasurement(context.self))
                  // Same as above.
                  // Iterate over the list of sensors, watch each one of them and send them a `PerformMeasurement` message.
                  sensors.foreach(sensor => {
                    context.watch(sensor)
                    sensor ! PerformMeasurement(context.self)
                  })
                  Behaviors.same
                // Restart a sensor if current failures are less than the maximum allowed failures,
                // otherwise stop the actor system.
                case RestartSensor(sensor) =>
                  context.log.info(s"An error occurred due to invalid measurements and `${sensor.path.name}` died.")
                  currentFailures += 1
                  if (currentFailures >= maxAllowedFailures) {
                    context.log.info("From main supervisor: AirBags deployed!")
                    Thread.sleep(3000)
                    // Failures received from cabin sensor, motor sensor and chassis sensor.
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
                case (_, PostStop) =>
                  Behaviors.stopped
                case (context, Terminated(ref)) =>
                  context.log.info("From wheel supervisor: AirBags deployed!")
                  Thread.sleep(3000)
                  // Signal received from wheel supervisor.
                  Behaviors.stopped
              }
          }
      }.onFailure(SupervisorStrategy.restart)
}
