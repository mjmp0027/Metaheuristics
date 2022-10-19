package uja.meta.algoritmos;

import lombok.AllArgsConstructor;
import org.apache.log4j.Logger;
import uja.meta.utils.Solucion;

import java.util.Random;
import java.util.concurrent.Callable;

import static uja.meta.utils.FuncionesAuxiliares.*;

@AllArgsConstructor
public class AlgBLk_Clase01_Grupo10 implements Callable<Solucion> {
    private final int D;
    private final long semilla;
    private final long iteraciones;
    private final double oscilacion;
    private final double rangoInf;
    private final double rangoSup;
    private final String funcion;
    private final String className;
    private double[] vSolucion;

    @Override
    public Solucion call() {
        Logger log = Logger.getLogger(className);
        log.info("Vector inicial: " + visualizaVectorLog(vSolucion));
        double tiempoInicial = System.nanoTime();
        Random random = new Random();
        double[] vecino = new double[D];
        double[] mejorVecino = vSolucion;
        double mejorCosteVecino = Double.MAX_VALUE;
        double costeMejor = calculaCoste(vSolucion, funcion);
        boolean mejora = true;
        int iter = 0;

        while (mejora && iter < iteraciones) {
            mejora = false;
            int x = random.nextInt(10 - 4) + 4;

            for (int i = 0; i <= x; i++) {
                for (int j = 0; j < D; j++) {
                    if (random.nextDouble() <= oscilacion) {
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
