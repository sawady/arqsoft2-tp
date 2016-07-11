# Tests de Carga

Todas las pruebas fueron realizadas en una arquitectura dockerizada, utilizando un contenedor para el servidor de Play y un contenedor por cada réplica de Base de Datos. El contenedor del servidor tenía un límite de 1 GB de memoria RAM, y 1 o 2 cores de CPU dependiendo del test.

Cada prueba empezó con los siguientes pasos:

- Inserción de 2 Shops.
- Inserción de 5 Products.

Antes de empezar cada prueba, nos aseguramos de que la Base de Datos estuviera completamente vacía.

## I) Casos de prueba de stress:

#### Caso 1A: Desempeño de un nodo virtual de aplicación con 1 CPU; y 1 nodo de BD

Se realizaron 2 tipos de pruebas:

- Pedido de un Shop al azar e Inserción de un Price durante 3 minutos, con una rampa desde 10 hasta 100 usuarios concurrentes y luego 3 minutos más de pedidos constantes de 100 usuarios.

[New Relic 1](https://rpm.newrelic.com/accounts/1142474/servers/19633973?tw%5Bend%5D=1468199823&tw%5Bstart%5D=1468199183)

[New Relic 2](https://rpm.newrelic.com/accounts/1142474/servers/19633973/processes?tw%5Bend%5D=1468199823&tw%5Bstart%5D=1468199183#id=5b2250726f6365737353616d706c65732f726f6f742f6a617661222c22225d)

- Pedido de un Shop al azar e Inserción de un Price durante 3 minutos, con una rampa desde 10 hasta 150 usuarios concurrentes y luego 3 minutos más de pedidos constantes de 150 usuarios.

[New Relic](https://rpm.newrelic.com/accounts/1142474/servers/19633973/processes?tw%5Bend%5D=1468202790&tw%5Bstart%5D=1468202245#id=5b2250726f6365737353616d706c65732f726f6f742f6a617661222c22225d)

En ambas hubo un funcionamiento satisfactorio. Todos los pedidos son respondidos sin errores y en menos de 800 ms.

Se añadieron luego otras pruebas, con una rampa de 3 min hasta 100 usuarios, y luego una presión constante de otros 3 minutos, con esos mismos usuarios pidiendo precios.

Los resultados fueron los siguientes, donde medidos uso de memoria y cpu:
1) [New Relic](https://rpm.newrelic.com/accounts/1142474/servers/19633973/processes?tw%5Bend%5D=1468199823&tw%5Bstart%5D=1468199183#id=5b2250726f6365737353616d706c65732f726f6f742f6a617661222c22225d)

2) [New Relic](https://rpm.newrelic.com/accounts/1142474/servers/19633973?tw%5Bend%5D=1468199823&tw%5Bstart%5D=1468199183)

Pasamos a probar qué pasaba con 150 usuarios en lugar de 100. Los resultados fueron los siguientes:
[New Relic](https://rpm.newrelic.com/accounts/1142474/servers/19633973/processes?tw%5Bend%5D=1468202790&tw%5Bstart%5D=1468202245#id=5b2250726f6365737353616d706c65732f726f6f742f6a617661222c22225d)

#### Caso 1B: Desempeño de un nodo virtual de aplicación con 2 CPU; y 1 nodo de BD

Se realizaron 3 tipos de pruebas:

- Pedido de un Shop al azar e Inserción de un Price durante 3 minutos, con una rampa desde 10 hasta 150 usuarios concurrentes y luego 3 minutos más de pedidos constantes de 150 usuarios.

[New Relic 1](https://rpm.newrelic.com/accounts/1142474/servers/19633973?tw%5Bend%5D=1468204034&tw%5Bstart%5D=1468203190)

[New Relic 2](https://rpm.newrelic.com/accounts/1142474/servers/19633973/processes?tw%5Bend%5D=1468204034&tw%5Bstart%5D=1468203190#id=5b2250726f6365737353616d706c65732f726f6f742f6a617661222c22225d)

[New Relic 3](https://rpm.newrelic.com/accounts/1142474/applications/17294755?tw%5Bend%5D=1468204098&tw%5Bstart%5D=1468203463)

- Pedido de un Shop al azar e Inserción de un Price durante 3 minutos, con una rampa desde 10 hasta 200 usuarios concurrentes y luego 3 minutos más de pedidos constantes de 200 usuarios.

[New Relic 1](https://rpm.newrelic.com/accounts/1142474/servers/19633973?tw%5Bend%5D=1468205079&tw%5Bstart%5D=1468204349)

[New Relic 2](https://rpm.newrelic.com/accounts/1142474/servers/19633973/processes?tw%5Bend%5D=1468205079&tw%5Bstart%5D=1468204349#id=5b2250726f6365737353616d706c65732f726f6f742f6a617661222c22225d)

- Pedido de un Shop al azar e Inserción de un Price durante 3 minutos, con una rampa desde 10 hasta 200 usuarios concurrentes y luego 7 minutos más de pedidos constantes de 200 usuarios.

[New Relic 1](https://rpm.newrelic.com/accounts/1142474/servers/19633973?tw%5Bend%5D=1468206413&tw%5Bstart%5D=1468205432)

[New Relic 2](https://rpm.newrelic.com/accounts/1142474/applications/17294755?tw%5Bend%5D=1468206424&tw%5Bstart%5D=1468205490)

En todas las pruebas hubo un funcionamiento satisfactorio. Todos los pedidos son respondidos sin errores y en menos de 800 ms.

Aclaración: Aunque los tiempos de respuesta fueron buenos, notamos que fueron 4 veces más lentos que la prueba del Caso 1A, y creemos que esto se debe a que al utilizar 2 CPUs en el servidor de Play, terminan compitiendo la Base de Datos y el Servidor por una de las CPU, haciendolo un poco más lento.

Luego hicimos pruebas que incluyen consumo de cpu y memoria, y una rampa para luego hacer presión constante.

Resultado con 150 usuarios por segundo, haciendo 2 request cada uno:
[New Relic 1](https://rpm.newrelic.com/accounts/1142474/servers/19633973?tw%5Bend%5D=1468204034&tw%5Bstart%5D=1468203190)

Resultado con 200 usuarios, haciendo 2 request cada uno:
[New Relic 1](https://rpm.newrelic.com/accounts/1142474/servers/19633973?tw%5Bend%5D=1468205079&tw%5Bstart%5D=1468204349)

Probamos también aumentar la presión a 7 minutos, con 200 usuarios por segundo haciendo 2 request cada uno:
[New Relic 1](https://rpm.newrelic.com/accounts/1142474/servers/19633973?tw%5Bend%5D=1468206413&tw%5Bstart%5D=1468205432)

En esta prueba concluimos que la estabilidad del servidor con 2 nucleos es superior a la prueba que utiliza sólo un núcleo.

#### Caso 2A: Desempeño de un nodo virtual de aplicación con 1 CPU; y 2 nodos de BD en réplica

La prueba utilizó la siguiente configuración de pedidos:

- Pedido de un Shop al azar e Inserción de un Price durante 10 minutos, con una rampa desde 10 hasta 200 usuarios concurrentes.

Funcionamiento satisfactorio. La gran mayoría de los pedidos son respondidos en menos de 800 ms, solo un pequeño porcentaje tarda más de 800 ms, y no hubo errores.

#### Caso 2B: Desempeño de un nodo virtual de aplicación con 1 CPU; y 3 nodos de BD en réplica

La prueba utilizó la siguiente configuración de pedidos:

- Pedido de un Shop al azar e Inserción de un Price durante 10 minutos, con una rampa desde 10 hasta 200 usuarios concurrentes.


Funcionamiento satisfactorio. La gran mayoría de los pedidos son respondidos en menos de 800 ms, aunque un %10 de los pedidos son respondidos en más de 1200 ms. Creemos que ésto se debe a que hay más nodos para replicar la información en la Base de Datos.


## II) Casos de desempeño de Tolerancia a Fallos y Alta Disponibilidad:

La pruebas utilizaron la siguiente configuración de pedidos:

- Pedido de un Shop al azar e Inserción de un Price durante 10 minutos, con una rampa desde 10 hasta 200 usuarios concurrentes.


#### Caso 3A: Desempeño de un nodo virtual de aplicación con 1 CPU; y 3 nodos de BD en réplica, eliminando un nodo Slave durante el tope de carga

Funcionamiento satisfactorio. Todos los pedidos son respondidos sin errores y en menos de 800 ms. Pareciera ser que al eliminar un nodo de replicación hiciera el trabajo aún más rápido.

#### Caso 3B: Desempeño de un nodo virtual de aplicación con 1 CPU; y 3 nodos de BD en réplica, eliminando un nodo Master durante el tope de carga.

Funcionamiento casi satisfactorio. La gran mayoría de los pedidos son respondidos en menos de 800 ms, aunque un 29% de los pedidos son respondidos en más de 1200 ms. Además, un 1% de los pedidos resultaron en error, y esto se debe a que al momento de desconectar el nodo Master de la Base de Datos, no pudimos responder en un tiempo por debajo del Timeout (3000 ms). 

Luego de que la Base de Datos consiguió asignar un nodo Master utilizando uno de los que estaban en modo Slave (tardando alrededor de 12 segundos) notamos que el servicio se había recuperado, aunque un poco más lento, resultando en el 29% de respuestas por encima de los 1200 ms.

#### Caso 3C: Desempeño de un nodo virtual de aplicación con 1 CPU; y 3 nodos de BD en réplica, eliminando un nodo Master durante el tope de carga y luego reintegrarlo al Cluster.

El funcionamiento de esta prueba fue muy parecida a la el Caso 3B, con un par de diferencias:

Hubo un 3% de errores, y casi al final de los 10 minutos de prueba tuvimos una baja del servicio inesperada (Luego de haber conectado el nodo que desconectados asignado como Slave)

# Conclusiones

- Observamos que la cantidad de nucleos influye en el rendimiento del servidor.
- La carga por la replicación de los datos repercute en la performance.
- Si se cae el nodo de DB master, demora unos 20 seg. en votarse uno que lo reemplace. En ese lapso se encolan tantos pedidos que nuestra aplicación no se recupera satisfactoriamente de ese evento. O por lo menos no supimos cómo configurarla para que eso suceda.
- Existen configuraciones de Play que no probamos, porque notamos tarde que podían influir en los test. En particular los Future corren en un contexto de ejecución que es configurable, y pueden tunearse dependiendo del entorno en el que va a correr la aplicación.
- No pudimos explicar del todo el comportamiento de la cola de tareas del framework, pero parece que al llenarse cada vez más el rendimiento de la aplicación baja muchísimo. No obstante, aunque esté con una demanda que prácticamente no puede atender, jamás se cae, sino que sigue funcionando, sólo que puede atender menos requests de forma satisfactoria. A los que no consigue responder, les responde con un "time out" propio de los Future que usa el framework. Ese tiempo es configurable, pero no se recomienda que sea elevado, dado que no es bueno tener encoladas tareas demasiado tiempo.
- La decisión de usar Play 2.4.x creemos que fue ventajosa, dado que teníamos que esperar a que ReactiveMongo saque una versión nueva, para la nueva versión de Play. No obstante, en Play 2.5.x se utilizan features nuevos de Java, que mejoran el rendimiento del framework. Sería interesante migrar a la nueva versión y volver a realizar las pruebas, para ver si la performance realmente varía.
