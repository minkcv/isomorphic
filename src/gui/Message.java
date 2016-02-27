package gui;

import java.util.ArrayList;
import java.util.Scanner;

public class Message {
	private String text;
	private int charIndex = 0; // for current line
	private int currentLine = 0;
	private int linesUntilAdvance = 3;
	private boolean finished;
	private boolean dismissed;
	private boolean waitForAdvance;
	private final static int maxCharsPerLine = 40; // assuming 32pt monospace font, text padding 16, left/right margin 5, 800 screen width
	private ArrayList<String> brokenText; // broken up based on where newlines should go
	public Message(String text){
		this.text = text;
		brokenText = new ArrayList<String>();

		// break the text on spaces with maxCharsPerLine
		Scanner s = new Scanner(text);
		int buildingLine = 0;
		brokenText.add("");
		while(s.hasNext()){
			String word = s.next();
			if(brokenText.get(buildingLine).length() + word.length() > maxCharsPerLine){ // should insert newline
				buildingLine++;
				brokenText.add("");
			}
			brokenText.set(buildingLine, brokenText.get(buildingLine) + word + " ");
		}
		s.close();
	}

	// advance a character and stop at linesUntilAdvance
	public void advanceText(){
		if(currentLine == brokenText.size() - 1 && charIndex >= brokenText.get(brokenText.size() - 1).length()){
			finished = true;
		}
		if(!finished && linesUntilAdvance > 0){
			if(charIndex >= brokenText.get(currentLine).length()){
				linesUntilAdvance--;
				if(linesUntilAdvance > 0){
					currentLine++;
					charIndex = 0;
				}
			}
			else{
				charIndex++;
			}
		}
		else if(linesUntilAdvance <= 0){
			waitForAdvance = true;
		}
	}

	// advance a few lines
	public void advanceLine(){
		if(brokenText.size() > 2){
			linesUntilAdvance = 3;
		}
		else if(brokenText.size() == 2){
			linesUntilAdvance = 2;
		}
		else{
			linesUntilAdvance = 1;
		}
		waitForAdvance = false;
	}

	// reset markers so the message can be read again
	public void reset(){
		finished = false;
		dismissed = false;
		charIndex = 0;
		currentLine = 0;
		linesUntilAdvance = 3;
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
	public boolean isDismissed(){ return dismissed; }
	public void dismiss(){ dismissed = true; }
	public boolean isWaitingForAdvance(){ return waitForAdvance; }
}
