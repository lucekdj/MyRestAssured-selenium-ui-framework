package APIpractice;

import com.github.javafaker.Faker;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class HwCRUDPost extends RESTBase {

    @Test
    public void getListPosts(){

        Response responseListPosts = HwRESTPost.getListPosts(AUTH);

        System.out.println(responseListPosts.asString());
        System.out.println(responseListPosts.getStatusCode());


        assertAll(
                () -> assertEquals(200, responseListPosts.getStatusCode(), "Status codes are not the same"), // here we are verifying whether the Status Code is correct
                () -> assertTrue(responseListPosts.getBody().asString().contains("id"), "id key is not present in the response body"),// here we are verifying if the "id" keyword is present in the body
                () -> assertTrue(responseListPosts.getBody().asString().contains("name"), "name key is not present in the response body"),
                () -> assertTrue(responseListPosts.getBody().asString().contains("title"), "title key is not present in the response body"),
                () -> assertTrue(responseListPosts.getBody().asString().contains("body"), "body key is not present in the response body")

        );
    }


    /*
  -Create post test
  -Create a new user
  -Get the newly created user
  -create a post for that new user
  -Retrieve the newly created post by post id

  -Assert that status code is 200
  -Assert that Response body has id, user_id , title and body fields present
   */
    @Test
    public void createPost(){

        //Create a new user

        String name = FAKER.name().fullName();
        String email = FAKER.internet().emailAddress();

        String body = "{\n" +
                "    \"name\": \"" + name + "\",\n" +
                "    \"email\": \"" + email + "\",\n" +
                "    \"gender\": \"male\",\n" +
                "    \"status\": \"inactive\"\n" +
                "}";

        Response responseCreateUser = restClient.createUser(AUTH,body);

        String userId = responseCreateUser.jsonPath().getString("id");

        System.out.println(responseCreateUser.asString());
        System.out.println(responseCreateUser.getStatusCode());
        Assumptions.assumeTrue(responseCreateUser.getStatusCode() == 201, "Create user didn't return 201 status code");


        //Get created user
        Response responseGetUserById = restClient.getUserById(AUTH, userId);
        Assumptions.assumeTrue(responseGetUserById.getStatusCode() == 200, "Get user didn't return 200 status code ");


        // Create new post for user

        String title=FAKER.lorem().fixedString(10);
        String newBodyPost=FAKER.lorem().characters(10,50);
        String bodyForCreatingPost="{\n" +
                "    \"title\":\""+title+"\",\n" +
                "    \"body\":\""+newBodyPost+"\"\n" +
                "}";
//        String forUpdateBody = "{\n" +
//                "    \"title\":\"011000\",\n" +
//                "    \"body\":\"56 active students as of 9th of March\"\n" +
//                "}";

        Response responseCreatePostByUsingUserId = hwRESTPost.createPostByUsingUserId(AUTH,newBodyPost,userId);
        Assumptions.assumeTrue(responseCreatePostByUsingUserId.getStatusCode() == 201, "Create New Post didn't return 201 status code");

        String newPostId = responseCreatePostByUsingUserId.jsonPath().getString("id");


        //Get the newly created post by post id

        Response responseGetNewCreatedPostByUsingPostId = hwRESTPost.getPostByUsingPostId(AUTH,newPostId);
        Assumptions.assumeTrue(responseGetNewCreatedPostByUsingPostId.getStatusCode() == 200, "Get new created post didn't return 200 status code ");




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
