package Gulnur;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class RESTPost {

    public Response getPosts(String AUTH){
        return RestAssured
                .given()
                .contentType(ContentType.JSON)
                .headers("Authorization","Bearer "+AUTH)
                .accept(ContentType.JSON)
                .when()
                .get("/posts");
    }
    public Response createPostUsingUserId(String AUTH, String bodyForCreatingPost, String userID){
        return RestAssured
                .given()
                .contentType(ContentType.JSON)
                .headers("Authorization","Bearer "+AUTH)
                .accept(ContentType.JSON)
                .body(bodyForCreatingPost)
                .when()
                .pathParam("userId",userID)
                .post("/users/{userId}/posts");
    }
    public Response getPostByPostId(String AUTH, String postID){
        return RestAssured
                .given()
                .contentType(ContentType.JSON)
                .headers("Authorization","Bearer "+AUTH)
                .accept(ContentType.JSON)
                .when()
                .pathParam("postId",postID)
                .get("/posts/{postId}");
    }
    public Response updatePostByPostId(String AUTH, String bodyForUpdatingPost, String postID){
        return RestAssured
                .given()
                .contentType(ContentType.JSON)
                .headers("Authorization","Bearer "+AUTH)
                .accept(ContentType.JSON)
                .body(bodyForUpdatingPost)
                .when()
                .pathParam("postId",postID)
                .put("/posts/{postId}");
    }
    public Response deletePostByPostId(String AUTH,String postID){
        return RestAssured
                .given()
                .contentType(ContentType.JSON)
                .headers("Authorization", "Bearer "+AUTH)
                .accept(ContentType.JSON)
                .pathParam("postID",postID)
                .delete("/posts/{postID}");
    }
}