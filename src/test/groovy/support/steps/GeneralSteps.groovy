package support.steps

import io.restassured.RestAssured;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Assert;
import common.Endpoints;
import io.cucumber.groovy.EN

this.metaClass.mixin(EN)

Response response
String basePath

Given(~/^I do a get to the "(.*)" endpoint$/) { String endpointName ->
    //Assert.assertTrue("Endpoint '$endpointName' is not defined in the json file.", Endpoints.endpointDefined(endpointName));
    basePath = Endpoints.getURI(endpointName);
    RequestSpecification req = RestAssured.given();
    response = req.when().get(new URI(basePath));
}

Then(~/^the returned status code is: "(\d{3})"$/) { String expectedCodeReturned ->
    int responseCode = response.then().extract().statusCode();
    Assert.assertEquals(expectedCodeReturned, String.valueOf(responseCode));
}

Then(~/^the response contains "(.*)" items$/) { int expectedAmount ->
    //response.then().statusCode(200);
    response = response.then().extract().response();
    JSONArray resJson = new JSONArray(response.asString());
    Assert.assertEquals(resJson.length(), expectedAmount);
}

Given(~/^I do a get to the "(.*)" endpoint just to test with bad parameters$/) { String endpointName ->
    basePath = Endpoints.getURI(endpointName);
    RequestSpecification req = RestAssured.given();
    JSONObject requestParams = new JSONObject();
    requestParams.put("userName", "test_rest");
    requestParams.put("password", "Testrest@123");
    response = req.header("U", "MyAppName")
            .header("Agent", "MyAppName")
            .queryParam("q1=1")
            .queryParam("q2=2")
            .body(requestParams.toString(1))
            .when().get(new URI(basePath));
}

Then(~/^the schema for the "(.*)" endpoint with "(.*)" response code is correct$/) { String endpoint, String responseCode ->
    response = response.then().extract().response();
    response.then().assertThat()
            .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("schemas/" + endpoint + "-" + responseCode + ".json"));
}
