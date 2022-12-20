package uja.meta;

import org.apache.log4j.BasicConfigurator;
import uja.meta.algoritmos.practica3.Hormigas;
import uja.meta.utils.Lector;
import uja.meta.utils.Solucion;
import uja.meta.utils.TSP;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static uja.meta.utils.FuncionesAuxiliares.*;
import static uja.meta.utils.LectorTSP.tspLector;

public class Proceso {

    public static void main(String[] args) throws Exception {
        double tiempoInicial = System.nanoTime();
        BasicConfigurator.configure();
        String ruta = "src/main/resources/configFiles/";
        ExecutorService executor = Executors.newCachedThreadPool();
        List<Future<Solucion>> resultadoHormigas = new ArrayList<>();
        final File folder = new File(ruta);
        List<String> archivosConfig = getFiles(folder);
        for (String archivo : archivosConfig) {
            Lector lector = new Lector(ruta + archivo);
            List<String> algoritmos = lector.getAlgoritmos();
            String funcion = lector.getFuncion();
            long[] semillas = lector.getSemillas();
            int D = lector.getD();
            int tp = lector.getTp();
            double rangoInf = lector.getRangoInf();
            double rangoSup = lector.getRangoSup();
            long iteraciones = lector.getIteraciones();
            int tHormigas = lector.getTHormigas();
            int ciudades = lector.getCiudades();
            int alfah = lector.getAlfah();
            int betah = lector.getBetah();
            double q0 = lector.getQ0();
            double p = lector.getP();
            double fi = lector.getFi();
            int tiempo = lector.getTiempo();
            TSP tsp = tspLector("ch130.tsp"); // FIXME prueba de 1 solo (el mas liviano) -- hacer for
            for (long semilla : semillas) {
                greedy(tsp.getMatriz(), tsp.getDimension());

                // TODO leer 3 archivos y ejecutar la función greedy(funcionesAuxiliares) para cada archivo
                // TODO dist tiene que ser igual a la matriz de distancias que dan en los ficheros(hay que leer los ficheros)
                double[][] dist = new double[10][10];
                Hormigas sch =
                        new Hormigas(funcion + ".hormigas." + semilla, dist, iteraciones, semilla,
                                ciudades, tHormigas, alfah, betah, q0, p, fi, 1.1, tiempo);
                resultadoHormigas.add(executor.submit(sch));
                //TODO appenders
                // FIXME comprobar configSCH, si se necesita cambiar algo se cambia
            }
        }

        executor.shutdown();
        if (!executor.awaitTermination(3, TimeUnit.MINUTES))
            executor.shutdownNow();

        //Conversion de resultados a CSV
//        exportCSV(resultadoEvMedia, "EvMedia");
//        exportCSV(resultadoEvBlX, "EvBlx");
//        exportCSV(resultadoBL3, "BL3");
//        exportCSV(resultadoBLk, "BLk");
//        exportCSV(resultadoTabu, "Tabu");
//        exportCSV(resultadoTabuVNS, "TabuVNS");
//        exportCSV(resultadoEvDif, "EvDif");
//        exportCSV(resultadoHormigas, "SCH");

        double tiempoFinal = System.nanoTime();
        System.out.println("Tiempo total PRÁCTICA 2: " + calcularTiempo(tiempoInicial, tiempoFinal) + " ms");
    }
}
