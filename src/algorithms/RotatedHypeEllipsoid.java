package algorithms;
//Pedro
public class RotatedHypeEllipsoid {
    static public double evaluateR(double[] genotype){
        double sum = 0.0;
        double d = 2;
        for (int i = 0; i < d; i++) {
            for (int j = 0; j < i; j++) {
                sum += Math.pow(2, genotype[j]);
            }
        }
        return sum;
    }
}
