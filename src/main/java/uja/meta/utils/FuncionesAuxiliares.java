package uja.meta.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static uja.meta.funciones.Ackley.evaluateA;
import static uja.meta.funciones.Dixonprice.evaluateD;
import static uja.meta.funciones.Griewank.evaluateG;
import static uja.meta.funciones.Michalewicz.evaluateM;
import static uja.meta.funciones.Perm.evaluateP;
import static uja.meta.funciones.Rastringin.evaluateR;
import static uja.meta.funciones.Rosenbrock.evaluateRosen;
import static uja.meta.funciones.RotatedHypeEllipsoid.evaluateRot;
import static uja.meta.funciones.Schewefel.evaluateS;
import static uja.meta.funciones.Trid.evaluateT;

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

    public static String visualizaVectorLog(double[] vSolucion) {
        StringBuilder vector = new StringBuilder();
        vector.append("[");
        for (int i = 0; i < vSolucion.length - 1; i++) {
            vector.append(i).append(", ");
        }
        vector.append(vSolucion[vSolucion.length - 1]).append("]");
        return vector.toString();
    }

    public static void intercambiaPos(double[] vSolucion, int i, int j) {
        double aux = vSolucion[i];
        vSolucion[i] = vSolucion[j];
        vSolucion[j] = aux;
    }

    public static double calculaCoste(double[] vSolucion, String funcion) {
        switch (funcion) {
            case "dixonprice" -> {
                return evaluateD(vSolucion);
            }
            case "ackley" -> {
                return evaluateA(vSolucion);
            }
            case "rosenbrock" -> {
                return evaluateRosen(vSolucion);
            }
            case "griewank" -> {
                return evaluateG(vSolucion);
            }
            case "rotatedHypeEllipsoid" -> {
                return evaluateRot(vSolucion);
            }
            case "perm" -> {
                return evaluateP(vSolucion);
            }
            case "trid" -> {
                return evaluateT(vSolucion);
            }
            case "schewefel" -> {
                return evaluateS(vSolucion);
            }
            case "rastringin" -> {
                return evaluateR(vSolucion);
            }
            case "michalewicz" -> {
                return evaluateM(vSolucion);
            }
            default -> {
                return 0;
            }
        }
    }

    public static String calcularTiempo(double tiempoInicial, double tiempoFinal) {
        double totalTiempo = (tiempoFinal - tiempoInicial) / 1000000;
        return formato(totalTiempo);
    }

    public static String formato(double variable) {
        return String.format("%.3f", variable);
    }
}
