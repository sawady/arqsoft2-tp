# Tests de Carga

Todas las pruebas fueron realizadas en una arquitectura dokerizada, utilizando un contenedor para el servidor de Play y un contenedor por cada réplica de Base de Datos. Esto fue levantado utilizando un límite de 1 GB de memoria RAM.

Cada prueba empezó con los siguientes pasos:

- Inserción de 2 Shops.
- Inserción de 5 Products.

Antes de empezar cada prueba, nos aseguramos de que la Base de Datos estuviera completamente vacía.

## I) Casos de prueba de stress:

#### Caso 1A: Desempeño de un nodo virtual de aplicación con 1 CPU; y 1 nodo de BD

Se realizaron 2 tipos de pruebas:

- Pedido de un Shop al azar e Inserción de un Price durante 3 minutos, con una rampa desde 10 hasta 100 usuarios concurrentes y luego 3 minutos más de pedidos constantes de 100 usuarios.

- Pedido de un Shop al azar e Inserción de un Price durante 3 minutos, con una rampa desde 10 hasta 150 usuarios concurrentes y luego 3 minutos más de pedidos constantes de 150 usuarios.

En ambas hubo un funcionamiento satisfactorio. Todos los pedidos son respondidos sin errores y en menos de 800 ms.

#### Caso 1B: Desempeño de un nodo virtual de aplicación con 2 CPU; y 1 nodo de BD

Se realizaron 3 tipos de pruebas:

- Pedido de un Shop al azar e Inserción de un Price durante 3 minutos, con una rampa desde 10 hasta 150 usuarios concurrentes y luego 3 minutos más de pedidos constantes de 150 usuarios.

- Pedido de un Shop al azar e Inserción de un Price durante 3 minutos, con una rampa desde 10 hasta 200 usuarios concurrentes y luego 3 minutos más de pedidos constantes de 200 usuarios.

- Pedido de un Shop al azar e Inserción de un Price durante 3 minutos, con una rampa desde 10 hasta 200 usuarios concurrentes y luego 7 minutos más de pedidos constantes de 200 usuarios.

En todas las pruebas hubo un funcionamiento satisfactorio. Todos los pedidos son respondidos sin errores y en menos de 800 ms.

Aclaración: Aunque los tiempos de respuesta fueron buenos, notamos que fueron 4 veces más lentos que la prueba del Caso 1A, y creemos que esto se debe a que al utilizar 2 CPUs en el servidor de Play, terminan compitiendo la Base de Datos y el Servidor por una de las CPU, haciendolo un poco más lento.

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
