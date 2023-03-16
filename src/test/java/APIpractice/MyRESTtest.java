package APIpractice;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static APIpractice.MyCRUDTest.FAKER;


public class MyRESTtest {
    // usually these private variables will be provided in the config.properties file
    // this is where we will provide the base URl
    private static final String APIHOST = "https://gorest.co.in/public";

    // this is where we will provide the version
    private static final String APIV = "/v2";




    public static void main(String[] args) {



        RestAssured.baseURI = APIHOST + APIV;

       // String name = FAKER.name().fullName();
       // String email = FAKER.internet().emailAddress();

        String nameFaker = FAKER.name().fullName();
        String emailFaker = FAKER.internet().emailAddress();

        String auth = "ad94b2663bace61006c7ea27d9a274373d44bee74ce366891e362c53f43faf44";

        // creating a variable to store the Request that we will be sending
        // Request stores the headers, the body, etc.
        // this is our structure to list all the users:
        Response responseListUsers = RestAssured
                .given()
                .contentType(ContentType.JSON) // here providing what format body we need
                //.headers("Authorization", "Bearer ad94b2663bace61006c7ea27d9a274373d44bee74ce366891e362c53f43faf44")
                .headers("Authorization", "Bearer " + auth) //providing information about header
                .accept(ContentType.JSON)  // what format response body will be
                // .then() - we will not be using this in our tests. REST Assured has some Asserts in it's libraries
                // such as .validate(), but we will not be using REST Assured's assertions, we will be using the ones from JUnit
                .when()
                .get("/users"); //end point


        System.out.println(responseListUsers.asString());
        System.out.println(responseListUsers.getStatusCode());


        Response responseGetUserById = RestAssured
                .given()
                .contentType(ContentType.JSON)
                .headers("Authorization", "Bearer " + auth)  //  "Bearer"  holderposiadacz
                .accept(ContentType.JSON)
                .when()
                .pathParam("userId","1017858")
                .get("/users/{userId}");
        System.out.println(responseGetUserById.asString());
        System.out.println(responseGetUserById.getStatusCode());


        // Triple A
        // Arrange -> given
        // Act -> when
        // Assert -> then

        String body  = "{\n" +
                "    \"name\": \"" + nameFaker + "\",\n" +
                "    \"email\": \"" + emailFaker + "\",\n" +
//                "    \"name\": \"123RESTLoloPolo123\",\n" +
//                "    \"email\": \"123RESTLoloPolo123@gmail.com\",\n" +
                "    \"gender\": \"male\",\n" +
                "    \"status\": \"active\"\n" +
                "}";

        Response responseCreateUser = RestAssured
                .given()
                .contentType(ContentType.JSON)
                .headers("Authorization", "Bearer " + auth)
                .accept(ContentType.JSON)
                .body(body)
                .when()
                .post("/users");

        System.out.println(responseCreateUser.asString());
        System.out.println(responseCreateUser.getStatusCode());


        String bodyUpdate  = "{\n" +
                "    \"name\": \"1UpdatedRESTLoloPolo123\",\n" +
                "    \"email\": \"1UpdatedRESTLoloPolo123@gmail.com\",\n" +
                "    \"gender\": \"male\",\n" +
                "    \"status\": \"inactive\"\n" +
                "}";

        Response responseUpdateUserBYid = RestAssured
                .given()
                .contentType(ContentType.JSON)
                .headers("Authorization", "Bearer " + auth)
                .accept(ContentType.JSON)
                .body(bodyUpdate)
                .when()
                .pathParam("userId","1017858")
                .put("/users/{userId}");
                //.patch("/users/{userId}");
        System.out.println(responseUpdateUserBYid.asString());
        System.out.println(responseUpdateUserBYid.getStatusCode());



        Response responseDeleteUser = RestAssured
                .given()
                .contentType(ContentType.JSON)
                .headers("Authorization", "Bearer " + auth)
                .accept(ContentType.JSON)
                .when()
                .pathParam("userId","1017858")
                .delete("/users/{userId}");
        //.patch("/users/{userId}");
        System.out.println(responseDeleteUser.asString());
        System.out.println(responseDeleteUser.getStatusCode());
    }

}
