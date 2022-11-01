package uja.meta.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

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

    public static double MAPE(double[] real, double[] estimation) {
        int N = real.length;
        double score;
        double sum = 0.0;
        double num = 0.0;
        for (int i = 0; i < N; i++) {
            if (real[i] != 0) {
                sum += Math.abs((real[i] - estimation[i]) / Math.abs(real[i]));
                num++;
            }
        }
        score = sum / num;
        return score;
    }

    public static double RMSE(double[] real, double[] estimation) {
        int N = real.length;
        double score;
        double sum = 0;
        for (int i = 0; i < N; i++) {
            sum += Math.pow(real[i] - estimation[i], 2);
        }
        score = Math.sqrt(1.0 / N * sum);
        return score;
    }

    public static List<String> getFiles(final File folder) {
        List<String> fileName = new ArrayList<>();
        for (final File fileEntry : Objects.requireNonNull(folder.listFiles())) {
            if (fileEntry.isDirectory()) {
                getFiles(fileEntry);
            } else {
                fileName.add(fileEntry.getName());
            }
        }
        return fileName.stream().sorted().collect(Collectors.toList());
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
            vector.append(vSolucion[i]).append(", ");
        }
        vector.append(vSolucion[vSolucion.length - 1]).append("]");
        return vector.toString();
    }

    public static void nuevaSolucion(double[] vSolucion, double[] vecino, int j, double rangoInf, double rangoSup) {
        double inf, sup;
        inf = vSolucion[j] * 0.9;
        sup = vSolucion[j] * 1.1;
        if (inf < rangoInf)
            inf = rangoInf;
        if (sup > rangoSup)
            sup = rangoSup;
        vecino[j] = (Math.random() * (sup - inf)) + inf;
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
        return String.format("%.2f", variable);
    }

    public static void exportCSV(List<Future<Solucion>> soluciones, String name)
            throws IOException, ExecutionException, InterruptedException {

        List<Solucion> resultado1 = new ArrayList<>();
        List<Solucion> resultado2 = new ArrayList<>();
        List<Solucion> resultado3 = new ArrayList<>();
        List<Solucion> resultado4 = new ArrayList<>();
        List<Solucion> resultado5 = new ArrayList<>();

        for (Future<Solucion> solucion : soluciones) {
            Solucion s = solucion.get();
            if (s.getSemilla() == 53916079) {
                resultado1.add(s);
            } else if (s.getSemilla() == 95391607) {
                resultado2.add(s);
            } else if (s.getSemilla() == 79539160) {
                resultado3.add(s);
            } else if (s.getSemilla() == 7953916) {
                resultado4.add(s);
            } else if (s.getSemilla() == 60795391) {
                resultado5.add(s);
            }
        }

        FileOutputStream csvFile = new FileOutputStream("csv/" + name + ".csv");
        try (PrintWriter pw = new PrintWriter(csvFile)) {
            resultado1.stream()
                    .map(FuncionesAuxiliares::convertToCSV)
                    .forEach(pw::print);

            pw.println();

            resultado2.stream()
                    .map(FuncionesAuxiliares::convertToCSV)
                    .forEach(pw::print);

            pw.println();

            resultado3.stream()
                    .map(FuncionesAuxiliares::convertToCSV)
                    .forEach(pw::print);

            pw.println();

            resultado4.stream()
                    .map(FuncionesAuxiliares::convertToCSV)
                    .forEach(pw::print);

            pw.println();

            resultado5.stream()
                    .map(FuncionesAuxiliares::convertToCSV)
                    .forEach(pw::write);
        }
    }

    private static String convertToCSV(Solucion solucion) {
        return solucion.toString();
    }

}
