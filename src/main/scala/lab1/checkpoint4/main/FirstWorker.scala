package org.marius
package lab1.checkpoint4.main

import lab1.checkpoint4.main.Supervisor.{LowerCaseAndSwap, Message, SignalFromChild, Split}

import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorRef, Behavior, SupervisorStrategy}

import scala.collection.mutable.ArrayBuffer
import scala.util.Random

object FirstWorker {
  def apply(nextWorker: ActorRef[Message]): Behavior[Message] =
    Behaviors
      .setup[Message] { context =>
        Behaviors
          .supervise[Message] {
            Behaviors
              .receiveMessagePartial[Message] {
                // Split the message into a list of words.
                case Split(message, replyTo) =>
                  // Try to split the message into words. If an exception occurs, catch it and send a signal to the supervisor.
                  try {
                    // Trim (delete the spaces at the beginning and end of the message) the received message and assign it to a new variable.
                    var originalMessage = message.trim()
                    // Create a new array buffer to store the words.
                    val splitMessage: ArrayBuffer[String] = new ArrayBuffer[String]()
                    // Index of the current character.
                    var i = 0
                    // Iterate through the message.
                    while (i < originalMessage.length - 1) {
                      // If the current character is a space.
                      if (originalMessage(i) == ' ') {
                        // Get the word from the beginning of the message to the current character.
                        val currentString = originalMessage.substring(0, i)
                        // Add the word to the array buffer.
                        splitMessage.addOne(currentString)
                        // Delete the added word from the message.
                        originalMessage = originalMessage.substring(i, originalMessage.length).trim()
                        // Reset the index.
                        i = 0
                      }
                      // If the current character is not a space, increment the index.
                      i += 1
                    }
                    // Add the last word to the array buffer.
                    splitMessage += originalMessage
                    
                    // Randomly throw an exception.
                    if (Random.nextBoolean()) {
                      throw new Exception("Random exception from first worker.")
                    }
                    context.log.info(s"First step: `$message` -> `$splitMessage`")
                    // Send the list of words to the next worker.
                    nextWorker ! LowerCaseAndSwap(splitMessage.toList, replyTo)
                  } catch {
                    // Send a signal to the supervisor if an exception occurs.
                    case _: Exception => replyTo ! SignalFromChild
                  }

                  Behaviors.same
              }
          }.onFailure(SupervisorStrategy.restart)
      }
}
