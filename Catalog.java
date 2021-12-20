//Ryan Ayotte
//101073548
//3005 Final Project
//Sun Dec 19, 2021

import java.sql.*;              // For SQL Queries
import java.util.Scanner;       // To grab user input from command line

//Class for client catalog and directing order requests
public class Catalog {
    static Scanner scan = new Scanner(System.in);
    Orders order = new Orders();

    public Catalog(){}

    public void browseBooks(Connection conn){
        String query = "";

        while(true){
            System.out.println("\nLibrary Catalog\n\n1: Browse by Author\n2: Browse by Genre\n3: Browse by Publisher\n4: Detailed Book Info\n5: Add Book(s) to Order\n6: Place order\n7: Track Order\nm: Back\n");

            System.out.print("Enter Choice: ");
            String choice = scan.nextLine();

            //Creates query for finding books by a given author
            if(choice.equals("1")){
                System.out.print("Enter Author Name: ");
                String author = scan.nextLine();
                query = "select ISBN, title, quantity from book natural join stock where author = '"+author+"'";
                System.out.println("\nBooks by " + author + "\n--------------");
                break;
            }

            //Creates query for finding books of a given genre
            else if(choice.equals("2")){
                System.out.print("Enter Genre: ");
                String genre = scan.nextLine();
                query = "select ISBN, title, quantity from book natural join stock where genre = '"+genre+"'";
                System.out.println("\n" + genre + " books\n--------------");
                break;
            }

            //Creates query for finding books by a given publisher
            else if(choice.equals("3")){
                System.out.print("Enter Publisher Name: ");
                String publisher = scan.nextLine();
                query = "select ISBN, title, quantity from book natural join stock where publisher = '"+publisher+"'";
                System.out.println("\nBooks published by " + publisher + "\n--------------");
                break;
            }

            //Routes user to function to get details about a given book
            else if(choice.equals("4")){
                System.out.println("\nDetails\n--------------");
                bookDetails(conn);
                continue;
            }
            //Routes user to function to add books to their order
            else if(choice.equals("5")){
                order.addToOrder(conn);
                continue;
            }

            //Routes user to function to place/confirm their order if they have an order in progress
            else if(choice.equals("6")){
                if(Bookstore.orderBooks.isEmpty()){

                }
                order.placeOrder(conn);
                continue;
            }

            //Routes user to function to track orders
            else if(choice.equals("7")){
                order.trackOrder(conn);
                continue;
            }

            //Go back
            else if (choice.equals("m"))
                return;
            else
                System.out.println("Invalid");
        }

        //Execure query and print results
        try (
            Statement stmt = conn.createStatement();
        ){
            ResultSet rset = stmt.executeQuery(query);

            while(rset.next()){
                System.out.println("Title: " + rset.getString("title") + ", ISBN: " + rset.getString("ISBN") + ", Stock: " + rset.getString("quantity"));
            }
            browseBooks(conn);

        }
        catch (Exception sqle){
            System.out.println("Exception : " + sqle);
        }

    }

    //Function for getting a book's details
    public void bookDetails(Connection conn){
        String query = "";

        System.out.print("Enter Book Name: ");
        String title = scan.nextLine();
        query = "select * from book natural join stock where title = '"+title+"'";
        try (
            Statement stmt = conn.createStatement();
        ){
            ResultSet rset = stmt.executeQuery(query);

            while(rset.next()){
                System.out.println("Title: " + rset.getString("title"));
                System.out.println("ISBN: " + rset.getString("ISBN"));
                System.out.println("Author: " + rset.getString("author"));
                System.out.println("Genre: " + rset.getString("genre"));
                System.out.println("Publisher: " + rset.getString("publisher"));
                System.out.println("Price: " + rset.getString("price"));
                System.out.println("Page Count: " + rset.getString("page_count"));
                System.out.println("Stock: " + rset.getString("quantity"));
            }

        }
        catch (Exception sqle){
            System.out.println("Exception : " + sqle);
        }

    }    
}
