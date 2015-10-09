package tp2mongo;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;

import com.mongodb.DBObject;
import com.mongodb.MapReduceOutput;

public class Archivo {

	// Map que almacena veces que aparece cada letra del alfabeto
	public static Map<Character, Integer> cantidadLetras = new HashMap<Character, Integer>();
	
	public static Map<String, BigDecimal> porcentajeLetras = new HashMap<String, BigDecimal>();

	long cantidadLetrasTotales = 0;
	
	int cantidadTemporal = 0;

	public void contartotalcaracteres(char caracter) {

		// Solo ingresa al if si el caracter es una letra
	
		if (Character.isLetter(caracter)) {
			cantidadLetrasTotales++;
		}
	}

			
		public void calcularFrecuencia(MapReduceOutput outLineas) {
			String porcentajeAux;
			BigDecimal porcentaje;
			DecimalFormat formatoDecimal = new DecimalFormat("#.##");
            
			//pasa los valores del mapreduce al Hashmap
	  		for (DBObject o : outLineas.results()) {
	  			
	  			String key = (String) o.get("_id");
	  			
	  			Double d = new Double((Double) o.get("value"));
	  			Double valor = d.parseDouble(d.toString());
	  			
	  			 // Se calcula el % de aparicion de cada letra
					
	  			porcentajeAux = formatoDecimal.format((valor * 100.00)/ this.cantidadLetrasTotales);
				porcentaje = new BigDecimal(porcentajeAux.replace(",", "."));
				
				//Para eliminar el elemento nulo del map reduce
				if (!key.equals("undefined")) {
					
				// Se almacena en un nuevo map < LETRA > < % DE APARICION >
				porcentajeLetras.put(key, porcentaje);
				
				}
				

	  			 
	  		}
	  		
		
	}
		
			
		
}
