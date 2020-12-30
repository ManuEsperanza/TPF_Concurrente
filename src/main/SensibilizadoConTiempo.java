package main;
import java.io.*;
import java.util.List;

public class SensibilizadoConTiempo {
	
		  //transición: 1,10,11,12,13,14,15,16,19,2,20,21,23,24,3,4,5,6,7,8,9   T 13 y 14 son las transic. de la caja
	long timeStamp[] = {0,0 ,0 ,0 ,0 ,0 ,0 ,0 ,0 ,0,0 ,0 ,0 ,0 ,0,0,0,0,0,0,0};	//tiempo de sensibilizado
	long[] alfa;	//tiempo alfa de cada transición. (en milisegundos)
	boolean primero[] = {false,false,false ,false ,false ,false ,false ,false ,false ,false ,false,false ,false ,false ,false ,false,false,false,false,false,false}; 
	boolean esperando[] = {false,false,false ,false ,false ,false ,false ,false ,false ,false ,false,false ,false ,false ,false ,false,false,false,false,false,false};
	boolean vieneDelSleep[] = {false,false,false ,false ,false ,false ,false ,false ,false,false}; //uno por hilo.
	int 	IDHiloSleep[] = {10,10,10 ,10 ,10 ,10 ,10 ,10 ,10 ,10 ,10,10 ,10 ,10 ,10 ,10,10,10,10,10,10};  // como usamos el hilo 0, el 10 indica como vacío.
				
	public SensibilizadoConTiempo(boolean tiempoActivado) {
		if (tiempoActivado == true) {
			     //transición: 1,10,11,12,13,14,15,16,19,2,20,21,23,24,3,4,5,6,7,8,9
			alfa = new long[] {0,10,0 ,0 ,1 ,1 ,0 ,0 ,0 ,0,0 ,0 ,0 ,0 ,0,0,0,0,0,0,10}; 
		}
		else {
			alfa = new long[] {0,0,0 ,0 ,0 ,0 ,0 ,0 ,0 ,0,0 ,0 ,0 ,0 ,0,0,0,0,0,0,0}; //sirve para quitar los tiempos a la hora de hacer los tests.
		}
	}
	
	boolean testVentantaTiempo(int transicionADisparar) {
		boolean ventana;
		if (alfa[transicionADisparar] <= (System.currentTimeMillis() - timeStamp[transicionADisparar])) {
			return ventana = true;
		}
		else {
			return ventana = false;
		}
	}
	
	void setNuevoTimeStamp(int transicion,boolean reset){
		if (reset == true) {
			timeStamp[transicion] = System.currentTimeMillis();
		}
		else { 
			if (timeStamp[transicion] == 0){
				timeStamp[transicion] = System.currentTimeMillis();
			}
		}
	}
	
	void resetTimeStamp(int transicion){
		timeStamp[transicion] = 0;
	}

	boolean antesDeLaVentana(int transicion) {
		
		if (primero[transicion] == false) {
			primero[transicion] = true;
			return true;
		}
		else {
			return false;
		}
	}
	
	void setEsperando(int transicion) {
		IDHiloSleep[transicion] = Integer.parseInt(Thread.currentThread().getName()); 
		esperando[transicion] = true;
	}
	
	void resetEsperando(int transicion) {
		IDHiloSleep[transicion] = 10; // el 10 indica que no hay hilos durmiendo esperando por disparar la transicion.
		esperando[transicion] = false;
	}

}
