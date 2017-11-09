package lab7;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.util.Scanner;

//import com.mysql.jdbc.PreparedStatement;

public class ecafe2 {
	public static void main(String[] args) throws SQLException {
		// here we create the connection
		Scanner inputVal = new Scanner(System.in);
		int continu = 1;
		String pass = "asdf1234";
		String name = "root";
		Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/ecafe", name, pass);
		Statement statement = connection.createStatement();

		System.out.println("Welcome to The Cafe Westros ");
		System.out.println("Please enter your name: ");

		String userName, userAddr;
		int userContact;

		System.out.print("name: ");
		userName = inputVal.nextLine();
		
		/*
		 * //select query with prepared statement String
		 * sqlUserInsert="insert into user (user_name) values (?)"; PreparedStatement
		 * statementUserInsert = (PreparedStatement)
		 * connection.prepareStatement(sqlUserInsert);
		 * statementUserInsert.setString(1,userName);
		 * 
		 * int userInsert = statementUserInsert.executeUpdate();
		 */
		
		//callable statement that call the stored function getUserName from the database

		java.sql.CallableStatement callableStatement = null;

		String getUserName = "{call getUserName(?)}";
		callableStatement = connection.prepareCall(getUserName);
		callableStatement.setString(1, userName);
		callableStatement.executeUpdate();

		//code below prints the course menu from the database
		while (continu != 0) {
			System.out.println("***** Select Your Order From the Menu: *******");
			// ResultSet rs = statement.executeQuery("SELECT * FROM courses");
			
			// select query with prepared statement
			String sqlSelectCourse = "SELECT * FROM courses";
			PreparedStatement statementSelectCourse = (PreparedStatement) connection.prepareStatement(sqlSelectCourse);
			ResultSet rsSelectCourse = statementSelectCourse.executeQuery();

			while (rsSelectCourse.next()) {
				System.out.print(rsSelectCourse.getInt(1));
				System.out.print(" ");
				System.out.print(rsSelectCourse.getString(2));
				System.out.print("\n");
			}

			System.out.print("**** Select the item to order ***** ");
			int courseSelected = inputVal.nextInt();
			System.out.println();

			// select query with prepared statement
			String sql = "SELECT * FROM item where course_no=?";
			PreparedStatement statementItems = (PreparedStatement) connection.prepareStatement(sql);
			statementItems.setInt(1, courseSelected);
			ResultSet rsItems = statementItems.executeQuery();

			while (rsItems.next()) {
				System.out.print(rsItems.getInt(1));
				System.out.print("  ");
				System.out.print(rsItems.getString(2));
				System.out.print("  -----  ");
				System.out.print(rsItems.getString(3) + "Rs");
				System.out.println();
			}

			int itemSelected = inputVal.nextInt();
			System.out.println();

			// select query with prepared statement
			String sqlItemVal = "select * from item where item_no=?";
			PreparedStatement statementItemVal = (PreparedStatement) connection.prepareStatement(sqlItemVal);
			statementItemVal.setInt(1, itemSelected);
			ResultSet rsItemVal = statementItemVal.executeQuery();

			int itemPrice, prepTime;

			rsItemVal.next();
			itemPrice = rsItemVal.getInt(3);
			// rsItemVal.next();
			prepTime = rsItemVal.getInt(4);

			String sqlUserId = "select userID from user where user_name=?";
			PreparedStatement statementUserId = (PreparedStatement) connection.prepareStatement(sqlUserId);
			statementUserId.setString(1, userName);
			ResultSet rsUserId = statementUserId.executeQuery();
			rsUserId.next();
			int userID = rsUserId.getInt(1);

			PreparedStatement statementOrder = (PreparedStatement) connection
					.prepareStatement("insert " + "into order (item_price,prep_time,userID) values(?,?,?)");
			statementOrder.setInt(1, itemPrice);// price
			statementOrder.setInt(2, prepTime);// prep time
			statementOrder.setInt(3, userID);// user id

			// int rsOrder = statementOrder.executeUpdate();

			System.out.print("Order Something Else[yes=1 / no=0] ");
			continu = inputVal.nextInt();
			
			// below we take user credentiels such as address contact etc
			if (continu == 0) {

				System.out.print("your order has been taken..");
				System.out.println();

				System.out.print("enter your address: ");
				String A = inputVal.nextLine();
				userAddr = inputVal.nextLine();

				System.out.print("enter your contact no. : ");
				userContact = inputVal.nextInt();

				// update query with prepared statement
				
				String sqlUserInfoInsert = "update user set user_address=?, user_contact=? where user_name=?";
				PreparedStatement statementUserInfoInsert = (PreparedStatement) connection
						.prepareStatement(sqlUserInfoInsert);
				statementUserInfoInsert.setString(1, userAddr);
				statementUserInfoInsert.setInt(2, userContact);
				statementUserInfoInsert.setString(3, userName);

				int UserInfoInsert = statementUserInfoInsert.executeUpdate();
			}
		}
	}
}
