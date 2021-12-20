-- Ryan Ayotte
-- 101073548
-- 3005 Final Project
-- Sun Dec 19, 2021

-- All variables are added in the actual function

-- Call for checking if the user exists in the database - authentication
select * from $user where username = '$username' and password = '$password';

-- Browse books by author
select ISBN, title, quantity from book natural join stock where author = '$author';

--Browse books by genre
select ISBN, title, quantity from book natural join stock where genre = '$genre';

--Browse books by publisher
select ISBN, title, quantity from book natural join stock where publisher = '$publisher';

--Details about a given book
select * from book natural join stock where title = '$title';

--Update stock of a given book (in place of triggers that are incomplete)
update stock set quantity = $newQuantity where ISBN = '$ISBN';

--Remove a given book from the store
delete from book where ISBN = '$ISBN';

--Calls for adding a new book to the database
PreparedStatement pStmt = conn.prepareStatement(
        "insert into book values(?,?,?,?,?,?,?,?)");
PreparedStatement pStmt2 = conn.prepareStatement(
        "insert into stock values(?,?)");
PreparedStatement pStmt3 = conn.prepareStatement(
        "insert into publishes values(?,?)");
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

--Get given publisher info 
select * from publisher where name = '$pub';

--Calls for adding new publisher to the database
PreparedStatement pStmt = conn.prepareStatement(
        "insert into publisher values(?,?,?,?,?,?)");
pStmt.setString(1,ID);
pStmt.setString(2,name);
pStmt.setString(3,email);
pStmt.setString(4,phone);
pStmt.setString(5,address);
pStmt.setString(6,bank);
pStmt.executeUpdate();

--Check to make sure book is in stock
select * from book natural join stock where title = '$title' and quantity > 1

--Place/confirm an order
PreparedStatement pStmt = conn.prepareStatement("insert into orders values(?,?,?,?,?,?)");
PreparedStatement pStmt2 = conn.prepareStatement("insert into place_order values(?,?)");
Statement stmt = conn.createStatement();
ResultSet rset = stmt.executeQuery(select ship_address, bill_address from client where ID = '$currentUserID');

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

--Check to see if an order with a given order number exists
select ID from orders where ID = '$orderNumber';

--Calls for adjusting the stock after completing an order
Statement stmt = conn.createStatement();
Statement stmt2 = conn.createStatement();
PreparedStatement pStmt = conn.prepareStatement("insert into order_book values(?,?)");

ResultSet rSet = stmt.executeQuery("select ISBN from book where title = '"+s+"'");
rSet.next();
stmt2.executeUpdate(update stock set quantity = quantity-1 where ISBN = 'rSet.getString("ISBN")');
pStmt.setString(1, rSet.getString("ISBN"));
pStmt.setString(2, Integer.toString(orderNumber));

--Find all orders for the current client
select * from orders where client_id = '$currentUserID';

