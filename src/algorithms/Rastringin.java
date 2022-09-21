package algorithms;

public class Rastringin {
    static public double evaluateR(double[] genotype) {
        double sum = 0.0;
        double d = 2;
        for (int i = 0; i < d; i++){
            sum += Math.pow(genotype[i],2) - 10 * Math.cos(2*Math.PI*genotype[i]);
        }
        return -10 * d + sum;
    }
}
