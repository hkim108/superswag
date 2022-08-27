package pkg;
import java.util.*;
import java.io.*;

public class Message {
	private String author;
	private String subject;
	private String body;
	private int idNum;
	private String indent = " .";
	private String totalIndent ="";
	private int countIndent = 0;
	private int messageCount = 0;

	ArrayList<Message> replyList = new ArrayList<Message>();

	// Default Constructor
	public Message() {
		
	}
	
	// Parameterized Constructor
	public Message(String auth, String subj, String bod, int i) {
		author = auth;
		subject = subj;
		body = bod;
		idNum = i;
	}

	// This function is responsbile for printing the Message
	// (whether Topic or Reply), and all of the Message's "subtree" recursively:

	// After printing the Message with indentation n and appropriate format (see output details),
	// it will invoke itself recursively on all of the Replies inside its childList, 
	// incrementing the indentation value at each new level.

	// Note: Each indentation increment represents 2 spaces. e.g. if indentation ==  1, the reply should be indented 2 spaces, 
	// if it's 2, indent by 4 spaces, etc. 
	public void print(int indentation){
		messageCount++;
		
		for (int c = 0; c <indentation; c++) {
			totalIndent = indent + totalIndent;
			//countIndent--;
		}

		System.out.println(totalIndent + "Message #" + getId() + ": \"" + getSubject() + "\"" +     "size is:" + replyList.size());
		System.out.println(totalIndent + "From " + author + ": \"" + body + "\"");
		
		
		for (int x=0; x<replyList.size(); x++) {
			System.out.println("");
			replyList.get(x).print(countIndent);
		}
	
		


/*
		messageCount++;
		
		for (int c = 0; c <indentation; c++) {
			totalIndent = indent + totalIndent;
		}

		System.out.println(totalIndent + "Message #" + getId() + ": \"" + getSubject() + "\"");
		System.out.println(totalIndent + "From " + author + ": \"" + body + "\"");
		
		
		totalIndent = "";

		for (int x=0; x<replyList.size(); x++) {
			System.out.println("");
			countIndent++;
			replyList.get(x).print(countIndent);
			countIndent = 1;

		}
		countIndent++;
 */


		//System.out.println("tessdfdsdfst");
	}

	// Default function for inheritance
	public boolean isReply(){
		return false;
	}

	// Returns the subject String
	public String getSubject(){
		return subject; //
	} 

	// Returns the ID
	public int getId(){
		return idNum; //
	}

	// Adds a child pointer to the parent's childList.
	public void addChild(Message child){
		replyList.add(child);
	}

}
