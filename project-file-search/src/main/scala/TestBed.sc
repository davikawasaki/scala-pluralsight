object TestBed {
  println("Welcome to the Scala Worksheet")
  var dictionary = Map("Justin" -> "An awesome pluralsight author!")
  dictionary = dictionary + ("Pluralsight" -> "www.pluralsight.com")
  dictionary = dictionary + ("Pluralsight" -> "A great place to learn new things!")
  dictionary("Justin")  // if it doesn't exists, it throws a NoSuchElementException
  dictionary contains "Pluralsight"  // check key existence in the map
  dictionary getOrElse("Oops", "No value found")
}