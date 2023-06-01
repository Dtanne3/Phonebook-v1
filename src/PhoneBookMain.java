import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.IOException;
import java.util.Scanner;
import java.util.Map;
import java.util.HashMap;

public class PhoneBookMain {
	
	//Globals
	static int mode, submode, subS, modeDel, modeS = 0;
	static int start = 1,end = 3;
	static ContactList currentList = null;
	static String CLName, CLSearchName;
	static Map<String, String> CLContacts = null;
	static Map<String, ContactList> PBList; 
	static String[]nameSet = null;
	static String filename = "ContactListInfo.ser";
	//Methods
	static void printMenu (int mode) {
		clr();
		switch(mode)
		{
			case 0:
				System.out.println("--------------------------------------");
				System.out.print("PhoneBook Manager v1.\n"
						+ "Press 1 to select phonebook\n"
						+ "Press 2 to exit.\n");
				System.out.println("--------------------------------------");
			break;	
			case 1: 
				nameSet = PBList.keySet().toArray(new String[PBList.size()]);
				System.out.println("--------------------------------------");
				printPhonebookSet();
				System.out.println("--------------------------------------");
				if(modeDel != 1)
				{
					System.out.println("Press 1-3 to select phonebook\n" + 
							"Press 4 or 5 to turn page(4 = left / 5 = right)\n"
									+ "Press 6 to create new phonebook\n"
									+ "Press 7 to delete phonebook\n"
									+ "Press 0 to exit");
				}
				else
				{
					System.out.println("Press 1 - 3 to delete Phonebook\n"
							+ "Press 4 or 5 to turn pages (4 = left / 5 = right)\n"
							+ "Press 0 to exit deletion mode");
				}
			break;
			case 2:
				System.out.println("--------------------------------------");
				if(submode == 6) {createContactList();} 
				else {createContact();}
				System.out.println("--------------------------------------");
		    break;
			case 3:
				if(modeS == 1) 
				{
					CLContacts = currentList.search(CLSearchName);
					nameSet = currentList.search(CLSearchName).keySet().toArray(new String[currentList.getSize()]);
				}
				else 
				{
					CLContacts = currentList.getList();
					nameSet = currentList.getList().keySet().toArray(new String[currentList.getSize()]);
				}
				
				System.out.println(String.format("Current PhoneBook: %s", CLName));
				System.out.println("--------------------------------------");
				printContactList();
				System.out.println("--------------------------------------");
				if(modeDel != 1 && modeS != 1)
				{
					System.out.println("Press 1 to add contact\n"
							+ "Press 2 to delete contact\n"
							+ "Press 3 or 4 to turn page(3 = left / 4 = right)\n"
							+ "Press 5 to search a contact\n"
							+ "Press 0 to exit");
				}
				else if(modeDel == 1) 
				{
					System.out.println("Press 1 - 3 to delete contact\n" 
							+ "Press 4 or 5 to turn pages (4 = left / 5 = right)\n"
							+ "Press 0 to exit deletion mode");
				}
				else if(modeS == 1)
				{
					System.out.println("Press 1 or 2 to turn page (1 = left / 2 = right)\n"
							+ "Press 0 to exit");
				}
				break;
			case 4:
				System.out.println("--------------------------------------");
				if(modeDel == 1) {removeContact();}
				else {searchContact();}
				System.out.println("--------------------------------------");
			break;
			case 5:
				System.out.println("--------------------------------------");
				System.out.println("Exiting..");
				System.out.println("--------------------------------------");
				break;
				
		}
		
		
	}
	static void clr()
	{
		try
		{
			new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
		}catch(Exception E){
			System.out.println(E);
		}
	}
	
	
	static void printPhonebookSet()
	{
		if(nameSet.length > 0)
		{
			for(int i = start - 1; i <= end - 1; i++)
			{
				try
				{
					 System.out.println((i+1) + ": " + nameSet[i]);
				}catch(Exception E) {break;}
			}
		}
		else
		{
			System.out.println("No phonebooks available");
		}
	}
	static void printContactList()
	{
		if(currentList != null && currentList.getSize() > 0)
		{
			
			for(int i = start - 1; i <= end - 1; i++)
			{
				
					try 
					{		
						if(nameSet[i] != null && CLContacts.get(nameSet[i]) != null)
						{
							System.out.println(" [" + (i+1) + "] " 
									+ nameSet[i] + " : " + CLContacts.get(nameSet[i]));
						}
					}catch(Exception E) {break;}
			}
		}
		else
		{
			System.out.println("No contacts available");
		}
	}
	
	static void createContactList()
	{
		System.out.print("Enter List Name: ");
		Scanner s = new Scanner(System.in);
		String listName = s.nextLine();
		if(!PBList.containsKey(listName))
		{
			ContactList list = new ContactList();
			PBList.put(listName, list);
			savePB();
		}
	}
	static void createContact()
	{
		Scanner s = new Scanner(System.in);
		System.out.print("Name (no special characters allowed): ");
		String name = s.nextLine();
		System.out.print("Number (format: ###-###-####): ");
		String number = s.nextLine();
		
		currentList.addEntry(name, number);
		currentList.sort();
		savePB();
	}
	static void movePage(int dir)
	{
		if(dir == -1) 
		{
			if (start > 1) {start -= 3; end -= 3;}
		}
		else if (dir == 1) 
		{
			if(end < nameSet.length) {start += 3; end += 3;}
		}
	}
	
	static void removeContact()
	{
		System.out.println("Enter the name of the contact you wish to delete.");
		Scanner s = new Scanner(System.in);
		String rem = s.nextLine();
		currentList.removeEntry(rem);
		savePB();
		modeDel = 0;
		currentList.sort();
	}
	static void searchContact()
	{
		System.out.println("Enter the name of the contact[s] of which you want to find.");
		Scanner s = new Scanner(System.in);
		String ser = s.nextLine();
		CLSearchName = ser;
		modeS = 1;
	}
	
	static void savePB() 
	{
		try
		{
			FileOutputStream fileOut = new FileOutputStream(filename);
			ObjectOutputStream infoOut = new ObjectOutputStream(fileOut);
			infoOut.writeObject(PBList);
			infoOut.close(); fileOut.close();
			System.out.println("Info Saved");
		}
		catch(Exception E) {System.out.println("ERROR: Info Could Not Be Saved");}
	}
		
	static Boolean loadPB() 
	{
		System.out.println("Loading SaveFile");
		try
		{
			FileInputStream fileIn = new FileInputStream(filename);
			ObjectInputStream infoIn = new ObjectInputStream(fileIn);
			PBList = (HashMap) infoIn.readObject();
			infoIn.close(); fileIn.close();
			clr();
			System.out.println("SUCCESS: SaveFile Found");
			return true;
		}
		catch(Exception E) {System.out.println("ERROR: SaveFile Could Not Be Loaded"); }
		return false;
		
	}
	//Main
	public static void main(String[] args)
	{
		if(loadPB() == false) {PBList = new HashMap<String, ContactList>();}
		mode = 0;
		while(true) 
		{
			submode = 0;
			printMenu(mode);
			Scanner s = new Scanner(System.in);
			if(mode == 1)
			{
				try {submode = s.nextInt();}
				catch(Exception E) {mode = 1;}
				if(submode > 0 && submode < 4)
				{
					if(nameSet.length > 0) 
					{
						if(modeDel != 1)
						{
							try
							{
								CLName = nameSet[(start-1) + (submode-1)];
								currentList = PBList.get(CLName);
								start = 1; end = 3;
								submode = 0;
							}
							catch(Exception E) {System.out.println("");}
							if(currentList != null)
							{
								int secmode = 3;
								subS = -1;
								while(true)
								{
									printMenu(secmode);
									Scanner ss = new Scanner(System.in);
									try {subS = ss.nextInt();}
									catch(Exception E) {System.out.println("");}
									if(subS == 0)
									{
										
										if(modeDel != 1 && modeS != 1)
										{
											CLName = null;
											currentList = null;
											start = 1; end = 3;
											subS = -1;
											break;
										}
										else if(modeDel == 1 && modeS == 0)
										{
											modeDel = 0;
										}
										else if(modeS == 1 && modeDel == 0)
										{
											start = 1; end = 3;
											modeS = 0;
										}
									}
									
									else
									{
										
										
										if(modeS == 0)
										{
											switch(subS)
											{
											case 1:
												printMenu(2);
												break;
											case 2:
												modeDel = 1;
												printMenu(4);
												break;
											case 3:
												movePage(-1);
												break;
											case 4:
												movePage(1);
												break;
											case 5:
												printMenu(4);
												secmode = 3;
												break;
											}
										}
										else
										{
											switch(subS)
											{
											case 1:
												movePage(-1);
												break;
											case 2:
												movePage(1);
												break;
											}
										}
										
									}
								}
							}
						}
						else 
						{
							try 
							{
								PBList.remove(nameSet[(start-1) + (submode-1)]);
							}
							catch(Exception E)
							{
								System.out.println("");
							}
							modeDel = 0;
						}
					}
					
				}
				else
				{
					switch(submode)
					{
					case 4:
						movePage(-1);
						break;
					case 5:
						movePage(1);
						break;
					case 6:
						printMenu(2);
						break;
					case 7:
						modeDel = 1;
						break;
					case 0:
						start = 1; end = 3;
						mode = 0;
						break;
					
					}
				}
			}
			else
			{
				try {mode = s.nextInt();}
				catch(Exception E) {mode = 1;}
				if(mode == 2) {printMenu(5); break;}
			}
		}
		return;
	}
}
