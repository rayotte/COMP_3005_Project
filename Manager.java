//Ryan Ayotte
//101073548
//3005 Final Project
//Sun Dec 19, 2021

import java.sql.*;              // For SQL Queries
import java.util.Scanner;       // To grab user input from command line

//Main class for the management menu
public class Manager {
    static Scanner scan = new Scanner(System.in);
    public Manager(){}
    
    //Function for managing the books in the bookstore
    public void manageBooks(Connection conn){
        String query = "";
        while(true){
            System.out.println("\nLibrary Management\n\n1: Restock Existing Book\n2: Add a New Book\n3: Remove a Book\n4: Add new publisher\nm: Back\n");

            System.out.print("Enter Choice: ");
            String choice = scan.nextLine();

            //Creates query for Restocking a book's quantity
            if(choice.equals("1")){
                System.out.print("ISBN of Book Being Restocked: ");
                String ISBN = scan.nextLine();
                System.out.print("New quantity: ");
                int newVal = scan.nextInt();
                query = "update stock set quantity = " + newVal + "where ISBN = '"+ISBN+"'";
                break;
            }

            //Routes owner to function for adding a new book to the store
            else if(choice.equals("2")){//Add new
                addNew(conn);
                continue;
            }

            //Creates query for deleting a book from the store
            else if(choice.equals("3")){
                System.out.print("ISBN of Book Being Removed: ");
                String ISBN = scan.nextLine();
                query = "delete from book where ISBN = '"+ISBN+"'";
                break;
            }

            //Routes owner to function for adding a new publisher to the store
            else if(choice.equals("4")){
                addNewPub(conn);
                continue;
            }
            //Go back
            else if (choice.equals("m"))
                return;
            
            //Quit application
            else
                System.out.println("Invalid");
        }
        try (
            Statement stmt = conn.createStatement();
        ){
            stmt.executeUpdate(query);
            manageBooks(conn);

        }
        catch (Exception sqle){
            System.out.println("Exception : " + sqle);
        }
    }

    //Function for adding a new book, prompting the user for values
    public void addNew(Connection conn){
        System.out.print("ISBN of New Book: ");
        String ISBN = scan.nextLine();

        System.out.print("Title of New Book: ");
        String title = scan.nextLine();

        System.out.print("Author of New Book: ");
        String author = scan.nextLine();

        System.out.print("Genre of New Book: ");
        String genre = scan.nextLine();

        System.out.print("Publisher of New Book: ");
        String pub = scan.nextLine();

        System.out.print("Price of New Book: ");
        double price = scan.nextDouble();

        System.out.print("Page count of New Book: ");
        int pages = scan.nextInt();

        System.out.print("Pub percent of New Book: ");
        double pub_per = scan.nextDouble();

        System.out.print("Current Stock of New Book: ");
        int stock = scan.nextInt();


        try (
            PreparedStatement pStmt = conn.prepareStatement(
                    "insert into book values(?,?,?,?,?,?,?,?)");
            PreparedStatement pStmt2 = conn.prepareStatement(
                    "insert into stock values(?,?)");
            PreparedStatement pStmt3 = conn.prepareStatement(
                "insert into publishes values(?,?)");
        ){
            pStmt.setString(1,ISBN);
            pStmt.setString(2,title);
            pStmt.setString(3,author);
            pStmt.setString(4,genre);
            pStmt.setString(5,pub);
            pStmt.setDouble(6,price);
            pStmt.setInt(7,pages);
            pStmt.setDouble(8,pub_per);
            pStmt.executeUpdate();
            pStmt2.setString(1, ISBN);
            pStmt2.setInt(2, stock);
            pStmt2.executeUpdate();
            pStmt3.setString(1, ISBN);
            pStmt3.setString(2, pub);
            pStmt3.executeUpdate();
            System.out.println("\nNew book successfully added\n");
            manageBooks(conn);

        }
        catch (Exception sqle){
            System.out.println("Exception : " + sqle);
        }

    }

    //Function for displaying publisher information
    public void pubInfo(Connection conn){
        String query = "";

        System.out.print("Enter Publisher Name: ");
        String pub = scan.nextLine();
        query = "select * from publisher where name = '"+pub+"'";
        System.out.println("\nPublisher Details\n-----------------");

        try (
            Statement stmt = conn.createStatement();
        ){
            ResultSet rset = stmt.executeQuery(query);

            while(rset.next()){
                System.out.println("ID: " + rset.getString("ID"));
                System.out.println("Name: " + rset.getString("name"));
                System.out.println("Email: " + rset.getString("email"));
                System.out.println("Phone: " + rset.getString("phone"));
                System.out.println("address: " + rset.getString("address"));
                System.out.println("Bank Info: " + rset.getString("bank_info"));
            }

        }
        catch (Exception sqle){
            System.out.println("Exception : " + sqle);
        }
    }

    //Function for adding a new publisher, prompting the user for values
    public void addNewPub(Connection conn){
        System.out.print("ID of New Publisher: ");
        String ID = scan.nextLine();

        System.out.print("Name of New Publisher: ");
        String name = scan.nextLine();

        System.out.print("Email of New Publisher: ");
        String email = scan.nextLine();

        System.out.print("Phone number (no dashes/spaces) of New Publisher: ");
        String phone = scan.nextLine();

        System.out.print("Address of New Publisher: ");
        String address = scan.nextLine();

        System.out.print("Bank Info of New Publisher: ");
        String bank = scan.nextLine();


        try (
            PreparedStatement pStmt = conn.prepareStatement(
                    "insert into publisher values(?,?,?,?,?,?)");
        ){
            pStmt.setString(1,ID);
            pStmt.setString(2,name);
            pStmt.setString(3,email);
            pStmt.setString(4,phone);
            pStmt.setString(5,address);
            pStmt.setString(6,bank);
            pStmt.executeUpdate();
            System.out.println("\nNew Publisher successfully added\n");
            manageBooks(conn);

        }
        catch (Exception sqle){
            System.out.println("Exception : " + sqle);
        }

    }
    
}
