import java.util.Comparator;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Map;
import java.util.HashMap;

public class ContactList {
	
	private class Contact 
	{

		private String name;
		private String phone;
		
		public Contact(){}
		public String getName()
		{
			if (phone != null) {return name;}
			return null;
		}
		public String getPhoneNumber()
		{
			if(phone != null){ return phone; }
			return null;
		}
		public void setName(String n_input)
		{
			name = n_input;
		}
		public void setPhoneNumber(String p_input)
		{
			phone = p_input;
		}
		
	}

	private ArrayList<Contact> cList;

	public ContactList()
	{
		cList = new ArrayList<Contact>();
	}
	
 	public Map<String, String> search(String input)
	{
 		Map<String, String> m = new HashMap<String, String>();
 		String reg = String.format("%s.*", input.toLowerCase());
 		Pattern format = Pattern.compile(reg);
 		for(int i = 0; i < cList.size(); i++)
		{
 			Contact obj = cList.get(i);
 			Matcher matcher = format.matcher(obj.getName().toLowerCase());
 			if(matcher.find())
 			{
 				m.put(obj.getName(), obj.getPhoneNumber());
 			}
 			
		}
 		return m;
 		
	}
 	public void sort()
	{
		if(cList.size()>1) 
		{
			Comparator<Contact> c = new Comparator<Contact>() {
				public int compare(Contact c1, Contact c2)
				{
					int s = (c1.getName().length() < c2.getName().length())? c1.getName().length() : c2.getName().length();
					for(int i = 0; i < s; i++)
					{
						if((int)c1.getName().charAt(i) < (int)c2.getName().charAt(i))
						{
							return -1;
						}
						else if((int)c1.getName().charAt(i) > (int)c2.getName().charAt(i)) 
						{
							return 1;
						}
					}
					return 0;
				}
			};
			
			cList.sort(c);
			c = null;
		}
		else {System.out.println("List contains one object");}
		
	}
	public Map<String, String> getList()
	{
		Map<String, String> m = new HashMap<>();
		for(int i = 0; i < cList.size(); i++)
		{
			m.put(cList.get(i).getName(), cList.get(i).getPhoneNumber());
		}
		return m;
	}
	public void addEntry(String name, String number)
	{
		Contact c = new Contact();
		if(name.length() > 0) {
			String reg = "[^A-Za-z ]";
			Pattern format = Pattern.compile(reg);
			Matcher matcher = format.matcher(name);
			if(!matcher.find())
			{
				c.setName(name);
			}
			else
			{
				System.out.println("Incorrect Format (no num or special char allowed)");
			}
		}
		if(number.length() > 0)
		{
			String reg = "^[0-9]{3}-[0-9]{3}-[0-9]{4}$";
			Pattern format = Pattern.compile(reg);
			Matcher matcher = format.matcher(number);
			if(matcher.find()) 
			{
				c.setPhoneNumber(number);
			}
			else
			{
				System.out.println("Incorrect Format (must be ###-###-####)");
			}
		}
		if(c.getName() != null && c.getPhoneNumber() != null) {cList.add(c);}
		else {c = null;}
	}
	public void removeEntry(String name)
	{
		String reg = String.format("^\\b%s\\b", name);
		Pattern format = Pattern.compile(reg);
		int targetIndex = -1;
		int foundCount = 0;
		for(int i = 0; i < cList.size(); i++)
 		{
 			Contact obj = cList.get(i);
 			Matcher matcher = format.matcher(obj.getName());
 			if(matcher.find())
 			{
 				++foundCount;
 				targetIndex = i;
 			}
 		}
 		if(foundCount > 1 || foundCount == 0)
 		{
 			System.out.println(String.format("ERROR: Either more than one contact or no contact that's named %s has been found!  ", name));
 		}
 		else 
 		{
 			System.out.println("name found");
			cList.remove(targetIndex);
		}
	}
	public int getSize()
	{
		return cList.size();
	}
}
