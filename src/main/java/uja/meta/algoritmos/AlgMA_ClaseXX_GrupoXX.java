package uja.meta.algoritmos;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.apache.log4j.Logger;

@AllArgsConstructor
public class AlgMA_ClaseXX_GrupoXX implements Runnable{
    private final String className;
    @Override
    public void run() {
        Logger log = Logger.getLogger(className);
        log.info("no implementado");
    }
}
