package main;
import java.io.*;
import java.lang.Thread.State;
import java.util.Vector;
import java.util.List;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

public class Main {
	
	public static void main (String args[]){
		
		//creación de arrays
		Thread threads[] = new Thread[10];
		Thread.State status[] = new Thread.State[10];
		
		//Archivo Log
		Log log = new Log("/home/manu3/Documentos/Concurrente/Trabajo Final/LogGeneral.ods","/home/manu3/Documentos/Concurrente/Trabajo Final/LogDisparos.ods");
		//politicas
		Politicas politicas = new Politicas();
		
		//semaforo de acceso al monitor
		Mutex mutex = new Mutex(1,log);		
		
		//Creación de Colas
		Mutex colasCondicion[] = new Mutex[10];				//colas de variable de condicion para cada hilo.
		Colas colas = new Colas(mutex,colasCondicion,log);
		
		SensibilizadoConTiempo sensibilizadoConTiempo = new SensibilizadoConTiempo(true); //true = transiciones con tiempo, false = transiciones inmediatas
		
		//instancia de la Red de Petri
		RedDePetri redDePetri = new RedDePetri(politicas,sensibilizadoConTiempo,mutex,log);
		
		String ruta = "/home/manu3/matricest0.html";	//ruta donde se encuentra el archivo html que contiene las matrices exportadas del pipe	
		Matrices matrices = new Matrices(ruta,redDePetri);			//levanta las matrices del archivo exportado por el pipe.
		//redDePetri.marcado.set(16,1);
		
		
		Ventana ventana = new Ventana(politicas);
		
		//vectores que representan las transiciones que quieren disparar cada uno de los 10 hilos definidos en el sistema.
					 //Transcisión:  1 10 11 12 13 14 15 16 19 2 20 21 23 24 3 4 5 6 7 8 9	
		 int TransicionesHilo0[]  = {0, 0, 0, 0, 0, 0, 0, 0, 1,0, 1, 1, 0, 0,0,0,0,0,0,0,0}; //transición   19,20,21	- Ingreso de Vehículos
		 int TransicionesHilo1[]  = {1, 0, 0, 0, 0, 0, 0, 0, 0,0, 0, 0, 0, 0,0,1,0,0,0,0,0}; //transiciones 1,4 			- Control de barrera de entrada 1
		 int TransicionesHilo2[]  = {0, 0, 0, 0, 0, 0, 0, 0, 0,1, 0, 0, 0, 0,0,0,1,0,0,0,0}; //transiciones 2,5 			- Control de barrera de entrada 2
		 int TransicionesHilo3[]  = {0, 0, 0, 0, 0, 0, 0, 0, 0,0, 0, 0, 0, 0,1,0,0,1,0,0,0}; //transiciones 3,6 			- Control de barrera de entrada 3
		 int TransicionesHilo4[]  = {0, 0, 0, 0, 0, 0, 0, 0, 0,0, 0, 0, 1, 1,0,0,0,0,1,1,0}; //transiciones 7,8,23,24 	- Acceso a los estacionamientos
		 int TransicionesHilo5[]  = {0, 0, 0, 0, 0, 0, 0, 0, 0,0, 0, 0, 0, 0,0,0,0,0,0,0,1}; //transicion   9				- Salida del piso 1
		 int TransicionesHilo6[]  = {0, 1, 0, 0, 0, 0, 0, 0, 0,0, 0, 0, 0, 0,0,0,0,0,0,0,0}; //transicion   10			- Salida del piso 2
		 int TransicionesHilo7[]  = {0, 0, 1, 0, 0, 0, 1, 0, 0,0, 0, 0, 0, 0,0,0,0,0,0,0,0}; //transiciones 11,15		- Control de barrera de salida 1
		 int TransicionesHilo8[]  = {0, 0, 0, 1, 0, 0, 0, 1, 0,0, 0, 0, 0, 0,0,0,0,0,0,0,0}; //transiciones 12,16		- Control de barrera de salida 2
		 int TransicionesHilo9[]  = {0, 0, 0, 0, 1, 1, 0, 0, 0,0, 0, 0, 0, 0,0,0,0,0,0,0,0}; //transiciones 13,14 		- Caja 
		
		 int[][] transicionesPorHilo = {TransicionesHilo0,TransicionesHilo1,TransicionesHilo2,TransicionesHilo3,TransicionesHilo4,TransicionesHilo5,TransicionesHilo6,TransicionesHilo7,TransicionesHilo8,TransicionesHilo9};
		
		//instancia del monitor
		GestorDeMonitor monitor = new GestorDeMonitor(redDePetri,transicionesPorHilo,politicas,sensibilizadoConTiempo,mutex,colas,log);
		
		Hilos hilo0 = new Hilos(monitor,TransicionesHilo0);			//Hilo es quien tiene el método run(). le pasamos el objeto monitor y también
		Hilos hilo1 = new Hilos(monitor,TransicionesHilo1);			//las transiciones que quiere disparar cada hilo.
		Hilos hilo2 = new Hilos(monitor,TransicionesHilo2);
		Hilos hilo3 = new Hilos(monitor,TransicionesHilo3);
		Hilos hilo4 = new Hilos(monitor,TransicionesHilo4);
		Hilos hilo5 = new Hilos(monitor,TransicionesHilo5);
		Hilos hilo6 = new Hilos(monitor,TransicionesHilo6);
		Hilos hilo7 = new Hilos(monitor,TransicionesHilo7);
		Hilos hilo8 = new Hilos(monitor,TransicionesHilo8);
		Hilos hilo9 = new Hilos(monitor,TransicionesHilo9);
		
		//Creación de Hilos:
		
		threads[0]=new Thread(hilo0,"0");
		threads[1]=new Thread(hilo1,"1");
		threads[2]=new Thread(hilo2,"2");
		threads[3]=new Thread(hilo3,"3");
		threads[4]=new Thread(hilo4,"4");
		threads[5]=new Thread(hilo5,"5");
		threads[6]=new Thread(hilo6,"6");
		threads[7]=new Thread(hilo7,"7");
		threads[8]=new Thread(hilo8,"8");
		threads[9]=new Thread(hilo9,"9");
		
		
		while(true) {			//en este bucle seteamos a traves de la ventana la cantidad de disparos que realizará cada hilo.
			System.out.println("");
			if (ventana.cantidadDisparos > 0) {
				log.disparos = ventana.cantidadDisparos;
				
				break;
			}
		}
		redDePetri.mostrarMarcado();
		//ejecucion de hilos	
		for (int i=0; i<10; i++){
			threads[i].start();
		}
	}
}
