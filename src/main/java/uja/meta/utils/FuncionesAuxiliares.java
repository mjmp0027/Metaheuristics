package uja.meta.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

import static java.lang.Math.max;
import static java.lang.Math.min;
import static uja.meta.funciones.Ackley.evaluateA;
import static uja.meta.funciones.Dixonprice.evaluateD;
import static uja.meta.funciones.Griewank.evaluateG;
import static uja.meta.funciones.MAPE.MAPE;
import static uja.meta.funciones.Michalewicz.evaluateM;
import static uja.meta.funciones.Perm.evaluateP;
import static uja.meta.funciones.RMSE.RMSE;
import static uja.meta.funciones.Rastringin.evaluateR;
import static uja.meta.funciones.Rosenbrock.evaluateRosen;
import static uja.meta.funciones.RotatedHypeEllipsoid.evaluateRot;
import static uja.meta.funciones.Schewefel.evaluateS;
import static uja.meta.funciones.Trid.evaluateT;
import static uja.meta.utils.LectorDaido.daidos;

public class FuncionesAuxiliares {

    public static void torneo(int tp, int[] posicion, double[] costes, List<double[]> cromosomas,
                              List<double[]> nuevaAg, double[] costesNuevaAg, Random random) {
        for (int i = 0; i < tp; i++) {
            int j, k;
            j = random.nextInt(tp);
            while (j == (k = random.nextInt(tp))) ;
            posicion[i] = (costes[i] < costes[k]) ? j : k;
        }
        for (int i = 0; i < tp; i++) {
            nuevaAg.add(i, cromosomas.get(posicion[i]));
            costesNuevaAg[i] = costes[posicion[i]];
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
                    nuevaAg.get(i)[j] = valor;
                }
            }
            if (m)
                marcados[i] = true;
        }
    }

    public static void reemplazamiento(int tp, List<double[]> nuevaAg, double[] mejorCr,
                                       double[] costesNuevaAg, double mejorCoste, Random random) {

        int p1, p2, p3, p4, peor;
        p1 = random.nextInt(tp);
        while (p1 == (p2 = random.nextInt(tp))) ;
        while (p1 == (p3 = random.nextInt(tp))) ;
        while (p1 == (p4 = random.nextInt(tp))) ;
        if (costesNuevaAg[p1] > costesNuevaAg[p2] && costesNuevaAg[p1] > costesNuevaAg[p3]
                && costesNuevaAg[p1] > costesNuevaAg[p4])
            peor = p1;
        else if (costesNuevaAg[p2] > costesNuevaAg[p1] && costesNuevaAg[p2] > costesNuevaAg[p3]
                && costesNuevaAg[p2] > costesNuevaAg[p4])
            peor = p2;
        else if (costesNuevaAg[p3] > costesNuevaAg[p1] && costesNuevaAg[p3] > costesNuevaAg[p2]
                && costesNuevaAg[p3] > costesNuevaAg[p4])
            peor = p3;
        else
            peor = p4;
        nuevaAg.add(peor, mejorCr);
        costesNuevaAg[peor] = mejorCoste;
    }

    public static void eleccion2aleatorios(int tp, List<double[]> cromosomas, int i,
                                           double[] ale1, double[] ale2, Random random, int a1, int a2) {
        do {
            a1 = random.nextInt(tp);
            while (a1 == (a2 = random.nextInt(tp))) ;
        } while (a1 != i && a2 != i);
        ale1 = cromosomas.get(a1);
        ale2 = cromosomas.get(a2);
    }

    public static void torneoK3(int tp, int i, int a1, int a2, Random random, List<double[]> cromosomas, double[] obj, double[] costes) {
        int k1, k2, k3;
        do {
            k1 = random.nextInt(tp);
            while (k1 == (k2 = random.nextInt(tp))) ;
            while (k1 == (k3 = random.nextInt(tp))) ;
        } while (k1 != i && k1 != a1 && k1 != a2 &&
                k2 != i && k2 != a1 && k2 != a2 &&
                k3 != i && k3 != a1 && k3 != a2);

        if (costes[k1] < costes[k2] && costes[k1] < costes[k3])
            obj = cromosomas.get(k1);
        else if (costes[k2] < costes[k1] && costes[k2] < costes[k3])
            obj = cromosomas.get(k2);
        else
            obj = cromosomas.get(k3);
    }

    public static void eleccionNuevoCr(int D, double probRecomb, double[] padre, double[] nuevo,
                                       double[] ale1, double[] ale2, double rangoMin, double rangoMax, double[] obj, Random random) {
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
    }

    public static void reemplazamiento(double nuevoCoste, int i, double[] costes, List<double[]> cromosomas,
                                       double[] nuevo, double mejorCoste, double[] mejorCr) {
        if (nuevoCoste < costes[i]) {
            cromosomas.add(i, nuevo);
            costes[i] = nuevoCoste;
            if (nuevoCoste < mejorCoste) {
                mejorCoste = nuevoCoste;
                mejorCr = nuevo;
            }
        }
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

        for (int j = 0; j < tp; j++) {
            double[] vectorAux = new double[D];
            for (int i = 0; i < D; i++) {
                vectorAux[i] = random.nextDouble(rangosup - rangoInf) + rangoInf;
            }
            vector.add(j, vectorAux);
        }
        return vector;
    }

    public static List<int[]> generadorH(long semilla, int ciudades, int tHormigas, List<boolean[]> marcados) {
        Random random = new Random();
        random.setSeed(semilla);
        List<int[]> vector = new ArrayList<>();
        for (int i = 0; i < tHormigas; i++) {
            int[] vectorAux = new int[ciudades];
            boolean[] vectorMarcados = new boolean[ciudades];
            vectorAux[0] = random.nextInt(ciudades);
            vectorMarcados[0] = true;
            vector.add(i, vectorAux);
            marcados.add(i, vectorMarcados);
        }
        return vector;
    }

    public static double calculaCoste(int[] hormiga, double[][] dist, int ciudades) {
        double coste = 0;
        for (int i = 0; i < ciudades - 1; i++) {
            coste += dist[hormiga[i]][hormiga[i + 1]];
        }
        coste += dist[hormiga[0]][hormiga[ciudades - 1]];
        return coste;
    }

    public static void mejorHormiga(double mejorCosteActual, int tHormigas, List<int[]> hormigas,
                                    double[][] dist, int ciudades, int[] mejorHormigaActual) {
        for (int i = 0; i < tHormigas; i++) {
            double coste = calculaCoste(hormigas.get(i), dist, ciudades);
            if (coste < mejorCosteActual) {
                mejorCosteActual = coste;
                mejorHormigaActual = hormigas.get(i);
            }
        }
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

    public static double calculaCoste(double[] vSolucion, String funcion) throws IOException {
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
            case "MAPE" -> {
                return potenciaMAPE(vSolucion);
            }
            case "RMSE" -> {
                return potenciaRMSE(vSolucion);
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
        double Cmax, Cmin, I;
        for (int i = 0; i < tam; i++) {
            Cmax = max(mejor1[i], mejor2[i]);
            Cmin = min(mejor1[i], mejor2[i]);
            I = Cmax - Cmin;
            double r1 = Cmin - (I * alfaBLX);
            if (r1 > rangoMin)
                r1 = rangoMin;
            double r2 = Cmax + (I * alfaBLX);
            if (r2 < rangoMax)
                r2 = rangoMax;
            h1[i] = random.nextDouble(r2 - r1) + r1;
        }
    }

    public static void cruceMedia(int D, double[] mejorCr1, double[] mejorCr2, double[] h) {
        for (int i = 0; i < D; i++)
            h[i] = (mejorCr1[i] + mejorCr2[i]) / 2;
    }

    public static void exportCSV(List<Future<Solucion>> soluciones, String name)
            throws IOException, ExecutionException, InterruptedException, TimeoutException {

        List<Solucion> resultado1 = new ArrayList<>();
        List<Solucion> resultado2 = new ArrayList<>();
        List<Solucion> resultado3 = new ArrayList<>();
        List<Solucion> resultado4 = new ArrayList<>();
        List<Solucion> resultado5 = new ArrayList<>();

        for (Future<Solucion> solucion : soluciones) {
            Solucion s = solucion.get(10, TimeUnit.SECONDS);
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

    private static String convertToLogAppender(String algoritmo, String funcion, String semilla) {
        return "log4j.appender." + funcion + "." + algoritmo + "." + semilla +
                " = org.apache.log4j.FileAppender\n" +
                "log4j.appender." + funcion + "." + algoritmo + "." + semilla +
                ".file = src/main/resources/logs/" + funcion + "/" + algoritmo + "/" + semilla + ".log\n" +
                "log4j.appender." + funcion + "." + algoritmo + "." + semilla +
                ".append = false\n" +
                "log4j.appender." + funcion + "." + algoritmo + "." + semilla +
                ".layout = org.apache.log4j.PatternLayout\n" +
                "log4j.appender." + funcion + "." + algoritmo + "." + semilla +
                ".layout.ConversionPattern = %d %c{3} - %m%n\n\n" +
                "log4j.logger." + funcion + "." + algoritmo + "." + semilla +
                " = INFO, " + funcion + "." + algoritmo + "." + semilla + "\n\n";
    }

    /**
     * @param archivosConfig
     * @param ruta
     * @throws IOException
     * @brief Solo se ejecuta una vez para crear el log4j.properties
     */
    public static void createAppendersLog(List<String> archivosConfig, String ruta) throws IOException {
        FileOutputStream csvFile = new FileOutputStream("log/log4j.properties");
        try (PrintWriter pw = new PrintWriter(csvFile)) {
            for (String archivo : archivosConfig) {
                Lector lector = new Lector(ruta + archivo);
                List<String> algoritmos = lector.getAlgoritmos();
                long[] semillas = lector.getSemillas();
                List<Long> semillasList = Arrays.stream(semillas).boxed().collect(Collectors.toList());

                for (String algoritmo : algoritmos) {
                    semillasList.stream()
                            .map(s -> convertToLogAppender(algoritmo, lector.getFuncion(), String.valueOf(s)))
                            .forEach(pw::print);

                    pw.println();
                }
            }
        }
    }

    public static double potenciaMAPE(double[] a) throws IOException {
        List<Daido> observaciones = daidos("src/main/resources/daido-tra.dat");
        double pm;
        int filas = observaciones.size();
        double[] real = new double[filas], estimado = new double[filas];

        for (int i = 0; i < filas; i++) {
            pm = observaciones.get(i).getDni() * (a[0] + (a[1] * observaciones.get(i).getDni()) + (a[2]
                    * observaciones.get(i).getTemp_amb()) + (a[3] * observaciones.get(i).getVel_viento()) +
                    (a[4] * observaciones.get(i).getSmr()));
            estimado[i] = pm;
            real[i] = observaciones.get(i).getPotencia();

        }
        return MAPE(real, estimado);
    }

    public static double potenciaRMSE(double[] a) throws IOException {
        List<Daido> observaciones = daidos("src/main/resources/daido-tra.dat");
        double pm;
        int filas = observaciones.size();
        double[] real = new double[filas], estimado = new double[filas];

        for (int i = 0; i < filas; i++) {
            pm = observaciones.get(i).getDni() * (a[0] + (a[1] * observaciones.get(i).getDni()) + (a[2]
                    * observaciones.get(i).getTemp_amb()) + (a[3] * observaciones.get(i).getVel_viento()) +
                    (a[4] * observaciones.get(i).getSmr()));
            estimado[i] = pm;
            real[i] = observaciones.get(i).getPotencia();

        }
        return RMSE(real, estimado);
    }
}
