package FileSearcher

// Property is private, due to the fact that is not using the val keyword
class FilterChecker(filter : String) {
  // Helper method added for readability
  // def matches(content : String) = content.contains(filter)
  def matches(content : String) = content contains filter

  // Expressiveness optimization: infix notation
  // Allows any method that takes only parameter to be called without the dot or parenthesis
  def findMatchedFiles(iOObjects : List[IOObject]) =
    for(iOObject <- iOObjects
      // Filter criterias
      if(iOObject.isInstanceOf[FileObject])  // Java reflection library
      if(matches(iOObject.name)))
    yield iOObject
}

// Companion object
// Creates a singleton or static class
object FilterChecker {
  def apply(filter: String) = new FilterChecker(filter)
}