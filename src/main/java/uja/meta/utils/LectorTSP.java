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
        double[][] distanciasXY = new double[dimension][2];
        double[][] matriz = new double[dimension][dimension];
        int contador = 0;

        while(contador < dimension) {
            linea = b.readLine();
            String[] separador1 = linea.split(" ");
            distanciasXY[contador][0] = Double.parseDouble(separador1[1]);
            distanciasXY[contador][1] = Double.parseDouble(separador1[2]);
            contador++;
        }

        for(int i = 0; i <= dimension; i++) {
            for(int j = i + 1; j < dimension; j++) {
                matriz[i][j] = Math.sqrt(Math.pow(distanciasXY[i][0] - distanciasXY[j][0], 2) + Math.pow(distanciasXY[i][1] - distanciasXY[j][1], 2));
                matriz[j][i] = matriz[i][j];
            }
        }
        tsp.setMatriz(matriz);
        return tsp;
    }
}
