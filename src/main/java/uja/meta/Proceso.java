package uja.meta;

import org.apache.log4j.BasicConfigurator;
import uja.meta.algoritmos.practica1.AlgBL3_Clase01_Grupo10;
import uja.meta.algoritmos.practica1.AlgBLk_Clase01_Grupo10;
import uja.meta.algoritmos.practica1.AlgTabuVNS_Clase01_Grupo10;
import uja.meta.algoritmos.practica1.AlgTabu_Clase01_Grupo10;
import uja.meta.algoritmos.practica2.AlgEDif_Clase01_Grupo10;
import uja.meta.algoritmos.practica2.AlgEvBLX_Clase01_Grupo10;
import uja.meta.algoritmos.practica2.AlgEvMedia_Clase01_Grupo10;
import uja.meta.utils.Lector;
import uja.meta.utils.Solucion;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static uja.meta.utils.FuncionesAuxiliares.*;

public class Proceso {

    public static void main(String[] args) throws Exception {
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
            double prob = lector.getProb();
            for (String algoritmo : algoritmos) {
                for (long semilla : semillas) {
                    List<double[]> cromosoma = generador(rangoInf, rangoSup, semilla, D, tp);
                    double[] vSolucion = new double[D];
                    switch (algoritmo) {
                        case "bl3" -> {
                            AlgBL3_Clase01_Grupo10 bl3 =
                                    new AlgBL3_Clase01_Grupo10(D, 3, semilla, iteraciones, oscilacion, rangoInf, rangoSup,
                                            funcion, funcion + ".bl3" + "." + semilla, vSolucion);
                            resultadoBL3.add(executor.submit(bl3));
                        }
                        case "blk" -> {
                            AlgBLk_Clase01_Grupo10 blk =
                                    new AlgBLk_Clase01_Grupo10(D, semilla, iteraciones, oscilacion, rangoInf, rangoSup, funcion,
                                            funcion + ".blk" + "." + semilla, vSolucion);
                            resultadoBLk.add(executor.submit(blk));
                        }
                        case "tabu" -> {
                            AlgTabu_Clase01_Grupo10 tabu =
                                    new AlgTabu_Clase01_Grupo10(funcion + ".tabu" + "." + semilla, semilla, D,
                                            iteraciones, vSolucion, rangoInf, rangoSup, funcion, 1,
                                            oscilacion);
                            resultadoTabu.add(executor.submit(tabu));
                        }

                        case "tabuVNS" -> {
                            AlgTabuVNS_Clase01_Grupo10 tabuVNS =
                                    new AlgTabuVNS_Clase01_Grupo10(funcion + ".tabuVNS" + "." + semilla, semilla, D,
                                            iteraciones, vSolucion, rangoInf, rangoSup, funcion, 1, oscilacion);
                            resultadoTabuVNS.add(executor.submit(tabuVNS));
                        }
                        case "evm" -> {
                            AlgEvMedia_Clase01_Grupo10 EvM =
                                    new AlgEvMedia_Clase01_Grupo10(funcion + ".evm." + semilla, tp, D, iteraciones,
                                            rangoInf, rangoSup, kProbMuta, kProCruce, funcion, semilla, prob);
                            resultadoEvMedia.add(executor.submit(EvM));
                        }
                        case "evblx" -> {
                            AlgEvBLX_Clase01_Grupo10 EvBlk =
                                    new AlgEvBLX_Clase01_Grupo10(funcion + ".evblx." + semilla, tp, D, iteraciones,
                                            rangoInf, rangoSup, kProbMuta, kProCruce, alfa, funcion, semilla, prob);
                            resultadoEvBlX.add(executor.submit(EvBlk));
                        }
                        case "ed" -> {
                            AlgEDif_Clase01_Grupo10 ed =
                                    new AlgEDif_Clase01_Grupo10(funcion + ".ed." + semilla, tp, D, iteraciones,
                                            cromosoma, vSolucion, rangoInf, rangoSup, funcion, probRecomb, semilla);
                            resultadoEvDif.add(executor.submit(ed));
                        }
                    }
                }
            }
        }

        executor.shutdown();
        if (!executor.awaitTermination(3, TimeUnit.MINUTES))
            executor.shutdownNow();

        //Conversion de resultados a CSV
        exportCSV(resultadoEvMedia, "EvMedia");
        exportCSV(resultadoEvBlX, "EvBlx");
//        exportCSV(resultadoBL3, "BL3");
//        exportCSV(resultadoBLk, "BLk");
//        exportCSV(resultadoTabu, "Tabu");
//        exportCSV(resultadoTabuVNS, "TabuVNS");
        exportCSV(resultadoEvDif, "EvDif");

        double tiempoFinal = System.nanoTime();
        System.out.println("Tiempo total PRÁCTICA 2: " + calcularTiempo(tiempoInicial, tiempoFinal) + " ms");
    }
}
