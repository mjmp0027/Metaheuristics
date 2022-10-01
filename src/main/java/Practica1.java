import utils.Lector;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import static utils.GeneradorSolucion.generador;
import static utils.Log.createLog;

public class Practica1 {
    public static void main(String[] args) throws IOException {
        Logger logger = createLog();

        Lector lector = new Lector("src/main/resources/configAckley.txt");
        List<String> funciones = lector.getAlgoritmos();
        long[] semillas = lector.getSemillas();
        int D = lector.getD();
        int k = lector.getK();
        double rangoInf = lector.getRangoInf();
        double rangoSup = lector.getRangoSup();


        for (long semilla : semillas) {
            double[] vector = generador(rangoInf, rangoSup, semilla, D);
            for (double v : vector) {
                logger.info(String.valueOf(v));
            }
        }

    }
}