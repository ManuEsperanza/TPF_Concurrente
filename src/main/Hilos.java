package main;

public class Hilos extends Thread {
	private GestorDeMonitor monitor;
	private int[] transicionesHilo;
	static String[] nombre = {"Hilo 0 - \"Ingreso de vehículos\" T:19,20,21","Hilo 1 - \"Control de barrera de entrada 1\" T:1,4","Hilo 2 - \"Control de barrera de entrada 2\" T:2,5","Hilo 3 - \"Control de barrera de entrada 3\" T:3,6","Hilo 4 - \"Acceso a los estacionamientos\" T:7,8,23,24","Hilo 5 - \"Salida del piso 1\" T:9","Hilo 6 - \"Salida del piso 2\" T:10","Hilo 7 - \"Control de barrera de salida 1\" T:11,15","Hilo 8 - \"Control de barrera de salida 2\" T:12,16","Hilo 9 - \"Control de la caja\" T:13,14"};
	
	public Hilos(GestorDeMonitor monitor,int[] transicionesHilo) {
		this.monitor = monitor;
		this.transicionesHilo = transicionesHilo;
	}
	
	static public void nombreDelHilo(int id) {
		System.out.println(nombre[id]);
	}
	
	public void run() {
		while(true) {	
			monitor.dispararTransicion(this.transicionesHilo);
		}
	}
	
}
