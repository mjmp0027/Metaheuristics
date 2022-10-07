import org.apache.log4j.BasicConfigurator;
import uja.meta.algoritmos.AlgBL3_ClaseXX_GrupoXX;
import uja.meta.algoritmos.AlgBLk_ClaseXX_GrupoXX;
import uja.meta.utils.Lector;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static uja.meta.utils.FuncionesAuxiliares.generador;
import static uja.meta.utils.FuncionesAuxiliares.getFiles;

public class Practica1 {

    public static void main(String[] args) throws IOException, InterruptedException {
        BasicConfigurator.configure();
        String ruta = "src/main/resources/configFiles/";
        ExecutorService executor = Executors.newCachedThreadPool();
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
            for (String algoritmo : algoritmos) {
                for (long semilla : semillas) {
                    System.out.println(funcion + ".BL3" + "." + semilla);
                    double[] vSolucion = generador(rangoInf, rangoSup, semilla, D);
                    switch (algoritmo) {
                        case "bl3" -> {
                            AlgBL3_ClaseXX_GrupoXX bl =
                                    new AlgBL3_ClaseXX_GrupoXX(D, 1000, vSolucion, rangoInf,
                                            rangoSup, funcion, funcion + ".BL3" + "." + semilla);
                            executor.execute(bl);
                        }
                        case "blk" -> {
                            AlgBLk_ClaseXX_GrupoXX prueba =
                                    new AlgBLk_ClaseXX_GrupoXX(D, 1000, vSolucion, rangoInf, rangoSup, funcion,
                                            funcion + ".BLk" + "." + semilla);
                            executor.execute(prueba);
                        }
                        case "ma" -> {
                            /*AlgMA_ClaseXX_GrupoXX prueba =
                                    new AlgMA_ClaseXX_GrupoXX(funcion + ".MA" + "." + semilla);
                            executor.execute(prueba);*/
                        }
                    }
                }
            }
        }
        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.MINUTES);
    }
}