package tp2mongo;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;

public class FrecuenciaIdiomas {

	public Map<String, BigDecimal> cantidadLetras = new HashMap<String, BigDecimal>();
	
	public Map<String, Map> listaIdiomas = new HashMap<String, Map>();
	
	public Map<String, Integer> contadorCoincidencias = new HashMap<String, Integer>();
	
	BigDecimal margenSuperior, margenInferior, numeroMargen = new BigDecimal("2.5");

	public void compararIdiomas(Archivo arch) {
		Iterator<String> iteratorIdiomas = this.listaIdiomas.keySet()
				.iterator();
	
		Map<String, Map> mapTmp = new HashMap<String, Map>();
		
		int contCoincidencias, max = 0;
		
		String idiomaTexto = null;
			
		while (iteratorIdiomas.hasNext()) {
			
			// Se itera cada idioma
			contCoincidencias = 0;
			String key = iteratorIdiomas.next();
			mapTmp = this.listaIdiomas.get(key);

			// Se itera la frecuencia de cada idioma
			Iterator<String> iteratorFrec = mapTmp.keySet().iterator();
			
			while (iteratorFrec.hasNext()) {
				// Se itera cada frecuencia
				String key2 = iteratorFrec.next();
				BigDecimal value = (BigDecimal) mapTmp.get(key2);
				String letra = key2.toString();
		   
				margenSuperior = value.add(numeroMargen);
				margenInferior = value.subtract(numeroMargen);

				BigDecimal porcentajeLetra = arch.porcentajeLetras.get(letra);
				
				if (porcentajeLetra != null) {
					if ((porcentajeLetra.compareTo(margenInferior) >= 0)
							&& (porcentajeLetra.compareTo(margenSuperior) <= 0)) {
						contCoincidencias++;
					}
				}
		
				contadorCoincidencias.put(key, contCoincidencias);
				
				if ((max == 0) || (contCoincidencias > max)) {
					idiomaTexto = key;
					max = contCoincidencias;
					
					
				}
			}
		}

		System.out.println("El idioma del texto es: " + idiomaTexto);
	}


}
