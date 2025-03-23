package com.example.jmeter;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.common.QuarkusTestResource;
import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasSize;

@QuarkusTest
@QuarkusTestResource(PostgreSQLTestResource.class)
public class CarApiResilienceTest {

    @Test
    void testFallbackOnTimeout() {
        given()
            .queryParam("simulateDelay", "true")
        .when()
            .get("/cars/resilient")
        .then()
            .statusCode(200)
            .body("$", hasSize(0));
    }
}
