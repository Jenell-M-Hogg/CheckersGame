import java.awt.Point;
import java.util.ArrayList;


public class Checker {

	public boolean isRed;
	public CheckerBoardSquare place;
	public boolean isKing=false;
	
	public Checker(CheckerBoardSquare m, boolean isRed){
		place=m;
		this.isRed=isRed;
		
	}
	
	public ArrayList<Point> standardMoves(){
		//finds all the standard possible moves for a checker
		//Gets rid of standard moves that are out of bounds of the checker
		
		ArrayList<Point> standardMoves=new ArrayList<Point>();
		int column=(int) place.location.getX();
		int row=(int) place.location.getY();
		
		int nextColumn=column+1;
		int nextColumn2=column-1;
		int nextRow=row+1;
		int nextRow2=row-1;
		
		Point possibleMove = null;
		Point possibleMove2 = null;
		
		

		if(isRed){
			possibleMove=new Point(nextColumn,nextRow);
			possibleMove2=new Point(nextColumn2,nextRow);
		
		}
		
		else if(!isRed){
			possibleMove=new Point(nextColumn,nextRow2);
			possibleMove2=new Point(nextColumn2,nextRow2);
			
		}
		standardMoves.add(possibleMove);
		standardMoves.add(possibleMove2);
		
		if(isKing){
			Point possibleMove3;
			Point possibleMove4;
			if(isRed){
				possibleMove3=new Point(nextColumn2,nextRow2);
				possibleMove4=new Point(nextColumn,nextRow2);
			}
			else{
				possibleMove3=new Point(nextColumn2,nextRow);
				possibleMove4=new Point(nextColumn,nextRow);
			}
			standardMoves.add(possibleMove3);
			standardMoves.add(possibleMove4);
		}
		
		//gets rid of moves that are out of bounds of the checker board
		for(int i=0;i<standardMoves.size();i++){
			int column1=(int) standardMoves.get(i).getX();
			int row1=(int) standardMoves.get(i).getY();
			if(column1>8||column1<1||row1>8||row1<1){
				standardMoves.remove(i);
				i=i-1;
			}
			
		}
		
		return standardMoves;

	}
	
	public void becomeKing(){
		isKing=true;
		if(isRed){
			place.placeCheckerOn("RedKingC.png");
		}
		else{
			place.placeCheckerOn("WhiteKingC.png");
		}
	}

	
	
	public void select() {
		String image;
		if(isKing){
			if(isRed){
				image="RedSelectedKingC.png";
			}
			else{
				image="WhiteSelectedKingC.png";
			}
			
		}
		else{
			if(isRed){
				image="RedSelectedC.png";
			}
			else{
				image="WhiteSelectedC.png";
			}
			
		}
		place.placeCheckerOn(image);
		
	}
	
	public void restoreImageInPlace(){
		//Places an image of a checker in its place
		//Used after a checker has been unselected or moved
		String image;
		if(isKing){
			if(isRed){
				image="RedKingC.png";
			}
			else{
				image="WhiteKingC.png";
			}
		}
		else if(isRed){
			image="RedC.png";
		}
		else{
			image="WhiteC.png";
		}
		
		place.placeCheckerOn(image);
	}

	public void moveTo(CheckerBoardSquare square) {
		//Moves the checker to a new square
		place.takeCheckerOff();
		place=square;
		restoreImageInPlace();
		
	}
	
	
	
}
