
package cs2365_project;

import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;


class Customer{
    String User, Password,Name,Phonenum,Cardnum,Acctype,Cart;
    double Total;
    
   
    public boolean Login(DBconnect sqlob,Scanner input){
        Total = 0.0;
        Cart = "";
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
           
           if(Password.equals(password) && acctype.equals("Customer")){
                System.out.println("Welcome!: "+ name);
                return true;
           }
           else if(!acctype.equals("Customer")){
               System.out.println("You are not a Customer");
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
        System.out.println("Phone Number:");
        Phonenum = input.nextLine();
        System.out.println("Card Number:");
        Cardnum = input.nextLine();

        
        try{
            if (Phonenum.length() > 10) {
                throw new NumberFormatException();
                                }
            if (Cardnum.length() > 16) {
                throw new NumberFormatException();
                                }
            
           sqlob.pstmt = sqlob.conn.prepareStatement("INSERT INTO account(Userid,"
                   + "Password, Name, PhoneNumber,CreditCardNumber, "
                   + "AccountType) VALUES(?,?,?,?,?,?)"); 
           
           sqlob.pstmt.setString(1, User);
           sqlob.pstmt.setString(2, Password);
           sqlob.pstmt.setString(3, Name);
           sqlob.pstmt.setString(4, Phonenum);
           sqlob.pstmt.setString(5, Cardnum);
           sqlob.pstmt.setString(6, "Customer");
           
           sqlob.pstmt.execute();
           System.out.println("Account Created Successfully!");
           return true;
           
           
        }catch(SQLException e){
            System.out.println("That Username is already taken!");
            return false;
        }
    }
    
    public void AddtoCart(DBconnect sqlob,Scanner input){
        

        System.out.println("Id Number:");
        String Id = input.nextLine();
        System.out.println("Quantity:");
        double Quant = Double.parseDouble(input.nextLine());
        try{

        sqlob.pstmt = sqlob.conn.prepareStatement("SELECT Item, Price"
                + " FROM catalog WHERE id = ?");
        
        
        sqlob.pstmt.setString(1, Id);
        sqlob.rs = sqlob.pstmt.executeQuery();//fix
        
        sqlob.rs.next();
        String item = sqlob.rs.getString("Item");
        Double price = sqlob.rs.getDouble("Price");
        
        Cart = Cart + "Q: "+Quant + "\tItem:" + item + "\t\tPrice: $"+ 
                price + "\tTotal: $" + price*Quant;
        Total = Total + price*Quant;
        
        }catch(SQLException e){
            System.out.println("Not and Option...");
        }

    }
    
    public void ConfirmOrder(DBconnect sqlob,Scanner input, Boolean bankauth){
        
        //Check to see if cart is empty
        if(this.Total <= 0){
            System.out.println("There is nothing in your Cart! "
                    + "\nAdd to Items to cart to proceed");
            return;
        }
        // else proceed
        
        // Bank Authorization 
        System.out.println("Authorizing Credit Card Info...");
        
        try {// Simulating time wait for authorizing card
            TimeUnit.SECONDS.sleep(10);
        } catch (InterruptedException ex) {
            Logger.getLogger(Customer.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if(!bankauth){
            System.out.println("Card Was not Authorized, Use another card: ");
            do{
            try{
               Cardnum = input.nextLine(); 
               
               if (Cardnum.length() > 16) {
                throw new NumberFormatException();
                                }
               break;
            }catch(NumberFormatException e){
                System.out.println("Not A Valid Card number!!");
            }
            
            }while(true);
            
        }else{
            System.out.println("Bank Authorization went through!");
        }
        
        Random random = new Random();
        int authnum = 100000 +(int)(random.nextFloat()*899900);
        
        System.out.println("Processing order...");
        
       try{
           sqlob.pstmt = sqlob.conn.prepareStatement("INSERT INTO orders(OrderStat,"
                   + " Total, Card, Items, AuthNum, Customer) VALUES(?,?,?,?,?,?)"); 
           
           sqlob.pstmt.setString(1, "Ordered");
           sqlob.pstmt.setString(2, Double.toString(Total));
           sqlob.pstmt.setString(3, Cardnum);
           sqlob.pstmt.setString(4, Cart);
           sqlob.pstmt.setString(5,Integer.toString(authnum) );
           sqlob.pstmt.setString(6, User);
           
           sqlob.pstmt.execute();
           System.out.println(Cart);
           System.out.println("Total: $" + Total);
           System.out.println("Card: " + Cardnum);
           System.out.println("Authorization Num: $" + authnum);
           System.out.println("Status: Ordered");
           System.out.println("Order Processed Sucessfully!");
          
        }catch(SQLException e){
            System.out.println("That Username is already taken!");
            
        }
        
    }

    public void ViewCustOrder(DBconnect sqlob,Scanner input){
         try {
            sqlob.pstmt = sqlob.conn.prepareStatement("SELECT OrderStat, Total, Card, "
                    + "AuthNum, Items FROM orders WHERE Customer = ?");
            
            sqlob.pstmt.setString(1, User);
            sqlob.rs = sqlob.pstmt.executeQuery();
            
            int i = 1;
            while(sqlob.rs.next()){
                String orderstat = sqlob.rs.getString("OrderStat");
                String card = sqlob.rs.getString("Card");
                String authnum = sqlob.rs.getString("AuthNum");
                String Items = sqlob.rs.getString("Items");
                
                
                System.out.println("Order: "+i+
                        "\nStatus: "+ orderstat+
                        "\nCard: "+card+"\tAuth Num: "+authnum+
                        "\n"+ Items);
                System.out.println("");
                
                i++;
            }

        } catch (SQLException e) {
            System.out.println(e);
        }
    }
    
    public void getcatalog(DBconnect sqlob){
        try {
            sqlob.pstmt = sqlob.conn.prepareStatement("SELECT id, Item, Price,Stock, "
                    + "Supplier FROM catalog");
            
            sqlob.rs = sqlob.pstmt.executeQuery();
            
            while(sqlob.rs.next()){
                String id = sqlob.rs.getString("id");
                String item = sqlob.rs.getString("Item");
                String price = sqlob.rs.getString("Price");
                String stock = sqlob.rs.getString("Stock");
                String supp = sqlob.rs.getString("Supplier");
                
                System.out.println("(" + id + ")" + item );
                System.out.println("Price: " + price +  "\t\tStock: " 
                        + stock +  "\t\tSupplier: " + supp);
                System.out.println("");
            }

        } catch (SQLException e) {
            System.out.println(e);
        }
    }
}

