package uja.meta.algoritmos;

import lombok.RequiredArgsConstructor;
import org.apache.log4j.Logger;

import static uja.meta.funciones.Ackley.evaluateA;
import static uja.meta.funciones.Dixonprice.evaluateD;
import static uja.meta.funciones.Griewank.evaluateG;
import static uja.meta.funciones.Michalewicz.evaluateM;
import static uja.meta.funciones.Perm.evaluateP;
import static uja.meta.funciones.Rastringin.evaluateR;
import static uja.meta.funciones.Rosenbrock.evaluateRosen;
import static uja.meta.funciones.RotatedHypeEllipsoid.evaluateRot;
import static uja.meta.funciones.Schewefel.evaluateS;
import static uja.meta.funciones.Trid.evaluateT;
import static uja.meta.utils.FuncionesAuxiliares.intercambiaPos;
import static uja.meta.utils.FuncionesAuxiliares.visualizaVectorLog;

@RequiredArgsConstructor
public class AlgBLk_ClaseXX_GrupoXX implements Runnable {
    private final double[] D;
    private final double[] vSolucion;
    private final String funcion;
    private final String className;
    private final long semilla;
    private double coste = Double.MAX_VALUE;

    @Override
    public void run() {
        Logger log = Logger.getLogger(className);
        double tiempoInicial = System.nanoTime();
        int N = D.length;
        double costeMejor = Double.MAX_VALUE;
        boolean mejora;
        boolean mejoraIteraciones = true; //para ver si vDLB está lleno de 1, por lo tanto no existiria mejor movimiento
        int iteraciones = 0;
        int posInter = 0;
        int[] vDLB = new int[N];

        for (int i = 0; i < N; i++) {
            vDLB[i] = 0;
        }

        while (iteraciones < 1000 && mejoraIteraciones) {
            mejoraIteraciones = false; //para cuando vDLB esté lleno de 1
            iteraciones++;

            for (int i = posInter, ite = 0; ite < N; i++, ite++) {
                if (i == N) {
                    i = 0;
                }
                if (vDLB[i] == 0) {
                    mejora = false;

                    for (int j = i + 1; j != i; j++) {
                        if (j == N) { //por si la i es igual a F.length - 1
                            j = 0;
                        }
                        switch (funcion) {
                            case "dixonprice" -> costeMejor = evaluateD(vSolucion);
                            case "ackley" -> costeMejor = evaluateA(vSolucion);
                            case "rosenbrock" -> costeMejor = evaluateRosen(vSolucion);
                            case "griewank" -> costeMejor = evaluateG(vSolucion);
                            case "rotatedHypeEllipsoid" -> costeMejor = evaluateRot(vSolucion);
                            case "perm" -> costeMejor = evaluateP(vSolucion);
                            case "trid" -> costeMejor = evaluateT(vSolucion);
                            case "schewefel" -> costeMejor = evaluateS(vSolucion);
                            case "rastringin" -> costeMejor = evaluateR(vSolucion);
                            case "michalewicz" -> costeMejor = evaluateM(vSolucion);
                        }
                        if (costeMejor < coste) {
                            log.info("INTERCAMBIO I-J: " + i + "-" + j);
                            intercambiaPos(vSolucion, i, j);
                            coste = costeMejor;
                            vDLB[i] = vDLB[j] = 0;
                            mejora = true;
                            mejoraIteraciones = true;
                            posInter = j;  //guardamos la posicion del intercambio
                            break; //cuando hay intercambio salimos del bucle doble
                        }
                        if (j == N - 1) { //para que la j sea circular
                            j = -1;
                        }
                    }
                    if (!mejora) {
                        vDLB[i] = 1;
                    } else {
                        break;
                    }
                }
            }
        }
        log.info("vector: " + visualizaVectorLog(vDLB));
        log.info("Iteraciones totales: " + iteraciones);
        log.info("Coste: " + costeMejor);
        double tiempoFinal = System.nanoTime();
        log.info("Tiempo transcurrido: " + (tiempoFinal - tiempoInicial) / 1000000 + "ms");
    }
}
