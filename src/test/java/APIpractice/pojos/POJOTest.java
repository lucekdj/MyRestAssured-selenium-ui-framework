package APIpractice.pojos;

import APIpractice.pojos.UserPojo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;

public class POJOTest {

    public static void main(String[] args) {

        String AUTH = "4a5df07f01f92a0b18a513fe4176f2e030c9bc4a6e4a18e43daea56172202843";
        String APIHOST = "https://gorest.co.in/public";
        String APIV = "/v2";
        RestAssured.baseURI = APIHOST + APIV;

        UserPojo serializedUser = new UserPojo("GSON Serialization",
                "gsonmethisehhmailform4134@gmail.com", "female", "active");

        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
//        Exclude Field Without Expose Annotation on the moment of De/serialization

        System.out.println(gson.toJson(serializedUser));

        Response responseCreateUser = RestAssured
                .given()
                .contentType(ContentType.JSON)
                .headers("Authorization", "Bearer " + AUTH)
                .accept(ContentType.JSON)
                .body(gson.toJson(serializedUser))
                .when()
                .post("/users");

        System.out.println(responseCreateUser.asString());
        System.out.println(responseCreateUser.getStatusCode());

        UserPojo deserializedUser = gson.fromJson(responseCreateUser.asString(), UserPojo.class);

        System.out.println("__________________________");
        System.out.println(deserializedUser);

        Assertions.assertEquals(201, responseCreateUser.getStatusCode());
        Assertions.assertEquals(serializedUser.getName(), deserializedUser.getName());
        Assertions.assertEquals(serializedUser.getEmail(), deserializedUser.getEmail());

    }


}