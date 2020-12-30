package main;
import java.util.ArrayList;
//import java.util.Random;
import java.util.List;

public class Politicas {
					
					//Transcisión:  	  1 10 11 12 13 14 15 16 19 2 20 21 23 24 3 4 5 6 7 8 9
	public static int prioridadesT_a[] = {1, 1, 1, 1, 1, 2, 1, 1, 1,1, 1, 1, 3, 1,1,1,1,1,2,1,2}; 	//Prioridad llenar de vehículos planta baja y luego habilitar el piso superior. Prioridad salida indistinta.
	public static int prioridadesT_b[] = {1, 1, 2, 1, 1, 2, 1, 1, 1,1, 1, 1, 2, 1,1,1,1,1,2,1,2};	//Prioridad llenado indistinta. Prioridad salida a calle 2.
	public static int prioridadesT[] = prioridadesT_a; 					  	
	 					   //hilo: 		  0 1 2 3 4 5 6 7 8 9
	public static int prioridadesH_a[] = {4,3,3,3,2,5,5,6,6,1};		//Prioridad llenar de vehículos planta baja y luego habilitar el piso superior. Prioridad salida indistinta.																														
	public static int prioridadesH_b[] = {4,3,3,3,2,5,5,7,6,1};		//Prioridad llenado indistinta. Prioridad salida a calle 2.
	public static int prioridadesH[] = prioridadesH_a;
	
	public Politicas() {
		
	}
	
	public static List<Integer> cual(List<Integer> entrada,int[] prioridades)  {		//este método es el encargado en decidir qué hilo será desbloqueado de la cola, o que  transición dispará un hilo.
		List<Integer> decision = new ArrayList<Integer>();
		int comparador = 8, posicion = 0, cantHilosBloqueadosListos = 1;
		for (int i=0;i<entrada.size();i++) {
			if(entrada.get(i) == 1) {
				decision.add(i,0);
				if (prioridades[i] < comparador){
					comparador = prioridades[i];
					posicion = i;
				}		
				else if (prioridades[i] == comparador) {
						cantHilosBloqueadosListos += 1;
						decision.set(posicion,1);
						decision.set(i,1);
						posicion = i;
				}
			}
			else {
				decision.add(i,0);
			}	
		}
		if (cantHilosBloqueadosListos > 1) {
			List<Integer> decisionAleatoria = elegirAleatoriamente(decision);
			return decisionAleatoria;
		}
		else {
			decision.set(posicion,1);
			return decision;
		}

	}
	
	static public List<Integer> elegirAleatoriamente(List<Integer> decision) {	// ante igualdad de prioridades elegimos un hilo o transicion a disparar aleatoriamente.
		int comparador = 10, posicion = 0;
		for (int i=0; i<decision.size(); i++) {
			if (decision.get(i) == 1) {
				decision.set(i, 1 + (int)(Math.random()*9));  		//a cada hilo bloqueado le asigno un numero random para elegir de manera aleatoria a alguno de ellos.
			}
		}
		for (int i=0;i<decision.size();i++) {
			if(decision.get(i) >= 1) {
				if(decision.get(i) <= comparador) {
					comparador = decision.get(i); 
					decision.set(i,0);
					posicion = i;
				}
				else {
					decision.set(i, 0);
				}	
			}
		}
		decision.set(posicion,1); 
		return decision;
	}

}

	
