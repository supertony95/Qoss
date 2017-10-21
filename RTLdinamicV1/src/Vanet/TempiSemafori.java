package Vanet;

import java.util.LinkedList;

public class TempiSemafori {

	private static LinkedList<String> semafori= new LinkedList<String>();
	private static double[] tempiEO;
	private static double[] contatoreEO;
	private static double[] tempiNS;
	private static double[] contatoreNS;
	
	
	public static void InserisciSemaforo(String semaforo){
		semafori.add(semaforo);
		}
	
	public static void creaLista(){
		int size=semafori.size();
		tempiEO=new double [size];
		contatoreEO= new double[size];
		tempiNS=new double [size];
		contatoreNS= new double[size];
		for (int i=0;i<size;i++){
			tempiEO[i]=0;
			contatoreEO[i]=0;
			tempiNS[i]=0;
			contatoreNS[i]=0;
			
		}
	}
	
	public static void CalcolaMedia(){
		int size= semafori.size();
		for (int i=0;i<size;i++){
			String semaforo= semafori.get(i);
			System.out.println(" Media Tempo  NS Semaforo "+ semaforo +" : " + (tempiNS[i]/contatoreNS[i]));
			System.out.println(" Media Tempo  EO Semaforo "+ semaforo +" : " + (tempiEO[i]/contatoreEO[i]));
		}
		
	}
	
	public static void InserisciTempi(String semaforo,double tempoEO, double tempoNS){
		if (tempiEO==null){
			creaLista();
		}
		int index= semafori.indexOf(semaforo);
		tempiEO[index]+=tempoEO;
		contatoreEO[index]++;
		tempiNS[index]+=tempoNS;
		contatoreNS[index]++;
	}
	
	public static void ToString(){
		for (String percorso: semafori){
			System.out.println(percorso);
		} 
	}
}