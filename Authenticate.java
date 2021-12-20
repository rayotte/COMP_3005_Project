//Ryan Ayotte
//101073548
//3005 Final Project
//Sun Dec 19, 2021

import java.sql.*;              // For SQL Queries
import java.util.Scanner;       // To grab user input from command line

//Class used to authenticate user
public class Authenticate {
    static Scanner scan = new Scanner(System.in);

    //Empty constructor
    public Authenticate(){}

    public boolean login(Connection conn, String user){

        //Takes in username and password from user (masking password) and checks to see that they are in the database
        System.out.print("Enter username: ");
        String username = scan.nextLine();

        //Password masking taken from https://stackoverflow.com/questions/40618193/hide-password-input-on-console-window
        char[] passwd = System.console().readPassword("[%s]", "Enter Password: ");
        
        String password = "";
        boolean found = false;

        //Convert password to string
        for (char c : passwd){
            password += c;
        }

        try (
            Statement stmt = conn.createStatement();
        ){
            ResultSet rset = stmt.executeQuery(
                "select * from " + user + " where username = '"+username+"' and password = '"+password+"'");

            //If the user is in the database, mark them as found and store the current user's id
            if(rset.next()){
                found = true;
                Bookstore.currentUserID = rset.getString("ID");
                Bookstore.loggedIn = true;
            }
        }
        catch (Exception sqle){
            System.out.println("Exception : " + sqle);
        }
        return found;
    }
    
}
