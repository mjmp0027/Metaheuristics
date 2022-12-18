package uja.meta.algoritmos.practica3;

import lombok.RequiredArgsConstructor;
import org.apache.log4j.Logger;
import uja.meta.utils.Solucion;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;

import static java.lang.Math.pow;
import static uja.meta.utils.FuncionesAuxiliares.*;

@RequiredArgsConstructor
public class Hormigas implements Callable<Solucion> {
    private final String className;
    private final double[][] dist;
    private final List<int[]> hormigas;
    private final List<boolean[]> marcados;
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

        List<double[]> feromona = new ArrayList<>();
        List<double[]> heuristica = new ArrayList<>();

        double[] costes = new double[tHormigas];

        //TODO revisar si bien inicializados

        double mejorCosteActual = Double.MAX_VALUE;
        double mejorCosteGlobal = Double.MAX_VALUE;
        int[] mejorHormigaActual = new int[ciudades];

        double fInicial = (float) 1 / (tHormigas * greedy);
        for (int i = 0; i < ciudades - 1; i++) {
            for (int j = i + 1; j < ciudades; j++) {
                if (i != j) {
                    feromona.get(j)[i] = feromona.get(i)[j] = fInicial;
                    heuristica.get(j)[i] = heuristica.get(i)[j] = 1 / dist[i][j];
                }
            }
        }
        int cont = 0;
        Timer t = null;
        int tiempo = 0;
        while (cont < iteraciones && tiempo < tiempoTotal) {
            t.start();

            char c;
            //GENERAMOS las n-1 componentes pdtes. de las hormigas
            for (int comp = 1; comp < ciudades; comp++) {
                //Para cada hormiga
                for (int h = 0; h < tHormigas; h++) {
                    //ELECCION del ELEMENTO de los no elegidos aun, a incluir en la solucion
                    double[] ferxHeu = new double[ciudades];
                    //calculo la cantidad total de feromonaxheuristica desde la ciudad actual al resto
                    //de ciudades no visitadas aun
                    for (int i = 0; i < ciudades; i++) {
                        if (!marcados.get(h)[i])
                            ferxHeu[i] = pow(heuristica.get(hormigas.get(h)[comp - 1])[i], betah)
                                    * pow(feromona.get(hormigas.get(h)[comp - 1])[i], alfah);
                    }
                    //calculo del argMax y sumatoria del total de feromonaxHeuristica
                    //(denominador)
                    double denominador = 0.0;
                    double argMax = 0.0;
                    int posArgMax = 0;
                    for (int i = 0; i < ciudades; i++) {
                        if (!marcados.get(h)[i]) {
                            denominador += ferxHeu[i];
                            if (ferxHeu[i] > argMax) {
                                argMax = ferxHeu[i];
                                posArgMax = i;
                            }
                        }
                    }

                    //FUNCION de TRANSICION
                    //List de probabilidades de transicion
                    int elegido = 0;
                    double[] prob = new double[ciudades];
                    double q = random.nextDouble(1.0);   //aleatorio inicial

                    if (q0 <= q) {  //aplicamos argumento maximo y nos quedamos con el mejor
                        elegido = posArgMax;
                    } else {  //aplicamos regla de transicion normal
                        for (int i = 0; i < ciudades; i++) {
                            if (!marcados.get(h)[i]) {
                                double numerador = ferxHeu[i];
                                prob[i] = numerador / denominador;
                            }
                        }

                        //elegimos la componente a añadir buscando en los intervalos de probabilidad
                        double Uniforme = random.nextDouble(1.0);  //aleatorio para regla de transición
                        double acumulado = 0.0;
                        for (int i = 0; i < ciudades; i++) {
                            if (!marcados.get(h)[i]) {
                                acumulado += prob[i];
                                if (Uniforme <= acumulado) {
                                    elegido = i;
                                    break;
                                }
                            }
                        }
                    }

                    hormigas.get(h)[comp] = elegido;
                    marcados.get(h)[elegido] = true;

                    //actualizacion local del arco seleccionado por la hormiga
                    feromona.get(hormigas.get(h)[comp - 1])[hormigas.get(h)[comp]] =
                            ((1 - fi) * feromona.get(hormigas.get(h)[comp - 1])[hormigas.get(h)[comp]]) + (fi * fInicial);
                    feromona.get(hormigas.get(h)[comp])[hormigas.get(h)[comp - 1]] =
                            feromona.get(hormigas.get(h)[comp - 1])[hormigas.get(h)[comp]];


                } //fin agregado una componente a cada hormiga
                //  cout << comp << endl;
            } //fin cuando las hormigas estan completas

            mejorHormiga(mejorCosteActual, tHormigas, hormigas, dist, ciudades, mejorHormigaActual);

            //ACTUALIZAMOS si la mejor actual mejora al mejor global
            if (mejorCosteActual < mejorCosteGlobal) {
                mejorCosteGlobal = mejorCosteActual;
                solucion = mejorHormigaActual;
            }
            //APLICAMOS el DEMONIO !!
            //(actualizacion de feromona (aporta la mejor Actual y solo a los arcos de dicha solucion
            double deltaMejor = 1 / mejorCosteActual;  //al ser minimizacion
            for (int i = 0; i < ciudades - 1; i++) {
                feromona.get(mejorHormigaActual[i])[mejorHormigaActual[i + 1]] += (p * deltaMejor);
                feromona.get(mejorHormigaActual[i + 1])[mejorHormigaActual[i]] =
                        feromona.get(mejorHormigaActual[i])[mejorHormigaActual[i + 1]];  //simetrica
            }

            // y se evapora en todos los arcos de la matriz de feromona (cristobal), solo se evapora en los arcos
            //de la mejor solución global (UGR)
            for (int i = 0; i < ciudades; i++) {
                for (int j = 0; j < ciudades; j++) {
                    if (i != j) {
                        feromona.get(i)[j] = ((1 - p) * feromona.get(i)[j]);
                    }
                }
            }

            hormigas.clear();
            costes = new double[tHormigas];
            for (int i = 0; i < tHormigas; i++) {
                for (int j = 0; j < ciudades; j++) {
                    marcados.get(i)[j] = false;
                }
            }
            cont++;
            if (cont % 100 == 0) {
                log.info("Iteracion: " + cont + " Coste: " + mejorCosteGlobal);
            }

            t.stop(); //FIXME aqui termina el tiempo
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
