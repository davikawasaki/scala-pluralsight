package FileSearcher

import java.io.File

// Left without a definition, therefore forces classes extending it to implement it or also be an abstract artifact
trait IOObject {
  val file : File
  val name = file.getName()
}

// Classes without any object implementation can hide brackets
// class FileObject(val name : String) extends IOObject {}
case class FileObject(file : File) extends IOObject
case class DirectoryObject(file : File) extends IOObject {
  def children() =
    try
      file.listFiles().toList map(file => FileConverter convertToIOObject file)
    catch {
      case _ : NullPointerException => List()
    }
}
