package support.env

import com.google.common.collect.ImmutableMap
import config.Configuration
import io.cucumber.groovy.Hooks
import io.cucumber.groovy.Scenario
import io.restassured.RestAssured
import io.restassured.filter.log.RequestLoggingFilter
import io.restassured.filter.log.ResponseLoggingFilter
import org.apache.commons.io.output.WriterOutputStream
import common.BasePath
import common.Environment
import util.CurlParser
import util.TestRailsLogger
import static com.github.automatedowl.tools.AllureEnvironmentWriter.allureEnvironmentWriter

this.metaClass.mixin(Hooks)

setUpConfigurations()
setUpAllureEnv()

static void setUpConfigurations() {
    Configuration.loadAllConfigs();
    Environment.getEnvironment()
    Environment.getMode()
}

static void setUpAllureEnv() {
    //RestAssured.port = "";
    //RestAssured.basePath = "";
    RestAssured.baseURI = BasePath.getBasePath()
    String tags = System.getProperty("cucumber.filter.tags")
    if (tags == null || tags.isEmpty()) {
        tags = "ALL"
    }
    HashMap<String, String> properties = new HashMap<String, String>()
    properties.put("Environment:", Environment.getEnvironment().toUpperCase());
    if (TestRailsLogger.testRunID != null && !TestRailsLogger.testRunID?.isEmpty()) {
        properties.put("Test Run:", TestRailsLogger.getTestRunLink())
    }
    properties.put("Base url:", BasePath.getBasePath())
    properties.put("Tags:", tags)
    ImmutableMap<String, String> immutableMap = ImmutableMap.copyOf(properties)
    allureEnvironmentWriter(immutableMap, System.getProperty("user.dir") + "/build/allure-results/")
}

StringWriter requestWriter = new StringWriter()
StringWriter responseWriter = new StringWriter()

Before() { ->
    PrintStream requestCapture = new PrintStream(new WriterOutputStream(requestWriter, "UTF-8"), true)
    PrintStream responseCapture = new PrintStream(new WriterOutputStream(responseWriter, "UTF-8"), true)
    RestAssured.filters(new RequestLoggingFilter(requestCapture), new ResponseLoggingFilter(responseCapture))
}

After(1000) { Scenario scenario ->
    if (scenario.isFailed()) {
        String request = requestWriter.toString()
        String curl = CurlParser.getCurls(request)
        scenario.attach("Request: \n" + request, "text/plain", "Requests")
        scenario.attach(curl, "text/plain", "Curls")
        scenario.attach("Response: \n" + responseWriter.toString(), "text/plain", "Responses")
        TestRailsLogger.logResultToTestRail(scenario, curl);
    } else {
        TestRailsLogger.logResultToTestRail(scenario, "")
    }
}
