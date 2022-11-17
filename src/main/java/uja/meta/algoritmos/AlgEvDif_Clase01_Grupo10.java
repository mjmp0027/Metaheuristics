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
public class AlgEvDif_Clase01_Grupo10 implements Callable<Solucion> {
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

    @Override
    public Solucion call() throws Exception {
        Logger logger = Logger.getLogger(className);
        Random random = new Random();
        int t = 0;
        double[] costes = new double[tp];
        double mejorCoste = Double.MAX_VALUE;
        double[] mejorCr = new double[tp];
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

        while (contEv < limiteEvaluaciones) {
            t++;

            double[] ale1, ale2, obj, nuevo = new double[tp], padre;
            int a1, a2, k1, k2, k3;

            for (int i = 0; i < tp; i++) {
                padre = cromosomas.get(i);
                do {
                    a1 = random.nextInt(tp - 1 - 0);
                    while (a1 == (a2 = random.nextInt(tp - 1 - 0))) ;
                } while (a1 != i && a2 != i);
                if (a1 >= tp)
                    a1 = tp - 1;
                ale1 = cromosomas.get(a1);
                ale2 = cromosomas.get(a2);

                do {
                    k1 = random.nextInt(tp - 1 - 0);
                    k2 = random.nextInt(tp - 1 - 0);
                    k3 = random.nextInt(tp - 1 - 0);
                    while (k1 == k2) ;
                    while (k1 == k2 && k2 == k3) ;
                } while (k1 != i && k1 != a1 && k1 != a2 &&
                        k2 != i && k2 != a1 && k2 != a2 &&
                        k3 != i && k3 != a1 && k3 != a2);
                if (costes[k1] < costes[k2] && costes[k1] < costes[k3])
                    obj = cromosomas.get(k1);
                else if (costes[k2] < costes[k1] && costes[k2] < costes[k3])
                    obj = cromosomas.get(k2);
                else
                    obj = cromosomas.get(k3);

                double factor = random.nextDouble();
                for (int j = 0; j < D; j++) {
                    double porc = random.nextDouble();
                    if (porc > probRecomb)
                        nuevo[j] = obj[j];
                    else {
                        nuevo[j] = padre[j] + (factor * (ale1[j] - ale2[j]));
                        if (nuevo[j] > rangoMax)
                            nuevo[j] = rangoMax;
                        else if (nuevo[j] < rangoMin)
                            nuevo[j] = rangoMin;
                    }
                }

                double nuevoCoste = calculaCoste(nuevo, funcion);
                contEv++;
                if (nuevoCoste < costes[i]) {
                    cromosomas.add(i, nuevo);
                    costes[i] = nuevoCoste;
                    if (nuevoCoste < mejorCoste) {
                        mejorCoste = nuevoCoste;
                        mejorCr = nuevo;
                    }
                }
            }
            if (mejorCoste < mejorCosteGlobal) {
                mejorCosteGlobal = mejorCoste;
                mejorCroGlobal = mejorCr;
            }
        }

        //TODO
        vSolucion = mejorCroGlobal;
        System.out.println("Total evaluaciones: " + contEv);
        System.out.println("Total iteraciones: " + t);
        return null;
    }
}
