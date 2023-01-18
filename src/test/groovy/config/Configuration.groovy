package config

import common.BasePath
import common.Endpoints
import common.Environment
import common.Accounts
import groovy.json.JsonSlurper
import org.junit.Assert

class Configuration {
    static final String configFile = "src/test/resources/configurations.json"
    static final String endpointsFile = "src/test/resources/endpoints.json"
    static final String accountsFile = "src/test/resources/accounts.json"

    static boolean loadEnvironments() {
        def jsonFile = new File(configFile)
        def parsedJson = new JsonSlurper().parseText(jsonFile.text)
        if (parsedJson != null){
            String newDefaultEnv = parsedJson.defaultEnvironment
            Environment.setDefaultEnv(newDefaultEnv)
            ArrayList<String> environments = parsedJson.environments
            Environment.setEnvs(environments)
            String defaultMode = parsedJson.defaultMode
            Environment.setDefaultMode(defaultMode)
            ArrayList<String> modes = parsedJson.modes
            Environment.setModes(modes)
            return true
        }
        else {
            return false
        }
    }

    static boolean loadPaths() {
        def jsonFile = new File(configFile)
        def parsedJson = new JsonSlurper().parseText(jsonFile.text)
        if (parsedJson != null){
            HashMap<String, HashMap<String, String>> newPaths = parsedJson.basePaths
            BasePath.setBasePaths(newPaths)
            return true
        }
        else {
            return false
        }
    }

    static boolean loadEndpoints() {
        def jsonFile = new File(endpointsFile)
        HashMap<String, HashMap<String, String>> newEndpoints = (HashMap<String, HashMap<String, String>>) new JsonSlurper().parseText(jsonFile.text)
        if (newEndpoints != null){
            Endpoints.setEndpoints(newEndpoints)
            return true
        }
        else {
            return false
        }
    }

    static boolean loadAccounts() {
        def jsonFile = new File(accountsFile)
        HashMap<String, HashMap<String, String>> newAccounts = (HashMap<String, HashMap<String, String>>) new JsonSlurper().parseText(jsonFile.text)
        if (newAccounts != null){
            Accounts.setAccounts(newAccounts)
            return true
        }
        else {
            return false
        }
    }

    static void loadAllConfigs() {
        Assert.assertTrue("Failed to load environments values from file.", loadEnvironments())
        Assert.assertTrue("Failed to load base paths and uris.", loadPaths())
        Assert.assertTrue("Failed to load endpoints.", loadEndpoints())
        Assert.assertTrue("Failed to load accounts.", loadAccounts())
        println "Configurations loaded."
    }

    static void main(String[] args) {
        loadAllConfigs()
        println Endpoints.getURI("users")
        println Accounts.getAccountEmail("pepe")
        println Accounts.getAccountPassword("pepe")
    }

}
