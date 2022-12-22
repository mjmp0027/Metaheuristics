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

import static java.lang.Math.*;

public class FuncionesAuxiliares {

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

    public static double calculaCoste(List<Integer> hormiga, double[][] dist, int ciudades) {
        double coste = 0;
        for (int i = 0; i < ciudades - 1; i++) {
            coste += dist[hormiga.get(i)][hormiga.get(i + 1)];
        }
        coste += dist[hormiga.get(0)][hormiga.get(ciudades - 1)];
        return coste;
    }

    public static String visualizaVectorLog(Integer[] vSolucion) {
        StringBuilder vector = new StringBuilder();
        vector.append("[");
        for (int i = 0; i < vSolucion.length - 1; i++) {
            vector.append(vSolucion[i]).append(", ");
        }
        vector.append(vSolucion[vSolucion.length - 1]).append("]");
        return vector.toString();
    }

    public static String calcularTiempo(double tiempoInicial, double tiempoFinal) {
        double totalTiempo = (tiempoFinal - tiempoInicial) / 1000000;
        return formato(totalTiempo);
    }

    public static String formato(double variable) {
        return String.format("%.2f", variable);
    }

    public static void exportCSV(List<Future<Solucion>> soluciones, String name) throws IOException, ExecutionException, InterruptedException, TimeoutException {

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
            resultado1.stream().map(FuncionesAuxiliares::convertToCSV).forEach(pw::print);

            pw.println();

            resultado2.stream().map(FuncionesAuxiliares::convertToCSV).forEach(pw::print);

            pw.println();

            resultado3.stream().map(FuncionesAuxiliares::convertToCSV).forEach(pw::print);

            pw.println();

            resultado4.stream().map(FuncionesAuxiliares::convertToCSV).forEach(pw::print);

            pw.println();

            resultado5.stream().map(FuncionesAuxiliares::convertToCSV).forEach(pw::write);
        }
    }

    private static String convertToCSV(Solucion solucion) {
        return solucion.toString();
    }

    private static String convertToLogAppender(String fichero, String semilla) {
        return "log4j.appender." + fichero + "." + semilla + " = org.apache.log4j.FileAppender\n"
                + "log4j.appender." + fichero + "." + semilla + ".file = src/main/resources/logs/"
                + fichero + "/" + semilla + ".log\n" + "log4j.appender." + fichero +
                "." + semilla + ".append = false\n" + "log4j.appender." + fichero + "." + semilla
                + ".layout = org.apache.log4j.PatternLayout\n" + "log4j.appender." + fichero + "."
                + semilla + ".layout.ConversionPattern = %d %c{3} - %m%n\n\n" + "log4j.logger." + fichero + "."
                + semilla + " = INFO, " + fichero + "." + semilla + "\n\n";
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
                long[] semillas = lector.getSemillas();
                List<Long> semillasList = Arrays.stream(semillas).boxed().collect(Collectors.toList());
                for (String fichero : lector.getFicheros()) {
                    semillasList.stream().map(s -> convertToLogAppender(fichero, String.valueOf(s))).forEach(pw::print);
                    pw.println();
                }
            }
        }
    }

    public static List<List<Integer>> generarPrimeraCiudad(long semilla, int ciudades, int tHormigas,
                                                           List<List<Boolean>> marcados, Random random) {
        random.setSeed(semilla);
        List<List<Integer>> vector = new ArrayList<>();
        for (int i = 0; i < tHormigas; i++) {
            List<Integer> vectorAux = new ArrayList<>();
            List<Boolean> vectorMarcados = new ArrayList<>();
            for (int j = 0; j < ciudades; j++) {
                vectorMarcados.add(j, false);
                vectorAux.add(j, 0);
            }
            int valor = random.nextInt(ciudades);
            vectorAux.set(0, valor);
            vectorMarcados.set(vectorAux.get(0), true);
            vector.add(i, vectorAux);
            marcados.add(i, vectorMarcados);
        }
        return vector;
    }

    public static void cargaInicial(double ferInicial, int ciudades, List<List<Double>> feromona,
                                    List<List<Double>> heuristica, double[][] dist) {

        for (int i = 0; i < ciudades - 1; i++)
            for (int j = i + 1; j < ciudades; j++)
                if (i != j) {
                    feromona.get(j).set(i, ferInicial);
                    feromona.get(i).set(j, ferInicial);
                    heuristica.get(j).set(i, 1 / dist[i][j]);
                    heuristica.get(i).set(j, 1 / dist[i][j]);
                }
    }

    public static double[] calculaFerxHeu(int ciudades, List<List<Boolean>> marcados,
                                          List<List<Double>> heuristica, List<List<Double>> feromona,
                                          List<List<Integer>> hormigas, int alfah, int betah, int h, int comp) {
        double[] ferxHeu = new double[ciudades];
        for (int i = 0; i < ciudades; i++) {
            if (!marcados.get(h).get(i))
                ferxHeu[i] = pow(heuristica.get(hormigas.get(h).get(comp - 1)).get(i), betah)
                        * pow(feromona.get(hormigas.get(h).get(comp - 1)).get(i), alfah);
        }
        return ferxHeu;
    }

    public static int transicion(int ciudades, List<List<Boolean>> marcados, int posArgMax,
                                 double q0, double[] ferxHeu, double denominador, Random random, int h) {
        int elegido = 0;
        double[] prob = new double[ciudades];
        double q = random.nextDouble();
        if (q0 <= q) {
            elegido = posArgMax;
        } else {
            for (int i = 0; i < ciudades; i++) {
                if (!marcados.get(h).get(i)) {
                    double numerador = ferxHeu[i];
                    prob[i] = numerador / denominador;
                }
            }
            double uniforme = random.nextDouble();
            double acumulado = 0.0;
            for (int i = 0; i < ciudades; i++) {
                if (!marcados.get(h).get(i)) {
                    acumulado += prob[i];
                    if (uniforme <= acumulado) {
                        elegido = i;
                        break;
                    }
                }
            }
        }
        return elegido;
    }

    public static void actualizacionLocal(List<List<Double>> feromona, List<List<Integer>> hormigas,
                                          int h, int comp, double fInicial, double fi) {
        feromona.get(hormigas.get(h).get(comp - 1)).set(hormigas.get(h).get(comp), ((1 - fi) *
                feromona.get(hormigas.get(h).get(comp - 1)).get(hormigas.get(h).get(comp))) + (fi * fInicial));

        feromona.get(hormigas.get(h).get(comp)).set(hormigas.get(h).get(comp - 1),
                feromona.get(hormigas.get(h).get(comp - 1)).get(hormigas.get(h).get(comp)));
    }

    public static void actualizarFeromona(double mejorCosteActual, int ciudades, List<List<Double>> feromona,
                                          Integer[] mejorHormigaActual, double p) {
        double deltaMejor = 1 / mejorCosteActual;  //al ser minimizacion
        for (int i = 0; i < ciudades - 1; i++) {
            double feromonaNueva = feromona.get(mejorHormigaActual[i]).get(mejorHormigaActual[i + 1]) + (p * deltaMejor);
            feromona.get(mejorHormigaActual[i]).set(mejorHormigaActual[i + 1], feromonaNueva);
            feromona.get(mejorHormigaActual[i + 1]).set(mejorHormigaActual[i],
                    feromona.get(mejorHormigaActual[i]).get(mejorHormigaActual[i + 1]));  //simetrica
        }

        for (int i = 0; i < ciudades; i++) {
            for (int j = 0; j < ciudades; j++) {
                if (i != j) {
                    feromona.get(i).set(j, ((1 - p) * feromona.get(i).get(j)));
                }
            }
        }
    }

    public static void limpiar(List<List<Integer>> hormigas, int tHormigas,
                               int ciudades, List<List<Boolean>> marcados) {
        hormigas.clear();
        for (int i = 0; i < tHormigas; i++) {
            for (int j = 0; j < ciudades; j++) {
                marcados.get(i).set(j, false);
            }
        }
    }

    /**
     * @param matriz   matriz de distancias cogida de cada archivo
     * @param ciudades número de ciudades cogida de cada archivo
     * @return el valor de greedy usado como parámetro al llamar al algoritmo
     * @brief se ejecuta una vez para cada archivo
     **/
    public static double greedy(double[][] matriz, int ciudades) {
        Random random = new Random();
        List<Integer> solucion = new ArrayList<>();
        for (int i = 0; i < ciudades; i++) {
            solucion.add(0);
        }
        boolean[] marcado = new boolean[ciudades];
        solucion.set(0, random.nextInt(ciudades));
        marcado[solucion.get(0)] = true;
        for (int i = 0; i < ciudades - 1; i++) {
            double menorDist = Double.MAX_VALUE;
            int posMenor = 0;
            for (int j = 0; j < ciudades; j++) {
                if (!marcado[j] && solucion.get(i) != j && matriz[solucion.get(i)][j] < menorDist) {
                    menorDist = matriz[solucion.get(i)][j];
                    posMenor = j;
                }
            }
            solucion.set(i + 1, posMenor);
            marcado[posMenor] = true;
        }
        return calculaCoste(solucion, matriz, ciudades);
    }
}
