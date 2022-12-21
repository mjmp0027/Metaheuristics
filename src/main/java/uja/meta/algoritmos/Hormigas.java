package uja.meta.algoritmos;

import lombok.RequiredArgsConstructor;
import org.apache.log4j.Logger;
import uja.meta.utils.Solucion;

import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

import static java.util.stream.IntStream.range;
import static uja.meta.utils.FuncionesAuxiliares.*;

@RequiredArgsConstructor
public class Hormigas implements Callable<Solucion> { //FIXME cambiar nombre
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
    private Integer[] solucion;

    public Solucion call() {
        Logger log = Logger.getLogger(className);
        int cont = 0;
        double mejorCosteGlobal = Double.MAX_VALUE;
        double tiempoInicial = System.nanoTime();
        proceso(cont, log, mejorCosteGlobal);
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

    private void proceso(int cont, Logger log, double mejorCosteGlobal) {
        Random random = new Random();

        // Inicializacion //FIXME revisar
        List<List<Double>> feromona = range(0, ciudades)
                .mapToObj(i -> range(0, ciudades).mapToObj(j -> 0.0)
                        .collect(Collectors.toList())).collect(Collectors.toList());
        List<List<Double>> heuristica = range(0, ciudades)
                .mapToObj(i -> range(0, ciudades).mapToObj(j -> 0.0)
                        .collect(Collectors.toList())).collect(Collectors.toList());
        List<List<Boolean>> marcados = range(0, tHormigas).mapToObj(i -> range(0, ciudades)
                .mapToObj(j -> false).collect(Collectors.toList())).collect(Collectors.toList());
        List<List<Integer>> hormigas;

        double mejorCosteActual = Double.MAX_VALUE;
        double argMax = 0;
        Integer[] mejorHormigaActual = new Integer[ciudades];
        double ferInicial = (float) 1 / (tHormigas * greedy);
        cargaInicial(ferInicial, ciudades, feromona, heuristica, dist);
        double tiempo = 0.0;
        while (cont < iteraciones && tiempo < tiempoTotal) {
            double tiempoInicial = System.nanoTime();
            hormigas = generadorH(semilla, ciudades, tHormigas, marcados);
            for (int comp = 1; comp < ciudades; comp++) {
                for (int h = 0; h < tHormigas; h++) {
                    double[] ferxHeu = //FIXME aqui peta, fallará en alguno más seguro...
                            calculaFerxHeu(ciudades, marcados, heuristica, feromona, hormigas, alfah, betah, h, comp);
                    log.info("peta3");
                    double denominador = 0.0;
                    int posArgMax = calculoArgMax(denominador, ciudades, argMax, marcados, ferxHeu, h);
                    int elegido = transicion(ciudades, marcados, posArgMax, q0, ferxHeu, denominador, random, h);
                    hormigas.get(h).set(comp, elegido);
                    marcados.get(h).set(elegido, true);
                    actualizacionLocal(feromona, hormigas, h, comp, ferInicial, fi);
                    log.info("peta4");
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
            double tiempoFinal = System.nanoTime();
            tiempo += (tiempoFinal - tiempoInicial) / 1000000;
//            registraLogDatos("SCH12.log", s, mejorCosteGlobal, cont);
        }
    }
}
