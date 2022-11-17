package uja.meta.algoritmos;

import lombok.AllArgsConstructor;
import org.apache.log4j.Logger;
import uja.meta.utils.Solucion;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;

import static uja.meta.utils.FuncionesAuxiliares.*;

@AllArgsConstructor
public class AlgEvBLX_Clase01_Grupo10 implements Callable<Solucion> {
    private final String className;
    private int tp;

    private int D;
    private long limiteEvaluaciones;
    private List<double[]> cromosomas;
    private double[] vSolucion;
    private double rangoMin;
    private double rangoMax;
    private double kProbMuta;
    private double kProbCruce;
    private double alfa;
    private String funcion;


    @Override
    public Solucion call() {
        Logger logger = Logger.getLogger(className);
        Random random = new Random();
        int t = 0;
        List<double[]> nuevag = new ArrayList<>();
        double[] costes = new double[tp], costesH = new double[tp], costesHH = new double[tp];
        int[] posi = new int[tp];
        double[] mejorCr = new double[tp];
        int peor;
        int mejorCrHijo = 1;
        double mejorCoste = Double.MAX_VALUE;
        double mejorCosteHijo = Double.MAX_VALUE;
        for (int i = 0; i < tp; i++) {
            costes[i] = calculaCoste(cromosomas.get(i), funcion);
            if (costes[i] < mejorCoste) {
                mejorCoste = costes[i];
                mejorCr = cromosomas.get(i);
            }
        }
        double mejorCosteGlobal = mejorCoste;
        double[] mejorCroGlobal = mejorCr;
        List<double[]> nuevaG = new ArrayList<>(D);

        int contEv = tp;

        while (contEv < limiteEvaluaciones) {
            t++;
            for (int i = 0; i < tp; i++) {
                int j, k;
                j = random.nextInt(tp - 1 - 0) + 0;
                while (j == (k = random.nextInt(tp - 1 - 0) + 0)) ;
                posi[i] = (costes[i] < costes[k]) ? j : k;
            }
            for (int i = 0; i < tp; i++) {
                nuevag.add(i, cromosomas.get(posi[i]));
                costesH[i] = costes[posi[i]];
            }
            int c1, c2, c3, c4;
            double costeMejor1, costeMejor2;
            double[] mejor1, mejor2;
            double[] h = new double[D];
            double x;
            int posAnt = 0;
            boolean[] marcados = new boolean[tp];
            for (int i = 0; i < tp; i++) {
                marcados[i] = false;
            }
            for (int i = 0; i < tp; i++) {
                c1 = random.nextInt((tp - 1 - 0) + 0);
                while (c1 == (c2 = random.nextInt(tp - 1 - 0) + 0)) ;
                if (costesH[c1] < costesH[c2]) {
                    mejor1 = nuevag.get(c1);
                    costeMejor1 = costesH[c1];
                } else {
                    mejor1 = nuevag.get(c2);
                    costeMejor1 = costesH[c2];
                }
                while (posAnt == (c3 = random.nextInt(tp - 1 - 0) + 0)) ;
                while (posAnt == (c4 = random.nextInt(tp - 1 - 0) + 0)) ;

                //Mirar
                while (c3 == c4) ;
                if (costesH[c3] < costesH[c4]) {
                    mejor2 = nuevag.get(c3);
                    costeMejor2 = costesH[c3];
                } else {
                    mejor2 = nuevag.get(c4);
                    costeMejor2 = costesH[c4];
                }
                x = random.nextDouble();
                if (x < kProbCruce) {
                    cruceBLX(D, mejor1, mejor2, alfa, h, rangoMin, rangoMax);
                    nuevaG.add(i, h);
                    marcados[i] = true;
                } else {
                    nuevaG.add(i, mejor1);
                    costesHH[i] = costeMejor1;
                }
            }
            nuevag = nuevaG;
            costesH = costesHH;

            for (int i = 0; i < tp; i++) {
                boolean m = false;
                for (int j = 0; j < D; j++) {
                    x = random.nextDouble();
                    if (x < kProbMuta) {
                        m = true;
                        double valor = random.nextDouble(rangoMax - rangoMin) + rangoMin;
                        Mutacion(nuevag.get(i),j,valor);
                    }
                }
                if (m)
                    marcados[i] = true;
            }

            for (int i = 0; i < tp; i++) {
                if (marcados[i]) {
                    costesH[i] = calculaCoste(nuevag.get(i), funcion);
                    contEv++;
                }
                if (costesH[i] < mejorCosteHijo) {
                    mejorCosteHijo = costesH[i];
                    mejorCrHijo = i;
                }
            }
            boolean enc = false;
            for (int i = 0; i < nuevag.size() && !enc; i++) {
                if (mejorCr == nuevag.get(i)) {
                    enc = true;
                }
            }
            if (!enc) {
                int p1, p2, p3, p4;
                p1 = random.nextInt(tp - 1 - 0) + 0;
                p2 = random.nextInt(tp - 1 - 0) + 0;
                p3 = random.nextInt(tp - 1 - 0) + 0;
                p4 = random.nextInt(tp - 1 - 0) + 0;
                while (p1 == p2) ;
                while (p1 == p2 && p2 == p3) ;
                while (p1 == p2 && p2 == p3 && p3 == p4) ;
                if (costesH[p1] > costesH[p2] && costesH[p1] > costesH[p3] && costesH[p1] > costesH[p4])
                    peor = p1;
                else if (costesH[p2] > costesH[p1] && costesH[p2] > costesH[p3] && costesH[p2] > costesH[p4])
                    peor = p2;
                else if (costesH[p3] > costesH[p1] && costesH[p3] > costesH[p2] && costesH[p3] > costesH[p4])
                    peor = p3;
                else
                    peor = p4;
                nuevag.add(peor, mejorCr);
                costesH[peor] = mejorCoste;
                if (mejorCoste < mejorCosteHijo) {
                    mejorCosteHijo = mejorCoste;
                    nuevag.add(mejorCrHijo, mejorCr);
                }
            }
            mejorCr = nuevag.get(mejorCrHijo);
            mejorCoste = mejorCosteHijo;

            if (mejorCosteHijo < mejorCosteGlobal) {
                mejorCosteGlobal = mejorCosteHijo;
                mejorCroGlobal = nuevag.get(mejorCrHijo);
            }
            costes = costesH;
            cromosomas = nuevag;
        }

        vSolucion = mejorCroGlobal;
        //TODO
        System.out.println("Total evaluaciones: " + contEv);
        System.out.println("Total iteraciones: " + t);

        return null;
    }
}
