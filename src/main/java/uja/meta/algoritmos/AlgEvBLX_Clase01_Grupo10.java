package uja.meta.algoritmos;

import lombok.AllArgsConstructor;
import org.apache.log4j.Logger;
import uja.meta.utils.Solucion;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;

import static uja.meta.utils.FuncionesAuxiliares.calculaCoste;

@AllArgsConstructor
public class AlgEvBLX_Clase01_Grupo10 implements Callable<Solucion> {
    private final String className;
    private int tp;

    private int tam;
    private int evaluaciones;
    private double[] s;
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
        List<double[]> cromosomas = new ArrayList<>();
        List<double[]> nuevaAg = new ArrayList<>(tam);
        double[] costes = new double[tp], costesH = new double[tp];
        int[] posi = new int[tp];
        double[] mejorCr = new double[tp];
        int peor = 0;
        double peorCoHijo = 0;
        int mejorCrHijo = 1;
        double mejorCo = Double.MAX_VALUE;
        double mejorCoHijo = Double.MAX_VALUE;
        for (int i = 0; i < tp; i++) {
            //cargaAleatoria(tam, cromosomas.get(i), rangoMin, rangoMax);
            costes[i] = calculaCoste(cromosomas.get(i), funcion);
            if (costes[i] < mejorCo) {
                mejorCo = costes[i];
                mejorCr = cromosomas.get(i);
            }
        }
        double mejorCosteGlobal = mejorCo;
        double[] mejorCroGlobal = mejorCr;

        double probMutacion = kProbMuta;
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
                nuevaAg.add(i, cromosomas.get(posi[i]));
                costesH[i] = costes[posi[i]];
            }
            int p1;
            double[] h1 = new double[tam];
            double[] h2 = new double[tam];
            boolean[] marcados = new boolean[tp];
            for (int i = 0; i < tp; i++) {
                marcados[i] = false;
            }
            for (int i = 0; i < tp; i++) {
                double x = random.nextDouble();
                if (x < kProbCruce) {
                    while (i == (p1 = random.nextInt(tp - 1 - 0) + 0)) ;
                    //cruceBLX(tam, nuevaAg.get(i), alfa, h1, h2);
                    nuevaAg.add(i, h1);
                    nuevaAg.add(p1, h2);
                    marcados[i] = marcados[p1] = true;
                }
            }
            int c1, c2;
            List<double[]> nuevaAG = nuevaAg;
            for (int i = 0; i < tp; i++) {
                c1 = random.nextInt(tp - 1 - 0) + 0;
                double x = random.nextDouble();
                if (x < kProbCruce) {
                    while (c1 == (c2 = random.nextInt(tp - 1 - 0) + 0)) ;
                    //crucleBLX(tam, nuevaAg.get(i), nuevaAg.get(p1), alfa, h1, h2);
                    nuevaAG.add(c1, h1);
                    nuevaAG.add(c2, h2);
                    marcados[c1] = marcados[c2] = true;
                }
            }
            nuevaAg = nuevaAG;
            for (int i = 0; i < tp; i++) {
                boolean m = false;
                for (int j = 0; j < tam; j++) {
                    double x = random.nextDouble();
                    if (x < probMutacion) {
                        m = true;
                        double valor = random.nextDouble(rangoMax - rangoMin) + rangoMin;
                        //Mutacion(nuevaAg.get(i), j, valor);
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
                if (costesH[i] > peorCoHijo) {
                    peorCoHijo = costesH[i];
                    peor = i;
                }
                if (costesH[i] < mejorCoHijo) {
                    mejorCoHijo = costesH[i];
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
                nuevaAg.add(peor, mejorCr);
                costesH[peor] = mejorCo;
            }
            mejorCr = nuevaAg.get(mejorCrHijo);
            costesH[peor] = mejorCo;

            if (mejorCoHijo < mejorCosteGlobal) {
                mejorCosteGlobal = mejorCoHijo;
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
