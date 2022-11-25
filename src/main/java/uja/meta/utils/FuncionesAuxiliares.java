package uja.meta.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import static java.lang.Math.max;
import static java.lang.Math.min;
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

    public static void torneo(int tp, int[] posicion, double[] costes, List<double[]> cromosomas,
                              List<double[]> nuevaAg, double[] costesNuevaAg, Random random) {
        for (int i = 0; i < tp; i++) {
            int j, k;
            j = random.nextInt(tp - 1);
            while (j == (k = random.nextInt(tp - 1))) ;
            posicion[i] = (costes[i] < costes[k]) ? j : k;
        }
        for (int i = 0; i < tp; i++) {
            nuevaAg.add(i, cromosomas.get(posicion[i]));
            costesNuevaAg[i] = costes[posicion[i]];
        }
    }

    public static void torneo2a2(int tp, List<double[]> nuevaAg, double[] costesNuevaAg, double[] mejor1,
                                 double[] mejor2, Random random, double costeMejor1, double costeMejor2) {
        int c1, c2, c3, c4;
        int posAnt = 0;

        c1 = random.nextInt(tp - 1);
        while (c1 == (c2 = random.nextInt(tp - 1))) ;
        if (costesNuevaAg[c1] < costesNuevaAg[c2]) {
            mejor1 = nuevaAg.get(c1);
            costeMejor1 = costesNuevaAg[c1];
        } else {
            mejor1 = nuevaAg.get(c2);
            costeMejor1 = costesNuevaAg[c2];
        }
        while (posAnt == (c3 = random.nextInt(tp - 1))) ;
        while (posAnt == (c4 = random.nextInt(tp - 1))) ;


        if (costesNuevaAg[c3] < costesNuevaAg[c4]) {
            mejor2 = nuevaAg.get(c3);
            costeMejor2 = costesNuevaAg[c3];
        } else {
            mejor2 = nuevaAg.get(c4);
            costeMejor2 = costesNuevaAg[c4];
        }
    }

    public static void mutar(int tp, int D, double kProbMuta, double rangoMin, double rangoMax,
                             List<double[]> nuevaAg, boolean[] marcados, Random random) {
        for (int i = 0; i < tp; i++) {
            boolean m = false;
            for (int j = 0; j < D; j++) {
                double uniforme = random.nextDouble();
                if (uniforme < kProbMuta) {
                    m = true;
                    double valor = random.nextDouble(rangoMax - rangoMin) + rangoMin;
                    Mutacion(nuevaAg.get(i), j, valor);
                }
            }
            if (m)
                marcados[i] = true;
        }
    }

    public static void calculoElite(int tp, List<double[]> nuevaAg, double[] mejorCr,
                                    double[] costesNuevaAg, double mejorCoste, Random random, int peor) {
        int p1, p2, p3, p4;
        p1 = random.nextInt(tp - 1);
        p2 = random.nextInt(tp - 1);
        p3 = random.nextInt(tp - 1);
        p4 = random.nextInt(tp - 1);
        while (p1 == p2) ;
        while (p1 == p2 && p2 == p3) ;
        while (p1 == p2 && p2 == p3 && p3 == p4) ;
        if (costesNuevaAg[p1] > costesNuevaAg[p2] && costesNuevaAg[p1] > costesNuevaAg[p3] && costesNuevaAg[p1] > costesNuevaAg[p4])
            peor = p1;
        else if (costesNuevaAg[p2] > costesNuevaAg[p1] && costesNuevaAg[p2] > costesNuevaAg[p3] && costesNuevaAg[p2] > costesNuevaAg[p4])
            peor = p2;
        else if (costesNuevaAg[p3] > costesNuevaAg[p1] && costesNuevaAg[p3] > costesNuevaAg[p2] && costesNuevaAg[p3] > costesNuevaAg[p4])
            peor = p3;
        else
            peor = p4;
        nuevaAg.add(peor, mejorCr);
        costesNuevaAg[peor] = mejorCoste;
    }

    public static void eleccion2aleatorios(int tp, List<double[]> cromosomas, double[] costes, int i,
                                           double[] ale1, double[] ale2, Random random, int a1, int a2) {
        do {
            a1 = random.nextInt(tp - 1 - 0);
            while (a1 == (a2 = random.nextInt(tp - 1 - 0))) ;
        } while (a1 != i && a2 != i);
        if (a1 >= tp)
            a1 = tp - 1;
        ale1 = cromosomas.get(a1);
        ale2 = cromosomas.get(a2);
    }

    public static void torneoK3(int tp, int i, int a1, int a2, int k1, int k2, int k3, int k4, Random random) {
        do {
            k1 = random.nextInt(tp - 1 - 0);
            k2 = random.nextInt(tp - 1 - 0);
            k3 = random.nextInt(tp - 1 - 0);
            while (k1 == k2) ;
            while (k1 == k2 && k2 == k3) ;
        } while (k1 != i && k1 != a1 && k1 != a2 &&
                k2 != i && k2 != a1 && k2 != a2 &&
                k3 != i && k3 != a1 && k3 != a2);
    }

    public static void reemplazamiento(double nuevoCoste, int i, double[] costes, List<double[]> cromosomas,
                                       double[] nuevo, double mejorCoste, double[] mejorCr){
        if (nuevoCoste < costes[i]) {
            cromosomas.add(i, nuevo);
            costes[i] = nuevoCoste;
            if (nuevoCoste < mejorCoste) {
                mejorCoste = nuevoCoste;
                mejorCr = nuevo;
            }
        }
    }

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

    public static List<double[]> generador(double rangoInf, double rangosup, long semilla, int D, int tp) {
        Random random = new Random();
        random.setSeed(semilla);
        List<double[]> vector = new ArrayList<>();
        double[] vectorAux = new double[D];
        for (int j = 0; j < tp; j++) {
            for (int i = 0; i < D; i++) {
                vectorAux[i] = random.nextDouble(rangosup - rangoInf) + rangoInf;
            }
            vector.add(j, vectorAux);
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

    public static void cruceBLX(int tam, double[] mejor1, double[] mejor2, double alfaBLX, double[] h1,
                                double rangoMin, double rangoMax) {
        Random random = new Random();
        double Cmax, Cmin, I = 0;
        for (int i = 0; i < tam; i++) {
            Cmax = max(mejor1[i], mejor2[i]);
            Cmin = min(mejor1[i], mejor2[i]);
            double r1 = Cmin - (I * alfaBLX);
            if (r1 > rangoMin)
                r1 = rangoMin;
            double r2 = Cmax + (I * alfaBLX);
            if (r2 < rangoMax)
                r2 = rangoMax;
            h1[i] = random.nextDouble(r2 - r1) + r1;
        }
    }

    public static void cruceMedia(int tam, double[] v, double[] w, double[] h) {

        for (int i = 0; i < tam; i++) {
            h[i] = (v[i] + w[i]) / 2;
        }
    }

    public static void Mutacion(double[] v, int pos, double valor) {
        v[pos] = valor;
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
    //TODO crear un archivo .txt que sea igual que el .properties con bucles leyendo los config
    // donde quede cada parámetro con un solo valor cada semilla con cada algoritmo y a su vez
    // con cada funcion 5(semillas) * 4(algoritmos) * 10(funciones) = 200(logs)

}
