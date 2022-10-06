package uja.meta.algoritmos;

import lombok.AllArgsConstructor;

import org.apache.log4j.Logger;

import java.util.Random;

import static uja.meta.utils.FuncionesAuxiliares.calculaCoste;
import static uja.meta.utils.FuncionesAuxiliares.visualizaVectorLog;

@AllArgsConstructor
public class AlgBL3_ClaseXX_GrupoXX implements Runnable {
    private final int D;
    private final long iteraciones;
    private double[] vSolucion;
    private final double rangoInf;
    private final double rangoSup;
    private final String funcion;
    private final String className;

    @Override
    public void run() {
        Logger log = Logger.getLogger(className);
        double tiempoInicial = System.nanoTime();
        Random random = new Random();

        double[] vecino = new double[D];
        double[] mejorVecino;
        mejorVecino = vSolucion;
        double mejorCosteVecino;
        double mejorCoste = calculaCoste(vSolucion, funcion);

        int iter = 0;
        boolean mejora = true;

        while (mejora && iter < iteraciones) {
            mejora = false;
            mejorCosteVecino = Double.MAX_VALUE;
            for (int j = 1; j <= 3; j++) {
                for (int k = 0; k < D; k++) {    //	Para k = 1 hasta d
                    double uniforme = random.nextDouble(1.0001 - 0) + 1.0001;//Aleatorio [0,1]
                    if (uniforme <= 0.3) { //0.3 probabilidad de cambio
                        double inf, sup;
                        inf = vSolucion[k] * 0.9;
                        sup = vSolucion[k] * 1.1;
                        if (inf < rangoInf)
                            inf = rangoInf;
                        if (sup > rangoSup)
                            sup = rangoSup;

                        vecino[k] = random.nextDouble(sup - inf) + sup;
                    } else
                        vecino[k] = vSolucion[k];
                }
                double costeVecino = calculaCoste(vecino, funcion);
                if (costeVecino < mejorCosteVecino) {
                    mejorVecino = vecino;
                    mejorCosteVecino = costeVecino;
                }
            }
            if (mejorCosteVecino < mejorCoste) {
                vSolucion = mejorVecino;
                mejorCoste = mejorCosteVecino;
                mejora = true;
                iter++;
            }
            //  if (iter % kPaso == 0) {

//            cout << "Paso = " << iter << endl;
//            cout << endl << "Coste BL3: " << mejorCoste << endl;
//            cout << "Vector Solucion:" << endl;
//            for (int i = 0; i <D; i++) {
//                cout << solActual[i] << " ";
//            }
//            cout << endl;
            //  }
        }
        log.info("vector: " + visualizaVectorLog(vSolucion));
        log.info("Coste: " + mejorCoste);
        log.info("Iteraciones: " + iter);
        double tiempoFinal = System.nanoTime();
        log.info("Tiempo transcurrido: " + (tiempoFinal - tiempoInicial) / 1000000 + "ms");
    }

//    int tipo;
//    //Calculamos el coste de la Solucion inicial
//    long CosteActual=Coste (SolActual,D);
//
//        vector<int> dlb;
//        dlb.resize(D);
//        for (int i=0; i<D; i++){
//                dlb[i]=0;
//        }
//        int iter=0;
//        bool mejora=true;
//        int pos=0;      //PARA ANOTAR LA ULTIMA POSICIÓN DE INTERCAMBIO ANTERIOR
//        while (mejora && iter<iteraciones) {
//            mejora=false;
//            if (selector==0)
//                tipo=pos;     //SI NO HAY CARGA ALEATORIA ESTA OPCION DA EL MISMO RESULTADO AUN CAMBIANDO SEMILLA
//            else
//                tipo=Randint(0,D-1);   //PRIMERA UNIDAD DE INTERCAMBIO ALEATORIA
//
//            //comenzar por el principio y llegar hasta el punto de partida
//            for (int i=tipo, cont=0; cont<D && !mejora; i++, cont++){
//                if (i==D) i=0;  //para que cicle
//                if (dlb[i]==0) {
//                    bool improve_flag = false;
//
//                    for (int j=i+1, cont1=0; cont1<D && !mejora; j++, cont1++){
//                        //checkMove(i,j)
//                        if (j==D) j=0;  //para que cicleiter++;
//                        int C = FactCoste2Opt (SolActual, flu,loc, D, CosteActual, i,j);
//                        if (C<CosteActual){
//                            iter++;
//                            CosteActual=C;
//                            Intercambia(SolActual,i,j);
//                            dlb[i] = dlb[j] = 0;
//                            pos=j;    //ULTIMA UNIDAD DE INTERCAMBIO
//                            improve_flag = true;
//                            mejora=true;
//                        }
//
//                    }
//                    if (improve_flag == false) {
//                        dlb[i] = 1;
//                    }
//                }
//
//            }
//

}
