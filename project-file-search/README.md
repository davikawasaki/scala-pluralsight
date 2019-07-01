# Scala: Getting Started

Pluralsight [Scala course](https://app.pluralsight.com/library/courses/scala-getting-started) code that develops a file search project using Scala and Java features.

## Table of Contents

* [Usage](#usage)
* Documentation

## Usage

All code was developed using Scala inside [Jetbrains IntelliJ IDEA](https://www.jetbrains.com/idea/), which has the right plugins to start developing and testing (e.g. SBT shell console). In order to start working with Scala in this IDE environment, some [extra configurations](https://docs.scala-lang.org/getting-started-intellij-track/getting-started-with-scala-in-intellij.html) are necessary.

One of the changes necessary for testing purposes is replacing the *rootPathUsed* and *testFilesRootPath* inside [MatcherTests.scala](https://github.com/davikawasaki/scala-pluralsight/blob/master/project-file-search/src/test/scala/FileSearcher/MatcherTests.scala) test file to your own root testing path:

```scala
val rootPathUsed = "/home/kawasaki/Git/study/scala-pluralsight/project-file-search/"
val testFilesRootPath = s"${rootPathUsed}testfiles/"
```

The [Runner.scala](https://github.com/davikawasaki/scala-pluralsight/blob/master/project-file-search/src/main/scala/FileSearcher/Runner.scala) application can be executed using the following commands (first will print the found files with text in the console, whilst the second will output a specific named file to the project root folder) - remember to change the folder in the commands to the desired files search folder:

```scala
run "txt" "/home/kawasaki/Git/study/scala-pluralsight/project-file-search/testfiles" "true" "foo"
run "txt" "/home/kawasaki/Git/study/scala-pluralsight/project-file-search/testfiles" "true" "foo" "results.out"
```

