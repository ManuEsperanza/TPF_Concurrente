package test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import main.Log;
import main.Matrices;
import main.Mutex;
import main.Politicas;
import main.RedDePetri;
import main.SensibilizadoConTiempo;

class TestSensibilizadoConTiempo {
	private final Politicas politicaT = new Politicas();
	private final Log logT = new Log("/home/manu3/Documentos/Concurrente/Trabajo Final/LogGeneralT.ods","/home/manu3/Documentos/Concurrente/Trabajo Final/LogDisparosT.ods");
	private final Mutex mutexT = new Mutex(1,logT);		
	private final SensibilizadoConTiempo sensibilizadoConTiempoT = new SensibilizadoConTiempo(true);
	private final RedDePetri redDePetriT = new RedDePetri(politicaT,sensibilizadoConTiempoT,mutexT,logT);
	String ruta = "/home/manu3/Documentos/Concurrente/Trabajo Final/Matrices/matricesSensibilizadoTiempoT.html";	//ruta donde se encuentra el archivo html que contiene las matrices exportadas del pipe	
	private final Matrices matricesT = new Matrices(ruta,redDePetriT);			//levanta las matrices del archivo exportado por el pipe.
	//private final GestorDeMonitor monitor = new GestorDeMonitor(redDePetriT, main.transicionesPorHilo,politicaT,sensibilizadoConTiempoT,mutexT);
	
	@Test
	void testSensibilizadoConTiempo() {
	//primero disparamos la transicion que ubica al token en el estacionamiento del piso 1.
	//luego inmediatamente intentamos disparar la transicion que libera dicho token.
	//como dicha transicion está sensibilizada con tiempo, no debería dispararse.
		Thread.currentThread().setName("0");
		   //Transcisión: 0 1 10 11 12 13 14 15 16 19 2 20 21 23 24 3 4 5 6 7 8 9
		int[] disparo1 = {0,0,0, 0, 0, 0, 0, 0, 0, 0, 0,0, 0, 0, 0, 0,0,0,0,1,0,0};
		   //Transcisión: 0 1 10 11 12 13 14 15 16 19 2 20 21 23 24 3 4 5 6 7 8 9
		int[] disparo2 = {0,0,0, 0, 0, 0, 0, 0, 0, 0, 0,0, 0, 0, 0, 0,0,0,0,0,0,1};
		redDePetriT.sensibilizadas();
		redDePetriT.disparar(disparo1);
		redDePetriT.sensibilizadas();
		redDePetriT.disparar(disparo2);
		int tokenP11 = redDePetriT.marcado.get(3);	//el 3 indica la plaza 11. (autos estacionados en el piso 1).
		assertEquals(tokenP11,1);					//comprobamos que el token esté en la plaza 11.
		int tokenP15 = redDePetriT.marcado.get(7);	//el 7 corresponde a la plaza 15.
		System.out.println(tokenP15);
		assertEquals(tokenP15,0);					//dicha plaza no debería tener tokens.
	}

}
