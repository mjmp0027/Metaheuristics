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
public class AlgEvMedia_Clase01_Grupo10 implements Callable<Solucion> {
    private final String className;
    private int tp;
    private int tam;
    private int evaluaciones;
    private double[] s;
    private double rangoMin;
    private double rangoMax;
    private double kProbMuta;
    private double kProbCruce;
    private String funcion;

    @Override
    public Solucion call() throws Exception {

        Logger logger = Logger.getLogger(className);
        Random random = new Random();
        int t = 0;
        List<double[]> cromosomas = new ArrayList<>();
        List<double[]> nuevaAg = new ArrayList<>(tam);
        double[] costes = new double[tp], costesH = new double[tp], costesHH = new double[tp];
        int[] posi = new int[tp];
        double[] mejorCr = new double[tp];
        int peor;
        int mejorCrHijo = 1;
        double mejorCoste = Double.MAX_VALUE;
        double mejorCosteHijo = Double.MAX_VALUE;
        for (int i = 0; i < tp; i++) {
            //cargaAleatoria(tam, cromosomas.get(i), rangoMin, rangoMax);
            costes[i] = calculaCoste(cromosomas.get(i), funcion);
            if (costes[i] < mejorCoste) {
                mejorCoste = costes[i];
                mejorCr = cromosomas.get(i);
            }
        }
        double mejorCosteGlobal = mejorCoste;
        double[] mejorCroGlobal = mejorCr;
        List<double[]> nuevaAG = new ArrayList<>(tam);

        int contEv = tp;

        while (contEv < evaluaciones) {
            t++;
            for (int i = 0; i < tp; i++) {
                int j, k;
                j = random.nextInt(tp - 1 - 0) + 0;
                while (j == (k = random.nextInt(tp - 1 - 0) + 0)) ;
                posi[i] = (costes[i] < costes[k]) ? j : k;
            }
            for (int i = 0; i < tp; i++) {
                if (posi[i] == 50)
                    posi[i]--;
                nuevaAg.add(i, cromosomas.get(posi[i]));
                costesH[i] = costes[posi[i]];
            }
            int c1, c2, c3, c4;
            double costeMejor1, costeMejor2;
            double[] mejor1, mejor2;
            double[] h = new double[tam];
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
                    mejor1 = nuevaAg.get(c1);
                    costeMejor1 = costesH[c1];
                } else {
                    mejor1 = nuevaAg.get(c2);
                    costeMejor1 = costesH[c2];
                }
                while (posAnt == (c3 = random.nextInt(tp - 1 - 0) + 0)) ;
                while (posAnt == (c4 = random.nextInt(tp - 1 - 0) + 0)) ;


                if (costesH[c3] < costesH[c4]) {
                    mejor2 = nuevaAg.get(c3);
                    costeMejor2 = costesH[c3];
                } else {
                    mejor2 = nuevaAg.get(c4);
                    costeMejor2 = costesH[c4];
                }
                x = random.nextDouble();
                if (x < kProbCruce) {
                    cruceMedia(tam, mejor1, mejor2, h);
                    nuevaAG.add(i, h);
                    marcados[i] = true;
                } else {
                    nuevaAG.add(i, mejor1);
                    costesHH[i] = costeMejor1;
                }
            }
            nuevaAg = nuevaAG;
            costesH = costesHH;

            for (int i = 0; i < tp; i++) {
                boolean m = false;
                for (int j = 0; j < tam; j++) {
                    x = random.nextDouble();
                    if (x < kProbMuta) {
                        m = true;
                        double valor = random.nextDouble(rangoMax - rangoMin) + rangoMin;
                        Mutacion(nuevaAg.get(i),j,valor);
                    }
                }
                if (m)
                    marcados[i] = true;
            }

            for (int i = 0; i < tp; i++) {
                if (marcados[i]) {
                    costesH[i] = calculaCoste(nuevaAg.get(i), funcion);
                    contEv++;
                }
                if (costesH[i] < mejorCosteHijo) {
                    mejorCosteHijo = costesH[i];
                    mejorCrHijo = i;
                }
            }
            boolean enc = false;
            for (int i = 0; i < nuevaAg.size() && !enc; i++) {
                if (mejorCr == nuevaAg.get(i)) {
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
                nuevaAg.add(peor, mejorCr);
                costesH[peor] = mejorCoste;

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
            costes = costesH;
            cromosomas = nuevaAg;
        }

        s = mejorCroGlobal;
        //TODO
        System.out.println("Total evaluaciones: " + contEv);
        System.out.println("Total iteraciones: " + t);

        return null;
    }
}
