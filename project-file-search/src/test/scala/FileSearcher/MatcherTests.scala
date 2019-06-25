package fileSearcher

import java.io.File

import FileSearcher.Matcher
import org.scalatest._

class MatcherTests extends FlatSpec {
    "Matcher that is passed a file matching the filter" should
    "return a list with that file name" in {
        val matcher = new Matcher("fake", "fakePath")
        val results = matcher.execute()
        assert(results == List("fakePath"))
    }

    "Matcher using a directory containing one file matching the filter" should
    "return a list with that file name" in {
        val matcher = new Matcher("txt", new File("testfiles/").getCanonicalPath())
        val results = matcher.execute()
        // assert(new File("testfiles\\").getCanonicalPath() == "/home/kawasaki/Git/study/scala-pluralsight/project-file-search/testfiles\\")
        assert(results == List("readme.txt"))
    }

    "Matcher that is not passed a root file location" should
    "use the current location" in {
        val matcher = new Matcher("filter")
        assert(matcher.rootLocation == new File(".").getCanonicalPath())
    }

    "Matcher with sub folder checking a root location with two subtree files matching" should
    "return a list with those file names" in {
        val searchSubDirectories = true
        val matcher = new Matcher("txt", new File("testfiles/").getCanonicalPath(), searchSubDirectories)
        val results = matcher.execute()
        assert(results == List("notes.txt", "readme.txt"))
    }
}