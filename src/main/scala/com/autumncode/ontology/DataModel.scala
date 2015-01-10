package com.autumncode.ontology

object DataModel {
  val data: Map[Int, Node] = Map(
    1 -> Node("Person", 0, 0),
    2 -> Node("Employee", 1, 0),
    3 -> Node("Name", 0, 1),
    4 -> Node("FirstName", 0, 3, Some("string")),
    5 -> Node("LastName", 0, 3, Some("string")),
    6 -> Node("BirthDay", 0, 1, Some("dateTime")))
}
