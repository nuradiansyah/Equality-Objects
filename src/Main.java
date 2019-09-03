import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.semanticweb.HermiT.structural.OWLAxioms;
import org.semanticweb.owlapi.reasoner.*;
import org.semanticweb.HermiT.*;
//import org.mindswap.pellet.*;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.RemoveAxiom;


class Main{
	public static void main(String args[]) throws IOException, OWLOntologyCreationException {
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		File folder = new File("owl");
		File[] listOfFiles = folder.listFiles();
		List<File> list_file = new ArrayList<File>();
		for (int i = 0; i < listOfFiles.length; i++) {
	      if (listOfFiles[i].isFile()) {
	        if(listOfFiles[i].getName().endsWith(".owl")){
	        	list_file.add(listOfFiles[i]);
	        }
	      } 
	    }
		
		System.out.println("List of File Names");
		for(int i = 0; i < list_file.size(); i++) {
			System.out.println((i+1) + " " + list_file.get(i).getName());
		}
		
		// Input the chosen OWL File Name
		System.out.println("Which file do you want to choose? Input the file number:");
		String file_num = br.readLine();	
		int choice_file_num = Integer.parseInt(file_num);
		String filename = list_file.get(choice_file_num-1).toString();
		
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		OWLOntology ontology = manager.loadOntologyFromOntologyDocument(new File(filename));
		
		OWLDataFactory df = 	OWLManager.getOWLDataFactory();
		
		
		Set<OWLNamedIndividual> set = new HashSet<OWLNamedIndividual>();
		List<OWLNamedIndividual> list = new LinkedList<OWLNamedIndividual>();
		
		set = ontology.getIndividualsInSignature();
		list.addAll(set);
		
		System.out.println("The list of individuals:");
		
		for (int i = 0; i<list.size(); i++) {
			String individual = list.get(i).getIRI().getFragment();
			System.out.println((i + 1) + ". " + individual);
		}
		
//		Set<OWLAxiom> axSet = new HashSet<OWLAxiom>();
//		List<OWLAxiom> axList = new LinkedList<OWLAxiom>();
//		
//		axSet = ontology.getAxioms();
//		axList.addAll(axSet);
//		
//		
//		System.out.println("The list of axioms:");
//		
//		for (int i = 0; i<axList.size(); i++) {
//			String individual = axList.get(i).toString();
//			System.out.println((i + 1) + ". " + individual);
//		}
		
		System.out.println();
		
		System.out.println("Please select two individuals you want to compare!");
		String line = "";
		while((line = br.readLine()) != null){
			String[] input = line.split(" and ");
			int num1 = Integer.parseInt(input[0]);
			int num2 = Integer.parseInt(input[1]);
			OWLNamedIndividual ind1 = list.get(num1-1);
			OWLNamedIndividual ind2 = list.get(num2-1);
		
			
			IRI iri1 = ontology.getOntologyID().getOntologyIRI();
			OWLClass clsA = df.getOWLClass(IRI.create(iri1 + "#A"));
			OWLAxiom axiom1 = df.getOWLClassAssertionAxiom(clsA, ind1);
			OWLAxiom axiom2 = df.getOWLClassAssertionAxiom(clsA, ind2);
			
			AddAxiom addAxiom = new AddAxiom(ontology,axiom1);
			manager.applyChange(addAxiom);
			
			Reasoner hermit = new Reasoner(ontology);
			System.out.println(hermit.isEntailed(axiom2));
		
			
			RemoveAxiom removeAxiom = new RemoveAxiom(ontology,axiom1);
			manager.applyChange(removeAxiom);

			System.out.println("Please select two individuals you want to compare!");
		}
	}
}