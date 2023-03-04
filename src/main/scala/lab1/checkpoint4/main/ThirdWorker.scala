package org.marius
package lab1.checkpoint4.main

import lab1.checkpoint4.main.Supervisor.{Join, LowerCaseAndSwap, Message, SignalFromChild}

import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorRef, Behavior, SupervisorStrategy}

import scala.collection.mutable.ArrayBuffer
import scala.util.Random

object ThirdWorker {
  def apply(): Behavior[Message] =
    Behaviors
      .setup[Message] { context =>
        Behaviors
          .supervise[Message] {
            Behaviors
              .receiveMessagePartial[Message] {
                // Join the words into a sentence.
                case Join(message, replyTo) =>
                  // Try to split the message into words. If an exception occurs, catch it and send a signal to the supervisor.
                  try {
                    // Create a new string to store the computed message.
                    var newMessage = ""
                    // Iterate through the list of words.
                    message.foreach(word => {
                      // Add the word to the new message.
                      newMessage += word
                      // Add a space if the word is not the last word.
                      if (word != message.lastOption.getOrElse("")) {
                        newMessage += " "
                      }
                    })
                    
                    // Randomly throw an exception.
                    if (Random.nextBoolean()) {
                      throw new Exception("Random exception from third worker.")
                    }
                    context.log.info(s"Third step: `$message` -> `$newMessage`")
                    context.log.info(s"--> Final result: `$newMessage`.`")
                  } catch {
                    // Send a signal to the supervisor if an exception occurs.
                    case _: Exception => replyTo ! SignalFromChild
                  }

                  Behaviors.same
              }
          }.onFailure(SupervisorStrategy.restart)
      }
}
