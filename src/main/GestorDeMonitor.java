package main;
import java.util.List;
import java.util.concurrent.Semaphore;

public class GestorDeMonitor{
	
	private RedDePetri redDePetri;
	private Politicas politicas;
	private SensibilizadoConTiempo sensibilizadoConTiempo;
	private Mutex mutex;
	private Colas colas;
	private Log log;
	
	int[][] transicionesPorHilo;
	List<Integer> decision;
	long tiempo;

	//constructor
	public GestorDeMonitor(RedDePetri redDePetri,int[][] transicionesPorHilo,Politicas politicas,SensibilizadoConTiempo sensibilizadoContiempo,Mutex mutex,Colas colas,Log log) {
		this.redDePetri = redDePetri;
		this.transicionesPorHilo = transicionesPorHilo;
		this.politicas = politicas;
		this.sensibilizadoConTiempo = sensibilizadoContiempo;
		this.mutex = mutex;
		this.colas = colas;
		this.log = log;
	}
	
	//metodo de exportación para acceder al monitor
	void dispararTransicion(int[] transicionesHilo){
		boolean k = false;
		int cantHilosBloqueados = 0;

		mutex.acquire();
		tiempo = System.nanoTime();
		log.escribir(tiempo,"--> Adentro del monitor "+Hilos.nombre[Integer.parseInt(Thread.currentThread().getName())]);
		System.out.println("Adentro del monitor"+Hilos.nombre[Integer.parseInt(Thread.currentThread().getName())]);
		boolean adentro = true;
		while (adentro == true) { //quiere decir que está dentro del monitor.
			k = redDePetri.disparar(transicionesHilo);
			redDePetri.mostrarMarcado();
			if (k == true) {											//pudo disparar la transicion!
				redDePetri.sensibilizadas();							//actualizo el vector de transiciones sensibilizadas.
				redDePetri.aserciones();								//funcion encargada de ejecutar las aserciones.
				log.finalizarPlanilla();
				List<Integer> hilosBloqueados = colas.quienesEstan();					//pregunto por los hilos que están bloqueados en la colas de variable de condición.
				int bandera = 1;
				for (int i = 0; i < 10;i++) {
					if (hilosBloqueados.get(i) == 1) {
						for (int j = 0; j< redDePetri.sensibilizadas.size();j++) {
							if(redDePetri.sensibilizadas.get(j) == 1 && transicionesPorHilo[i][j] == 1) {
								bandera = 0; 	//bandera que indica que al menos una transicion que quiere disparar el hilo bloqueado está sensibilizada.
								break;	
							}
						}
						if(bandera == 1) {
							colas.hilosBloqueadosListos.set(i,0);
						}
						else {
							bandera = 1;							//reseteo la bandera para la proxima iteración.
							colas.hilosBloqueadosListos.set(i,1);
						}	
					}
					else {
						colas.hilosBloqueadosListos.set(i,0);
					}
				}
				colas.mostrarHilosBloqueados();
				for (int i=0;i<10;i++) {
					cantHilosBloqueados += colas.hilosBloqueadosListos.get(i);		//sumo la cantidad de hilos bloqueados que pueden ser desbloqueados.
				}
				if (cantHilosBloqueados >= 1) {		//si hay hilos bloqueados listos: // se puede cambiar qué prioridad elegimos.
					decision = politicas.cual(colas.hilosBloqueadosListos,politicas.prioridadesH);// hay que elegir a qué hilo desbloquear, si hay uno solo, se desbloqueará ese.
					colas.resume(decision);	
				}		
				adentro = false;				//El hilo ya disparó su transición y liberó o no a los hilos bloqueados listos para continuar.
	
				if (cantHilosBloqueados == 0) {
					mutex.release();									//libero el mutex de acceso al monitor						
				}
			}
			else {	//(K=false)						si no puedo disparar la transicion entonces... 		
				if (sensibilizadoConTiempo.vieneDelSleep[Integer.parseInt(Thread.currentThread().getName())] == true) {	//si viene del sleep
					log.escribir(System.nanoTime(),"El Hilo "+Hilos.nombre[Integer.parseInt(Thread.currentThread().getName())]+" que viene del sleep, abandona el monitor.");
					System.out.println("El Hilo "+Hilos.nombre[Integer.parseInt(Thread.currentThread().getName())]+" que viene del sleep, abandona el monitor.");
					adentro = false;			//es un hilo que viene del sleep por lo q se tiene que ir del monitor.
					sensibilizadoConTiempo.vieneDelSleep[Integer.parseInt(Thread.currentThread().getName())] = false; //reseteo la bandera.
				}
				else {
					mutex.release();					//libero el mutex de acceso al monitor
					colas.delay();	//si no pudo disparar la transicion entonces que se vaya a la cola de variable de condicion correspondiente a dicho hilo.					
				}
			}
				
		}			
	}
}
