/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Mobility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.graphstream.algorithm.Dijkstra;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.*;
import org.graphstream.stream.ProxyPipe;
import org.graphstream.ui.view.Viewer;

import Mobility.MobilityMap.Direzione;
import Vanet.NodoMacchina;
import Vanet.NodoSemaforo.StatoSemaforo;

/**
 *
 * @author afsantamaria
 */
public class MobilityMap {

	public Graph cityRoadMap;
	Dijkstra dijkstra;
	Dijkstra dijkstra_avg_speed;
	public HashMap<String, car_node> vehicles = new HashMap<String, car_node>();
	public HashMap<String, sem_node> semaphore = new HashMap<String, sem_node>();
	ProxyPipe pipe;

	public enum Direzione {
		EST, OVEST, NORD, SUD
	}

	public Graph getCityRoadMap() {
		return cityRoadMap;
	}

	public void setCityRoadMap(Graph cityRoadMap) {
		this.cityRoadMap = cityRoadMap;
	}

	public MobilityMap(int numMappa) {
		System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");

		createCityMap(numMappa);

		// Edge lengths are stored in an attribute called "length"
		// The length of a path is the sum of the lengths of its edges
		dijkstra = new Dijkstra(Dijkstra.Element.EDGE, null, "length");
		dijkstra_avg_speed = new Dijkstra(Dijkstra.Element.EDGE, null, "avgSpeed");

		// Compute the shortest paths in g from A to all nodes
		dijkstra.init(cityRoadMap);
		dijkstra.setSource(cityRoadMap.getNode("A"));
		dijkstra.compute();

		// Print the lengths of all the shortest paths
		for (Node node : cityRoadMap) {
			System.out.printf("%s->%s:%10.2f%n", dijkstra.getSource(), node, dijkstra.getPathLength(node));

		}

		// Color in blue all the nodes on the shortest path form A to B
		for (Node node : dijkstra.getPathNodes(cityRoadMap.getNode("E"))) {
			// node.addAttribute("ui.style", "fill-color: white; size:
			// 25px,25px;");
		}

		// Color in red all the edges in the shortest path tree
		for (Edge edge : dijkstra.getTreeEdges()) {
			// edge.addAttribute("ui.style", "fill-color: red;");
		}

		// Print the shortest path from A to B
		System.out.println(dijkstra.getPath(cityRoadMap.getNode("E")));

		// Build a list containing the nodes in the shortest path from A to B
		// Note that nodes are added at the beginning of the list
		// because the iterator traverses them in reverse order, from B to A
		ArrayList<Node> list1 = new ArrayList<Node>();
		for (Node node : dijkstra.getPathNodes(cityRoadMap.getNode("E"))) {
			list1.add(0, node);
		}

		dijkstra_avg_speed.init(cityRoadMap);
		dijkstra_avg_speed.setSource(cityRoadMap.getNode("A"));
		dijkstra_avg_speed.compute();
		System.out.println("\n\n...Calcolo dei cammini in base alle velocità medie...");
		// Print the lengths of all the shortest paths
		for (Node node : cityRoadMap) {
			System.out.printf("%s->%s:%10.2f%n", dijkstra_avg_speed.getSource(), node,
					dijkstra_avg_speed.getPathLength(node));
		}

		for (Node n : cityRoadMap) {
			n.addAttribute("label", n.getId());
//			if (n.getId().equals("T")) {
//				// || n.getId().equals("F")
//				// || n.getId().equals("E")
//				// || n.getId().equals("H")) {
//				if (n.getAttribute("ui.style") == null) {
//					n.addAttribute("ui.style", "fill-color: red; size: 30px,30px;");
//				} else {
//					n.setAttribute("ui.style", "fill-color: red; size: 30px,30px;");
//				}
			if (n.getAttribute("ui.style") == null) {
				n.addAttribute("ui.style", "fill-color: gray; size: 20px,20px;");
			} else {
				n.setAttribute("ui.style", "fill-color: gray; size: 20px,20px;");
			}
		}

		// Viewer viewer = cityRoadMap.display();
		// viewer.disableAutoLayout();
		/*
		 * Viewer viewer; for (int i = 0; i < 100; i++) {
		 * 
		 * cityRoadMap.getNode("Car").setAttribute("xy",500+(i*5),250); viewer =
		 * cityRoadMap.display(); viewer.disableAutoLayout();
		 * 
		 * try { Thread.sleep(100); } catch (InterruptedException ex) {
		 * Logger.getLogger(MobilityMap.class.getName()).log(Level.SEVERE, null,
		 * ex); } }
		 */
		Viewer v = cityRoadMap.display();
		v.disableAutoLayout();
		pipe = v.newViewerPipe();
		pipe.addAttributeSink(cityRoadMap);

		/*
		 * for (int i = 0; i < 100; i++) { try { Thread.sleep(100); pipe.pump();
		 * cityRoadMap.getNode("Car").setAttribute("xy",500+i,250+i); } catch
		 * (InterruptedException ex) {
		 * Logger.getLogger(MobilityMap.class.getName()).log(Level.SEVERE, null,
		 * ex); } }
		 */
		for (Node node : cityRoadMap) {

			System.out.println(node.toString() + " Posizione (x,y) :" + node.getNumber("x") + ","
					+ ((Object[]) node.getAttribute("xy"))[1]);
		}

	}

	/**
	 * Creamo la struttura della citta utilizzando una libreria graphStream per
	 * la gestione delle strade 1.0 Creamo il grafo della rete 2.0 Associamo la
	 * struttura del grafo agli oggetti java
	 */
	public void createCityMap(int numMappa) {
		// Node represents the crossway among road
		cityRoadMap = new SingleGraph("ColdRiver");

		if (numMappa==1){
			cityRoadMap.addNode("A");
			cityRoadMap.getNode("A").setAttribute("xy", 250, 300);
			cityRoadMap.addNode("B");
			cityRoadMap.getNode("B").setAttribute("xy", 0, 300);
			cityRoadMap.addNode("C");
			cityRoadMap.getNode("C").setAttribute("xy", 250, 0);
			cityRoadMap.addNode("D");
			cityRoadMap.getNode("D").setAttribute("xy", 650, 300);
			cityRoadMap.addNode("I");
			cityRoadMap.getNode("I").setAttribute("xy", 450, 300);
			cityRoadMap.addNode("E");
			cityRoadMap.getNode("E").setAttribute("xy", 250, 600);
			cityRoadMap.addNode("F");
			cityRoadMap.getNode("F").setAttribute("xy", 650, 0);
			cityRoadMap.addNode("G");
			cityRoadMap.getNode("G").setAttribute("xy", 650, 600);
			cityRoadMap.getNode("G").setAttribute("color","red");
			cityRoadMap.addNode("H");
			cityRoadMap.getNode("H").setAttribute("xy", 900, 300);
			cityRoadMap.getNode("H").setAttribute("color","yellow");
			cityRoadMap.addNode("L");
			cityRoadMap.getNode("L").setAttribute("xy", 450, 0);
			cityRoadMap.addNode("M");
			cityRoadMap.getNode("M").setAttribute("xy", 450, 150);
			cityRoadMap.getNode("M").setAttribute("color","gray");
			cityRoadMap.addNode("N");
			cityRoadMap.getNode("N").setAttribute("xy", 450, -150);
			
	
			cityRoadMap.addEdge("BA", "B", "A",true).addAttribute("length", 200);
			cityRoadMap.addEdge("AC", "A", "C",true).addAttribute("length", 200);
			// cityRoadMap.addEdge("AD", "A", "D").addAttribute("length", 200);
			cityRoadMap.addEdge("AI", "A", "I",true).addAttribute("length",200); /*
																			 * aggiungiamo un nodo "fittizio" I in modo tale da
																			 * sistemare il verso di percorrenza dell'arco, in
																			 * quanto AD e DA avevano un solo verso di percorrenza,
																			 * e invece ne servono 2. Abbiamo quindi aggiunto il
																			 * nodo I in mezzo all'arco in modo da impostare AI =
																			 * OVEST e ID = EST
																			 */
			cityRoadMap.addEdge("ID", "I", "D",true).addAttribute("length", 200);
			cityRoadMap.addEdge("EA", "E", "A",true).addAttribute("length", 200);
			cityRoadMap.addEdge("DG", "D", "G",true).addAttribute("length", 200);
			cityRoadMap.addEdge("FD", "F", "D",true).addAttribute("length", 200);
			cityRoadMap.addEdge("DH", "D", "H",true).addAttribute("length", 200);
			cityRoadMap.addEdge("CL", "C", "L",true).addAttribute("length", 200);
			cityRoadMap.addEdge("LM", "L", "M",true).addAttribute("length", 200);
			cityRoadMap.addEdge("LF", "L", "F",true).addAttribute("length", 200);
			cityRoadMap.addEdge("NL", "N", "L",true).addAttribute("length", 200);
			// cityRoadMap.addEdge("BH", "B", "H").addAttribute("length", 200);
			// cityRoadMap.addEdge("AG", "A", "G").addAttribute("length", 200);
			// cityRoadMap.addEdge("GC", "G", "C").addAttribute("length", 200);
			// cityRoadMap.addEdge("CF", "C", "F").addAttribute("length", 100);
			// cityRoadMap.addEdge("GF", "G", "F").addAttribute("length", 200);
			// cityRoadMap.addEdge("CD", "C", "D").addAttribute("length", 100);
			// cityRoadMap.addEdge("DE", "D", "E").addAttribute("length", 100);
			// cityRoadMap.addEdge("FE", "F", "E").addAttribute("length", 120);
			// cityRoadMap.addEdge("HC", "H", "C").addAttribute("length", 100);
	
			cityRoadMap.getEdge("BA").addAttribute("avgSpeed", 4.0);
			cityRoadMap.getEdge("AC").addAttribute("avgSpeed", 4.0);
	//		cityRoadMap.getEdge("AD").addAttribute("avgSpeed", 4.0);
			cityRoadMap.getEdge("AI").addAttribute("avgSpeed", 4.0);
			cityRoadMap.getEdge("ID").addAttribute("avgSpeed", 4.0);
			cityRoadMap.getEdge("EA").addAttribute("avgSpeed", 4.0);
			cityRoadMap.getEdge("DG").addAttribute("avgSpeed", 4.0);
			cityRoadMap.getEdge("FD").addAttribute("avgSpeed", 4.0);
			cityRoadMap.getEdge("DH").addAttribute("avgSpeed", 4.0);
			cityRoadMap.getEdge("CL").addAttribute("avgSpeed", 4.0);
			cityRoadMap.getEdge("LM").addAttribute("avgSpeed", 4.0);
			cityRoadMap.getEdge("LF").addAttribute("avgSpeed", 4.0);
			cityRoadMap.getEdge("NL").addAttribute("avgSpeed", 4.0);
			// cityRoadMap.getEdge("BH").addAttribute("avgSpeed", 9.2);
			// cityRoadMap.getEdge("AG").addAttribute("avgSpeed", 10.3);
			// cityRoadMap.getEdge("GC").addAttribute("avgSpeed", 8.1);
			// cityRoadMap.getEdge("CF").addAttribute("avgSpeed", 7.25);
			// cityRoadMap.getEdge("GF").addAttribute("avgSpeed", 9.5);
			// cityRoadMap.getEdge("CD").addAttribute("avgSpeed", 8.5);
			// cityRoadMap.getEdge("DE").addAttribute("avgSpeed", 9.3);
			// cityRoadMap.getEdge("FE").addAttribute("avgSpeed", 8.15);
			// cityRoadMap.getEdge("HC").addAttribute("avgSpeed", 9.2);
	
			cityRoadMap.getEdge("BA").addAttribute("Direction", Direzione.OVEST);
			cityRoadMap.getEdge("AC").addAttribute("Direction", Direzione.SUD);
	//		cityRoadMap.getEdge("AD").addAttribute("Direction", Direzione.EST);
			cityRoadMap.getEdge("AI").addAttribute("Direction", Direzione.EST);
			cityRoadMap.getEdge("EA").addAttribute("Direction", Direzione.NORD);
			
			cityRoadMap.getEdge("ID").addAttribute("Direction", Direzione.OVEST);
			cityRoadMap.getEdge("DG").addAttribute("Direction", Direzione.NORD);
			cityRoadMap.getEdge("DH").addAttribute("Direction", Direzione.EST);
			cityRoadMap.getEdge("FD").addAttribute("Direction", Direzione.SUD);
			cityRoadMap.getEdge("CL").addAttribute("Direction", Direzione.OVEST);
			cityRoadMap.getEdge("LM").addAttribute("Direction", Direzione.NORD);
			cityRoadMap.getEdge("LF").addAttribute("Direction", Direzione.EST);
			cityRoadMap.getEdge("NL").addAttribute("Direction", Direzione.SUD);
		}
		else if(numMappa==2){
			cityRoadMap.addNode("A");
			cityRoadMap.getNode("A").setAttribute("xy", 0, 900);
			
			cityRoadMap.addNode("B");
			cityRoadMap.getNode("B").setAttribute("xy", 400, 950);
			cityRoadMap.getNode("B").setAttribute("color","red");
			
			cityRoadMap.addNode("C");
			cityRoadMap.getNode("C").setAttribute("xy",800, 850);
			
			cityRoadMap.addNode("D");
			cityRoadMap.getNode("D").setAttribute("xy",-300, 650);
			
			cityRoadMap.addNode("E");
			cityRoadMap.getNode("E").setAttribute("xy", 0, 650);
			
			cityRoadMap.addNode("F");
			cityRoadMap.getNode("F").setAttribute("xy", 400, 650);
			
			cityRoadMap.addNode("J");
			cityRoadMap.getNode("J").setAttribute("xy", 600, 650);
			
			cityRoadMap.addNode("G");
			cityRoadMap.getNode("G").setAttribute("xy", 800, 650);
			
			cityRoadMap.addNode("H");
			cityRoadMap.getNode("H").setAttribute("xy", 1000, 650);
			cityRoadMap.getNode("H").setAttribute("color","yellow");
			
			cityRoadMap.addNode("I");
			cityRoadMap.getNode("I").setAttribute("xy", -200, 450);
			cityRoadMap.getNode("I").setAttribute("color","gray");
			
			cityRoadMap.addNode("L");
			cityRoadMap.getNode("L").setAttribute("xy", 0,450);
			
			cityRoadMap.addNode("M");
			cityRoadMap.getNode("M").setAttribute("xy", -200, 250);
			
			cityRoadMap.addNode("N");
			cityRoadMap.getNode("N").setAttribute("xy", 0, 250);
			
			cityRoadMap.addNode("K");
			cityRoadMap.getNode("K").setAttribute("xy", 200, 250);
			
			cityRoadMap.addNode("O");
			cityRoadMap.getNode("O").setAttribute("xy", 400, 250);
			
			cityRoadMap.addNode("P");
			cityRoadMap.getNode("P").setAttribute("xy", 800, 250);
			
			cityRoadMap.addNode("Q");
			cityRoadMap.getNode("Q").setAttribute("xy", 1000, 250);
			
			cityRoadMap.addNode("R");
			cityRoadMap.getNode("R").setAttribute("xy", 1200,250);
			cityRoadMap.getNode("R").setAttribute("color","pink");
			
			cityRoadMap.addNode("S");
			cityRoadMap.getNode("S").setAttribute("xy", 0, 50);
			cityRoadMap.getNode("S").setAttribute("color","cyan");
			
			cityRoadMap.addNode("T");
			cityRoadMap.getNode("T").setAttribute("xy", 400, 50);
			
			cityRoadMap.addNode("U");
			cityRoadMap.getNode("U").setAttribute("xy", 1000, 50);
			cityRoadMap.getNode("U").setAttribute("color","orange");
			
			cityRoadMap.addNode("Y");
			cityRoadMap.getNode("Y").setAttribute("xy", 400, 450);
			
	
			cityRoadMap.addEdge("AE", "E", "A",true).addAttribute("length", 200);
			cityRoadMap.addEdge("BF", "B", "F",true).addAttribute("length", 200);
			cityRoadMap.addEdge("GC", "G", "C",true).addAttribute("length", 200);
			cityRoadMap.addEdge("ED", "E", "D",true).addAttribute("length",200); /*
																			 * aggiungiamo un nodo "fittizio" I in modo tale da
																			 * sistemare il verso di percorrenza dell'arco, in
																			 * quanto AD e DA avevano un solo verso di percorrenza,
																			 * e invece ne servono 2. Abbiamo quindi aggiunto il
																			 * nodo I in mezzo all'arco in modo da impostare AI =
																			 * OVEST e ID = EST
																			 */
			cityRoadMap.addEdge("FE", "F", "E",true).addAttribute("length", 200);
			cityRoadMap.addEdge("JF", "J", "F",true).addAttribute("length", 200);
			cityRoadMap.addEdge("GJ", "G", "J",true).addAttribute("length", 200);
			cityRoadMap.addEdge("HG", "H", "G",true).addAttribute("length", 200);
			cityRoadMap.addEdge("IL", "I", "L",true).addAttribute("length", 200);
			cityRoadMap.addEdge("LE", "L", "E",true).addAttribute("length", 200);
			cityRoadMap.addEdge("NL", "N", "L",true).addAttribute("length", 200);
			cityRoadMap.addEdge("NM", "N", "M",true).addAttribute("length", 200);
			cityRoadMap.addEdge("KN", "K", "N",true).addAttribute("length", 200);
			cityRoadMap.addEdge("OK", "O", "K",true).addAttribute("length", 200);
			cityRoadMap.addEdge("FY", "F", "Y",true).addAttribute("length", 200);
			cityRoadMap.addEdge("YO", "Y", "O",true).addAttribute("length", 200);
			cityRoadMap.addEdge("PG", "P", "G",true).addAttribute("length", 200);
			cityRoadMap.addEdge("QP", "Q", "P",true).addAttribute("length", 200);
			cityRoadMap.addEdge("RQ", "R", "Q",true).addAttribute("length", 200);
			cityRoadMap.addEdge("SN", "S", "N",true).addAttribute("length", 200);
			cityRoadMap.addEdge("OT", "O", "T",true).addAttribute("length", 200);
			cityRoadMap.addEdge("UQ", "U", "Q",true).addAttribute("length", 200);
			cityRoadMap.addEdge("PO", "P", "O",true).addAttribute("length", 200);

	
			
			cityRoadMap.getEdge("AE").addAttribute("avgSpeed", 4.0);
			cityRoadMap.getEdge("BF").addAttribute("avgSpeed", 4.0);
			cityRoadMap.getEdge("GC").addAttribute("avgSpeed", 4.0);
			cityRoadMap.getEdge("ED").addAttribute("avgSpeed", 4.0);
			cityRoadMap.getEdge("FE").addAttribute("avgSpeed", 4.0);
			cityRoadMap.getEdge("JF").addAttribute("avgSpeed", 4.0);
			cityRoadMap.getEdge("GJ").addAttribute("avgSpeed", 4.0);
			cityRoadMap.getEdge("HG").addAttribute("avgSpeed", 4.0);
			cityRoadMap.getEdge("IL").addAttribute("avgSpeed", 4.0); 
			cityRoadMap.getEdge("LE").addAttribute("avgSpeed", 4.0);
			cityRoadMap.getEdge("NL").addAttribute("avgSpeed", 4.0); 
			cityRoadMap.getEdge("NM").addAttribute("avgSpeed", 4.0);
			cityRoadMap.getEdge("KN").addAttribute("avgSpeed", 4.0);
			cityRoadMap.getEdge("OK").addAttribute("avgSpeed", 4.0);
			cityRoadMap.getEdge("FY").addAttribute("avgSpeed", 4.0);
			cityRoadMap.getEdge("YO").addAttribute("avgSpeed", 4.0);
			cityRoadMap.getEdge("PG").addAttribute("avgSpeed", 4.0);
			cityRoadMap.getEdge("QP").addAttribute("avgSpeed", 4.0); 
			cityRoadMap.getEdge("RQ").addAttribute("avgSpeed", 4.0);
			cityRoadMap.getEdge("SN").addAttribute("avgSpeed", 4.0);
			cityRoadMap.getEdge("OT").addAttribute("avgSpeed", 4.0);
			cityRoadMap.getEdge("UQ").addAttribute("avgSpeed", 4.0);
			cityRoadMap.getEdge("PO").addAttribute("avgSpeed", 4.0);
			
			
//			cityRoadMap.getEdge("AE").addAttribute("Direction", Direzione.OVEST);
//			cityRoadMap.getEdge("BF").addAttribute("Direction", Direzione.OVEST);
			cityRoadMap.getEdge("GC").addAttribute("Direction", Direzione.NORD);
//			cityRoadMap.getEdge("ED").addAttribute("Direction", Direzione.OVEST);
			cityRoadMap.getEdge("FE").addAttribute("Direction", Direzione.OVEST);
			cityRoadMap.getEdge("JF").addAttribute("Direction", Direzione.EST);
			cityRoadMap.getEdge("GJ").addAttribute("Direction", Direzione.OVEST);
			cityRoadMap.getEdge("HG").addAttribute("Direction", Direzione.EST);
//			cityRoadMap.getEdge("IL").addAttribute("Direction", Direzione.OVEST); 
//			cityRoadMap.getEdge("LE").addAttribute("Direction", Direzione.OVEST);
			cityRoadMap.getEdge("NL").addAttribute("Direction", Direzione.NORD); 
			cityRoadMap.getEdge("NM").addAttribute("Direction", Direzione.OVEST);
			cityRoadMap.getEdge("KN").addAttribute("Direction", Direzione.EST);
			cityRoadMap.getEdge("OK").addAttribute("Direction", Direzione.OVEST);
			cityRoadMap.getEdge("FY").addAttribute("Direction", Direzione.SUD);
			cityRoadMap.getEdge("YO").addAttribute("Direction", Direzione.NORD);
			cityRoadMap.getEdge("PG").addAttribute("Direction", Direzione.SUD);
			cityRoadMap.getEdge("QP").addAttribute("Direction", Direzione.OVEST);
			cityRoadMap.getEdge("RQ").addAttribute("Direction", Direzione.EST);
			cityRoadMap.getEdge("SN").addAttribute("Direction", Direzione.SUD);
			cityRoadMap.getEdge("OT").addAttribute("Direction", Direzione.SUD);
			cityRoadMap.getEdge("UQ").addAttribute("Direction", Direzione.SUD);
			cityRoadMap.getEdge("PO").addAttribute("Direction", Direzione.EST);
			
			
		}
		else if (numMappa==3){
			cityRoadMap.addNode("A");
      		cityRoadMap.getNode("A").setAttribute("xy", 200,-200);
      		cityRoadMap.addNode("B");
      		cityRoadMap.getNode("B").setAttribute("xy", 1, 1);
      		cityRoadMap.addNode("C");
      		cityRoadMap.getNode("C").setAttribute("xy", 200, 400);
      		cityRoadMap.addNode("D");
      		cityRoadMap.getNode("D").setAttribute("xy", 0, 600);
      		cityRoadMap.addNode("I");
      		cityRoadMap.getNode("I").setAttribute("xy", 200, 600);
      		cityRoadMap.addNode("E");
      		cityRoadMap.getNode("E").setAttribute("xy", 200, 800);
      		cityRoadMap.addNode("F");
      		cityRoadMap.getNode("F").setAttribute("xy", 600, 0);
      		cityRoadMap.addNode("L");
      		cityRoadMap.getNode("L").setAttribute("xy", 400, 0);
      		cityRoadMap.addNode("M");
      		cityRoadMap.getNode("M").setAttribute("xy", 600, -200);
      		cityRoadMap.addNode("H");
      		cityRoadMap.getNode("H").setAttribute("xy", 600, 200);
      		cityRoadMap.addNode("W");
      		cityRoadMap.getNode("W").setAttribute("xy", 600, 400);
      		cityRoadMap.addNode("G");
      		cityRoadMap.getNode("G").setAttribute("xy", 600, 600);
      		cityRoadMap.addNode("P");
      		cityRoadMap.getNode("P").setAttribute("xy", 1100, -200);
      		cityRoadMap.addNode("Q");
      		cityRoadMap.getNode("Q").setAttribute("xy", 1100, 0);
      		cityRoadMap.addNode("R");
      		cityRoadMap.getNode("R").setAttribute("xy", 1100, 200);
      		cityRoadMap.addNode("S");
      		cityRoadMap.getNode("S").setAttribute("xy", 1100, 400);
      		cityRoadMap.addNode("T");
      		cityRoadMap.getNode("T").setAttribute("xy", 1100, 600);
      		cityRoadMap.addNode("O");
      		cityRoadMap.getNode("O").setAttribute("xy", 900, 400);
      		cityRoadMap.addNode("U");
      		cityRoadMap.getNode("U").setAttribute("xy", 900, 0);
      		cityRoadMap.addNode("V");
      		cityRoadMap.getNode("V").setAttribute("xy", 1300, 0);
      		cityRoadMap.addNode("Z");
      		cityRoadMap.getNode("Z").setAttribute("xy", 1500, 600);
      		cityRoadMap.addNode("K");
      		cityRoadMap.getNode("K").setAttribute("xy", 1500, 800);
      		cityRoadMap.addNode("Y");
      		cityRoadMap.getNode("Y").setAttribute("xy", 200, 0);
      		cityRoadMap.addNode("N");
      		cityRoadMap.getNode("N").setAttribute("xy", 1300, 400);

			cityRoadMap.addEdge("RQ", "R", "Q",true).addAttribute("length", 200);
			cityRoadMap.addEdge("HF", "H", "F",true).addAttribute("length", 200);
			cityRoadMap.addEdge("ID", "I", "D",true).addAttribute("length", 200);
			cityRoadMap.addEdge("AY", "A", "Y",true).addAttribute("length", 200);
			cityRoadMap.addEdge("BY", "B", "Y",true).addAttribute("length", 200);
			cityRoadMap.addEdge("YL", "Y", "L",true).addAttribute("length", 200);
			cityRoadMap.addEdge("YC", "Y", "C",true).addAttribute("length", 200);
			cityRoadMap.addEdge("CI", "C", "I",true).addAttribute("length", 200);
			cityRoadMap.addEdge("IE", "I", "E",true).addAttribute("length", 200);
			cityRoadMap.addEdge("LF", "L", "F",true).addAttribute("length", 200);
			cityRoadMap.addEdge("WC", "W", "C",true).addAttribute("length", 200);
			cityRoadMap.addEdge("WH", "W", "H",true).addAttribute("length", 200);
			cityRoadMap.addEdge("FM", "F", "M",true).addAttribute("length", 200);
			cityRoadMap.addEdge("FU", "F", "U",true).addAttribute("length", 200);
			cityRoadMap.addEdge("GW", "G", "W",true).addAttribute("length", 200);
			cityRoadMap.addEdge("GI", "G", "I",true).addAttribute("length", 200);
			cityRoadMap.addEdge("UQ", "U", "Q",true).addAttribute("length", 200);
			cityRoadMap.addEdge("OW", "O", "W",true).addAttribute("length", 200);
			cityRoadMap.addEdge("QP", "Q", "P",true).addAttribute("length", 200);
			cityRoadMap.addEdge("QV", "Q", "V",true).addAttribute("length", 200);
			cityRoadMap.addEdge("SR", "S", "R",true).addAttribute("length", 200);
			cityRoadMap.addEdge("SO", "S", "O",true).addAttribute("length", 200);
			cityRoadMap.addEdge("ZN", "Z", "N",true).addAttribute("length", 200);
			cityRoadMap.addEdge("NS", "N", "S",true).addAttribute("length", 200);
			cityRoadMap.addEdge("TS", "T", "S",true).addAttribute("length", 200);
			cityRoadMap.addEdge("TG", "T", "G",true).addAttribute("length", 200);
			cityRoadMap.addEdge("KT", "K", "T",true).addAttribute("length", 200);
			cityRoadMap.getEdge("RQ").addAttribute("avgSpeed", 4.0);
			cityRoadMap.getEdge("HF").addAttribute("avgSpeed", 4.0);
			cityRoadMap.getEdge("ID").addAttribute("avgSpeed", 4.0);
			cityRoadMap.getEdge("AY").addAttribute("avgSpeed", 4.0);
			cityRoadMap.getEdge("BY").addAttribute("avgSpeed", 4.0);
			cityRoadMap.getEdge("YL").addAttribute("avgSpeed", 4.0);
			cityRoadMap.getEdge("YC").addAttribute("avgSpeed", 4.0);
			cityRoadMap.getEdge("CI").addAttribute("avgSpeed", 4.0);
			cityRoadMap.getEdge("IE").addAttribute("avgSpeed", 4.0);
			cityRoadMap.getEdge("LF").addAttribute("avgSpeed", 4.0);
			cityRoadMap.getEdge("WC").addAttribute("avgSpeed", 4.0);
			cityRoadMap.getEdge("WH").addAttribute("avgSpeed", 4.0);
			cityRoadMap.getEdge("FM").addAttribute("avgSpeed", 4.0);
			cityRoadMap.getEdge("FU").addAttribute("avgSpeed", 4.0);
			cityRoadMap.getEdge("GW").addAttribute("avgSpeed", 4.0);
			cityRoadMap.getEdge("GI").addAttribute("avgSpeed", 4.0);
			cityRoadMap.getEdge("UQ").addAttribute("avgSpeed", 4.0);
			cityRoadMap.getEdge("OW").addAttribute("avgSpeed", 4.0);
			cityRoadMap.getEdge("QP").addAttribute("avgSpeed", 4.0);
			cityRoadMap.getEdge("QV").addAttribute("avgSpeed", 4.0);
			cityRoadMap.getEdge("SR").addAttribute("avgSpeed", 4.0);
			cityRoadMap.getEdge("SO").addAttribute("avgSpeed", 4.0);
			cityRoadMap.getEdge("ZN").addAttribute("avgSpeed", 4.0);
			cityRoadMap.getEdge("NS").addAttribute("avgSpeed", 4.0);
			cityRoadMap.getEdge("TS").addAttribute("avgSpeed", 4.0);
			cityRoadMap.getEdge("TG").addAttribute("avgSpeed", 4.0);
			cityRoadMap.getEdge("KT").addAttribute("avgSpeed", 4.0);
			//ricontrollare i lati
			cityRoadMap.getEdge("RQ").addAttribute("Direction", Direzione.NORD);
      		cityRoadMap.getEdge("HF").addAttribute("Direction", Direzione.NORD);
      		cityRoadMap.getEdge("ID").addAttribute("Direction", Direzione.OVEST);
      		cityRoadMap.getEdge("AY").addAttribute("Direction", Direzione.SUD);
      		cityRoadMap.getEdge("BY").addAttribute("Direction", Direzione.OVEST);
      		cityRoadMap.getEdge("YL").addAttribute("Direction", Direzione.EST);
      		cityRoadMap.getEdge("YC").addAttribute("Direction", Direzione.NORD);
      		cityRoadMap.getEdge("CI").addAttribute("Direction", Direzione.SUD);
      		cityRoadMap.getEdge("IE").addAttribute("Direction", Direzione.NORD);
      		cityRoadMap.getEdge("LF").addAttribute("Direction", Direzione.OVEST);
      		cityRoadMap.getEdge("WC").addAttribute("Direction", Direzione.OVEST);
      		cityRoadMap.getEdge("WH").addAttribute("Direction", Direzione.SUD);
      		cityRoadMap.getEdge("FM").addAttribute("Direction", Direzione.SUD);
      		cityRoadMap.getEdge("FU").addAttribute("Direction", Direzione.EST);
      		cityRoadMap.getEdge("GW").addAttribute("Direction", Direzione.NORD);
      		cityRoadMap.getEdge("GI").addAttribute("Direction", Direzione.OVEST);
      		cityRoadMap.getEdge("UQ").addAttribute("Direction", Direzione.OVEST);
      		cityRoadMap.getEdge("OW").addAttribute("Direction", Direzione.EST);
      		cityRoadMap.getEdge("QP").addAttribute("Direction", Direzione.SUD);
      		cityRoadMap.getEdge("QV").addAttribute("Direction", Direzione.EST);
      		cityRoadMap.getEdge("SR").addAttribute("Direction", Direzione.SUD);
      		cityRoadMap.getEdge("SO").addAttribute("Direction", Direzione.OVEST);
      		cityRoadMap.getEdge("NS").addAttribute("Direction", Direzione.EST);
      		cityRoadMap.getEdge("TS").addAttribute("Direction", Direzione.NORD);


			
		}
		

		for (Edge e : cityRoadMap.getEachEdge()) {
			e.addAttribute("label", "" + (int) e.getNumber("length"));
		}
	}

	public boolean validatePos(String id, double x, double y) {
		boolean res = true;
		for (Entry<String, car_node> entry : vehicles.entrySet()) {
			String key = entry.getKey();
			car_node car = (car_node) entry.getValue();
			if (!key.equals(id) && car.getX() == x && car.getY() == y) {
				res = false;
				NodoMacchina.frenata++;
				break;
			}
		}
		if (res == true) {
			car_node car = vehicles.get(id);
			car.setX(x);
			car.setY(y);
			
			// Test car nodes
			if (cityRoadMap.getNode(id) == null) {
				cityRoadMap.addNode(id);
				cityRoadMap.getNode(id).setAttribute("label", id);
				String color=cityRoadMap.getNode(car.getDestinazione()).getAttribute("color");
				cityRoadMap.getNode(id).setAttribute("ui.style", "fill-color: "+color+"; size: 10px,10px;");
//				switch (car.padre) {
//				case "B":
//					cityRoadMap.getNode(id).setAttribute("ui.style", "fill-color: red; size: 10px,10px;");
//					break;
//				case "C":
//					cityRoadMap.getNode(id).setAttribute("ui.style", "fill-color: yellow; size: 10px,10px;");
//					break;
//				default:
//					break;
//				}
			}

			cityRoadMap.getNode(id).setAttribute("xy", x, y);
			pipe.pump();
		}
		return res;
	}

	public void updateVehiclePos(String id, double x, double y) {
		car_node car = vehicles.get(id);
		car.setX(x);
		car.setY(y);

	}

	public void modificaColore(StatoSemaforo ns, StatoSemaforo eo, String nodoingresso) {
		if (ns == StatoSemaforo.ROSSO && eo == StatoSemaforo.ROSSO) {
			for (Node n : cityRoadMap) {
				if (n.getId().equals(nodoingresso))
					n.setAttribute("ui.style", "fill-color: red; size: 30px,30px;");
			}
		} else if (ns == StatoSemaforo.VERDE && eo == StatoSemaforo.ROSSO) {
			for (Node n : cityRoadMap) {
				if (n.getId().equals(nodoingresso))
					n.setAttribute("ui.style", "fill-color: green; size: 30px,30px;");
			}
		} else if (ns == StatoSemaforo.ROSSO && eo == StatoSemaforo.VERDE) {
			for (Node n : cityRoadMap) {
				if (n.getId().equals(nodoingresso))
					n.setAttribute("ui.style", "fill-color: blue; size: 30px,30px;");
			}
		} else {
			for (Node n : cityRoadMap) {
				if (n.getId().equals(nodoingresso))
					n.setAttribute("ui.style", "fill-color: grey; size: 30px,30px;");
			}
		}
	}
}
