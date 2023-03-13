package org.marius
package lab2.checkpoint1

import lab2.checkpoint1
import lab2.checkpoint1.SseReaderActor.Start as StartReader

import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors

object Pool {
  sealed trait Command

  case object Start extends Command

  case object Stop extends Command

  private val urlOne: String = "http://localhost:4000/tweets/1"
  private val urlTwo: String = "http://localhost:4000/tweets/2"
  
  def apply(): Behavior[Command] =
    Behaviors
      .setup[Command] { context =>
        Behaviors.receiveMessagePartial[Command] {
          // Creates actors to handle the SSE stream and sends them messages to start processing the streams.
          case Start =>
            val ssePrinter = context.spawn(SsePrinterActor(), "sse-printer")
            val sseHashTagPrinter = context.spawn(SseHashtagPrinterActor(), "sse-hashtag-printer")
            val sseReaderOne = context.spawn(checkpoint1.SseReaderActor(ssePrinter, sseHashTagPrinter), "sse-reader-1")
            val sseReaderTwo = context.spawn(checkpoint1.SseReaderActor(ssePrinter, sseHashTagPrinter), "sse-reader-2")
            sseReaderOne ! StartReader(urlOne)
            sseReaderTwo ! StartReader(urlTwo)
            Behaviors.same
          case Stop =>
            Behaviors.stopped
        }
      }
}
