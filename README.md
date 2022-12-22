# PRÁCTICA 3

## Introducción

En este proyecto realizamos la lectura de unos archivos .tsp para, ayudándonos de un algoritmo SHC, 
podamos resolver este problema con los datos previos de estos archivos.

*Implementación lectura TSP en: _'src/main/java/uja/meta/utils/LectorTSP.java'_  
*Implementación algoritmo SHC en: _'src/main/java/uja/meta/algoritmos/SCH_TSP_Clase01_Grupo10.java'_

A continuación trataremos los elementos del desarrollo más destacados del proyecto:

## Config Files

En la ruta _'src/main/resources/configFiles'_ tendremos el fichero de configuración.
Se leerá desde la clase _'Lector.java'_ y servirá para crear nuestros entornos.

## Logs

Los logs generados se encuentran en _'src/main/resources/logs'_ existiendo un directorio para cada archivo 
y dentro los logs para cada semilla. La configuración se encuentra en _'src/main/resources/log4j.properties'_.
Para generar estos appenders usamos la función createAppendersLog() en _'src/main/java/uja/meta/utils/FuncionesAuxiliares.java'_.

## Concurrencia

Debido al número de repetición de ejecuciones similares, aplicamos concurrencia para 
así conseguir un mayor rendimiento y velocidad de ejecución sin alterar los resultados finales. 
Nos ayudamos de _java.util.concurrent_.

## CSV

Para facilitar la escritura en tablas adaptadas, realizamos un proceso previo de conversión de 
los archivos a .csv, obteniendo de esta forma unos datos mucho más fáciles de leer y manejar posteriormente. 
Podemos encontrar los archivos generados en el directorio _csv_.


### Fin
_Para obtener información de ejecución y pruebas, leer el MANUAL.md en la ráiz de este documento._
