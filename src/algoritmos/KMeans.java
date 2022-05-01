package algoritmos;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class KMeans {
    
    /*Clases*/
    public class KMeansResultado{

        private List<Cluster> clusters = new ArrayList<>();
        private Double ofv;

        public KMeansResultado(List<Cluster> clusters, Double ofv) {
            super();
            this.ofv = ofv;
            this.clusters = clusters;
        }

        public List<Cluster> Clusters() {
            return clusters;
        }

        public Double Ofv() {
            return ofv;
        }
    }
    
    public class Punto {

        private Float[] data;

        public Punto(String[] strings) {
            super();
            try{
                List<Float> puntos = new ArrayList<>();
                for (String string : strings) {
                    puntos.add(Float.parseFloat(string));
                }
                this.data = puntos.toArray(new Float[strings.length]);
            }
            catch(NumberFormatException ex){
                System.err.println(ex.getMessage());
            }
        }

        public Punto(Float[] data) {
            this.data = data;
        }

        public float Dato(int dimension) {
            return data[dimension];
        }

        public int Grado() {
            return data.length;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append(data[0]);
            for (int i = 1; i < data.length; i++) {
                sb.append(", ");
                sb.append(data[i]);
            }
            return sb.toString();
        }

        public Double distanciaEuclideana(Punto destino) {
            Double d = 0d;
            for (int i = 0; i < data.length; i++) {
                d += Math.pow(data[i] - destino.Dato(i), 2);
            }
            return Math.sqrt(d);
        }

        @Override
        public boolean equals(Object obj) {
            
            if (obj == null) {
                return false;
            }

            if (this == obj) {
                return true;
            }

            if (!(obj instanceof Punto))
                return false;
            
            Punto other = (Punto) obj;
            for (int i = 0; i < data.length; i++) {
                if (data[i] != other.Dato(i)) {
                    return false;
                }
            }
            return true;
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 83 * hash + Arrays.deepHashCode(this.data);
            return hash;
        }
    }
    
    public class Cluster {
        private List<Punto> puntos = new ArrayList<>();
        private Punto centroide;
        private boolean termino = false;

        public Punto Centroide() {
            return centroide;
        }

        public void Centroide(Punto centroide) {
            this.centroide = centroide;
        }

        public List<Punto> Puntos() {
            return puntos;
        }

        public boolean Termino() {
            return termino;
        }

        public void Termino(boolean termino) {
            this.termino = termino;
        }

        public void limpiarPuntos() {
            puntos.clear();
        }

        @Override
        public String toString() {
            return centroide.toString();
        }
    }

    public KMeansResultado Calcular(List<Punto> puntos, Integer k) {
	List<Cluster> clusters = elegirCentroides(puntos, k);

	while (!finalizo(clusters)) {
	    prepararClusters(clusters);
	    asignarPuntos(puntos, clusters);
	    recalcularCentroides(clusters);
	}

	Double ofv = calcularFuncionObjetivo(clusters);

	return new KMeansResultado(clusters, ofv);
    }

    private void recalcularCentroides(List<Cluster> clusters) {
	for (Cluster c : clusters) {
	    if (c.Puntos().isEmpty()) {
		c.Termino(true);
		continue;
	    }

	    Float[] d = new Float[c.Puntos().get(0).Grado()];
	    Arrays.fill(d, 0f);
	    for (Punto p : c.Puntos()) {
		for (int i = 0; i < p.Grado(); i++) {
		    d[i] += (p.Dato(i) / c.Puntos().size());
		}
	    }

	    Punto nuevoCentroide = new Punto(d);

	    if (nuevoCentroide.equals(c.Centroide())) {
		c.Termino(true);
	    } else {
		c.Centroide(nuevoCentroide);
	    }
	}
    }

    private void asignarPuntos(List<Punto> puntos, List<Cluster> clusters) {
	for (Punto punto : puntos) {
	    Cluster masCercano = clusters.get(0);
	    Double distanciaMinima = Double.MAX_VALUE;
	    for (Cluster cluster : clusters) {
		Double distancia = punto.distanciaEuclideana(cluster
			.Centroide());
		if (distanciaMinima > distancia) {
		    distanciaMinima = distancia;
		    masCercano = cluster;
		}
	    }
	    masCercano.Puntos().add(punto);
	}
    }

    private void prepararClusters(List<Cluster> clusters) {
	for (Cluster c : clusters) {
	    c.limpiarPuntos();
	}
    }

    private Double calcularFuncionObjetivo(List<Cluster> clusters) {
	Double ofv = 0d;

	for (Cluster cluster : clusters) {
	    for (Punto punto : cluster.Puntos()) {
		ofv += punto.distanciaEuclideana(cluster.Centroide());
	    }
	}

	return ofv;
    }

    private boolean finalizo(List<Cluster> clusters) {
	for (Cluster cluster : clusters) {
	    if (!cluster.Termino()) {
		return false;
	    }
	}
	return true;
    }

    private List<Cluster> elegirCentroides(List<Punto> puntos, Integer k) {
	List<Cluster> centroides = new ArrayList<>();

	List<Float> maximos = new ArrayList<>();
	List<Float> minimos = new ArrayList<>();
	// me fijo máximo y mínimo de cada dimensión

	for (int i = 0; i < puntos.get(0).Grado(); i++) {
	    Float min = Float.POSITIVE_INFINITY, max = Float.NEGATIVE_INFINITY;

	    for (Punto punto : puntos) {
		min = min > punto.Dato(i) ? punto.Dato(0) : min;
		max = max < punto.Dato(i) ? punto.Dato(i) : max;
	    }

	    maximos.add(max);
	    minimos.add(min);
	}

	Random random = new Random();

	for (int i = 0; i < k; i++) {
	    Float[] data = new Float[puntos.get(0).Grado()];
	    Arrays.fill(data, 0f);
	    for (int d = 0; d < puntos.get(0).Grado(); d++) {
		data[d] = random.nextFloat()
			* (maximos.get(d) - minimos.get(d)) + minimos.get(d);
	    }

	    Cluster c = new Cluster();
	    Punto centroide = new Punto(data);
	    c.Centroide(centroide);
	    centroides.add(c);
	}

	return centroides;
    }
}
