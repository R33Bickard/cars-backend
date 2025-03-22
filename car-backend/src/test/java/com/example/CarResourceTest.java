package com.example;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class CarResourceTest {

    @Test
    public void testGetCarsEndpoint() {
        given()
          .when().get("/cars")
          .then()
             .statusCode(200);
    }
}