package main;
import java.util.*;
import java.util.concurrent.Semaphore;

public class Colas {
	
	private Mutex mutex;
	private Log log;
	List<Integer> hilosBloqueados = new ArrayList<Integer>(Collections.nCopies(10, 0)); 		//array que determina mediante un 1, qué hilos están bloqueados en la cola. lo inicializo en cero.
	List<Integer> hilosBloqueadosListos = new ArrayList<Integer>(Collections.nCopies(10, 0));	//array que determina mediante un 1, qué hilos bloqueados pueden disparar alguna transición sensibilizada.	 													
	Mutex colasCondicion[] = new Mutex[10];
	
	public Colas(Mutex mutex,Mutex colasCondicion[],Log log) {	// se tiene que pasar como argumentos un semaforo de tipo Mutex
		this.mutex = mutex;
		this.colasCondicion = colasCondicion;
		this.log = log;
		for (int i = 0;i<10;i++) {
			colasCondicion[i] = new Mutex(0,log);
		}
	}						 
	
	void delay() {	
		hilosBloqueados.set(Integer.parseInt(Thread.currentThread().getName()),1);		//pongo en uno el elemento del array correspondiente a dicho hilo, en representacion de que está bloqueado.	
		log.escribir(System.nanoTime(),"El "+Hilos.nombre[Integer.parseInt(Thread.currentThread().getName())]+" se bloquea en la cola dentro del monitor");
		System.out.println("El "+Hilos.nombre[Integer.parseInt(Thread.currentThread().getName())]+" se bloquea en la cola dentro del monitor");
		colasCondicion[Integer.parseInt(Thread.currentThread().getName())].acquire();	//se bloquea el hilo en la cola de variable de condicion correspondiente a dicho hilo.
	}
	
	void resume(List<Integer> decision) {	
		for (int i = 0; i < 10;i++) {
			if (decision.get(i) == 1) {
				hilosBloqueados.set(i,0);										//marco como liberado dicho hilo
				hilosBloqueadosListos.set(i,0);	
				colasCondicion[i].release();								//libero el unico hilo bloqueado que tiene transiciones sensibilizadas listas para disparar.
				log.escribir(System.nanoTime(),"Hilo "+i +" desbloqueado.");
				System.out.println("Hilo "+i +" desbloqueado.");
			}	
		}	
	}
	
	public List<Integer> quienesEstan() {
		return hilosBloqueados;
	}
	
	void mostrarHilosBloqueados() {
		int suma1 = 0,suma2 = 0;
		String hBloqueados = "";
		String hBloqueadosListos = "";
		for (int i = 0;i<10;i++) {
			if (hilosBloqueados.get(i) == 1) {
				hBloqueados += " " +Integer.toString(i);
				suma1++; 
			}
		}
		if (suma1 == 0) {
			log.escribir(System.nanoTime(), "No hay hilos bloqueados en la cola.");
			System.out.println("No hay hilos bloqueados en la cola.");
		}
		else if (suma1 == 1) {
			log.escribir(System.nanoTime(), "El hilo "+ hBloqueados +" está bloqueado");
			System.out.println("El hilo "+ hBloqueados +" está bloqueado");
		}
		else {
			log.escribir(System.nanoTime(), "los hilos "+ hBloqueados +" están bloqueados");
			System.out.println("los hilos "+ hBloqueados +" están bloqueados");
		}
		for (int i = 0;i<10;i++) {
			if (hilosBloqueadosListos.get(i) == 1) {
				hBloqueadosListos += " " + Integer.toString(i);
				suma2++; 
			}
		}
		if (suma2 == 0) {
			log.escribir(System.nanoTime(), "No hay hilos bloqueados listos para ser desbloqueados.");
			System.out.println("No hay hilos bloqueados listos para ser desbloqueados.");
		}
		else if (suma2 == 1) {
			log.escribir(System.nanoTime(), "El hilo "+ hBloqueadosListos +" está bloqueado y listo para ser desbloqueado.");
			System.out.println("El hilo "+ hBloqueadosListos +" está bloqueado y listo para ser desbloqueado.");
		}
		else {
			log.escribir(System.nanoTime(), "los hilos "+ hBloqueadosListos +" están bloqueados y listos para ser desbloqueados");
			System.out.println("los hilos "+ hBloqueadosListos +" están bloqueados y listos para ser desbloqueados");
		}
	}
}

