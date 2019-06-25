package FileSearcher

import java.io.File

import scala.annotation.tailrec

class Matcher(filter: String, val rootLocation: String = new File(".").getCanonicalPath(),
              checkSubFolders : Boolean = false) {
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
    matchedFiles map(iOObject => iOObject.name)
  }
}
