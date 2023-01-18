package common

class Endpoints {
    private static HashMap<String, HashMap<String, String>> endpoints

    static String getURI(String name){
        return endpoints[name][Environment.getMode()]
    }

    static void setEndpoints(HashMap<String, HashMap<String, String>> newUris) {
        endpoints = newUris
    }

}
