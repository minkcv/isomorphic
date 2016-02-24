package gui;

import java.util.ArrayList;
import java.util.Scanner;

public class Message {
	private String text;
	private int charIndex = 0; // for current line
	private int currentLine = 0;
	private boolean finished;
	private int maxCharsPerLine;
	private ArrayList<String> brokenText; // broken up based on where newlines should go
	public Message(String text, int maxCharsPerLine){
		this.text = text;
		this.maxCharsPerLine = maxCharsPerLine;
		brokenText = new ArrayList<String>();

		Scanner s = new Scanner(text);
		int buildingLine = 0;
		brokenText.add("");
		while(s.hasNext()){
			String word = s.next();
			if(brokenText.get(buildingLine).length() + word.length() > maxCharsPerLine){ // should insert newline
				//add padding of spaces on right to meet maxCharsPerLine
				//brokenText.set(buildingLine, brokenText.get(buildingLine) + String.format("%" + word.length() + "s", " "));
				buildingLine++;
				brokenText.add("");
			}
			brokenText.set(buildingLine, brokenText.get(buildingLine) + word + " ");
		}
	}

	public void advanceText(){
		if(currentLine == brokenText.size() - 1 && charIndex >= brokenText.get(brokenText.size() - 1).length()){
			finished = true;
		}
		else{
			charIndex++;
			if(charIndex > brokenText.get(currentLine).length()){
				currentLine++;
				charIndex = 0;
			}
		}
	}

	public String[] getCurrentText(){
		String[] textLines = {"", "", ""};
		if(currentLine == 0){
			textLines[0] = brokenText.get(0).substring(0, charIndex);
		}
		else if(currentLine == 1){
			textLines[0] = brokenText.get(0);
			textLines[1] = brokenText.get(1).substring(0, charIndex);
		}
		else{
			for (int i = 0; i < textLines.length; i++) {
				textLines[0] = brokenText.get(currentLine - 2);
				textLines[1] = brokenText.get(currentLine - 1);
				textLines[2] = brokenText.get(currentLine).substring(0, charIndex);
			}
		}
		return textLines;
	}
	public boolean isFinished(){ return finished; }
}
