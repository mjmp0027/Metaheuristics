package algoritmos;

public class Trid {
    public static double evaluateT(double[] genotype) {
        double score;
        double sum1 = 0.0;
        double sum2 = 0.0;
        double d = genotype.length;
        for (double v : genotype) {
            sum1 += Math.pow(v - 1, 2);
        }
        for (int i = 1; i < d; i++) {
            sum2 += genotype[i] * genotype[i - 1];
        }
        score = sum1 - sum2;
        return score;
    }
}
