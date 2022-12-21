package uja.meta.utils;

import lombok.Getter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
public class Lector {
    private final List<String> algoritmos = new ArrayList<>();
    private int tamSem;
    private long[] semillas;
    private int ciudades;
    private long iteraciones;
    private int tHormigas;
    private int alfah;
    private int betah;
    private double q0;
    private double p;
    private double fi;
    private int tiempo;
    private List<String> ficheros = new ArrayList<>();

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
                case "algoritmos" -> {
                    String[] paramArch = separador[1].split(" ");
                    algoritmos.addAll(Arrays.asList(paramArch));
                }
                case "ficheros" -> {
                    String[] paramArch = separador[1].split(" ");
                    ficheros.addAll(Arrays.asList(paramArch));
                }
                case "ciudades" -> ciudades = Integer.parseInt(separador[1]);
                case "iteraciones" -> iteraciones = Long.parseLong(separador[1]);
                case "hormigas" -> tHormigas = Integer.parseInt(separador[1]);
                case "alfah" -> alfah = Integer.parseInt(separador[1]);
                case "betah" -> betah = Integer.parseInt(separador[1]);
                case "q0" -> q0 = Double.parseDouble(separador[1]);
                case "p" -> p = Double.parseDouble(separador[1]);
                case "fi" -> fi = Double.parseDouble(separador[1]);
                case "tiempo" -> tiempo = Integer.parseInt(separador[1]);
            }
        }
    }
}
