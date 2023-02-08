package util

import io.cucumber.core.backend.TestCaseState
import io.cucumber.groovy.Scenario
import org.apache.commons.lang3.reflect.FieldUtils
import org.apache.commons.lang3.reflect.MethodUtils
import testrail.APIClient
import java.lang.reflect.Field
import java.lang.reflect.Method

class TestRailsLogger {
    static String baseUrl
    static String usernameTestRail
    static String passwordTestRail
    static APIClient client
    private static  int SUCCESS_STATE
    private static  String SUCCESS_COMMENT
    private static  int FAIL_STATE
    private static  String FAILED_COMMENT
    static String testRunID = System.getProperty("testRunID")
    static String jenkinsLink = System.getProperty("jenkinsLink")

    static APIClient testRailApiClient() {
        if (client == null) {
            client = new APIClient(baseUrl)
            client.setUser(usernameTestRail)
            client.setPassword(passwordTestRail)
        }
        return client
    }

    static setConfig(String url, String user, String pass, int successState, String successComment, int failState, String failComment){
        baseUrl = url
        usernameTestRail = user
        passwordTestRail = pass
        SUCCESS_STATE = successState
        SUCCESS_COMMENT = successComment
        FAIL_STATE = failState
        FAILED_COMMENT = failComment
    }

    static String getTestRunLink(){
        return baseUrl + "index.php?/runs/view/" + testRunID
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
                    if (jenkinsLink != null) {
                        comment += "Jenkins Link: " + jenkinsLink + "\n"
                    }
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
        scenario.getSourceTagNames().find { String s ->
            if (s.contains("tmsLink=")) {
                caseId = s.substring(s.lastIndexOf("=") + 1)
                return true
            }
        }
        return caseId
    }

}