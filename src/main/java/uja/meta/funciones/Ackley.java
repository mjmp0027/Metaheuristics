package uja.meta.funciones;

public class Ackley {

    public static double evaluateA(double[] vector) {
        double score;
        double sum1 = 0.0;
        double sum2 = 0.0;
        double d = vector.length;
        for (double v : vector) {
            sum1 += Math.pow(v, 2);
            sum2 += Math.cos(2 * Math.PI * v);
        }
        score = -20 * Math.exp(-0.02 * Math.sqrt((1.0 / d) * sum1)) -
                Math.exp((1.0 / d) * sum2) + 20 + Math.exp(1);
        return score;
    }
}
