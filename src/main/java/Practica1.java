import java.io.IOException;
import java.util.List;

public class Practica1 {
    public static void main(String[] args) throws IOException {
        Lector lector = new Lector("src/main/resources/configAckley.txt");
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
        for (long semilla : semillas) System.out.println("semillas: " + semilla);

        System.out.println("d: " + D);
        System.out.println("k: " + k);
        System.out.println("raninf: " + rangoInf);
        System.out.println("ransup: " + rangoSup);

    }
}