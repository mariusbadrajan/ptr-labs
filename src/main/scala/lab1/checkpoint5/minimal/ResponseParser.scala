package org.marius
package lab1.checkpoint5.minimal

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpRequest, HttpResponse}
import akka.stream.Materializer

import scala.collection.mutable.Map
import scala.concurrent.duration.*
import scala.concurrent.{Await, Future}

object ResponseParser {
  implicit val ec: scala.concurrent.ExecutionContext = scala.concurrent.ExecutionContext.global
  implicit val system: ActorSystem = ActorSystem()

  private val url = "https://quotes.toscrape.com/"
  private val statusCode = "Response status code"
  private val headers = "Response headers"
  private val body = "Response body"
  private val author = "Author"
  private val text = "Text"
  private val tags = "Tags"

  private val responseMap: Map[String, String] = Map[String, String]()
  private var extractedQuotes: List[Map[String, String]] = List[Map[String, String]]()

  def parseResponse(): Map[String, String] = {
    val responseFuture: Future[HttpResponse] = Http().singleRequest(HttpRequest(uri = url))

    val parsedResponse: Future[Any] = responseFuture.map { response =>
      responseMap += (statusCode -> s"${response.status.intValue}")
      responseMap += (headers -> "")
      response.headers.foreach(header =>
        responseMap(headers) += s"${header.name}: ${header.value}"
        if (header != response.headers.lastOption.getOrElse("")) {
          responseMap(headers) += "\n"
        }
      )
      val responseBody: Future[Map[String, String]] = response.entity.toStrict(1.seconds)
        .flatMap { strictEntity =>
          val byteString = strictEntity.data
          val utf8String = byteString.utf8String
          val convertedString = convertHtmlApostropheEntity(utf8String)
          responseMap += (body -> s"$convertedString")
          Future.successful(responseMap)
        }

      Await.ready(responseBody, 10.seconds)
    }
    Await.ready(parsedResponse, 10.seconds)

    responseMap
  }

  def extractQuotes(): Unit = {
    val quoteBlockRegex = "<div class=\"text\".*?>([\\s\\S]*?)<\\/div>\\s*?<\\/div>".r
    val quoteTextRegex = "<span class=\"text\".*?>(.*?)<\\/span>".r
    val quoteAuthorRegex = "<span.*?class=\"author\".*?>(.*?)<\\/small>[\\s\\S]*?<\\/span>".r
    val quoteTagRegex = "<a class=\"tag\".*?>(.*?)<\\/a>".r

    val responseBody = responseMap(body)
    val parsedQuotes = quoteBlockRegex.findAllIn(responseBody).toList

    extractedQuotes = parsedQuotes.map { quote =>
      val author = quoteAuthorRegex.findFirstMatchIn(quote).map(_.group(1)).getOrElse("")
      val text = quoteTextRegex.findFirstMatchIn(quote).map(_.group(1)).getOrElse("")
      val tags = quoteTagRegex.findAllMatchIn(quote).map(_.group(1)).mkString(", ")

      Map(this.author -> author, this.text -> text, this.tags -> tags)
    }
  }

  def printParsedResponse(): Unit = {
    println("--> HTTP Response <--")
    println(s"Response status code: ${responseMap(statusCode)}")
    val responseHeaders = responseMap(headers).split("\n")
    println("Response headers:")
    responseHeaders.foreach(header => println(s"\t$header"))
    println(s"Response body:\n${responseMap(body)}")
    println()
  }

  def printExtractedQuotes(): Unit = {
    println("--> Parsed quotes <--")
    extractedQuotes.zipWithIndex.foreach { (item, index) =>
      val parsedItem = parseAnyToMap(item).getOrElse(Map.empty)
      println(s"${index + 1}:")
      println(s"\tQuote: ${parsedItem(text).substring(1, parsedItem(text).length - 1)};")
      println(s"\tAuthor: ${parsedItem(author)};")
      println(s"\tTags: ${parsedItem(tags)};")
    }
    println()
  }

  private val convertHtmlApostropheEntity: String => String =
    (inputString: String) => inputString.replaceAll("&#39;", "'")

  private val parseAnyToMap: Any => Option[Map[String, String]] =
    (anyValue: Any) => anyValue match {
      case map: Map[String, String] => Some(map)
      case _ => None
    }
}
