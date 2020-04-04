/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cs2365_project;

import java.sql.*;

class DBconnect { //Class for msql Database, used to pass object to methods in classes

    public Connection conn;
    public ResultSet rs;
    public PreparedStatement pstmt;

    DBconnect() {
        try {
            conn = DriverManager.getConnection(
                    "jdbc:mysql://**.***.***.***:***/OSS", // ip address of database
                    "root", "*****");
        } catch (SQLException e) {
            System.err.println(e);
        }
    }

    public void GetCustomers() { // used by admin
        try {
            this.pstmt = this.conn.prepareStatement("SELECT Userid, Password, Name, "
                    + "AccountType FROM account WHERE AccountType = 'Customer'");

            this.rs = this.pstmt.executeQuery();

            while(this.rs.next()){
                String User = this.rs.getString("Userid");
                String Pass = this.rs.getString("Password");
                String Name = this.rs.getString("Name");
                String Acc = this.rs.getString("AccountType");

                System.out.println(User + "\t\t" + Pass + "\t\t" + Name +
                        "\t\t" + Acc);
            }

        } catch (SQLException e) {
            System.out.println(e);
        }
    }
    public void GetSuppliers(){// used by admin
        try {
            this.pstmt = this.conn.prepareStatement("SELECT Userid, Password, Name, "
                    + "AccountType FROM account WHERE AccountType = 'Supplier'");

            this.rs = this.pstmt.executeQuery();

            while(this.rs.next()){
                String User = this.rs.getString("Userid");
                String Pass = this.rs.getString("Password");
                String Name = this.rs.getString("Name");
                String Acc = this.rs.getString("AccountType");

                System.out.println(User + "\t\t\t" + Pass + "\t\t" + Name +
                        "\t\t" + Acc);
            }

        } catch (SQLException e) {
            System.out.println(e);
        }
    }


}
