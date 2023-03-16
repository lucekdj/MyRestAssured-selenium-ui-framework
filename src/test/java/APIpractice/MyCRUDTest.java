package APIpractice;

import com.github.javafaker.Faker;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertAll;
public class MyCRUDTest {

    public static final Faker FAKER = new Faker();
    private static final String APIHOST = "https://gorest.co.in/public";
    private static final String APIV = "/v2";
    private static final String AUTH = "ad94b2663bace61006c7ea27d9a274373d44bee74ce366891e362c53f43faf44";

    @Test
    public void getUsersTest(){
        // setting up base URL by adding host to version of api
        RestAssured.baseURI = APIHOST + APIV;

        // Response interface is where we store our response results
        // * status code ,* body, * headers
        Response responseListUsers = RestAssured
                .given()
                .contentType(ContentType.JSON)
                //.headers("Authorization", "Bearer ad94b2663bace61006c7ea27d9a274373d44bee74ce366891e362c53f43faf44")
                .headers("Authorization", "Bearer " + AUTH)
                .accept(ContentType.JSON)
                .when()
                .get("/users");


        // TIP: we do not have to put "Assert" in the beginning, because we imported the Assert statically
        // .getBody() - gives us the entire body of the Response, not just one Object in it (in case if we are retrieving the list of all users


        // Assert your  test
        assertAll(
                () -> assertEquals(200, responseListUsers.getStatusCode(), "Status codes are not the same"), // here we are verifying whether the Status Code is correct
                () -> assertTrue(responseListUsers.getBody().asString().contains("id"), "id key is not present in the response body"),// here we are verifying if the "id" keyword is present in the body
                () -> assertTrue(responseListUsers.getBody().asString().contains("name"), "name key is not present in the response body"),
                () -> assertTrue(responseListUsers.getBody().asString().contains("email"), "email key is not present in the response body"),
                () -> assertTrue(responseListUsers.getBody().asString().contains("status"), "status key is not present in the response body"),
                () -> assertTrue(responseListUsers.getBody().asString().contains("gender"), "gender key is not present in the response body")
        );


    }
    @Test

    public void createUserTest(){

        RestAssured.baseURI = APIHOST + APIV;

        String name = FAKER.name().fullName();
        String email = FAKER.internet().emailAddress();

        //Prepare request body
        String body = "{\n" +
                "    \"name\": \"" + name + "\",\n" +
                "    \"email\": \"" + email + "\",\n" +
                "    \"gender\": \"male\",\n" +
                "    \"status\": \"inactive\"\n" +
                "}";

        //Save post response to variable
        //we will need status code of response
        //we
        Response responseCreateUser = RestAssured
                .given()
                .contentType(ContentType.JSON)
                .headers("Authorization", "Bearer " + AUTH)
                .accept(ContentType.JSON)
                //
                .body(body)
                .when()
                .post("/users");

        // Assumptions to find issues with your implementation
        // Assumptions to find issues with application under test
        // Assumptions are used to check that our steps are actually correct, we need to verify that we do not have any issues in our test steps
        // When do we use assumptions - additional check if you're having everything correct; you are ready for assertions
        // assertions are used to test assumptions made by the programmer about the program's behavior, while assumptions are
        // beliefs or expectations that guide the design and implementation of Java code.
        Assumptions.assumeTrue(responseCreateUser.getStatusCode() == 201, "Create user didn't return 201 status code");
        // Save get response in variable
        // We need to make sure 100% that our user has been created and saved to DB
        // We need body of response
        //get user by id, userid is path param where we dynamically provide id of newly created user
        Response responseGetUserById = RestAssured
                //Arrange
                .given()
                .contentType(ContentType.JSON)
                .headers("Authorization", "Bearer " + AUTH)
                .accept(ContentType.JSON)
                // Act
                .when()
                .pathParam("userId",responseCreateUser.jsonPath().getString("id"))
                .get("/users/{userId}");

        assertAll(
                // assert 201 for create user
                () -> assertEquals(201, responseCreateUser.getStatusCode(), "Status codes are not the same"),
                //jsonPath.getString() -> gets value using a key from the json response
                () -> assertEquals(name, responseCreateUser.jsonPath().getString("name"), "Names are not the same"),
                // assert that name and email are correct in post response
                () -> assertEquals(email, responseCreateUser.jsonPath().getString("email"), "Emails are not the same"),
                () -> assertEquals(200, responseGetUserById.getStatusCode(), "Status codes are not the same"),
                () -> assertEquals(name, responseGetUserById.jsonPath().getString("name"), "Names are not the same"),
                () -> assertEquals(email, responseGetUserById.jsonPath().getString("email"), "Emails are not the same"),
                () -> assertTrue(responseGetUserById.getBody().asString().contains("id"), "id key is not present in the response body"),
                () -> assertTrue(responseGetUserById.getBody().asString().contains("name"), "name key is not present in the response body"),
                () -> assertTrue(responseGetUserById.getBody().asString().contains("email"), "email key is not present in the response body"),
                () -> assertTrue(responseGetUserById.getBody().asString().contains("status"), "status key is not present in the response body"),
                () -> assertTrue(responseGetUserById.getBody().asString().contains("gender"), "gender key is not present in the response body")
        );
    }

    // How to test delete user and make it independent
    //1 create user
    //2 get user, validate he exists
    //3 delete user
    //4 get user, validate he doesn't exists
    //...........
    // In the hooks, you have before  and after
    // before test you can create user
    // hook clean data after you , delete user and the history

    @Test
    public void deleteUserTest() {
        // Setting up baseURL by adding host to version of api
        RestAssured.baseURI = APIHOST + APIV;

        // Preparing request body
        String name = FAKER.name().fullName();
        String email = FAKER.internet().emailAddress();
        String body = "{\n" +
                "    \"name\": \"" + name + "\",\n" +
                "    \"email\": \"" + email + "\",\n" +
                "    \"gender\": \"male\",\n" +
                "    \"status\": \"inactive\"\n" +
                "}";

        // Save post response to variable
        // We will need status code of response
        // We will need body of response
        Response responseCreateUser = RestAssured
                // Arrange part
                .given()
                .contentType(ContentType.JSON)
                .headers("Authorization", "Bearer " + AUTH)
                .accept(ContentType.JSON)
                // We provide the request body to the .body()
                // .body() accepts String and converts it to JSON
                .body(body)
                // Act part
                .when()
                // Use post method to create new user in /users ep
                .post("/users");

        String userId = responseCreateUser.jsonPath().getString("id");

        // Assumptions to find issues with your implementation
        // Assertions to find issues with the application under test
        Assumptions.assumeTrue(responseCreateUser.getStatusCode() == 201, "Create user didn't return 201 status code");

        // Save get response in variable
        // We need it to make sure 100% that our user has been created and saved to DB
        // We need status code
        // We need body of response
        Response responseGetUserById = RestAssured
                // Arrange
                .given()
                .contentType(ContentType.JSON)
                .headers("Authorization", "Bearer " + AUTH)
                .accept(ContentType.JSON)
                // Act
                .when()
                // responseCreateUser.jsonPath().getString("id") -> get id from post response
                .pathParam("userId", userId)
                // get user by id, userId is pathparam where we dynamically provide id of newly created user
                .get("/users/{userId}");

        Assumptions.assumeTrue(responseGetUserById.getStatusCode() == 200, "Get user didn't return 200 status code");

        // Save delete response in variable
        Response responseDeleteUser = RestAssured
                // Arrange
                .given()
                .contentType(ContentType.JSON)
                .headers("Authorization", "Bearer " + AUTH)
                .accept(ContentType.JSON)
                // Act
                .when()
                // Provide id of user you want to delete
                .pathParam("userId", userId)
                // Delete method is used
                .delete("/users/{userId}");

        Assumptions.assumeTrue(responseDeleteUser.getStatusCode() == 204, "Delete user didn't return 204 status code");

        // Save get response after delete
        Response responseGetUserByIdAfterDelete = RestAssured
                // Arrange
                .given()
                .contentType(ContentType.JSON)
                .headers("Authorization", "Bearer " + AUTH)
                .accept(ContentType.JSON)
                // Act
                .when()
                // responseCreateUser.jsonPath().getString("id") -> get id from post response
                .pathParam("userId", userId)
                // get user by id, userId is pathparam where we dynamically provide id of newly created user
                .get("/users/{userId}");

        // Assert
        assertAll(
                () -> assertEquals(204, responseDeleteUser.getStatusCode(), "Status codes are not the same"),
                () -> assertEquals(404, responseGetUserByIdAfterDelete.getStatusCode(), "Status codes are not the same"),
                () -> assertEquals("Resource not found", responseGetUserByIdAfterDelete.jsonPath().getString("message"), "Message key is not present in the response with status code 404")
        );
    }

}
