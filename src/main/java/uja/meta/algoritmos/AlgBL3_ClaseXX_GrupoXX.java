package uja.meta.algoritmos;

import lombok.AllArgsConstructor;

import org.apache.log4j.Logger;
import uja.meta.utils.Solucion;

import java.util.Random;
import java.util.concurrent.Callable;

import static uja.meta.utils.FuncionesAuxiliares.*;

@AllArgsConstructor
public class AlgBL3_ClaseXX_GrupoXX implements Callable<Solucion> {
    private final int D;
    private final int k;
    private final long semilla;
    private final long iteraciones;
    private final double probabilidad;
    private final double rangoInf;
    private final double rangoSup;
    private final String funcion;
    private final String className;
    private double[] vSolucion;

    @Override
    public Solucion call() {
        Logger log = Logger.getLogger(className);
        double tiempoInicial = System.nanoTime();
        Random random = new Random();
        double[] vecino = new double[D];
        log.info("Vector inicial: " + visualizaVectorLog(vSolucion));
        double[] mejorVecino = vSolucion;
        double mejorCosteVecino = Double.MAX_VALUE;
        double costeMejor = calculaCoste(vSolucion, funcion);
        boolean mejora = true;
        int iter = 0;

        while (mejora && iter < iteraciones) {
            mejora = false;
            for (int i = 1; i <= k; i++) {
                for (int j = 0; j < D; j++) {    //	Para j = 1 hasta d
                    if (random.nextDouble() <= probabilidad) {
                        nuevaSolucion(vSolucion, vecino, j, rangoInf, rangoSup);
                    } else
                        vecino[j] = vSolucion[j];
                }
                double costeVecino = calculaCoste(vecino, funcion);
                if (costeVecino < mejorCosteVecino) {
                    mejorVecino = vecino;
                    mejorCosteVecino = costeVecino;
                }
            }

            // Comparacion actual con entorno
            if (mejorCosteVecino < costeMejor) {
                vSolucion = mejorVecino;
                costeMejor = mejorCosteVecino;
                mejora = true;
                iter++;
            }
        }

        log.info("Vector solucion: " + visualizaVectorLog(vSolucion));
        String costeFormat = formato(costeMejor);
        log.info("Coste: " + costeFormat);
        log.info("Iteraciones: " + iter);
        double tiempoFinal = System.nanoTime();
        String tiempoTotal = calcularTiempo(tiempoInicial, tiempoFinal);
        log.info("Tiempo transcurrido: " + tiempoTotal + " ms");
        return new Solucion(costeFormat, tiempoTotal, semilla);
    }
}
