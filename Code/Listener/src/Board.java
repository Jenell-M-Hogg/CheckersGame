import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.TextField;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class Board {
	public CheckerBoardSquare[] squares=new CheckerBoardSquare[64];
	public JFrame mainWindow;
	
	//TODO blank was added revision history
	private JPanel menu,start;
	JPanel turn;
	JLabel turnLabel;
	private JPanel reset, start1, start2, custom, whiteButton, redButton, saveGame, continueGame, board;
	
	private TextField messagePanel;

	//Trace for Requirement 1.1
	public Board(){
		
		//Set up the visual part of the board
		
		//ensures the colors alternate
		Color squareColor=Color.black;
		
		//to be used in the for loop to set up the array of squares
		int index=0;
		
		//Something to add the squares to
		board=new JPanel();
		board.setLayout(new GridLayout(8,8));
		board.setPreferredSize(new Dimension(400,400));
		
		for(int y=1;y<=8;y++){
			
			//Makes sure the color of squares on the rows of the board alternate
			//This ensures that the bottom right square on the board is the right colour as outlined in Requirement 1.1
			if(squareColor.equals(Color.black)){
				squareColor=Color.white;
			}
			else{
				squareColor=Color.black;
			}
			
			for(int x=1;x<=8;x++){
				
				//Ensures the colors of the columns of the board alternate. 
				CheckerBoardSquare newSquare=new CheckerBoardSquare(new Point(x,y));
				newSquare.setSize(50,50);
				
				if (squareColor.equals(Color.black)){
					newSquare.setBackground(squareColor);
					squareColor=Color.white;

					
				}
				else{
					newSquare.setBackground(squareColor);
					newSquare.location.setLocation(-2,-2);;
					squareColor=Color.black;
				}
				
				//Adds the squares to the array for later access
				
				squares[index]=newSquare;
				board.add(newSquare);
				board.revalidate();
				index++;
			}
			
		}
		//Set up the menu
		
		//Make the reset button
		reset=new JPanel();
		reset.setBackground(Color.gray);
		JLabel resetLabel=new JLabel("Reset");
		reset.add(resetLabel);
		
		//Make the start 1player game button
		start1=new JPanel();
		start1.setBackground(Color.green);
		JLabel startLabel1=new JLabel("Start 1 Player Game");
		start1.add(startLabel1);
		
		//Make the start 2player game button
		start2=new JPanel();
		start2.setBackground(Color.blue);
		JLabel startLabel2=new JLabel("Start 2 Player Game");
		start2.add(startLabel2);
		
		//Make the continue game button
		continueGame=new JPanel();
		continueGame.setBackground(Color.orange);
		JLabel continueLabel=new JLabel("Continue Game");
		continueGame.add(continueLabel);
		
		//Make custom game button
		custom=new JPanel();
		custom.setBackground(Color.red);
		JLabel customLabel=new JLabel("Custom Game");
		custom.add(customLabel);
		
		//Make the whiteChecker button, for use during custom game setup when user wants to place a whiteChecker
		whiteButton=new JPanel();
		whiteButton.setBackground(Color.white);
		JLabel whiteLabel=new JLabel("Place a White Checker");
		whiteButton.add(whiteLabel);
		
		//Make the redChecker Button, for use during custom game setup when user wants to place a redChecker
		redButton=new JPanel();
		redButton.setBackground(Color.red);
		JLabel redLabel=new JLabel("Place a Red Checker");
		redButton.add(redLabel);
		
		//Make the Save Game Button
		saveGame=new JPanel();
		saveGame.setBackground(Color.gray);
		JLabel saveLabel=new JLabel("Save Game");
		saveGame.add(saveLabel);
		
		//Make the turn button for use later
		turn=new JPanel();
		turn.setBackground(Color.white);
		turnLabel=new JLabel("White's Turn");
		turn.add(turnLabel);
		
		
		
		
		
		start = new JPanel();
		start.setPreferredSize(new Dimension(133,50));
		start.setLayout(new GridLayout(2,1));
		start.add(start1);
		start.add(start2);
		
		menu=new JPanel();
		menu.setPreferredSize(new Dimension(400,50));
		menu.setLayout(new GridLayout(1,3));
		menu.add(continueGame);
		menu.add(start);

		menu.add(custom);
		
		//Create the message panel, that will display error messages to the user
		messagePanel=new TextField();
		
		//Makes sure the user cannot type in the panel
		messagePanel.setSize(new Dimension(400,100));
		
		//Make the frame
		mainWindow=new JFrame();
		mainWindow.setSize(new Dimension(400,500));
		mainWindow.setResizable(false);
		
		//Add the checkerboard panes, message panel, menu to the main window
		mainWindow.setLayout(new BoxLayout(mainWindow.getContentPane(),3));
		mainWindow.add(board);   
		mainWindow.add(messagePanel);
		mainWindow.add(menu);
		mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainWindow.revalidate();
		mainWindow.repaint();
		mainWindow.setVisible(true);
	}



	public void setMessage(String message){
		//helper method for printing user prompts and error messages
		messagePanel.setText(message);
	}
	
	public void switchTurns(){
		if (turn.getBackground().equals(Color.white)){
			turn.setBackground(Color.red);
			turnLabel.setText("Red's Turn");
		}
		else{
			turn.setBackground(Color.white);
			turnLabel.setText("White's Turn");
		}
		turn.repaint();
	}
	
	public void startGameMenu(){
		//visually changes the menu for when a game is started
		
		menu.removeAll();
		
		//update the menu
		
		
		menu.add(saveGame);
		menu.add(turn);
		menu.add(reset);
		
		menu.revalidate();
		menu.repaint();
		
	}
	
	public void customGameMenu(){
		menu.removeAll();
		
		
		
		menu.add(redButton);
		menu.add(start);
		menu.add(whiteButton);
		
		menu.revalidate();
		menu.repaint();
		
	
	

	}
	
	public void resetMenu(){
		//Resets the menu display
		turn.setBackground(Color.white);
		turnLabel.setText("White's turn");
		menu.removeAll();
		menu.add(continueGame);
		menu.add(start);
		menu.add(custom);
		menu.revalidate();
		menu.repaint();
	}
	
	public void setUp(CheckerBoardSquare[] squares){
		startGameMenu();
		
		board.removeAll();
		for (int i=0; i<squares.length;i++){
			board.add(squares[i]);
		}
		board.revalidate();
		board.repaint();
	}
	
	
	
	
	

	

}