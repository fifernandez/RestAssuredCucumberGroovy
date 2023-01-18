package util

import io.cucumber.core.backend.TestCaseState
import io.cucumber.groovy.Scenario
import org.apache.commons.lang3.reflect.FieldUtils
import org.apache.commons.lang3.reflect.MethodUtils
import testrail.APIClient
import java.lang.reflect.Field
import java.lang.reflect.Method

class TestRailsLogger {
    static String baseUrl = ""
    static String usernameTestRail = ""
    static String passwordTestRail = ""
    static APIClient client

    private static final int FAIL_STATE = 5
    private static final int SUCCESS_STATE = 1
    private static final String SUCCESS_COMMENT = "This test passed with cucumber automation."
    private static final String FAILED_COMMENT = "This test failed with cucumber automation."
    private static String testRunID = System.getProperty("testRunID")
    private static String jenkinsLink = System.getProperty("jenkinsLink")

    static APIClient testRailApiClient() {
        if (client == null) {
            client = new APIClient(baseUrl);
            client.setUser(usernameTestRail);
            client.setPassword(passwordTestRail);
        }
        return client;
    }

    static void logResultToTestRail(Scenario scenario, String curls) {
        if (testRunID != null) {
            String caseId = getCaseId(scenario)
            if (caseId != '') {
                Map<String, Serializable> data = new HashMap<>()
                String comment = "\n"
                comment += "Scenario Name: " + scenario.getName() + "\n"
                if (!scenario.isFailed()) {
                    data.put("status_id", SUCCESS_STATE)
                    comment += SUCCESS_COMMENT + "\n"
                } else {
                    data.put("status_id", FAIL_STATE)
                    comment += FAILED_COMMENT + "\n"
                    comment += "Jenkins Link: " + jenkinsLink + "\n"
                    comment += getError(scenario)
                    comment += "\n"
                    comment += "Last curl: \n"
                    comment += curls
                }
                data.put("comment", comment)
                try {
                    testRailApiClient().sendPost("add_result_for_case/" + testRunID + "/" + caseId, data)
                }
                catch (Exception e) {
                    println "Error pushing result to Test Rail. Please check if Test Rail is working as expected."
                    println e.getMessage()
                }
            }
        }
    }

    private static final Field field = FieldUtils.getField(Scenario.class, "delegate", true)
    private static Method getError

    static Throwable getError(Scenario scenario) {
        try {
            final TestCaseState testCase = (TestCaseState) field.get(scenario)
            if (getError == null) {
                getError = MethodUtils.getMatchingMethod(testCase.getClass(), "getError")
                getError.setAccessible(true);
            }
            return (Throwable) getError.invoke(testCase)
        } catch (Exception e) {
            println "Error receiving exception: " + e.stackTrace
        }
        return null
    }

    private static String getCaseId(Scenario scenario) {
        String caseId = ""
        for (String s : scenario.getSourceTagNames()) {
            if (s.contains("TestRail")) {
                String[] res = s.split("(\\(.*?)")
                caseId = res[1].substring(0, res[1].length() - 1)
            }
        }
        return caseId
    }

}