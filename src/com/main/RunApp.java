package com.main;


import java.io.File;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

import com.main.UserCredentials;
import com.main.Users;

public class RunApp {

	// input data
	private static Scanner input;
	private static Scanner reg_input;
	private static Scanner login_input;
	private static Scanner db_read;

	// output data
	private static PrintWriter db_write;

	// model to store data.
	private static Users users;
	private static UserCredentials userCredentials;

	
	public static void main(String[] args) throws IOException {
		initApp();
		welcomeScreen();
		signInOptions();

	}

	// initialize the app by reading the database
	public static void initApp() {

		File dbFile = new File("files/database.txt");

		try {
			// read data from db file
			db_read = new Scanner(dbFile);

			// out put
			db_write = new PrintWriter(new FileWriter(dbFile, true));

			users = new Users();
			userCredentials = new UserCredentials();

		} catch (IOException e) {
			System.out.println("404 : File Not Found ");
		}

	}


	
	

	

	

	public static void welcomeScreen() {
		System.out.println("==========================================");
		System.out.println("*					*");
		System.out.println("*   Welcome To LockMe.com		*");
		System.out.println("*   Developer: Thammineni Satish	*");
		System.out.println("*					*");
		System.out.println("==========================================");

	}

	public static void signInOptions() throws IOException {
		input = new Scanner(System.in);
		boolean correctInput = false;

		do {

			System.out.println("1 . Registration ");
			System.out.println("2 . Login ");

			String option = input.nextLine();
			switch (option) {
			case "1":
				registerUser();

				correctInput = true;
				break;
			case "2":
				loginUser();

				correctInput = true;
				break;
			default:
				System.out.println("Please select 1 Or 2");

				break;
			}

		} while (correctInput == false);
		input.close();
	}

	public static void registerUser() throws IOException {

		reg_input = new Scanner(System.in);


		System.out.println("Enter Username :");
		String username = reg_input.next();
		boolean checkDuplication = isDuplicateName(username);

		if (checkDuplication) {
			System.out.println("The username is duplicated. Please try again");

			registerUser();
		}

		else {
			users.setUsername(username);

			System.out.println("Enter Password :");
			String password = reg_input.next();
			users.setPassword(password);

			db_write.println(users.getUsername());
			db_write.println(users.getPassword());

			createUserFile(users.getUsername());

			System.out.println("USER REGISTRATION SUSCESSFUL!");
			db_write.close();
			initApp();
			do {
				
				System.out.println("Enter (0) to return");
				password = reg_input.next();
				
			} while (!password.equals("0"));
			
			db_write.close();
			welcomeScreen();
			signInOptions();
			
			
		}

	}

	public static void loginUser() throws IOException {

		login_input = new Scanner(System.in);

		System.out.println("Enter Username :");
		String inpUsername = login_input.next();
		 
		boolean found = false;
		while (db_read.hasNext() && !found) {
			 
			if (db_read.next().equals(inpUsername)) {
				System.out.println("Enter Password :");
				String inpPassword = login_input.next();
				if (db_read.next().equals(inpPassword)) {
					//System.out.println("Login Successful ! 200OK");
					found = true;
					db_read.close();
					UserOptions.lockerOptions(inpUsername);

					break;
				}
			}
		}
		if (found == false) {
			db_read.close();
			System.out.println("User Not Found. Please try again");
			initApp();
			loginUser();
		}

	}

	public static void createUserFile(String user_name) {
		try {
			File file = new File("files/users/" + user_name + ".txt");
			if (file.createNewFile()) {

			} else {

			}
		} catch (IOException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}

	}

	public static boolean isDuplicateName(String user_name) {

		boolean found = false;
		while (db_read.hasNext() && !found) {
			if (db_read.next().equals(user_name)) {
				found = true;
				db_read.close();
				return true;
			}
		}
		db_read.close();
		return false;

	}

}

