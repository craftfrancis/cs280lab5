//Troy Dinga, Francis Craft, and Alexander Means
//September 25th, 2015
//
//The TreeHouse program, or git-TreeHouse, as we'll be referring to it, performs several 
//useful functions for the user. The first is that it can create a tree beginning at a 
//user-specified root directory encompassing all subdirectories of this directory, all 
//subdirectories of those subdirectories, etc. 

import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.ArrayList;

public class gitStati {

	//Constructor
	private static class Node {
		String path; //Filepath
		Node parent; //Parent Directory
		boolean isDir = false; //Directory?
		boolean isRepo = false; //Git repository?
		ArrayList<Node> children = new ArrayList<Node>(); //Child directories.
	}//Node

	public static void main(String[] args) {
		//Escape sequences to color the terminal output.
		final String ANSI_GREEN = "\u001B[32m";
		final String ANSI_RESET = "\u001B[0m";

		Scanner in = new Scanner(System.in); 		//Scanner to read in user's root directory.

		//Prompt the user for, and store, their chosen root directory.
		System.out.print("Please enter the root directory you'd like to begin analyzing from: ");
		File rootFile = new File(in.nextLine()); 	

		//Making sure that they've actually specified a directory.
		if (rootFile.isDirectory()) {
			//The first Node in the tree (the root) is this directory.
			Node rootNode = new Node();
			rootNode.path = rootFile.getAbsolutePath();

			//Build the tree from this root.
			buildTree(rootFile,rootNode);
		
			//Will later add a 'verbose' option to determine whether or not this 
			//is printed.
			printTree(rootNode, ANSI_GREEN, ANSI_RESET);

			//Providing the user with choices as to how they may continue using or cease using the program.
			System.out.print("\n");
			System.out.println("What would you like to do, now? (Type and return number of chosen option.)");
			System.out.println("\n1) List all git repositories in the tree created?");
			System.out.println("2) Display the information provided by the 'git status' command for some or all repositories?");
			System.out.println("3) Exit program?");
			System.out.print("\nPlease enter your choice, here (do not include paren in choice): ");

			String choice = in.nextLine();
			System.out.print("\n");

			//So long as the user doesn't wish to end the program yet.
			while (!choice.equals("3")) {
				switch (choice) {
					case "1" :
						//List the repos in the main tree.
						for (Node repo : findRepos(rootNode)) {
							System.out.println(ANSI_GREEN + repo.path + ANSI_RESET);
						}
						break;
					case "2":
						//Offer options for how many repositories this search should handle.
						System.out.println("\n1) Would you like to select a specific repository?");
						System.out.println("2) Have git status run on all repositories in the tree?");
						System.out.print("\nPlease select one of the above options: ");

						//User response.
						choice = in.nextLine();

						switch (choice) {
							case "1" :
								//User must choose a repository to check the status of.
								System.out.print("\nPlease enter the repository you wish to check: ");
								File gitToCheck = new File(in.nextLine());

								try {
									//A process to hold the execution of the 'git status' command.
									Process p = Runtime.getRuntime().exec("git status", null, gitToCheck);

									//From the following topic thread on Stack Overflow:
									//http://stackoverflow.com/questions/236737/making-a-system-call-that-returns-the-stdout-output-as-a-string/236873#236873
									//All due credit to this individual for this constructor and the methods and 
									//variable usage involving it, all of which can be seen in StreamGobbler.java, 
									//in this same directory.
		
									StreamGobbler errorGobbler = new StreamGobbler(p.getErrorStream(), "ERROR");
									StreamGobbler outputGobbler = new StreamGobbler(p.getInputStream(), ">");

									//Start the streams.
									errorGobbler.start();
									outputGobbler.start();

									//Give the streams a little time.
									int exitVal = p.waitFor();

									//Output what ought to be output.
									String output = outputGobbler.getOutput();
									System.out.println("\n" + output);

								} catch(Exception e) {
									System.out.println("\nSomething went wrong running the git command!");
								}

								break;
							case "2" :
								//Unfortunately, due to time constraints that had arisen due to the search
								//for an effective way of achieving this functionality, this option is not yet 
								//available. However, to implement this will be an almost trivial matter, given 
								//the methods and previous coding already available for use.
								break;
						}

						break;
					default :
						//The user has entered an option that does not exist.
						System.out.println("\nI'm sorry, that's not one of the listed options!");
						break;
				}
				
				//The user can continue to use this system until they are ready to terminate the execution.
				System.out.print("\nWhat would you like to do now?");
				System.out.println("\n1) List all git repositories in the tree created?");
				System.out.println("2) Display the information provided by the 'git status' command for some or all repositories?");
				System.out.println("3) Exit program?");
				System.out.print("\nPlease enter your choice, here (do not include paren in choice): ");

				choice = in.nextLine();
			}

			//Goodbye!
			System.out.println("\nThank you for using git-TreeHouse!");
		} else {
			//If the user has not entered a directory, we cannot really continue.
			System.out.println("Sorry, but you must enter a valid directory!");
		}
	}


	/**
	 * The buildTree method does not return any value, itself, but, during the 
	 * course of it's execution, builds a directory tree from the root outward. 
	 * For each item in the current directory (currentDir), the method checks to 
	 * see if it's a directory or not, if it is, it becomes a new Node in the tree 
	 * is created, and the relationship between parent and child is established. 
	 * The method is then called recursively, using this new node and directory, 
	 * repeating this process until a directory containing no other directories is 
	 * reached, and the recursive process backs up to the previous call. The method 
	 * terminates once all items in the root directory have been examined fully.
	 */
	static void buildTree(File currentDir, Node currentNode) {

		//For each item in the current directory.
		for (String item : currentDir.list()) {

			//Each item is some type of file.
			File possibleDir = new File(currentDir.getAbsolutePath() + File.separatorChar + item);

			//Is the file a directory?
			if (possibleDir.isDirectory()) {
				
				//If so, we have a new Node in the tree!
				Node foundNode = new Node();
				foundNode.isDir = true;
				foundNode.parent = currentNode;
				foundNode.path = possibleDir.getAbsolutePath();

				//Keep record of each Node's subdirectories.
				currentNode.children.add(foundNode);

				//Is the current directory a git repository?
				if (possibleDir.getName().equals(".git")) {
					currentNode.isRepo = true;
				}

				//Recursive call to buildTree.
				buildTree(possibleDir, foundNode);
			}
		}
	}

	/**
	 * The findRepos method takes the root of the tree built in the 
	 * buildTree method, and, recursively, lists any directory nodes 
	 * in the tree that happen to be git repositories. The actual list 
	 * of these repositories is returned after the method terminates.
	 */
	static Node[] findRepos(Node currentDir) {
		//To hold the, as of yet, unknown number of repositories.
		ArrayList<Node> repos = new ArrayList<Node>();
		Node[] reposToAdd; //To be added to the ArrayList later on.

		//Making sure that the node in question has subdirectories.
		if (currentDir.children.size() > 0) {
			//For every child of the current directory.
			for (Node child : currentDir.children) {
				reposToAdd = findRepos(child); //Recursive call.
				
				//Add any repos found to the ArrayList.
				for (Node repo : reposToAdd) {
					repos.add(repo);
				}
			}

			//Is the current directory a repository?
			if (currentDir.isRepo) {
				repos.add(currentDir);
			}
		} else {
			//With no children, there's only one question:
			//Is the current directory a repository?
			if (currentDir.isRepo) {
				repos.add(currentDir);
			}
		}

		//Return an array holding all repos found.
		Node[] reposFound = new Node[repos.size()];
		return repos.toArray(reposFound);
	}

	/**
	 * In the same recursive manner as findRepos, but without any 
	 * return type, printTree prints out all nodes in the tree built 
	 * off of a specified root. Git repositories are colored green.
	 */
	static void printTree(Node root, String green, String reset) {
		//The node has children.
		if (root.children.size() > 0) {
			//Recursive call for each child present.
			for (Node child : root.children) {
				printTree(child, green, reset);
			}

			//The directory is exhausted of children;
			//Print the current node, but color it if 
			//it is a git repository.
			if(root.isRepo) {
				System.out.println(green + root.path + reset);
			} else {
				System.out.println(root.path);
			}
		} else {
			//There are no children to call the method with.
			//Print the current node, but color it if 
			//it is a git repository.
			if(root.isRepo) {
				System.out.println(green + root.path + reset);
			} else {
				System.out.println(root.path);
			}
		}
	}
}

