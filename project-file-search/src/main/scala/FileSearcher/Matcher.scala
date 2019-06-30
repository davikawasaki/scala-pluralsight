package FileSearcher

import java.io.File

import scala.annotation.tailrec

// Option: explicit stating the optionality of the parameter avoiding all unusual behaviors that comes
// from unsafe values (e.g. null or empty)
class Matcher(filter: String, val rootLocation: String = new File(".").getCanonicalPath(),
              checkSubFolders : Boolean = false, contentFilter : Option[String] = None) {
  val rootIOObject = FileConverter.convertToIOObject(new File(rootLocation))

  def execute() = {
    // Recurse type inference in recursive methods in Scala is not allowed. Therefore, return must be implicitly setted.
    @tailrec
    def recursiveMatch(files: List[IOObject], currentList: List[FileObject]) : List[FileObject] =
      // using pattern matching to simplify the deconstruction of IOObject list
      files match {
        case List() => currentList
          // case with the head of the list, followed by the rest of the list (or tail) using the cons (::) operator
          // cons (::) operator: corresponds to the list's append operator, where the first element is the head
          // and the second the tail, normally the rest of the list or just the Nil (end of the list)
          // note: it works as a heap, so every el that is added goes to the top of the pile as a head
          // more info: https://mohanjune.wordpress.com/2013/01/04/scala-data-structures-lists/
        case iOObject :: rest =>
          iOObject match {
            case file : FileObject if FilterChecker(filter) matches file.name =>
              recursiveMatch(rest, file :: currentList)  // append the matched file object to the accumulated matches
            case directory : DirectoryObject =>
              recursiveMatch(rest ::: directory.children(), currentList)  // append two lists using the triple colons operator
            case _ => recursiveMatch(rest, currentList)
          }
      }

    val matchedFiles = rootIOObject match {
      case file : FileObject if FilterChecker(filter) matches file.name => List(file)
      // case directory : DirectoryObject => ???  // triple question marks correspond to not implemented exception to be thrown
      case directory : DirectoryObject =>
        if (checkSubFolders) recursiveMatch(directory.children(), List())  // List() as the tail recursive accumulator
        else FilterChecker(filter) findMatchedFiles directory.children()
      case _ => List()  // default class as underline
    }

    // Some and None are considered case classes and can be pattern matched directly in a form that just looks like a ctor
    val contentFilteredFiles = contentFilter match {
      // case Some(dataFilter) => matchedFiles filter(iOObject =>
      //   FilterChecker(dataFilter).findMatchedContentCount(iOObject.file) > 0)  // loops through all objects, returning a list instead of a bool
      case Some(dataFilter) =>
        matchedFiles.map(iOObject =>
          (iOObject, Some(FilterChecker(dataFilter)
            .findMatchedContentCount(iOObject.file))))
          // tuple retrieval with underscore followed by the 1-based index of the slot
          // default retrieval with getOrElse(0)
          .filter(matchTuple => matchTuple._2.get > 0)
      case None => matchedFiles map (iOObject => (iOObject, None))
      // if commented the None case, compiler would say that the match may not be exhaustive,
      // since it would fail in this non-existing commented case
    }

    // contentFilteredFiles map(iOObject => iOObject.name)
    contentFilteredFiles map { case (iOObject, count) => (iOObject.name, count) }
  }
}
