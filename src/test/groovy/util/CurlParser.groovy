package util

class CurlParser {

    static String getCurls(String requests) {
        StringBuilder curls = new StringBuilder()
        String[] allCurls = requests.replace("Request method:", "#-#Request method:").split("#-#")
        for (int i = 1; i < allCurls.length; i++) {
            curls.append("Curl ").append(i).append(": \n")
            curls.append(prepareCurl(allCurls[i]))
            curls.append("\n").append("\n")
        }
        return curls.toString()
    }

    static String prepareCurl(String process) {
        HashMap<String, Object> request = new HashMap<String, Object>()
        ArrayList<String> headers = new ArrayList<String>()
        ArrayList<String> queryParams = new ArrayList<String>()
        ArrayList<String> formParams = new ArrayList<String>()
        ArrayList<String> cookies = new ArrayList<String>()
        String body = ""
        String currentMultiline = ""

        Scanner scanner = new Scanner(process);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (!line.contains("<none>")) {
                if (line.startsWith("Request method:")) {
                    request.put("method", line.split("^Request method:\\s+")[1])
                } else if (line.startsWith("Request URI:")) {
                    request.put("uri", line.split("^Request URI:\\s+")[1])
                } else if (line.startsWith("Proxy:")) {
                    request.put("proxy", line.split("^Proxy:\\s+")[1])
                } else if (line.startsWith("Request params:")) {
                    currentMultiline = "reqParams"
                } else if (line.startsWith("Query params:")) {
                    String aux = line.split("^Query params:\\s+")[1]
                    queryParams.add(aux)
                    currentMultiline = "queryParams"
                    System.out.println()
                } else if (line.startsWith("Form params:")) {
                    String aux = line.split("^Form params:\\s+")[1]
                    formParams.add(aux)
                    currentMultiline = "formParams"
                } else if (line.startsWith("Path params:")) {
                    System.out.println()
                } else if (line.startsWith("Headers:")) {
                    String aux = line.split("^Headers:\\s+")[1].replace("=", ":")
                    headers.add(aux)
                    currentMultiline = "headers"
                } else if (line.startsWith("Cookies:")) {
                    String aux = line.split("^Cookies:\\s+")[1].replace("=", ":")
                    headers.add(aux)
                    currentMultiline = "cookies"
                } else if (line.startsWith("Multiparts:")) {
                    currentMultiline = "multiparts"
                } else if (line.startsWith("Body:")) {
                    currentMultiline = "body"
                } else {
                    String aux = line.replace("\t", "").replace(" ", "")
                    switch (currentMultiline) {
                        case "headers":
                            aux = aux.replace("=", ":")
                            headers.add(aux)
                            break
                        case "queryParams":
                            queryParams.add(aux)
                            break
                        case "formParams":
                            formParams.add(aux)
                            break
                        case "cookies":
                            cookies.add(aux)
                            break
                        case "body":
                            body = body.concat(aux)
                            break
                    }
                }
            }
        }
        scanner.close()
        if (!headers.isEmpty()) {
            request.put("headers", headers)
        }
        if (!queryParams.isEmpty()) {
            request.put("queryParams", queryParams)
        }
        if (!formParams.isEmpty()) {
            request.put("formParams", formParams)
        }
        if (!cookies.isEmpty()) {
            request.put("cookies", cookies)
        }
        if (!body.isEmpty()) {
            request.put("body", body)
        }
        return generateCurl(request)
    }

    @SuppressWarnings("unchecked")
    static String generateCurl(HashMap<String, Object> request) {
        StringBuilder builder = new StringBuilder("curl --location ")
        builder.append("--request ").append(request.get("method")).append(" '").append(request.get("uri")).append("'")
        if (request.containsKey("headers")) {
            builder.append("\n")
            ArrayList<String> headers = (ArrayList<String>) request.get("headers");
            headers.forEach(current ->
                    builder.append("-H '").append(current).append("' ")
            )
        }
        if (request.containsKey("cookies")) {
            builder.append("\n")
            ArrayList<String> cookies = (ArrayList<String>) request.get("cookies");
            cookies.forEach(current ->
                    builder.append("--cookie '").append(current).append("' ")
            )
        }
        if (request.containsKey("formParams")) {
            builder.append("\n")
            ArrayList<String> formParams = (ArrayList<String>) request.get("formParams")
            formParams.forEach(current ->
                    builder.append("-d '").append(current).append("' ")
            );
        }
        if (request.containsKey("proxy")) {
            builder.append("\n")
            builder.append("--proxy '").append(request.get("proxy")).append("' ")
        }
        if (request.containsKey("body")) {
            builder.append("\n")
            builder.append("-d '").append(request.get("body")).append("' ")
        }
        return builder.toString()
    }

    static void main(String[] args) {
        String request = "Request method:     GET\n" +
                "Request URI:        https://jsonplaceholder.typicode.com/users?q1%3D1&q2%3D2\n" +
                "Proxy:              <none>\n" +
                "Request params:     <none>\n" +
                "Query params:       q1=1\n" +
                "                    q2=2\n" +
                "Form params:        <none>\n" +
                "Path params:        <none>\n" +
                "Headers:            U=MyAppName\n" +
                "                    Agent=MyAppName\n" +
                "                    Accept=*/*\n" +
                "                    Content-Type=text/plain; charset=ISO-8859-1\n" +
                "Cookies:            <none>\n" +
                "Multiparts:         <none>\n" +
                "Body:\n" +
                "{\n" +
                " \"password\": \"Testrest@123\",\n" +
                " \"userName\": \"test_rest\"\n" +
                "}\n"
        println getCurls(request)
    }

}