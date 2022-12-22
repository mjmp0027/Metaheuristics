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
public class SCH_TSP_Clase01_Grupo10 implements Callable<Solucion> {
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
        return proceso(cont, log, mejorCosteGlobal, tiempoInicial);
    }

    private Solucion proceso(int cont, Logger log, double mejorCosteGlobal, double tiempoInicial) {
        Random random = new Random();

        // Inicializacion
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
        double ferInicial = (float) 1 / (ciudades * greedy);
        cargaInicial(ferInicial, ciudades, feromona, heuristica, dist);
        double tiempo = 0.0;

        while (cont < iteraciones && tiempo < tiempoTotal) {
            double tInicial = System.nanoTime();
            hormigas = generarPrimeraCiudad(semilla, ciudades, tHormigas, marcados, random);
            for (int comp = 1; comp < ciudades; comp++) {
                for (int h = 0; h < tHormigas; h++) {
                    double[] ferxHeu = calculaFerxHeu(ciudades, marcados, heuristica, feromona, hormigas, alfah, betah, h, comp);
                    double denominador = 0;
                    int posArgMax = 0;
                    for (int i = 0; i < ciudades; i++) {
                        if (!marcados.get(h).get(i)) {
                            denominador += ferxHeu[i];
                            if (ferxHeu[i] > argMax) {
                                argMax = ferxHeu[i];
                                posArgMax = i;
                            }
                        }
                    }
                    int elegido = transicion(ciudades, marcados, posArgMax, q0, ferxHeu, denominador, random, h);
                    hormigas.get(h).set(comp, elegido);
                    marcados.get(h).set(elegido, true);
                    actualizacionLocal(feromona, hormigas, h, comp, ferInicial, fi);
                }
            }

            for (int i = 0; i < tHormigas; i++) {
                Integer[] array = new Integer[hormigas.get(i).size()];
                double coste = calculaCoste(hormigas.get(i), dist, ciudades);
                if (coste < mejorCosteActual) {
                    mejorCosteActual = coste;
                    mejorHormigaActual = hormigas.get(i).toArray(array);
                }
            }

            if (mejorCosteActual < mejorCosteGlobal) {
                mejorCosteGlobal = mejorCosteActual;
                solucion = mejorHormigaActual;
            }
            actualizarFeromona(mejorCosteActual, ciudades, feromona, mejorHormigaActual, p);
            limpiar(hormigas, tHormigas, ciudades, marcados);
            cont++;
            if (cont % 100 == 0) {
                log.info("Iteracion: " + cont + " Coste: " + mejorCosteGlobal);
                log.info("Mejor hormiga: " + visualizaVectorLog(solucion));
            }
            double tiempoFinal = System.nanoTime();
            tiempo += (tiempoFinal - tInicial) / 1000000;
        }
        double tiempoFinal = System.nanoTime();
        String tiempoTotal = calcularTiempo(tiempoInicial, tiempoFinal);
        log.info("Tiempo transcurrido: " + tiempoTotal + " ms");
        String costeFormat = formato(mejorCosteGlobal);
        log.info("Mejor coste: " + mejorCosteGlobal);
        log.info("Total iteraciones: " + cont);
        return new Solucion(costeFormat, tiempoTotal, semilla);
    }
}
