package APIpractice;

import com.github.javafaker.Faker;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class HwPostCRUD extends RESTBase {

    @Test
    public void getListPosts(){

        Response responseListPosts = HwPostREST.getListPosts(AUTH);

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
    public void createPostTest(){

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

        System.out.println(responseCreateUser.asPrettyString() + "  created new User");
        System.out.println(responseCreateUser.getStatusCode());
        Assumptions.assumeTrue(responseCreateUser.getStatusCode() == 201, "Create user didn't return 201 status code");

        //Get created user id ,for use to create new post
        Response responseGetUserById = restClient.getUserById(AUTH, userId);
        System.out.println(responseGetUserById.asPrettyString() + "  returned User");
        System.out.println(responseGetUserById.getStatusCode());
        Assumptions.assumeTrue(responseGetUserById.getStatusCode() == 200, "Get user didn't return 200 status code ");


        // Create new post for user
        String title=FAKER.lorem().fixedString(10);
        String bodyInfo=FAKER.lorem().characters(10,50);
        String bodyForCreatingPost="{\n" +
                "    \"title\":\""+title+"\",\n" +
                "    \"body\":\""+bodyInfo+"\"\n" +
                "}";
//        String forUpdateBody = "{\n" +
//                "    \"title\":\"011000\",\n" +
//                "    \"body\":\"56 active students as of 9th of March\"\n" +
//                "}";

        Response responseCreatePostByUsingUserId = hwPostREST.createPostByUsingUserId(AUTH,bodyForCreatingPost,userId);
        System.out.println(responseCreatePostByUsingUserId.asPrettyString() + "  created body post");
        System.out.println(responseCreatePostByUsingUserId.getStatusCode());
        Assumptions.assumeTrue(responseCreatePostByUsingUserId.getStatusCode() == 201, "Create New Post didn't return 201 status code");

        String newPostId=responseCreatePostByUsingUserId.jsonPath().getString("id");

        //Get the newly created post by post id
        Response responseGetNewCreatedPostByUsingPostId = hwPostREST.getPostByUsingPostId(AUTH,newPostId);
        System.out.println(responseGetNewCreatedPostByUsingPostId.asPrettyString() + "  returned body post");
        System.out.println(responseGetNewCreatedPostByUsingPostId.getStatusCode());
        Assumptions.assumeTrue(responseGetNewCreatedPostByUsingPostId.getStatusCode() == 200, "Get new created post didn't return 200 status code ");

        assertAll(
                // assert 201 for create post
                () -> assertEquals(201, responseCreatePostByUsingUserId.getStatusCode(), "status codes are not the same"),
                () -> assertEquals(userId, responseCreatePostByUsingUserId.jsonPath().getString("user_id"), "userIds  are not the same"),
                () -> assertEquals(title, responseCreatePostByUsingUserId.jsonPath().getString("title"), "titles are not the same"),
                () -> assertEquals(bodyInfo, responseCreatePostByUsingUserId.jsonPath().getString("body"), "bodies are not the same"),
                // assert 200 for get new post
                () -> assertEquals(200, responseGetNewCreatedPostByUsingPostId.getStatusCode(), "status codes are not the same"),
                () -> assertEquals(userId, responseGetNewCreatedPostByUsingPostId.jsonPath().getString("user_id"), "userIds  are not the same"),
                () -> assertEquals(title, responseGetNewCreatedPostByUsingPostId.jsonPath().getString("title"), "titles are not the same"),
                () -> assertEquals(bodyInfo, responseGetNewCreatedPostByUsingPostId.jsonPath().getString("body"), "bodies are not the same"),
                //Asserting all the fields are present
                () -> assertTrue(responseGetNewCreatedPostByUsingPostId.getBody().asString().contains("id"), "id key is not present in the response body"),
                () -> assertTrue(responseGetNewCreatedPostByUsingPostId.getBody().asString().contains("user_id"), "name key is not present in the response body"),
                () -> assertTrue(responseGetNewCreatedPostByUsingPostId.getBody().asString().contains("title"), "email key is not present in the response body"),
                () -> assertTrue(responseGetNewCreatedPostByUsingPostId.getBody().asString().contains("body"), "status key is not present in the response body")
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
    public void updatePostTest() {

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

        System.out.println(responseCreateUser.asPrettyString() + "  created new User");
        System.out.println(responseCreateUser.getStatusCode());
        Assumptions.assumeTrue(responseCreateUser.getStatusCode() == 201, "Create user didn't return 201 status code");

        //Get created user id ,for use to create new post
        Response responseGetUserById = restClient.getUserById(AUTH, userId);
        System.out.println(responseGetUserById.asPrettyString() + "  returned User");
        System.out.println(responseGetUserById.getStatusCode());
        Assumptions.assumeTrue(responseGetUserById.getStatusCode() == 200, "Get user didn't return 200 status code ");


        // Create new post for user
        String title=FAKER.lorem().fixedString(10);
        String bodyInfo=FAKER.lorem().characters(10,50);
        String bodyForCreatingPost="{\n" +
                "    \"title\":\""+title+"\",\n" +
                "    \"body\":\""+bodyInfo+"\"\n" +
                "}";
//        String forUpdateBody = "{\n" +
//                "    \"title\":\"011000\",\n" +
//                "    \"body\":\"56 active students as of 9th of March\"\n" +
//                "}";

        Response responseCreatePostByUsingUserId = hwPostREST.createPostByUsingUserId(AUTH,bodyForCreatingPost,userId);
        System.out.println(responseCreatePostByUsingUserId.asPrettyString() + " request to create new post");
        System.out.println(responseCreatePostByUsingUserId.getStatusCode());
        Assumptions.assumeTrue(responseCreatePostByUsingUserId.getStatusCode() == 201, "Create New Post didn't return 201 status code");

        String newPostId=responseCreatePostByUsingUserId.jsonPath().getString("id");

        //Get the newly created post by post id
        Response responseGetNewCreatedPostByUsingPostId = hwPostREST.getPostByUsingPostId(AUTH,newPostId);
        System.out.println(responseGetNewCreatedPostByUsingPostId.asPrettyString() + " response with created post");
        System.out.println(responseGetNewCreatedPostByUsingPostId.getStatusCode());
        Assumptions.assumeTrue(responseGetNewCreatedPostByUsingPostId.getStatusCode() == 200, "Get new created post didn't return 200 status code ");


        //Update created post

        String updTitle=FAKER.animal().name();
        String updBodyInfo=FAKER.lorem().characters(5, 20,true);
        String updBodyForCreatingPost="{\n" +
                "    \"title\":\""+updTitle+"\",\n" +
                "    \"body\":\""+updBodyInfo+"\"\n" +
                "}";

        Response responseUpdatePostUsingPostId = hwPostREST.updatePostByUsingPostId(AUTH,updBodyForCreatingPost,newPostId);
        System.out.println(responseUpdatePostUsingPostId.asPrettyString() + " request to get updated post");
        System.out.println(responseUpdatePostUsingPostId.getStatusCode());
        Assumptions.assumeTrue(responseUpdatePostUsingPostId.getStatusCode() == 200, "Updated Post didn't return 201 status code");

        String updatedPostId = responseUpdatePostUsingPostId.jsonPath().getString("id");

        // Get updated post
        Response responseGetUpdatedPost = hwPostREST.getPostByUsingPostId(AUTH,updatedPostId);
        System.out.println(responseGetUpdatedPost.asPrettyString() + "response with updated post");
        System.out.println(responseGetUpdatedPost.getStatusCode());
        Assumptions.assumeTrue(responseGetUpdatedPost.getStatusCode()==200,"Get Post didn't return 200 status code");

        assertAll(
                //Asserting update response body
                () -> assertEquals(200,responseUpdatePostUsingPostId.getStatusCode(),"status codes are not the same "),
                () -> assertEquals(userId,responseUpdatePostUsingPostId.jsonPath().getString("user_id"),"bodies are not the same "),
                () -> assertEquals(updTitle,responseUpdatePostUsingPostId.jsonPath().getString("title"),"body titles are not the same "),
                () -> assertEquals(updBodyInfo,responseUpdatePostUsingPostId.jsonPath().getString("body"),"bodies are not the same "),
                //Asserting update method results are saved in DB by using GET response method
                () -> assertEquals(200,responseGetUpdatedPost.getStatusCode(),"status codes are not the same "),
                () -> assertEquals(userId,responseGetUpdatedPost.jsonPath().getString("user_id"),"userIds are not the same "),
                () -> assertEquals(updTitle,responseGetUpdatedPost.jsonPath().getString("title"),"titles are not the same "),
                () -> assertEquals(updBodyInfo,responseGetUpdatedPost.jsonPath().getString("body")," bodies are not the same "),
                //Asserting all the fields are present
                () -> assertTrue( responseGetUpdatedPost.getBody().asString().contains("id"), "id key is not present in the response body"),
                () -> assertTrue( responseGetUpdatedPost.getBody().asString().contains("user_id"), "name key is not present in the response body"),
                () -> assertTrue( responseGetUpdatedPost.getBody().asString().contains("title"), "email key is not present in the response body"),
                () -> assertTrue( responseGetUpdatedPost.getBody().asString().contains("body"), "status key is not present in the response body")
        );
    }
    @Test
    public void deletePost(){
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
        Assumptions.assumeTrue(responseCreateUser.getStatusCode() == 201, "Create user didn't return 201 status code");
        String userId = responseCreateUser.jsonPath().getString("id");
        System.out.println(responseCreateUser.asPrettyString() + " request to create user");
        System.out.println(responseCreateUser.getStatusCode());

        //Get created user id ,for use to create new post
        Response responseGetUserById = restClient.getUserById(AUTH, userId);
        Assumptions.assumeTrue(responseGetUserById.getStatusCode() == 200, "Get user didn't return 200 status code ");
        System.out.println(responseGetUserById.asPrettyString() + "  Get respond with created user");
        System.out.println(responseGetUserById.getStatusCode());

        // Create new post for user
        String title=FAKER.lorem().fixedString(10);
        String bodyInfo=FAKER.lorem().characters(10,50);
        String bodyForCreatingPost="{\n" +
                "    \"title\":\""+title+"\",\n" +
                "    \"body\":\""+bodyInfo+"\"\n" +
                "}";

        Response responseCreatePostByUsingUserId = hwPostREST.createPostByUsingUserId(AUTH,bodyForCreatingPost,userId);
        Assumptions.assumeTrue(responseCreatePostByUsingUserId.getStatusCode() == 201, "Create New Post didn't return 201 status code");
        System.out.println(responseCreatePostByUsingUserId.asPrettyString() + " request create post");
        System.out.println(responseCreatePostByUsingUserId.getStatusCode());

        String newPostId=responseCreatePostByUsingUserId.jsonPath().getString("id");

        //Get the newly created post by post id
        Response responseGetNewCreatedPostByUsingPostId = hwPostREST.getPostByUsingPostId(AUTH,newPostId);
        Assumptions.assumeTrue(responseGetNewCreatedPostByUsingPostId.getStatusCode() == 200, "Get new created post didn't return 200 status code ");
        System.out.println(responseGetNewCreatedPostByUsingPostId.asPrettyString() + " Get respond with created  post");
        System.out.println(responseGetNewCreatedPostByUsingPostId.getStatusCode());

        Response responseDeleteCreatedPost = hwPostREST.deletePostUsingPostId(AUTH,newPostId);
        Assumptions.assumeTrue(responseDeleteCreatedPost.getStatusCode()==204, "Status code is not 204");
        System.out.println(responseDeleteCreatedPost.asPrettyString() + " request to delete post");
        System.out.println(responseDeleteCreatedPost.getStatusCode());

        Response responseGetPostAfterDelete = hwPostREST.getPostByUsingPostId(AUTH,newPostId);
        Assumptions.assumeTrue(responseGetPostAfterDelete.getStatusCode()==404,"Status code is not 204");

        System.out.println(responseGetPostAfterDelete.asPrettyString() + " Get request with deleted post");
        System.out.println(responseGetPostAfterDelete.getStatusCode());

        assertAll(
                () -> assertEquals(204,responseDeleteCreatedPost.statusCode(),"Status codes are not the same"),
                () -> assertEquals(404,responseGetPostAfterDelete.statusCode(),"Status codes are not the same"),
                () -> assertEquals("Resource not found", responseGetPostAfterDelete.jsonPath().getString("message"),
                "Message key is not present in the response with status code 404")
        );

    }

}
