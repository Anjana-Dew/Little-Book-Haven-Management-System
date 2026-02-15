CREATE DATABASE Little_Book_Haven;
USE Little_Book_Haven;

CREATE TABLE Books(
	Book_ID INT AUTO_INCREMENT PRIMARY KEY,
    Title VARCHAR (100),
    Author VARCHAR (100),
    Genre VARCHAR(50),
    Price DECIMAL(8,2)
    );
    
CREATE TABLE Cashiers(
	Cashier_ID VARCHAR (50) PRIMARY KEY,
    Full_Name VARCHAR (100),
    Contact_Number VARCHAR (50),
    Salary DECIMAL (10,2)
    
    );
    
CREATE TABLE Order_Details(
	Order_ID INT AUTO_INCREMENT PRIMARY KEY,
    BOOK_ID INT,
    Quantity INT,
    Total_Price DECIMAL (8,2),
    Order_Date DATETIME,
    CONSTRAINT FK_Book FOREIGN KEY (Book_ID) REFERENCES Books(Book_ID)
   
    );
    
    -- =========  Sample data for Books =========
INSERT INTO Books (Title, Author, Genre, Price) 
VALUES 
('To Kill a Mockingbird',        'Harper Lee',          'Fiction',        1590.00),
('The Great Gatsby',             'F. Scott Fitzgerald', 'Fiction',        1390.00),
('The Girl with the Dragon Tattoo','Stieg Larsson',     'Mystery',        1780.00),
('Gone Girl',                    'Gillian Flynn',       'Mystery',        1650.00),
('1984',                         'George Orwell',       'Science Fiction',1299.00),
('Dune',                         'Frank Herbert',       'Science Fiction',2250.00),
('Harry Potter and the Sorcerer''s Stone','J.K. Rowling','Fantasy',       1499.00),
('The Hobbit',                   'J.R.R. Tolkien',      'Fantasy',        1750.00),
('Becoming',                     'Michelle Obama',      'Non-Fiction',    1850.00),
('Educated',                     'Tara Westover',       'Non-Fiction',    1625.00),
('Atomic Habits',                'James Clear',         'Self-Help',      1500.00),
('The 7 Habits of Highly Effective People','Stephen Covey','Self-Help',   1650.00),
('Charlotte''s Web',             'E.B. White',          'Children',       990.00),
('Matilda',                      'Roald Dahl',          'Children',       1125.00);
 
 -- ======== Sample Cashier Data ========
INSERT INTO Cashiers (Cashier_ID,Full_Name, Contact_Number, Salary) 
VALUES
('CASH001','Anjali Perera',     '0771234567', 75000.00),
('CASH002','Nuwan Fernando',    '0789876543', 68500.00),
('CASH003','Ishara Gunasekara', '0763456789', 72000.00),
('CASH004','Kasun Silva',       '0752345678', 70000.00);

-- ======== Sample Order Details ========
INSERT INTO Order_Details (Book_ID, Quantity, Total_Price, Order_Date) VALUES
(1,  2, 3180.00, '2025-06-10'),
(4,  1, 1650.00, '2025-06-11'),
(7,  3, 4497.00, '2025-06-12'),
(2,  1, 1390.00, '2025-06-13'),
(5,  2, 2598.00, '2025-06-14'),
(10, 1, 1625.00, '2025-06-14'),
(3,  2, 3560.00, '2025-06-15');
    