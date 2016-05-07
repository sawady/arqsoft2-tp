# Aplicación para Arquitectura de Software 2

## Tecnologías utilizadas

Estamos utilizando:
- Scala 2.11
- Play framework 2.4.x + Activator
- MongoDB, con Reactive Mongo para Play
- Logback como sistema de logging
- ScalaTest + Spec2 para testing

## Requisitos para levantar la aplicación

Se requiere:
- MongoDB
- Java 8+ (que *javac* sea accesible desde la consola en donde se va a utilizar el activator)

## Activator

En el root del proyecto tenemos un binario que permite compilar, testear, levantar y empaquetar la aplicación. Usa SBT por detrás.

### Pasos para probar la aplicación

1) Parado en el root del proyecto ejecutar `activator`. La primera vez demora porque se baja muchas dependencias del activator. Al terminar abre una consola.

2) Si es la primera vez conviene ejecutar `compile`, para que compile varios archivos y genere código para algunos otros. En este momento también puede bajar dependencias de nuestro proyecto.

3) Si se va a trabajar con eclipse, ejecutar `eclipse`. Eso genera los archivos de proyecto de eclipse. Luego desde eclipse importarlo como un proyecto existente.

4) En este punto puede levantarse la aplicación en como desarollo ejecutando `run`. El activator revisa cambios en el código cuando la aplicación recibe peticiones, y recompila ciertas partes en caso de ser necesario.

5) El run congela la consola del activator. Para volver a la consola presionar `CTRL+D`.

Opcionalmente pueden correrse tests con el comando `test`.

### Empaquetado de la aplicación

El activator es capaz de generar un empaquetado deployable con el comando `dist`. Eso genera un archivo ejecutable que contiene un servidor para nuestra aplicación, y guarda en distintas carpetas todos los jars, código y assets necesarios. La aplicación luego puede deployarse en cualquier servidor que posea los requisitos antes mencionados.

Luego en la carpeta `target/universal/<app-name>-<version>.zip` tenemos la aplicación zipeada.

Para correrla podemos hacer `unzip target/universal/<app-name>-<version>.zip`.

Para ejecutarla alcanza con ir hasta dicha ruta y en la carpeta `bin` ejecutar la aplicación como con cualquier binario (linux o windows).

En Heroku no estamos usando esto, porque ya trae un contenedor que permite deployar la aplicación Play directamente.

## Docker

### Contenedor MongoDB

La base de datos con mongo corre en su propio contenedor. Primero ejecutar esto antes de correr el contenedor de play.

Armarlo con los siguientes pasos:

1) `docker pull mongo`
2) `docker run --name play-mongo -d mongo`

Para parar el contenedor `docker stop play-mongo`, y para eliminarlo `docker rm play-mongo`.

### Contenedor Play

Se basa en la imagen `beevelop/java:latest`.

1) `./activator dist` (desde la ruta del proyecto).

2) `unzip ./target/universal/arqsof2-tp-*.zip`

3) `rm -rf ./target/build-dev && mv arqsof2-tp-*/ ./target/build-dev/`

4) `docker run -v /home/vm/Escritorio/arqsoft2-tp/target/build-dev:/opt/app -p 9000:9000 --name play-server --link play-mongo:mongo --rm beevelop/java:latest /opt/app/bin/arqsof2-tp -Dconfig.resource=application-docker.conf`

## Ambiente de desarrollo

Estamos utilizando el Scala IDE 4.4.0, JDK 8 y MongoDB 3.0.

Para importar el proyecto primero corremos `activator eclipse` desde la ruta del proyecto, y luego importamos el mismo desde eclipse con `import as existing proyect`.

Testeado en Linx Mint 17.3, Ubuntu 14.04, Windows 7 y Windows 10.

## Arquitectura de la aplicación:

Contamos con los siguientes tipos de entidades:
- *Controllers*: encargados de tomar los requests y devolver respuestas.
- *Repositories*: encargados de realizar queries a la base de datos.
- *Models*: contienen estructura del modelo lógico de la aplicación y lógica de negocio de esas entidades.
- *JsonModels*: definen cómo los models se transforman en JSON y viceversa.

Configuraciones:
- Las rutas de la aplicación se configuran en el archivo `conf/routes`.
- La configuración de ciertas partes de la aplicación se encuentra en `conf/application.conf`.
- La configuración de *Logback* se encuentra en `conf/logback.xml`.

Propiedades del proyecto (de SBT):
- En la carpeta `proyect` aparecen dos archivos: `build.properties` para configuraciones de sbt, y `plugins.sbt` para configurar plugins a sbt.
