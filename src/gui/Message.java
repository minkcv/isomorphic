package gui;

import java.util.ArrayList;

public class Message {
	private String text;
	private int charIndex = 0;
	private int currentLine = 0;
	private boolean finished;
	private int maxCharsPerLine;
	private int numLines;
	private ArrayList<String> brokenText; // broken up based on where newlines should go
	public Message(String text, int maxCharsPerLine){
		this.text = text;
		this.maxCharsPerLine = maxCharsPerLine;
		this.maxCharsPerLine = maxCharsPerLine;
		brokenText = new ArrayList<String>();
		numLines = 0;
		int i; //TODO wrap text at spaces
		for(i = 0; i + maxCharsPerLine < text.length(); i += maxCharsPerLine){
			brokenText.add(new String(text.substring(i, i + maxCharsPerLine)));
		}
		if(text.length() % maxCharsPerLine != 0)
			brokenText.add(new String(text.substring(i, text.length())));
	}

	public void advanceText(){
		if(charIndex < text.length()){
			charIndex++;
			currentLine = charIndex / maxCharsPerLine;
		}
		else{
			finished = true;
		}
	}

	public String[] getCurrentText(){
		String[] textLines = {"", "", ""};
		if(currentLine == 0){
			textLines[0] = text.substring(0, charIndex);
		}
		else if(currentLine == 1){
			textLines[0] = brokenText.get(0);
			textLines[1] = brokenText.get(1).substring(0, charIndex % maxCharsPerLine);
		}
		else{
			for (int i = 0; i < textLines.length; i++) {
				textLines[0] = brokenText.get(currentLine - 2);
				textLines[1] = brokenText.get(currentLine - 1);
				textLines[2] = brokenText.get(currentLine).substring(0, charIndex % maxCharsPerLine);
			}
		}
		return textLines;
	}
	public boolean isFinished(){ return finished; }
}
