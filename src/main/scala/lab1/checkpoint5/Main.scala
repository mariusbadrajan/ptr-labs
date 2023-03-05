package org.marius
package lab1.checkpoint5

import lab1.checkpoint5.minimal.ResponseParser

object Main extends App {
  // Minimal task
  // Call the method to parse the response (status code, headers, body).
  ResponseParser.parseResponse()
  // Print the parsed response.
  ResponseParser.printParsedResponse()

  // Call the method to extract the quotes from the response body.
  ResponseParser.extractQuotes()
  // Print the extracted quotes.
  ResponseParser.printExtractedQuotes()

  // Call the method to convert the quotes to JSON.
  ResponseParser.convertToJson()
  // Print the JSON quotes.
  ResponseParser.printJsonQuotes()
}