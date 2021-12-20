//Ryan Ayotte
//101073548
//3005 Final Project
//Sun Dec 19, 2021

//Disclaimer - Code for connecting to database and running queries (try catch loop) was taken from class notes

import java.sql.*;              // For SQL Queries
import java.util.Scanner;       // To grab user input from command line
import java.util.ArrayList;     // To store the courses

//Main class of the whole project
public class Bookstore{
    static Scanner scan = new Scanner(System.in);
    static String currentUserID = "";

    //Calls for other classes
    static Authenticate loginPage = new Authenticate();
    static Catalog catalog = new Catalog();
    static Manager managerPage = new Manager();
    static boolean loggedIn = false;
    static ArrayList<String> orderBooks = new ArrayList<String>();
    static double orderCost = 0;

    // Main menu for authorized clients
    public static void client(Scanner scan, Connection conn){

        //Loops through options for client until they either logout or quit the application
        while(true){

            //Check to see if they have been logged in already - redendency for cients backing out from a further menu
            if(!loggedIn){

                //If they are not logged in, call the authorization method 
                if (!loginPage.login(conn, "client"))
                    System.out.println("\nInvalid credentials\n");
            }
            else{
                //Main client menu
                System.out.println("\nMain Menu\n---------\n1: Browse/order Books\n2: View current Order\nm: Logout\nq: Quit Application\n");

                System.out.print("Enter Choice: ");
                String choice = scan.nextLine();

                //Directs user to the catalog to browse and/or order books from the bookstore
                if(choice.equals("1"))
                    catalog.browseBooks(conn);
                
                // Displays the user's current order if there is one in progress
                else if(choice.equals("2")){
                    if(orderBooks.isEmpty()){
                        System.out.println("\nNo current orders\n");
                        continue;
                    }
                    System.out.println("\nCurrent books in order\n---------");
                    for (String s : orderBooks){
                        System.out.println(s);
                    }
                    System.out.println("Current cost: $" + orderCost + "\n");
                }
                // Logs the user out of the bookstore
                else if(choice.equals("m")){
                    currentUserID = "";
                    loggedIn = false;
                    orderBooks.clear();
                    break;
                }
                //Exits the application
                else if(choice.equals("q")){
                    System.exit(0);
                }
                else{
                    System.out.println("Invalid Option");
                }
            }
        }
    }
    
    //Main menu for authorized owners
    public static void owner(Scanner scan, Connection conn){
        if(!loggedIn){
            if (!loginPage.login(conn, "owner"))
                System.out.println("\nInvalid credentials\n");
        }
        while(true){
            System.out.println("\nMain Menu\n---------\n1: Manage Books\n2: View Publisher Info\n3: Store Sales Reports\nm: Logout\nq: Quit Application\n");

            System.out.print("Enter choice: ");
            String choice = scan.nextLine();

            //Route owner to management page
            if(choice.equals("1")){
                managerPage.manageBooks(conn);
            }

            //Route pwner to publisher info function
            else if(choice.equals("2")){
                managerPage.pubInfo(conn);
            }

            //Not implemented
            else if(choice.equals("3")){
                System.out.println("Currently not implemented");
            }

            //Logs owner out of bootstore
            else if(choice.equals("m")){
                currentUserID = "";
                loggedIn = false;
                break;
            }

            //Quit program
            else if(choice.equals("q")){
                System.exit(0);
            }
            else{
                System.out.println("Invalid Option");
            }
        }
    }

    //Main funciton driving everything. Starts user off at the login screen and connections to the database
    public static void main(String[] args){
        System.out.println("\nWelcome to the Bookstore!\n-------------------------\n");
        try (
            Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Bookstore", "postgres", "0027");
        ) {
            while(true){
                System.out.println("Login\n-------");
                System.out.println("1: Owner\n2: Client\nq: Quit\n");

                System.out.print("choice: ");
                String choice = scan.nextLine();

                //Directs user to owner login screen / main menu
                if(choice.equals("1"))
                    owner(scan, conn);

                //Directs user to client login screen / main menu
                else if(choice.equals("2"))
                    client(scan, conn);

                //Quits application
                else if(choice.equals("q"))
                    break;  
                else
                    System.out.println("Invalid Option\n");
            }    
            
        }

        //Returns an exception if the database cannot be contacted -- provided in class
        catch (Exception sqle)
        {
            System.out.println("Exception : " + sqle);
        }
    }
}