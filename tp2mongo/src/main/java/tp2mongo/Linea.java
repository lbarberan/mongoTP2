package tp2mongo;

import com.mongodb.BasicDBObject;

public class Linea {

	
		private String renglon;
		private Integer cantidad;

	
		public Linea() {
		}

		public Linea(String renglon) {
			this.renglon = renglon;
		//	this.cantidad = cantidad;
		}

		// Transformo un objecto que me da MongoDB a un Objecto Java
		public Linea(BasicDBObject dBObjectLinea) {
			this.renglon = dBObjectLinea.getString("renglon");
	//		this.cantidad = dBObjectLinea.getInt("cantidad");
		}

		public BasicDBObject toDBObjectLinea() {

			// Creamos una instancia BasicDBObject
			BasicDBObject dBObjectLinea = new BasicDBObject();

			dBObjectLinea.append("renglon", this.getrenglon());
		//	dBObjectLinea.append("cantidad", this.getcantidad());

			return dBObjectLinea;
		}

		public String getrenglon() {
			return renglon;
		}

		public void setrenglon(String renglon) {
			this.renglon = renglon;
		}

		public int getcantidad() {
			return cantidad;
		}

		public void setcantidad(int cantidad) {
			this.cantidad = cantidad;
		}


		@Override
		public String toString() {
			return this.getrenglon();
		}
	}



