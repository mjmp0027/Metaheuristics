package algoritmos;

public class Rosenbrock {
    public static double evaluateRosen(double[] genotype) {
        double score = 0.0;
        double sum1;
        double sum2;
        double d = genotype.length;
        for (int i = 0; i < d - 1; i++) {
            sum1 = 100 * Math.pow(genotype[i + 1] - Math.pow(genotype[i], 2), 2);
            sum2 = Math.pow(genotype[i] - 1, 2);
            score += (sum1 + sum2);
        }
        return score;
    }
}
