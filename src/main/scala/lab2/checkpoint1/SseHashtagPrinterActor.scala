package org.marius
package lab2.checkpoint1

import lab2.checkpoint1.SsePrinterActor.Command

import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorRef, Behavior, SupervisorStrategy}
import akka.actor.{ActorSystem, Cancellable}
import akka.stream.scaladsl.Source
import akka.stream.{ActorMaterializer, Materializer}
import io.circe.Json
import io.circe.parser.parse
import org.slf4j.Logger

import scala.concurrent.duration.*
import scala.util.Random

object SseHashtagPrinterActor {
  implicit val system: ActorSystem = ActorSystem()

  sealed trait Command

  case class Find(data: String) extends Command

  def apply(): Behavior[Command] =
    Behaviors
      .supervise[Command] {
        Behaviors
          .setup[Command] { context =>
            // Keeps track of the count of each hashtag that appears in the incoming messages.
            var hashTagsMap: Map[String, Int] = Map[String, Int]()
            // Stores hashtags that have the maximum count (strings delimited by `,`).
            var keysWithMaxValue: String = ""
            // Declares a new log variable by extracting the logging context from the context object,
            // because source will run on another thread so we need to pass to it the current logging context as a new variable
            val logger: Logger = context.log

            // Creates a Source object that emits a tick message every 5 seconds.
            // The first tick message will be emitted immediately after the source is created.
            val source: Source[String, Cancellable] = Source.tick(0.seconds, 5.seconds, "tick")
            // Attaches a runForeach callback to the source object.
            // This callback will be called every time a tick message is emitted by the source.
            // Inside the callback, the code checks if hashTagsMap is empty.
            // If it's not, the code finds the hashtag(s) with the maximum count and logs a message indicating which hashtags are the most popular.
            // Finally, hashTagsMap is cleared to start counting hashtags for the next time interval.
            source.runForeach { _ =>
              if (hashTagsMap.nonEmpty) {
                val maxValue = hashTagsMap.values.max
                keysWithMaxValue = hashTagsMap.filter(_._2 == maxValue).keys.mkString(", ")
                logger.info(s"Most popular hashtag: $keysWithMaxValue")
                hashTagsMap = Map.empty[String, Int]
              }
            }

            Behaviors.receiveMessagePartial[Command] {
              case Find(data) =>
                // Splits the string variable data into two parts using the delimiter "data: ".
                // The first part (event: "message") of the split is discarded, and only the second part (which starts with "data: ") is kept.
                val splitData: Array[String] = data.split("data: ")
                // Takes the second part of splitData and attempts to parse it as JSON.
                // If the parse is successful, the resulting Json value is stored in a new variable called jsonData.
                // If the parse fails, the value Json.Null is used instead.
                val jsonData: Json = parse(splitData(1)).getOrElse(Json.Null)
                // Navigates through the JSON structure to reach the "hashtags" field.
                // The resulting list of hashtag strings is stored in the hashtags variable.
                val hashtags: Vector[String] = jsonData.hcursor
                  .downField("message")
                  .downField("tweet")
                  .downField("entities")
                  // The as[Vector[Json]] method is called on the "hashtags" field to extract a vector of JSON objects representing the hashtags.
                  .downField("hashtags").as[Vector[Json]]
                  // If the "hashtags" field is missing from the JSON data, an empty vector is returned by getOrElse.
                  .getOrElse(Vector.empty)
                  // Then, the map method is called on the vector to extract the text of each hashtag.
                  .map { jsonHashtag =>
                    // For each hashtag, the downField method is called on its JSON object to extract its "text" field.
                    jsonHashtag.hcursor.downField("text")
                      // The as[String] method is called on the "text" field to extract its value as a string.
                      .as[String] match {
                      // If the string extraction is successful, the value is returned by Right.
                      case Right(value) => value
                      // Otherwise, an error message is logged, and an exception is thrown.
                      case Left(failure) =>
                        context.log.warn("An error encountered. Continuing ...")
                        throw new Exception(failure.message)
                    }
                  }

                // Iterating over the list of extracted hashtags and updates the hashTagsMap variable accordingly.
                hashtags.foreach(hashtag => {
                  // For each hashtag, the code checks if it exists in the map by calling contains on the hashTagsMap.
                  if (!hashTagsMap.contains(hashtag)) {
                    // If the hashtag is not in the map, it is added with a count of 1 using the updated method on the hashTagsMap.
                    hashTagsMap = hashTagsMap.updated(hashtag, 1)
                  }
                  else {
                    // If the hashtag is already in the map, its count is incremented by 1 and the hashTagsMap is updated accordingly.
                    hashTagsMap = hashTagsMap.updated(hashtag, hashTagsMap(hashtag) + 1)
                  }
                })

                Behaviors.same
            }
          }
      }.onFailure(SupervisorStrategy.resume)
}
