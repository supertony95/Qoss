/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Vanet;

import Mobility.MobilityMap;
import Mobility.MobilityMap.Direzione;
import Mobility.sem_node;
import Vanet.NodoSemaforo.StatoSemaforo;
import base_simulator.Grafo;
import base_simulator.Messaggi;
import base_simulator.Nodo;
import base_simulator.canale;
import base_simulator.layers.LinkLayer;
import base_simulator.layers.NetworkLayer;
import base_simulator.layers.TransportLayer;
import base_simulator.layers.physicalLayer;
import base_simulator.scheduler;
import java.util.ArrayList;
import org.graphstream.algorithm.Dijkstra;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import reti_tlc_gruppo_0.nodo_host;

/**
 *
 * @author franco
 */
public class NodoMacchina extends nodo_host {

	public static int frenata=0;
    final String UPDATE_POSITION = "update_pos";
    final String START_ROAD_RUN = "start_road_run";
    final String WAITING_ROAD = "waiting_road";
    final String ARRIVAL_ROAD_NS= "arrival_road_ns";
    final String LEAVE_ROAD_NS= "leave_road_ns";
    final String ARRIVAL_ROAD_EO= "arrival_road_eo";
    final String LEAVE_ROAD_EO= "leave_road_eo";
    final double UPDATE_POSITION_TIME = 1000.0; //UPDATE_POSITION_TIME
    final double STOP_WAITING_TIME = 10000.0; //WAIT AT ROAD_CROSS
    private double tempo = 0;
    String nodo_ingresso;
    String nodo_uscita;
    int index_nodo_attuale;

    double currX = 0;
    double currY = 0;
    double currDistance = 0;
    double Partenza;
    double Arrivo;


    MobilityMap cityMap;
    Graph mappa;
    Dijkstra dijkstra;

    ArrayList<Node> list1;
    private canale my_wireless_channel;
    
    private boolean carIsPowerOff = true;
    private String POWER_OFF = "car power off";
    
    private int idSem;
    private final int  DistanzaSem=50;
    private boolean NS;
    private boolean EO;

    public canale getMy_wireless_channel() {
        return my_wireless_channel;
    }

    public void setMy_wireless_channel(canale my_wireless_channel) {
        this.my_wireless_channel = my_wireless_channel;
    }

    public NodoMacchina(scheduler s, int id_nodo, physicalLayer myPhyLayer, LinkLayer myLinkLayer, NetworkLayer myNetLayer, TransportLayer myTransportLayer, Grafo network, String tipo, int gtw) {
        super(s, id_nodo, myPhyLayer, myLinkLayer, myNetLayer, myTransportLayer, network, tipo, gtw);
        dijkstra = new Dijkstra(Dijkstra.Element.EDGE, null, "length");

    }

    public String getNodo_ingresso() {
        return nodo_ingresso;
    }

    public void setNodo_ingresso(String nodo_ingresso) {
        this.nodo_ingresso = nodo_ingresso;
    }

    public String getNodo_uscita() {
        return nodo_uscita;
    }

    public void setNodo_uscita(String nodo_uscita) {
        this.nodo_uscita = nodo_uscita;
    }

    public MobilityMap getMappa() {
        return cityMap;
    }

    public void setMappa(MobilityMap mappa) {
        this.cityMap = mappa;
        this.mappa = mappa.cityRoadMap;
    }

    public void calcolaNuovaPosizione(Edge e, double x1, double y1, double x2, double y2) {
        double avgSpeed = (Double) e.getAttribute("avgSpeed");
        double angle = Math.atan(Math.abs(y2 - y1) / Math.abs(x2 - x1));
        double distance = avgSpeed * UPDATE_POSITION_TIME / 1000.0;

        double addX = 0;
        double addY = 0;
        //Find quadrante
        if ((x2 >= x1) && (y2 >= y1)) {
            //SONO nel primo quadrante sono tutti contributi positivi
            addX = 1;
            addY = 1;
        } else if ((x2 >= x1) && (y2 < y1)) {
            //SONO nel secondo quadrante i contributi di y sono negativi
            addX = 1;
            addY = -1;
        } else if ((x2 < x1) && (y2 < y1)) {
            //SONO nel terzo quadrante i contributi di y sono negativi
            addX = -1;
            addY = -1;
        } else if ((x2 < x1) && (y2 >= y1)) {
            //SONO nel terzo quadrante i contributi di y sono negativi
            addX = -1;
            addY = 1;
        }

        double temp_currX = currX + (addX) * distance * Math.cos(angle);
        double temp_currY = currY + (addY) * distance * Math.sin(angle);

        if (cityMap.validatePos("" + this.id_nodo, temp_currX, temp_currY)) {
            currX = temp_currX;
            currY = temp_currY;
            currDistance = currDistance + distance;
        }
    }

    @Override
    public void Handler(Messaggi m) {
        if (m.getTipo_Messaggio().equals(START_ROAD_RUN)) {
            carIsPowerOff = false;
            dijkstra.init(mappa);
            dijkstra.setSource(mappa.getNode(nodo_ingresso));
            dijkstra.compute();

            index_nodo_attuale = 0;
            list1 = new ArrayList<Node>();
            for (Node node : dijkstra.getPathNodes(mappa.getNode(nodo_uscita))) {
                list1.add(0, node);
            }

            Node curr = list1.get(index_nodo_attuale);
            Object x1 = ((Object[]) curr.getAttribute("xy"))[0];
            Object y1 = ((Object[]) curr.getAttribute("xy"))[1];

            currX = Double.parseDouble("" + x1);
            currY = Double.parseDouble("" + y1);
            currDistance = 0;
            tempo=s.orologio.getCurrent_Time();
            m.setTipo_Messaggio(UPDATE_POSITION);
            m.shifta(UPDATE_POSITION_TIME);
            m.setDestinazione(this);
            m.setSorgente(this);
            s.insertMessage(m);
            
            
            Node next = list1.get(index_nodo_attuale + 1);
            
            
            if(cityMap.semaphore.containsKey(next.toString())){
	            
            	String edge_label = curr.toString() + next.toString();
	            Edge e = mappa.getEdge(edge_label);
	            if(e == null){
	               edge_label =  next.toString()+curr.toString();
	               e = mappa.getEdge(edge_label);
	            };
	            
	            sem_node sem=cityMap.semaphore.get(next.toString());
        		idSem=sem.GetId();
        		NodoSemaforo nodo=(NodoSemaforo) info.getNodo(idSem);
	            
	            Messaggi m1 = new Messaggi(null,this,this,this,s.orologio.getCurrent_Time());
	            if (e.getAttribute("Direction")== Direzione.EST || e.getAttribute("Direction")==Direzione.OVEST){
	            	m1.setTipo_Messaggio(ARRIVAL_ROAD_EO);
	            }else{
	            	m1.setTipo_Messaggio(ARRIVAL_ROAD_NS);
	            }
	           
	            m1.shifta(UPDATE_POSITION_TIME);
	            m1.setDestinazione(nodo);
	            m1.setSorgente(this);
	            s.insertMessage(m1);
	           }
            Partenza=s.orologio.getCurrent_Time();
            MediaTempi.InserisciPercosi(nodo_ingresso+nodo_uscita);

        } else if (m.getTipo_Messaggio().equals(UPDATE_POSITION)) {
            //TODO: MANAGE MOVEMENTS
            if (!nodo_ingresso.equals(nodo_uscita)) {
                if (index_nodo_attuale < list1.size() - 1) {                    
                    Node curr = list1.get(index_nodo_attuale);
                    Node next = list1.get(index_nodo_attuale + 1);
                    
                  

                    Object x1 = ((Object[]) curr.getAttribute("xy"))[0];
                    Object x2 = ((Object[]) next.getAttribute("xy"))[0];
                    Object y1 = ((Object[]) curr.getAttribute("xy"))[1];
                    Object y2 = ((Object[]) next.getAttribute("xy"))[1];

                    double xComp = Math.pow((Double.parseDouble("" + x2) - Double.parseDouble("" + x1)), 2.0);
                    double yComp = Math.pow((Double.parseDouble("" + y2) - Double.parseDouble("" + y1)), 2.0);
                    double segment_length = Math.sqrt(xComp + yComp);

                    //Get average speed from cityMap by reading edge info
                    String edge_label = curr.toString() + next.toString();
                    Edge e = mappa.getEdge(edge_label);
                    
                    if(e == null){
                       edge_label =  next.toString()+curr.toString();
                       e = mappa.getEdge(edge_label);
                    }

                    calcolaNuovaPosizione(e, Double.parseDouble("" + x1),
                            Double.parseDouble("" + y1),
                            Double.parseDouble("" + x2),
                            Double.parseDouble("" + y2));

                    double waitingTime = UPDATE_POSITION_TIME;
                    //Sono arrivato alla fine del segmento
                    
                    if(cityMap.semaphore.containsKey(next.toString())){
                    	if (currDistance >= segment_length- DistanzaSem){
                    		sem_node sem=cityMap.semaphore.get(next.toString());
                    		idSem=sem.GetId();
                    		NodoSemaforo nodo=(NodoSemaforo) info.getNodo(idSem);
                    		if (e.getAttribute("Direction")== Direzione.EST || e.getAttribute("Direction")==Direzione.OVEST){
                    			if(nodo.getStatoEO()==StatoSemaforo.ROSSO){
                    				frenata++;
                    				EO=true;
                    				NS=false;
                    				m.setTipo_Messaggio(WAITING_ROAD);
                    				//m.shifta(UPDATE_POSITION_TIME);
                    				waitingTime=UPDATE_POSITION_TIME;
                    				m.setDestinazione(this);
                    				m.setSorgente(this);
                    				//s.insertMessage(m);
                    			}
                    		}
	                    	
	                    	if (e.getAttribute("Direction")== Direzione.NORD || e.getAttribute("Direction")==Direzione.SUD){
	                			if(nodo.getStatoNS()==StatoSemaforo.ROSSO){
	                				frenata++;
	                				NS=true;
	                				EO=false;
	                				m.setTipo_Messaggio(WAITING_ROAD);
	                				//m.shifta(UPDATE_POSITION_TIME);
	                				waitingTime=UPDATE_POSITION_TIME;
	                				m.setDestinazione(this);
	                				m.setSorgente(this);
	                				//s.insertMessage(m);
	                			}
                    		}
                    	}
                    }
        

                    if (currDistance >= segment_length) {
                        currX = Double.parseDouble("" + x2);
                        currY = Double.parseDouble("" + y2);
                        cityMap.updateVehiclePos("" + this.id_nodo, currX, currY);
                        currDistance = 0;
                        
                        if(cityMap.semaphore.containsKey(next.toString())){ 
            	            sem_node sem=cityMap.semaphore.get(next.toString());
                    		idSem=sem.GetId();
                    		NodoSemaforo nodo=(NodoSemaforo) info.getNodo(idSem);
                    		Messaggi m1 = new Messaggi(null,this,this,this,s.orologio.getCurrent_Time());
            	            if (e.getAttribute("Direction")== Direzione.EST || e.getAttribute("Direction")==Direzione.OVEST){
            	            	m1.setTipo_Messaggio(LEAVE_ROAD_EO);
            	            }else{
            	            	m1.setTipo_Messaggio(LEAVE_ROAD_NS);
            	            }
            	            m1.shifta(UPDATE_POSITION_TIME);
            	            m1.setDestinazione(nodo);
            	            m1.setSorgente(this);
            	            s.insertMessage(m1);
            	        }
                        
                       // System.out.println("nodo " + this.getId() + " Arrivato su incrocio " + next + " al tempo " + s.orologio.getCurrent_Time());
                        index_nodo_attuale++;
                        if (index_nodo_attuale < list1.size() - 1){
	                        Node new_next= list1.get(index_nodo_attuale + 1);
	                        
	                        if(cityMap.semaphore.containsKey(new_next.toString())){ 
	            	            sem_node sem=cityMap.semaphore.get(new_next.toString());
	                    		idSem=sem.GetId();
	                    		NodoSemaforo nodo=(NodoSemaforo) info.getNodo(idSem);
	                    		edge_label = next.toString() + new_next.toString();
	                            e = mappa.getEdge(edge_label);
	            	            
	            	            Messaggi m1 = new Messaggi(null,this,this,this,s.orologio.getCurrent_Time());
	            	            if (e.getAttribute("Direction")== Direzione.EST || e.getAttribute("Direction")==Direzione.OVEST){
	            	            	m1.setTipo_Messaggio(ARRIVAL_ROAD_EO);
	            	            }else{
	            	            	m1.setTipo_Messaggio(ARRIVAL_ROAD_NS);
	            	            }
	            	            m1.shifta(UPDATE_POSITION_TIME);
	            	            m1.setDestinazione(nodo);
	            	            m1.setSorgente(this);
	            	            s.insertMessage(m1);
	            	           }
                        }
                        
                        waitingTime = STOP_WAITING_TIME;
                       
                        
                        if(this.nodo_uscita.equals(next.toString())){
                            carIsPowerOff = true;
                            Arrivo= s.orologio.getCurrent_Time()-Partenza;
                            System.out.println(Arrivo +"   "+ this.id_nodo );
                            MediaTempi.InserisciTempi(nodo_ingresso+nodo_uscita,Arrivo);
                            for(Nodo n : info.getNodes())
                            {
                            	currX = 0;
                                currY = 0;
                                cityMap.updateVehiclePos("" + this.id_nodo, currX, currY);
                           
                                Messaggi m1 = new Messaggi(POWER_OFF,this,this,n,s.orologio.getCurrent_Time());
                                m1.setNodoSorgente(this);
                                m1.saliPilaProtocollare = false;
                                m1.setNextHop(n);
                                m1.setNextHop_id(n.getId());
                                s.insertMessage(m1);
                            }
                            tempo=s.orologio.getCurrent_Time()-tempo;
                           // System.out.println("nodo " + this.getId() + " posizione x,y (" + currX + "," + currY + ") al tempo " + s.orologio.getCurrent_Time()+" ha impiegato : "+tempo);
                        }
                    }
                    
                    if(carIsPowerOff == false)
                    {
                      m.shifta(waitingTime);
                      s.insertMessage(m);
                    }
                }
            }
        }else if(m.getTipo_Messaggio().equals(WAITING_ROAD)){
        	NodoSemaforo nodo=(NodoSemaforo) info.getNodo(idSem);
        	if (EO == true){//est ovest è rosso
	        	//System.out.println(nodo.getStatoEO()+" nodo " + this.getId());
	        	if(nodo.getStatoEO()==StatoSemaforo.VERDE){
	        		 m.setTipo_Messaggio(UPDATE_POSITION);
	                 m.shifta(UPDATE_POSITION_TIME);
	                 m.setDestinazione(this);
	                 m.setSorgente(this);
	                 s.insertMessage(m);
	        	}
	        	else{
	        		//System.out.println("nodo " + this.getId() + " sono fermo " + s.orologio.getCurrent_Time());
	        		 m.setTipo_Messaggio(WAITING_ROAD);
	    	         m.shifta(UPDATE_POSITION_TIME);
	    	         m.setDestinazione(this);
	    	         m.setSorgente(this);
	    	         s.insertMessage(m);
	        	}
        	}
        	
        	else if(NS=true){
        		//System.out.println(nodo.getStatoNS()+" nodo " + this.getId());
	        	if(nodo.getStatoNS()==StatoSemaforo.VERDE){
	        		 m.setTipo_Messaggio(UPDATE_POSITION);
	                 m.shifta(UPDATE_POSITION_TIME);
	                 m.setDestinazione(this);
	                 m.setSorgente(this);
	                 s.insertMessage(m);
	        	}
	        	else{
	        	//	System.out.println("nodo " + this.getId() + " sono fermo " + s.orologio.getCurrent_Time());
	        		 m.setTipo_Messaggio(WAITING_ROAD);
	    	         m.shifta(UPDATE_POSITION_TIME);
	    	         m.setDestinazione(this);
	    	         m.setSorgente(this);
	    	         s.insertMessage(m);
	        	}
        	}
        
        } else if (m.getTipo_Messaggio().equals("DISCOVER_NEIGHBOURS")) {
            if(this.carIsPowerOff == false)
            {
                if (m.saliPilaProtocollare == false) {
                    //Invia messaggio a canale
                    m.shifta(0);
                    m.setDestinazione(my_wireless_channel);
                    m.setSorgente(this);
                    s.insertMessage(m);
                } else {
                    //invia messaggio a PHY
                    m.shifta(0);
                    m.setDestinazione(this.myPhyLayer);
                    m.setSorgente(this);
                    s.insertMessage(m);
                }
            }
        } else {
            super.Handler(m);
        }
    }

    public void setExitFromGate(double exitGateAt) {
        Messaggi m = new Messaggi(START_ROAD_RUN, this, this, this, s.orologio.getCurrent_Time());        
        m.shifta(exitGateAt);
        s.insertMessage(m);
    }

}
