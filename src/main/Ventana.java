package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class Ventana extends JFrame implements ActionListener {
	public static int cantidadDisparos=0;			// variable que contiene la cantidad de disparos
	private Politicas politicas;
	private JLabel Etiqueta;			//Etiquetas de comentarios que estan dentro de la ventana
	private JLabel Etiqueta2;
	private JLabel Etiqueta3;
	private JLabel Etiqueta4;
	private JLabel Etiqueta5;			//Las Etiquetas 5 y 6 son para la segunda ventana
	private JLabel Etiqueta6;
	private JRadioButton opcion1; 				//boton para la primera opcion
	private JRadioButton opcion2;				//boton para la segunda opcion
	private JButton	botonComenzar;					//boton de para comenzar la ejecucion
	private JTextField campoDisparos;		//campo de texto para escribir la cantidad de disparos
	private String campo;					//variable que usamos para guardar lo seleccionado en campoDisparos
	
	public Ventana(Politicas politicas){	//VENTANA PRINCIPAL
		this.politicas = politicas;
			
		//instanciamos los objetos para crear las etiquetas
		Etiqueta = new JLabel();
		Etiqueta2 = new JLabel();
		Etiqueta3 = new JLabel();
		Etiqueta4 = new JLabel();
		
		//Seteamos la Etiqueta
		Etiqueta.setText("Seleccione la politica deseada: ");
		Etiqueta.setBounds(10,5,500,15); // x,y, ancho, alto 
		add(Etiqueta);
			
		//Seteamos la Etiqueta2
		// perteneciente a la opcion 1
		Etiqueta2.setText("Prioridad llenar de vehículos planta baja y luego habilitar el piso superior. Prioridad salida indistinta.");
		Etiqueta2.setBounds(30,25,800,15); // x,y, ancho, alto 
		add(Etiqueta2);
			
		//Seteamos la Etiqueta3
		// perteneciente a la opcion 2
		Etiqueta3.setText("Prioridad llenado indistinta. Prioridad salida a calle 2.");
		Etiqueta3.setBounds(30,45,500,15); // x,y, ancho, alto 
		add(Etiqueta3);
			
		//Seteamos la Etiqueta4
		// perteneciente al campo de texto para rellenar
		Etiqueta4.setText("Elija la cantidad de disparos a realizar: ");
		Etiqueta4.setBounds(10,85,500,15); // x,y, ancho, alto 
		add(Etiqueta4);
		
		//Seteamos la opcion 1
		opcion1 = new JRadioButton();
		opcion1.setBounds(5, 25, 20, 15);
		add(opcion1);
		
		//Seteamos la opcion 2
		opcion2 = new JRadioButton();
		opcion2.setBounds(5, 45, 20, 15);
		add(opcion2);
		
		//Seteamos el campo de texto que sirve para indicar la cantidad de autos que ingresarán
		campoDisparos = new JTextField();
		campoDisparos.setBounds(290, 83, 50, 20);
		add(campoDisparos);
		    
		//Seteamos el boton Comenzar
		botonComenzar = new JButton();
		//botonComenzar.setBounds(470, 83, 110, 20);   //boton ubicado al costado del campo texto
		botonComenzar.setBounds(395, 115, 110, 20);		//boton ubicado abajo del campo texto
		botonComenzar.setText("Comenzar");
		add(botonComenzar);
			
		//Creamos un grupo de botones, para que cuando se seleccione uno, los demas de deseleccionen solos.
		ButtonGroup grupoBotones = new ButtonGroup();
		grupoBotones.add(opcion1);
		grupoBotones.add(opcion2);
		
		//Aqui se queda esperando que se seleccione una opcion o un boton para luego realizar una accion
		opcion1.addActionListener(this);
		opcion2.addActionListener(this);
		botonComenzar.addActionListener(this);
			
		//inicializo la ventana
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //al cerrar la ventana se detiene el programa
		setSize(900,180); //seteamos el tamaño de la ventana
		setLocationRelativeTo(null); //centra la ventana en la pantalla
		setLayout(null); //elimina toda plantilla
		setResizable(false); // evita modificar el tamaño de ventana
		setTitle("*--Playa de estacionamiento--*"); //Titulo de la ventana
		setVisible(true); //hace visible la ventana
	}	
		
	public Ventana() {	//VENTANA SECUNDARIA
			
			
		//inicializo la Subventana
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //al cerrar la ventana se detiene el programa
		setSize(400,130); //seteamos el tamaño de la ventana
		setLocationRelativeTo(null); //centra la ventana en la pantalla
		setLayout(null); //elimina toda plantilla
		setResizable(false); // evita modificar el tamaño de ventana
		setTitle("*Fin del programa*"); //Titulo de la ventana
		setVisible(true); //hace visible la ventana	
			
		//Configuracion de las etiquetas
		Etiqueta5 = new JLabel();
		Etiqueta5.setText("Ejecucion Finalizada");
		Etiqueta5.setBounds(120,35,160,15); // x,y, ancho, alto 
		add(Etiqueta5);	
		Etiqueta6 = new JLabel();
		Etiqueta6.setText("La ejecucion se guardó en un archivo Log.ods");
		Etiqueta6.setBounds(24,65,330,15); // x,y, ancho, alto 
		add(Etiqueta6);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		//Realiza la Accion correspondiente al seleccionar cada opcion
		if(e.getSource().equals(opcion1)){
			
			politicas.prioridadesT = politicas.prioridadesT_a;
			politicas.prioridadesH = politicas.prioridadesH_a;
		}
		if(e.getSource().equals(opcion2)){
			politicas.prioridadesT = politicas.prioridadesT_b;
			politicas.prioridadesH = politicas.prioridadesH_b; 
        }
		if(e.getSource().equals(botonComenzar)) {
			campo = campoDisparos.getText();		//guardamos en la variable String llamada campo, la cantidad de disparos seleccionados
			cantidadDisparos = Integer.parseInt(campo);	// convertimos la variable anterior a una variable de tipo entero
		}
	}

}
