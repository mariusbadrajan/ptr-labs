package org.marius
package lab1.checkpoint4.bonus

import lab1.checkpoint4.bonus.Respondent.{Kill, SendMessage}

import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{Behavior, SupervisorStrategy, Terminated}

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}
import scala.util.Random

object Examiner {
  private val minNrOfMessages = 5
  private var previousAnswer = true
  private var passed = 0
  private var failed = 0
  private var inARow = 0
  private val waitQuestion = 700
  val waitResponse = 300

  sealed trait ExaminerCommand

  case object Fail extends ExaminerCommand

  case object Success extends ExaminerCommand

  case object StartExamination extends ExaminerCommand

  def apply(maxNrOfMessages: Int): Behavior[ExaminerCommand] =
    Behaviors
      .supervise[ExaminerCommand] {
        Behaviors
          .setup[ExaminerCommand] { context =>
            val totalNrOfMessages = Random.nextInt((maxNrOfMessages - minNrOfMessages) + 1) + minNrOfMessages
            val consecutive = 40 * totalNrOfMessages / 100
            var remainingAttempts = totalNrOfMessages

            context.log.info(s"Total questions: $totalNrOfMessages.")

            val respondent = context.spawn(Respondent(), "respondent")
            context.watch(respondent)

            def SendQuestion(): Behavior[ExaminerCommand] = {
              Thread.sleep(waitQuestion)
              context.log.info("Question asked ...")
              respondent ! SendMessage(context.self)
              Behaviors.same
            }

            def LuckyOne(): Behavior[ExaminerCommand] = {
              context.log.error("Also lucky one, but you dead ;)")
              respondent ! Kill
              Behaviors.same
            }

            Behaviors
              .receiveMessagePartial[ExaminerCommand] {
                case StartExamination => SendQuestion()
                case Success =>
                  Thread.sleep(waitResponse)
                  context.log.info(s"${totalNrOfMessages - remainingAttempts + 1}. Correct answer.")
                  passed += 1
                  remainingAttempts -= 1
                  if (!previousAnswer) {
                    previousAnswer = true
                    inARow = 0
                  }
                  inARow += 1
                  if ((remainingAttempts == 0 && passed == totalNrOfMessages) || inARow > consecutive) {
                    context.log.error("Lucky one. Pif paf ;)")
                    respondent ! Kill
                    Behaviors.same
                  }
                  else if (remainingAttempts == 0) {
                    LuckyOne()
                  }
                  else {
                    SendQuestion()
                  }
                case Fail =>
                  Thread.sleep(waitResponse)
                  context.log.warn(s"${totalNrOfMessages - remainingAttempts + 1}. Wrong answer.")
                  failed += 1
                  remainingAttempts -= 1
                  if (previousAnswer) {
                    previousAnswer = false
                    inARow = 0
                  }
                  if (failed * 100 / totalNrOfMessages >= 50) {
                    context.log.error("Too many wrong answers. You dead ;)")
                    respondent ! Kill
                    Behaviors.same
                  }
                  else if (remainingAttempts == 0) {
                    LuckyOne()
                  }
                  else {
                    SendQuestion()
                  }
              }
              .receiveSignal {
                case (_, Terminated(_)) =>
                  context.log.info(s"Wrong: $failed;")
                  context.log.info(s"Passed: $passed;")
                  context.log.info(s"In a row: $inARow;")
                  Behaviors.stopped
              }
          }
      }.onFailure[Exception](SupervisorStrategy.restart)
}
