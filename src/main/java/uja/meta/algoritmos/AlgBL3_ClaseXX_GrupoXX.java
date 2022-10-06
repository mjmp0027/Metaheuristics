package uja.meta.algoritmos;

import lombok.RequiredArgsConstructor;
import org.apache.log4j.Logger;

import java.util.Arrays;
import java.util.Random;
import java.util.ArrayList;

@RequiredArgsConstructor
public class AlgBL3_ClaseXX_GrupoXX implements Runnable {
    public double SoluBLocal3(int tam, long evaluaciones, Double[] solActual, double rMin, double rMax,
                       int selector) {


        for (int i = 0; i < tam; i++) {
            solActual[i] = Math.random() * (rMin - rMax) + rMax;
        }
        for (int i = 0; i < solActual.length; i++) {
            System.out.println(", " + solActual[i]);
        }

        Double[] vecino = new Double[tam];
        Double[] mejorVecino;
        mejorVecino = solActual;
        double mejorCosteVecino;
        double mejorCoste = CalculaCoste(solActual, selector);
        int iter = 0;
        boolean mejora = true;

        while (mejora && iter < evaluaciones) {
            mejora = false;
            mejorCosteVecino = 999999999;
            for (int j = 1; j <= 3; j++) {
                for (int k = 0; k < tam; k++) {    //	Para k = 1 hasta d
                    double uniforme = Math.random() * (0 - 1.0001) + 1.0001;//Aleatorio [0,1]
                    if (uniforme <= 0.3) { //0.3 probabilidad de cambio
                        double inf, sup;
                        inf = solActual[k] * 0.9;
                        sup = solActual[k] * 1.1;
                        if (inf < rMin)
                            inf = rMin;
                        if (sup > rMax)
                            sup = rMax;

                        vecino[k] = Math.random() * (inf - sup) + sup;
                    } else
                        vecino[k] = solActual[k];
                }
                double costeVecino = CalculaCoste(vecino, selector);  //GriewankEvaluate(vecino);
                if (costeVecino < mejorCosteVecino) {
                    mejorVecino = vecino;
                    mejorCosteVecino = costeVecino;
                }
            }
            if (mejorCosteVecino < mejorCoste) {
                solActual = mejorVecino;
                mejorCoste = mejorCosteVecino;
                mejora = true;
                iter++;
            }
            //  if (iter % kPaso == 0) {

//            cout << "Paso = " << iter << endl;
//            cout << endl << "Coste BL3: " << mejorCoste << endl;
//            cout << "Vector Solucion:" << endl;
//            for (int i = 0; i <tam; i++) {
//                cout << solActual[i] << " ";
//            }
//            cout << endl;
            //  }
        }

        System.out.println("Iteraciones: " + iter);
        return mejorCoste;
    }

//    int tipo;
//    //Calculamos el coste de la Solucion inicial
//    long CosteActual=Coste (SolActual,tam);
//
//        vector<int> dlb;
//        dlb.resize(tam);
//        for (int i=0; i<tam; i++){
//                dlb[i]=0;
//        }
//        int iter=0;
//        bool mejora=true;
//        int pos=0;      //PARA ANOTAR LA ULTIMA POSICIÓN DE INTERCAMBIO ANTERIOR
//        while (mejora && iter<evaluaciones) {
//            mejora=false;
//            if (selector==0)
//                tipo=pos;     //SI NO HAY CARGA ALEATORIA ESTA OPCION DA EL MISMO RESULTADO AUN CAMBIANDO SEMILLA
//            else
//                tipo=Randint(0,tam-1);   //PRIMERA UNIDAD DE INTERCAMBIO ALEATORIA
//
//            //comenzar por el principio y llegar hasta el punto de partida
//            for (int i=tipo, cont=0; cont<tam && !mejora; i++, cont++){
//                if (i==tam) i=0;  //para que cicle
//                if (dlb[i]==0) {
//                    bool improve_flag = false;
//
//                    for (int j=i+1, cont1=0; cont1<tam && !mejora; j++, cont1++){
//                        //checkMove(i,j)
//                        if (j==tam) j=0;  //para que cicleiter++;
//                        int C = FactCoste2Opt (SolActual, flu,loc, tam, CosteActual, i,j);
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
