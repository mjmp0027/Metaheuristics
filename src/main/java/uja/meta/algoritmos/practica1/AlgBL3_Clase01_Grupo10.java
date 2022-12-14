package uja.meta.algoritmos.practica1;

import lombok.AllArgsConstructor;

import org.apache.log4j.Logger;
import uja.meta.utils.Solucion;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.Callable;

import static uja.meta.utils.FuncionesAuxiliares.*;

@AllArgsConstructor
public class AlgBL3_Clase01_Grupo10 implements Callable<Solucion> {
    private final int D;
    private final int k;
    private final long semilla;
    private final long limiteIteraciones;
    private final double oscilacion;
    private final double rangoInf;
    private final double rangoSup;
    private final String funcion;
    private final String className;
    private double[] vSolucion;

    @Override
    public Solucion call() throws IOException {
        Logger log = Logger.getLogger(className);
        double tiempoInicial = System.nanoTime();
        Random random = new Random();
        double[] vecino = new double[D];
        log.info("Vector inicial: " + visualizaVectorLog(vSolucion));
        double[] mejorVecino = vSolucion;
        double vecinoMejorCoste = Double.MAX_VALUE;
        double mejorCoste = calculaCoste(vSolucion, funcion);
        boolean mejora = true;
        int iteraciones = 0;

        while (mejora && iteraciones < limiteIteraciones) {
            mejora = false;
            for (int i = 1; i <= k; i++) {
                for (int j = 0; j < D; j++) {    //	Para j = 1 hasta d
                    if (random.nextDouble() <= oscilacion) {
                        nuevaSolucion(vSolucion, vecino, j, rangoInf, rangoSup);
                    } else
                        vecino[j] = vSolucion[j];
                }
                double costeVecino = calculaCoste(vecino, funcion);
                if (costeVecino < vecinoMejorCoste) {
                    mejorVecino = vecino;
                    vecinoMejorCoste = costeVecino;
                }
            }

            // Comparacion actual con entorno
            if (vecinoMejorCoste < mejorCoste) {
                vSolucion = mejorVecino;
                mejorCoste = vecinoMejorCoste;
                mejora = true;
                iteraciones++;
            }
        }
        double tiempoFinal = System.nanoTime();
        String tiempoTotal = calcularTiempo(tiempoInicial, tiempoFinal);
        log.info("Tiempo transcurrido: " + tiempoTotal + " ms");
        log.info("Vector solucion: " + visualizaVectorLog(vSolucion));
        String costeFormat = formato(mejorCoste);
        log.info("Coste: " + costeFormat);
        log.info("Iteraciones: " + iteraciones);

        return new Solucion(costeFormat, tiempoTotal, semilla);
    }
}
