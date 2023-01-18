package common

class BasePath {
    private static HashMap<String, HashMap<String, String>> basePaths

    static String getBasePath() {
        return basePaths[Environment.getEnvironment()][Environment.getMode()]
    }

    static void setBasePaths(HashMap<String, HashMap<String, String>> newPaths) {
        basePaths = newPaths
    }

}
