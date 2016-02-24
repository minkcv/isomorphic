package gui;

import java.util.ArrayList;

public class Message {
	private String text;
	private int charIndex = 0;
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
		for(int i = 0; i + maxCharsPerLine < text.length(); i += maxCharsPerLine){
			brokenText.add(new String(text.substring(i, i + maxCharsPerLine)));
		}
	}

	public void advanceText(){
		if(charIndex < text.length()){
			charIndex++;
		}
		else{
			finished = true;
		}
	}

	public String[] getCurrentText(){
		String[] textLines = new String[3];
		for (int i = 0; i < textLines.length; i++) {
			//			textLines[i] = text.substring(i * maxCharsPerLine, );
		}
		return textLines;
	}
	public boolean isFinished(){ return finished; }
}
