package org.marius
package lab1.checkpoint4.main

import lab1.checkpoint4.main.Supervisor.{Join, LowerCaseAndSwap, Message, SignalFromChild}

import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorRef, Behavior, SupervisorStrategy}

import scala.collection.mutable.ArrayBuffer
import scala.util.Random

object SecondWorker {
  def apply(nextWorker: ActorRef[Message]): Behavior[Message] =
    Behaviors
      .setup[Message] { context =>
        Behaviors
          .supervise[Message] {
            Behaviors
              .receiveMessagePartial[Message] {
                // Lowercase the words and swap the letters m and n.
                case LowerCaseAndSwap(message, replyTo) =>
                  // Try to split the message into words. If an exception occurs, catch it and send a signal to the supervisor.
                  try {
                    // Create a new array buffer to store the computed words.
                    val computedWords: ArrayBuffer[String] = new ArrayBuffer[String]()
                    // Iterate through the words and lowercase them and swap the letters m and n and add them to the array buffer.
                    message.foreach(word => {
                      computedWords.addOne(word.toLowerCase().replace("m", "_").replace("n", "m").replace("_", "n"))
                    })

                    // Randomly throw an exception.
                    if (Random.nextBoolean()) {
                      throw new Exception("Random exception from first worker.")
                    }
                    context.log.info(s"Second step: `$message` -> `$computedWords`")
                    // Send the list of words to the next worker.
                    nextWorker ! Join(computedWords.toList, replyTo)
                  } catch {
                    // Send a signal to the supervisor if an exception occurs.
                    case _: Exception => replyTo ! SignalFromChild
                  }

                  Behaviors.same
              }
          }.onFailure(SupervisorStrategy.restart)
      }
}
