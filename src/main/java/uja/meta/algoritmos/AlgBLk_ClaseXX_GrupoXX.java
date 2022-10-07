package uja.meta.algoritmos;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.apache.log4j.Logger;

import java.util.Random;

import static uja.meta.utils.FuncionesAuxiliares.calculaCoste;
import static uja.meta.utils.FuncionesAuxiliares.visualizaVectorLog;

@AllArgsConstructor
public class AlgBLk_ClaseXX_GrupoXX implements Runnable {
    private final int D;
    private final long iteraciones;
    private double[] vSolucion;
    private final double rangoInf;
    private final double rangoSup;
    private final String funcion;
    private final String className;

    @Override
    public void run() {
        Logger log = Logger.getLogger(className);
        double tiempoInicial = System.nanoTime();
        Random random = new Random();

        double[] vecino = new double[D];
        double[] mejorVecino;
        mejorVecino = vSolucion;
        double mejorCosteVecino = Double.MAX_VALUE;
        double costeMejor = calculaCoste(vSolucion, funcion);
        boolean mejora = true;
        boolean mejoraIteraciones = true;
        int iter = 0;

        while (mejora && iter < iteraciones) {
            mejora = false;
            int x = random.nextInt(4, 10) + 10;

            for (int i = 0; i <= x; i++) {
                for (int j = 0; j < D; j++) {
                    double uniforme = random.nextDouble(1.0001 - 0) + 1.0001;
                    if (uniforme <= 0.3) {
                        double inf, sup;
                        inf = vSolucion[j] * 0.9;
                        sup = vSolucion[j] * 1.1;
                        if (inf < rangoInf)
                            inf = rangoInf;
                        if (sup > rangoSup)
                            sup = rangoSup;
                        vecino[j] = random.nextDouble(sup - inf) + sup;
                    } else
                        vecino[j] = vSolucion[j];
                }
                double costeVecino = calculaCoste(vecino, funcion);
                if (costeVecino < mejorCosteVecino) {
                    mejorVecino = vecino;
                    mejorCosteVecino = costeVecino;
                }
            }
            if(mejorCosteVecino<costeMejor){
                vSolucion=mejorVecino;
                costeMejor=mejorCosteVecino;
                mejora=true;
                iter++;
            }
        }
        log.info("vector: " + visualizaVectorLog(vSolucion));
        log.info("Iteraciones totales: " + iteraciones);
        log.info("Coste: " + costeMejor);
        double tiempoFinal = System.nanoTime();
        log.info("Tiempo transcurrido: " + (tiempoFinal - tiempoInicial) / 1000000 + "ms");
    }
}
