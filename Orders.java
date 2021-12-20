//Ryan Ayotte
//101073548
//3005 Final Project
//Sun Dec 19, 2021

import java.sql.*;              // For SQL Queries
import java.util.Scanner;       // To grab user input from command line

//Class for managing order calls
public class Orders {
    public Orders(){};
    static Scanner scan = new Scanner(System.in);
    static int orderNumber = 40001;

    //Function to add books to the user's order
    public void addToOrder(Connection conn){
        if(Bookstore.orderBooks.isEmpty())
            genOrdNum(conn);
        while(true){
            System.out.print("Enter title of book to add to order (or (m)Back): ");
            String choice = scan.nextLine();
            if (choice.equals("m"))
                return;

            //Ensures that the book is in stock
            if(checkForOrder(conn, choice)){
                Bookstore.orderBooks.add(choice);
                System.out.println("Book added to order\n");
            }
            else
                System.out.println("Book not found or out of stock");
        }

    }


    //Function to ensure the book is in stock, quering the database
    public boolean checkForOrder(Connection conn, String title){
        boolean found = false;
        try (
            Statement stmt = conn.createStatement();
        ){
            ResultSet rset = stmt.executeQuery("select * from book natural join stock where title = '"+title+"' and quantity > 1");
            if(rset.next()){
                found = true;
                Bookstore.orderCost += rset.getInt("price");
            }

        }
        catch (Exception sqle){
            System.out.println("Exception : " + sqle);
        }
        return found;
        
    }

    //Function to place the order
    public void placeOrder(Connection conn){
        String bookList = "";
        for (String s : Bookstore.orderBooks){
            bookList+= s + ", ";
        }
        try (
            //Adds the book to the orders table as well as the place_order table
            PreparedStatement pStmt = conn.prepareStatement("insert into orders values(?,?,?,?,?,?)");
            PreparedStatement pStmt2 = conn.prepareStatement("insert into place_order values(?,?)");
            Statement stmt = conn.createStatement();
        ){
            ResultSet rset = stmt.executeQuery("select ship_address, bill_address from client where ID = '"+Bookstore.currentUserID+"'");
            rset.next();

            pStmt.setString(1,Integer.toString(orderNumber));
            pStmt.setDouble(2,Bookstore.orderCost);
            pStmt.setString(3,rset.getString("ship_address"));
            pStmt.setString(4,rset.getString("bill_address"));
            pStmt.setString(5,bookList);
            pStmt.setString(6,Bookstore.currentUserID);
            pStmt.executeUpdate();

            pStmt2.setString(1, Bookstore.currentUserID);
            pStmt2.setString(2, Integer.toString(orderNumber));
            pStmt2.executeUpdate();
            System.out.println("\nOrder Placed\n");
            Bookstore.orderBooks.clear();
            Bookstore.orderCost = 0;

            //Adjust the stock of the books in the order
            adjustStock(conn);
        }
        catch (Exception sqle){
            System.out.println("Exception : " + sqle);
        }

    }

    //Generates a unique order number that is not already 
    public void genOrdNum(Connection conn){
        try (
            Statement stmt = conn.createStatement();
        ){
            ResultSet rset = stmt.executeQuery("select ID from orders where ID = '"+orderNumber+"'");
            if(rset.next()){
                orderNumber+=1;
                genOrdNum(conn);
            }   
            else
                return;
        }
        catch (Exception sqle){
            System.out.println("Exception : " + sqle);
        }

    }

    //Function to adjust the stock (table) of the bokks from the order
    public void adjustStock(Connection conn){
        try (
            Statement stmt = conn.createStatement();
            Statement stmt2 = conn.createStatement();
            PreparedStatement pStmt = conn.prepareStatement("insert into order_book values(?,?)");
        ){
            for (String s : Bookstore.orderBooks){
                ResultSet rSet = stmt.executeQuery("select ISBN from book where title = '"+s+"'");
                rSet.next();
                stmt2.executeUpdate("update stock set quantity = quantity-1 where ISBN = '"+rSet.getString("ISBN")+"'");

                pStmt.setString(1, rSet.getString("ISBN"));
                pStmt.setString(2, Integer.toString(orderNumber));
            }
        }
        catch (Exception sqle){
            System.out.println("Exception : " + sqle);
        }
    }

    //Views for tracking orders for the user
    public void trackOrder(Connection conn){
        String query = "";
        while(true){
            System.out.print("\n1: Track current order\n2: See your Past Orders\nm: Back\nq: Quit Application): ");
            String choice = scan.nextLine();

            //Get current order details if there is an order that exists
            if (choice.equals("1")){
                if(Bookstore.orderBooks.isEmpty()){
                    System.out.println("\nNo orders currently in progress");
                    continue;
                }
                else{
                    System.out.println("\nCurrent order number: " + orderNumber+"\n Currently in progress");
                }
            }

            //Query for getting past orders
            else if (choice.equals("2")){
                query = "select * from orders where client_id = '"+Bookstore.currentUserID+"'";
                break;
            }

            //Go Back
            else if (choice.equals("m"))
                return;

            //Quit application
            else if(choice.equals("q")){
                System.exit(0);
            }
            else{
                System.out.println("Invalid Option");
            }
        }
        
        try (
            Statement stmt = conn.createStatement();
        ){
            ResultSet rSet = stmt.executeQuery(query);
            System.out.println("\nYour Current Orders: \n----------------");
            while(rSet.next()){
                System.out.println("Order number: " + rSet.getString("ID"));
                System.out.println("Order cost: $" + rSet.getString("total_cost"));
                System.out.println("Order Items: " + rSet.getString("book_list"));
                System.out.println("Order Shipping Address: " + rSet.getString("ship_address") + "\n");
            }
        }
        catch (Exception sqle){
            System.out.println("Exception : " + sqle);
        }
    }
    
}
