package atm;

import java.sql.*;

public class SQLManage {
    Connection con;

    public SQLManage() throws SQLException {
        String usr = "root";
        String pass = "root";
        String url = "jdbc:mysql://localhost:3306/atm"; // Corrected connection URL
        con = DriverManager.getConnection(url, usr, pass);
    }

    public ResultSet check(String usr, String pass) throws SQLException {
        String str = "SELECT * FROM users WHERE card = ? AND pin = ?";
        PreparedStatement stm = con.prepareStatement(str);
        stm.setString(1, usr);
        stm.setString(2, pass);
        ResultSet rst = stm.executeQuery();
        return rst;
    }

    public void deposit(int amt, int id) throws SQLException {
        String str = "UPDATE users SET bal = bal + ? WHERE id = ?";
        PreparedStatement stm = con.prepareStatement(str);
        stm.setInt(1, amt);
        stm.setInt(2, id);
        stm.executeUpdate();

        int bal = balCheck(id);
        str = "INSERT INTO transactions (id, amount, stat, bal) VALUES (?, ?, 'dep', ?)";
        PreparedStatement stm2 = con.prepareStatement(str);
        stm2.setInt(1, id);
        stm2.setInt(2, amt);
        stm2.setInt(3, bal);
        stm2.executeUpdate();
    }

    public int withdraw(int amt, int id) throws SQLException {
        int bal = balCheck(id);
        if (bal >= amt) {
            String str = "UPDATE users SET bal = bal - ? WHERE id = ?";
            PreparedStatement stm = con.prepareStatement(str);
            stm.setInt(1, amt);
            stm.setInt(2, id);
            stm.executeUpdate();
            bal -= amt;
            str = "INSERT INTO transactions (id, amount, stat, bal) VALUES (?, ?, 'wit', ?)";
            PreparedStatement stm2 = con.prepareStatement(str);
            stm2.setInt(1, id);
            stm2.setInt(2, amt);
            stm2.setInt(3, bal);
            stm2.executeUpdate();
            return 1;
        }
        return 0;
    }

    public void pinchange(String pin, int id) throws SQLException {
        String str = "UPDATE users SET pin = ? WHERE id = ?";
        PreparedStatement stm = con.prepareStatement(str);
        stm.setString(1, pin);
        stm.setInt(2, id);
        stm.executeUpdate();
    }

    public int balCheck(int id) throws SQLException {
        String str = "SELECT bal FROM users WHERE id = ?";
        PreparedStatement stm = con.prepareStatement(str);
        stm.setInt(1, id);
        ResultSet rst = stm.executeQuery();
        rst.next();
        return rst.getInt("bal");
    }

    public ResultSet stmt(int id) throws SQLException {
        String str = "SELECT * FROM transactions WHERE id = ? ORDER BY transid DESC";
        PreparedStatement stm = con.prepareStatement(str);
        stm.setInt(1, id);
        ResultSet rst = stm.executeQuery();
        return rst;
    }

    public void adding(String card, String pin, String name, String bal) throws SQLException {
        String str = "INSERT INTO users (card, pin, uname, bal) VALUES (?, ?, ?, ?)";
        PreparedStatement stm = con.prepareStatement(str);
        stm.setString(1, card);
        stm.setString(2, pin);
        stm.setString(3, name);
        stm.setString(4, bal);
        stm.executeUpdate();
    }
}
