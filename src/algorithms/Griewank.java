package algorithms;

public class Griewank {
    static public double fitnessFunc(double[] genotype) {
        double part1 = 0;
        double part2 = 1;
        for (int i = 0; i < genotype.length; i++) {
            part1 += genotype[i] * 2 * 2;

            for (int j = 0; j < genotype.length; j++) {
                part2 *= Math.cos((genotype[i]) / Math.sqrt(i + 1));

            }
        }
        return (part1 / 4000.0) - part2 + 1;
    }
}
