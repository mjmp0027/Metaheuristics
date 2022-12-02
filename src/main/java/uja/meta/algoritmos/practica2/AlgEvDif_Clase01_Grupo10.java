package uja.meta.algoritmos.practica2;

import lombok.AllArgsConstructor;
import org.apache.log4j.Logger;
import uja.meta.utils.Solucion;

import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;

import static uja.meta.utils.FuncionesAuxiliares.*;

@AllArgsConstructor
public class AlgEvDif_Clase01_Grupo10 implements Callable<Solucion> {
    private final String className;
    private int tp;
    private int D;
    private long limiteEvaluaciones;
    private List<double[]> cromosomas;
    private double[] vSolucion;
    private double rangoMin;
    private double rangoMax;
    private String funcion;
    private double probRecomb;
    private Long semilla;

    @Override
    public Solucion call() {
        Logger log = Logger.getLogger(className);
        Random random = new Random();
        double tiempoInicial = System.nanoTime();
        int t = 0;
        double[] costes = new double[tp];
        double mejorCoste = Double.MAX_VALUE;
        double[] mejorCr = new double[tp];
        for (int i = 0; i < tp; i++) {
            costes[i] = calculaCoste(cromosomas.get(i), funcion);
            if (costes[i] < mejorCoste) {
                mejorCoste = costes[i];
                mejorCr = cromosomas.get(i);
            }
        }

        double mejorCosteGlobal = mejorCoste;
        double[] mejorCroGlobal = mejorCr;

        int contEv = tp;
        double[] ale1, ale2, obj,
                nuevo = new double[tp], padre;
        int a1, a2, k1, k2, k3;

        while (contEv < limiteEvaluaciones) {
            for (int i = 0; i < tp; i++) {
                padre = cromosomas.get(i);
                do {
                    a1 = random.nextInt(tp);
                    while (a1 == (a2 = random.nextInt(tp))) ;
                } while (a1 != i && a2 != i);
                ale1 = cromosomas.get(a1);
                ale2 = cromosomas.get(a2);

                do {
                    k1 = random.nextInt(tp);
                    while (k1 == (k2 = random.nextInt(tp))) ;
                    while ((k2 == (k3 = random.nextInt(tp)))) ;
                } while (k1 != i && k1 != a1 && k1 != a2 &&
                        k2 != i && k2 != a1 && k2 != a2 &&
                        k3 != i && k3 != a1 && k3 != a2);

                if (costes[k1] < costes[k2] && costes[k1] < costes[k3])
                    obj = cromosomas.get(k1);
                else if (costes[k2] < costes[k1] && costes[k2] < costes[k3])
                    obj = cromosomas.get(k2);
                else
                    obj = cromosomas.get(k3);

                double factor = random.nextDouble();

                for (int j = 0; j < D; j++) {
                    double porc = random.nextDouble();
                    if (porc > probRecomb)
                        nuevo[j] = obj[j];
                    else {
                        nuevo[j] = padre[j] + (factor * (ale1[j] - ale2[j]));
                        if (nuevo[j] > rangoMax)
                            nuevo[j] = rangoMax;
                        else if (nuevo[j] < rangoMin)
                            nuevo[j] = rangoMin;
                    }
                }

                double nuevoCoste = calculaCoste(nuevo, funcion);
                contEv++;
                reemplazamiento(nuevoCoste, i, costes, cromosomas, nuevo, mejorCoste, mejorCr);

            }
            if (mejorCoste < mejorCosteGlobal) {
                mejorCosteGlobal = mejorCoste;
                mejorCroGlobal = mejorCr;
            }
            t++;
            mejorCoste = Double.MAX_VALUE;
        }

        vSolucion = mejorCroGlobal;
        //TODO
        double tiempoFinal = System.nanoTime();
        String tiempoTotal = calcularTiempo(tiempoInicial, tiempoFinal);
        log.info("Tiempo transcurrido: " + tiempoTotal + " ms");
        log.info("Mejor cromosoma: " + visualizaVectorLog(vSolucion));
        String costeFormat = formato(mejorCosteGlobal);
        log.info("Mejor coste: " + costeFormat);
        log.info("Total evaluaciones: " + contEv);
        log.info("Total iteraciones: " + t);

        return new Solucion(costeFormat, tiempoTotal, semilla);
    }
}
