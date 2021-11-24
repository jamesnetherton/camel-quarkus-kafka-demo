package org.apache.camel.example;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.ResponseBodyExtractionOptions;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

@QuarkusTest
public class ExchangeRatesTest {

    @Test
    public void exchangeRates() {
        await().atMost(10, TimeUnit.SECONDS).pollDelay(100, TimeUnit.MILLISECONDS).until(() -> {
            JsonPath jsonPath = RestAssured.get("/exchangerates")
                    .then()
                    .statusCode(200)
                    .extract()
                    .body()
                    .jsonPath();

            List<LinkedHashMap<String, Integer>> results = jsonPath.getList(".");
            if (results.isEmpty()) {
                return false;
            }

            LinkedHashMap<String, Integer> exchangeRateData = results.get(0);
            return exchangeRateData.get("timestamp") > 0 && exchangeRateData.get("value") > 0;
        });
    }
}
