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

    public Solucion call() {
        Logger log = Logger.getLogger(className);
        Random random = new Random();
        double tiempoInicial = System.nanoTime();

        List<boolean[]> marcados = new ArrayList<>();
        List<int[]> hormigas;

        List<double[]> feromona = new ArrayList<>();
        List<double[]> heuristica = new ArrayList<>();

        double mejorCosteActual = Double.MAX_VALUE;
        double mejorCosteGlobal = Double.MAX_VALUE;
        int[] mejorHormigaActual = new int[ciudades];
        double argMax = 0;

        double ferInicial = (float) 1 / (tHormigas * greedy);
        cargaInicial(ferInicial, ciudades, feromona, heuristica, dist);
        int cont = 0;
        Timer t = null;
        int tiempo = 0;
        while (cont < iteraciones && tiempo < tiempoTotal) {
            t.start();

            hormigas = generadorH(semilla, ciudades, tHormigas, marcados);
            //GENERAMOS las n-1 componentes pdtes. de las hormigas
            for (int comp = 1; comp < ciudades; comp++) {
                //Para cada hormiga
                for (int h = 0; h < tHormigas; h++) {
                    //ELECCION del ELEMENTO de los no elegidos aun, a incluir en la solucion
                    double[] ferxHeu = calculaFerxHeu(ciudades, marcados, heuristica, feromona, hormigas, alfah, betah, h, comp);
                    //calculo la cantidad total de feromonaxheuristica desde la ciudad actual al resto
                    //de ciudades no visitadas aun
                    //calculo del argMax y sumatoria del total de feromonaxHeuristica
                    //(denominador)
                    double denominador = 0.0;

                    int posArgMax = calculoArgMax(denominador, ciudades, argMax, marcados, ferxHeu, h);
                    int elegido = transicion(ciudades, marcados, posArgMax, q0, ferxHeu, denominador, random, h);

                    hormigas.get(h)[comp] = elegido;
                    marcados.get(h)[elegido] = true;

                    //actualizacion local del arco seleccionado por la hormiga
                    actualizacionLocal(feromona, hormigas, h, comp, ferInicial, fi);
                } //fin agregado una componente a cada hormiga
            } //fin cuando las hormigas estan completas

            mejorHormiga(mejorCosteActual, tHormigas, hormigas, dist, ciudades, mejorHormigaActual);

            //ACTUALIZAMOS si la mejor actual mejora al mejor global
            if (mejorCosteActual < mejorCosteGlobal) {
                mejorCosteGlobal = mejorCosteActual;
                solucion = mejorHormigaActual;
            }
            //APLICAMOS el DEMONIO !!
            //(actualizacion de feromona (aporta la mejor Actual y solo a los arcos de dicha solucion
            actualizarFeromona(mejorCosteActual, ciudades, feromona, mejorHormigaActual, p);

            limpiar(hormigas, tHormigas, ciudades, marcados);

            cont++;
            if (cont % 100 == 0) {
                log.info("Iteracion: " + cont + " Coste: " + mejorCosteGlobal);
            }

            t.stop(); //FIXME comprobar si está bien
            tiempo += t.getDelay();

//            registraLogDatos("SCH12.log", s, mejorCosteGlobal, cont);
            // TODO lo implementamos??? +info
        }

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
}
