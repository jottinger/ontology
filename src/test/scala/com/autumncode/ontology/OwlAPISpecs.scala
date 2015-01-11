package com.autumncode.ontology

import org.junit.runner.RunWith
import org.specs2.mutable.SpecificationLike
import org.specs2.runner.JUnitRunner

@RunWith(classOf[JUnitRunner])
class OwlAPISpecs extends SpecificationLike {
  "model" should {
    "have six elements" in {
      DataModel.data.size mustEqual 6
    }
  }
  "map" in  {
    val o=new Ontology("http://autumncode.com/ontologies/Person")
    o.modelData(DataModel.data)
    println(o.toString)
    success
  }
}
