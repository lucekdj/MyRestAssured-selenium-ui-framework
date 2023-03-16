package APIpractice;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class RESTtestFirstclass {

    private static final String APIHOST = "https://gorest.co.in/public";
    private static final String APIV = "/v2";

    public static void main(String[] args) {
        RestAssured.baseURI = APIHOST + APIV;

        String auth = "4a5df07f01f92a0b18a513fe4176f2e030c9bc4a6e4a18e43daea56172202843";

        Response responseListUsers = RestAssured
                .given()
                .contentType(ContentType.JSON)
                .headers("Authorization", "Bearer " + auth)
                .accept(ContentType.JSON)
                .when()
                .get("/users");

        System.out.println(responseListUsers.asString());
        System.out.println(responseListUsers.getStatusCode());

        Response responseGetUserByid = RestAssured
                .given()
                .contentType(ContentType.JSON)
                .headers("Authorization", "Bearer " + auth)
                .accept(ContentType.JSON)
                .when()
                .pathParam("userId", "45250")
                .get("/users/{userId}");

        System.out.println(responseGetUserByid.asString());
        System.out.println(responseGetUserByid.getStatusCode());

        // Triple A
        // Arrange -> given
        // Act -> when
        // Assert -> then

        String body = "{\n" +
                "    \"name\": \"Tim from RestAssured\",\n" +
                "    \"email\": \"justarandomemailverawd@gmail.com\",\n" +
                "    \"gender\": \"male\",\n" +
                "    \"status\": \"inactive\"\n" +
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

        String bodyUpdate = "{\n" +
                "    \"name\": \"Tim from RestAssured\",\n" +
                "    \"email\": \"updatedRandomEmailFromRestAssured@yahoo.com\",\n" +
                "    \"gender\": \"male\",\n" +
                "    \"status\": \"active\"\n" +
                "}";

        Response responseUpdateUserById = RestAssured
                .given()
                .contentType(ContentType.JSON)
                .headers("Authorization", "Bearer " + auth)
                .accept(ContentType.JSON)
                .body(bodyUpdate)
                .when()
                .pathParam("userId", "1017406")
                .put("/users/{userId}");

        System.out.println(responseUpdateUserById.asString());
        System.out.println(responseUpdateUserById.getStatusCode());


        Response responseDeleteUser = RestAssured
                .given()
                .contentType(ContentType.JSON)
                .headers("Authorization", "Bearer " + auth)
                .accept(ContentType.JSON)
                .when()
                .pathParam("userId", "1017406")
                .delete("/users/{userId}");

        System.out.println(responseDeleteUser.asString());
        System.out.println(responseDeleteUser.getStatusCode());
    }
}