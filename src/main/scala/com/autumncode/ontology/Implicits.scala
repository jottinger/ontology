package com.autumncode.ontology

import org.semanticweb.owlapi.model.IRI

object Implicits {
  implicit def StringToIRI(iri: String): IRI = {
    IRI.create(iri)
  }
}
