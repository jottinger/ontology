import java.io.File

import org.semanticweb.owlapi.apibinding.OWLManager
import org.semanticweb.owlapi.io.SystemOutDocumentTarget
import org.semanticweb.owlapi.model.{AddAxiom, IRI, OWLOntologyChange}
import org.semanticweb.owlapi.util.AutoIRIMapper
import org.semanticweb.owlapi.vocab.OWL2Datatype

import scala.collection.JavaConversions._

case class Node(name: String, p: Int, r: Int, dataType: Option[String] = None)

object Test1 {
  val m = OWLManager.createOWLOntologyManager
  m.addIRIMapper(new AutoIRIMapper(new File("materializedOntologies"), true))
  val df = m.getOWLDataFactory
  val source = "http://autumncode.com/ontologies/Person"
  val xsd = "http://www.w3.org/2001/XMLSchema#"
  val NS = s"$source#"
  val baseIRI: IRI = NS
  val o = m.createOntology(baseIRI)

  implicit def StringToIRI(iri: String): IRI = {
    IRI.create(iri)
  }

  def getOntClass(name: String) = {
    val r = df.getOWLClass(NS + name)
    r
  }

  def getOntProperty(name: String) = {
    df.getOWLDataProperty(NS + name)
  }

  val data: Map[Int, Node] = Map(
    1 -> Node("Person", 0, 0),
    2 -> Node("Patient", 1, 0),
    3 -> Node("Name", 0, 1),
    4 -> Node("FirstName", 0, 3, Some("string")),
    5 -> Node("LastName", 0, 3, Some("string")),
    6 -> Node("BirthDay", 0, 1, Some("dateTime")))

  def main(args: Array[String]) {
    data.foreach(n => {
      val node = n._2
      println(node)
      if (node.r != 0) {
        val clazz = getOntProperty(n._2.name)
        // this is a property
        val parent = getOntClass(data(node.r).name)
        val hasA = getOntProperty("has" + node.name)
        m.applyChanges(List[OWLOntologyChange](
          new AddAxiom(o, df.getOWLDeclarationAxiom(parent)),
          new AddAxiom(o, df.getOWLDeclarationAxiom(clazz)),
          new AddAxiom(o, df.getOWLDeclarationAxiom(hasA)),
          new AddAxiom(o, df.getOWLSubDataPropertyOfAxiom(hasA, df.getOWLTopDataProperty)),
          new AddAxiom(o, df.getOWLDataPropertyDomainAxiom(clazz, parent))
        ))
        node.dataType match {
          case s: Some[String] =>
            val datatype = OWL2Datatype.XSD_STRING.getDatatype(df)

            val dataRange = df.getOWLDatatypeRestriction(datatype)
            val restriction = df.getOWLDataExactCardinality(1, clazz)
            val expression = df.getOWLDataSomeValuesFrom(clazz, dataRange)
            m.applyChanges(List(
              new AddAxiom(o, df.getOWLSubClassOfAxiom(parent, expression)),
              new AddAxiom(o, df.getOWLSubClassOfAxiom(parent, restriction))
            ))
          case None =>
        }
      } else if (node.p != 0) {
        val clazz = getOntClass(n._2.name)
        // it's got a parent node, thus this is a subclass.
        val parent = getOntClass(data(node.p).name)
        m.applyChanges(List[OWLOntologyChange](
          new AddAxiom(o, df.getOWLDeclarationAxiom(clazz)),
          new AddAxiom(o, df.getOWLDeclarationAxiom(parent)),
          new AddAxiom(o, df.getOWLSubClassOfAxiom(clazz, parent))
        )
        )
      }
    })

    val target = new SystemOutDocumentTarget
    m.saveOntology(o, target)
  }

}
