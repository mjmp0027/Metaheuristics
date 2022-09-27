import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Lector {
    private final List<String> funciones = new ArrayList<>();
    private double[] genotype;
    private int D;

    public Lector(String rutaArchConfig) throws IOException {
        String linea;
        FileReader f;
        f = new FileReader(rutaArchConfig);
        BufferedReader b = new BufferedReader(f);
        while ((linea = b.readLine()) != null) {
            String[] separador = linea.split("=");
            switch (separador[0]) {
                case "D" -> D = Integer.parseInt(separador[1]);
                case "Genotype" -> {
                    String[] paramArch = separador[1].split(" ");
                    genotype = new double[D];
                    for (int i = 0; i < paramArch.length; i++) {
                        genotype[i] = Double.parseDouble(paramArch[i]);
                    }
                }
                case "Algoritmos" -> {
                    String[] paramArch = separador[1].split(" ");
                    funciones.addAll(Arrays.asList(paramArch));
                }
            }
        }
    }

    public List<String> getFunciones() {
        return funciones;
    }

    public double[] getGenotype() {
        return genotype;
    }
}
