package APIpractice;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class HwRESTPost {



        public static Response getListPosts(String AUTH) {
            return RestAssured
                    .given()
                    .contentType(ContentType.JSON)
                    .headers("Authorization", "Bearer " + AUTH)
                    .accept(ContentType.JSON)
                    .when()
                    .get("/post");
        }

        public Response createPostByUsingUserId(String AUTH, String newCreatePostBody,String userId) {
            return RestAssured
                    .given()
                    .contentType(ContentType.JSON)
                    .headers("Authorization", "Bearer " + AUTH)
                    .accept(ContentType.JSON)
                    .body(newCreatePostBody)
                    .when()
                    .pathParam("postId",userId)
                    .post("/users/{userId}/posts");
        }

        public Response getPostByUsingPostId(String AUTH, String postId) {
            return RestAssured
                    .given()
                    .contentType(ContentType.JSON)
                    .headers("Authorization", "Bearer " + AUTH)
                    .accept(ContentType.JSON)
                    .when()
                    .pathParam("id", postId)
                    .get("/postId/{id}");
        }

        public Response deletePostUsingPostId(String AUTH, String postId) {
            return RestAssured
                    .given()
                    .contentType(ContentType.JSON)
                    .headers("Authorization", "Bearer " + AUTH)
                    .accept(ContentType.JSON)
                    .when()
                    .pathParam("id", postId)
                    .delete("/posts/{id}");
        }

        public Response updatePostByUsingPostId(String AUTH, String body, String postId) {
            return RestAssured
                    .given()
                    .contentType(ContentType.JSON)
                    .headers("Authorization", "Bearer " + AUTH)
                    .accept(ContentType.JSON)
                    .body(body)
                    .when()
                    .pathParam("id", postId)
                    .put("/posts/{id}");
        }
    }


