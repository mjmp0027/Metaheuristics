import org.apache.log4j.BasicConfigurator;
import uja.meta.algoritmos.AlgBL3_Clase01_Grupo10;
import uja.meta.algoritmos.AlgBLk_Clase01_Grupo10;
import uja.meta.algoritmos.AlgTabuVNS_Clase01_Grupo10;
import uja.meta.algoritmos.AlgTabu_Clase01_Grupo10;
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
        List<Future<Solucion>> resultadoBL3 = new ArrayList<>();
        List<Future<Solucion>> resultadoBLk = new ArrayList<>();
        List<Future<Solucion>> resultadoTabu = new ArrayList<>();
        List<Future<Solucion>> resultadoTabuVNS = new ArrayList<>();
        final File folder = new File(ruta);
        List<String> archivosConfig = getFiles(folder);

        for (String archivo : archivosConfig) {
            Lector lector = new Lector(ruta + archivo);
            List<String> algoritmos = lector.getAlgoritmos();
            String funcion = lector.getFuncion();
            long[] semillas = lector.getSemillas();
            int D = lector.getD();
            int k = lector.getK();
            double rangoInf = lector.getRangoInf();
            double rangoSup = lector.getRangoSup();
            long iteraciones = lector.getIteraciones();
            double oscilacion = lector.getOscilacion();
            for (String algoritmo : algoritmos) {
                for (long semilla : semillas) {
                    double[] vSolucion = generador(rangoInf, rangoSup, semilla, D);
                    switch (algoritmo) {
                        case "bl3" -> {
                            AlgBL3_Clase01_Grupo10 bl3 =
                                    new AlgBL3_Clase01_Grupo10(D, k, semilla, iteraciones, oscilacion, rangoInf, rangoSup,
                                            funcion, funcion + ".BL3" + "." + semilla, vSolucion);
                            resultadoBL3.add(executor.submit(bl3));
                        }
                        case "blk" -> {
                            AlgBLk_Clase01_Grupo10 blk =
                                    new AlgBLk_Clase01_Grupo10(D, semilla, iteraciones, oscilacion, rangoInf, rangoSup, funcion,
                                            funcion + ".BLk" + "." + semilla, vSolucion);
                            resultadoBLk.add(executor.submit(blk));
                        }
                        case "tabu" -> {
                            AlgTabu_Clase01_Grupo10 tabu =
                                    new AlgTabu_Clase01_Grupo10(funcion + ".Tabu" + "." + semilla, semilla, D,
                                            iteraciones, vSolucion, rangoInf, rangoSup, funcion, 1,
                                            oscilacion);
                            resultadoTabu.add(executor.submit(tabu));
                        }

                        case "tabuVNS" ->{
                            AlgTabuVNS_Clase01_Grupo10 tabuVNS =
                                    new AlgTabuVNS_Clase01_Grupo10(funcion + ".TabuVNS" + "." + semilla, semilla, D,
                                            iteraciones, vSolucion, rangoInf, rangoSup, funcion, 1, oscilacion);
                            resultadoTabuVNS.add(executor.submit(tabuVNS));
                        }
                    }
                }
            }
        }

        executor.shutdown();
        if (!executor.awaitTermination(1, TimeUnit.MINUTES))
            executor.shutdownNow();

        // Conversion de resultados a CSV
        // NOTA: no hace falta ordenar porque sigue siempre el orden del directorio 'configFiles'
        exportCSV(resultadoBL3, "BL3");
        exportCSV(resultadoBLk, "BLk");
        exportCSV(resultadoTabu, "Tabu");
        exportCSV(resultadoTabuVNS, "TabuVNS");

        double tiempoFinal = System.nanoTime();
        System.out.println("Tiempo total PRÁCTICA 1: " + calcularTiempo(tiempoInicial, tiempoFinal) + " ms");
    }
}
