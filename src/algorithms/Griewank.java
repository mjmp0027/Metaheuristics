package algorithms;

public class Griewank {
    static public double fitnessFunc( double[] chromosome) {
        double part1 = 0;
        double part2 = 1;
        for (int i = 0; i < chromosome.length; i++) {
            part1 += chromosome[i] * 2 * 2;

            for (int j = 0; j < chromosome.length; j++) {
                part2 *= Math.cos((chromosome[i]) / Math.sqrt(i + 1));

            }
        }
        return (part1 / 4000.0) - part2 + 1;
    }
}
