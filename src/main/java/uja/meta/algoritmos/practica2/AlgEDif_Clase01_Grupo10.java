package uja.meta.algoritmos.practica2;

import lombok.AllArgsConstructor;
import org.apache.log4j.Logger;
import uja.meta.utils.Solucion;

import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;

import static uja.meta.utils.FuncionesAuxiliares.*;

@AllArgsConstructor
public class AlgEDif_Clase01_Grupo10 implements Callable<Solucion> {
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
    public Solucion call() throws IOException {
        Logger log = Logger.getLogger(className);
        Random random = new Random();
        double tiempoInicial = System.nanoTime();
        int t = 0;
        double[] costes = new double[tp];
        double mejorCoste = Double.MAX_VALUE;
        double[] mejorCr = new double[D];
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
        double[] ale1 = new double[tp], ale2 = new double[tp], obj = new double[tp],
                nuevo = new double[tp], padre;
        int a1 = 0, a2 = 0;

        while (contEv < limiteEvaluaciones) {
            for (int i = 0; i < tp; i++) {
                padre = cromosomas.get(i);

                eleccion2aleatorios(tp, cromosomas, i, ale1, ale2, random, a1, a2);

                torneoK3(tp, i, a1, a2, random, cromosomas, obj, costes);

                eleccionNuevoCr(D, probRecomb, padre, nuevo, ale1, ale2, rangoMin, rangoMax, obj, random);

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
