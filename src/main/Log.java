package main;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;


import java.io.File;
import java.util.ArrayList;
import java.util.Date; 
 
import org.jdom.Element; 
import org.jopendocument.dom.ODPackage; 
import org.jopendocument.dom.ODSingleXMLDocument;
import org.jopendocument.dom.OOUtils; 
import org.jopendocument.dom.spreadsheet.Table; 


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import org.jopendocument.dom.spreadsheet.Sheet;
import org.jopendocument.dom.spreadsheet.SpreadSheet;

public class Log {	
	
	private String archivoLogGeneral;
	private String archivoLogDisparos;
	List<List<String>> logGeneral = new ArrayList<List<String>>();
	List<List<String>> logDisparos = new ArrayList<List<String>>();
	public int disparos = 0;		// variable que indica la cantidad de disparos total que se realizará en la ejecución del programa.
	int banderita = 1;
	
	public Log(String logGeneral,String logDisparos) {
		this.archivoLogGeneral = logGeneral;
		this.archivoLogDisparos = logDisparos;
		crearPlanilla();
	}
	
	
	void crearPlanilla() {
		logGeneral.add(new ArrayList<String>());	//creo la 1er sublista que representa la 1er columna (tiempo)
		logGeneral.add(new ArrayList<String>());	//creo la 2da sublista que representa la 2da columna (acción)
		logDisparos.add(new ArrayList<String>());
		logDisparos.add(new ArrayList<String>());
	}
	
	void escribir(Long tiempo, String accion) {
		if (banderita == 1) {			//uso la banderita para que ningún hilo siga escribiendo en el arreglo cuando quiero exportar la info a la planilla de cálculo.
			logGeneral.get(0).add(Long.toString(tiempo));	//agrego el tiempo a la 1er columna
			logGeneral.get(1).add(accion);					//agrego la accion a la 2da columna
		}
	}
	
	void escribir2(Long tiempo, String accion) {
		if (banderita == 1) {			//uso la banderita para que ningún hilo siga escribiendo en el arreglo cuando quiero exportar la info a la planilla de cálculo.
			logDisparos.get(0).add(Long.toString(tiempo));	//agrego el tiempo a la 1er columna
			logDisparos.get(1).add(accion);					//agrego la accion a la 2da columna
		}
	}
	
	void finalizarPlanilla() {
		disparos--;
		
		if (disparos == 0 && banderita == 1) {
			banderita = 0;
			final String[][] data = new String[logDisparos.get(0).size()][2];
			for (int i = 0;i<logDisparos.get(0).size();i++) {
				data[i] = new String[] {logDisparos.get(0).get(i),logDisparos.get(1).get(i)};			
			}			
			try {
				String[] columnas = new String[] { "Tiempo", "Accion" };
				TableModel model = new DefaultTableModel(data, columnas);
				final File file = new File(archivoLogDisparos);
				SpreadSheet.createEmpty(model).saveAs(file);
			}	catch (FileNotFoundException e) {
				//ErrorManager.showErrorMessage("createOds", e.toString());
				} catch (IOException e) {
				//ErrorManager.showErrorMessage("createOds", e.toString());
				} catch (IllegalArgumentException e) {
				//ErrorManager.showErrorMessage("createOds", e.toString());
				} catch (Exception e){
				//ErrorManager.showErrorMessage("createOds", e.toString());
				}
			
			final String[][] data2 = new String[logGeneral.get(0).size()][2];
			for (int i = 0;i<logGeneral.get(0).size();i++) {
				data2[i] = new String[] {logGeneral.get(0).get(i),logGeneral.get(1).get(i)};			
			}			
			try {
				String[] columnas = new String[] { "Tiempo", "Accion" };
				TableModel model = new DefaultTableModel(data2, columnas);
				final File file = new File(archivoLogGeneral);
				SpreadSheet.createEmpty(model).saveAs(file);
			}	catch (FileNotFoundException e) {
				//ErrorManager.showErrorMessage("createOds", e.toString());
				} catch (IOException e) {
				//ErrorManager.showErrorMessage("createOds", e.toString());
				} catch (IllegalArgumentException e) {
				//ErrorManager.showErrorMessage("createOds", e.toString());
				} catch (Exception e){
				//ErrorManager.showErrorMessage("createOds", e.toString());
				}
			
			Ventana mensajeFinal = new Ventana();
		}
	}
}
