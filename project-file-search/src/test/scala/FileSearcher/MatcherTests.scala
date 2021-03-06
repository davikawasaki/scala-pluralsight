package fileSearcher

import java.io.File

import FileSearcher.Matcher
import org.scalatest._

class MatcherTests extends FlatSpec {
    val rootPathUsed = "/home/kawasaki/Git/study/scala-pluralsight/project-file-search/"
    val testFilesRootPath = s"${rootPathUsed}testfiles/"

    "Matcher that is passed a file matching the filter" should
    "return a list with that file name" in {
        val matcher = new Matcher("fake", "fakePath")
        val results = matcher.execute()
        assert(results == List((s"${rootPathUsed}fakePath", None)))  // usage of tuples, which can contain until 22 slots that are each strong typed
    }

    "Matcher using a directory containing one file matching the filter" should
    "return a list with that file name" in {
        val matcher = new Matcher("txt", new File("testfiles/").getCanonicalPath())
        val results = matcher.execute()
        // assert(new File("testfiles\\").getCanonicalPath() == "/home/kawasaki/Git/study/scala-pluralsight/project-file-search/testfiles\\")
        assert(results == List((s"${testFilesRootPath}readme.txt", None)))
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
        assert(results == List((s"${testFilesRootPath}innerFolder/notes.txt", None),
            (s"${testFilesRootPath}readme.txt", None)))
    }

    "Matcher given a path that has one file that matches file filter and content filter" should
    "return a list with that file name" in {
        val matcher = new Matcher("data", new File(".").getCanonicalPath(), true,
            Some("pluralsight"))
        val matchedFiles = matcher.execute()
        assert(matchedFiles == List((s"${testFilesRootPath}pluralsight.data", Some(3))))
    }

    "Matcher given a path that has no file that matches file filter and content filter" should
    "return an empty list" in {
        val matcher = new Matcher("txt", new File(".").getCanonicalPath(), true,
            Some("pluralsight"))
        val matchedFiles = matcher.execute()
        assert(matchedFiles == List())
    }
}