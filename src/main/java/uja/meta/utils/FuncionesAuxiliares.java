package uja.meta.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FuncionesAuxiliares {
    public static List<String> getFiles(final File folder) {
        List<String> fileName = new ArrayList<>();
        for (final File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                getFiles(fileEntry);
            } else {
                fileName.add(fileEntry.getName());
            }
        }
        return fileName;
    }

    public static double[] generador(double rangoInf, double rangosup, long semilla, int D) {
        Random random = new Random();
        random.setSeed(semilla);
        double[] vector = new double[D];
        for (int i = 0; i < D; i++) {
            vector[i] = random.nextDouble(rangosup - rangoInf) + rangoInf;
        }
        return vector;
    }

    public static String visualizaVectorLog(int[] vSolucion) {
        StringBuilder vector = new StringBuilder();
        vector.append("[");
        for (int i = 0; i < vSolucion.length-1; i++) {
            vector.append(i).append(", ");
        }
        vector.append(vSolucion[vSolucion.length-1]).append("]");
        return vector.toString();
    }

    public static void intercambiaPos(double[] vSolucion, int i, int j) {
        double aux = vSolucion[i];
        vSolucion[i] = vSolucion[j];
        vSolucion[j] = aux;
    }
}
