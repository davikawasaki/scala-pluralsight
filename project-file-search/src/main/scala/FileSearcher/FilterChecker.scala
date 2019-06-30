package FileSearcher

import java.io.File

import scala.util.control.NonFatal

// Property is private, due to the fact that is not using the val keyword
class FilterChecker(filter : String) {
  val filterAsRegex = filter.r

  // Helper method added for readability
  // def matches(content : String) = content.contains(filter)
  // def matches(content : String) = content contains filter
  def matches(content : String) = filterAsRegex findFirstMatchIn content match {
    case Some(_) => true
    case None => false
  }

  // Expressiveness optimization: infix notation
  // Allows any method that takes only parameter to be called without the dot or parenthesis
  def findMatchedFiles(iOObjects : List[IOObject]) =
    for(iOObject <- iOObjects
      // Filter criterias
      if(iOObject.isInstanceOf[FileObject])  // Java reflection library
      if(matches(iOObject.name)))
    yield iOObject

  // java.nio, rapture.io, scalaz.stream should be used in bigger projects instead of the scala.io.Source
  // def matchesFileContent(file : File) = {
  def findMatchedContentCount(file : File) = {
    import scala.io.Source

    def getFilterMatchCount(content : String) =
      (filterAsRegex findAllIn content).length

    try {
      // Converting the java file into a Scala source
      val fileSource = Source.fromFile(file)
      try
        // fileSource.getLines() exists(line => matches(line))
        // Gets sum of matched lines with a folding list functional programming technique
        fileSource.getLines().foldLeft(0)(
          (accumulator, line) => accumulator + getFilterMatchCount(line))
      catch {
        case NonFatal(_) => 0
      }
      finally
        fileSource.close()
    }
    catch {
      case NonFatal(_) => 0
    }
  }
}

// Companion object
// Creates a singleton or static class
object FilterChecker {
  def apply(filter: String) = new FilterChecker(filter)
}