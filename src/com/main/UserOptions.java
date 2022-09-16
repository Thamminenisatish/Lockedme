package com.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Scanner;

import com.main.UserCredentials;

public class UserOptions {

	private static UserCredentials userCredentials;
	private static Scanner input;
	private static PrintWriter lockerOutput;
	private static Scanner db_read;

	public static void lockerOptions(String inpUsername) throws IOException {
		input = new Scanner(System.in);
		System.out.println("Welecome " + inpUsername);
		System.out.println("1 . Fetch all stored credentials");
		System.out.println("2 . Add a new credential");
		System.out.println("3 . Delete a credential");
		System.out.println("4 . Search a credential");
		System.out.println("5 . Log out");
		System.out.println("6 . Exit");

		String option = input.nextLine();
		switch (option) {
		case "1":
			fetchCredentials(inpUsername);
			break;
		case "2":
			addCredential(inpUsername);
			break;
		case "3":
			deleteCredential(inpUsername);
			break;
		case "4":
			searchCredentials(inpUsername);
			break;
		case "5":
			System.out.println("Log out is done. Take care.");
			logOut();
			break;
		case "6":
			System.out.println("Thank you for using the app.");
			System.exit(0);
			break;
		default:
			System.out.println("Please select one of these numbers 1 - 6");
			System.out.println("Try again:");
			System.out.println("-------------");
			lockerOptions(inpUsername);
			break;
		}
		// lockerInput.close();
	}

	public static void fetchCredentials(String loggedInUser) throws IOException {

		// create a list to store all credentials
		ArrayList<String> siteCredentials = new ArrayList<String>();

		// create a 2D list to store the sites and their info
		ArrayList<ArrayList<String>> credentials = new ArrayList<ArrayList<String>>();

		// read the locker file
		File lockerFile = new File("files/users/" + loggedInUser + ".txt");
		db_read = new Scanner(lockerFile);

		String line;

		int x = 0;
		int y = -1;

		// retrieve the credentials and add them to the lists
		while (db_read.hasNext()) {

			line = db_read.nextLine();

			if (x == 0 || x % 3 == 0) {

				// add the site name
				siteCredentials.add(line);

				// new array for each site
				ArrayList<String> tempArr = new ArrayList<String>();
				credentials.add(tempArr);

				// add to the site array
				y++;
				credentials.get(y).add(line);

			}

			else {
				// add to the site array
				credentials.get(y).add(line);
			}

			x++;

		}
		
		db_read.close();

		ArrayList<String> sortedCredentials = new ArrayList<String>();
		sortedCredentials = insertionSort(siteCredentials);
		String userInput;
		if (siteCredentials.size() == 0) {
			System.out.println("NO CREDENTIALS WERE ADDED");
			do {

				System.out.println("Enter (0) to return");
				userInput = input.next();
				
			} while (!userInput.equals("0"));
			lockerOptions(loggedInUser);
		}

		else {
			System.out.println("===========================");
			System.out.println("CREDENTIALS IN AN ASCENDING ORDER BY SITE NAME:");
			for (int j = 0; j < sortedCredentials.size(); j++) {

				// check for the order
				for (int h = 0; h < credentials.size(); h++) {
					if (sortedCredentials.get(j).equals(credentials.get(h).get(0))) {
						System.out.println("Site Name: " + credentials.get(h).get(0));
						System.out.println("Username: " + credentials.get(h).get(1));
						System.out.println("Password: " + credentials.get(h).get(2));
						System.out.println("----------");
						break;
					}

				}

			}

			
			do {

				System.out.println("Enter (0) to return");
				userInput = input.next();

			} while (!userInput.equals("0"));
			 
			lockerOptions(loggedInUser);
		}
		// System.out.println(sortedCredentials);

		// Re-order the list in an ascending order

	}

	public static void addCredential(String loggedInUser) throws IOException {
		input = new Scanner(System.in);
		userCredentials = new UserCredentials();
		userCredentials.setLoggedInUser(loggedInUser);
		File lockerFile = new File("files/users/" + loggedInUser + ".txt");
		lockerOutput = new PrintWriter(new FileWriter(lockerFile, true));
	
		System.out.println("Enter Site Name :");
		String siteName = input.nextLine();
		String newSiteName = siteName.toLowerCase();

		if (checkRepeatedSite(loggedInUser, newSiteName)) {
			System.out.println("THE SITE NAME IS DUPLICATED. PLESE TRY AGAIN");
			lockerOutput.close();
			addCredential(loggedInUser);
		}
		
		else {
			
		
		userCredentials.setSiteName(newSiteName);

		System.out.println("Enter Username :");
		String username = input.nextLine();

		userCredentials.setUsername(username);

		System.out.println("Enter Password :");
		String password = input.nextLine();
		userCredentials.setPassword(password);

		lockerOutput.println(userCredentials.getSiteName());
		lockerOutput.println(userCredentials.getUsername());
		lockerOutput.println(userCredentials.getPassword());

		lockerOutput.close();
		System.out.println("YOUR CREDENTIAL IS STORED AND SECURED!");
		String userInput;
		do {

			System.out.println("Enter (0) to return");
			 userInput = input.next();

		} while (!userInput.equals("0"));
		 
		lockerOptions(loggedInUser);
		
		}	
			

	}

	public static void deleteCredential(String loggedInUser) throws IOException {

		input = new Scanner(System.in);

		// read the locker file
		File lockerFile = new File("files/users/" + loggedInUser + ".txt");
		db_read = new Scanner(lockerFile);

		// enter site name
		System.out.println("Enter Site Name You Want To Delete :");
		String siteName = input.nextLine();

		String line;
		boolean found = false;

		// create a new file
		File new_file = new File("files/users/temp.txt");
		new_file.createNewFile();
		PrintWriter pw = new PrintWriter(new_file);

		int i = 0;
		boolean isSiteName = false;

		while (db_read.hasNext()) {

			if (i == 0 || i % 3 == 0) {
				isSiteName = true;
			}

			line = db_read.nextLine();

			if (line.equals(siteName) && isSiteName == true) {

				found = true;

				db_read.nextLine();
				db_read.nextLine();

				i += 2;

			}

			else {

				pw.println(line);
				pw.flush();

			}

			isSiteName = false;
			i++;
		}

		db_read.close();
		pw.close();

		if (found == false) {
			System.out.println("THE SITE YOU ENTERED IS NOT AVAILABLE.");
			String userInput;
			do {

				System.out.println("Enter (0) to return. Enter (1) to try again. ");
				 userInput = input.nextLine();

			} while (!userInput.equals("0") && !userInput.equals("1"));
			
			if (userInput.equals("0")) {
				 
				lockerOptions(loggedInUser);
			}
			else if (userInput.equals("1")) {
				 
				deleteCredential(loggedInUser);
			}
			
			
		
		}

		else {
			// delete old file

			try {
				boolean result = Files.deleteIfExists(Paths.get("files/users/" + loggedInUser + ".txt"));

				if (!result) {
					System.out.println("Sorry, unable to delete the file.");
				}

			}

			catch (IOException e) {
				e.printStackTrace();
			}

			// rename new file
			File old = new File("files/users/temp.txt");
			File newf = new File("files/users/" + loggedInUser + ".txt");
			old.renameTo(newf);
			System.out.println("THE CREDENTIAL WAS DELETED.");

			String userInput;
			do {

				System.out.println("Enter (0) to return");
				 userInput = input.nextLine();

			} while (!userInput.equals("0"));
			lockerOptions(loggedInUser);
		}
	}

	public static void searchCredentials(String loggedInUser) throws IOException {

		// create a list to store all credentials
		ArrayList<String> siteCredentials = new ArrayList<String>();

		// create a 2D list to store the sites and their info
		ArrayList<ArrayList<String>> credentials = new ArrayList<ArrayList<String>>();

		// read the locker file
		File lockerFile = new File("files/users/" + loggedInUser + ".txt");
		db_read = new Scanner(lockerFile);

		String line;

		int x = 0;
		int y = -1;

		// retrieve the credentials and add them to the lists
		while (db_read.hasNext()) {

			line = db_read.nextLine();

			if (x == 0 || x % 3 == 0) {

				// add the site name
				siteCredentials.add(line);

				// new array for each site
				ArrayList<String> tempArr = new ArrayList<String>();
				credentials.add(tempArr);

				// add to the site array
				y++;
				credentials.get(y).add(line);

			}

			else {
				// add to the site array
				credentials.get(y).add(line);
			}

			x++;

		}

		ArrayList<String> sortedCredentials = new ArrayList<String>();
		sortedCredentials = insertionSort(siteCredentials);

		System.out.println("Enter Site Name You Want To Search :");
		String siteName = input.nextLine();

		int result = binarySearch(sortedCredentials, siteName);

		if (result != -1) {

			System.out.println("THIS IS YOUR CREDENTIAL:");
			for (int j = 0; j < sortedCredentials.size(); j++) {

				// check for the order

				if (sortedCredentials.get(result).equals(credentials.get(j).get(0))) {
					System.out.println("----------");
					System.out.println("Site Name: " + credentials.get(j).get(0));
					System.out.println("Username: " + credentials.get(j).get(1));
					System.out.println("Password: " + credentials.get(j).get(2));
					System.out.println("----------");
					break;

				}

			}
			
			String userInput;
			do {

				System.out.println("Enter (0) to return. Enter (1) to search again");
				 userInput = input.nextLine();

			} while (!userInput.equals("0") && !userInput.equals("1"));
			
			if (userInput.equals("0")) {
				db_read.close();
				lockerOptions(loggedInUser);
			}
			else if (userInput.equals("1")) {
				db_read.close();
				searchCredentials(loggedInUser);
			} 
		}

		else {
			System.out.println("Not Found");
			String userInput;
			do {

				System.out.println("Enter (0) to return. Enter (1) to try again");
				 userInput = input.nextLine();

			} while (!userInput.equals("0") && !userInput.equals("1"));
			
			if (userInput.equals("0")) {
				db_read.close();
				lockerOptions(loggedInUser);
			}
			else if (userInput.equals("1")) {
				db_read.close();
				searchCredentials(loggedInUser);
			} 
			
		}

	}

	public static void logOut() throws IOException {
		RunApp.initApp();
		RunApp.welcomeScreen();
		RunApp.signInOptions();
	}

	public static ArrayList<String> insertionSort(ArrayList<String> arr) {

		String temp = "";

		for (int i = 0; i < arr.size(); i++) {
			for (int j = i + 1; j < arr.size(); j++) {
				if (arr.get(i).compareToIgnoreCase(arr.get(j)) > 0) {
					temp = arr.get(i);
					arr.set(i, arr.get(j));
					arr.set(j, temp);

				}
			}
		}

		return arr;
	}

	public static int binarySearch(ArrayList<String> arr, String text) {

		ArrayList<String> sorted = new ArrayList<String>();
		sorted = insertionSort(arr);

		int l = 0, r = sorted.size() - 1;
		while (l <= r) {
			int m = l + (r - l) / 2;

			int res = text.compareTo(sorted.get(m));

			// Check if x is present at mid
			if (res == 0)
				return m;

			// If x greater, ignore left half
			if (res > 0)
				l = m + 1;

			// If x is smaller, ignore right half
			else
				r = m - 1;
		}

		return -1;
	}

	public static boolean checkRepeatedSite(String userName, String siteName) throws FileNotFoundException {

		File lockerFile = new File("files/users/" + userName + ".txt");
		db_read = new Scanner(lockerFile);
		String line;
		boolean found = false;

		while (db_read.hasNext()) {

			line = db_read.nextLine();

			if (line.equals(siteName)) {

				found = true;

				db_read.nextLine();
				db_read.nextLine();

			}

		}
		db_read.close();
		return found;
	}

}


		
	