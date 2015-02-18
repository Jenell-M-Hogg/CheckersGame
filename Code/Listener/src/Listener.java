import java.io.IOException;


	import java.awt.Point;
	import java.awt.event.ActionEvent;
	import java.awt.event.ActionListener;
	import java.awt.event.MouseEvent;
	import java.awt.event.MouseListener;

	import javax.swing.Timer;




	public class Listener implements MouseListener, ActionListener {
		Game thisGame;
		AI ai;
		Timer timer=new Timer(0,this);
		

		
		
		
		
		
		public Listener(){
			thisGame=new Game();
			thisGame.checkerBoard.mainWindow.addMouseListener(this);
			ai=new AI(thisGame);
					
		}
		
		public static void main(String args[]){
			Listener n=new Listener();
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			
			Point click=e.getPoint();
			click=decipherPoint(click);
			thisGame.readClicked(click);
			
			
			
			//the above method changes the state of the game based on where the user clicked. 	
		}
		

		
		
		private Point decipherPoint(Point clicked){


			int y=(int) Math.ceil((clicked.getY()-25)/50);



			int x=(int) Math.ceil((clicked.getX())/50);



			//Indicates the point given lies within the bottom menu

			if (y>=9){


			double m=clicked.getX()/(400/3);

			int b=(int)Math.ceil(m);


			if (b==1){

			//indicates the point is on the far left panel of the menu

			return new Point(-1,-1);

			}

			if (b==2){

			//indicates the middle panel of the menu was pressed

			if (9<=(clicked.getY()-25)/50){

			return new Point(-2,-2);
			//two player mode

			}

			else return new Point(-1,-2);
			//one player mode
			}

			if(b==3){

			//indicates the right panel of the menu was pressed

			return new Point(-1,-3);

			}

			}

			//If the point is not in the bottom menu, will return what point on the board

			return new Point(x,y);


			}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if(!thisGame.player1Turn&&thisGame.singlePlayerMode){
			timer.setInitialDelay(1000);
			timer.start();
			ai.moveOnTurn();
			thisGame.player1Turn=true;
			thisGame.selectedChecker=null;
			thisGame.checkerBoard.switchTurns();
			
			
			
		}
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
			
			
		
	}
