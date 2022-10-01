package funciones;

public class Rastringin {
    static public double evaluateR(double[] genotype) {
        double sum = 0.0;
        double d = genotype.length;
        for (double v : genotype) {
            sum += Math.pow(v, 2) - (10 * Math.cos(2 * Math.PI * v));
        }
        return 10 * d + sum;
    }
}
