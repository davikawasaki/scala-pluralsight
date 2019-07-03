// Companion objects: abstract implementation of a class
// Properties:
// 1. same name as the class
// 2. start with the object keyword
// 3. live in same source file as class
// 4. access private members

object MathCompanion {
  def sum(a : Int, b : Int) : Int = a + b
  def getPrivateMember : Int = new MathCompanion().max  // whenever a function does not have args, parenthesis can be omitted
}

class MathCompanion {
  private val max = 100
}
