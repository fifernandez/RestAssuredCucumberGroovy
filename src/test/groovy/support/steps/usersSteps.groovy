package support.steps

import org.junit.Assert
import io.cucumber.groovy.EN
import common.Global
this.metaClass.mixin(EN)


And(~/^I see the "users" response contains the user: "(.*)"$/) { String userToSearch ->
    List<String> users = Global.response.then().extract().path("username")
    boolean userFound = false
    users.find { String currentUser ->
        if (currentUser == userToSearch) {
            userFound = true
            return true
        }
    }
    Assert.assertTrue("User '${userToSearch}' not found in the users response", userFound)
}

And(~/^in the "users" response there is a user with the zipcode: "([^"]*)"$/) { String zipcodeWanted ->
    List users = Global.response.then().extract().body().jsonPath().getList(".")
    boolean zipcodeFound = false
    users.find{ currentUser ->
        if (currentUser.address.zipcode == zipcodeWanted) {
            zipcodeFound = true
            return true
        }
    }
    Assert.assertTrue("Zipcode '${zipcodeWanted}' not found in the users response", zipcodeFound)
}