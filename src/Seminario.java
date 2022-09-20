import static algorithms.Ackley.evaluate;

public class Seminario {
    public static void main(String[] args) {
        double[] juan = new double[2];
        juan[0] = 1.1;
        juan[1] = 1.2;
        System.out.println("score= " + evaluate(juan));
    }
}
