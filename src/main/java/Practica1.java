import org.apache.log4j.BasicConfigurator;
import uja.meta.algoritmos.*;
import uja.meta.utils.Lector;
import uja.meta.utils.Solucion;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import static uja.meta.utils.FuncionesAuxiliares.*;

public class Practica1 {

    public static void main(String[] args) throws IOException, InterruptedException, ExecutionException {
        double tiempoInicial = System.nanoTime();
        BasicConfigurator.configure();
        String ruta = "src/main/resources/configFiles/";
        ExecutorService executor = Executors.newCachedThreadPool();
        List<Future<Solucion>> resultadoEvMedia = new ArrayList<>();
        List<Future<Solucion>> resultadoEvBlX = new ArrayList<>();
        List<Future<Solucion>> resultadoEvDif = new ArrayList<>();
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
            double kProbMuta = lector.getKProbMuta();
            double kProCruce = lector.getKProbCruce();
            double alfa = lector.getAlfa();
            double probRecomb = lector.getProbRecomb();
            for (String algoritmo : algoritmos) {
                for (long semilla : semillas) {
                    List<double[]> cromosoma = generador(rangoInf, rangoSup, semilla, D, tp);
                    double[] vSolucion = new double[D];
                    switch (algoritmo) {
//                        case "evm" -> {
//                            AlgEvMedia_Clase01_Grupo10 EvM =
//                                    new AlgEvMedia_Clase01_Grupo10(funcion + ".EvM." + semilla, tp, D, iteraciones,
//                                            cromosoma, vSolucion, rangoInf, rangoSup, kProbMuta, kProCruce, funcion, semilla);
//                            resultadoEvMedia.add(executor.submit(EvM));
//                        }
//                        case "evblx" -> {
//                            AlgEvBLX_Clase01_Grupo10 EvBlk =
//                                    new AlgEvBLX_Clase01_Grupo10(funcion + ".EvBlk." + semilla, tp, D, iteraciones,
//                                            cromosoma, vSolucion, rangoInf, rangoSup, kProbMuta, kProCruce, alfa, funcion, semilla);
//                            resultadoEvBlX.add(executor.submit(EvBlk));
//                        }
                        case "ed" -> {
                            AlgEvDif_Clase01_Grupo10 ed =
                                    new AlgEvDif_Clase01_Grupo10(funcion + ".EvDif." + semilla, tp, D, iteraciones,
                                            cromosoma, vSolucion, rangoInf, rangoSup, funcion, probRecomb, semilla);
                            resultadoEvDif.add(executor.submit(ed));
                        }
                    }
                }
            }
        }

        executor.shutdown();
        if (!executor.awaitTermination(1, TimeUnit.MINUTES))
            executor.shutdownNow();

        // Conversion de resultados a CSV
//        exportCSV(resultadoEvMedia, "EvMedia");
//        exportCSV(resultadoEvBlX, "EvBlX");
//        exportCSV(resultadoEvDif, "EvDif");

        double tiempoFinal = System.nanoTime();
        System.out.println("Tiempo total PRÁCTICA 2: " + calcularTiempo(tiempoInicial, tiempoFinal) + " ms");
    }
}
