package uja.meta.algoritmos.practica2;

import lombok.RequiredArgsConstructor;
import org.apache.log4j.Logger;
import uja.meta.utils.Solucion;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;

import static uja.meta.utils.FuncionesAuxiliares.*;

@RequiredArgsConstructor
public class AlgEvBLX_Clase01_Grupo10 implements Callable<Solucion> {
    private final String className;
    private final int tp;
    private final int D;
    private final long limiteEvaluaciones;
    private final double rangoInf;
    private final double rangoSup;
    private final double kProbMuta;
    private final double kProbCruce;
    private final double alfa;
    private final String funcion;
    private final Long semilla;
    private List<double[]> cromosomas;
    private double[] vSolucion;
    private double[] mejor1;
    private double[] mejor2;
    private double costeMejor1;
    private double costeMejor2;

    @Override
    public Solucion call() {

        Logger log = Logger.getLogger(className);
        Random random = new Random();
        double tiempoInicial = System.nanoTime();
        cromosomas = generador(rangoInf, rangoSup, semilla, D, tp);
        vSolucion = new double[D];
        int t = 0;
        List<double[]> nuevaAg = new ArrayList<>();
        double[] costes = new double[tp], costesNuevaAg = new double[tp], costesHH = new double[tp];
        int[] posicion = new int[tp];
        double[] mejorCr = new double[tp];
        int peor = 0;
        int mejorCrHijo = 1;
        double mejorCoste = Double.MAX_VALUE;
        double mejorCosteHijo = Double.MAX_VALUE;
        double mejorCosteGlobal = mejorCoste;
        double[] mejorCroGlobal = mejorCr;
        double[] h = new double[D];
        List<double[]> nuevaAG = new ArrayList<>();
        int contEv = tp;

        for (int i = 0; i < tp; i++) {
            costes[i] = calculaCoste(cromosomas.get(i), funcion);
            if (costes[i] < mejorCoste) {
                mejorCoste = costes[i];
                mejorCr = cromosomas.get(i);
            }
        }

        while (contEv < limiteEvaluaciones) {
            boolean[] marcados = new boolean[tp];
            for (int i = 0; i < tp; i++) {
                marcados[i] = false;
            }
            double uniforme;

            torneo(tp, posicion, costes, cromosomas, nuevaAg, costesNuevaAg, random);

            for (int i = 0; i < tp; i++) {
                torneo2a2(tp, nuevaAg, costesNuevaAg, random);
                uniforme = random.nextDouble();
                if (uniforme < kProbCruce) {
                    cruceBLX(D, mejor1, mejor2, alfa, h, rangoInf, rangoSup);
                    nuevaAG.add(i, h);
                    marcados[i] = true;
                } else {
                    nuevaAG.add(i, mejor1);
                    costesHH[i] = costeMejor1;
                }
            }
            nuevaAg = nuevaAG;
            costesNuevaAg = costesHH;

            mutar(tp, D, kProbMuta, rangoInf, rangoSup, nuevaAG, marcados, random);

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
                if (mejorCr == nuevaAg.get(i))
                    enc = true;


            if (!enc) {

                calculoElite(tp, nuevaAG, mejorCr, costesNuevaAg, mejorCoste, random, peor);

                if (mejorCoste < mejorCosteHijo) {
                    mejorCosteHijo = mejorCoste;
                    nuevaAg.add(mejorCrHijo, mejorCr);
                }
            }

            mejorCr = nuevaAg.get(mejorCrHijo);
            mejorCoste = mejorCosteHijo;

            if (mejorCosteHijo < mejorCosteGlobal) {
                mejorCosteGlobal = mejorCosteHijo;
                mejorCroGlobal = nuevaAg.get(mejorCrHijo);
            }
            costes = costesNuevaAg;
            cromosomas = nuevaAg;
            t++;
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
