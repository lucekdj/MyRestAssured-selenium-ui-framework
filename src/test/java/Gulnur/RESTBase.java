package Gulnur;

import APIpractice.RESTClient;
import com.github.javafaker.Faker;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

public class RESTBase {
    public final Faker FAKER = new Faker();
    public final String APIHOST = "https://gorest.co.in/public";
    public final String APIV = "/v2";
    public final String AUTH ="6a78c7d15cba4099344a701e0a95b00e6c69a1680556ffdec6b96d35bae9964d";

    RESTClient restClient=new RESTClient();
    RESTPost restPost=new RESTPost();

    @BeforeEach
    public void baseUrlSetup() {
        // Setting up baseURL by adding host to version of api
        RestAssured.baseURI = APIHOST + APIV;
    }
}