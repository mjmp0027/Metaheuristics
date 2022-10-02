package uja.meta.funciones;

public class Michalewicz {
    public static double evaluateM(double[] genotype) {
        double score = 0.0;
        double operacion1;
        double operacion2;
        double d = genotype.length;
        int m = 10;
        for (int i = 0; i < d; i++) {
            operacion1 = Math.sin(genotype[i]);
            operacion2 = Math.sin(Math.pow(i * Math.pow(genotype[i], 2) / Math.PI, 2*m));
            score += operacion1 * operacion2;
        }

        score = (-1) * score;
        return score;
    }
}
