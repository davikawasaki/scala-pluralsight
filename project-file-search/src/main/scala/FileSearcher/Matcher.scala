package FileSearcher

import java.io.File

import scala.annotation.tailrec

// Option: explicit stating the optionality of the parameter avoiding all unusual behaviors that comes
// from unsafe values (e.g. null or empty)
/**
  * This is the main entry point for checking the file system via supplied specs
  * @param filter The filter that will be used to match against the filenames
  * @param rootLocation The starting location to search
  * @param checkSubFolders A boolean denoting whether or not to search all subfolders
  * @param contentFilter A filter that will be used to match against the file contents
  */
class Matcher(filter: String, val rootLocation: String = new File(".").getCanonicalPath(),
              checkSubFolders : Boolean = false, contentFilter : Option[String] = None) {
  val rootIOObject = FileConverter.convertToIOObject(new File(rootLocation))

  /**
    * This searches for the files that match the supplied specs
    * @return A list of filename, content match count pairs
    */
  def execute() = {
    def getPagingInfo(page : Int, pageSize : Int, totalItems : Int) = {
      val pages = 1 to totalItems by pageSize
      val from = pages(page - 1)
      val to = from + pageSize - 1 min totalItems
      val totalPages = pages.size
      (from, to, totalPages)
    }

    def getListForRange(list : List[(Int, IOObject, Double)], start : Int, end : Int) =
      list.dropWhile(_._1 < start)
          .takeWhile(ioGlob => ioGlob._1 >= start && ioGlob._1 <= end)

    def contentMatch(dataFilter : String, matchedFiles : List[IOObject]) = {
      var id = 0
      val matchedFilesWithIdAndFileSize = matchedFiles.map(x => {
        id = id + 1
        (id, x, x.fileSize)
      })

      val pageData = getPagingInfo(1, 10000, matchedFiles.length)
      println("starting content match process")
      var totalMb = 0D
      val beginTime = System.nanoTime()
      val totalPages = pageData._3
      val results = for(currPage <- 1 to totalPages)
        yield {
          val currPageData = getPagingInfo(currPage, 10000, matchedFiles.length)
          val currRunList = getListForRange(matchedFilesWithIdAndFileSize, currPageData._1, currPageData._2)
          // val listToFilter = currRunList
          val listToFilter = currRunList.par  // parallelizing the search
          val currentRunStartTime = System.nanoTime()
          val result = listToFilter
            .map(iOObject =>
              (iOObject._2, Some(FilterChecker(dataFilter).findMatchedContentCount(iOObject._2.file))))
            .filter(matchTuple => matchTuple._2.get > 0)
          val currentRunEndTime = System.nanoTime
          val currentRunMb = listToFilter.foldLeft(0D)((accum, ioGlob) => accum.toDouble + ioGlob._3)
          totalMb = totalMb + currentRunMb
          println(s"Page: $currPage; "
                + s"Total Running Time: ${(currentRunEndTime - beginTime) * 1e-9}; "
                + s"Total MB: $totalMb; "
                + s"Current Run Time: ${(currentRunEndTime - currentRunStartTime) * 1e-9}; "
                + s"Current Run MB: $currentRunMb")
          result
        }

      val finalTime = System.nanoTime()
      println(s"Total Running Time: ${(finalTime - beginTime)*1e-9}; "
            + s"Total MB: $totalMb")
      results.toList.flatten
    }

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
    contentFilteredFiles map { case (iOObject, count) => (iOObject.fullName, count) }
  }
}
