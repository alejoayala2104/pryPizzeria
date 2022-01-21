package app;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.HashMap;
import java.util.Map;

import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class AppPizzeria extends Application {
	
	//Se definen los textfield de precios como atributos para poder acceder a ellos en ventas.
	TextField txfPrGrande,txfPrMediana,txfPrPequena;

	private double prGrande=0.0,prMediana=0.0,prPequeña=0.0,
				   totalVentas=0.0, sbtotalVentas=0.0,precioPizzas=0.0, precioLlevar = 0.0;	

	private int cantPizzas = 0;
	
	//Mapa para el conteo de estadísiticas.
	private Map<String,Integer> conteoEstTamaño = new HashMap<>();
	private Map<String,Integer> conteoEstSabor = new HashMap<>();
		
	private static final String ROOT = "-fx-font-family: 'Lucida Sans Unicode';"+
								 		"-fx-background-color: white;";
			
	private static final String BOTON = "-fx-background-color: #008C45;"+
									   	"-fx-text-fill: white;"+
									   	"-fx-background-radius: 10;";
										
	private static final String BOTON_ENCIMA = "-fx-background-color: #00D46A;"+
											   	"-fx-text-fill: white;"+
											   	"-fx-background-radius: 10;";
	
	private static final String BOTON_PRESIONADO = "-fx-background-color: #19FF8C;"+
												   	"-fx-text-fill: white;"+
												   	"-fx-background-radius: 10;";
	
	private static final String TEXTFIELD = "-fx-background-color: white;"+
		   									"-fx-text-fill: #008C45;"+
		   									"-fx-border-color: #008C45;";
	
	private static final String CHECK_IVA ="-fx-text-fill: #008C45;";
	private static final String CHECK_ADICIONALES = "-fx-text-fill:#cd212a";
	private static final String RDB_TAMAÑO = "-fx-text-fill: #0B8C00;";
	private static final String RDB_SABORES = "-fx-text-fill: #D46215;";
	private static final String LABEL = "-fx-font-size: 14;";
	private static final String TEXTAREA = "-fx-font-family: 'Lucida Console';"+
										   "-fx-text-fill: #cd212a;";
	
	@Override
	public void start(Stage primaryStage) throws Exception {
				
		//Pane general
		BorderPane mainPane = new BorderPane();
		mainPane.setMinSize(100, 50);
		mainPane.setCenter(mostrarVentas());
		//Botones
		Button btnVentas = new Button("Ventas");
		btnVentas.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);		
		Button btnPrecios = new Button("Precios");
		btnPrecios.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
		Button btnEst = new Button("Estadísiticas");
		btnEst.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
		Button btnSalir = new Button("Salir");
		btnSalir.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
		
		VBox vbxBotones = new VBox(30,btnVentas,btnPrecios, btnEst, btnSalir);
		vbxBotones.setPadding(new Insets(80,10,70,10));
		mainPane.setLeft(vbxBotones);
		
		//Añadiendo estilos.
		btnVentas.setStyle(BOTON);
		btnVentas.setOnMouseEntered(event->{btnVentas.setStyle(BOTON_ENCIMA);});
		btnVentas.setOnMouseExited(event->{btnVentas.setStyle(BOTON);});
		btnVentas.setOnMousePressed(event->btnVentas.setStyle(BOTON_PRESIONADO));
		
		btnPrecios.setStyle(BOTON);
		btnPrecios.setOnMouseEntered(event->{btnPrecios.setStyle(BOTON_ENCIMA);});
		btnPrecios.setOnMouseExited(event->{btnPrecios.setStyle(BOTON);});
		btnPrecios.setOnMousePressed(event->btnPrecios.setStyle(BOTON_PRESIONADO));
		
		btnEst.setStyle(BOTON);
		btnEst.setOnMouseEntered(event->{btnEst.setStyle(BOTON_ENCIMA);});
		btnEst.setOnMouseExited(event->{btnEst.setStyle(BOTON);});
		btnEst.setOnMousePressed(event->btnEst.setStyle(BOTON_PRESIONADO));
		
		btnSalir.setStyle(BOTON);
		btnSalir.setOnMouseEntered(event->{btnSalir.setStyle(BOTON_ENCIMA);});
		btnSalir.setOnMouseExited(event->{btnSalir.setStyle(BOTON);});
		btnSalir.setOnMousePressed(event->btnSalir.setStyle(BOTON_PRESIONADO));		
		
		mainPane.setStyle(ROOT);
		
		//Carga y display del logotipo.
		Image pizzalogo = new Image(getClass().getResourceAsStream("imagenpizzeria.png"));
		ImageView displaylogo = new ImageView(pizzalogo);
		mainPane.setTop(displaylogo);
		BorderPane.setAlignment(displaylogo, Pos.CENTER);
		
		//Se crea apriori el anchorpane que contiene el gridpane de precios para que
		//los valores en los texfields de los precios se mantengan al cambiar de opciones.
		Parent rootVentas = mostrarVentas();
		Parent rootPrecios = mostrarPrecios();	
		
		//Inicializar el map con las opciones del programa.
		//Para Tamaños
		this.conteoEstTamaño.put("Grande", 0);
		this.conteoEstTamaño.put("Mediana",0);
		this.conteoEstTamaño.put("Pequeña",0);
		//Para sabores
		this.conteoEstSabor.put("Hawaiana",0);
		this.conteoEstSabor.put("Pollo/Champiñones",0);
		this.conteoEstSabor.put("Mixta",0);
		
		//Controles.
		btnVentas.setOnAction(event -> mainPane.setCenter(rootVentas));
		btnPrecios.setOnAction(event -> mainPane.setCenter(rootPrecios));
		btnEst.setOnAction(event -> mainPane.setCenter(mostrarEst()));
		btnSalir.setOnAction(event->{
			primaryStage.close();
			mostrarAlerta(AlertType.INFORMATION, "About", "Creador de la app: Alejandro Ayala Chamorro", null);
		});
		
		Scene scene = new Scene(mainPane,800,500);
		mainPane.setMaxSize(scene.getWidth(), scene.getHeight());
		primaryStage.setTitle("Maui's Pizza: Alejandro Ayala");
		primaryStage.setScene(scene);
		primaryStage.show();

	}
	
	public Parent mostrarVentas() {
		
		//Layout general.
		BorderPane mainVentas = new BorderPane();
		mainVentas.setMinSize(100, 50);
		//Se agrega una margen de lado inferior.
		mainVentas.setPadding(new Insets(0,0,10,0));
		
		//Layout de la parte superior de ventas.		
		TextField txfCantidad = new TextField();
		txfCantidad.setPromptText("Cantidad");
		CheckBox chkbIva = new CheckBox("IVA");
		Button btnFacturar = new Button("Facturar");
		HBox hbxTopVentas = new HBox(50,txfCantidad,chkbIva,btnFacturar);
			hbxTopVentas.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
			hbxTopVentas.setAlignment(Pos.CENTER);
			hbxTopVentas.setPadding(new Insets(10,100,10,0));
		mainVentas.setTop(hbxTopVentas);
		
		//Layout de la parte derecha de ventas.
		Label lblTamano = new Label("Tamaño");
		ToggleGroup grupoTamanos = new ToggleGroup();
		RadioButton rdbGrande = new RadioButton("Grande");
		rdbGrande.setToggleGroup(grupoTamanos);
		RadioButton rdbMediana = new RadioButton("Mediana");
		rdbMediana.setToggleGroup(grupoTamanos);
		RadioButton rdbPequena = new RadioButton("Pequeña");	
		rdbPequena.setToggleGroup(grupoTamanos);
		//Se selecciona por defecto el tamaño grande.
		grupoTamanos.selectToggle(rdbGrande);
		VBox vbxRightVentas = new VBox(30,lblTamano,rdbGrande,rdbMediana,rdbPequena);
			vbxRightVentas.setPadding(new Insets(20));
		mainVentas.setRight(vbxRightVentas);
		
		//Layout de la parte inferior de ventas.
		BorderPane bdpBottomVentas = new BorderPane();
		bdpBottomVentas.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
			//Creación de los controles de "Sabores"
			VBox vbxSabores = new VBox(7);
				vbxSabores.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
				vbxSabores.setAlignment(Pos.CENTER);
				Label lblSabores = new Label("Sabores");
					lblSabores.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
					lblSabores.setAlignment(Pos.CENTER);
				HBox hbxSaborPizza = new HBox(50);
				hbxSaborPizza.setAlignment(Pos.CENTER);
					ToggleGroup grupoSabores = new ToggleGroup();
					RadioButton rdbHawa = new RadioButton("Hawaiana");
					rdbHawa.setToggleGroup(grupoSabores);
					RadioButton rdbPolloChamp = new RadioButton("Pollo/Champiñones");
					rdbPolloChamp.setToggleGroup(grupoSabores);
					RadioButton rdbMixta = new RadioButton("Mixta");
					rdbMixta.setToggleGroup(grupoSabores);
					//Se selecciona por defecto el sabor Hawaiana.
					grupoSabores.selectToggle(rdbHawa);
					hbxSaborPizza.getChildren().addAll(rdbHawa,rdbPolloChamp,rdbMixta);
				vbxSabores.getChildren().addAll(lblSabores, hbxSaborPizza);
			//Creación de controles de "Adicionales"
			VBox vbxAdicionales = new VBox(10);
			vbxAdicionales.setPadding(new Insets(10,60,20,0));
				Label lblAdicionales = new Label("Adicionales");
				CheckBox chkbLlevar = new CheckBox("Para llevar");
				CheckBox chkbBebida = new CheckBox("Bebida");
				vbxAdicionales.getChildren().addAll(lblAdicionales,chkbLlevar,chkbBebida);
			
			bdpBottomVentas.setCenter(vbxSabores);
			bdpBottomVentas.setRight(vbxAdicionales);
		mainVentas.setBottom(bdpBottomVentas);
		
		//Crear y centrar el textarea de ventas
		TextArea txaVentas = new TextArea();
		mainVentas.setCenter(txaVentas);
		//Hacer que el textarea no sea editable por el usuario.
		txaVentas.setEditable(false);
		
		//Añadiendo estilos
		//Botones
		btnFacturar.setStyle(BOTON);
		btnFacturar.setOnMouseEntered(event->{btnFacturar.setStyle(BOTON_ENCIMA);});
		btnFacturar.setOnMouseExited(event->{btnFacturar.setStyle(BOTON);});
		btnFacturar.setOnMousePressed(event->btnFacturar.setStyle(BOTON_PRESIONADO));
		
		txfCantidad.setStyle(TEXTFIELD);
		
		chkbIva.setStyle(CHECK_IVA);
		
		//Tamaño
		rdbGrande.setStyle(RDB_TAMAÑO);
		rdbMediana.setStyle(RDB_TAMAÑO);
		rdbPequena.setStyle(RDB_TAMAÑO);	
		
		//Sabores
		rdbHawa.setStyle(RDB_SABORES);
		rdbMixta.setStyle(RDB_SABORES);
		rdbPolloChamp.setStyle(RDB_SABORES);
		
		//Adicionales		
		chkbBebida.setStyle(CHECK_ADICIONALES);
		chkbLlevar.setStyle(CHECK_ADICIONALES);
		
		//Label
		lblAdicionales.setStyle(LABEL);
		lblSabores.setStyle(LABEL);
		lblTamano.setStyle(LABEL);
		
		txaVentas.setStyle(TEXTAREA);	
		
		//Programación del botón facturar: Genera la factura en el textarea.
		btnFacturar.setOnAction(event ->{
			//Reset de variables.			
			totalVentas=0.0; sbtotalVentas=0.0;
			this.cantPizzas = 0;
			//Obtención y validación de la casilla de cantidad.
			try {
				this.cantPizzas = Integer.parseInt(txfCantidad.getText());
			}catch(Exception e) {
				mostrarAlerta(AlertType.ERROR, "ERROR: Cantidad", "Cantidad inválida", "La casilla cantidad solo debe contener valores enteros.");
				return;
			}
			
			//Validación de precios no definidos. Si algún campo en precios está vacío.
			if(this.txfPrGrande.getText().isEmpty() || this.txfPrMediana.getText().isEmpty()||
					this.txfPrPequena.getText().isEmpty()) {
				mostrarAlerta(AlertType.ERROR, "ERROR: Precios no definidos", "Precios no definidos", "Por favor definalos en la opción Precios.");
				return;
			}
			//Validación campos numéricos en precios.
			try {
				this.prGrande = Double.parseDouble(this.txfPrGrande.getText());
				this.prMediana = Double.parseDouble(this.txfPrMediana.getText());
				this.prPequeña = Double.parseDouble(this.txfPrPequena.getText());
			}catch(Exception e) {
				mostrarAlerta(AlertType.ERROR, "ERROR: Precios inválidos", "Precios no numéricos", "Los campos de precios solo deben contener valores numéricos");
				return;
			}
			
			//Cálculo del precio de las bebidas.
			double precioBebidas=0.0;			
			if(chkbBebida.isSelected()) 
				precioBebidas = 5000.0*this.cantPizzas;				
			
			//Se obtiene el RadioButton seleccionado. Se hace el cast de toggle a radiobutton.
			RadioButton rdbSelTamaño= (RadioButton) grupoTamanos.getSelectedToggle();
			
			//Se añade 1 al contador del tamaño elegido.
			añadirConteo(rdbSelTamaño, this.conteoEstTamaño,this.cantPizzas);
			
			//Obtención del subtotal según el tamaño de la pizza.
			//También se asigna el precio de la caja para llevar.
			if(rdbSelTamaño.equals(rdbGrande)) {
				this.precioPizzas = this.prGrande*this.cantPizzas;	
				this.precioLlevar = 2000.0;
			}
			else if(rdbSelTamaño.equals(rdbMediana)) {
				this.precioPizzas = this.prMediana*this.cantPizzas;	
				this.precioLlevar = 1500.0;
			}
			else{
				this.precioPizzas = this.prPequeña*this.cantPizzas;
				this.precioLlevar = 1000.0;
			}
			
			RadioButton rdbSelSabor = (RadioButton) grupoSabores.getSelectedToggle();
			
			//Se añade 1 al contador del sabor elegido.
			añadirConteo(rdbSelSabor, this.conteoEstSabor,this.cantPizzas);
			
			//Se suma el precio de las bebidas. Si no se seleccionó dicha casilla, el precio es de 0.
			sbtotalVentas = precioPizzas+precioBebidas;
			
			//Cálculo del IVA
			double iva=0.0;
			if(chkbIva.isSelected())
				iva=0.08 * this.sbtotalVentas;
			
			//Cálculo del total de la venta.
			this.totalVentas = this.sbtotalVentas+iva;			
			//Se verifica si se seleccionó la casilla "Para llevar" y se suma el precio de las cajas al resultado.
			if(chkbLlevar.isSelected()) {
				this.precioLlevar = this.precioLlevar*this.cantPizzas;
				this.totalVentas += this.precioLlevar;
			}
			
			
			//Creación de la factura a mostrar.
			StringBuilder stbFactura = new StringBuilder();			
			stbFactura.append("******************************** MAUI'S PIZZA ********************************");
			stbFactura.append("\n-----------------------------| Factura de venta |-----------------------------\n");
			
			//Obtención y formateo de la fecha actual del sistema.
			DateTimeFormatter dtformatter = DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm:ss a");			
			LocalDateTime fechaActual = LocalDateTime.now();			
			String fechaActualStg = dtformatter.format(fechaActual);
			
			stbFactura.append("------------------------>> "+fechaActualStg+" <<-----------------------\n");
			
			stbFactura.append(String.format("Cantidad %15s %53s","Detalle","Precio"));
			stbFactura.append(String.format("\n-------- %15s %54s","-------","------\n"));
			String precioPizzasFormateado = String.format("$ %-,1.1f", this.precioPizzas);
			stbFactura.append(String.format("%4s %11s %-25s %35s", this.cantPizzas,"",rdbSelTamaño.getText()+"/"+rdbSelSabor.getText(),precioPizzasFormateado));
			
			if(chkbBebida.isSelected()) {
				String precioBebidasFormateado = String.format("$ %-,1.1f",precioBebidas);
				stbFactura.append(String.format("\n%4s %11s %-25s %35s", this.cantPizzas,"","Bebidas",precioBebidasFormateado));
			}
			
			stbFactura.append("\n\n");
			String sbtotalVentasFormateado = String.format("$ %-,1.1f", this.sbtotalVentas);
			stbFactura.append(String.format("SUBTOTAL:%69s",sbtotalVentasFormateado));
			if(chkbIva.isSelected()) {
				String ivaFormateado = String.format("$ %-,1.1f", iva);
				stbFactura.append(String.format("\nIMP CONSUMO 8%%: %62s",ivaFormateado));
			}
			if(chkbLlevar.isSelected()) {
				String precioLlevarFormateado = String.format("$ %-,1.1f", this.precioLlevar);
				stbFactura.append(String.format("\nPARA LLEVAR:%66s",precioLlevarFormateado));
			}
			
			stbFactura.append("\n==============================================================================\n");
			String totalVentasFormateado = String.format("$ %-,1.1f", this.totalVentas);
			stbFactura.append(String.format("TOTAL: %71s",totalVentasFormateado));
			//Se muestra la factura en el textarea.
			txaVentas.setText(stbFactura.toString());			
			
		});

		
		return mainVentas;
	}
	
	public Parent mostrarPrecios() {
		
		Label lblPrecios = new Label("Configurar Lista de Precios");
		this.txfPrGrande = new TextField();
		this.txfPrGrande.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
		this.txfPrGrande.setPromptText("$$ Precio Pizza Grande");
		this.txfPrMediana = new TextField();
		this.txfPrMediana.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
		this.txfPrMediana.setPromptText("$$ Precio Pizza Mediana");
		this.txfPrPequena = new TextField();
		this.txfPrPequena.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
		this.txfPrPequena.setPromptText("$$ Precio Pizza Pequeña");
		
		//Creación del gridpane y añadidura de los controles.
		GridPane grpPrecios = new GridPane();
		grpPrecios.add(lblPrecios, 0, 0);
		grpPrecios.add(this.txfPrGrande, 0, 1);
		grpPrecios.add(this.txfPrMediana, 0, 2);
		grpPrecios.add(this.txfPrPequena, 0, 3);

		//Espaciado
		grpPrecios.setVgap(30);	
		
		//Creación de un anchorpane para centrar el gridpane
		AnchorPane acpPrecios = new AnchorPane();
		//Hacer que el anchorpane tenga el tamaño del gridpane para centrarlo (Consulta).		
		acpPrecios.setMaxSize(Control.USE_PREF_SIZE, Control.USE_PREF_SIZE);
		//Se añade el gridpane al anchorpane
		acpPrecios.getChildren().add(grpPrecios);
		
		//Constraints para definir el tamaño del gridpane.
		ColumnConstraints reglaCols = new ColumnConstraints(200);
		grpPrecios.getColumnConstraints().add(reglaCols);
		RowConstraints reglaRows = new RowConstraints(30);
		grpPrecios.getRowConstraints().addAll(reglaRows,reglaRows,reglaRows,reglaRows);
		
		//Adicionando estilos.
		txfPrGrande.setStyle(TEXTFIELD);
		txfPrMediana.setStyle(TEXTFIELD);
		txfPrPequena.setStyle(TEXTFIELD);
		
		lblPrecios.setStyle(LABEL);
		
		
		return acpPrecios;
	}
	
	public Parent mostrarEst() {
		
		StringBuilder stbEstadisticas = new StringBuilder();
		
		int totalVentas=0;
		//Recorre y suma todas los valores del conteo de pizzas por tamaño.
		//La suma de todos esos valores es el total de pizzas vendidas.
		for(Map.Entry<String,Integer> entry : this.conteoEstTamaño.entrySet()) {			
			totalVentas += entry.getValue();
		}
		
		stbEstadisticas.append("\nTotal de pizzas vendidas: " + totalVentas);
		stbEstadisticas.append("\n------------------------------------------\n");
		
		final double totalVentasAux = totalVentas;
		
		stbEstadisticas.append("\nEstadisticas de venta por tamaño:\n");
		this.conteoEstTamaño.forEach((kLenguaje,vConteo) ->{
			//Se redondea a dos decimales.
			stbEstadisticas.append(String.format("♦ %-30s %s",kLenguaje, (double)Math.round((vConteo*100/totalVentasAux) * 100d) / 100d + "% \n"));
		});
		
		stbEstadisticas.append("\nEstadisticas de venta por sabor:\n");
		this.conteoEstSabor.forEach((kLenguaje,vConteo) ->{
			//Se redondea a dos decimales.	
			stbEstadisticas.append(String.format("♦ %-30s %s",kLenguaje, (double)Math.round((vConteo*100/totalVentasAux) * 100d) / 100d + "% \n"));
		});
		
		TextArea txaEst = new TextArea();
		txaEst.setPadding(new Insets(20));
		txaEst.setStyle(TEXTAREA);
		txaEst.setText(stbEstadisticas.toString());
		
		return txaEst;
	}	
	
	//Método que crea y muestra una alerta.
	public void mostrarAlerta(AlertType tipoAlerta,String tituloVentana,String tituloMensaje,String mensaje) {
		Alert alerta = new Alert(tipoAlerta);
		alerta.setTitle(tituloVentana);
		alerta.setHeaderText(tituloMensaje);
		alerta.setContentText(mensaje);
		alerta.showAndWait();		
	}
	
	//Método que añade un número al valor de la clave que correspondiente al texto del RadioButton
	public void añadirConteo(RadioButton rdbSel,Map<String, Integer> conteo,int numero) {
		int cnSabor = conteo.get(rdbSel.getText());			
		cnSabor = cnSabor + numero;
		conteo.put(rdbSel.getText(), cnSabor);		
	}	

	public static void main(String[] args) {
		launch(args);

	}
}
