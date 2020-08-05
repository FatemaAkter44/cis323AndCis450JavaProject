package brickBrcker;

import javax.swing.JFrame;

public class Main {

	public static void main(String[] args) {
	JFrame obj = new JFrame();
	Gameplay gamePlay = new Gameplay(); //create a object of new class GmaePlay
	obj.setBounds(10, 10, 700, 600); //frame size
	obj.setTitle("Breakout Ball");  //creating a frame titel
	obj.setResizable(false); //not resizable
	obj.setVisible(true);    // make it visible
	obj.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	
	obj.add(gamePlay);
	}

}