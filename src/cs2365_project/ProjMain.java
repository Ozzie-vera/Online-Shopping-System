package cs2365_project;

import java.util.*;

public class ProjMain {

    public static void main(String[] args) {

        boolean BankAuth = true;// This will simulate the bank authorization process
                                // if true, bank authorization will always occurs
                                // if false, bank authorization will fail once.
        
        DBconnect connection = new DBconnect();

        Scanner input = new Scanner(System.in);

        Customer customer = new Customer();
        Supplier supplier = new Supplier();

        //command line interface in place of gui  all screens can be made in a class
        System.out.println("Welcome to the Online Shopping System");

//--------First Screen Login or create Account or exit app----------------------
        String User, Password;
        boolean nextscreen = false;
        int screenopt = 100;
        do {
            System.out.println("(1)Login (2)Create Customer Account "
                    + "(3)Create Supplier Account (0)Exit");
            try {
                screenopt = Integer.parseInt(input.nextLine());
                
                switch (screenopt) {
                    case 0:
                        break;
                    case 1:
                        nextscreen = true;
                        break;
                    case 2:
                        do {
                            try {
                                
                             if (customer.CreateAccount(connection, input)){
                                 break;
                             }

                            } catch (NumberFormatException e) {
                                System.out.println("Not a valid Number");
                            }

                        } while (true);
                        break;
                    case 3:
                        do {
          
                            if (supplier.CreateAccount(connection,input)) {
                                break;
                            }
                            break;
                            
                        } while (true);
                        break;
                    default:
                        throw new NumberFormatException();
                }
            } catch (NumberFormatException e) {
                System.out.println("No Option for that..");
            }

            if (nextscreen || screenopt == 0) {
                break;
            }

        } while (true);

//--------Second Screen Who to login as Supplier, Customer, (Hidden) Bank,Admin-
        int accounttype = 100;
        boolean loginsuccess = false;
        while (nextscreen) {

            System.out.println("Login as (1)Customer (2)Supplier (0)Exit");

            try {
                accounttype = Integer.parseInt(input.nextLine());
                
                switch (accounttype) {
                    
                    case 0:
                        break;
                    case 1:
                        loginsuccess = customer.Login(connection,input);
                        break;
                    case 2:
                        loginsuccess = supplier.Login(connection,input);
                        break;
                    case 3:// could make admin a class??-information hiding
                        System.out.println("Username:");
                        User = input.nextLine();
                        System.out.println("Password:");
                        Password = input.nextLine();
                        System.out.println("Loggin in....");
                        if (User.equals("Admin") && 
                                Password.equals("password")) {
                            loginsuccess = true;
                        }
                        else{
                            System.out.println("NOT ADMIN");
                        }
                        break;
                    
                    default:
                        throw new NumberFormatException();
                }

            } catch (NumberFormatException e) {
                System.out.println("No Option for that..");
            }

            if (loginsuccess || accounttype == 0) {
                break;
            }
        }
    int custopt=100; 
    switch(accounttype){
//-----Third Screen for customer View catalog, view orders, logout--------------Customer     
        case 1:
            
            do{
               System.out.println("(0)Logout (1)View Catalog (2) View your Orders ");
               try {
                    screenopt = Integer.parseInt(input.nextLine());
                    switch(screenopt){
                        case 0:
                            break;
                        case 1:// View Catalog
                            customer.getcatalog(connection);
                            do{
                                System.out.println("(0)Exit (1)Add item to Cart "
                                    + "(2) View Cart "); 
                                try{
                                  custopt = Integer.parseInt(input.nextLine());
                                  
                                  switch(custopt){
                                      case 0:
                                          break;
                                      case 1: // Add to cart
                                          customer.AddtoCart(connection, input);
                                          break;
                                      case 2:// View Cart
                                          System.out.println(customer.Cart);
                                          System.out.println("Total: $" + customer.Total);
                                          
                                          System.out.println("(0)Exit (1)ConfirmOrder"); 
                                          try{
                                             
                                             if(Integer.parseInt(input.nextLine()) == 1){
                                                 
                                                 customer.ConfirmOrder(connection, input, BankAuth);
                                             }
                                             
                                          }catch(NumberFormatException e){
                                              System.out.println("No Option for that...");
                                          }
                                          
                                
                                          break;
                                      default:
                                          throw new NumberFormatException();
                                          
                                }
                                }catch(NumberFormatException e){
                                    System.out.println("No Option for that...");
                                }
                                
                                if(custopt == 0){
                                    break;
                                }
 
                            }while(true);
                            
                            break;
                        case 2://view orders
                            customer.ViewCustOrder(connection, input);
                            break;
                        default:
                            throw new NumberFormatException();
                    }
               }catch(NumberFormatException e){
                   System.out.println("No Option for that...");
               }
               
               if(screenopt == 0){
                   break;
               }
            }while(loginsuccess);
            break;
//-----Third Screen for Supplier additem, process order, Confirm Shipment, Logout-Supplier           
        case 2:
            
            do{
               System.out.println("(0)Logout (1)Add Item (2)Process Orders "
                       + "(3)Confirm shipment ");
               try {
                    screenopt = Integer.parseInt(input.nextLine());
                    switch(screenopt){
                        case 0:
                            break;
                        case 1:// Add item
                            do{
                                if(supplier.AddItem(connection, input)){break;}
                            }while(true );
                            break;
                        case 2: //process order
                            supplier.Getordersprocess(connection);
                            System.out.println("(0)Exit (1)Process Order"); 
                            try{            
                                if(Integer.parseInt(input.nextLine()) == 1){
                                    supplier.processorder(connection, input);
                                }
                                             
                            }catch(NumberFormatException e){
                            System.out.println("No Option for that...");
                            }
                            
                            break;
                        case 3: // Confirm shipment
                            supplier.Getordersshipment(connection);
                            System.out.println("(0)Exit (1) Confirm Shipment"); 
                            try{            
                                if(Integer.parseInt(input.nextLine()) == 1){
                                    supplier.confirmship(connection, input);
                                }
                                             
                            }catch(NumberFormatException e){
                            System.out.println("No Option for that...");
                            }
                            break;
                        default:
                            throw new NumberFormatException();
                    }
               }catch(NumberFormatException e){
                   System.out.println("No Option for that...");
               }
               
               if(screenopt == 0){
                   break;
               }
            }while(loginsuccess);
            
            break;
//------Third Screen for Admin, list accounts, catalog, orders------------------Admin           
        case 3:
            System.out.println("ADMIN PAGE");
            do{
               System.out.println("List: (1)Customers (2)Suppliers "
                       + " (0)Exit");
               try {
                    screenopt = Integer.parseInt(input.nextLine());
                    switch(screenopt){
                        case 0:
                            break;
                        case 1:// view customers
                            connection.GetCustomers();
                            break;
                        case 2://view suppliers
                            connection.GetSuppliers();
                            break;
                        default:
                            throw new NumberFormatException();
                    }
               }catch(NumberFormatException e){
                   System.out.println("No Option for that...");
               }
               
               if(screenopt == 0){
                   break;
               }
            }while(loginsuccess);
            
            break;
    }
//End of third Screen  
    
    
    }
}


