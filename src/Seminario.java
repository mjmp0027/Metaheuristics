import java.io.IOException;
import java.util.List;

import static algorithms.Michalewicz.evaluateM;
import static algorithms.Trid.evaluateT;
import static algorithms.Ackley.evaluateA;
import static algorithms.Rosenbrock.evaluateRosen;
import static algorithms.Rastringin.evaluateR;
import static algorithms.Dixonprice.evaluateD;
import static algorithms.Griewank.fitnessFunc;
import static algorithms.Perm.evaluateP;
import static algorithms.Schewefel.evaluateS;
import static algorithms.RotatedHypeEllipsoid.evaluateRot;

public class Seminario {
    public static void main(String[] args) throws IOException {
        Lector lector = new Lector("config.txt");
        List<String> funciones = lector.getFunciones();
        double[] genotype = lector.getGenotype();

        for (String funcion : funciones) {
            switch (funcion) {
                case "Dixonprice" -> System.out.println("Score Dixonprice: " + evaluateD(genotype));
                case "Ackley" -> System.out.println("Score Ackley: " + evaluateA(genotype));
                case "Rosenbrock" -> System.out.println("Score Rosenbrock: " + evaluateRosen(genotype));
                case "Griewank" -> System.out.println("Score Griewank: " + fitnessFunc(genotype));
                case "Rotated" -> System.out.println("Score Rotated: " + evaluateRot(genotype));
                case "Perm" -> System.out.println("Score Perm: " + evaluateP(genotype));
                case "Trid" -> System.out.println("Score Trid: " + evaluateT(genotype));
                case "Schewefel" -> System.out.println("Score Schewefel: " + evaluateS(genotype));
                case "Rastringin" -> System.out.println("Score Rastringin: " + evaluateR(genotype));
                case "Michalewicz" -> System.out.println("Score Michalewicz: " + evaluateM(genotype));
            }
        }
    }
}