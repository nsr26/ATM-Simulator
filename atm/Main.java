package atm;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws InterruptedException, SQLException {
        atm.Login login = new atm.Login();
        login.loginView();
    }
}