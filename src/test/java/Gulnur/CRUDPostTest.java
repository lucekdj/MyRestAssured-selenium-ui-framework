package Gulnur;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CRUDPostTest extends RESTBase{

    /*
    -Get list of posts test
    -Get posts
    -Assert that status code is 200
    -Assert that Response body has id, user_id , title and body fields present
     */

    @Test
    public void getPosts(){
        Response responseListPosts= restPost.getPosts(AUTH);
        assertAll(
                () -> assertEquals(200, responseListPosts.getStatusCode(), "Status codes are not the same"),
                () -> assertTrue(responseListPosts.getBody().asString().contains("id"), "id key is not present in the response body"),
                () -> assertTrue(responseListPosts.getBody().asString().contains("user_id"), "user_id is not present in the response body"),
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
    public void createPosts(){
        // Preparing request body
        String name = FAKER.name().fullName();
        String email = FAKER.internet().emailAddress();
        String body = "{\n" +
                "    \"name\": \"" + name + "\",\n" +
                "    \"email\": \"" + email + "\",\n" +
                "    \"gender\": \"male\",\n" +
                "    \"status\": \"inactive\"\n" +
                "}";
        Response responseCreateUser=restClient.createUser(AUTH, body);
        Assumptions.assumeTrue(responseCreateUser.getStatusCode()==201, "Status code is not 201");

        String userID=responseCreateUser.jsonPath().getString("id");

        Response responseGetUserById=restClient.getUserById(AUTH,userID);
        Assumptions.assumeTrue(responseGetUserById.getStatusCode()==200, "Status code is not 200");

        String title=FAKER.lorem().fixedString(10);
        String bodyOfPost=FAKER.lorem().characters(10,50);
        String bodyForCreatingPost="{\n" +
                "    \"title\":\""+title+"\",\n" +
                "    \"body\":\""+bodyOfPost+"\"\n" +
                "}";

        Response responseCreatePostUsingUserId=restPost.createPostUsingUserId(AUTH, bodyForCreatingPost, userID);
        Assumptions.assumeTrue(responseCreatePostUsingUserId.getStatusCode()==201, "Status code is not 201");

        String postID=responseCreatePostUsingUserId.jsonPath().getString("id");

        Response responseGetPostByPostId=restPost.getPostByPostId(AUTH, postID);
        Assumptions.assumeTrue(responseGetPostByPostId.getStatusCode()==200, "Status code is not 200");

        assertAll(
                //Asserting create response body
                () -> assertEquals(201, responseCreatePostUsingUserId.getStatusCode(), "Status codes are not the same"),
                () -> assertEquals(title, responseCreatePostUsingUserId.jsonPath().getString("title"), "titles are not the same"),
                () -> assertEquals(bodyOfPost, responseCreatePostUsingUserId.jsonPath().getString("body"), "bodies are not the same"),
                () -> assertEquals(userID, responseCreatePostUsingUserId.jsonPath().getString("user_id"),"user ids don't match"),
                //Asserting post method results are saved in DB by using get response method
                () -> assertEquals(200, responseGetPostByPostId.getStatusCode(), "Status codes are not the same"),
                () -> assertEquals(title, responseGetPostByPostId.jsonPath().getString("title"), "titles are not the same"),
                () -> assertEquals(bodyOfPost, responseGetPostByPostId.jsonPath().getString("body"), "bodies are not the same"),
                () -> assertEquals(userID, responseGetPostByPostId.jsonPath().getString("user_id"),"user ids don't match"),
                //Asserting all the fields are present
                () -> assertTrue(responseGetPostByPostId.getBody().asString().contains("id"), "id key is not present in the response body"),
                () -> assertTrue(responseGetPostByPostId.getBody().asString().contains("user_id"), "name key is not present in the response body"),
                () -> assertTrue(responseGetPostByPostId.getBody().asString().contains("title"), "email key is not present in the response body"),
                () -> assertTrue(responseGetPostByPostId.getBody().asString().contains("body"), "status key is not present in the response body")
        );
    }
    // + Update post
//   -Create a new user
//   -Get the newly created user, validate they are saved in db
//   -create a post for that new user
//   -Retrieve the newly created post by post id, validate it is saved in db
//   -Update post
//   -Get Post
//   -Assert it is updated
//   -Assert it is updated in db
    @Test
    public void updatePostByPostId(){
        // Preparing request body
        String name = FAKER.name().fullName();
        String email = FAKER.internet().emailAddress();
        String body = "{\n" +
                "    \"name\": \"" + name + "\",\n" +
                "    \"email\": \"" + email + "\",\n" +
                "    \"gender\": \"male\",\n" +
                "    \"status\": \"inactive\"\n" +
                "}";
        Response responseCreateUser=restClient.createUser(AUTH, body);
        Assumptions.assumeTrue(responseCreateUser.getStatusCode()==201, "Status code is not 201");

        String userID=responseCreateUser.jsonPath().getString("id");

        Response responseGetUserById=restClient.getUserById(AUTH,userID);
        Assumptions.assumeTrue(responseGetUserById.getStatusCode()==200, "Status code is not 200");

        String title=FAKER.lorem().fixedString(10);
        String bodyOfPost=FAKER.lorem().characters(10,50);
        String bodyForCreatingPost="{\n" +
                "    \"title\":\""+title+"\",\n" +
                "    \"body\":\""+bodyOfPost+"\"\n" +
                "}";

        Response responseCreatePostUsingUserId=restPost.createPostUsingUserId(AUTH, bodyForCreatingPost, userID);
        Assumptions.assumeTrue(responseCreatePostUsingUserId.getStatusCode()==201, "Status code is not 201");

        String postID=responseCreatePostUsingUserId.jsonPath().getString("id");

        Response responseGetPostByPostId=restPost.getPostByPostId(AUTH, postID);
        Assumptions.assumeTrue(responseGetPostByPostId.getStatusCode()==200, "Status code is not 200");

        String titleUpdated=FAKER.lorem().fixedString(10);
        String bodyOfPostUpdated=FAKER.lorem().characters(10,50);
        String bodyForUpdatingPost="{\n" +
                "    \"title\":\""+titleUpdated+"\",\n" +
                "    \"body\":\""+bodyOfPostUpdated+"\"\n" +
                "}";
        Response responseUpdatePostUsingPostId=restPost.updatePostByPostId(AUTH,bodyForUpdatingPost,postID);
        Assumptions.assumeTrue(responseUpdatePostUsingPostId.getStatusCode()==200, "Status code is not 200");

        Response responseGetPostAfterUpdate= restPost.getPostByPostId(AUTH,postID);
        Assumptions.assumeTrue(responseGetPostAfterUpdate.getStatusCode()==200, "Status code is not 200");

        assertAll(
                //Asserting update response body
                () -> assertEquals(200, responseUpdatePostUsingPostId.getStatusCode(), "Status codes are not the same"),
                () -> assertEquals(titleUpdated, responseUpdatePostUsingPostId.jsonPath().getString("title"), "titles are not the same"),
                () -> assertEquals(bodyOfPostUpdated, responseUpdatePostUsingPostId.jsonPath().getString("body"), "bodies are not the same"),
                () -> assertEquals(userID, responseUpdatePostUsingPostId.jsonPath().getString("user_id"),"user ids don't match"),
                //Asserting update method results are saved in DB by using get response method
                () -> assertEquals(200, responseGetPostAfterUpdate.getStatusCode(), "Status codes are not the same"),
                () -> assertEquals(titleUpdated, responseGetPostAfterUpdate.jsonPath().getString("title"), "titles are not the same"),
                () -> assertEquals(bodyOfPostUpdated, responseGetPostAfterUpdate.jsonPath().getString("body"), "bodies are not the same"),
                () -> assertEquals(userID, responseGetPostAfterUpdate.jsonPath().getString("user_id"),"user ids don't match"),
                //Asserting all the fields are present
                () -> assertTrue( responseGetPostAfterUpdate.getBody().asString().contains("id"), "id key is not present in the response body"),
                () -> assertTrue( responseGetPostAfterUpdate.getBody().asString().contains("user_id"), "name key is not present in the response body"),
                () -> assertTrue( responseGetPostAfterUpdate.getBody().asString().contains("title"), "email key is not present in the response body"),
                () -> assertTrue( responseGetPostAfterUpdate.getBody().asString().contains("body"), "status key is not present in the response body")
        );
    }

    // + Delete post
//   -Create a new user
//   -Get the newly created user, validate they are saved in db
//   -create a post for that new user
//   -Retrieve the newly created post by post id, validate it is saved in db
//   -Delete post
//   -Get Post
//   -Assert it is deleted
//   -Assert it is deleted in db

    @Test
    public void deletePostByPostID(){
        // Preparing request body
        String name = FAKER.name().fullName();
        String email = FAKER.internet().emailAddress();
        String body = "{\n" +
                "    \"name\": \"" + name + "\",\n" +
                "    \"email\": \"" + email + "\",\n" +
                "    \"gender\": \"male\",\n" +
                "    \"status\": \"inactive\"\n" +
                "}";
        Response responseCreateUser=restClient.createUser(AUTH, body);
        Assumptions.assumeTrue(responseCreateUser.getStatusCode()==201, "Status code is not 201");

        String userID=responseCreateUser.jsonPath().getString("id");

        Response responseGetUserById=restClient.getUserById(AUTH,userID);
        Assumptions.assumeTrue(responseGetUserById.getStatusCode()==200, "Status code is not 200");

        String title=FAKER.lorem().fixedString(10);
        String bodyOfPost=FAKER.lorem().characters(10,50);
        String bodyForCreatingPost="{\n" +
                "    \"title\":\""+title+"\",\n" +
                "    \"body\":\""+bodyOfPost+"\"\n" +
                "}";
        Response responseCreatePostUsingUserId=restPost.createPostUsingUserId(AUTH, bodyForCreatingPost, userID);
        Assumptions.assumeTrue(responseCreatePostUsingUserId.getStatusCode()==201, "Status code is not 201");

        String postID=responseCreatePostUsingUserId.jsonPath().getString("id");

        Response responseGetPostByPostId=restPost.getPostByPostId(AUTH, postID);
        Assumptions.assumeTrue(responseGetPostByPostId.getStatusCode()==200, "Status code is not 200");

        Response responseDeletePostUsingPostId=restPost.deletePostByPostId(AUTH,postID);
        Assumptions.assumeTrue(responseDeletePostUsingPostId.getStatusCode()==204, "Status code is not 204");

        Response responseGetPostAfterDeletion=restPost.getPostByPostId(AUTH,postID);
        Assumptions.assumeTrue(responseGetPostAfterDeletion.getStatusCode()==404, "Status code is not 404");
        assertAll(
                () -> assertEquals(204, responseDeletePostUsingPostId.getStatusCode(), "Status codes are not the same"),
                () -> assertEquals(404, responseGetPostAfterDeletion.getStatusCode(), "Status codes are not the same"),
                () -> assertEquals("Resource not found", responseGetPostAfterDeletion.jsonPath().getString("message"),
                        "Message key is not present in the response with status code 404")
        );
    }

}














