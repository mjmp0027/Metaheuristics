package utils;

import java.util.Random;

public class GeneradorSolucion {
    public static double[] generador(double rangoInf, double rangosup, long semilla, int D) {
        Random random = new Random();
        random.setSeed(semilla);
        double[] vector = new double[D];
        for (int i = 0; i < D; i++) {
            vector[i] = random.nextDouble(rangosup - rangoInf) + rangoInf;
        }
        return vector;
    }
}
