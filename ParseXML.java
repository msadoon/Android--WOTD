package mkat.apps.wotd;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.util.Log;

public class ParseXML extends DefaultHandler{

	boolean rev = false;
	String Word, Definition = "";
	int orCounter, backOr, charCount, deleteChars = 0;
	int defNum = 1;
	boolean duplicate, word, countCalls, endBraces = false;
	StringBuilder WordSB = new StringBuilder("");
	StringBuilder DefinitionSB = new StringBuilder("");
	String currentValue = "";
	StringBuilder test = new StringBuilder("");
	
	public String getWord(){
		return Word;
	}
	
	public String getDefinition(){
		return Definition;
	}
	
	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		// TODO Auto-generated method stub
		currentValue = "";
		countCalls = false;
		if (qName=="rev"){
			rev = true;
			countCalls = true;
		}
	}
	
	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		// TODO Auto-generated method stub
		if (countCalls){
			
			if (endBraces != true) {
				DefinitionSB.append(defNum);
				DefinitionSB.append('.');
				DefinitionSB.append(" ");
			}
			
		char[] ch = currentValue.toCharArray(); 
		//char[] chUnchanged = ch;
		currentValue="";
		int i =0;
		int length = ch.length;
		Log.i("entire definition tag", Integer.toString(length));
		
	
		
		while((i < ch.length-1)&& endBraces!=true){
			test  = new StringBuilder("");
			for (int y = i; y<length;y++){
				test.append(ch[y]);
			}
			Log.i("entire definition tag", test.toString());
			if (ch[i]=='|'){
				//Log.i("value", "OrMethod1");
				//Log.i("value", Integer.toString(i));
					orMethod(ch, i+1, length);		
					
			}//A
			else if ((ch[i]=='[')&&(ch[i+1]=='[')){
				//Log.i("value", "WikiLinkMethod1");	
				wikiLink(ch, i+2, length);
				
					
			}//B
			else if ((ch[i]==']')&&(ch[i+1]==']')){
				//Log.i("value", "WikiLinkEndMethod1");	
				wikiLinkEnd(ch, i+2, length); 
				
			}//C
			else if (ch[i]=='#'){
				//Log.i("value", "#Method1");
				anotherDef(ch, i+1, length);
				
			}//D
			else if ((ch[i]=='\'')&&(ch[i+1]=='\'')&&(ch[i+2]=='\'')){
				//Log.i("value", "#Method1");
				i += 2;
				
			}//E bold italics ignored
			i++;
			//Log.i("value", "This is the length" + Integer.toString(length));
			if (i == (ch.length-2)){
				endBraces = true;
			}
		}
		
		Word = WordSB.toString();
		
		if (endBraces == true){
			deleteChars = 0;
			endBraces = false;
			for (charCount = ch.length-1; charCount>0 && backOr <2; charCount--){
				
					if (ch[charCount] == '|') {backOr++;}
					if (ch[charCount] != '}') {
						deleteChars++;
						
					}
					else if (ch[charCount] != '|') {
						deleteChars++;
						
					}
					if (ch[charCount] == '|' | ch[charCount] == '}'){
							deleteChars--;
					}
					
					}
			
			DefinitionSB.delete(DefinitionSB.length()-deleteChars, DefinitionSB.length());
			DefinitionSB.append('.');
			Definition = DefinitionSB.toString();
			
			}	
		}
			
		
	}

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		// TODO Auto-generated method stub
		if(rev){
			currentValue = currentValue + new String(ch, start, length);
			
		} 
	}
	
	public void orMethod(char[] ch, int b, int length) {
		orCounter++;
		if (orCounter==2){
			if (endBraces != true) {DefinitionSB.append('(');}
		}
		if (orCounter==3){
			if (endBraces != true) {DefinitionSB.append(')');}
			if (endBraces != true) {DefinitionSB.append(' ');}
		}
		if (word==false){//if word = false, set word = true, read text into word until you hit a |, [[, ]], #
			word=true;
			int  i = b;
			//for (int i = b; i<length; i++){
				
			while((i < ch.length-1) && endBraces!=true){
					test  = new StringBuilder("");
					for (int y = i; y<length;y++){
						test.append(ch[y]);
						//break;
					}
					//Log.i("entire definition tag", test.toString());
					if (ch[i]=='|'){
						//Log.i("value", "OrMethod2a");	
						//Log.i("value", Integer.toString(i));
						orMethod(ch, i+1, length);	
							//break;
					}//A
					else if ((ch[i]=='[')&&(ch[i+1]=='[')){
						//Log.i("value", "WikiLinkMethod2a");	
						wikiLink(ch, i+2, length);
							//break;
					}//B
					else if ((ch[i]==']')&&(ch[i+1]==']')){
						//Log.i("value", "WikiLinkEndMethod2a");	
						wikiLinkEnd(ch, i+2, length);
							//break;
					}//C
					else if (ch[i]=='#'){
						//Log.i("value", "#Method2a");
						anotherDef(ch, i+1, length);
						//break;
					}//D
					else if ((ch[i]=='\'')&&(ch[i+1]=='\'')&&(ch[i+2]=='\'')){
						//Log.i("value", "#Method1");
						i += 2;
						
					}//E bold italics ignored
					if (endBraces != true) {WordSB.append(ch[i]);}
					i++;
					if (i == (ch.length-2)){
						endBraces = true;
					}
				}
			//}
			
		}
		if (word==true && duplicate==false){//if word = true, read the text into definition until you hit a |, [[, ]], #
			int i =b;
			//for (int i = b; i<length; i++){
				while((i < ch.length-1) && endBraces!=true){
					test  = new StringBuilder("");
					for (int y = i; y<length;y++){
						test.append(ch[y]);
					}
					//Log.i("entire definition tag", test.toString());
					if (ch[i]=='|'){
							//Log.i("value", "OrMethod2b");
							//Log.i("value", Integer.toString(i));
							orMethod(ch, i+1, length);	
							//break;
							
					}//A
					else if ((ch[i]=='[')&&(ch[i+1]=='[')){
							//Log.i("value", "wikilinkMethod2b");
							wikiLink(ch, i+2, length);
							//break;
					}//B
					else if ((ch[i]==']')&&(ch[i+1]==']')){
							//Log.i("value", "wikiLinkEndMethod2b");
							wikiLinkEnd(ch, i+2, length);
							//break;
					}//C
					else if (ch[i]=='#'){
						//Log.i("value", "#Method2b");
						anotherDef(ch, i+1, length);
						//break;
					}//D
					else if ((ch[i]=='\'')&&(ch[i+1]=='\'')&&(ch[i+2]=='\'')){
						//Log.i("value", "#Method1");
						i += 2;
					}//E bold italics ignored
					if (endBraces != true) {DefinitionSB.append(ch[i]);}
					i++;
					//Log.i("value", Integer.toString(i));
					if (i == (ch.length-2)){
						endBraces = true;
					}
				
				}
			//}
			
		}
		if (word==true && duplicate == true){//if word = true, duplicate = true, add nothing to Definition, but keep reading until you hit a |, [[, ]], #
			
			int i = b;
				while((i < ch.length-1) && endBraces!=true){
					test  = new StringBuilder("");
					for (int y = i; y<length;y++){
						test.append(ch[y]);
					}
					//Log.i("entire definition tag", test.toString());
					if (ch[i]=='|'){
						//Log.i("value", "OrMethod2c");
						duplicate = false;	
						orMethod(ch, i+1, length);	
							//break;
					}//A
					else if ((ch[i]=='[')&&(ch[i+1]=='[')){
						//Log.i("value", "WikiLinkMethod2c");
							wikiLink(ch, i+2, length);
							//break;
					}//B
					else if ((ch[i]==']')&&(ch[i+1]==']')){
						//Log.i("value", "WikiLinkEndMethod2c");	
						wikiLinkEnd(ch, i+2, length);
							//break;
					}//C
					else if (ch[i]=='#'){
						//Log.i("value", "#Method2c");
						anotherDef(ch, i+1, length);
						//break;
					}//D
					else if ((ch[i]=='\'')&&(ch[i+1]=='\'')&&(ch[i+2]=='\'')){
						//Log.i("value", "#Method1");
						i += 2;
						
					}//E bold italics ignored
					//DefinitionSB.append(ch[b])
					i++;
					if (i == (ch.length-2)){
						endBraces = true;
					}
					
				}
			
			
		}
		
	}

	public void wikiLink(char[] ch, int b, int length) {
			int i = b;
			if (ch[i]=='w'&&(ch[i+1]==':')){
				//Log.i("value", "#Method3");
				i += 2;
			}//E //takes care of the w: tag inside the [[
				while((i < ch.length-1)&& endBraces!=true){
					test  = new StringBuilder("");
					for (int y = i; y<length;y++){
						test.append(ch[y]);
					}
					//Log.i("entire definition tag", test.toString());
					if (ch[i]=='|'){
						//Log.i("value", "OrMethod3");	
						duplicate = true;
						orMethod(ch, i+1, length);	
							
					}//A
					else if ((ch[i]=='[')&&(ch[i+1]=='[')){
						//Log.i("value", "WikiLinkMethod3");	
						wikiLink(ch, i+2, length);
							
					}//B
					else if ((ch[i]==']')&&(ch[i+1]==']')){
						//Log.i("value", "WikiLinkEndMethod3");	
						wikiLinkEnd(ch, i+2, length);
							
					}//C
					else if (ch[i]=='#'){
						//Log.i("value", "#Method3");
						anotherDef(ch, i+1, length);
						
					}//D
					else if ((ch[i]=='\'')&&(ch[i+1]=='\'')&&(ch[i+2]=='\'')){
						//Log.i("value", "#Method1");
						i += 2;
					}//E bold italics ignored
					if (endBraces != true) {DefinitionSB.append(ch[i]);}
					i++;
					if (i == (ch.length-2)){
						endBraces = true;
					}
					
				}
			
			
		
	}
	
	public void wikiLinkEnd(char[] ch, int b, int length) {
		if (duplicate == true){
			duplicate = false;
		}
		
			int i= b;
			while((i < ch.length-1) && endBraces!=true){
				test  = new StringBuilder("");
				for (int y = i; y<length;y++){
					test.append(ch[y]);
				}
				Log.i("entire definition tag", test.toString());
				if (ch[i]=='|'){
					Log.i("value", "OrMethod4");	
					orMethod(ch, i+1, length);	
						
						//duplicate = true;
				}//A
				else if ((ch[i]=='[')&&(ch[i+1]=='[')){
					Log.i("value", "WikiLinkMethod4");	
					wikiLink(ch, i+2, length);
						
				}//B
				else if ((ch[i]==']')&&(ch[i+1]==']')){
					Log.i("value", "WikiLinkEndMethod4");	
					wikiLinkEnd(ch, i+2, length); 
						
				}//C
				else if (ch[i]=='#'){
					Log.i("value", "#Method4");
					anotherDef(ch, i+1, length);
					
				}//D
				else if ((ch[i]=='\'')&&(ch[i+1]=='\'')&&(ch[i+2]=='\'')){
					//Log.i("value", "#Method1");
					i += 2;
				}//E bold italics ignored
				if (endBraces != true) {DefinitionSB.append(ch[i]);}
				i++;
				if (i == (ch.length-2)){
					endBraces = true;
				}
			}
		
	}
	
	public void anotherDef(char[] ch, int b, int length) {
		
		if (endBraces != true) {DefinitionSB.append('\n');}
		defNum++;
		if (endBraces != true) {
			DefinitionSB.append(defNum);
			DefinitionSB.append('.');
			DefinitionSB.append(' ');
		}
		int i= b;
		
			while((i < ch.length-1) && endBraces!=true){
				test  = new StringBuilder("");
				for (int y = i; y<length;y++){
					test.append(ch[y]);
					
				}
				Log.i("entire definition tag", test.toString());
				if (ch[i]=='|'){
					Log.i("value", "OrMethod5");	
					orMethod(ch, i+1, length);	
						
						//duplicate = true;
				}//A
				else if ((ch[i]=='[')&&(ch[i+1]=='[')){
					Log.i("value", "WikilinkMethod5");	
					wikiLink(ch, i+2, length);
						
				}//B
				else if ((ch[i]==']')&&(ch[i+1]==']')){
					Log.i("value", "wikiLinkEndMethod5");	
					wikiLinkEnd(ch, i+2, length); 
						
						
				}//C
				else if (ch[i]=='#'){
					Log.i("value", "#Method5");
					anotherDef(ch, i+1, length);
					
				}//D
				else if ((ch[i]=='\'')&&(ch[i+1]=='\'')&&(ch[i+2]=='\'')){
					//Log.i("value", "#Method1");
					i += 2;
				}//E bold italics ignored
				if (endBraces != true) {DefinitionSB.append(ch[i]);}
				i++;
				if (i == (ch.length-2)){
					endBraces = true;
				}
				
			}
		
	}
	
	
	

}
