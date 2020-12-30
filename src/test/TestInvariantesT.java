package test;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import main.Log;
import main.Matrices;
import main.Mutex;
import main.Politicas;
import main.RedDePetri;
import main.SensibilizadoConTiempo;

class TestInvariantesT {
	
	private final Politicas politicaT = new Politicas();
	private final Log logT = new Log("/home/manu3/Documentos/Concurrente/Trabajo Final/LogGeneralT.ods","/home/manu3/Documentos/Concurrente/Trabajo Final/LogDisparosT.ods");
	private final Mutex mutexT = new Mutex(1,logT);		
	private final SensibilizadoConTiempo sensibilizadoConTiempoT = new SensibilizadoConTiempo(false);
	private final RedDePetri redDePetriT = new RedDePetri(politicaT,sensibilizadoConTiempoT,mutexT,logT); //instanciamos 2 objetos de la red de petri, para comparar el marcado inicial con el actual luego de haber realizado la secuencia de disparos
	private final RedDePetri redDePetri2T = new RedDePetri(politicaT,sensibilizadoConTiempoT,mutexT,logT);
	String ruta = "/home/manu3/Documentos/Concurrente/Trabajo Final/Matrices/matrices.html";	//ruta donde se encuentra el archivo html que contiene las matrices exportadas del pipe	
	private final Matrices matricesT = new Matrices(ruta,redDePetriT);			//levanta las matrices del archivo exportado por el pipe.  (matriz en la cual solo usamos para ver su marcado inicial)
	private final Matrices matrices2T = new Matrices(ruta, redDePetri2T);		//matriz utilizada para ver el marcado actual despues de ejecutar cada secuencia de disparos 
	
	@Test
	void testInvarianteTrans0() {
		Thread.currentThread().setName("0");
		//Secuencia de Transiciones disparadas: 0,19,1,4,23,8,10,24,11,13,15
		
		//Transcisión: 0 1 10 11 12 13 14 15 16 19 2 20 21 23 24 3 4 5 6 7 8 9	
		int[][] disparos = {{1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0},{0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0},{0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0},{0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0},{0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},{0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0}};
		for (int i=0;i<disparos.length;i++) {//realizamos los disparos correspondientes a la secuencia de transiciones dada.
			redDePetri2T.sensibilizadas();
			redDePetri2T.disparar(disparos[i]);
		}
		for(int j=0; j<redDePetriT.marcado.size(); j++) {//comparamos el marcado inicial con el marcado actual luego de haber disparado la secuencia de transiciones dada.
		assertEquals(redDePetri2T.marcado.get(j),redDePetriT.marcado.get(j));
		//fail("Not yet implemented");
		}

	}
	@Test
	void testInvarianteTrans1() {
		//Secuencia de Transiciones disparadas: 0,20,2,5,23,8,10,24,11,13,15
		
		//Transcisión: 0 1 10 11 12 13 14 15 16 19 2 20 21 23 24 3 4 5 6 7 8 9
		int[][] disparos = {{1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0},{0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0},{0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0},{0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},{0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0}};
		for (int i=0;i<disparos.length;i++) {//realizamos los disparos correspondientes a la secuencia de transiciones dada.
			redDePetri2T.sensibilizadas();
			redDePetri2T.disparar(disparos[i]);
		}
		for(int j=0; j<redDePetriT.marcado.size(); j++) {//comparamos el marcado inicial con el marcado actual luego de haber disparado la secuencia de transiciones dada.
			assertEquals(redDePetri2T.marcado.get(j),redDePetriT.marcado.get(j));
			//fail("Not yet implemented");
		}
	}
	@Test
	void testInvarianteTrans2() {
		//Secuencia de Transiciones disparadas: 0,21,3,6,23,8,10,24,11,13,15
		
		//Transcisión: 0 1 10 11 12 13 14 15 16 19 2 20 21 23 24 3 4 5 6 7 8 9
		int[][] disparos = {{1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0},{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0},{0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0},{0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0},{0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},{0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0}};
		for (int i=0;i<disparos.length;i++) {//realizamos los disparos correspondientes a la secuencia de transiciones dada.
			redDePetri2T.sensibilizadas();
			redDePetri2T.disparar(disparos[i]);
		}
		for(int j=0; j<redDePetriT.marcado.size(); j++) {//comparamos el marcado inicial con el marcado actual luego de haber disparado la secuencia de transiciones dada.
			assertEquals(redDePetri2T.marcado.get(j),redDePetriT.marcado.get(j));
			//fail("Not yet implemented");
		}
	}
	@Test
	void testInvarianteTrans3() {
		//Secuencia de Transiciones disparadas: 0,19,1,4,7,9,11,13,15
		
		//Transcisión: 0 1 10 11 12 13 14 15 16 19 2 20 21 23 24 3 4 5 6 7 8 9	
		int[][] disparos = {{1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0},{0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0},{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0},{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},{0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},{0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0}};
		for (int i=0;i<disparos.length;i++) {//realizamos los disparos correspondientes a la secuencia de transiciones dada.
			redDePetri2T.sensibilizadas();
			redDePetri2T.disparar(disparos[i]);
		}
		for(int j=0; j<redDePetriT.marcado.size(); j++) {//comparamos el marcado inicial con el marcado actual luego de haber disparado la secuencia de transiciones dada.
		assertEquals(redDePetri2T.marcado.get(j),redDePetriT.marcado.get(j));
		//fail("Not yet implemented");
		}

	}
}
