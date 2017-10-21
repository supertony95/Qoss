package Vanet;

import java.util.ArrayList;

import Mobility.MobilityMap;
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

public class NodoSemaforo extends nodo_host {

	// final String UPDATE_POSITION = "update_pos";
	final String UPDATE_STATUS = "update_pos";
	final String STOP_STATUS = "stop_pos";
	final String START_S = "start_s";
	final String ARRIVAL_ROAD_NS= "arrival_road_ns";
	final String LEAVE_ROAD_NS= "leave_road_ns";
	final String ARRIVAL_ROAD_EO= "arrival_road_eo";
	final String LEAVE_ROAD_EO= "leave_road_eo";
	// final double UPDATE_POSITION_TIME = 1000.0; //UPDATE_POSITION_TIME
	final double UPDATE_STATUS_TIME = 1000.0; // UPDATE_POSITION_TIME
	final double STOP_WAITING_TIME = 10000.0; // WAIT AT ROAD_CROSS
	private int numeroCampioni=3;
	private double ULTIMO_CONTROLLO;
	private double MAX_TIME= 120000.0;
	private double MIN_TIME= 35000.0;
	private double TIME_UNIT=3000.0;
	private double UPDATE_TIME =120000.0* numeroCampioni;
	private double GREEN_TIME_NS = 60000.0;
	private double GREEN_TIME_EO = 60000.0;
	private double TIME_WAIT=0.0;
	private int CarNS=0;
	private int CarEO=0;
	private int TOTNS=0;
	private int TOTEO=0;
	String nodo_ingresso;
	String nodo_uscita;
	// int index_nodo_attuale;
	StatoSemaforo StatSemNS;
	StatoSemaforo StatSemEO;
	// double currX = 0;
	// double currY = 0;
	// double currDistance = 0;

	MobilityMap cityMap;
	Graph mappa;
	// Dijkstra dijkstra;

	// ArrayList<Node> list1;
	private canale my_wireless_channel;

	// private boolean carIsPowerOff = true;
	// private String POWER_OFF = "car power off";

	public enum StatoSemaforo {
		ROSSO, VERDE;
	}

	public canale getMy_wireless_channel() {
		return my_wireless_channel;
	}

	public void setMy_wireless_channel(canale my_wireless_channel) {
		this.my_wireless_channel = my_wireless_channel;
	}

	public NodoSemaforo(scheduler s, int id_nodo, physicalLayer myPhyLayer, LinkLayer myLinkLayer,
			NetworkLayer myNetLayer, TransportLayer myTransportLayer, Grafo network, String tipo, int gtw) {
		super(s, id_nodo, myPhyLayer, myLinkLayer, myNetLayer, myTransportLayer, network, tipo, gtw);
		// dijkstra = new Dijkstra(Dijkstra.Element.EDGE, null, "length");

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

	public StatoSemaforo getStatoNS() {
		return StatSemNS;
	}

	public StatoSemaforo getStatoEO() {
		return StatSemEO;
	}

	//
	@Override
	public void Handler(Messaggi m) {
		
		if (m.getTipo_Messaggio().equals(START_S)) {
		
			StatSemNS = StatoSemaforo.ROSSO;
			StatSemEO = StatoSemaforo.ROSSO;
			ULTIMO_CONTROLLO= s.orologio.getCurrent_Time();
			cityMap.modificaColore(StatSemNS, StatSemEO, this.nodo_ingresso);
			m.setTipo_Messaggio(UPDATE_STATUS);
			m.shifta(UPDATE_STATUS_TIME);
			m.setDestinazione(this);
			m.setSorgente(this);
			s.insertMessage(m);
			TempiSemafori.InserisciSemaforo(nodo_ingresso);
		} else if (m.getTipo_Messaggio().equals(UPDATE_STATUS)) {
			System.out.println(s.orologio.getCurrent_Time());
			if(s.orologio.getCurrent_Time()==1160000){
				MediaTempi.CalolaMedia();
				System.out.println("");
				TempiSemafori.CalcolaMedia();
				System.out.println("");
				System.out.println("TOTALE FRENATE : "+NodoMacchina.frenata);
				System.out.println("");
			}
			if (!PresenzaConflitto()){
				if(CarEO==0){
					StatSemNS = StatoSemaforo.VERDE;
					StatSemEO = StatoSemaforo.ROSSO;
					TIME_WAIT=GREEN_TIME_NS;
					ULTIMO_CONTROLLO= s.orologio.getCurrent_Time();
				}
				else{
					StatSemNS = StatoSemaforo.ROSSO;
					StatSemEO = StatoSemaforo.VERDE;
					TIME_WAIT=GREEN_TIME_EO;
					ULTIMO_CONTROLLO= s.orologio.getCurrent_Time();
				}
			}else{
				if(ULTIMO_CONTROLLO + TIME_WAIT <= s.orologio.getCurrent_Time()){
					cambiaStato();
					ULTIMO_CONTROLLO= s.orologio.getCurrent_Time();
				}
				
			}
			
			cityMap.modificaColore(StatSemNS, StatSemEO,this.nodo_ingresso);
			m.setTipo_Messaggio(UPDATE_STATUS);
			m.shifta(UPDATE_STATUS_TIME);
			m.setDestinazione(this);
			m.setSorgente(this);
			s.insertMessage(m);
			
			if (s.orologio.getCurrent_Time() % UPDATE_TIME == 0) {
				updateTIme();
			}
		} else if (m.getTipo_Messaggio().equals(STOP_STATUS)) {
			
			m.shifta(STOP_WAITING_TIME);
			s.insertMessage(m);
			
		} else if(m.getTipo_Messaggio().equals(ARRIVAL_ROAD_EO)){
			CarEO++;
			TOTEO++;
		} else if(m.getTipo_Messaggio().equals(LEAVE_ROAD_EO)){
			CarEO--;
		} else if(m.getTipo_Messaggio().equals(ARRIVAL_ROAD_NS)){
			CarNS++;
			TOTNS++;
		} else if(m.getTipo_Messaggio().equals(LEAVE_ROAD_NS)){
			CarNS--;
		} else if (m.getTipo_Messaggio().equals("DISCOVER_NEIGHBOURS")) {
			
		} else {
			super.Handler(m);
			
		}

	}
	
	private void updateTIme() {
		GREEN_TIME_EO=TOTEO*TIME_UNIT;
		GREEN_TIME_NS=TOTNS*TIME_UNIT;
		if(GREEN_TIME_EO > MAX_TIME)
			GREEN_TIME_EO=MAX_TIME;
		else if (GREEN_TIME_EO < MIN_TIME)
			GREEN_TIME_EO=MIN_TIME;
		if(GREEN_TIME_NS > MAX_TIME)
			GREEN_TIME_NS=MAX_TIME;
		else if (GREEN_TIME_NS < MIN_TIME)
			GREEN_TIME_NS=MIN_TIME;
		TempiSemafori.InserisciTempi(nodo_ingresso,GREEN_TIME_EO, GREEN_TIME_NS);
	}

	public boolean PresenzaConflitto(){
		if (CarEO==0 || CarNS ==0)
			return false;
		else 
			return true;
	};
	
	public void cambiaStato() {
		if (StatSemNS == StatoSemaforo.ROSSO) {
			StatSemNS = StatoSemaforo.VERDE;
			StatSemEO = StatoSemaforo.ROSSO;
			TIME_WAIT=GREEN_TIME_NS;
		} else {
			StatSemNS = StatoSemaforo.ROSSO;
			StatSemEO = StatoSemaforo.VERDE;
			TIME_WAIT=GREEN_TIME_EO;
		}
	}

	public void setExitFromGate(double exitGateAt) {
		Messaggi m = new Messaggi(START_S, this, this, this, s.orologio.getCurrent_Time());
		m.shifta(exitGateAt);
		s.insertMessage(m);
	}
}
