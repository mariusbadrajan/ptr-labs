package org.marius
package lab1.checkpoint4.bonus

import lab1.checkpoint4.bonus.Respondent.{Kill, SendMessage}

import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{Behavior, SupervisorStrategy, Terminated}

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}
import scala.util.Random

object Examiner {
  // Min number of questions to ask.
  private val minNrOfMessages = 5
  // Remember the type (corect or wrong asnwer) of the previous question.
  private var previousAnswer = true
  // Number of correct answers.
  private var passed = 0
  // Number of wrong answers.
  private var failed = 0
  // Number of correct answers in a row.
  private var inARow = 0
  // Time to wait before asking a question.
  private val waitQuestion = 700
  // Time to wait before answering a question.
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
            // Generate a random number of questions to ask within the specified range.
            val totalNrOfMessages = Random.nextInt((maxNrOfMessages - minNrOfMessages) + 1) + minNrOfMessages
            // Number of correct answers in a row to pass the `exam` has to be at least 40% of the total number of questions.
            val consecutive = 40 * totalNrOfMessages / 100
            // Number of remaining questions to ask.
            var remainingAttempts = totalNrOfMessages

            context.log.info(s"Total questions: $totalNrOfMessages.")

            // Create a respondent.
            val respondent = context.spawn(Respondent(), "respondent")
            // Watch the respondent.
            context.watch(respondent)

            // Define method to send a question to the respondent.
            def SendQuestion(): Behavior[ExaminerCommand] = {
              Thread.sleep(waitQuestion)
              context.log.info("Question asked ...")
              respondent ! SendMessage(context.self)
              Behaviors.same
            }

            // Define method to handle the case when the respondent is `lucky`.
            def LuckyOne(): Behavior[ExaminerCommand] = {
              context.log.error("Also lucky one, but you dead ;)")
              respondent ! Kill
              Behaviors.same
            }

            Behaviors
              .receiveMessagePartial[ExaminerCommand] {
                // Start the `exam` by sending the first question.
                case StartExamination => SendQuestion()
                // Handle correct answer from the respondent.
                case Success =>
                  Thread.sleep(waitResponse)
                  context.log.info(s"${totalNrOfMessages - remainingAttempts + 1}. Correct answer.")
                  // Increment the number of correct answers.
                  passed += 1
                  // Decrement the number of remaining questions to ask.
                  remainingAttempts -= 1
                  // If the previous answer was wrong, reset the number of correct answers in a row.
                  if (!previousAnswer) {
                    previousAnswer = true
                    inARow = 0
                  }
                  // Increment the number of correct answers in a row.
                  inARow += 1
                  // If the respondent answered correctly to all questions or 
                  // the number of correct answers in a row is greater than 40% of the total number of questions,
                  // kill the respondent.
                  if ((remainingAttempts == 0 && passed == totalNrOfMessages) || inARow > consecutive) {
                    context.log.error("Lucky one. Pif paf ;)")
                    respondent ! Kill
                    Behaviors.same
                  }
                  // If the are no more questions to ask, kill the respondent.
                  else if (remainingAttempts == 0) {
                    LuckyOne()
                  }
                  // Otherwise, send the next question.
                  else {
                    SendQuestion()
                  }
                // Handle wrong answer from the respondent.
                case Fail =>
                  Thread.sleep(waitResponse)
                  context.log.warn(s"${totalNrOfMessages - remainingAttempts + 1}. Wrong answer.")
                  // Increment the number of wrong answers.
                  failed += 1
                  // Decrement the number of remaining questions to ask.
                  remainingAttempts -= 1
                  // If the previous answer was correct, reset the number of correct answers in a row.
                  if (previousAnswer) {
                    previousAnswer = false
                    inARow = 0
                  }
                  // If the number of wrong answers is greater than 50% of the total number of questions,
                  // kill the respondent.
                  if (failed * 100 / totalNrOfMessages >= 50) {
                    context.log.error("Too many wrong answers. You dead ;)")
                    respondent ! Kill
                    Behaviors.same
                  }
                  // If the are no more questions to ask, kill the respondent.
                  else if (remainingAttempts == 0) {
                    LuckyOne()
                  }
                  // Otherwise, send the next question.
                  else {
                    SendQuestion()
                  }
              }
              .receiveSignal {
                // Handle the case when the respondent is terminated, log the overall results.
                case (_, Terminated(_)) =>
                  context.log.info(s"Wrong: $failed;")
                  context.log.info(s"Passed: $passed;")
                  context.log.info(s"In a row: $inARow;")
                  Behaviors.stopped
              }
          }
      }.onFailure[Exception](SupervisorStrategy.restart)
}
