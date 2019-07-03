# Scala: The Big Picture

Pluralsight [Scala course](https://app.pluralsight.com/library/courses/scala-big-picture/table-of-contents) that contains small snippets with basic language functionalities.

## Table of Contents

* [Usage](#usage)
* [Language Functionalities](#language-functionalities)
    * [Class and private attributes](#class-and-private-attributes)
    * [Object Companion](#object-companion)
    * [Object apply constructor](#object-apply-constructor)
    * [Case classes](#case-classes)

## Usage

All code was developed using Scala inside [Jetbrains IntelliJ IDEA](https://www.jetbrains.com/idea/), which has the right plugins to start developing and testing (e.g. SBT shell console). In order to start working with Scala in this IDE environment, some [extra configurations](https://docs.scala-lang.org/getting-started-intellij-track/getting-started-with-scala-in-intellij.html) are necessary.

## Language Functionalities
### [Class and private attributes](https://github.com/davikawasaki/scala-pluralsight/blob/master/scala-the-big-picture/src/main/scala/Employee.scala)

```scala
class Employee {
  private var salary: Int = 100

  def getSalary() = salary
  def setSalary(newSalary : Int) = {
    salary = newSalary
  }
}
```

### [Object Companion](https://github.com/davikawasaki/scala-pluralsight/blob/master/scala-the-big-picture/src/main/scala/MathCompanion.scala)

```scala
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
```

### [Object apply constructor](https://github.com/davikawasaki/scala-pluralsight/blob/master/scala-the-big-picture/src/main/scala/Person.scala)

```scala
object Person {
  def apply(firstName: String, lastName: String) = new Person(firstName, lastName)
  // create new instances without new keyword (i.e. val joe = Person("joe", "williams")
}

class Person(firstName : String, lastName : String) {
  def getName : String = s"$firstName $lastName"
}
```

### [Case classes](https://github.com/davikawasaki/scala-pluralsight/blob/master/scala-the-big-picture/src/main/scala/Person.scala)

```scala
// Case classes: used for immutable modeling. Properties:
// 1. Has a default apply() method which handles object construction on companion objects
// 2. Allows immutable arguments in parameter list
// 3. Has a copy method to make modified copies
// 4. Has, by default, hashCode, equals and toString methods
// 5. Has the right data structure for pattern matching
// Example for copy:
// val newScalaCourse = scalaCourse.copy(title = "New Scala Course")
case class Course(title : String, author : String)
```