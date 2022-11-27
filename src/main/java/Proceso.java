import org.apache.log4j.BasicConfigurator;
import uja.meta.algoritmos.practica1.AlgBL3_Clase01_Grupo10;
import uja.meta.algoritmos.practica1.AlgBLk_Clase01_Grupo10;
import uja.meta.algoritmos.practica1.AlgTabuVNS_Clase01_Grupo10;
import uja.meta.algoritmos.practica1.AlgTabu_Clase01_Grupo10;
import uja.meta.algoritmos.practica2.AlgEvDif_Clase01_Grupo10;
import uja.meta.utils.Daido;
import uja.meta.utils.Lector;
import uja.meta.utils.LectorDaido;
import uja.meta.utils.Solucion;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

import static uja.meta.utils.FuncionesAuxiliares.*;
import static uja.meta.utils.LectorDaido.daidos;

public class Proceso {

    public static void main(String[] args) throws IOException, InterruptedException, ExecutionException, TimeoutException {
        double tiempoInicial = System.nanoTime();
        BasicConfigurator.configure();
        String ruta = "src/main/resources/configFiles/";
        ExecutorService executor = Executors.newCachedThreadPool();
        List<Future<Solucion>> resultadoBL3 = new ArrayList<>();
        List<Future<Solucion>> resultadoBLk = new ArrayList<>();
        List<Future<Solucion>> resultadoTabu = new ArrayList<>();
        List<Future<Solucion>> resultadoTabuVNS = new ArrayList<>();
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
            double oscilacion = lector.getOscilacion();
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
//                        case "bl3" -> {
//                            AlgBL3_Clase01_Grupo10 bl3 =
//                                    new AlgBL3_Clase01_Grupo10(D, 3, semilla, iteraciones, oscilacion, rangoInf, rangoSup,
//                                            funcion, funcion + ".bl3" + "." + semilla, vSolucion);
//                            resultadoBL3.add(executor.submit(bl3));
//                        }
//                        case "blk" -> {
//                            AlgBLk_Clase01_Grupo10 blk =
//                                    new AlgBLk_Clase01_Grupo10(D, semilla, iteraciones, oscilacion, rangoInf, rangoSup, funcion,
//                                            funcion + ".blk" + "." + semilla, vSolucion);
//                            resultadoBLk.add(executor.submit(blk));
//                        }
//                        case "tabu" -> {
//                            AlgTabu_Clase01_Grupo10 tabu =
//                                    new AlgTabu_Clase01_Grupo10(funcion + ".tabu" + "." + semilla, semilla, D,
//                                            iteraciones, vSolucion, rangoInf, rangoSup, funcion, 1,
//                                            oscilacion);
//                            resultadoTabu.add(executor.submit(tabu));
//                        }
//
//                        case "tabuVNS" -> {
//                            AlgTabuVNS_Clase01_Grupo10 tabuVNS =
//                                    new AlgTabuVNS_Clase01_Grupo10(funcion + ".tabuVNS" + "." + semilla, semilla, D,
//                                            iteraciones, vSolucion, rangoInf, rangoSup, funcion, 1, oscilacion);
//                            resultadoTabuVNS.add(executor.submit(tabuVNS));
//                        }
//                        case "evm" -> {
//                            AlgEvMedia_Clase01_Grupo10 EvM =
//                                    new AlgEvMedia_Clase01_Grupo10(funcion + ".evm." + semilla, tp, D, iteraciones,
//                                            cromosoma, vSolucion, rangoInf, rangoSup, kProbMuta, kProCruce, funcion, semilla);
//                            resultadoEvMedia.add(executor.submit(EvM));
//                        }
//                        case "evblx" -> {
//                            AlgEvBLX_Clase01_Grupo10 EvBlk =
//                                    new AlgEvBLX_Clase01_Grupo10(funcion + ".evblx." + semilla, tp, D, iteraciones,
//                                            cromosoma, vSolucion, rangoInf, rangoSup, kProbMuta, kProCruce, alfa, funcion, semilla);
//                            resultadoEvBlX.add(executor.submit(EvBlk));
//                        }
//                        case "ed" -> {
//                            AlgEvDif_Clase01_Grupo10 ed =
//                                    new AlgEvDif_Clase01_Grupo10(funcion + ".ed." + semilla, tp, D, iteraciones,
//                                            cromosoma, vSolucion, rangoInf, rangoSup, funcion, probRecomb, semilla);
//                            resultadoEvDif.add(executor.submit(ed));
//                        }
                    }
                }
            }
        }
//        double[] vSolucion = new double[10];
//        List<double[]> cromosoma = generador(-32765, 32765, 53914154, 10, 50);
//        AlgEvDif_Clase01_Grupo10 ed =
//                new AlgEvDif_Clase01_Grupo10("asa" + ".EvDif." + 53914154, 50, 10, 10000,
//                        cromosoma, vSolucion, -32765, 32765, "ackley", 0.5, 53914154L);
//        resultadoEvDif.add(executor.submit(ed));

        List<Daido> daidos = daidos("src/main/resources/daido-tra.dat");

        executor.shutdown();
        if (!executor.awaitTermination(1, TimeUnit.MINUTES))
            executor.shutdownNow();

        // Conversion de resultados a CSV
//        exportCSV(resultadoEvMedia, "EvMedia");
//        exportCSV(resultadoEvBlX, "EvBlX");
//        exportCSV(resultadoBL3, "BL3");
//        exportCSV(resultadoBLk, "BLk");
//        exportCSV(resultadoTabu, "Tabu");
//        exportCSV(resultadoTabuVNS, "TabuVNS");
//        exportCSV(resultadoEvDif, "EvDif");

        double tiempoFinal = System.nanoTime();
        System.out.println("Tiempo total PRÁCTICA 2: " + calcularTiempo(tiempoInicial, tiempoFinal) + " ms");
    }
}
