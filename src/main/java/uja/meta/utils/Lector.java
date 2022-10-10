package uja.meta.utils;

import lombok.Getter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
public class Lector { // TODO agregar: oscilacion, iteraciones,
    private final List<String> algoritmos = new ArrayList<>();
    private int tamSem;
    private long[] semillas;
    private int D;
    private int k;
    private String funcion;
    private double rangoInf;
    private double rangoSup;
    private long iteraciones;

    public Lector(String rutaArchConfig) throws IOException {
        String linea;
        FileReader f;
        f = new FileReader(rutaArchConfig);
        BufferedReader b = new BufferedReader(f);
        while ((linea = b.readLine()) != null) {
            String[] separador = linea.split("=");
            switch (separador[0]) {
                case "tamSem" -> tamSem = Integer.parseInt(separador[1]);
                case "semillas" -> {
                    String[] paramArch = separador[1].split(" ");
                    semillas = new long[tamSem];
                    for (int i = 0; i < paramArch.length; i++) {
                        semillas[i] = Long.parseLong(paramArch[i]);
                    }
                }
                case "D" -> D = Integer.parseInt(separador[1]);
                case "iteraciones" -> iteraciones = Long.parseLong(separador[1]);
                case "algoritmos" -> {
                    String[] paramArch = separador[1].split(" ");
                    algoritmos.addAll(Arrays.asList(paramArch));
                }
                case "k" -> k = Integer.parseInt(separador[1]);
                case "funcion" -> funcion = separador[1];
                case "rangoInf" -> rangoInf = Double.parseDouble(separador[1]);
                case "rangoSup" -> {
                    if (separador[1].equals("2PI")) {
                        rangoSup = 2 * Math.PI;
                    } else {
                        rangoSup = Double.parseDouble(separador[1]);
                    }
                }
            }
        }
    }
}
