package test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import main.Log;
import main.Matrices;
import main.Mutex;
import main.Politicas;
import main.RedDePetri;
import main.SensibilizadoConTiempo;

class TestRedDePetri {
	private final Politicas politicaT = new Politicas();
	private final Log logT = new Log("/home/manu3/Documentos/Concurrente/Trabajo Final/LogGeneralT.ods","/home/manu3/Documentos/Concurrente/Trabajo Final/LogDisparosT.ods");
	private final Mutex mutexT = new Mutex(1,logT);		
	private final SensibilizadoConTiempo sensibilizadoConTiempoT = new SensibilizadoConTiempo(true);
	private final RedDePetri redDePetriT = new RedDePetri(politicaT,sensibilizadoConTiempoT,mutexT,logT);
	String ruta = "/home/manu3/Documentos/Concurrente/Trabajo Final/Matrices/matrices.html";	//ruta donde se encuentra el archivo html que contiene las matrices exportadas del pipe	
	private final Matrices matricesT = new Matrices(ruta,redDePetriT);			//levanta las matrices del archivo exportado por el pipe.
	
	@Test
	void testDisparoSinSensiblizado() {
	//dado un marcado inicial.
	//intentaremos disparar una transición que no está sensibilizada.
	//como resultado deberíamos esperar encontrar 0 tokens en la plaza destino.
		Thread.currentThread().setName("0");   
		   //Transcisión: 0 1 10 11 12 13 14 15 16 19 2 20 21 23 24 3 4 5 6 7 8 9
		int[] disparo1 = {0,0,0, 0, 0, 0, 0, 0, 0, 1, 0,0, 0, 0, 0, 0,0,0,0,0,0,0};
		redDePetriT.sensibilizadas();
		redDePetriT.disparar(disparo1);
		int tokenP0 = redDePetriT.marcado.get(0);	//el 0 indica la plaza 0. (Ingreso a Barrera 1).
		assertEquals(tokenP0,0);
	}
	
	@Test
	void testDisparoConSensiblizado() {
	//dado un marcado inicial.
	//intentaremos disparar una transición que sí está sensibilizada.
	//como resultado deberíamos esperar encontrar 1 token en la plaza destino. 
		   //Transcisión: 0 1 10 11 12 13 14 15 16 19 2 20 21 23 24 3 4 5 6 7 8 9
		int[] disparo1 = {1,0,0, 0, 0, 0, 0, 0, 0, 0, 0,0, 0, 0, 0, 0,0,0,0,0,0,0};
		redDePetriT.sensibilizadas();
		redDePetriT.disparar(disparo1);
		int tokenP26 = redDePetriT.marcado.get(16);	//el 16 indica la plaza 26. (llegada de un auto)
		assertEquals(tokenP26,1);
	}
	
	@Test
	void testSensibilizado() {
	//dado un marcado inicial	
	//mediante un disparo sensibilizaremos algunas transiciónes que no estaban sensibilizadas.	
	//luego debería encontrarse como sensibilizadas dichas transiciones.
		  //Transcisión: 0 1 10 11 12 13 14 15 16 19 2 20 21 23 24 3 4 5 6 7 8 9
		int[] disparo = {1,0,0, 0, 0, 0, 0, 0, 0, 0, 0,0, 0, 0, 0, 0,0,0,0,0,0,0};
		redDePetriT.sensibilizadas();
		int sensibilizadoT19 = redDePetriT.sensibilizadas.get(9); //el 9 corresponde a la transicion 19
		assertEquals(sensibilizadoT19,0);	//antes  de disparar corroboramos que no esté sensibilizada.
		redDePetriT.disparar(disparo);
		redDePetriT.sensibilizadas();
		sensibilizadoT19 = redDePetriT.sensibilizadas.get(9);
		assertEquals(sensibilizadoT19,1);	//luego del disparo corroboramos que la transición esté sensibilizada.
	}
}
