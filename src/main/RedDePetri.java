package main;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.*;
import java.util.List;

import java.util.ArrayList;
import java.util.Iterator;


public class RedDePetri {
	
	private Politicas politicas;
	private SensibilizadoConTiempo sensibilizadoConTiempo;
	private Mutex mutex;
	private Log log;
	
	//creación de matrices (son listas que contienen sublistas).
	public List<List<Integer>> matrizIncidencia = new ArrayList<List<Integer>>(); 
	public List<Integer> marcado = new ArrayList<Integer>();
	public List<Integer> sensibilizadas = new ArrayList<Integer>();
	int transiciones[] = {1,10,11,12,13,14,15,16,19,2,20,21,23,24,3,4,5,6,7,8,9};
	
	int transicionADisparar;
	
	public RedDePetri(Politicas politicas,SensibilizadoConTiempo sensibilizadoConTiempo,Mutex mutex,Log log) {
		this.politicas = politicas;
		this.sensibilizadoConTiempo = sensibilizadoConTiempo;
		this.mutex = mutex;
		this.log = log;
	}

	public void sensibilizadas() {
		int cantPlazasI = matrizIncidencia.get(0).size();
		int cantTransicionesI = matrizIncidencia.size()/cantPlazasI;
		int sensibilizado = 1;
		for (int i = 0;i<cantTransicionesI;i++) {
			for (int j = 0;j<cantPlazasI;j++) {
				if ((matrizIncidencia.get(i).get(j) + marcado.get(j)) < 0  ) {	//si la suma es menor que cero
					sensibilizado = 0;	//bandera que indica que la transición no está sensibilizada.
					break;
				}
			}
			if(sensibilizado == 1) {
				sensibilizadas.set(i,1);	//marco como sensibilizada dicha transicion.
				sensibilizadoConTiempo.setNuevoTimeStamp(i,false);		//seteo el tiempo en que se sensibilizó la transición.
			}
			else {
				sensibilizadas.set(i,0);	//marco como no sensibilizada dicha transicion.
				sensibilizado = 1; //reseteo la bandera para la proxima iteración.
			}
		}
	}
	
	public boolean disparar(int[] transicionesHilo) {
		log.escribir(System.nanoTime(),"Intenta disparar una transicion el "+Hilos.nombre[Integer.parseInt(Thread.currentThread().getName())]);
		System.out.println("Intenta disparar una transicion el "+Hilos.nombre[Integer.parseInt(Thread.currentThread().getName())]);
		int cantTListas = 0;
		List<Integer> tListas = new ArrayList<Integer>();	//vector columna que tendrá el resultado de hacer un and entre las transiciones que quiere 
															//disparar el hilo y las transiciones sensibilizadas en ese momento.
		for(int i=0;i<sensibilizadas.size();i++) {
			if(sensibilizadas.get(i) == 1 && transicionesHilo[i] == 1 ) {
				tListas.add(1); // 1 = transición lista para ser disparada.
			}
			else {
				tListas.add(0);
			}
		}
		for (int i = 0;i<tListas.size();i++) {		//cuento las cantidades de transiciones que quiere disparar el hilo y que están sensibilizadas
			cantTListas += tListas.get(i);			//Si hay mas de 1, entonces hay que enviar a politicas para que decida cual disparar.
		}
		mostrarSensibilizadas();
		
		if (cantTListas  >= 1) {			//si hay mas de una transicion sensibilizada que pueda disparar un hilo
			if (cantTListas == 1) {
				log.escribir(System.nanoTime(), "hay 1 transicion sensibilizada");
				System.out.println("hay 1 transicion sensibilizada");
			}
			else {
				log.escribir(System.nanoTime(), "hay mas de 1 transicion sensibilizada");
				System.out.println("hay mas de 1 transicion sensibilizada");
			}	
			List<Integer> decision = politicas.cual(tListas,Politicas.prioridadesT);	//hay que llamar a politicas para que decida que transicion va a disparar si solo hay una para disparar, entonces  elegirirá esa transicion :)
			for(int k=0; k<decision.size(); k++){
				if (decision.get(k) == 1) {
					if (cantTListas > 1) {
						log.escribir(System.nanoTime(), "por politica se decide que la transicion a disparar es: "+transiciones[k] );
						System.out.println("por politica se decide que la transicion a disparar es: "+transiciones[k]);
					}	
					transicionADisparar = k;
				}
			}
			boolean ventana = sensibilizadoConTiempo.testVentantaTiempo(transicionADisparar);
			if (ventana == true) {
				if (sensibilizadoConTiempo.esperando[transicionADisparar] == false) {
					sensibilizadoConTiempo.setNuevoTimeStamp(transicionADisparar, true);
					actualizaMarcado(decision);		//llamo a la funcion que genera el nuevo marcado (disparo la transicion)
					return true;					//quiere decir que pudo disparar la transicion.
				}
				else {		//esperando == true. es decir hay un hilo durmiendo esperando por disparar dicha transicion.
					if (Integer.parseInt(Thread.currentThread().getName()) == sensibilizadoConTiempo.IDHiloSleep[transicionADisparar]) { // si el hilo que quiere disparar a la transicion es el mismo que hizo el sleep esperandola.
						log.escribir(System.nanoTime(), "El hilo "+Integer.parseInt(Thread.currentThread().getName())+" viene de un sleep y ahora quiere disparar la transición "+transiciones[transicionADisparar] );
						System.out.println("El hilo "+Integer.parseInt(Thread.currentThread().getName())+" viene de un sleep y ahora quiere disparar la transición "+transiciones[transicionADisparar] );
						sensibilizadoConTiempo.resetEsperando(transicionADisparar);		//ponemos esperando = false.
						actualizaMarcado(decision);		//llamo a la funcion que genera el nuevo marcado (disparo la transicion)
						return true;					//quiere decir que pudo disparar la transicion.
					}
					else {			//el id del hilo que quiere disparar no corresponde con el id del hilo que estuvo en el sleep.
						log.escribir(System.nanoTime(), "Ya hay un hilo esperando a disparar dicha transición.");
						System.out.println("Ya hay un hilo esperando a disparar dicha transición.");
						return false;
					} 
				}
			}
			else {			//ventana == false.
				log.escribir(System.nanoTime(), "La transición "+transiciones[transicionADisparar]+" está sensibilizada pero aún no se cumplió el tiempo alfa");
				System.out.println("La transición "+transiciones[transicionADisparar]+" está sensibilizada pero aún no se cumplió el tiempo alfa");
				boolean antes = sensibilizadoConTiempo.antesDeLaVentana(transicionADisparar);  // esto indica si ya hay alguien durmiendo esperando disparar la transicion.
				if (antes = true) {			// si no hay nadie durmiendo
					log.escribir(System.nanoTime(), "no hay ningún hilo esperando para disparar dicha transición por lo tanto el hilo se va a dormir esperando a que se cumpla el tiempo alfa");
					System.out.println("no hay ningún hilo esperando para disparar dicha transición por lo tanto el hilo se va a dormir esperando a que se cumpla el tiempo alfa");
					mutex.release(); 	//libero el mutex antes de mandar a dormir al hilo.
					sensibilizadoConTiempo.setEsperando(transicionADisparar);
					try {
						Thread.currentThread().sleep(sensibilizadoConTiempo.timeStamp[transicionADisparar] + sensibilizadoConTiempo.alfa[transicionADisparar] - System.currentTimeMillis());
					}
					catch (Exception e) {
					}
					log.escribir(System.nanoTime(), "El hilo "+Integer.parseInt(Thread.currentThread().getName()) +" Acaba de despertarse ya que se cumplió el tiempo alfa");
					System.out.println("El hilo "+Integer.parseInt(Thread.currentThread().getName()) +" Acaba de despertarse ya que se cumplió el tiempo alfa");
					sensibilizadoConTiempo.vieneDelSleep[Integer.parseInt(Thread.currentThread().getName())] = true;
					return false;
				}
				else {		//si ya hay un hilo durmiendo.
					log.escribir(System.nanoTime(), "Ya hay un hilo durmiendo esperando por disparar la transición.");
					System.out.println("Ya hay un hilo durmiendo esperando por disparar la transición.");
					return false;
				}
					
			}
			
		}
		
		else {	//si no hay transiciones sensibilizadas
			log.escribir(System.nanoTime(), "No hay transiciones sensibilizadas que pueda disparar este hilo.");
			System.out.println("No hay transiciones sensibilizadas que pueda disparar este hilo.");
			return false;					//quiere decir que no pudo disparar ninguna transicion
		}	
	}
	
	public void actualizaMarcado(List<Integer> disparo) {			//Método encargado de hacer el nuevo marcado que sería Mi+1 = Mi + I.U 
		for(int i=0;i<disparo.size();i++) {
			if(disparo.get(i) == 1) {
				for (int j =0;j<matrizIncidencia.get(0).size();j++) {
					marcado.set(j,(marcado.get(j)+matrizIncidencia.get(i).get(j)));		
				}
				log.escribir(System.nanoTime(), "transición " +"T"+transiciones[i] +" disparada. :)");
				log.escribir2(System.nanoTime(),"T"+transiciones[i]);
				System.out.println("transición " +"T"+transiciones[i] +" disparada. :)");
				sensibilizadoConTiempo.resetEsperando(i);
				break;
			}
		}
		
	}
	
	void mostrarMatrizIncidencia() {
		// IMPRESION DE LAS MATRICES GUARDADAS EN LAS LISTAS
		int cantPlazasI = matrizIncidencia.get(0).size();
		int cantTransicionesI = matrizIncidencia.size()/cantPlazasI;
		System.out.println("\n");
	    System.out.print("La matriz de incidencia es: ");
	    
	    for(int j=0; j<cantPlazasI; j++){ // filas
	    	System.out.print("\n");
	    	//21
	    	for(int k=0; k<cantTransicionesI; k++){ //columnas
	    	System.out.print(" "+matrizIncidencia.get(k).get(j));
	    	}
	    }
	    System.out.println("\n");
	}    
	
	void mostrarMarcado() {   
		String marca = "";
	    for(int k=0; k<marcado.size(); k++){ //filas
	    	marca += "     " +Integer.toString(marcado.get(k));
	    }
	    log.escribir(System.nanoTime(),"                           Plaza: 0     1     10     11   12   13     14     15   16   17   18    19    2     20   21   22   26       29   3     30   31     4     5     6     7     8     9");
	    log.escribir(System.nanoTime(),"el marcado actual es: " +marca);
	    System.out.println("                    Plaza: 0     1     10     11   12     13     14     15   16     17    18    19    2     20    21    22    26    29    3     30    31    4     5     6     7     8     9");
	    System.out.println("el marcado actual es: " +marca);
	}    
	
	void mostrarSensibilizadas() {    
		String sensi = "";
		for(int k=0; k<sensibilizadas.size(); k++){ //filas
			if (sensibilizadas.get(k) ==1 ) {
				sensi += " "+Integer.toString(transiciones[k]); 
			}
	    }
		log.escribir(System.nanoTime(), "La transiciones " + sensi +" están sensibilizadas");
		System.out.println("\"La transiciones " + sensi +" están sensibilizadas");
	}
	
	public void aserciones() {
		int cantTokens1 = 0,cantTokens2 = 0,cantTokens3 = 0,cantTokens4 = 0,cantTokens5 = 0,cantTokens6 = 0,cantTokens7 = 0,cantTokens8 = 0,cantTokens9 = 0,cantTokens10 = 0;
		int[] invariante1 = {0,1,2,3,4,12,17,18,20,21,22,26}; //estas son las plazas correspondiente a cada invariante.
		int[] invariante2 = {3,5};     int[] invariante3 = {4,6,17,20}; int[] invariante4 = {8,10,13}; 								
		int[] invariante5 = {9,11,14}; int[] invariante6 = {13,14,15};  int[] invariante7 = {17,19,20};
		int[] invariante8 = {18,23};   int[] invariante9 = {21,24};     int[] invariante10 = {22,25};
		
		for (int i=0;i<invariante1.length;i++) {
			cantTokens1 += marcado.get(invariante1[i]); //contabilizamos  la cantidad de tokes en las plazas de interés.
		}
		for (int i=0;i<invariante2.length;i++) { //contabiliziamos todos los invariantes del mismo "length" para no hacer tantos bucles for.
			cantTokens2 += marcado.get(invariante2[i]); 
			cantTokens8 += marcado.get(invariante8[i]);
			cantTokens9 += marcado.get(invariante9[i]);
			cantTokens10 += marcado.get(invariante10[i]);
		}
		for (int i=0;i<invariante4.length;i++) {
			cantTokens4 += marcado.get(invariante4[i]);
			cantTokens5 += marcado.get(invariante5[i]);
			cantTokens6 += marcado.get(invariante6[i]);
			cantTokens7 += marcado.get(invariante7[i]);
		}
		for (int i=0;i<invariante3.length;i++) {
			cantTokens3 += marcado.get(invariante3[i]);
		}	
		
		assert cantTokens1 == 60 : "\n El invariante M(P0) + M(P1) + M(P10) + M(P11) + M(P12) + M(P2) + M(P29) + M(P3) + M(P31) + M(P4) + M(P5) + M(P9) = 60 No se cumple.";
		assert cantTokens2 == 30 : "\n El invariante M(P11) + M(P13) = 30 No se cumple.";
		assert cantTokens3 == 30 : "\n El invariante M(P12) + M(P14) + M(P29) + M(P31) = 30 No se cumple.";
		assert cantTokens4 == 1  : "\n El invariante M(P16) + M(P18) + M(P20) = 1 No se cumple.";	
		assert cantTokens5 == 1  : "\n El invariante M(P17) + M(P19) + M(P21) = 1 No se cumple.";
		assert cantTokens6 == 1  : "\n El invariante M(P20) + M(P21) + M(P22) = 1 No se cumple.";
		assert cantTokens7 == 1  : "\n El invariante M(P29) + M(P30) + M(P31) = 1 No se cumple.";
		assert cantTokens8 == 1  : "\n El invariante M(P3)  +  M(P6) = 1  No se cumple.";
		assert cantTokens9 == 1  : "\n El invariante M(P4)  +  M(P7) = 1  No se cumple.";
		assert cantTokens10 == 1 : "\n El invariante M(P5)  +  M(P8) = 1  No se cumple.";
	}
}				
