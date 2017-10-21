package Vanet;

import java.util.ArrayList;
import java.util.LinkedList;

public class MediaTempi {

	private static LinkedList<String> percorsi= new LinkedList<String>();

	private static ArrayList<Double> tempi= new ArrayList<Double>();
	private static ArrayList<Integer> contatore= new ArrayList<Integer>();
	
	public static void InserisciPercosi(String percorso){
		if(!(percorsi.contains(percorso))){
			percorsi.add(percorso);
			tempi.add(0.0);
			contatore.add(0);
			//System.out.println(percorsi.toString());
		}
	}

	
	public static void CalolaMedia(){
		int size= percorsi.size();
		for (int i=0;i<size;i++){
			String percorso= percorsi.get(i);
			System.out.println(" media percorso "+ percorso +" : " + (tempi.get(i)/contatore.get(i)));
		}
		
	}
	
	public static void InserisciTempi(String percorso,double tempo){

		int index= percorsi.indexOf(percorso);
		//System.out.println(tempi.toString());
		tempi.set(index, tempi.get(index)+tempo);
		contatore.set(index,contatore.get(index)+1);
	}
	
	public static void ToString(){
		for (String percorso: percorsi){
			System.out.println(percorso);
		} 
	}
}