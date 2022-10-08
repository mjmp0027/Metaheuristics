package uja.meta.utils;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class Solucion {
    String coste, tiempo;
    long semilla;

    @Override
    public String toString() {
        return coste + ";" + tiempo + ";";
    }
}
