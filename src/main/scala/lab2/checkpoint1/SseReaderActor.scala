package org.marius
package lab2.checkpoint1

import lab2.checkpoint1.SseHashtagPrinterActor.{Find, Command as HashTagPrinterCommand}
import lab2.checkpoint1.SsePrinterActor.{Print, Command as PrinterCommand}

import akka.Done
import akka.actor.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorRef, Behavior}
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.sse.ServerSentEvent
import akka.http.scaladsl.model.{HttpRequest, HttpResponse}
import akka.stream.scaladsl.{Sink, Source}
import akka.util.ByteString

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.concurrent.duration.*
import scala.util.Random

object SseReaderActor {
  implicit val system: ActorSystem = ActorSystem()
  
  sealed trait Command

  case class Start(url: String) extends Command

  case object Stop extends Command

  def apply(printer: ActorRef[PrinterCommand], hashTagPrinter: ActorRef[HashTagPrinterCommand] = null): Behavior[Command] =
    Behaviors
      .setup[Command] { context =>
        Behaviors
          .receiveMessagePartial[Command] {
            case Start(url) =>
              // Future[HttpResponse] -> object, asynchronous computation, that will eventually yield an HTTP response object.
              val responseFuture: Future[HttpResponse] = Http().singleRequest(HttpRequest(uri = url))

              // responseFuture.map -> transforms the result of an asynchronous computation,
              // result: A new Future[U].
              // responseFuture.flatMap -> chains asynchronous operations together, where the output of one operation is the input of the next operation,
              // result: A new Future[U] that represents the result of the chained operations.
              val source: Future[Done] = responseFuture.flatMap { response =>
                // response: HttpResponse -> http response.
                // response.entity: ResponseEntity -> payload / body of http response.

                // NOTES:
                // Response entity can be represented as a stream-based entity or a strict entity.

                // 1. STREAM-BASED (Source[ByteString, Any]) entity is represented as a Source[ByteString, Any], which is a reactive stream of data chunks.
                // This representation is efficient for handling large response bodies because it allows the data to be processed incrementally as it arrives.
                // This can be especially important for responses that are too large to fit in memory all at once.
                // STREAM-BASED entities are more memory-efficient and can handle very large response bodies, but they require stream processing operations to access the data.

                // 2. STRICT (ByteString) entity is represented as a single ByteString, which contains the entire response body in memory.
                // This representation can be more convenient for processing because it allows the data to be accessed as a single chunk, without the need for stream processing operations.
                // STRICT entities are more convenient for processing but can be memory-intensive, especially for large response bodies.

                // 1. STREAM entity: response.entity.dataBytes: Source[ByteString, Any] ->
                // -> stream of bytes / chunks of response body.
                // 2. STRICT entity: response.entity.toStrict(1.seconds): Future[HttpEntity.Strict] ->
                // -> converts a stream-based entity to a strict entity. The resulting strict entity contains the entire response body in memory as a single ByteString.

                // response.entity.dataBytes.map(_.utf8String): Source[ByteString, Any]#Repr[String] / Source[String, Any] ->
                // -> represents the response body as a stream of UTF-8 encoded strings.
                val sourceByteString: Source[ByteString, Any]#Repr[String] = response.entity.dataBytes.map(_.utf8String)

                // NOTES:
                // Sink - represents the end of the data flow.
                // A Sink is a set of stream processing steps that has `one open input`. Can be used as a Subscriber.
                // One open input = the Sink has exactly one input stream that is continuously receiving data.
                // The data flows into the Sink's input stream, where it can be processed according to the steps defined in the Sink.

                // Sink.foreach[String]: Sink[String, Future[Done]] ->
                // -> represents a stream processing construct (object) in which each incoming element of the stream (which is expected to be of type String) is processed by the specified function.
                val sink = Sink.foreach[String] { (tweet: String) =>
                  printer ! Print(tweet)
                  hashTagPrinter ! Find(tweet)
                }
                // sourceByteString.runWith(sink): Future[Done] ->
                // -> creates a data processing pipeline by connecting a Source[ByteString, Any] to a Sink and executes the pipeline.
                sourceByteString.runWith(sink)
              }
              // source.onComplete(_ => system.terminate()): Unit ->
              // -> shutdowns an Akka Streams Source after all elements have been processed.
              source.onComplete(_ => system.terminate())
              
              Behaviors.same
            case Stop =>
              system.terminate()
              Behaviors.stopped
          }
      }
}
