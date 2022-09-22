package algorithms;

public class Schewefel {
    static public double evaluateS(double[] genotype) {
        double sum = 0.0;
        double d = 2;
        for (int i = 0; i < d; i++) {
            sum += (genotype[i] * Math.sin(Math.sqrt(Math.abs(genotype[i]))));
        }
        return -418.9829 * d - sum;

    }
}
