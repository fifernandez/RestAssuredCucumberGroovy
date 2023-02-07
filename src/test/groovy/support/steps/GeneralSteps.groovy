package support.steps

import common.Global
import io.restassured.RestAssured
import io.restassured.module.jsv.JsonSchemaValidator
import io.restassured.specification.RequestSpecification
import org.json.JSONArray
import org.junit.Assert
import common.Endpoints
import io.cucumber.groovy.EN

this.metaClass.mixin(EN)

String basePath

Given(~/^I do a get to the "(.*)" endpoint$/) { String endpointName ->
    basePath = Endpoints.getURI(endpointName);
    RequestSpecification req = RestAssured.given();
    Global.response = req.when().get(new URI(basePath));
}

Then(~/^the returned status code is: "(\d{3})"$/) { String expectedCodeReturned ->
    int responseCode = Global.response.then().extract().statusCode();
    Assert.assertEquals(expectedCodeReturned, String.valueOf(responseCode));
}

Then(~/^the response contains "(.*)" items$/) { int expectedAmount ->
    response = Global.response.then().extract().response()
    JSONArray resJson = new JSONArray(response.asString())
    Assert.assertEquals(resJson.length(), expectedAmount)
}

Then(~/^the response schema for the "(.*)" endpoint is correct$/) { String endpoint ->
    Global.response.then().assertThat()
            .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("schemas/" + endpoint + ".json"));
}