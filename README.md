# mutant-svc  
mutant-svc es una aplicación basada en Spring Boot que responde a los requerimientos hechos para una prueba técnica de MercadoLibre. Para los detalles completos acerca de los requerimientos de la solución, diríjase al archivo **Examen Mercadolibre  - Mutantes.pdf** en la raíz de este repositorio.  

## Funcionalidades de la aplicación.  
### POST /mutant
mutant-svc expone un endpoint '/mutant' que recibe peticiones POST con un arreglo de Strings que representan una matriz a ser analizada. El cuerpo de la petición luce así: 
```yaml
{
"dna":["ATGCGA","CAGTGC","TTATGT","AGAAGG","CCCCTC","TCACTG"]
}
```
 
Que representa la siguiente matriz de nucleobases:

| **A** | **T** | **G** | **C** | **G** | **A** |
| - | - | - | - | - | - |
| **C** | **A** | **G** | **T** | **G** | **C** |
| **T** | **T** | **A** | **T** | **G** | **T** |
| **A** | **G** | **A** | **A** | **G** | **G** |
| **C** | **C** | **C** | **C** | **T** | **C** |
| **T** | **C** | **A** | **C** | **T** | **G** |

Para cada matriz, la aplicación analiza si corresponde a un ADN mutante o no, devolviendo una respuesta **200 OK** si la matriz analizada cumple las condiciones definidas para ser mutante o **403 Forbidden** en caso contrario. Una matriz corresponde a un ADN mutante si se encuentra más de una secuencia de cuatro letras iguales, de forma oblicua, horizontal o vertical.  
Adicionalmente, para cada petición se validan dos restricciones de forma. Que la matriz es cuadrada (dimensión NxN) y que N no sea mayor a 1000.  

La aplicación almacena cada petición válida única en una base de datos MongoDB con la información de la matriz y la propiedad que indica si es mutante o no. La persistencia en base de datos se realiza de modo asíncrono, de manera que el tiempo de respuesta de esta operación no afecte la respuesta del endpoint.  

### GET /stats
Igualmente, mutant-svc expone un endpoint ‘/stats’ que devuelva un JSON con las estadísticas de las verificaciones de ADN de la siguiente manera:
```yaml
{
"count_mutant_dna":40, 
"count_human_dna":100: 
"ratio":0.4
}
```

## ¿Cómo usar la aplicación?  
### Entorno Local:  
* **Pre requisitos**  
	* JRE 8 o superior
		* Docker con el servicio de docker-compose (Creación de instancia local de MongoDB)
		* Apache Maven (Construcción del proyecto Spring Boot)
* **Configuración**
	* Clone el repositorio o descargue una copia del mismo
	* Desde la raíz del proyecto, levante la instancia de MongoDB y Mongo Express utilizando el siguiente comando
``` console 
docker-compose -f ./src/main/resources/environment/mongo-docker-compose.yml up
```

>MongoDB estará disponible en localhost:27017 y Mongo Express podrá accederse en la URL [http://localhost:8081](http://localhost:8081). 
* Credenciales para conectarse a mongodb/mongo-express **mutantdbuser/mutantdbpass**
* En la raíz de la carpeta del proyecto, construya la aplicación utilizando el siguiente comando:  
``` console 
mvn clean install -P local
```
* Maven generará un jar con la aplicación lista para ser ejecutada dentro de la carpeta **target** llamado ** mutant-svc-1.0.0.jar** el cual puede ejecutar utilizando el siguiente comando:
``` console
java -jar target/mutant-svc-1.0.0.jar
```
> La aplicación quedará expuesta en la URL [http://localhost:8080](http://localhost:8080).
* También es posible desplegar la aplicación dentro de un contenedor docker mediante el siguiente comando:
``` console
./mvnw spring-boot:build-image -Dspring-boot.build-image.imageName=edwinmunozrios/mutant-svc
```

### Entorno AWS:
La aplicación también ha sido desplegada en AWS utilizando las herramientas que provee la capa gratuita de la siguiente manera:
* MongoDB y Mongo Express han sido desplegados en una instancia EC2 independiente.  
* la aplicación mutant-svc ha sido desplegada en Elastic Beanstalk y sirve en el puerto 80.  
> [http://mutant-svc.us-east-2.elasticbeanstalk.com](http://mutant-svc.us-east-2.elasticbeanstalk.com)

## Pruebas de carga y recursos adicionales  
Con el fin de probar el rendimiento de la aplicación, en la carpeta '/src/main/resources/load-test' se proveen los siguientes recursos:  
* Una colección de de peticiones de PostMan que puede ser importada para probar los endpoints de la aplicación tanto en ambiente local como en AWS (Postman es requerido)
* Un Test Plan de Apache JMeter con que ejecuta pruebas sobre el endpoint de **'/stats'** en AWS y que se puede ejecutar de la siguiente manera (Apache JMeter es requerido):
``` console 
./jmeter -n -t mutant-svc-load-results.jmx -l results.csv
```
> Ejecutado desde la carpeta **bin** de JMeter sin usar GUI, donde el parámetro -t es el nombre del archivo con el Test Plan y el parámetro -l es el archivo donde se almacenan los resultados de la prueba

