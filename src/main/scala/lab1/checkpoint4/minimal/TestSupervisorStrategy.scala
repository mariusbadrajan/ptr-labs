package org.marius
package lab1.checkpoint4.minimal

import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorSystem, Behavior, PostStop, SupervisorStrategy}

object Child {
  def apply(): Behavior[Any] =
    Behaviors
      .receiveMessage[Any] {
        case message: String =>
          println(s"Child received message `$message`.")
          Behaviors.same
      }
      .receiveSignal {
        case (_, PostStop) =>
          println("Child received stop signal.")
          Behaviors.stopped
      }
}

object Parent {
  def apply(): Behavior[Any] =
    Behaviors
      .supervise[Any] {
        Behaviors
          .setup[Any] { context =>
            val child = context.spawn(Child(), "child")

            Behaviors
              .receiveMessage[Any] {
                case message: String =>
                  child ! message
                  Behaviors.same
                case message: Int =>
                  println(s"Parent threw an exception due to incoming message `$message`.")
                  throw new Exception("Parent threw an exception.")
              }
          }
      //! WILL NOT WORK with .withStopChildren(stop) when .setup is inside .supervise.
      // It will throw continuously `Supervisor RestartSupervisor saw failure: actor name [child] is not unique.`,
      // as when the Parent actor fails and is restarted, the `Child` actor is not stopped,
      // because we override the default behavior of the child actor to be stopped when parent is stopped with - .withStopChildren(false),
      // therefore every time a new `Child` actor with the same name as the old one is tried to be created.
      // This violates the rule that actor names must be unique within an actor system, which leads to the exception above.

      //! BROKEN FLOW
      //* }.onFailure(SupervisorStrategy.restart.withStopChildren(false))
      // NORMAL FLOW
      }.onFailure(SupervisorStrategy.restart)
}

object Main extends App {
  private val testSupervisorStrategySystem = ActorSystem(Parent(), "parent")
  testSupervisorStrategySystem ! "message to child"
  Thread.sleep(10000)
  testSupervisorStrategySystem ! 1
  Thread.sleep(10000)
  testSupervisorStrategySystem ! "new message to child"
}

//* BROKEN FLOW
// Child -> Child received message `message to child`.
// Parent -> Parent threw an exception due to incoming message `1`.
// The endless loop of creating a new child with the same name as previous one which was not stopped

//* NORMAL FLOW
// Child  -> Child received message `message to child`.
// Parent -> Parent threw an exception due to incoming message `1`.
// Child  -> Child received stop signal.
// Child  -> Child received message `new message to child`.
