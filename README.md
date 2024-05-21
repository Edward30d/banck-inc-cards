# Bank Inc Manage Cards

## Descripción

Microservicio encargado de la gestión de tarjetas de crédito y débito de los clientes de Bank Inc.

## Tecnologías Utilizadas

- Java 17
- Maven 
- Spring Boot 3
- Mysql

## Configuración del Entorno Local

1. Clona el repositorio: `git clone https://github.com/Edward30d/banck-inc-cards.git`
2. Navega al directorio del proyecto: `cd banck-inc-cards`
3. Instala las dependencias: `mvn clean install`
4. Levantar la base de datos docker `docker-compose -f docker-compose.yml up`
5. Configura las credenciales de la base en las variables de entorno correspondientes 

```shell
  export MYSQL_DATABASE=bank
  export MYSQL_ENDPOINT=localhost:3306
  export MYSQL_PASSWORD=secret
  export MYSQL_USER=root
```

6. Ejecuta el proyecto: `java  -jar target/banck-inc-cards-0.0.1-SNAPSHOT.jar`



## Despliegue en la Nube

Descripción de cómo desplegar el proyecto en la nube. Esto variará dependiendo del proveedor de servicios en la nube que estés utilizando. para desplegarlo en un cluster de kubernetes :

1. Ejecuta `cd ./resources`
2. Ejecuta el comando: `docker build -t banck-inc-cards`
3. Gestiona la imagen en tu registry de Docker preferido.
4. Ejecuta el comando: `kubectl apply -f deployment.yaml`


## Reporte de Jacoco

Para generar el reporte de Jacoco, ejecuta el siguiente comando: `mvn jacoco:report` o  `mvn clean install`
El reporte se generará en la siguiente ruta: `target/site/jacoco/index.html`

## Uso

Haga uso de las siguientes Curl para probar el servicio, o puede usar la colección de Postman que se encuentra en la carpeta `resorce` del proyecto.

1. Crear una tarjeta
```shell
curl --location 'localhost:8080/bank-inc/card' \
--header 'Content-Type: application/json' \
--data '{
    "product_id": "123456",
    "card_holder_name": "Edward Loaiza"
}'
```

2. Obtener numero de tarjeta
```shell
curl --location 'localhost:8080/bank-inc/card/123456/number'
```

3. Bloquear tarjeta
```shell
curl --location --request DELETE 'localhost:8080/bank-inc/card/1'
```
4. Recargar tarjeta
```shell
curl --location 'localhost:8080/bank-inc/card/balance' \
--header 'Content-Type: application/json' \
--data '{
    "card_id": "1234566079136284",
    "balance": 5.0
}'
```
5. Consultar saldo
```shell
curl --location 'localhost:8080/bank-inc/card/balance/1234566079136284'
```
6. Realizar compra
```shell
curl --location 'localhost:8080/bank-inc/transaction/purchase' \
--header 'Content-Type: application/json' \
--data '{
    "card_id": "1234566079136284",
    "price": 20.0
}'
```
7. Anular compra 
```shell
curl --location 'localhost:8080/bank-inc/transaction/anulation' \
--header 'Content-Type: application/json' \
--data '{
    "card_id": "1234566079136284",
    "transaction_id": 1
}'
```



## Documentación

`/resource/sagger.yaml`

