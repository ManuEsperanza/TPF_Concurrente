package test;
import main.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class TestInvariantesP {
	
	private final Politicas politicaT = new Politicas();
	private final Log logT = new Log("/home/manu3/Documentos/Concurrente/Trabajo Final/LogGeneralT.ods","/home/manu3/Documentos/Concurrente/Trabajo Final/LogDisparosT.ods");
	private final Mutex mutexT = new Mutex(1,logT);		
	private final SensibilizadoConTiempo sensibilizadoConTiempoT = new SensibilizadoConTiempo(false);
	private final RedDePetri redDePetriT = new RedDePetri(politicaT,sensibilizadoConTiempoT,mutexT,logT);
	String ruta = "/home/manu3/Documentos/Concurrente/Trabajo Final/Matrices/matricesInvarariantesT.html";	//ruta donde se encuentra el archivo html que contiene las matrices exportadas del pipe	
	private final Matrices matricesT = new Matrices(ruta,redDePetriT);			//levanta las matrices del archivo exportado por el pipe.
	//GestorDeMonitor monitor = new GestorDeMonitor(redDePetriT, transicionesPorHilo,politicaT,sensibilizadoConTiempoT,mutexT);
	//Transcisión: 0 1 10 11 12 13 14 15 16 19 2 20 21 23 24 3 4 5 6 7 8 9	
	int[][] disparos = {{0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0},{0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0},{0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0},{1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},{0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},{0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0},{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0},{0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0},{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0},{0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},{0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},{0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0},{0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0}}; 
	//Plazas:      0 1 10 11 12	13 14 15 16	17 18 19 2  20 21 22 26 29 3  30 31 32 4  5  6  7  8  9
				// 0 1 2  3  4  5  6  7  8  9  10 11 12 13 14 15 16 17 18 19 20 21 22 23 24 25 26 27	
	@Test
	void testInvariantesPlaza1() throws InterruptedException {
		Thread.currentThread().setName("0");
		//M(P0) + M(P1) + M(P10) + M(P11) + M(P12) + M(P2) + M(P26) + M(P29) + M(P3) + M(P31) + M(P4) + M(P5) + M(P9) = 60
		
		int[] 	plazas = {0,1,2,3,4,12,16,17,18,20,22,23,27}; //estas son las plazas correspondiente al invariante 1.
		int cantTokens = 0;
		for (int j=0;j<10;j++) {	//disparamos  10 veces todas las transiciones.
			for (int i=0;i<disparos.length;i++) {
				redDePetriT.sensibilizadas();
				redDePetriT.disparar(disparos[i]);
			}
		}	
		for (int i=0;i<plazas.length;i++) {
			cantTokens += redDePetriT.marcado.get(plazas[i]); //contabilizamos  la cantidad de tokes en las plazas de interés.
		}
		assertEquals(cantTokens,60);
	}
	
	@Test
	void testInvariantesPlaza2() throws InterruptedException {
		//M(P11) + M(P13) = 30
		int[] 	plazas = {3,5}; //estas son las plazas correspondiente al invariante 2.
		int cantTokens = 0;
		for (int j=0;j<10;j++) {	//disparamos  10 veces todas las transiciones.
			for (int i=0;i<disparos.length;i++) {
				redDePetriT.sensibilizadas();
				redDePetriT.disparar(disparos[i]);
			}
		}	
		for (int i=0;i<plazas.length;i++) {
			cantTokens += redDePetriT.marcado.get(plazas[i]); //contabilizamos  la cantidad de tokes en las plazas de interés.
		}
		assertEquals(cantTokens,30);
	}
	
	@Test
	void testInvariantesPlaza3() throws InterruptedException {
		//M(P12) + M(P14) + M(P29) + M(P31) = 30
		int[] 	plazas = {4,6,17,20}; //estas son las plazas correspondiente al invariante 3.
		int cantTokens = 0;
		for (int j=0;j<10;j++) {	//disparamos  10 veces todas las transiciones.
			for (int i=0;i<disparos.length;i++) {
				redDePetriT.sensibilizadas();
				redDePetriT.disparar(disparos[i]);
			}
		}	
		for (int i=0;i<plazas.length;i++) {
			redDePetriT.sensibilizadas();
			cantTokens += redDePetriT.marcado.get(plazas[i]); //contabilizamos  la cantidad de tokes en las plazas de interés.
		}
		assertEquals(cantTokens,30);
	}
	
	@Test
	void testInvariantesPlaza4() throws InterruptedException {
		//M(P16) + M(P18) + M(P20) = 1
		int[] 	plazas = {8,10,13}; //estas son las plazas correspondiente al invariante 3.
		int cantTokens = 0;
		for (int j=0;j<10;j++) {	//disparamos  10 veces todas las transiciones.
			for (int i=0;i<disparos.length;i++) {
				redDePetriT.sensibilizadas();
				redDePetriT.disparar(disparos[i]);
			}
		}	
		for (int i=0;i<plazas.length;i++) {
			cantTokens += redDePetriT.marcado.get(plazas[i]); //contabilizamos  la cantidad de tokes en las plazas de interés.
		}
		assertEquals(cantTokens,1);
	}
	
	@Test
	void testInvariantesPlaza5() throws InterruptedException {
		//M(P17) + M(P19) + M(P21) = 1
		int[] 	plazas = {9,11,14}; //estas son las plazas correspondiente al invariante 3.
		int cantTokens = 0;
		for (int j=0;j<10;j++) {	//disparamos  10 veces todas las transiciones.
			for (int i=0;i<disparos.length;i++) {
				redDePetriT.sensibilizadas();
				redDePetriT.disparar(disparos[i]);
			}
		}	
		for (int i=0;i<plazas.length;i++) {
			cantTokens += redDePetriT.marcado.get(plazas[i]); //contabilizamos  la cantidad de tokes en las plazas de interés.
		}
		assertEquals(cantTokens,1);
	}
	
	@Test
	void testInvariantesPlaza6() throws InterruptedException {
		//M(P20) + M(P21) + M(P22) = 1
		int[] 	plazas = {13,14,15}; //estas son las plazas correspondiente al invariante 3.
		int cantTokens = 0;
		for (int j=0;j<10;j++) {	//disparamos  10 veces todas las transiciones.
			for (int i=0;i<disparos.length;i++) {
				redDePetriT.sensibilizadas();
				redDePetriT.disparar(disparos[i]);
			}
		}	
		for (int i=0;i<plazas.length;i++) {
			cantTokens += redDePetriT.marcado.get(plazas[i]); //contabilizamos  la cantidad de tokes en las plazas de interés.
		}
		assertEquals(cantTokens,1);
	}
	
	@Test
	void testInvariantesPlaza7() throws InterruptedException {
		//M(P29) + M(P30) + M(P31) = 1
		int[] 	plazas = {17,19,20}; //estas son las plazas correspondiente al invariante 3.
		int cantTokens = 0;
		for (int j=0;j<10;j++) {	//disparamos  10 veces todas las transiciones.
			for (int i=0;i<disparos.length;i++) {
				redDePetriT.sensibilizadas();
				redDePetriT.disparar(disparos[i]);
			}
		}	
		for (int i=0;i<plazas.length;i++) {
			cantTokens += redDePetriT.marcado.get(plazas[i]); //contabilizamos  la cantidad de tokes en las plazas de interés.
		}
		assertEquals(cantTokens,1);
	}
	
	@Test
	void testInvariantesPlaza8() throws InterruptedException {
		//M(P3) + M(P6) = 1
		int[] 	plazas = {18,24}; //estas son las plazas correspondiente al invariante 3.
		int cantTokens = 0;
		for (int j=0;j<10;j++) {	//disparamos  10 veces todas las transiciones.
			for (int i=0;i<disparos.length;i++) {
				redDePetriT.sensibilizadas();
				redDePetriT.disparar(disparos[i]);
			}
		}	
		for (int i=0;i<plazas.length;i++) {
			cantTokens += redDePetriT.marcado.get(plazas[i]); //contabilizamos  la cantidad de tokes en las plazas de interés.
		}
		assertEquals(cantTokens,1);
	}
	
	@Test
	void testInvariantesPlaza9() throws InterruptedException {
		//M(P4) + M(P7) = 1
		int[] 	plazas = {22,25}; //estas son las plazas correspondiente al invariante 3.
		int cantTokens = 0;
		for (int j=0;j<10;j++) {	//disparamos  10 veces todas las transiciones.
			for (int i=0;i<disparos.length;i++) {
				redDePetriT.sensibilizadas();
				redDePetriT.disparar(disparos[i]);
			}
		}	
		for (int i=0;i<plazas.length;i++) {
			cantTokens += redDePetriT.marcado.get(plazas[i]); //contabilizamos  la cantidad de tokes en las plazas de interés.
		}
		assertEquals(cantTokens,1);
	}
	
	@Test
	void testInvariantesPlaza10() throws InterruptedException {
		//M(P5) + M(P8) = 1
		int[] 	plazas = {23,26}; //estas son las plazas correspondiente al invariante 3.
		int cantTokens = 0;
		for (int j=0;j<10;j++) {	//disparamos  10 veces todas las transiciones.
			for (int i=0;i<disparos.length;i++) {
				redDePetriT.sensibilizadas();
				redDePetriT.disparar(disparos[i]);
			}
		}	
		for (int i=0;i<plazas.length;i++) {
			cantTokens += redDePetriT.marcado.get(plazas[i]); //contabilizamos  la cantidad de tokes en las plazas de interés.
		}
		assertEquals(cantTokens,1);
	}

}
