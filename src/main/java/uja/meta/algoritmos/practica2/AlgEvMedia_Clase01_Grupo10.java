package uja.meta.algoritmos.practica2;

import lombok.RequiredArgsConstructor;
import org.apache.log4j.Logger;
import uja.meta.utils.Solucion;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;

import static uja.meta.utils.FuncionesAuxiliares.*;

@RequiredArgsConstructor
public class AlgEvMedia_Clase01_Grupo10 implements Callable<Solucion> {
    private final String className;
    private final int tp;
    private final int D;
    private final long limiteEvaluaciones;
    private final double rangoMin;
    private final double rangoMax;
    private final double kProbMuta;
    private final double kProbCruce;
    private final String funcion;
    private final Long semilla;
    private final double prob;
    private List<double[]> cromosomas;
    private double[] vSolucion;
    private double[] mejor1;
    private double[] mejor2;
    private double costeMejor1;
    private double costeMejor2;

    @Override
    public Solucion call() throws IOException {

        Logger log = Logger.getLogger(className);
        Random random = new Random();
        double tiempoInicial = System.nanoTime();
        cromosomas = generador(rangoMin, rangoMax, semilla, D, tp);
        int t = 0;
        List<double[]> nuevaAg = new ArrayList<>();
        double[] costes = new double[tp], costesNuevaAg = new double[tp], costesNuevaAG = new double[tp];
        int[] posicion = new int[tp];
        double[] elite = new double[D];
        int mejorCrHijo = 1;
        double mejorCoste = Double.MAX_VALUE;
        double mejorCosteHijo = Double.MAX_VALUE;
        double mejorCosteGlobal = mejorCoste;
        double[] mejorCroGlobal = elite;
        double[] h = new double[D];
        List<double[]> nuevaAG = new ArrayList<>();
        int contEv = tp;

        for (int i = 0; i < tp; i++) {
            costes[i] = calculaCoste(cromosomas.get(i), funcion);
            if (costes[i] < mejorCoste) {
                mejorCoste = costes[i];
                elite = cromosomas.get(i);
            }
        }

        while (contEv < limiteEvaluaciones) {
            t++;
            torneo(tp, posicion, costes, cromosomas, nuevaAg, costesNuevaAg, random);

            boolean[] marcados = new boolean[tp];
            for (int i = 0; i < tp; i++) {
                marcados[i] = false;
            }
            double uniforme;

            for (int i = 0; i < tp; i++) {
                torneo2a2(tp, nuevaAg, costesNuevaAg, random);
                uniforme = random.nextDouble();
                if (uniforme < kProbCruce) {
                    cruceMedia(D, mejor1, mejor2, h);
                    nuevaAG.add(i, h);
                    marcados[i] = true;
                } else {
                    if (uniforme < prob) {
                        nuevaAG.add(i, mejor1);
                        costesNuevaAG[i] = costeMejor1;
                    } else {
                        nuevaAG.add(i, mejor2);
                        costesNuevaAG[i] = costeMejor2;
                    }
                }
            }
            nuevaAg = nuevaAG;
            costesNuevaAg = costesNuevaAG;

            mutar(tp, D, kProbMuta, rangoMin, rangoMax, nuevaAG, marcados, random);

            for (int i = 0; i < tp; i++) {
                if (marcados[i]) {
                    costesNuevaAg[i] = calculaCoste(nuevaAg.get(i), funcion);
                    contEv++;
                }
                if (costesNuevaAg[i] < mejorCosteHijo) {
                    mejorCosteHijo = costesNuevaAg[i];
                    mejorCrHijo = i;
                }
            }

            boolean enc = false;
            for (int i = 0; i < nuevaAg.size() && !enc; i++)
                if (elite == nuevaAg.get(i))
                    enc = true;

            if (!enc) {

                reemplazamiento(tp, nuevaAG, elite, costesNuevaAg, mejorCoste, random);

                if (mejorCoste < mejorCosteHijo) {
                    mejorCosteHijo = mejorCoste;
                    nuevaAg.add(mejorCrHijo, elite);
                }
            }

            elite = nuevaAg.get(mejorCrHijo);
            mejorCoste = mejorCosteHijo;

            if (mejorCosteHijo < mejorCosteGlobal) {
                mejorCosteGlobal = mejorCosteHijo;
                mejorCroGlobal = nuevaAg.get(mejorCrHijo);
            }
            costes = costesNuevaAg;
            cromosomas = nuevaAg;

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

    public void torneo2a2(int tp, List<double[]> nuevaAg, double[] costesNuevaAg, Random random) {
        int c1 = random.nextInt(tp), c2, c3, c4, posAnt;

        while (c1 == (c2 = random.nextInt(tp))) ;
        if (costesNuevaAg[c1] < costesNuevaAg[c2]) {
            mejor1 = nuevaAg.get(c1);
            costeMejor1 = costesNuevaAg[c1];
            posAnt = c1;
        } else {
            mejor1 = nuevaAg.get(c2);
            costeMejor1 = costesNuevaAg[c2];
            posAnt = c2;
        }

        while (posAnt == (c3 = random.nextInt(tp))) ;
        while (posAnt == (c4 = random.nextInt(tp))) ;

        if (costesNuevaAg[c3] < costesNuevaAg[c4]) {
            mejor2 = nuevaAg.get(c3);
            costeMejor2 = costesNuevaAg[c3];
        } else {
            mejor2 = nuevaAg.get(c4);
            costeMejor2 = costesNuevaAg[c4];
        }
    }
}
