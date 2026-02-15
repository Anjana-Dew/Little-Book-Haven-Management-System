/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package assignment3;

import java.sql.*;
import java.util.Scanner;
import java.util.InputMismatchException;

class Book {
    private int bookId;
    private String title;
    private String author;
    private String genre;
    private double price;

    public Book() {
    }

    public Book(int bookId, String title, String author, String genre, double price) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.price = price;
    }
    public int getBookId() { 
        return bookId; 
    }
    public void setBookId(int bookId) { 
        this.bookId = bookId; 
    }

    public String getTitle() { 
        return title; 
    }
    public void setTitle(String title) { 
        this.title = title; 
    }

    public String getAuthor() { 
        return author; 
    }
    public void setAuthor(String author) { 
        this.author = author; 
    }

    public String getGenre() { 
        return genre; 
    }
    public void setGenre(String genre) { 
        this.genre = genre; 
    }

    public double getPrice() { 
        return price; 
    }
    public void setPrice(double price) { 
        this.price = price; 
    }
    
    public void addBooks() throws Exception {
        
        int recordCount = 0;
        boolean validInput = false;

        System.out.println("----Add new Books to the Inventory----");

        while (!validInput) {
            try {
                System.out.print("How many Book Records Do You Want to Add? ");
                recordCount = LittleBookHaven.input.nextInt();
                LittleBookHaven.input.nextLine();

                if (recordCount <= 0) {
                    System.out.println("\nPlease Enter a Number Greater Than 0.\n");
                } else {
                    validInput = true;
                }
            } catch (InputMismatchException e) {
                System.out.println("\nError: Please Enter a Valid Input.\n");
                LittleBookHaven.input.nextLine();
            }
        }

        System.out.println("\nEnter Book Details Below, ");

        for (int i = 0; i < recordCount; i++) {
            
            Book b = new Book(); 

            System.out.println("----------------------");
            System.out.print("Enter Book Title: ");
            b.setTitle(LittleBookHaven.input.nextLine());

            System.out.print("Enter Author's Name: ");
            b.setAuthor(LittleBookHaven.input.nextLine());

            System.out.print("Enter Genre: ");
            b.setGenre(LittleBookHaven.input.nextLine());

            System.out.print("Enter Price: ");
            b.setPrice(LittleBookHaven.input.nextDouble());
            LittleBookHaven.input.nextLine();

            
            String sql = "INSERT INTO Books (Title, Author, Genre, Price) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = LittleBookHaven.conn.prepareStatement(sql);

            stmt.setString(1, b.getTitle());
            stmt.setString(2, b.getAuthor());
            stmt.setString(3, b.getGenre());
            stmt.setDouble(4, b.getPrice());

            stmt.executeUpdate();
            System.out.println("\nData Inserted Successfully!\n");
            stmt.close();
        }
    }
    
    public void viewBooks() throws Exception {
        
        Statement stmt = LittleBookHaven.conn.createStatement();
        String query = "SELECT * FROM Books";
        ResultSet rs = stmt.executeQuery(query);

        System.out.println("   Book Inventory   ");

        if (!rs.isBeforeFirst()) {
            System.out.println("Book Inventory is Empty.");
        } else {
            while (rs.next()) {
                Book book = new Book();
                
                book.setBookId(rs.getInt("Book_ID"));
                book.setTitle(rs.getString("Title"));
                book.setAuthor(rs.getString("Author"));
                book.setGenre(rs.getString("Genre"));
                book.setPrice(rs.getDouble("Price"));

                System.out.println("---------------");
                System.out.println("Book ID: " + book.getBookId());
                System.out.println("Title  : " + book.getTitle());
                System.out.println("Author : " + book.getAuthor());
                System.out.println("Genre  : " + book.getGenre());
                System.out.println("Price  : " + book.getPrice());
            }
        }
        rs.close();
        stmt.close();
}
    
    public void deleteBooks() throws Exception {
        
        int deleteCount = 0;
        boolean validInput = false;

        System.out.println("----Delete Book Records----");

        while (!validInput) {
            try {
                System.out.print("How Many Records Do You Want To Delete: ");
                deleteCount = LittleBookHaven.input.nextInt();
                LittleBookHaven.input.nextLine();

                if (deleteCount <= 0) {
                    System.out.println("Please Enter a Number Greater Than 0.");
                } else {
                    validInput = true;
                }
            } catch (InputMismatchException e) {
                System.out.println("Please Enter a Valid Input.");
                LittleBookHaven.input.nextLine();
            }
        }

        for (int i = 0; i < deleteCount; i++) {
            System.out.print("Enter Book ID to delete: ");
            int id = LittleBookHaven.input.nextInt();
            LittleBookHaven.input.nextLine();

            Book book = new Book();
            book.setBookId(id);

            String query = "DELETE FROM Books WHERE Book_ID = ?";
            PreparedStatement pstmt = LittleBookHaven.conn.prepareStatement(query);
            pstmt.setInt(1, book.getBookId());

            int rowsDeleted = pstmt.executeUpdate();

            if (rowsDeleted > 0) {
                System.out.println("Record Deleted Successfully");
            } else {
                System.out.println("Book ID not Found");
            }

            pstmt.close();
        }
    }
    
    public void searchByCategory(Book book)throws Exception {
        
        String query = "SELECT Title FROM Books WHERE Genre = ?";
        PreparedStatement pstmt = LittleBookHaven.conn.prepareStatement(query);
        pstmt.setString(1, book.getGenre());

        ResultSet rs = pstmt.executeQuery();

        if (!rs.isBeforeFirst()) {
            System.out.println("\n------------------------------------------------");
            System.out.println("No books found under the category named: " + book.getGenre());
            System.out.println("------------------------------------------------");
        } else {
            System.out.println("\nBooks in Category: " + book.getGenre());
            System.out.println("-----------------------");
            while (rs.next()) {
                System.out.println("- " + rs.getString("Title"));
            }
        }

        rs.close();
        pstmt.close();
    }
    
    public void updateBook(Book book) throws Exception {
        
        String query = "UPDATE Books SET Title = ?, Author = ?, Genre = ?, Price = ? WHERE Book_ID = ?";
        PreparedStatement pstmt = LittleBookHaven.conn.prepareStatement(query);

        pstmt.setString(1, book.getTitle());
        pstmt.setString(2, book.getAuthor());
        pstmt.setString(3, book.getGenre());
        pstmt.setDouble(4, book.getPrice());
        pstmt.setInt(5, book.getBookId());

        int rowsUpdated = pstmt.executeUpdate();

        if (rowsUpdated > 0) {
            System.out.println("Record Updated Successfully");
        } else {
            System.out.println("Book ID not Found");
        }

        pstmt.close();
    }
    
    public void updateBookTitle(Book book) throws Exception {
        
        String query = "UPDATE Books SET Title = ? WHERE Book_ID = ?";
        PreparedStatement pstmt = LittleBookHaven.conn.prepareStatement(query);

        pstmt.setString(1, book.getTitle());         
        pstmt.setInt(2, book.getBookId());           

        int rowsUpdated = pstmt.executeUpdate();

        if (rowsUpdated > 0) {
            System.out.println("Title Updated Successfully.");
        } else {
            System.out.println("Book ID not found.");
        }

        pstmt.close();
    }
    
    public void updateBookAuthor(Book book) throws Exception {
        
        String query = "UPDATE Books SET Author = ? WHERE Book_ID = ?";
        PreparedStatement pstmt = LittleBookHaven.conn.prepareStatement(query);

        pstmt.setString(1, book.getAuthor());        
        pstmt.setInt(2, book.getBookId());           

        int rowsUpdated = pstmt.executeUpdate();

        if (rowsUpdated > 0) {
            System.out.println("Author Updated Successfully.");
        } else {
            System.out.println("Book ID not found.");
        }

        pstmt.close();
    }
    
    public void updateBookGenre(Book book) throws Exception {
        
        String query = "UPDATE Books SET Genre = ? WHERE Book_ID = ?";
        PreparedStatement pstmt = LittleBookHaven.conn.prepareStatement(query);

        pstmt.setString(1, book.getGenre());         
        pstmt.setInt(2, book.getBookId());           

        int rowsUpdated = pstmt.executeUpdate();

        if (rowsUpdated > 0) {
            System.out.println("Genre Updated Successfully.");
        } else {
            System.out.println("Book ID not found.");
        }

        pstmt.close();
    }
    
    public void updateBookPrice(Book book) throws Exception {
        
        String query = "UPDATE Books SET Price = ? WHERE Book_ID = ?";
        PreparedStatement pstmt = LittleBookHaven.conn.prepareStatement(query);

        pstmt.setDouble(1, book.getPrice());         // new price
        pstmt.setInt(2, book.getBookId());           // book ID to update

        int rowsUpdated = pstmt.executeUpdate();

        if (rowsUpdated > 0) {
            System.out.println("Price Updated Successfully.");
        } else {
            System.out.println("Book ID not found.");
        }

        pstmt.close();
    } 
    
}
class cashierServices {
    public void addNewCashier()throws Exception{
        
        int count = 0;
        boolean validInput = false;
     
        System.out.println("--- Add New Cashier ---");
     
        while(!validInput){
            try{
                System.out.print("Please enter how many cashiers you want to add: ");
                count = LittleBookHaven.input.nextInt();
                LittleBookHaven.input.nextLine();
            
                if (count <= 0){
                    System.out.println("\nPlease Enter a Number Greater Than 0.\n"); 
                } else{
                    validInput= true;   
                }
            }catch(InputMismatchException e){
                System.out.println("\nError: Please Enter a Valid Input.\n");
                LittleBookHaven.input.nextLine();
            }
        }
        System.out.println("\nPlease enter the below details of the new cashier: ");
        System.out.println("---------------------------------------------------");
    
        for (int i = 0; i < count; i++) {

            cashier c = new cashier();
        
            System.out.print("Enter Cashier's ID:  "); 
            c.setCashierID(LittleBookHaven.input.nextLine());
        
            System.out.print("Enter Full Name: ");
            c.setFullName(LittleBookHaven.input.nextLine());
        
            System.out.print("Enter Contact Number: ");
            c.setContactNumber(LittleBookHaven.input.nextLine());
            
            System.out.print("Enter Their Salary: ");
            c.setSalary(LittleBookHaven.input.nextDouble());
            LittleBookHaven.input.nextLine();
              
            String sql = "INSERT INTO Cashiers (Cashier_ID,Full_Name,Contact_Number,Salary)VALUES(?,?,?,?)";
            PreparedStatement stmt = LittleBookHaven.conn.prepareStatement(sql);
            
            stmt.setString(1, c.getCashierID());
            stmt.setString(2, c.getFullName());
            stmt.setString(3, c.getContactNumber());
            stmt.setDouble(4, c.getSalary());
                
            stmt.executeUpdate();
            System.out.println("\nData Inserted Successfully!\n");
            stmt.close();
        }
    }
    
    public void updateCashier(cashier cashier) throws Exception {
        
        String query = "UPDATE Cashiers SET Cashier_ID = ?, Full_Name = ?, Contact_Number = ?, Salary = ? WHERE Cashier_ID = ?";
        PreparedStatement pstmt = LittleBookHaven.conn.prepareStatement(query);

        pstmt.setString(1, cashier.getCashierID()); 
        pstmt.setString(2, cashier.getFullName());
        pstmt.setString(3, cashier.getContactNumber());
        pstmt.setDouble(4, cashier.getSalary());
        pstmt.setString(5, cashier.getCashierID());  

        int rowsUpdated = pstmt.executeUpdate();

        if (rowsUpdated > 0) {
            System.out.println("\nCashier record updated successfully.");
        } else {
            System.out.println("\nCashier ID not found.");
        }

        pstmt.close();
    }
    
    public void updateCashierSalary(cashier cashier) throws Exception {
        
        String query = "UPDATE Cashiers SET Salary = ? WHERE Cashier_ID = ?";
        PreparedStatement pstmt = LittleBookHaven.conn.prepareStatement(query);

        pstmt.setDouble(1, cashier.getSalary());
        pstmt.setString(2, cashier.getCashierID());

        int rowsUpdated = pstmt.executeUpdate();

        if (rowsUpdated > 0) {
            System.out.println("\nSalary updated successfully.");
        } else {
            System.out.println("\nCashier ID not found.");
        }
        pstmt.close();
    }
    public void updateCashierContact(cashier cashier) throws Exception {
        
        String query = "UPDATE Cashiers SET Contact_Number = ? WHERE Cashier_ID = ?";
        PreparedStatement pstmt = LittleBookHaven.conn.prepareStatement(query);

        pstmt.setString(1, cashier.getContactNumber());
        pstmt.setString(2, cashier.getCashierID());
        int rowsUpdated = pstmt.executeUpdate();
        if (rowsUpdated > 0) {
            System.out.println("\nContact number updated successfully.");
        } else {
            System.out.println("\nCashier ID not found.");
        }

        pstmt.close();
}
    
    public void updateCashierName(cashier cashier) throws Exception {
        
        String query = "UPDATE Cashiers SET Full_Name = ? WHERE Cashier_ID = ?";
        PreparedStatement pstmt = LittleBookHaven.conn.prepareStatement(query);

        pstmt.setString(1, cashier.getFullName());
        pstmt.setString(2, cashier.getCashierID());

        int rowsUpdated = pstmt.executeUpdate();

        if (rowsUpdated > 0) {
            System.out.println("\nCashier name updated successfully.");
        } else {
            System.out.println("\nCashier ID not found.");
        }

        pstmt.close();
}
    
    public void updateCashierID (String oldID, String newID) throws Exception {
        
        String query3 = "UPDATE Cashiers SET Cashier_ID = ? WHERE Cashier_ID = ? ";
                       
        PreparedStatement pstmt2 = LittleBookHaven.conn.prepareStatement(query3);
                       
        pstmt2.setString(1, newID);
        pstmt2.setString(2, oldID);
        
        int rowsUpdate = pstmt2.executeUpdate();
    
        if(rowsUpdate>0){
            System.out.println("\nRecord Updated Successfully");
        }else {
            System.out.println("\nCashier ID not Found");
        }
        
        pstmt2.close();                    
    }
    
    public void viewAllCashiers()throws Exception{
        
        Statement stmt = LittleBookHaven.conn.createStatement();      
        String query = "SELECT * FROM Cashiers";
        ResultSet rs = stmt.executeQuery(query);
               
        System.out.println("\n----- Registered Cashiers ----- ");
    
        if(!rs.isBeforeFirst()){  
            System.out.println("No Registered Cashier Accounts Found in the System");
        }else{
            System.out.println("Displaying all cashier accounts currently in the system: ");
               
            while (rs.next()){
                cashier c = new cashier();
                
                c.setCashierID(rs.getString("Cashier_ID"));
                c.setFullName(rs.getString("Full_Name")); 
                c.setContactNumber(rs.getString("Contact_Number"));
                c.setSalary(rs.getDouble("Salary")); 
                   
                System.out.println("---------------");
                System.out.println("Cashier ID     : "+c.getCashierID());
                System.out.println("Full Name      : "+c.getFullName());
                System.out.println("Contact Number : "+c.getContactNumber());
                System.out.println("Salary         : "+c.getSalary());
            }
        }
        stmt.close();
    }
    
    public void deleteCashier()throws Exception{
        
        int DeleteCount =0;
        boolean validInput2 = false;
    
        System.out.println("----- Delete Cashier Account -----");
        while (!validInput2){
            try{
                System.out.print("How Many Records Do You Want To Delete: ");
                DeleteCount = LittleBookHaven.input.nextInt();
                LittleBookHaven.input.nextLine();
                    
                if (DeleteCount <= 0){
                    System.out.println("\nPlease Enter a Number Greater Than 0.\n"); 
                } else{
                    validInput2= true;   
                }
                } catch (InputMismatchException e){
                    System.out.println("\nError: Please Enter a Valid Input.\n");
                    LittleBookHaven.input.nextLine();
                }        
            }  
            for (int i = 0; i<DeleteCount ; i++){
                System.out.print("\nEnter Cashier's ID to delete: ");
                String ID =LittleBookHaven.input.nextLine();
                
                cashier c = new cashier();
                c.setCashierID(ID);
            
                String query2 = "DELETE FROM Cashiers WHERE Cashier_ID = ?";
                PreparedStatement pstmt = LittleBookHaven.conn.prepareStatement(query2);
               
                pstmt.setString(1, c.getCashierID());
               
                int rowsDelete = pstmt.executeUpdate();
               
                if (rowsDelete > 0){
                    System.out.println("Record Deleted Successfully");
                }else {
                    System.out.println("Cashier ID not Found");
                }
                pstmt.close();
            } 
    }
    
}
class transactionServices {
    
    public void newTransaction()throws Exception{
        System.out.println("\n-----New Transactions-----");
        System.out.print("Enter Book ID: ");
        int BID = LittleBookHaven.input.nextInt();
        System.out.print("Enter Quantity: ");
        int qty = LittleBookHaven.input.nextInt();
                       
        String query9 = "SELECT Title , Price FROM Books WHERE Book_ID = ?";
        PreparedStatement pstmt8 = LittleBookHaven.conn.prepareStatement(query9);
                       
        pstmt8.setInt(1, BID);
                       
        ResultSet rs2 = pstmt8.executeQuery();
        if(rs2.next()){
            String title = rs2.getString("Title");
            double price = rs2.getDouble("Price");
                           
            double total = price * qty;
            System.out.println("\n--------Transaction Details---------");
            System.out.println("Book Title: "+title);
            System.out.println("Unit Price: "+price);
            System.out.println("Total Price: "+total);
            System.out.println("-------------------------");
                           
            String query10 = "INSERT INTO Order_Details (Book_ID,Quantity,Total_Price,Order_Date) Values (?,?,?,NOW())";
                           
            PreparedStatement pstmt9 = LittleBookHaven.conn.prepareStatement(query10);
            pstmt9.setInt(1, BID);
            pstmt9.setInt(2, qty);
            pstmt9.setDouble(3, total);
                           
            pstmt9.executeUpdate();
            pstmt9.close();
                           
            System.out.println("\nTransaction Recorded Successfully!");
        }else {
            System.out.println("No book found with the given ID.");
        }
        rs2.close();
        pstmt8.close();
    }
 
    public void viewAllTransactions()throws Exception{
        Statement stmt2 = LittleBookHaven.conn.createStatement();
    
        String query11 = "SELECT * FROM Order_Details";
        ResultSet rs3 = stmt2.executeQuery(query11);
                       
        System.out.println("\n-------Order Details---------");
        if(!rs3.isBeforeFirst()){
            System.out.println("No Transaction Record Found.");
            System.out.println("The order history is currently empty.");
        }else{   
            while (rs3.next()){
                int OId = rs3.getInt("Order_ID");
                int BId = rs3.getInt("Book_ID");
                int Quantity = rs3.getInt("Quantity");
                double total_Price = rs3.getDouble("Total_Price");
                Timestamp orderDate = rs3.getTimestamp("Order_Date");
                          
                   
                System.out.println("---------------");
                System.out.println("Order ID     : "+OId);
                System.out.println("Book ID      : "+BId);
                System.out.println("Quntity      : "+Quantity);
                System.out.println("Total Amount : "+total_Price);
                System.out.println("Order Date   : "+orderDate);
                         
            }
        }
        stmt2.close(); 
    }
}

class user {
  protected String userRole;
  
  public void login()throws Exception{  
      
    boolean correctCredential = false ;
        
    while(correctCredential == false){
            
    System.out.println("Welcome to Little Book Haven Login System!!");
    System.out.println("Please Enter Your User Credentials Below, \n");
    System.out.print("User Name: ");
    String userName = LittleBookHaven.input.nextLine();
    System.out.print("Password: ");
    String userPassword = LittleBookHaven.input.nextLine();
    System.out.println("\n");
       
        
    if (userName.equalsIgnoreCase("admin")){
        if (userPassword.equals("1234")){
                
            correctCredential = true;
            manager admin = new manager();
            userRole = "manager";
        }else{            
        System.out.println("Error: Incorrect Password.\n");
        }
        
    }else if (userName.toUpperCase().startsWith("CASH")){
        if(userPassword.equalsIgnoreCase("cash1234")){
            
            String query = "SELECT * FROM Cashiers WHERE Cashier_ID = ?";
            
            PreparedStatement pstmt = LittleBookHaven.conn.prepareStatement(query);
            pstmt.setString(1, userName);
            
            ResultSet rs = pstmt.executeQuery();
            
                if(rs.next()){
                    cashier newCashier = new cashier(true); 
                
                    correctCredential = true;
                    userRole = "cashier";
                }else{
                    System.out.println("Error: No such Cashier Found in the DataBase\n");
                }
            rs.close();
            pstmt.close();
        }else {
            System.out.println("Incorrect Password\n");
        }
    }else {
       System.out.println("Error: Invalid Usename. Enter Valid Username.  \n");
    }
        
    }     
  }
  
  public void salesDesk(String userRole) throws Exception {
      
    int choice = 0;
    while(choice != 7){
        
    try{
       System.out.println("   \n   Welcome to The Little Book Haven - Sales Desk ");
       System.out.println("Please Choose an Option");
       System.out.println("--------------------------------------------");
       System.out.println("1. Add books");
       System.out.println("2. View All Books");
       System.out.println("3. Delete Book Records");
       System.out.println("4. Update Book Records");
       System.out.println("5. Search Books by Catergory");
       System.out.println("6. Transaction");
       System.out.println("7. Exit");
       System.out.println("--------------------------------------------");
       System.out.print("Enter your choice(1-7): ");
       choice  = LittleBookHaven.input.nextInt();
       LittleBookHaven.input.nextLine();
       System.out.println("\n");
       
       switch (choice){
           case 1: 
               new Book().addBooks();
               break;
           case 2:
               new Book().viewBooks();
               break;
           case 3:
               new Book().deleteBooks();
               break;
           case 4:
               System.out.println("----Update Book Records----");
               System.out.println("Here you can modify existing book details in the inventory.\n");
               
               System.out.print("Enter Book ID to Update: ");
               int IDtoUpdate = LittleBookHaven.input.nextInt();
               LittleBookHaven.input.nextLine();
               
               int columnUpdate = 0;
               Book b = new Book();
               b.setBookId(IDtoUpdate);
               
               while(columnUpdate !=6){
                   try{
                       System.out.println("\n What Do You Want to Update? ");
                       System.out.println("1. Title");
                       System.out.println("2. Author");
                       System.out.println("3. Genre");
                       System.out.println("4. Price");
                       System.out.println("5. All");
                       System.out.println("6. Cancel");
                       System.out.print("Enter Which Column to Update(1-6): ");
                       columnUpdate = LittleBookHaven.input.nextInt();
                       LittleBookHaven.input.nextLine();
                       
                       
                       switch (columnUpdate){
                           case 1:
                               System.out.print("Enter New Title to Update: ");
                               String newTitle = LittleBookHaven.input.nextLine();
                               
                               
                               b.setTitle(newTitle);
                               b.updateBookTitle(b);
                               break;
                           case 2:
                               System.out.print("Enter New Author's Name to Update: ");
                               String newName = LittleBookHaven.input.nextLine();
                               
                               b.setAuthor(newName);
                               b.updateBookAuthor(b);
                               break;
                           case 3:
                               System.out.print("Enter New Genre to Update: ");
                               String newGenre = LittleBookHaven.input.nextLine();
                               
                               b.setGenre(newGenre);
                               b.updateBookGenre(b);
                               break;
                           case 4:
                               System.out.print("Enter New Price to Update: ");
                               double newPrice = LittleBookHaven.input.nextDouble();
                               
                               b.setPrice(newPrice);
                               b.updateBookPrice(b);
                               break;
                           case 5:
                               System.out.println("To Update Entire Record Please Enter, ");
                               System.out.println("New Title: ");
                               String newTitle2 = LittleBookHaven.input.nextLine();
                               System.out.println("Author: ");
                               String newAuthor2 = LittleBookHaven.input.nextLine();
                               System.out.println("Genre: ");
                               String newGenre2 = LittleBookHaven.input.nextLine();
                               System.out.println("Price: ");
                               double newPrice2 = LittleBookHaven.input.nextDouble();
                               
                               b.setTitle(newTitle2);
                               b.setAuthor(newAuthor2);
                               b.setGenre(newGenre2);
                               b.setPrice(newPrice2);
                               b.updateBook(b);
                               break;
                          case 6:
                               System.out.println("Returning to the Main Page...");
                               break;
                         default: 
                              System.out.println("\nError: Invalid Option. Please Select (1-6)");
                        }
                    }catch(InputMismatchException e){
                         System.out.println("\nError: Please Enter a Valid Input.");
                         LittleBookHaven.input.nextLine();
                   }
               }
               break;
           case 5:
               System.out.print("Enter catergory to search: ");
               String category = LittleBookHaven.input.nextLine();
               
               Book book = new Book();         
               book.setGenre(category);        

                new Book().searchByCategory(book);
                
               break;
           case 6: 
               if(userRole.equals("manager")){
                   new transactionServices().viewAllTransactions();
               }else if (userRole.equals("cashier")){
                   new transactionServices().newTransaction();
               }else {
                   System.out.println("Access Denied: Invalid role for transaction.");
               }
               break;
           case 7:
               System.out.println("Exiting From The System....");
               System.out.println("Thank You For Using The Little Book Haven Salase-Desk System!");
               break;
           default:
               System.out.println("Invalid Choice! Please enter a number from 1 to 7.");
            }   
       }catch(InputMismatchException e){
            System.out.println("\nError: Please Enter a Valid Input. \n");
            LittleBookHaven.input.nextLine();
        }
    }
  }
  
  public void cashierManagement()throws Exception{
    int choice =0;
    while(choice != 5){
        try{
            System.out.println("\n===========  Cashier Management Panel  ===========  ");
            System.out.println("Welocme, Admin!");
            System.out.println("Manage cashier accounts with the options below:");
            System.out.println("-----------------------------------------------");
            System.out.println("1. Add New Cashier");
            System.out.println("2. Update Existing Cashier");
            System.out.println("3. Delete Cashier");
            System.out.println("4. View All Cashiers");
            System.out.println("5. Exit");
            System.out.println("------------------------------------------------");
            System.out.print("Enter Your Choice(1-5): ");
            choice = LittleBookHaven.input.nextInt();
            LittleBookHaven.input.nextLine();
            
            switch(choice){
                case 1: 
                   new cashierServices().addNewCashier();
                   break;
                case 2:
                    System.out.println("\n------- Update Cashier Information -------");
                    System.out.print("Enter Cashier ID to Update: ");
                    String IDtoUpdate = LittleBookHaven.input.nextLine();
              
                    int columnUpdate = 0;
                    cashier c = new cashier();
                    c.setCashierID(IDtoUpdate);
               
                    while(columnUpdate != 6){
                   
                        try{
                            System.out.println("\n What Do You Want to Update? ");
                            System.out.println("1. Cashier ID");
                            System.out.println("2. Full Name");
                            System.out.println("3. Contact Number");
                            System.out.println("4. Salary");
                            System.out.println("5. All");
                            System.out.println("6. Cancel");
                            System.out.print("Enter Which Column to Update(1-6): ");
                            columnUpdate = LittleBookHaven.input.nextInt();
                            LittleBookHaven.input.nextLine();
                            
                            cashierServices services = new cashierServices();

                            switch(columnUpdate){
                                  case 1:
                                      System.out.print("Enter New ID to Update: ");
                                      String newID = LittleBookHaven.input.nextLine();
                                      
                                      services.updateCashierID(IDtoUpdate,newID);
                                      break;
                                  case 2:
                                      System.out.print("Enter New Name to Update: ");
                                      String newName = LittleBookHaven.input.nextLine();
                                      
                                      c.setFullName(newName);
                                      services.updateCashierName(c);
                                      break;
                                  case 3:
                                      System.out.print("Enter New Contact Number to Update: ");
                                      String newContact = LittleBookHaven.input.nextLine();
                                      
                                      c.setContactNumber(newContact);
                                      services.updateCashierContact(c);
                                      break;
                                  case 4:
                                      System.out.print("Enter New Salary to Update: ");
                                      double newSalary = LittleBookHaven.input.nextDouble();
                                      
                                      c.setSalary(newSalary);
                                      services.updateCashierSalary(c);
                                      break;
                                  case 5:
                                      System.out.println("To Update Entire Record Please Enter, ");
                                      System.out.print("New ID: ");
                                      String newID2 = LittleBookHaven.input.nextLine();
                                      System.out.print("Full Name: ");
                                      String newName2 = LittleBookHaven.input.nextLine();
                                      System.out.print("Contact Number: ");
                                      String newContact2 = LittleBookHaven.input.nextLine();
                                      System.out.print("Salary: ");
                                      double newSalary2 = LittleBookHaven.input.nextDouble();
                                      
                                      c.setCashierID(newID2);
                                      c.setFullName(newName2);
                                      c.setContactNumber(newContact2);
                                      c.setSalary(newSalary2);
                                      services.updateCashier(c);
                                      break;
                                  case 6:
                                      System.out.println("Returning to the Main Page...");
                                      break;
                                  default:
                                      System.out.println("\nError: Invalid Option. Please Select (1-5) ");
                                }
                                
                        }catch(InputMismatchException e){
                            System.out.println("\nError: Please Enter a Valid Input. \n");
                            LittleBookHaven.input.nextLine();
                        }
                    }
                    break;
                case 3:
                    new cashierServices().deleteCashier();
                    break;
                case 4:
                    new cashierServices().viewAllCashiers();
                    break;
                case 5:
                    System.out.println("\nExiting To Main Menu....");
                    System.out.println("Thank You For Using The Little Book Haven Cashier Management System!");
              
                    break;
                default:
                    System.out.println("\nError: Invalid Option. Please Select (1-5) ");
                    
                }
            }catch(InputMismatchException e){
                System.out.println("\nError: Please Enter a Valid Input. \n");
                LittleBookHaven.input.nextLine();
            }
    }
  }
  
}
class manager extends user  {
 manager()throws Exception{
        System.out.println("===========================================");
        System.out.println("Access Granted: Manager Module Activated");
        System.out.println("===========================================");
        
        int choice = 0;
        while(choice !=3){
            try{ 
               
                System.out.println("\nPlease choose the section you'd like to access next: ");
                System.out.println("----------------------------------------------------");
                System.out.println("1. Sales Desk(Manage Books & Transactions)");
                System.out.println("2. Cashier Management");
                System.out.println("3. Exit");
                System.out.println("----------------------------------------------------");
                
                System.out.print("\nEnter Your Choice(1-3): ");
                choice = LittleBookHaven.input.nextInt();
                LittleBookHaven.input.nextLine();
                
                switch(choice){
                    case 1:
                       super.salesDesk("manager");
                       break;
                    case 2:
                        super.cashierManagement();
                        break;
                    case 3:
                        System.out.println("Exiting From the System...");
                        System.out.println("Thank You For Using The Little Book Haven System!");
                        break;
                    default:
                        System.out.println("\nError: Invalid Choice: Choose From(1-3)");
                }
            }catch(InputMismatchException e){
                System.out.println("\nError: Please Enter a Valid Input.");
                LittleBookHaven.input.nextLine();
            }
                               
                    
        }
    }
 }


class cashier extends user {
    private String cashierID;
    private String fullName;
    private String contactNumber;
    private double salary;
    
    public cashier(){}

    public cashier(String cashierID, String fullName, String contactNumber, double salary) {
        this.cashierID = cashierID;
        this.fullName = fullName;
        this.contactNumber = contactNumber;
        this.salary = salary;
    }

    public String getCashierID() {
        return cashierID;
    }

    public void setCashierID(String cashierID) {
        this.cashierID = cashierID;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }
   public cashier(boolean loginMode) throws Exception {
       if(loginMode){
        System.out.println("===========================================");
        System.out.println("Access Granted: Cashier Module Activated");
        System.out.println("===========================================");

        super.salesDesk("cashier");
       }
    }
}

public class LittleBookHaven {
    public static Scanner input = new Scanner(System.in);
    public static Connection conn ;
    
    public static void main(String[] args) throws Exception {
        String url = "jdbc:mysql://localhost:3306/Little_Book_Haven";
        String user = "root";
        String Password = "";
        
        Class.forName("com.mysql.cj.jdbc.Driver");
        conn = DriverManager.getConnection(url,user,Password);
        
       
        System.out.println("     Little Book Haven Management System      ");
        System.out.println("-----------------------------------------------");
        user user1 = new user();
        user1.login();
      
    }
    
}
