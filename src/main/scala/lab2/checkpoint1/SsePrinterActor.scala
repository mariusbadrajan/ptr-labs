package org.marius
package lab2.checkpoint1

import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{Behavior, SupervisorStrategy}
import io.circe.Json
import io.circe.parser.*

import scala.util.Random

object SsePrinterActor {
  sealed trait Command

  case class Print(data: String) extends Command

  def apply(): Behavior[Command] =
    Behaviors
      .supervise[Command] {
        Behaviors
          .setup[Command] { context =>

            def sleep(): Unit = {
              // duration: This is the base sleep duration in milliseconds. It is generated randomly between 5ms and 50ms using Random.nextInt(46) + 5.
              // Random.nextInt(46): generates a random integer between 0 and 45 (inclusive), and adding 5 to it gives a random integer between 5 and 50 (inclusive).
              val duration: Int = Random.nextInt(46) + 5
              // durationWithJitter: This is the actual sleep duration in milliseconds, after applying some jitter to the base duration. The jitter is introduced to make the sleep duration follow a Poisson distribution, which can more accurately model the actual arrival times of events in some systems.
              // Random.nextDouble(): generates a random double between 0 and 1 (exclusive), which is then used to add some jitter to the base duration.
              // (Random.nextDouble() - 0.5): generates a random double between -0.5 and 0.5, representing a uniform distribution centered at 0.
              // / 10.0: divides the random jitter by 10, effectively scaling it to a range of -0.05 to 0.05.
              // * (1.0 + (Random.nextDouble() - 0.5) / 10.0): multiplies the base duration by a factor randomly between 0.95 and 1.05, effectively adding some jitter to the base duration. This produces the final sleep duration after applying the jitter.
              val durationWithJitter: Int = (duration * (1.0 + (Random.nextDouble() - 0.5) / 10.0)).toInt
              Thread.sleep(durationWithJitter)
            }

            Behaviors.receiveMessagePartial[Command] {
              case Print(data) =>
                // Splits the string variable data into two parts using the delimiter "data: ".
                // The first part (event: "message") of the split is discarded, and only the second part (which starts with "data: ") is kept.
                val splitData: Array[String] = data.split("data: ")
                // Takes the second part of splitData and attempts to parse it as JSON.
                // If the parse is successful, the resulting Json value is stored in a new variable called jsonData.
                // If the parse fails, the value Json.Null is used instead.
                val jsonData: Json = parse(splitData(1)).getOrElse(Json.Null)
                // Uses the hcursor method on the jsonData object to navigate through its nested fields and extract the value of the "text" field.
                val text: String = jsonData.hcursor
                  .downField("message")
                  .downField("tweet")
                  .downField("text").as[String] match {
                  // If the value of this field is a String, it is stored in a new variable called text.
                  case Right(value) => value
                  // If the value is not a String, an error message is logged and an exception is thrown.
                  case Left(failure) =>
                    context.log.warn("An error encountered. Continuing ...")
                    throw new Exception(failure.message)
                }

                // Logs the text of received tweet.
                context.log.info(s"text: $text")
                // Pauses the current thread for random time value from 5ms to 50ms.
                sleep()
                Behaviors.same
            }
          }
      }.onFailure(SupervisorStrategy.resume)
}
