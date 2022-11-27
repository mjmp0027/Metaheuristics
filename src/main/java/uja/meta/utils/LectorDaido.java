package uja.meta.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LectorDaido {
    public static List<Daido> daidos(String rutaArchConfig) throws IOException {
        List<Daido> daidos = new ArrayList<>();
        FileReader f;
        f = new FileReader(rutaArchConfig);
        BufferedReader b = new BufferedReader(f);
        b.readLine(); // para skipear la primera linea
        String linea;
        while ((linea = b.readLine()) != null) {
            Daido daido = new Daido();
            String[] separador = linea.split(",");
            daido.setDni(Double.parseDouble(separador[0]));
            daido.setApe(Double.parseDouble(separador[1]));
            daido.setTemp_amb(Double.parseDouble(separador[2]));
            daido.setVel_viento(Double.parseDouble(separador[3]));
            daido.setSmr(Double.parseDouble(separador[4]));
            daido.setPotencia(Double.parseDouble(separador[5]));
            daidos.add(daido);
        }
        return daidos;
    }
}
