package uja.meta.funciones;

public class Schewefel {
    static public double evaluateS(double[] genotype) {
        double sum = 0.0;
        double d = genotype.length;
        for (double v : genotype) {
            sum += (v * Math.sin(Math.sqrt(Math.abs(v))));
        }
        return 418.9829 * d - sum;

    }
}
