package algorithms;

public class Perm {
    static public double evaluateP(double[] genotype) {
        double sum = 0.0;
        double d = 2;
        for (int i = 1; i < d; i++) {
            for (int j = 1; j < d; j++) {
                sum += Math.pow(2, ((j + 10) * (genotype[j] ^ i - (1 / j ^ i))));
            }
        }
        return sum;
    }
}
