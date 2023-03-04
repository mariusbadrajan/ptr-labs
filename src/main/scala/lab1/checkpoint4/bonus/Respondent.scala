package org.marius
package lab1.checkpoint4.bonus

import lab1.checkpoint4.bonus.Examiner.{ExaminerCommand, Fail, Success}

import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorRef, Behavior, SupervisorStrategy}

import scala.util.Random

object Respondent {
  sealed trait RespondentCommand
  case class SendMessage(replyTo: ActorRef[ExaminerCommand]) extends RespondentCommand
  case object Kill extends RespondentCommand

  def apply(): Behavior[RespondentCommand] =
    Behaviors
      .setup[RespondentCommand] { context =>
        Behaviors
          .supervise[RespondentCommand] {
            Behaviors
              .receiveMessagePartial[RespondentCommand] {
                // Respond randomly to the question.
                case SendMessage(replyTo) =>
                  Thread.sleep(Examiner.waitResponse)
                  context.log.info("Answered ;|")
                  if (Random.nextBoolean()) {
                    replyTo ! Success
                  }
                  else {
                    replyTo ! Fail
                  }
                  Behaviors.same
                // Kill the respondent.
                case Kill =>
                  context.log.error("Respondent killed.")
                  Behaviors.stopped
              }
          }.onFailure[Exception](SupervisorStrategy.stop)
      }
}
