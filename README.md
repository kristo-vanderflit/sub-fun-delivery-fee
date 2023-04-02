# Sub-functionality of the food delivery application
A sub-functionality of the food delivery application, which calculates the delivery fee for food couriers based on regional base fee, vehicle type, and weather conditions.

## Technologies to use
* Java
* Spring framework
* H2 database
* OpenAPI

## Getting Started
HTTPS clone URL: https://github.com/kristo-vanderflit/sub-fun-delivery-fee.git
### Running the app locally
After cloning the project, Install Maven requirements (if not prompted automatically, hit "Reload All Maven Projects" under the right-side navbar Maven menu). 

**Run application :** src/main/java/com/fujitsu/deliveryfee/DeliveryFeeApplication.java

### Cronjob and weather url is configurable in **src/main/resources/application.properties**
* cron.expression.value =
* station.weather.url =

H2 Database: http://localhost:8080/h2

REST API documentation: http://localhost:8080/swagger-ui/index.html#/

## REST API
### City
#### Methods working with the city and calculating delivery fee
* Getting a list of cities.   
  * GET **/api/v1/cities** 
* Adding a new city.  
  * POST **/api/v1/cities** 
* Getting city by its name.   
  * GET **/api/v1/cities/{city}** 
* Calculates delivery fee by city name and vehicle type.  
  * GET **/api/v1/cities/{city}/{vehicle}** 
    * Vehicle type: car, scooter, bike
* Deleting city by ID.  
  * DELETE **/api/v1/cities/{id}** 

### Extra Fee
* Updates fee by ID and value
  * PUT **/api/v1/extrafees/{id}/{value}** 
    * {value} variable should get 3 values
     * Use commas to separate values
      * example: 2,2,2 -> OK.
* Getting list of fees 
  * GET **/api/v1/extrafees** 
* Get fee by ID
  * GET **/api/v1/extrafees/{id}** 
### Regional Base Fee
* Updates fee by ID and value
  * PUT **/api/v1/basefees/{id}/{value}** 
    * {value} variable should get 5 values
     * Use commas to separate values
      * example: 2,2,2,2,2 -> OK.
* Getting list of fees
  * GET **/api/v1/basefees** 
* Get fee by ID 
  * GET **/api/v1/basefees/{id}** 



