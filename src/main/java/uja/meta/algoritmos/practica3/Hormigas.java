package uja.meta.algoritmos.practica3;

import lombok.RequiredArgsConstructor;
import org.apache.log4j.Logger;
import uja.meta.utils.Solucion;

import java.util.Random;
import java.util.concurrent.Callable;

import static java.lang.Math.pow;
import static uja.meta.utils.FuncionesAuxiliares.*;

@RequiredArgsConstructor
public class Hormigas implements Callable<Solucion> {
    private final String className;
    private final double[][] dist;
    private final long iteraciones;
    private final long semilla;
    private final int n;
    private final int poblacion;
    private final int alfa;
    private final int beta;
    private final double q0;
    private final double p;
    private final double greedy;
    private final double fi;
    private int[] s;

    public Solucion call() {
        Logger log = Logger.getLogger(className);
        Random random = new Random();
        double tiempoInicial = System.nanoTime();

        //Declaracion de variables y estructuras de sistema
        s = new int[n];
        double[][] feromona = new double[n][n];
        double[][] heuristica = new double[n][n];
        //List<int> ciudades = new ArrayList<>();
        int[][] hormigas = new int[poblacion][n];
        double[] costes = new double[poblacion];
        boolean[][] marcados = new boolean[poblacion][n];

        //TODO revisar si bien inicializados

        double mejorCosteActual;
        double mejorCosteGlobal = Double.MAX_VALUE;
        int[] mejorHormigaActual = new int[n];

        //Inicializamos la matriz de Hormigas, Heurística y de Feromona, Listes

//        feromona.resize(n, List < double>(n));
//        heuristica.resize(n, List < double>(n));
//        hormigas.resize(poblacion, List < int>(n, 0));
//        costes.resize(poblacion);
//        marcados.resize(poblacion, List < bool > (n, false));

        //Carga inicial de feromona y de la heuristica
        double fInicial = (float) 1 / (poblacion * greedy);
        for (int i = 0; i < n - 1; i++) {
            for (int j = i + 1; j < n; j++) {
                if (i != j) {
                    feromona[j][i] = feromona[i][j] = fInicial;
                    heuristica[j][i] = heuristica[i][j] = 1 / dist[i][j];
                }
            }
        }

        //Generamos los numeros de ciudad
//    for (int i=0; i<n; i++){
//        ciudades.push_back(i);
//    }

        //Contador de iteraciones del sistema
        int cont = 0;

        //PRINCIPAL: Comienzan las iteraciones

        double tiempo = 0;
        while (cont < iteraciones) {
            // FIXME auqi controla el tiempo (while) 60000s supongo, ns si sera necesario


            //Carga de las hormigas iniciales
            for (int i = 0; i < poblacion; i++) {
                s[i] = random.nextInt(n - 1);
            }

            for (int i = 0; i < poblacion; i++) {
                for (int j = 0; j < n; j++) {
                    marcados[i][j] = true;
                }
            }
            // FIXME nose qué hace asi que inicializo marcados a true todos
            // FIXME despues de revisarlo creo que no son todos a true ... (revisar)
            char c;
            //GENERAMOS las n-1 componentes pdtes. de las hormigas
            for (int comp = 1; comp < n; comp++) {
                //Para cada hormiga
                for (int h = 0; h < poblacion; h++) {

                    //ELECCION del ELEMENTO de los no elegidos aun, a incluir en la solucion

                    double[] ferxHeu = new double[n];

                    //calculo la cantidad total de feromonaxheuristica desde la ciudad actual al resto
                    //de ciudades no visitadas aun
                    for (int i = 0; i < n; i++) {
                        if (!marcados[h][i])
                            ferxHeu[i] = pow(heuristica[hormigas[h][comp - 1]][i], beta)
                                    * pow(feromona[hormigas[h][comp - 1]][i], alfa);
                    }

                    //calculo del argMax y sumatoria del total de feromonaxHeuristica
                    //(denominador)
                    double denominador = 0.0;
                    double argMax = 0.0;
                    int posArgMax = 0;
                    for (int i = 0; i < n; i++) {
                        if (!marcados[h][i]) {
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
                    double[] prob = new double[n];
                    double q = random.nextDouble(1.0);   //aleatorio inicial

                    if (q0 <= q) {  //aplicamos argumento maximo y nos quedamos con el mejor
                        elegido = posArgMax;
                    } else {  //aplicamos regla de transicion normal
                        for (int i = 0; i < n; i++) {
                            if (!marcados[h][i]) {
                                double numerador = ferxHeu[i];
                                prob[i] = numerador / denominador;
                            }
                        }

                        //elegimos la componente a añadir buscando en los intervalos de probabilidad
                        double Uniforme = random.nextDouble(1.0);  //aleatorio para regla de transición
                        double acumulado = 0.0;
                        for (int i = 0; i < n; i++) {
                            if (!marcados[h][i]) {
                                acumulado += prob[i];
                                if (Uniforme <= acumulado) {
                                    elegido = i;
                                    break;
                                }
                            }
                        }
                        //    cout << ".";
                    }

                    hormigas[h][comp] = elegido;
                    marcados[h][elegido] = true;

                    //actualizacion local del arco seleccionado por la hormiga
                    feromona[hormigas[h][comp - 1]][hormigas[h][comp]] =
                            ((1 - fi) * feromona[hormigas[h][comp - 1]][hormigas[h][comp]]) + (fi * fInicial);
                    feromona[hormigas[h][comp]][hormigas[h][comp - 1]] =
                            feromona[hormigas[h][comp - 1]][hormigas[h][comp]];


                } //fin agregado una componente a cada hormiga
                //  cout << comp << endl;
            } //fin cuando las hormigas estan completas


            //CALCULAMOS la mejor HORMIGA
            mejorCosteActual = 999999999e+100;
            for (int i = 0; i < poblacion; i++) {
                double coste = 0.0;
                for (int j = 0; j < n - 1; j++) {
                    coste += dist[s[j]][s[j + 1]];
                }
                coste += dist[s[0]][s[n - 1]];  //cierro el ciclo

                if (coste < mejorCosteActual) {
                    mejorCosteActual = coste;
                    mejorHormigaActual = hormigas[i];
                }
            }

            //ACTUALIZAMOS si la mejor actual mejora al mejor global
            if (mejorCosteActual < mejorCosteGlobal) {
                mejorCosteGlobal = mejorCosteActual;
                s = mejorHormigaActual;
            }

            //APLICAMOS el DEMONIO !!
            //(actualizacion de feromona (aporta la mejor Actual y solo a los arcos de dicha solucion
            double deltaMejor = 1 / mejorCosteActual;  //al ser minimizacion
            for (int i = 0; i < n - 1; i++) {
                feromona[mejorHormigaActual[i]][mejorHormigaActual[i + 1]] += (p * deltaMejor);
                feromona[mejorHormigaActual[i + 1]][mejorHormigaActual[i]] =
                        feromona[mejorHormigaActual[i]][mejorHormigaActual[i + 1]];  //simetrica
            }

            // y se evapora en todos los arcos de la matriz de feromona (cristobal), solo se evapora en los arcos
            //de la mejor solución global (UGR)
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (i != j) {
                        feromona[i][j] = ((1 - p) * feromona[i][j]);
                    }
                }
            }

            //LIMPIAMOS HORMIGAS

            // FIXME esto supongo que limpiará registros ns
            hormigas = new int[poblacion][n];
            costes = new double[poblacion];
            for (int i = 0; i < poblacion; i++) {
                for (int j = 0; j < n; j++) {
                    marcados[i][j] = true;
                }
            }

            cont++;
            if (cont % 100 == 0) {
                log.info("Iteracion: " + cont + " Coste: " + mejorCosteGlobal);
            }

//            t.stop(); //FIXME aqui termina el tiempo
//            tiempo += t.getElapsedTimeInMilliSec();

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
