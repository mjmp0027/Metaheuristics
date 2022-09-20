package algorithms;

public class Ackley {
    static public double evaluate(double[] genotype) {
        double score;
        double sum1 = 0.0;
        double sum2 = 0.0;
        double d = 2;
        for (int i = 0; i < d; i++) {
            sum1 += Math.pow(genotype[i], 2);
            sum2 += Math.cos(2 * Math.PI * genotype[i]);
        }
        score = -20 * Math.exp(-0.02 * Math.sqrt((1.0 / d) * sum1)) -
                Math.exp((1.0 / d) * sum2) + 20 + Math.exp(1);
        return score;
    }
}
