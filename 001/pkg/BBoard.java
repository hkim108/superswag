package pkg;
import java.util.*;
import java.io.*;

public class BBoard {		// This is your main file that connects all classes.
	// Think about what your global variables need to be.
	private String title;
	private ArrayList<String> userList = new ArrayList <String>();
	private User currentUser;
	private boolean valid = false;

	ArrayList<Message> messageList = new ArrayList<Message>();
	ArrayList<Message> replyList = new ArrayList<Message>();


	// Default constructor that creates a board with a defaulttitle, empty user and message lists,
	// and no current user
	public BBoard() {
		title = "Haley's Awesome BBoard";
	}

	// Same as the default constructor except it sets the title of the board
	public BBoard(String ttl) {	
		title = ttl;
	}

	// Gets a filename of a file that stores the user info in a given format (users.txt)
	// Opens and reads the file of all authorized users and passwords
	// Constructs a User object from each name/password pair, and populates the userList ArrayList.
	public void loadUsers(String inputFile) throws FileNotFoundException {
		File f = new File (inputFile);
		Scanner fileScan = new Scanner(f);

		while (fileScan.hasNextLine()) {
			String s = fileScan.nextLine().toString();
			int space = s.indexOf(" ");
			userList.add ((s.substring(0, space)));
			userList.add ((s.substring(space+1)));
		}
		System.out.println("\n" + title);
		login();
	}

	// Asks for and validates a user/password. 
	// This function asks for a username and a password, then checks the userList ArrayList for a matching User.
	// If a match is found, it sets currentUser to the identified User from the list
	// If not, it will keep asking until a match is found or the user types 'q' or 'Q' as username to quit
	// When the users chooses to quit, say "Bye!" and return from the login function
	public void login(){
		Scanner scanUser = new Scanner(System.in);
		while (!valid) {
			System.out.print("Enter your username ('Q' or 'q' to quit): ");
			String usr = scanUser.nextLine();
			int usrIndex = userList.indexOf(usr);
			if (usr.equals("q") || usr.equals("Q")) {
				System.out.print("Bye!");
				System.exit(0);
			}

			System.out.print("Enter your password: ");
			String pwd =  scanUser.nextLine();
			int pwdIndex = userList.indexOf(pwd);

			if ((usrIndex != -1 && pwdIndex != -1) && (usrIndex+1 == pwdIndex)) {
				currentUser = new User(usr, pwd);
				System.out.println("\nWelcome back " + currentUser.getUsername() + "!");
				valid = true;
			}
			else {
				System.out.println("Invalid username or password, try again.\n");
			}
		}

		// System.out.println("success");
		// System.out.println(valid);
		
	}
	
	// Contains main loop of Bulletin Board
	// IF and ONLY IF there is a valid currentUser, enter main loop, displaying menu items
	// --- Display Messages ('D' or 'd')
	// --- Add New Topic ('N' or 'n')
	// --- Add Reply ('R' or 'r')
	// --- Change Password ('P' or 'p')
	// --- Quit ('Q' or 'q')
	// With any wrong input, user is asked to try again
	// Q/q should reset the currentUser to 0 and then end return
	// Note: if login() did not set a valid currentUser, function must immediately return without showing menu
	public void run(){
		
		while (valid) {
			Scanner menu = new Scanner(System.in);
			System.out.print("\nMenu\n-  Display Messages ('D' or 'd')\n-  Add New Topic ('N' or 'n')\n-  Add New Reply to a Topic ('R' or 'r')\n-  Change Password ('P' or 'p')\n-  Quit ('Q' or 'q')\nChoose an action: ");
			String action = menu.next();

			if (action.equals("D") || action.equals("d")) {
				display();
			}
			else if (action.equals("N") || action.equals("n")) {
				addTopic();
			}
			else if (action.equals("R") || action.equals("r")) {
				addReply();
			}
			else if (action.equals("P") || action.equals("p")) {
				setPassword();
			}
			else if (action.equals("Q") || action.equals("q")) {
				System.out.print("Bye!");
				System.exit(0);
			}
			else {
				System.out.println("Invalid input - Please enter another.");
			}
		}
	}

	// Traverse the BBoard's message list, and invote the print function on Topic objects ONLY
	// It will then be the responsibility of the Topic object to invoke the print function recursively on its own replies
	// The BBoard display function will ignore all reply objects in its message list
	private void display(){
		//System.out.println(messageList.size());
		if (messageList.size() > 0) {
			for (int c=0; c < messageList.size(); c++) {
				if (!messageList.get(c).isReply()) {
					System.out.println("\n------------------------------------------------------------");
					messageList.get(c).print(0);
					System.out.println("------------------------------------------------------------");

				}
			}
		}

		else {
			System.out.println("\nNo messages to display\n");
		}
	}


	// This function asks the user to create a new Topic (i.e. the first message of a new discussion "thread")
	// Every Topic includes a subject (single line), and body (single line)

	/* 
	Subject: "Thanks"
	Body: "I love this bulletin board that you made!"
	*/

	// Each Topic also stores the username of currentUser; and message ID, which is (index of its Message + 1)

	// For example, the first message on the board will be a Topic who's index will be stored at 0 in the messageList ArrayList,
	// so its message ID will be (0+1) = 1
	// Once the Topic has been constructed, add it to the messageList
	// This should invoke your inheritance of Topic to Message
	private void addTopic(){

		Scanner topic = new Scanner(System.in);
		System.out.print("\nSubject: ");
		String sub = topic.nextLine();
		System.out.print("Body: ");
		String bod = topic.nextLine();


		//System.out.println(sub + "     " + bod);

		Topic t = new Topic(currentUser.getUsername(), sub, bod, messageList.size()+1);
		messageList.add(t);


	}

	// This function asks the user to enter a reply to a given Message (which may be either a Topic or a Reply, so we can handle nested replies).
	//		The addReply function first asks the user for the ID of the Message to which they are replying;
	//		if the number provided is greater than the size of messageList, it should output and error message and loop back,
	// 		continuing to ask for a valid Message ID number until the user enters it or -1.
	// 		(-1 returns to menu, any other negative number asks again for a valid ID number)
	
	// If the ID is valid, then the function asks for the body of the new message, 
	// and constructs the Reply, pushing back the Reply on to the messageList.
	// The subject of the Reply is a copy of the parent Topic's subject with the "Re: " prefix.
	// e.g., suppose the subject of message #9 was "Thanks", the user is replying to that message:


	/*
			Enter Message ID (-1 for Menu): 9
			Body: It was a pleasure implementing this!
	*/

	// Note: As before, the body ends when the user enters an empty line.
	// The above dialog will generate a reply that has "Re: Thanks" as its subject
	// and "It was a pleasure implementing this!" as its body.

	// How will we know what Topic this is a reply to?
	// In addition to keeping a pointer to all the Message objects in BBoard's messageList ArrayList
	// Every Message (wheather Topic or Reply) will also store an ArrayList of pointers to all of its Replies.
	// So whenever we build a Reply, we must immediately store this Message in the parent Message's list. 
	// The Reply's constructor should set the Reply's subject to "Re: " + its parent's subject.
	// Call the addChild function on the parent Message to push back the new Message (to the new Reply) to the parent's childList ArrayList.
	// Finally, push back the Message created to the BBoard's messageList. 
	// Note: When the user chooses to return to the menu, do not call run() again - just return from this addReply function. 
	private void addReply(){
		Scanner sc = new Scanner(System.in);
		System.out.print("\nEnter a Message ID that you want to reply to (-1 for Menu): ");
		int replyID = sc.nextInt();
		if (replyID == -1) {
			run(); //idk how to "return from this addReply function"
		}
		if (replyID >0 && replyID <= messageList.size()+1){
	
			String sub = "Re: " + messageList.get(replyID-1).getSubject();
			System.out.print("Body: ");
			String bod = sc.next();

			Reply r = new Reply(currentUser.getUsername(), sub, bod, messageList.size()+1);
			replyList.add(r);
			messageList.add(r);
			messageList.get(replyID-1).addChild(r);
		}
		else {
			System.out.println("Invalid Message ID!");
			addReply();
		}

		
	}

	// This function allows the user to change their current password.
	// The user is asked to provide the old password of the currentUser.
	// 		If the received password matches the currentUser password, then the user will be prompted to enter a new password.
	// 		If the received password doesn't match the currentUser password, then the user will be prompted to re-enter the password. 
	// 		The user is welcome to enter 'c' or 'C' to cancel the setting of a password and return to the menu.
	// Any password is allowed except 'c' or 'C' for allowing the user to quit out to the menu. 
	// Once entered, the user will be told "Password Accepted." and returned to the menu.
	private void setPassword(){
		Scanner sc = new Scanner(System.in);
		System.out.print("\nOld Password ('C' or 'c' for Menu): ");
		String oldPass = sc.next();

		if (currentUser.check(currentUser.getUsername(), oldPass)) {
			System.out.print("Please enter your new password: ");
			String newPass = sc.next();

			if (currentUser.setPassword(oldPass, newPass)) {
				System.out.println("Password Accepted.");
				currentUser = new User(currentUser.getUsername(), newPass);
				run();
			}
		}
		
		else if (oldPass.equals("c") || oldPass.equals("C")) {
			run();
		}

		else {
			System.out.println("Invalid Password, please re-enter");
			setPassword();
		}

	}

}
