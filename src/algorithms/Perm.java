package algorithms;

public class Perm {
    static public double evaluateP(double[] genotype) {
        double sum = 0.0;
        double d = genotype.length;
        for (int i = 1; i < d; i++) {
            for (int j = 1; j < d; j++) {
                sum += Math.pow((j + 10) * Math.pow(genotype[j], i) - (1 / Math.pow(j, i)), 2);
            }
        }
        return sum;
    }
}
