import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Lector {
    private int tamSem;
    private long[] semillas;
    private int D;
    private final List<String> algoritmos = new ArrayList<>();
    private int k;
    private String funcion;
    private double rangoInf;
    private double rangoSup;


    public Lector(String rutaArchConfig) throws IOException {
        String linea;
        FileReader f;
        f = new FileReader(rutaArchConfig);
        BufferedReader b = new BufferedReader(f);
        while ((linea = b.readLine()) != null) {
            String[] separador = linea.split("=");
            switch (separador[0]) {
                case "tamSem" -> tamSem = Integer.parseInt(separador[1]);
                case "semillas" -> {
                    String[] paramArch = separador[1].split(" ");
                    semillas = new long[tamSem];
                    for (int i = 0; i < paramArch.length; i++) {
                        semillas[i] = Long.parseLong(paramArch[i]);
                    }
                }
                case "D" -> D = Integer.parseInt(separador[1]);

                case "algoritmos" -> {
                    String[] paramArch = separador[1].split(" ");
                    algoritmos.addAll(Arrays.asList(paramArch));
                }
                case "k" -> k = Integer.parseInt(separador[1]);
                case "funcion" -> funcion = separador[1];
                case "rangoInf" -> rangoInf = Double.parseDouble(separador[1]);
                case "rangoSup" -> rangoSup = Double.parseDouble(separador[1]);
            }
        }
    }

    public List<String> getAlgoritmos() {
        return algoritmos;
    }

    public long[] getSemillas() {
        return semillas;
    }

    public int getD() {
        return D;
    }

    public int getK() {
        return k;
    }

    public String getFuncion() {
        return funcion;
    }

    public double getRangoInf() {
        return rangoInf;
    }

    public double getRangoSup() {
        return rangoSup;
    }

}
