package uja.meta.algoritmos;

import lombok.AllArgsConstructor;
import org.apache.log4j.Logger;
import uja.meta.utils.Solucion;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;

import static uja.meta.utils.FuncionesAuxiliares.*;
import static uja.meta.utils.FuncionesAuxiliares.calcularTiempo;

@AllArgsConstructor
public class AlgMA_Clase01_Grupo10 implements Callable<Solucion> {
    private final String className;
    private final long semilla;
    private int D;
    private long limiteIteraciones;
    private double[] vSolucion;
    private double rangoInf;
    private double rangoSup;
    private String funcion;
    private int tenenciaTabu;
    private double oscilacion;

    void menosVisitados(long[][] mat, double[] nuevaSol, double rmin, double rmax) {
        int tam = nuevaSol.length;
        double menor = Double.MAX_VALUE;
        int pc = 0;
        int[] columnas = new int[3];
        Random random = new Random();

        for (int i = 0; i < tam; i++) {
            for (int k = 0; k < 3; k++) {
                for (int j = 0; j < 10; j++) {
                    if (mat[i][j] <= menor) {
                        menor = mat[i][j];
                        pc = j;
                    }
                }
                columnas[k] = pc;
                mat[i][pc] = Integer.MAX_VALUE;
            }
            int aleatorio = random.nextInt(2);
            int col = columnas[aleatorio];
            double ancho = (rmax - rmin + 1) / 10;
            double ini = rmin + (col * ancho);
            double fin = ini + ancho;
            nuevaSol[i] = random.nextDouble(fin - ini) + ini;
        }
    }

    void masVisitados(long[][] mat, double[] nuevaSol, double rmin, double rmax) {
        int tam = nuevaSol.length;
        double mayor;
        int pc = 0;
        int[] columnas = new int[3];
        Random random = new Random();

        for (int i = 0; i < tam; i++) {
            for (int k = 0; k < 3; k++) {
                mayor = -1;
                for (int j = 0; j < 10; j++) {
                    if (mat[i][j] >= mayor) {
                        mayor = mat[i][j];
                        pc = j;
                    }
                }
                columnas[k] = pc;
                mat[i][pc] = -1;
            }
            int aleatorio = random.nextInt(2);
            int col = columnas[aleatorio];
            double ancho = (rmax - rmin + 1) / 10;
            double ini = rmin + (col * ancho);
            double fin = ini + ancho;
            nuevaSol[i] = random.nextDouble(fin - ini) + ini;
        }
    }

    @Override
    public Solucion call() {
        Logger log = Logger.getLogger(className);
        //log.info("Vector inicial: " + visualizaVectorLog(vSolucion));
        Random random = new Random();
        double inf, sup;
        int contador = 0;

        double[] solucionInicial = vSolucion;
        visualizaVectorLog(vSolucion);

        double tiempoInicial = System.nanoTime();
        double costeActual = calculaCoste(vSolucion, funcion);
        double costeMejorPeor, costeMejorMomento = Double.MAX_VALUE;
        double costeGlobal = costeActual;
        int osc = 0;
        long[][] memFrec = new long[10][10];



        List<double[]> listaTabu = new ArrayList<>();
        List<List<Integer>> listaTabuMov = new ArrayList<>();
        List<Integer> cambiosVecino = new ArrayList<>();
        List<Integer> cambiosMejorVecino = new ArrayList<>();
        listaTabu.add(vSolucion);

        double[] vecino = new double[100];
        double[] mejorVecino = new double[100];
        double[] mejorPeores = new double[100],
                solGlobal = vSolucion,
                nuevaSol = new double[100];
        int iteraciones = 0;

        boolean mejora;
        double vecinoMejorCoste = Double.MAX_VALUE;
        int contadorNoTabu;
        int multiarranque = 1;

        while (iteraciones < limiteIteraciones) {
            iteraciones++;
            mejora = false;
            costeMejorPeor = Double.MAX_VALUE;
            contadorNoTabu = 0;
            int x = random.nextInt(10 - 4) + 4;
            for (int j = 1; j <= x; j++) {
                for (int k = 0; k < D; k++) {
                    float uniforme = random.nextFloat();
                    if (uniforme <= oscilacion) {
                        cambiosVecino.add(k, 1);
                        if (multiarranque == 1) {
                            inf = vSolucion[k] * 0.9;
                            sup = vSolucion[k] * 1.1;

                            if (vSolucion[k] < 0) {
                                double aux;
                                aux = inf;
                                inf = sup;
                                sup = aux;
                            }

                            if (inf < rangoInf)
                                inf = rangoInf;
                            if (sup > rangoSup)
                                sup = rangoSup;
                            vecino[k] = random.nextDouble(sup - inf) + inf;
                        } else {
                            if (multiarranque == 2) {
                                vecino[k] = random.nextDouble(rangoSup - rangoInf) + rangoInf;
                            } else {
                                vecino[k] = vSolucion[k] * -1;
                            }
                        }
                    } else {
                        vecino[k] = vSolucion[k];
                        cambiosVecino.add(k, 0);
                    }
                }

                boolean tabu = false;

                for (int i = 0; i < listaTabu.size(); i++) {
                    int cont = 0;
                    for (int p = 0; p < D; p++) {
                        double valor = listaTabu.get(i)[p];
                        inf = valor * 0.99;
                        sup = valor * 1.01;
                        if (valor < 0) {
                            double aux = inf;
                            inf = sup;
                            sup = aux;
                        }

                        if (vecino[p] < inf || vecino[p] > sup) {
                            cont++;
                            break;
                        }
                    }
                    if (cont == 0) {
                        tabu = true;
                        break;
                    }
                }

                if (!tabu) {
                    for (int i = 0; i < listaTabuMov.size(); i++) {
                        if (cambiosVecino == (listaTabuMov.get(i))) {
                            tabu = true;
                            break;
                        }
                    }
                }

                if (!tabu) {
                    contadorNoTabu++;
                    double costeVecino = calculaCoste(vecino, funcion);

                    if (costeVecino < vecinoMejorCoste) {
                        mejorVecino = vecino;
                        vecinoMejorCoste = costeVecino;
                        cambiosMejorVecino = cambiosVecino;

                    }
                }
            }

            if (contadorNoTabu != 0) {
                double ancho = (rangoSup - rangoInf - 1) / 10;
                for (int i = 0; i < D; i++) {
                    int posCol = 0;
                    for (double j = rangoInf; j < rangoSup; j += ancho) {
                        if (j < 0)
                            if (Math.abs(mejorVecino[i]) >= Math.abs(j) && Math.abs(mejorVecino[i]) < Math.abs(j + ancho)) {
                                memFrec[i][posCol]++;
                                break;
                            } else if (mejorVecino[i] >= j && mejorVecino[i] < j + ancho) {
                                memFrec[i][posCol]++;
                                break;
                            }
                        posCol++;
                    }
                }

                listaTabu.add(mejorVecino);
                if (listaTabu.size() > tenenciaTabu)
                    listaTabu.remove(0);

                listaTabuMov.add(cambiosMejorVecino);
                if (listaTabuMov.size() > tenenciaTabu)
                    listaTabuMov.remove(0);

                if (vecinoMejorCoste < costeActual) {
                    vSolucion = mejorVecino;
                    costeActual = vecinoMejorCoste;
                    mejora = true;
                } else {
                    if (vecinoMejorCoste < costeMejorPeor) {
                        costeMejorPeor = vecinoMejorCoste;
                        mejorPeores = vSolucion;
                    }
                }
                if (!mejora) {

                    costeActual = costeMejorPeor;
                    vSolucion = mejorPeores;
                    contador++;

                    multiarranque = (multiarranque + 1) % 4;
                    if (multiarranque == 0)
                        multiarranque = 1;

                } else {
                    contador = 0;
                    if (costeActual < costeGlobal) {
                        costeGlobal = costeActual;
                        solGlobal = vSolucion;

                    }
                }

                if (contador == 50) {
                    if (osc == 0) {
                        if (costeMejorMomento > costeActual) {
                            costeMejorMomento = costeActual;
                        }
                    } else {
                        if (costeMejorMomento > costeActual) {
                            costeMejorMomento = costeActual;
                        }
                    }

                    contador = 0;

                    int prob = random.nextInt(100 - 1) + 1;
                    if (prob <= 50) {
                        osc = 0;
                        menosVisitados(memFrec, nuevaSol, rangoInf, rangoSup);
                    } else {
                        osc = 1;
                        masVisitados(memFrec, nuevaSol, rangoInf, rangoSup);

                    }

                    vSolucion = nuevaSol;
                    costeActual = calculaCoste(vSolucion, funcion);

                    if (costeActual < costeGlobal) {
                        costeGlobal = costeActual;
                        solGlobal = vSolucion;
                    }

                    for (int i = 0; i < D; i++)
                        for (int j = 0; j < 10; j++)
                            memFrec[i][j] = 0;
                    listaTabu.clear();
                }
            }
        }
        double tiempoFinal = System.nanoTime();
        String tiempoTotal = calcularTiempo(tiempoInicial, tiempoFinal);
        log.info("Tiempo transcurrido: " + tiempoTotal + " ms");
        log.info("CGGlobal: " + formato(costeGlobal));
        log.info("Vector solucion: " + visualizaVectorLog(vSolucion));
        String costeFormat = formato(costeActual);
        log.info("Coste: " + costeFormat);
        log.info("Solución global: " + visualizaVectorLog(solGlobal));
        log.info("Iteraciones: " + iteraciones);
        double MAPE = MAPE(solucionInicial, vSolucion);
        double RMSE = RMSE(solucionInicial, vSolucion);
        log.info("MAPE :  " + MAPE);
        log.info("RMSE: " + RMSE);
        return new Solucion(costeFormat, tiempoTotal, semilla);
    }
}
