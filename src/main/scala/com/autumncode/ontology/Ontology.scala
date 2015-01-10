package com.autumncode.ontology

import java.io.File

import org.semanticweb.owlapi.apibinding.OWLManager
import org.semanticweb.owlapi.io.StringDocumentTarget
import org.semanticweb.owlapi.model.{AddAxiom, IRI}
import org.semanticweb.owlapi.util.AutoIRIMapper

import scala.collection.JavaConversions._

class Ontology(NS: String) {

  import com.autumncode.ontology.Implicits._

  val m = OWLManager.createOWLOntologyManager
  m.addIRIMapper(new AutoIRIMapper(new File("materializedOntologies"), true))
  val df = m.getOWLDataFactory
  val xsd = "http://www.w3.org/2001/XMLSchema#"
  val baseIRI: IRI = s"$NS#"
  val o = m.createOntology(baseIRI)
  val topProperty = df.getOWLTopDataProperty


  def getOntClass(name: String) = {
    val r = df.getOWLClass(baseIRI + name)
    // do we need to do anything to this?
    r
  }

  def getOntDataProperty(name: String) = {
    val r = df.getOWLDataProperty(baseIRI + name)
    // anything needed here?
    r
  }

  def getOntObjectProperty(name: String) = {
    val r = df.getOWLObjectProperty(baseIRI + name)
    // anything needed here?
    r
  }

  override def toString: String = {
    val target = new StringDocumentTarget
    m.saveOntology(o, target)
    target.toString
  }

  def modelData(data: Map[Int, Node]): Unit = {
    data.foreach(d => {
      val (id, node) = d
      // create the node reference
      if (node.p != 0) {
        // this is an is-A relationship
        val clazz = getOntClass(node.name)

        val parent = getOntClass(data(node.p).name)
        m.applyChanges(List(
          new AddAxiom(o, df.getOWLDeclarationAxiom(clazz)),
          new AddAxiom(o, df.getOWLSubClassOfAxiom(clazz, parent))
        ))
      } else {
        if (node.r != 0) {
          // this is a has-A relationship
          val parent = getOntClass(data(node.r).name)
          // think head has a nose = parent is head
          // clazz is nose
          val clazz = getOntClass(node.name)
          // hasClazz is the data property
          val hasClazz = getOntObjectProperty("has" + node.name)
          val hasClazzSomeClazz = df.getOWLObjectSomeValuesFrom(hasClazz, clazz)
          // note: we do NOTHING with typed fields
          m.applyChanges(List(
            new AddAxiom(o, df.getOWLSubClassOfAxiom(parent, hasClazzSomeClazz))
          ))
        }
      }
    }

    )
  }

}