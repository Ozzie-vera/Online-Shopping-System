
package cs2365_project;

import java.sql.SQLException;
import java.util.*;

class Supplier{
    String User,Password,Name;
    
    public boolean Login(DBconnect sqlob, Scanner input){
        System.out.println("Username:");
        User = input.nextLine();
        System.out.println("Password:");
        Password = input.nextLine();
        System.out.println("Loggin in....");
        try{
           sqlob.pstmt = sqlob.conn.prepareStatement("SELECT Userid, Password, Name, "
                   + "AccountType FROM account WHERE Userid = ?"); 
           
           sqlob.pstmt.setString(1, User);

           sqlob.rs = sqlob.pstmt.executeQuery();
           
           sqlob.rs.next();
           String name = sqlob.rs.getString("Name");
           String password = sqlob.rs.getString("Password");
           String acctype = sqlob.rs.getString("AccountType");
           
           if(Password.equals(password)&& acctype.equals("Supplier")){
                System.out.println("Welcome Supplier: "+ name);
                return true;
           }
           else if(!acctype.equals("Supplier")){
               System.out.println("You are not a supplier");
               return false; 
           }
           else{
               System.out.println("Incorrect Password!!");
               return false;
           }
          
        }catch(SQLException e){
            System.out.println("Incorrect Password or UserID");
            return false;
        }
    }
    
    public boolean CreateAccount(DBconnect sqlob,Scanner input){
        System.out.println("Username:");
        User = input.nextLine();
        System.out.println("Password:");
        Password = input.nextLine();
        System.out.println("Name:");
        Name = input.nextLine();
        try{
           sqlob.pstmt = sqlob.conn.prepareStatement("INSERT INTO account(Userid,"
                   + " Password, Name, AccountType) VALUES(?,?,?,?)"); 
           
           sqlob.pstmt.setString(1, User);
           sqlob.pstmt.setString(2, Password);
           sqlob.pstmt.setString(3, Name);
           sqlob.pstmt.setString(4, "Supplier");
           
           sqlob.pstmt.execute();
           System.out.println("Account Created Successfully!");
           return true;
           
        }catch(SQLException e){
            System.out.println("That Username is already taken!");
            return false;
        }
    }
    
    public boolean AddItem(DBconnect sqlob,Scanner input ){
         System.out.println("Item:");
         String item = input.nextLine();
         System.out.println("Price:");
         String price = input.nextLine();
         System.out.println("stock:");
         String stock = input.nextLine();       
         
        try{
           sqlob.pstmt = sqlob.conn.prepareStatement("INSERT INTO catalog(Item,"
                   + " Price, Supplier,Stock) VALUES(?,?,?,?)"); 
           
           sqlob.pstmt.setString(1, item);
           sqlob.pstmt.setString(2, price);
           sqlob.pstmt.setString(3, User);
           sqlob.pstmt.setString(4, stock);
           
           
           sqlob.pstmt.execute();
           System.out.println("Item Added Successfully!");
           return true;
           
        }catch(SQLException e){
            System.out.println("Something went wrong, ERROR: " + e);
            return false;
        }
        
    }
        
    public void Getordersprocess(DBconnect sqlob){
        try {
            sqlob.pstmt = sqlob.conn.prepareStatement("SELECT id, Total, Card, "
                    + "AuthNum, Items FROM orders WHERE OrderStat = 'Ordered'");
            
            sqlob.rs = sqlob.pstmt.executeQuery();
            
            
            while(sqlob.rs.next()){
                String Id = sqlob.rs.getString("id");
                String card = sqlob.rs.getString("Card");
                String authnum = sqlob.rs.getString("AuthNum");
                String Items = sqlob.rs.getString("Items");
                
                
                System.out.println(
                        "Id("+Id+")"+
                        "\nCard: "+card+"\tAuth Num: "+authnum+
                        "\n"+ Items);
                System.out.println("");
                
                
            }

        } catch (SQLException e) {
            System.out.println(e);
        }
    }    
        
    public void Getordersshipment(DBconnect sqlob){
        try {
            sqlob.pstmt = sqlob.conn.prepareStatement("SELECT id, Total, Card, "
                    + "AuthNum, Items FROM orders WHERE OrderStat = 'Ready'");
            
            sqlob.rs = sqlob.pstmt.executeQuery();
            
           
            while(sqlob.rs.next()){
                String Id = sqlob.rs.getString("id");
                String card = sqlob.rs.getString("Card");
                String authnum = sqlob.rs.getString("AuthNum");
                String Items = sqlob.rs.getString("Items");
                
                
                System.out.println(
                        "Id("+Id+")"+
                        "\nCard: "+card+"\tAuth Num: "+authnum+
                        "\n"+ Items);
                System.out.println("");
                
              
            }

        } catch (SQLException e) {
            System.out.println(e);
        }
    }  
    
    public void processorder(DBconnect sqlob, Scanner input){
        
        System.out.println("Id: ");
        String Id = input.nextLine();
        
        try{

        sqlob.pstmt = sqlob.conn.prepareStatement("UPDATE orders SET OrderStat = 'Ready'"
                + " WHERE id = ?");
        
        
        sqlob.pstmt.setString(1, Id);
        sqlob.pstmt.executeUpdate();
     
        System.out.println("Order Processed.");
        
        }catch(SQLException e){
            System.out.println("Not and Option..." + e);
        }
        
    }
    
    public void confirmship(DBconnect sqlob, Scanner input){
        
        System.out.println("Id: ");
        String Id = input.nextLine();
        
        try{

        sqlob.pstmt = sqlob.conn.prepareStatement("UPDATE orders SET OrderStat = 'Shipped'"
                + " WHERE id = ?");
        
        
        sqlob.pstmt.setString(1, Id);
        sqlob.pstmt.executeUpdate();
     
        System.out.println("Order Shipped.");
        
        }catch(SQLException e){
            System.out.println("Not and Option...");
        }
        
    }
}

