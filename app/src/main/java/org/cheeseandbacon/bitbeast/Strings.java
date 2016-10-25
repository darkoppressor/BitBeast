package org.cheeseandbacon.bitbeast;

public class Strings{
	public static String first_letter_capital(String input){
		String output=input.toLowerCase();
		
		if(output.length()>1){
			char first_letter=output.charAt(0);
			output=output.substring(1,output.length());
			output=Character.toUpperCase(first_letter)+output;
		}
		
		return output;
	}
	
	public static String first_letter_capital_all_words(String input){
		String output="";
		
		String[] strings=input.split(" ");
		
		for(int i=0;i<strings.length;i++){
			output+=first_letter_capital(strings[i])+" ";
		}
		
		//Remove the ending space.
		if(output.length()>0){
			output=output.substring(0,output.length()-1);
		}
		
		return output;
	}
	
	public static String newline_to_br(String input){
		return input.replace("\n","<br>");
	}
	
	public static String br_to_newline(String input){
		return input.replace("<br>","\n");
	}
}
