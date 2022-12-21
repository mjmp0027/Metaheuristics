package uja.meta;

import org.apache.log4j.BasicConfigurator;
import uja.meta.algoritmos.Hormigas;
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
    //TODO actualizar readme
    //TODO actualizar Manual
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
            long[] semillas = lector.getSemillas();
            List<String> ficheros = lector.getFicheros();
            for (String fichero : ficheros) {
                TSP tsp = tspLector("src/main/resources/tspFiles/" + fichero);
                for (long semilla : semillas) {
                    double greedy = greedy(tsp.getMatriz(), tsp.getDimension());
                    Hormigas sch = new Hormigas(fichero + "." + semilla, tsp.getMatriz(), lector.getIteraciones(),
                            semilla, tsp.getDimension(), lector.getTHormigas(), lector.getAlfah(), lector.getBetah(),
                            lector.getQ0(), lector.getP(), lector.getFi(), greedy, lector.getTiempo());
                    resultadoHormigas.add(executor.submit(sch));
                }
            }
        }
        executor.shutdown();
        if (!executor.awaitTermination(3, TimeUnit.MINUTES))
            executor.shutdownNow();

        //Conversion de resultados a CSV);
//        exportCSV(resultadoHormigas, "SCH");

        double tiempoFinal = System.nanoTime();
        System.out.println("Tiempo total PRÁCTICA 2: " + calcularTiempo(tiempoInicial, tiempoFinal) + " ms");
    }
}
