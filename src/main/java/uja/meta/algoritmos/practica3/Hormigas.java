package uja.meta.algoritmos.practica3;

import lombok.RequiredArgsConstructor;
import org.apache.log4j.Logger;
import uja.meta.utils.Solucion;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;

import static uja.meta.utils.FuncionesAuxiliares.*;

@RequiredArgsConstructor
public class Hormigas implements Callable<Solucion> {
    private final String className;
    private final double[][] dist;
    private final long iteraciones;
    private final long semilla;
    private final int ciudades;
    private final int tHormigas;
    private final int alfah;
    private final int betah;
    private final double q0;
    private final double p;
    private final double fi;
    private final double greedy;
    private final double tiempoTotal;
    private int[] solucion;
    private Random random = new Random();
    private double tiempoInicial = System.nanoTime();
    private List<boolean[]> marcados = new ArrayList<>();
    private List<int[]> hormigas;
    private List<double[]> feromona = new ArrayList<>();
    private List<double[]> heuristica = new ArrayList<>();
    private double mejorCosteActual = Double.MAX_VALUE;
    private double mejorCosteGlobal = Double.MAX_VALUE;
    private double argMax = 0;

    public Solucion call() {
        Logger log = Logger.getLogger(className);
        int cont = 0;
        proceso(cont, log);
        double tiempoFinal = System.nanoTime();
        String tiempoTotal = calcularTiempo(tiempoInicial, tiempoFinal);
        log.info("Tiempo transcurrido: " + tiempoTotal + " ms");
        //        log.info("Mejor cromosoma: " + visualizaVectorLog());
        String costeFormat = formato(mejorCosteGlobal);
        log.info("Mejor coste: " + costeFormat);
//        log.info("Total evaluaciones: " + contEv);
        log.info("Total iteraciones: " + cont);
        return new Solucion(costeFormat, tiempoTotal, semilla);
    }

    private void proceso(int cont, Logger log) {
        int[] mejorHormigaActual = new int[ciudades];
        double ferInicial = (float) 1 / (tHormigas * greedy);
        cargaInicial(ferInicial, ciudades, feromona, heuristica, dist);
        Timer t = null;
        int tiempo = 0;
        while (cont < iteraciones && tiempo < tiempoTotal) {
            t.start();
            hormigas = generadorH(semilla, ciudades, tHormigas, marcados);
            for (int comp = 1; comp < ciudades; comp++) {
                for (int h = 0; h < tHormigas; h++) {
                    double[] ferxHeu =
                            calculaFerxHeu(ciudades, marcados, heuristica, feromona, hormigas, alfah, betah, h, comp);
                    double denominador = 0.0;
                    int posArgMax = calculoArgMax(denominador, ciudades, argMax, marcados, ferxHeu, h);
                    int elegido = transicion(ciudades, marcados, posArgMax, q0, ferxHeu, denominador, random, h);
                    hormigas.get(h)[comp] = elegido;
                    marcados.get(h)[elegido] = true;
                    actualizacionLocal(feromona, hormigas, h, comp, ferInicial, fi);
                }
            }
            mejorHormiga(mejorCosteActual, tHormigas, hormigas, dist, ciudades, mejorHormigaActual);
            if (mejorCosteActual < mejorCosteGlobal) {
                mejorCosteGlobal = mejorCosteActual;
                solucion = mejorHormigaActual;
            }
            actualizarFeromona(mejorCosteActual, ciudades, feromona, mejorHormigaActual, p);
            limpiar(hormigas, tHormigas, ciudades, marcados);
            cont++;
            if (cont % 100 == 0) {
                log.info("Iteracion: " + cont + " Coste: " + mejorCosteGlobal);
            }
            t.stop();
            tiempo += t.getDelay();
//            registraLogDatos("SCH12.log", s, mejorCosteGlobal, cont);
        }
    }
}
