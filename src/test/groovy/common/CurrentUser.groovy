package common

import io.restassured.response.Response

class CurrentUser {
    static Response userData;
    static String account;

    static void logUser(String userAccount, Response LoginResponse) {
        account = userAccount
        userData = LoginResponse
    }

    static String getParameter(String param) {
        return userData.then().extract().body().path("param")
    }
}
