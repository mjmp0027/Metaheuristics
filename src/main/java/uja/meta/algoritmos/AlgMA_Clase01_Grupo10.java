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
    private int D;
    private long iteraciones;
    private double[] vSolucion;
    private double rmin;
    private double rmax;
    private String funcion;
    private int tenenciaTabu;
    private final String className;
    private final long semilla;

    void menosVisitados(long mat[][], double[] nuevaSol, double rmin, double rmax) {
        int tam = nuevaSol.length;
        double menor;
        int pc = 0;
        int columnas[] = new int[3];
        Random random = new Random();

        for (int i = 0; i < tam; i++) {
            for (int k = 0; k < 3; k++) {
                menor = 999999999;
                for (int j = 0; j < 10; j++) {
                    if (mat[i][j] <= menor) {
                        menor = mat[i][j];
                        pc = j;
                    }
                }
                columnas[k] = pc;
                mat[i][pc] = 999999999;
            }
            int aleatorio = random.nextInt(2);
            int col = columnas[aleatorio];
            double ancho = (rmax - rmin + 1) / 10;
            double ini = rmin + (col * ancho);
            double fin = ini + ancho;
            nuevaSol[i] = random.nextDouble(fin - ini) - ini;
        }
    }

    void masVisitados(long mat[][], double[] nuevaSol, double rmin, double rmax) {
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
            nuevaSol[i] = random.nextDouble(fin - ini) - ini;
        }
    }

    @Override
    public Solucion call() {
        Logger log = Logger.getLogger(className);
        log.info("Vector inicial: " + visualizaVectorLog(vSolucion));
        Random random = new Random();
        double inf, sup;
        int contador;

        visualizaVectorLog(vSolucion);

        double tiempoInicial = System.nanoTime();
        double costeActual = calculaCoste(vSolucion, funcion);
        double CosteMejorPeor, CGlobal = costeActual, CosteMejorMomento = 999999999e+100;
        int osc = 0;
        long memFrec[][] = new long[10][10];

        List<double[]> lTabu = new ArrayList<>();
        List<List<Integer>> lTabuMov = new ArrayList<>();
        List<Integer> cambiosVecino = new ArrayList<>();
        List<Integer> cambiosMejorVecino = new ArrayList<>();

        lTabu.add(vSolucion);

        double[] vecino = new double[100];
        double[] mejorVecino = new double[100];
        double[] mejorPeores = new double[100],
                SolGlobal = vSolucion,
                nuevaSol = new double[0];
        int iter;

        boolean mejora;
        double mejorCosteVecino = 999999999e+100;
        int contNoTabu;
        int multiarranque = 1;
        iter = 0;
        contador = 0;

        while (iter < iteraciones) {
            iter++;
            mejora = false;
            CosteMejorPeor = 999999999e+100;
            contNoTabu = 0;
            int x = random.nextInt(10, 4) - 4;
            for (int j = 1; j <= x; j++) {

                for (int k = 0; k < D; k++) {
                    float uniforme = random.nextFloat();
                    if (uniforme <= 0.3) {
                        cambiosVecino.set(k, 1);
                        if (multiarranque == 1) {
                            inf = vSolucion[k] * 0.9;
                            sup = vSolucion[k] * 1.1;

                            if (vSolucion[k] < 0)
                                inf = sup;
                            sup = inf;

                            if (inf < rmin)
                                inf = rmin;
                            if (sup > rmax)
                                sup = rmax;
                            vecino[k] = random.nextDouble(inf, sup);
                        } else {
                            if (multiarranque == 2) {
                                vecino[k] = random.nextDouble(rmin, rmax);
                            } else {
                                vecino[k] = vSolucion[k] * -1;
                            }
                        }
                    } else {
                        vecino[k] = vSolucion[k];
                        cambiosVecino.set(k, 0);
                    }
                }

                boolean tabu = false;

                for (int i = 0; i < lTabu.size(); i++) {
                    int cont = 0;
                    for (int p = 0; p < D; p++) {
                        double valor = lTabu.get(i)[p];
                        inf = valor * 0.99;
                        sup = valor * 1.01;
                        if (valor < 0)
                            inf = sup;
                        sup = inf;

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
                    for (int i = 0; i < lTabuMov.size(); i++) {
                        if (cambiosVecino == (lTabuMov.get(i))) {
                            break;
                        }
                    }
                }

                if (!tabu) {
                    contNoTabu++;
                    double costeVecino = calculaCoste(vecino, funcion);

                    if (costeVecino < mejorCosteVecino) {
                        mejorVecino = vecino;
                        mejorCosteVecino = costeVecino;
                        cambiosMejorVecino = cambiosVecino;

                    }
                }
            }

            if (contNoTabu != 0) {
                double ancho = (rmax - rmin - 1) / 10;
                for (int i = 0; i < D; i++) {
                    int posCol = 0;
                    for (double j = rmin; j < rmax; j += ancho) {
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

                lTabu.add(mejorVecino);
                if (lTabu.size() > tenenciaTabu)
                    lTabu.remove(0);

                lTabuMov.add(cambiosMejorVecino);
                if (lTabuMov.size() > tenenciaTabu)
                    lTabuMov.remove(0);

                if (mejorCosteVecino < costeActual) {
                    vSolucion = mejorVecino;
                    costeActual = mejorCosteVecino;
                    mejora = true;
                } else {
                    if (mejorCosteVecino < CosteMejorPeor) {
                        CosteMejorPeor = mejorCosteVecino;
                        mejorPeores = vSolucion;
                    }
                }
                if (!mejora) {

                    costeActual = CosteMejorPeor;
                    vSolucion = mejorPeores;
                    contador++;

                    multiarranque = (multiarranque + 1) % 4;
                    if (multiarranque == 0)
                        multiarranque = 1;

                } else {
                    contador = 0;
                    if (costeActual < CGlobal) {
                        CGlobal = costeActual;
                        SolGlobal = vSolucion;

                    }
                }

                if (contador == 50) {
                    if (osc == 0) {
                        if (CosteMejorMomento > costeActual) {
                            CosteMejorMomento = costeActual;
                        }
                    } else {
                        if (CosteMejorMomento > costeActual) {
                            CosteMejorMomento = costeActual;
                        }
                    }

                    contador = 0;

                    int prob = random.nextInt(100 - 1) - 1;
                    if (prob <= 50) {
                        osc = 0;
                        menosVisitados(memFrec, nuevaSol, rmin, rmax);
                    } else {
                        osc = 1;
                        masVisitados(memFrec, nuevaSol, rmin, rmax);

                    }

                    vSolucion = nuevaSol;
                    costeActual = calculaCoste(vSolucion, funcion);

                    if (costeActual < CGlobal) {
                        CGlobal = costeActual;
                        SolGlobal = vSolucion;
                    }

                    for (int i = 0; i < D; i++)
                        for (int j = 0; j < 10; j++)
                            memFrec[i][j] = 0;
                    lTabu.clear();
                }
            }
        }

        log.info("Vector solucion: " + visualizaVectorLog(vSolucion));
        String costeFormat = formato(costeActual);
        log.info("Coste: " + costeFormat);
        log.info("Iteraciones: " + iter);
        double tiempoFinal = System.nanoTime();
        String tiempoTotal = calcularTiempo(tiempoInicial, tiempoFinal);
        log.info("Tiempo transcurrido: " + tiempoTotal + " ms");
        return new Solucion(costeFormat, tiempoTotal, semilla);
    }
}
