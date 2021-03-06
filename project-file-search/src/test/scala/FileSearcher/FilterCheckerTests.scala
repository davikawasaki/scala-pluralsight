package FileSearcher

import java.io.File
import org.scalatest._

// http://www.scalatest.org/user_guide/selecting_a_style
class FilterCheckerTests extends FlatSpec {
  "FilterChecker passed a list where one file matches the filter" should
  "return a list with that file" in {
    val matchingFile = FileObject(new File("match"))
    val listOfFiles = List(FileObject(new File("random")), matchingFile)

    // First option instantiate a class, second uses a companion object with infix notation
    // val matchedFiles = new FilterChecker("match").findMatchedFiles(listOfFiles)
    val matchedFiles = FilterChecker("match") findMatchedFiles listOfFiles

    // == check object value equality, meanwhile .eq() check reference equality
    assert(matchedFiles == List(matchingFile))
  }

  "FilterChecker passed a list with a directory that matches the filter" should
  "should not return the directory" in {
    val listOfIOObjects = List(FileObject(new File("random")), DirectoryObject(new File("match")))
    val matchedFiles = FilterChecker("match") findMatchedFiles listOfIOObjects
    assert(matchedFiles.isEmpty)
  }

  "FilterChecker passed a file with content that matches the filter 3 times" should
  "return a 3" in {
    val isContentMatched = FilterChecker("pluralsight")
      .findMatchedContentCount(new File("testfiles/pluralsight.data"))
    assert(isContentMatched == 3)
  }

  "FilterChecker passed a file with content that does not match the filter" should
  "return a 0" in {
    val isContentMatched = FilterChecker("pluralsight")
      .findMatchedContentCount(new File("testfiles/readme.txt"))
    assert(isContentMatched == 0)
  }
}
