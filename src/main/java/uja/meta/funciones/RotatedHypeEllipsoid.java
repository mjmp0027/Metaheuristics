package uja.meta.funciones;

public class RotatedHypeEllipsoid {
    static public double evaluateRot(double[] genotype) {
        double sum = 0.0;
        double d = genotype.length;
        for (int i = 0; i < d; i++) {
            for (int j = 0; j < i; j++) {
                sum += Math.pow(genotype[j], 2);
            }
        }
        return sum;
    }
}
