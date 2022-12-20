package uja.meta.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class LectorTSP {
    public static TSP tspLector(String rutaArchConfig) throws IOException {
        FileReader f;
        f = new FileReader(rutaArchConfig);
        BufferedReader b = new BufferedReader(f);
        String linea = b.readLine();
        TSP tsp = new TSP();

        // Leemos primera parte
        String[] separador = linea.split(": ");
        tsp.setName(separador[1]);
        linea = b.readLine();
        separador = linea.split(": ");
        tsp.setComment(separador[1]);
        linea = b.readLine();
        separador = linea.split(": ");
        tsp.setType(separador[1]);
        linea = b.readLine();
        separador = linea.split(": ");
        tsp.setDimension(Integer.valueOf(separador[1]));
        int dimension = tsp.getDimension();
        linea = b.readLine();
        separador = linea.split(": ");
        tsp.setEdgeWeightType(separador[1]);
        linea = b.readLine();
        // Leemos segunda parte
        double[][] matriz = new double[dimension][dimension];
        boolean cambio = false;
        while ((linea = b.readLine()) != null && !linea.equalsIgnoreCase("EOF")) {
            for (int i = 0; i < dimension; i++) { //FIXME hacer esto bien
                for (int j = 0; j < i; j++) {
                    matriz[i][j] = !cambio ? Double.parseDouble(linea.split(" ")[1]) :
                            Double.parseDouble(linea.split(" ")[2]);
                    cambio = true;
                    matriz[j][i] = matriz[i][j];
                }
            }
        }
        tsp.setMatriz(matriz);
        return tsp;
    }
}
