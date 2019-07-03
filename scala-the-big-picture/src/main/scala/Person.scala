object Person {
  def apply(firstName: String, lastName: String) = new Person(firstName, lastName)
  // create new instances without new keyword (i.e. val joe = Person("joe", "williams")
}

class Person(firstName : String, lastName : String) {
  def getName : String = s"$firstName $lastName"
}

// Case classes: used for immutable modeling. Properties:
// 1. Has a default apply() method which handles object construction on companion objects
// 2. Allows immutable arguments in parameter list
// 3. Has a copy method to make modified copies
// 4. Has, by default, hashCode, equals and toString methods
// 5. Has the right data structure for pattern matching
// Example for copy:
// val newScalaCourse = scalaCourse.copy(title = "New Scala Course")
case class Course(title : String, author : String)