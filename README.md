# COMP_3005_Project
Final Project for COMP 3005 Fall 2021, created by Ryan Ayotte

## Description
This an an application simulating an online Bookstore with 2 different user interfaces: one for clients who can browse and order books, and one for owners who can manage the books in the bookstore, display publisher information, and generate reports on the sales.

## Requirements
Postgresql is the database server for the application so a running instance of that is needed.

Java is required to compile and run the program (Used JDK 16 and vscode with java extension)

The postgresql jdbc driver is also needed in the class path of the program to run (Used 42.3.1, vscode allowed easy linking of the driver and the compiling of code)

Ensure postgresql is running and that a new database is created for this. Add the url and login credentials to the driver manager in the Bookstore.java file (line 127) before compiling.

## Compiling
If all requirements are met, compile all of the java files and include the jdbc driver jar, then run from Bookstore to run the program.

## Running
Before running the program, run the queries in the DDL.sql file (in SQL sub directory) to populate the database with the required tables and some starting insert entities and attributes.

Run the compiled Bookstore file (including the jdbc driver jar file).