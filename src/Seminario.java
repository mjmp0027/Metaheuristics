import java.io.IOException;
import java.util.List;

public class Seminario {
    public static void main(String[] args) throws IOException {
        Lector lector = new Lector("configAckley.txt");
        List<String> funciones = lector.getAlgoritmos();
        long[] semillas = lector.getSemillas();
        int D = lector.getD();
        int k = lector.getK();
        double rangoInf = lector.getRangoInf();
        double rangoSup = lector.getRangoSup();
        funciones
                .stream()
                .map(funcion -> "funciones: " + funcion)
                .forEach(System.out::println);
        for (int i = 0; i < semillas.length; i++)
            System.out.println("semillas: " + semillas[i]);

        System.out.println("d: " + D);
        System.out.println("k: " + k);
        System.out.println("raninf: " + rangoInf);
        System.out.println("ransup: " + rangoSup);

    }
}