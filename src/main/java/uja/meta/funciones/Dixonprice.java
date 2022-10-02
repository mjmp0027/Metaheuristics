package uja.meta.funciones;

public class Dixonprice {
    public static double evaluateD(double[] genotype) {
        double score;
        double operacion1 = Math.pow(genotype[0] - 1, 2);
        double operacion2 = 0.0;
        double d = genotype.length;

        for (int i = 1; i < d; i++) {
            operacion2 += i * (Math.pow(Math.pow(2 * genotype[i], 2) - genotype[i - 1], 2));
        }

        score = operacion1 + operacion2;
        return score;
    }
}
