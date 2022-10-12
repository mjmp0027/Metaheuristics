import org.apache.log4j.BasicConfigurator;
import uja.meta.algoritmos.AlgBL3_ClaseXX_GrupoXX;
import uja.meta.algoritmos.AlgBLk_ClaseXX_GrupoXX;
import uja.meta.algoritmos.AlgMA_ClaseXX_GrupoXX;
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
            double probabilidad = lector.getProbabilidad();
            for (String algoritmo : algoritmos) {
                for (long semilla : semillas) {
                    double[] vSolucion = generador(rangoInf, rangoSup, semilla, D);
                    switch (algoritmo) {
                        case "bl3" -> {
                            AlgBL3_ClaseXX_GrupoXX bl =
                                    new AlgBL3_ClaseXX_GrupoXX(D, k, semilla, iteraciones, probabilidad, rangoInf, rangoSup,
                                            funcion, funcion + ".BL3" + "." + semilla, vSolucion);
                            resultadoBL3.add(executor.submit(bl));
                        }
                        case "blk" -> {
                            AlgBLk_ClaseXX_GrupoXX prueba =
                                    new AlgBLk_ClaseXX_GrupoXX(D, semilla, iteraciones, probabilidad, rangoInf, rangoSup, funcion,
                                            funcion + ".BLk" + "." + semilla, vSolucion);
                            resultadoBLk.add(executor.submit(prueba));
                        }
                        case "ma" -> { // TODO
                            AlgMA_ClaseXX_GrupoXX prueba =
                                    new AlgMA_ClaseXX_GrupoXX(funcion + ".MA" + "." + semilla);
                            executor.execute(prueba);
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

        double tiempoFinal = System.nanoTime();
        System.out.println("Tiempo total PRÁCTICA 1: " + calcularTiempo(tiempoInicial, tiempoFinal) + " ms");
    }
}