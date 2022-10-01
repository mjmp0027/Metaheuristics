package utils;

import java.io.IOException;
import java.util.Date;
import java.util.logging.*;

public class Log {
    public static Logger createLog() {
        Logger LOGGER = Logger.getLogger(Log.class.getName());
        try {

            ConsoleHandler consoleHandler = new ConsoleHandler();
            Handler fileHandler = new FileHandler("./archivolog.log", false);
            fileHandler.setFormatter(new SimpleFormatter() {
                private static final String format = "[%1$tF %1$tT] %2$-7s %3$s %n";

                @Override
                public synchronized String format(LogRecord lr) {
                    return String.format(format,
                            new Date(lr.getMillis()),
                            lr.getSourceMethodName() + " ------",
                            lr.getMessage()
                    );
                }
            });
            consoleHandler.setLevel(Level.ALL);
            fileHandler.setLevel(Level.ALL);
            LOGGER.addHandler(fileHandler);
            System.setProperty("java.util.logging.SimpleFormatter.format",
                    "[%1$tF %1$tT] [%4$-7s] %5$s %n");
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "Error de IO");
        } catch (SecurityException ex) {
            LOGGER.log(Level.SEVERE, "Error de Seguridad");
        }
        return LOGGER;
    }
}
