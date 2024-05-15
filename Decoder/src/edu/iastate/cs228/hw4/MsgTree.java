package edu.iastate.cs228.hw4;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/*
 * Implementation of a class used to decode archived files with 
 * encryptions and bit messages using a binary tree based algorithm.
 * 
 * @author - Hrishikesha Kyathsandra
 * @version - 1.0.
 */
public class MsgTree 
{
	public static void main(String[] args) throws FileNotFoundException
	{
		System.out.print("Please enter the name of the file to decode: ");
		Scanner scnr = new Scanner(System.in);
		String fileName = scnr.next();
		scnr.close();
		File f = new File(fileName);
		Scanner fScan = new Scanner(f);

		String encode = "";
		
		while (fScan.hasNextLine()) {  // extracting info from the file.
			encode += fScan.nextLine();
			if (fScan.hasNextLine()) {
				encode += "\n";
			}
		}

		fScan.close();
		
		// extracting the encryption and bit archive message.
		int encodeLastIndex = encode.lastIndexOf('\n');
		String encryptedMessage = encode.substring(0, encodeLastIndex);
		String bitCode = encode.substring(encodeLastIndex);

		MsgTree root = new MsgTree(encryptedMessage);
		
		System.out.print("\n");
		System.out.println("-----------------------------");
		System.out.println(" Charachter    Code");
		System.out.println("-----------------------------");
		printCodes(root, encryptedMessage);
		System.out.println("-----------------------------");
		System.out.println("");
		
		System.out.print("MESSAGE: "+"\n");
		root.decode(root, bitCode);
	}

	/*
	 * Data which the node holds.
	 */
	public char payloadChar;

	/*
	 * Left subtree.
	 */
	public MsgTree left;

	/*
	 * Right subtree.
	 */
	public MsgTree right;

	/*
	 * Static character index for recursive solution.
	 */
	private static int charIndex = 0;

	/*
	 * Recursively constructs a message tree.
	 * 
	 * @param encodingString - encoded string.
	 */
	public MsgTree(String encodingString) 
	{
		if (encodingString == null || encodingString.length()<=1 || charIndex >= encodingString.length()) return;
			
		char c = encodingString.charAt(charIndex++);
		this.payloadChar = c;

		if (c != '^') return;

		left = new MsgTree(encodingString);
		right = new MsgTree(encodingString);
	}

	/*
	 * Constructor for a single node with no children.
	 * 
	 * @param payloadChar - data of the node.
	 */
	public MsgTree(char payloadChar) 
	{
		this.payloadChar = payloadChar;
		this.left = null;
		this.right = null;
	}

	/*
	 * Method to print charachters and their respective binary codes.
	 * 
	 * @param root - root of the binary tree.
	 * @param code - string of code.
	 */
	public static void printCodes(MsgTree root, String code) 
	{
		if (code.length() > 0 && code.charAt(0) != '1' && code.charAt(0) != '0') {
			code = "";
		}

		if (root.left == null && root.right == null) {
			
			char c = root.payloadChar;
			
			if (c == '\n') {
				System.out.println("    \\n         " + code);
			} 
			else {
				System.out.println("     " + c + "         " + code);
			}
		}
		
		else 
		{
			if (root.left != null) {
				printCodes(root.left, code + "0");
			}
			if (root.right != null) {
				printCodes(root.right, code + "1");
			}
		}
	}

	/*
	 * Method to decode the encrypted message
	 * 
	 * @param codes - binary tree of encoded string.
	 * @param msg - bit representation of the archived message.
	 */
	public void decode(MsgTree codes, String msg) 
	{
		MsgTree root = codes;
		MsgTree node = root;

		String message = "";

		char[] arr = msg.toCharArray();

		for (char i : arr) 
		{
			if (i == '0') {
				node = node.left;
			} 
			else if (i == '1') {
				node = node.right;
			}
			if (node.payloadChar != '^') {
				message += node.payloadChar;
				node = root;
			}
		}
		
		System.out.println(message);
		
		// printing the stats of the process.
		System.out.println("");
		statistc(msg, message);
		System.out.println("");
		
	}
	
	/*
	 * Statistics of decoding the message.
	 * 
	 * @param uncompressed - uncompressed string.
	 * @param compressed - compressed string.
	 */
	private void statistc(String uncompressed, String compressedString) 
	{
		System.out.println("STATISTICS:");
		System.out.println("-------------------------------");
		System.out.printf("Avg bits/char:       %5.1f", uncompressed.length() / (double) compressedString.length());
		System.out.println("");
		System.out.println("Total Characters:      " + compressedString.length());
		System.out.printf("Space Saving:        %6.1f" , (1 - compressedString.length() / (double) uncompressed.length()) * 100);
		System.out.println("");
		System.out.print("-------------------------------");
	}
}
