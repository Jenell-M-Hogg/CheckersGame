import java.awt.Color;
import java.awt.Point;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;


public class Game {
	
	
	
	public Board checkerBoard;
	
	
	private ArrayList<Checker> checkers;
	private CheckerBoardSquare[] squares;
	ArrayList<CheckerBoardSquare> moves=new ArrayList<CheckerBoardSquare>();
	ArrayList<CheckerBoardSquare> captureSquares=new ArrayList<CheckerBoardSquare>();
	Checker selectedChecker=null;
	ArrayList<Checker> validCheckers=new ArrayList<Checker>();
	
	
	private boolean isCustomGame=false;
	boolean isGameStarted=false;
	private boolean toPlaceWhite=false;
	private boolean toPlaceRed=false;
	
	
	private File savedGame=new File("SavedGame.txt");


	boolean player1Turn=true;
	boolean singlePlayerMode;
	boolean isThereACaptureMove;
	boolean hasToFollowThrough;
	
	
	
	
	
	public Game(){
		
		this.checkerBoard=new Board();				
		this.squares=checkerBoard.squares;
		checkers=new ArrayList<Checker>();
		
				
	}
	

	
	

	public void readClicked(Point click) {
		
		
		//Finds the square and checker in the place that was clicked
		//Returns null if no checkerboardsquare where the user clicked
		CheckerBoardSquare square=findSquare(click);
		
		
		//Note: if there is no checker on the square that was clicked, this will return null
		Checker clickedChecker=findChecker(square);
		
		
		//printSquares(moves,"These are moves for the currently selected checker","");
		
		
		
		//Resets the message panel with each new click
		checkerBoard.setMessage(" ");
		
		
		if (click.equals(new Point(-1,-1))){
			//If the far left panel is clicked it could mean either continue game, place a red checker, or save game
			//this will check the booleans that act as switches to determine what should be done
			
			//Indicates the redChecker button was pressed during custom game setup
			if (isCustomGame){
				
				toPlaceRed=true;
				toPlaceWhite=false;
				checkerBoard.setMessage("Click a square to place a red checker");
				
			}
			
			//Indicates the save game button was pressed
			else if (isGameStarted){
				unselectAll();
				try {
					saveGame();
					checkerBoard.setMessage("The game was saved!");
				} catch (ClassNotFoundException | IOException e1) {
					checkerBoard.setMessage("Impossible! The game wasn't saved!");
				}
			}
			
			//Indicates the continue button was pressed
			else{
				try {
					continueGame();
				} catch (Exception e1) {
					checkerBoard.setMessage("There is no currently saved game");
				}
			}
			
			
		}
		
		else if (click.equals(new Point(-1,-2))|click.equals(new Point(-2,-2))){
			//This indicates the middle panel of the menu was clicked. It could mean either start game or nothing
			
			if (isGameStarted==false){
				if(click.equals(new Point(-2,-2))){
					singlePlayerMode=false;
				}
				if (click.equals(new Point(-1,-2))){
					singlePlayerMode=true;
				}
				startGame();
				
			}
			
		}
		
		else if (click.equals(new Point(-1,-3))){
			//This indicates the right panel of the menu was clicked. It could mean either customGame, whiteChecker, or reset was pressed
			
			//If custom game has already been pressed, this means the whiteCheckerButton was pressed during custom game setup
			
			//If the game is started, the reset game button must have been pressed
			if (isGameStarted){
				reset();
			}
			
			//If custom game has already been pressed, this means the whiteCheckerButton was pressed during custom game setup
			else if (isCustomGame){
				toPlaceRed=false;
				toPlaceWhite=true;
				checkerBoard.setMessage("Click a square to place a white checker");
			}
			
			//Otherwise the customGame button was pressed
			else{
				customGameSetup();
			}
		}
		
		
		//Listens for when a square is clicked after the redChecker Button was pressed
		else if (toPlaceRed|toPlaceWhite){
			if(square!=null){
				if(toPlaceRed){
					if(colourCount().getY()<12){
						putChecker(square,true);
					}
					else{
						checkerBoard.setMessage("Maximum amount of red checkers have been placed.");
					}
				}
				
				else{
					if (colourCount().getX()<12){
						putChecker(square, false);
					}
					else{
						checkerBoard.setMessage("Maximum amount of white checkers have been placed.");
					}
					
				}
			}
			else{
				checkerBoard.setMessage("Can't put a checker there!");
			}
			
		}
		
		
		
		
		else if (isGameStarted){
			
			
			
			//Checks to see if the game should be over
			
		
			
			
			
			
			if(selectedChecker!=null&&!square.hasChecker){
				
				//Listens for the click after a checker is selected
				//there are 4 cases- the square is a valid move, the square is invalid, the square is the same one that was clicked
				
				if(moves.contains(square)){
					moveChecker(square);
					checkIfKinged(square, selectedChecker);
					
					if (captureSquares.size()!=0){
						//If the last move was a capturing move, don't switch turns yet
						unselectSquares();
						selectedChecker.select();
						selectedChecker.restoreImageInPlace();
						
						validMoves(selectedChecker);
						highlightPossibleMoves();
					
						}
					if (captureSquares.size()==0){
						//if there isn't a capturing move after the checker moves, switch turns
						unselectAll();
						
						player1Turn=!player1Turn;
						checkerBoard.switchTurns();
						
					
						}
										
					}
				Point count=colourCount();
				int whites=(int) count.getX();
				int reds=(int) count.getY();
				if(whites==0){
					checkerBoard.setMessage("Red Wins!!");
					checkerBoard.turnLabel.setText("White is Defeated!");
					checkerBoard.turn.repaint();
				}
				else if(reds==0){
					checkerBoard.setMessage("White Wins!");
					checkerBoard.turnLabel.setText("Red is Defeated!");
					checkerBoard.turn.repaint();
				}
			}
			else if(square!=null){
				if(square.hasChecker){
					unselectAll();
					
					
						
					
					findValidCheckers();
					
					if(clickedChecker!=null){
						selectedChecker=clickedChecker;
						selectedChecker.select();
						System.out.println(selectedChecker.place.location);
							
						if(validCheckers.contains(selectedChecker)){
								validMoves(selectedChecker);
								highlightPossibleMoves();
						
						}
					
						else if(!validCheckers.contains(selectedChecker)){
							if (player1Turn&&selectedChecker.isRed||!player1Turn&&!selectedChecker.isRed){
								checkerBoard.setMessage("Not your turn!");
								
							}
							else{
								checkerBoard.setMessage("No valid moves for that checker!");
							}
							
							unselectAll();
						}
			
						
						else{
							System.out.println("The checker is null and wasn't selected");
						}
					}
					else{
						checkerBoard.setMessage("Take a jump!");
					}
					
			}
			}
					
					
		}
	}
			
					

				
			
			

				
	
	



			private void saveGame() throws IOException, ClassNotFoundException {
				
				//the instances of the checker class are not able to become a part of an object array and then saved into a file this way,therefore
				//the info of the checkers on the board must be saved and new instances of the checker class will be constructed to restore them
				
				//For storing the location of the checkers on the board
				ArrayList<Point> checkerPoints=new ArrayList<Point>();
				
				//For storing the booleans of each checker on the board corresponding to whether they are red or not
				ArrayList<Boolean> checkerIsRed=new ArrayList<Boolean>();
				
				//For storing the booleans of each checker on the board corresponding to whether they are kings or not
				ArrayList<Boolean> checkerIsKing= new ArrayList<Boolean>();
				
				//Store all the information of the checkers into the arrays created above
				for(int i=0;i<checkers.size();i++){
					checkerPoints.add(checkers.get(i).place.location);
					checkerIsRed.add(checkers.get(i).isRed);
					checkerIsKing.add(checkers.get(i).isKing);
				}
				
				//Create an array of objects that contain all the state information necessary to restore a game
				Object[] objects=new Object[8];
				
				objects[0]=squares;
				objects[1]=checkerPoints;
				objects[2]=checkerIsRed;
				objects[3]=checkerIsKing;
				objects[4]=moves;
				
				
				objects[5]=singlePlayerMode;
				objects[6]=player1Turn;
				objects[7]=isThereACaptureMove;
			
				
				//Write these objects to a file. 
				FileOutputStream writeTo=new FileOutputStream(savedGame);
				ObjectOutputStream objectWriteTo= new ObjectOutputStream(writeTo);
				objectWriteTo.writeObject(objects);
				objectWriteTo.close();
				
				
	}
			void continueGame() throws IOException, ClassNotFoundException{
				//Reads the Object array created in the saveGame() function from a file
				FileInputStream readFrom=new FileInputStream(savedGame);
				ObjectInputStream objectReadFrom=new ObjectInputStream(readFrom);
				
				//Restores the object array containing all the necessary objects needed to restore a game
				Object[] readFromFile=(Object[]) objectReadFrom.readObject();
				objectReadFrom.close();
				
				//Updates the squares arraylist to match the one that was saved
				squares=(CheckerBoardSquare[]) readFromFile[0];
				
				//Identifies all the information we need to recreate the checkers
				ArrayList<Point> checkerPoints=(ArrayList<Point>) readFromFile[1];
				ArrayList<Boolean> checkerisRed=(ArrayList<Boolean>) readFromFile[2]; 
				ArrayList<Boolean> checkerIsKing=(ArrayList<Boolean>) readFromFile[3];
				
				//Identifies the rest
			
				player1Turn=(boolean) readFromFile[5];
				singlePlayerMode=(boolean) readFromFile[6];
				isThereACaptureMove=(boolean) readFromFile[7];
				
				//Construct the checkers from the above info
				for(int i=0; i<checkerPoints.size();i++){
					
					//Creates new Checkers from the data stored in the file to match all the saved checkers
					Checker savedCheck=new Checker(findSquare(checkerPoints.get(i)),checkerisRed.get(i));
					
					//If the checker was a King in the saved game, make it a King in this one.
					savedCheck.isKing=checkerIsKing.get(i);
					
					//Update the arraylist of checkers to have all the restore checkers
					checkers.add(savedCheck);
				}
				
				//Updates the arraylist of possible moves that was stored in the file
				moves=(ArrayList<CheckerBoardSquare>) readFromFile[4];
				
				
				//The saved game was already started, so restore that
				isGameStarted=true;
				//Make sure there is no selected checker when the game is started
				selectedChecker=null;
				
				//Finally, set up the board to display all squares and checkers
				checkerBoard.setUp(squares);
				
			}

			void moveChecker(CheckerBoardSquare moveToSquare) {
			//Given a square, judges to see if it is possible to move there and if so, moves the currently selected checker	
			//Will remove a checker that was captured during the move
			
			
			//If there is a possible move, move the checker there
			if (moves.size()!=0){
				selectedChecker.moveTo(moveToSquare);
				
				//If the move was a capturing move, this will remove a checker off the screen and from the array of checkers
				if(captureSquares.size()!=0){
					int index=-1;
					for(int i=0;i<moves.size();i++){
						if(moves.get(i).equals(moveToSquare)){
							//Finds the index of the captured square (this will always match the index of where the moveToSquare is within the array moves)
							index=i;
						}
					}
					//Remove the checker that was captured, if it was a capturing move
					if(index!=-1){
						CheckerBoardSquare withCapturedChecker = captureSquares.get(index);
						removeChecker(withCapturedChecker);
					}
					
					
					
				}
				
			}
		
	}
		

			private void checkIfKinged(CheckerBoardSquare square, Checker check) {
				//It is possible that the checker moved to a place where it must be kinged
				if (square.location.getY()==1|square.location.getY()==8){
					
					//If the checker is red and is in the bottom row
					if(check.isRed&&square.location.getY()==8){
						check.becomeKing();
					}
					//If the checker is white and is in the top row
					else if(check.isRed==false&&square.location.getY()==1){
						check.becomeKing();
					}
				}
		
	}



	private void customGameSetup() {	
		//Sets up the board for a custom game set up
		isCustomGame=true;
		//Sets up the menu so that the proper buttons are displayed
		checkerBoard.customGameMenu();
		
	}

	private void highlightPossibleMoves() {
		if(moves.size()!=0){
		for(int i=0;i<moves.size();i++){
			moves.get(i).highlightSquare();
			}
			
		}
	}

	void unselectAll() {
		// updates the images on the board so that the squares and checkers 
		//no checker is selected
		
		//gets rid of the glow around the squares if there are any
		unselectSquares();
		
		//gets rid of the glow around the checker that was selected
		if(selectedChecker!=null){
			selectedChecker.restoreImageInPlace();
			selectedChecker=null;
		}
		
		
	}
	
	private void unselectSquares(){
		for (int i=0;i<moves.size();i++){
			moves.get(i).unhighlightSquare();
		}
		
	}

	void validMoves(Checker check) {
		// Given a checker, finds the checkers valid Moves and returns them as an ArrayList of checkerBoardSquares.If there are no moves, this method returns an arraylist of size 0.
		//This move recognizes capturing moves and changes isThereACaptureMove accordingly
		
		
		
		
		//first finds the standard moves for the given checker
			ArrayList<Point> possibleMoves=check.standardMoves();
		
		//Resets the arrays
		moves=new ArrayList<CheckerBoardSquare>();
		captureSquares=new ArrayList<CheckerBoardSquare>();
		
		//Checks for capture moves first
		for (int i=0;i<possibleMoves.size();i++){
			
			Point possiblePoint=possibleMoves.get(i);
			//Finds a CheckerBoardSquare that may or may not have a checker on it
			CheckerBoardSquare possibleMoveTo=findSquare(possiblePoint);
			
			if (possibleMoveTo!=null){
				//If the square has a checker, see if it can be captured.
			
				if (possibleMoveTo.hasChecker){
					
					Checker check1=findChecker(possibleMoveTo);
					//printChecker(check1,"Is check1 null?","");
					//Checks to make sure the checkers are of opposite colours
					if(check1!=null){
						if((!check.isRed)==check1.isRed){
							//This checks that the checker can be captured
							CheckerBoardSquare moveTo=canBeCaptured(possibleMoveTo, check.place);
							
							if (moveTo!=null){
								//This ensures that the capture squares match the index of the moves
								isThereACaptureMove=true;
								captureSquares.add(possibleMoveTo);
								moves.add(moveTo);
						
							}
						}
					}
					
					
				}
				
				//checks to see if the checker on the possibleSquare can be captured
				//If this is true, valid moves must be limited only to capture moves
			}
		}
		//Add the non capturing moves to the valid moves list if there are no capturing moves
		if(moves.size()==0){
			for(int i=0;i<possibleMoves.size();i++){
				
				CheckerBoardSquare moveTo=findSquare(possibleMoves.get(i));
				if(moveTo!=null){
					isThereACaptureMove=false;
					if(!moveTo.hasChecker){
						moves.add(moveTo);
						
					}
				}
			}
		}
		
		
				
				
			}
		

		
	
	

	private CheckerBoardSquare canBeCaptured(CheckerBoardSquare possibleSquare, CheckerBoardSquare currentSquare) {
		// Given a square with a checker on it and a selected checker, sees if that checker can capture
		Point location=currentSquare.location;
		int column = (int) location.getX();
		int row = (int) location.getY();
		
		Point possibleCapture=possibleSquare.location;
		int captureColumn = (int) possibleCapture.getX();
		int captureRow = (int) possibleCapture.getY();
		
		Point moveToAfterCapture=new Point();
		
		//Moving up and to the left
		if (column>captureColumn&&row>captureRow){
			moveToAfterCapture=new Point(captureColumn-1,captureRow-1);
			}
		
		//Moving down and to the left
		else if(column<captureColumn&&row>captureRow){
			moveToAfterCapture=new Point(captureColumn+1,captureRow-1);
		}
		//Moving up and to the right
		else if(column>captureColumn&&row<captureRow){
			moveToAfterCapture=new Point(captureColumn-1,captureRow+1);
		}
		//Moving down and to the right
		else if(column<captureColumn&&row<captureRow){
			moveToAfterCapture=new Point(captureColumn+1,captureRow+1);
		}
		
		CheckerBoardSquare moveToSquare = findSquare(moveToAfterCapture);
		
		if(moveToSquare!=null&&moveToSquare.hasChecker==false){
			
			return moveToSquare;
		}
		else{
			
			return null;
		}
		
		
	}

	Checker findChecker(CheckerBoardSquare square) {
		//Finds a checker on a square when given that square, otherwise returns null
		
		//The below condition handles the case that the square clicked is on the menu.
		if(square!=null){
			if(square.location!=null){
			for (int i=0; i<checkers.size();i++){
				
				if(checkers.get(i)==null){
					System.out.println("It's null");
				}
				else{
					if(checkers.get(i)!=null){
						if (square.location.equals(checkers.get(i).place.location))
						return checkers.get(i);
					}
				}
				
			}
			}
		
		}
		return null;
	}

	
	
	
	


	

	
	
	private void startGame(){
		//Resets these switches so the next click doesn't place a checker
		toPlaceRed=false;
		toPlaceWhite=false;
		
		
		//Checks to see if there are no checkers on the board when the button is pressed during custom game setup
		if(isCustomGame&&checkers.size()==0){
				checkerBoard.setMessage("Please place some checkers before starting the game");
			}
		
		//Checks to see if there are no checkers of 1 colour placed upon the board, if so prints a user prompt
		else if(isCustomGame&&(colourCount().getX()==0|colourCount().getY()==0)){
			checkerBoard.setMessage("Atleast one white and one red checker must be on the board");
			
		}
		
		
		//Starts the game. 
		else{
	
			isGameStarted=true;
			
			checkerBoard.startGameMenu();
			
			if(isCustomGame==false){
				//If it's a standard game, setup in standard layout. 
				standardGameSetup();
			
			}
			else {
				isCustomGame=false;
			}
		}
		
			
	}
		
		
	
	void putChecker(CheckerBoardSquare square, boolean isRed){
		String checkerColour="WhiteC.png";
		
		if(isRed){
			checkerColour="RedC.png";
		}
		
		if (square.hasChecker){
			//checks to see if there is already a checker at that square, if so, removes it from the array of checkers and visually from the board
			removeChecker(square);
		}
		square.placeCheckerOn(checkerColour);
		
		//Makes a new instance of checker, and adds it to the array for later access
		Checker check= new Checker(square, isRed);
		checkIfKinged(square, check);
		
		
		checkers.add(check);
		
	}
	
	void removeChecker(CheckerBoardSquare square){
		//Removes a checker at a point from the board and from the array of checkers
		square.takeCheckerOff();
		
		//Removes the checker from the array of checkers
		CheckerBoardSquare checkerLocation=square;
		
		
		for (int i=0;i<checkers.size();i++){
			if(checkers.get(i).place.equals(checkerLocation)){
				checkers.remove(i);
				
			}
		}
		
		
	}
	
	Point colourCount(){
		//Counts the number of red and white checkers on the board
		//returns a point in the form (#ofwhites, #ofreds)
		int numberOfWhites=0;
		int numberOfReds=0;
		for (int i=0; i<checkers.size();i++){
			if (checkers.get(i).isRed){
				numberOfReds++;
			}
			else{
				numberOfWhites++;
			}
		}
		return new Point(numberOfWhites,numberOfReds);
	}
	
	
	CheckerBoardSquare findSquare(Point x){
		//helper method that, given a point, find a square with the same point in it
		for (int y=0;y<64;y++){
			if (squares[y].location.equals(x)){
				return squares[y];
			}
		}
		return null;
	}
	
	
	private void standardGameSetup(){
		//place white checkers on the top of the board
		for (int i=0;i<24;i++){
			if(squares[i].getBackground().equals(Color.black)){
				squares[i].placeCheckerOn("RedC.png");
				Checker check= new Checker(squares[i],true);
				checkers.add(check);
			}
		}
		//place red checkers at the bottom of the board
		for (int i=40;i<64;i++){
			if (squares[i].getBackground().equals(Color.black)){
				squares[i].placeCheckerOn("WhiteC.png");
				Checker check= new Checker(squares[i], false);
				checkers.add(check);
				
			}
		}
	}
	
	private void reset(){
		unselectAll();
		//Clears the checkers off the board
		for(int i=0;i<squares.length;i++){
			
			CheckerBoardSquare square=squares[i];
			if (square.hasChecker){
				square.takeCheckerOff();
			}
			//Empties the array list
			checkers=new ArrayList<Checker>();
			
			//Resets all the switches
			isCustomGame=false;
			toPlaceRed=false;
			toPlaceWhite=false;
			isGameStarted=false;
			player1Turn=true;
			selectedChecker=null;
			
			//resets the menu 
			checkerBoard.resetMenu();
		}

	}
	
	
	public void findValidCheckers(){
		ArrayList<Checker> captureCheckers = new ArrayList<Checker>();
		ArrayList<Checker> validCheckers= new ArrayList<Checker>();
		for (int i=0;i<checkers.size();i++){
			
			Checker check= checkers.get(i);
			
			
			if ((player1Turn&&!check.isRed)|(!player1Turn&&check.isRed)){
				
				validMoves(check);
				
				if (moves.size()!=0){
					validCheckers.add(check);
					
				}
				
				if (captureSquares.size()!=0){
					captureCheckers.add(check);
					
				}
			}
			
			
		}
		if (captureCheckers.size()!=0){
			this.validCheckers=captureCheckers;
			isThereACaptureMove=true;
		}
		else{
			this.validCheckers=validCheckers;
			isThereACaptureMove=false;
			
		}
		moves=new ArrayList<CheckerBoardSquare>();
		captureSquares=new ArrayList<CheckerBoardSquare>();
	}
}


	