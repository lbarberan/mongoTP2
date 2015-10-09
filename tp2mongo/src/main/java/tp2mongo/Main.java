package tp2mongo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;

import org.bson.BSONObject;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MapReduceCommand;
import com.mongodb.MapReduceOutput;
import com.mongodb.MongoClient;
import com.mongodb.MapReduceCommand.OutputType;

public class Main {
	
	public static void main(String[] args) throws IOException {
		
		File txtBaseIdiomas = null;
		FileReader frBi, fr = null;
		BufferedReader brBi,  br = null;
		
		txtBaseIdiomas = new File("d:\\mismongos\\dataInput\\base_idiomas.txt");
		
		String lineaBi;
		frBi = new FileReader(txtBaseIdiomas);
		brBi = new BufferedReader(frBi);

		FrecuenciaIdiomas frec = new FrecuenciaIdiomas();
		
		String idiomaActual = "";
		String idiomaAnterior = "";
		
		while ((lineaBi = brBi.readLine()) != null) {
			String listaarray[] = lineaBi.split("\\Q|\\E");
			idiomaActual = listaarray[0].trim();
			if (!(idiomaAnterior.equalsIgnoreCase(idiomaActual))) {
		
				// Se crea un nuevo mapa cantidadLetras para el nuevo idioma, luego se agrega la letra y su frecuencia
				frec.cantidadLetras = new HashMap<String, BigDecimal>();
		
				frec.cantidadLetras.put(listaarray[1], new BigDecimal(listaarray[2]));
				frec.listaIdiomas.put(idiomaActual, frec.cantidadLetras);
			} else {
				// Se agrega la letra y su frecuencia
				frec.cantidadLetras.put(listaarray[1], new BigDecimal(listaarray[2]));
			}

			idiomaAnterior = listaarray[0].trim();
		}

		int caracter;		
		
		File s1 = null;
		String linea;
		
		Archivo arch = new Archivo();

	    //creo una lista para almacenar cada palabra formateada entre "" para que mongo la lea 
	    ArrayList<Linea> listaarray = new ArrayList<Linea>();
	    	
	    //Leer Archivo en Java
	  // Activar el path que quiero que procese.
	    
	    //s1 = new File("d:\\mismongos\\dataInput\\goethe.txt");
	   //s1 = new File("d:\\mismongos\\dataInput\\donQuijote.txt");
	   //s1 = new File("d:\\mismongos\\dataInput\\shakespeare.txt");
	   s1 = new File("d:\\mismongos\\dataInput\\bovary.txt");
	        
	    fr = new FileReader(s1);
		br = new BufferedReader(fr);
		
        //recorre el archivo de entrada para calcular la cantidad total de letras del archivo de entrada, PARA LUEGO OBTENER PROCENTAJE
		while ((caracter = br.read()) != -1) {
			arch.contartotalcaracteres(Character.toLowerCase((char) caracter));
		}
	    
	    fr = new FileReader(s1);
		br = new BufferedReader(fr);
		
		//lee el archivo de entrada por linea ejemplo  Donquijote.txt
		while ((linea = br.readLine()) != null) {
			//cargo vector a partir de cada linea 
			String vec[] = linea.split("\\Q|\\E");
			listaarray.add(new Linea(vec[0]));
			
			
		}
	
	    //Paso variable a mongodb y cargar la base
	    // PASO 1: Conexi�n al Server de MongoDB Pasandole el host y el puerto

		MongoClient mongoClient = new MongoClient("localhost", 27017);
	  	
	    // PASO 2: Conexi�n a la base de datos
	  	DB db = mongoClient.getDB("tpmongodb");
	  	
	  	//DB db = mongoClient.getDB("tpmongodb");
	  	//crear colleccion en mongodb
	  	DBCollection colleccion = db.getCollection("Textos");
	  	
	  	//Para que no recalcue con valores de corridas anteriores
	  	colleccion.drop(); ;
	  
	  	//db.dropDatabase();
	  //	db.colleccion.drop();
	  	
	  	//db.collectionExists("Textos").

		// PASO 3: CRUD (Create-Read-Update-Delete)
		// PASO 3.1: "CREATE" -> inserto los objetos lineas (o documentos en Mongo) en la coleccion lineas
	  	
	  	for (Linea line : listaarray )
	  			colleccion.insert(line.toDBObjectLinea());
	  			  	
	  	// mapReduce para saber la cantidad de letras por linea
	  	String map = "function() {" + "var vector= {};"
				+ "for(var i = 0; i <= this.renglon.length; i++) {" + 
								"var caracter= this.renglon[i];"
								+ "if(caracter != ' ') {" + 
														"if(isNumber(vector[caracter])) {" +
																						"vector[caracter]++; " +
																						" }" +  
						 			" else { " + 
						 						"vector[caracter]=1; " +
						 			"} "+
						 						"} "+
						 								"}" 
						 						+ " for(var letra in vector) {"+" emit(letra, vector[letra]);} }";
	  		
	  		String reduce = "function(key, contadores){" + "return Array.sum(contadores)}";
	  		
	  		MapReduceCommand cantidadLetras = new MapReduceCommand (colleccion, map, reduce, null, OutputType.INLINE, null);
	  		//llamo a mapReduce 
	  		MapReduceOutput outLineas = colleccion.mapReduce(cantidadLetras);
	  			
	 	// Se ejecuta metodo para calcular la frecuencia de letras para el texto leido
			arch.calcularFrecuencia(outLineas);
			
		// Se ejecuta metodo para identificar el idioma del txt leido
			frec.compararIdiomas(arch);
    
	}
	

		}
	

	
