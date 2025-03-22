package com.example.jmeter;

import io.quarkus.test.junit.QuarkusTest;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;
import org.junit.jupiter.api.Test;

@QuarkusTest
public class CarApiResilienceTest {

    @Test
    public void testFallbackOnTimeout() {
        // Hier simulieren wir einen Delay (zum Beispiel durch einen speziellen Query-Parameter).
        // In deiner API solltest du diesen Parameter auswerten und im Fehlerfall einen Fallback auslösen.
        given()
          .queryParam("simulateDelay", "true")
        .when()
          .get("/car")
        .then()
          .statusCode(200)
          // Prüfe, ob der Fallback-Response den String "Fallback" enthält.
          .body(containsString("Fallback"));
    }
}
