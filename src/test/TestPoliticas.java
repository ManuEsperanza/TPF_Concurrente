package test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import main.Log;
import main.Matrices;
import main.Mutex;
import main.Politicas;
import main.RedDePetri;
import main.SensibilizadoConTiempo;

class TestPoliticas {
	private final Politicas politicaT = new Politicas();
	private final Log logT = new Log("/home/manu3/Documentos/Concurrente/Trabajo Final/LogGeneralT.ods","/home/manu3/Documentos/Concurrente/Trabajo Final/LogDisparosT.ods");
	private final Mutex mutexT = new Mutex(1,logT);		
	private final SensibilizadoConTiempo sensibilizadoConTiempoT = new SensibilizadoConTiempo(false);
	private final RedDePetri redDePetriT = new RedDePetri(politicaT,sensibilizadoConTiempoT,mutexT,logT);
	String ruta = "/home/manu3/Documentos/Concurrente/Trabajo Final/Matrices/matricesPoliticasT.html";	//ruta donde se encuentra el archivo html que contiene las matrices exportadas del pipe	
	private final Matrices matricesT = new Matrices(ruta,redDePetriT);			//levanta las matrices del archivo exportado por el pipe.
	   
	
	
	
	@Test
	void testPolitica1() throws InterruptedException{
		Thread.currentThread().setName("0");
		//Prioridad llenar de vehículos planta baja (piso 1) y luego habilitar el piso
		//superior. Prioridad salida indistinta (caja).
		politicaT.prioridadesT = politicaT.prioridadesT_a;
		politicaT.prioridadesH = politicaT.prioridadesH_a;
		   //Transcisión: 0 1 10 11 12 13 14 15 16 19 2 20 21 23 24 3 4 5 6 7 8 9
		int[] disparos = {0,0,0, 0, 0, 0, 0, 0, 0, 0, 0,0, 0, 1, 0, 0,0,0,0,1,0,0};
		//intentamos disparar reiterativamente las transiciones correspondientes a cada piso para comprobar
		//que primero se llena el piso 1.
		for(int i=0;i<30;i++) {
			redDePetriT.sensibilizadas();
			redDePetriT.disparar(disparos);
		}
		int cantTokens=redDePetriT.marcado.get(3); //el 3 indica la plaza 11. (autos estacionados en el piso 1).
		assertEquals(cantTokens,30); 
		
	}
	
	@Test
	void testPolitica2() throws InterruptedException{
		//Prioridad llenado indistinta. Prioridad salida a calle 2.
		politicaT.prioridadesT = politicaT.prioridadesT_b;
		politicaT.prioridadesH = politicaT.prioridadesH_b;
		   //Transcisión: 0 1 10 11 12 13 14 15 16 19 2 20 21 23 24 3 4 5 6 7 8 9
		int[] disparos = {0,0,0, 1, 1, 0, 0, 0, 0, 0, 0,0, 0, 0, 0, 0,0,0,0,0,0,0};
		//intentamos disparar reiterativamente las transiciones correspondientes a cada salida para comprobar
		//que tiene prioridad de salida por la calle 2.
		for(int i=0;i<30;i++) {
			redDePetriT.sensibilizadas();
			redDePetriT.disparar(disparos);
		}
		int cantTokens=redDePetriT.marcado.get(9); //el 9 indica la plaza 17. (Barrera de salida 2).
		assertEquals(cantTokens,30); 
	}

}
