package common

class Accounts {
    private static HashMap<String, HashMap<String, String>> accounts

    static String getAccountEmail(String userName) {
        return accounts[userName]["email"]
    }

    static String getAccountPassword(String userName) {
        return accounts[userName]["password"]
    }

    static void setAccounts(HashMap<String, HashMap<String, String>> newAccounts) {
        accounts = newAccounts
    }

}
