import java.awt.*;
import java.awt.event.*;
import java.util.Stack;
import java.lang.Math; 
import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.util.Scanner;

public class GridLayoutManager extends JFrame {
	
	private Container contents;
	private JButton[][] squares = new JButton[global.n][global.n];
	private Color colorGray = Color.gray;
	
	public GridLayoutManager() {
		
		super("SAW GAME");
		
		 try {
			    UIManager.setLookAndFeel( UIManager.getCrossPlatformLookAndFeelClassName() );
			 } catch (Exception e) {
			            e.printStackTrace();
			 }
		
		contents = getContentPane();
		contents.setLayout(new GridLayout(global.n,global.n));
		ButtonHandler buttonHandler = new ButtonHandler();
		
		for(int i = 0; i < global.n; i++) {
			for(int j = 0; j < global.n; j++) {
				squares[i][j] = new JButton();
				if((i + j) % 2 != 0) {
					squares[i][j].setBackground(colorGray);
					squares[i][j].setOpaque(true);
				}
				contents.add(squares[i][j]);
				squares[i][j].addActionListener(buttonHandler);
			}
			
		}
		setSize(500,500);
		setResizable(true);
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	
	// 1 is win, 2 is lose, 3 is invalid move, 4 valid move
	
	private int determineMove(int i, int j) {
		String check = i+","+j;
		
		if(game.turn > 4) {
			if(game.startr == i && game.startc == j && ((Math.abs(i - game.currentBlock.getRow()) == 1 && game.currentBlock.getCol() == j) || (Math.abs(j - game.currentBlock.getCol()) == 1 && game.currentBlock.getRow() == i))) {
				return 1;
			}
		}
		if(game.turn > 4) {
			if(game.stack.contains(check) && ((Math.abs(i - game.currentBlock.getRow()) == 1 && game.currentBlock.getCol() == j) || (Math.abs(j - game.currentBlock.getCol()) == 1 && game.currentBlock.getRow() == i))) {
				String s = game.stack.get(game.stack.size()-2);
				String[] nums = s.split(",");
				int r;
				int c;
				r = Integer.parseInt(nums[0]);
				c = Integer.parseInt(nums[1]);
				if(r == i && c == j) {
					return 3;
				}else {
					return 2;
				}
			}
		}
		if(game.stack.contains(check)) {
			return 3;
		}
		if((i == game.currentBlock.getRow() && j == game.currentBlock.getCol()+1) || (i == game.currentBlock.getRow() && j == game.currentBlock.getCol()-1) || (i == game.currentBlock.getRow()+1 && j == game.currentBlock.getCol()) || (i == game.currentBlock.getRow()-1 && j == game.currentBlock.getCol()) ) {
			return 4;
		}
		return 3;
	}
	
	private void processClick(int i, int j) {
		
		if(game.turn > 1) {
			if(determineMove(i,j) == 1) {
				JOptionPane.showMessageDialog(null, "Congrats player "+(game.currentPlayer+1)+", you won!");
				System.exit(0);
			}
			if(determineMove(i,j) == 2) {
				JOptionPane.showMessageDialog(null, "Sorry player "+(game.currentPlayer+1)+", you lost!");
				System.exit(0);
			}
			if(determineMove(i,j) == 3) {
				return;
			}
			if(determineMove(i,j) == 4) {
				
			}
		}
		if(game.turn == 1) {
			game.currentBlock.setRow(i);
			game.currentBlock.setCol(j);
			squares[i][j].setBorder(new MatteBorder(8, 8, 8, 8, currentPlayers.obj[game.currentPlayer].color));
			squares[i][j].setText(Integer.toString(game.currentPlayer+1));
		    game.previousBlock.setRow(i);
			game.previousBlock.setCol(j);
			game.startr = i;
			game.startc = j;
		}
		else {
			squares[game.previousBlock.getRow()][game.previousBlock.getCol()].setBackground(currentPlayers.obj[game.previousPlayer].color);
			squares[game.previousBlock.getRow()][game.previousBlock.getCol()].setBorder(new MatteBorder(8, 8, 8, 8, currentPlayers.obj[game.previousPlayer].color));
			squares[i][j].setText(Integer.toString(game.currentPlayer+1));
			squares[i][j].setBorder(new MatteBorder(8, 8, 8, 8, currentPlayers.obj[game.currentPlayer].color));
			game.previousBlock.setRow(game.currentBlock.getRow());
			game.previousBlock.setCol(game.currentBlock.getCol()); 
			game.currentBlock.setRow(i);
			game.currentBlock.setCol(j);
			
		}
		if(game.currentPlayer == global.k-1) {
			game.currentPlayer = 0;
		}
		else {
			game.currentPlayer++;
		}
		
		if(game.previousPlayer == global.k-1) {
			game.previousPlayer = 0;
		}
		else {
			game.previousPlayer++;
		}
		game.turn++;
		System.out.println("turn "+game.turn);
		System.out.println("player "+(game.currentPlayer+1)+"\n");
		String coord;
		coord = i+","+j;
		game.stack.add(coord);
	}
	
	private class ButtonHandler implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			Object source = e.getSource();
			
			for(int i = 0; i < global.n; i++) {
				for(int j = 0; j < global.n; j++) {
					if(source == squares[i][j]) {
						processClick(i,j);
						return;
					}
				}
			}
		}
	}
	
	public static void main(String[] args){
		Scanner input = new Scanner(System.in);
		System.out.print("Please enter an n value: ");
		global.n = input.nextInt();
		System.out.print("Please enter an k value: ");
		global.k = input.nextInt();
		game game = new game();
		currentPlayers currentPlayers = new currentPlayers();
		System.out.println("turn 0");
		System.out.println("player 1"+"\n");
	    GridLayoutManager gui = new GridLayoutManager();
	  	gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}

class game{
	public game() {
	}
	public static Stack<String> stack = new Stack<String>(); 
	public static int currentPlayer = 0;
	public static int previousPlayer = -1;
	public static block previousBlock = new block();
	public static block currentBlock = new block();
	public static int turn = 1;
	public static int startr;
	public static int startc;
}

class player{
	public player(int x) {
		int upper = 255;
		int lower = 0;
		int result = (int) (Math.random() * (upper - lower)) + lower;
		int r = result;
		result = (int) (Math.random() * (upper - lower)) + lower;
		int g = result;
		result = (int) (Math.random() * (upper - lower)) + lower;
		int b = result;
		color = new Color(r, g, b);
		number = x;
		
	}
	public int number;
	public Color color;
}

class currentPlayers{
	public currentPlayers() {
		player player;

		for(int i = 0; i < global.k; i++) {
			player = new player(i+1);
			obj[i] = player;
		}
		
	}
	public static player obj[] = new player[global.k]; 
}

class block {
	public block(){
		
	}
	private static int row = 0;
	private static int column = 0;
	
	public int getRow() {
		return row;
	}
	public int getCol() {
		return column;
	}
	public void setRow(int r) {
		row = r;
	}
	public void setCol(int c) {
		column = c;
	}
}

class global{
	public static int n=0;
	public static int k=0;

}


