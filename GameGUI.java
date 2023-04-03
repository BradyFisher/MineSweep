import java.awt.Point;
import java.awt.Graphics;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Color;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import java.net.URL;
import javax.swing.JOptionPane;

/**
 * This class provides a GUI for solitaire games related to Elevens.
 */
public class GameGUI extends JFrame implements ActionListener {

	/** Height of the game frame. */
	private static final int DEFAULT_HEIGHT = 1500;
	/** Width of the game frame. */
	private static final int DEFAULT_WIDTH = 1700;
	/** Width of a tile */
	private static final int TILE_WIDTH = 50;
	/** Height of a tile. */
	private static final int TILE_HEIGHT = 50;
	/** Row (y coord) of the upper left corner of the first tile. */
	private static final int LAYOUT_TOP = 30;
	/** Column (x coord) of the upper left corner of the first tile. */
	private static final int LAYOUT_LEFT = 30;
	/** Distance between the upper left x coords of
	 *  two horizonally adjacent tiles. */
	private static final int LAYOUT_WIDTH_INC = 60;
	/** Distance between the upper left y coords of
	 *  two vertically adjacent tiles. */
	private static final int LAYOUT_HEIGHT_INC = 60;
	/** y coord of the "auto solver" button. */
	private static final int BUTTON_TOP = 30;
	/** x coord of the "auto solver" button. */
	private static final int BUTTON_LEFT = 1200;
	/** Distance between the tops of the "auto solver" and "Flag" buttons. */
	private static final int BUTTON_HEIGHT_INC = 60;
	/** y coord of the "n unmarked mines remain" label. */
	private static final int LABEL_TOP = 1200;
	/** x coord of the "n unmarked mines remain" label. */
	private static final int LABEL_LEFT = 540;
	/** Distance between the tops of the "n unmarked mines" and
	 *  the "You lose/win" labels. */
	private static final int LABEL_HEIGHT_INC = 35;

	/** The board (Board subclass). */
	private MBoard board;

	/** The main panel containing the game components. */
	private JPanel panel;
	/** The Auto-Solver button. */
	private JButton autosolverButton;

	/** The Auto-Solver button. */
	private JButton restartButton;

	/** The Flag button. */
	private JButton flagButton;
	/** The "number of unmarked mines remain" message. */
	private JLabel statusMsg;
	/** The "you've won n out of m games" message. */
	private JLabel totalsMsg;
	/** The tile displays. */
	private JLabel[] displayTiles;
	/** The win message. */
	private JLabel winMsg;
	/** The loss message. */
	private JLabel lossMsg;
	/** The flag message. */
	private JLabel flagMsg;
	/** The guess message. */
	private JLabel guessMsg;

	/** The coordinates of the tile displays. */
	private Point[] tileCoords;

	/** selection is true if selecting tile, false if placing flag */
	private boolean selection;
	/** The number of games won. */
	private int totalWins;
	/** The number of games played. */
	private int totalGames;


	/**
	 * Initialize the GUI.
	 */
	public GameGUI() {

		board = reset(true);

		totalWins = 0;
		totalGames = 0;

		// Initialize tileCoords
		tileCoords = new Point[board.size()];
		int x = LAYOUT_LEFT;
		int y = LAYOUT_TOP;
		for (int i = 0; i < tileCoords.length; i++) {
			tileCoords[i] = new Point(x, y);
			if (i % board.get_num_rows() == (board.get_num_columns() - 1)) {
				x = LAYOUT_LEFT;
				y += LAYOUT_HEIGHT_INC;
			} else {
				x += LAYOUT_WIDTH_INC;
			}
		}

		selection = true;
		initDisplay();
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		repaint();
	}

	/**
	 * Run the game.
	 */
	public void displayGame() {
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				setVisible(true);
			}
		});
	}

	/**
	 * Draw the display (tiles and messages).
	 */
	public void repaint() {
		for (int k = 0; k < board.size(); k++) {

			String tileImageFileName =
				imageFileName(board.MTileAt(k/board.get_num_rows(),k % board.get_num_columns()));
			URL imageURL = getClass().getResource(tileImageFileName);
			if (imageURL != null) {
				ImageIcon icon = new ImageIcon(imageURL);
				displayTiles[k].setIcon(icon);
				displayTiles[k].setVisible(true);
			} else {
				throw new RuntimeException(
					"tile image not found: \"" + tileImageFileName + "\"");
			}
		}
		statusMsg.setText(board.get_num_minesLeft()
			+ " unmarked mines remain.");
		statusMsg.setVisible(true);
		totalsMsg.setText("You've won " + totalWins
			 + " out of " + totalGames + " games.");
		totalsMsg.setVisible(true);
		pack();
		panel.repaint();
	}

	/**
	 * Initialize the display.
	 */
	private void initDisplay()	{
		panel = new JPanel() {
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
			}
		};

		// If board object's class name follows the standard format
		// of ...Board or ...board, use the prefix for the JFrame title
		String className = board.getClass().getSimpleName();
		int classNameLen = className.length();
		int boardLen = "Board".length();
		String boardStr = className.substring(classNameLen - boardLen);
		if (boardStr.equals("Board") || boardStr.equals("board")) {
			int titleLength = classNameLen - boardLen;
			setTitle(className.substring(0, titleLength));
		}

		// Calculate number of rows of tiles
		// and adjust JFrame height if necessary
		this.setSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT));
		panel.setLayout(null);
		panel.setPreferredSize(
			new Dimension(DEFAULT_WIDTH - 20, DEFAULT_HEIGHT - 20));
		displayTiles = new JLabel[board.size()];
		for (int k = 0; k < board.size(); k++) {
			displayTiles[k] = new JLabel();
			panel.add(displayTiles[k]);
			displayTiles[k].setBounds(tileCoords[k].x, tileCoords[k].y,
										TILE_WIDTH, TILE_HEIGHT);
			displayTiles[k].addMouseListener(new MyMouseListener());
		}
		autosolverButton = new JButton();
		autosolverButton.setText("AUTO-SOLVE");
		panel.add(autosolverButton);
		autosolverButton.setBounds(BUTTON_LEFT, BUTTON_TOP + (4*BUTTON_HEIGHT_INC), 200, 110);
		autosolverButton.addActionListener(this);

		flagButton = new JButton();
		flagButton.setText("CHANGE FLAG OR GUESS");
		panel.add(flagButton);
		flagButton.setBounds(BUTTON_LEFT, BUTTON_TOP + (2*BUTTON_HEIGHT_INC)  ,200, 110);
		flagButton.addActionListener(this);

		restartButton = new JButton();
		restartButton.setText("RESTART");
		panel.add(restartButton);
		restartButton.setBounds(BUTTON_LEFT, BUTTON_TOP + (6*BUTTON_HEIGHT_INC)  ,200, 110);
		restartButton.addActionListener(this);

		statusMsg = new JLabel(
			board.get_num_minesLeft() + " unmarked mines remain.");
		panel.add(statusMsg);
		statusMsg.setBounds(LABEL_LEFT, LABEL_TOP, 250, 30);

		winMsg = new JLabel();
		winMsg.setBounds(LABEL_LEFT, LABEL_TOP + LABEL_HEIGHT_INC, 400, 30);
		winMsg.setFont(new Font("SansSerif", Font.BOLD, 25));
		winMsg.setForeground(Color.GREEN);
		winMsg.setText("Congratulations, you won!!");
		panel.add(winMsg);
		winMsg.setVisible(false);

		lossMsg = new JLabel();
		lossMsg.setBounds(LABEL_LEFT, LABEL_TOP + LABEL_HEIGHT_INC, 200, 30);
		lossMsg.setFont(new Font("SanSerif", Font.BOLD, 25));
		lossMsg.setForeground(Color.RED);
		lossMsg.setText("You hit a mine, you lost.");
		panel.add(lossMsg);
		lossMsg.setVisible(false);

		flagMsg = new JLabel();
		flagMsg.setBounds(BUTTON_LEFT, BUTTON_TOP, 400, 110);
		flagMsg.setFont(new Font("SanSerif", Font.BOLD, 55));
		flagMsg.setForeground(Color.RED);
		flagMsg.setText("SET FLAG");
		panel.add(flagMsg);
		flagMsg.setVisible(false);

		guessMsg = new JLabel();
		guessMsg.setBounds(BUTTON_LEFT, BUTTON_TOP, 200, 110);
		guessMsg.setFont(new Font("SanSerif", Font.BOLD, 55));
		guessMsg.setForeground(Color.GREEN);
		guessMsg.setText("GUESS");
		panel.add(guessMsg);
		guessMsg.setVisible(true);

		totalsMsg = new JLabel("You've won " + totalWins
			+ " out of " + totalGames + " games.");
		totalsMsg.setBounds(LABEL_LEFT, LABEL_TOP + 2 * LABEL_HEIGHT_INC,
								  250, 30);
		panel.add(totalsMsg);

		if (board.get_gameOver()) {
			signalLoss();
		}

		pack();
		getContentPane().add(panel);
		getRootPane().setDefaultButton(autosolverButton);
		panel.setVisible(true);
	}

	/**
	 * Deal with the user clicking on something other than a button or a card.
	 */
	private void signalError() {
		Toolkit t = panel.getToolkit();
		t.beep();
	}

	/**
	 * Returns the image that corresponds to the input card.
	 * Image names have the format "#.GIF" or "flag.GIF" or "mine.GIF" or "unturned.GIF",
	 * @param t Tile to get the image for
	 * @return String representation of the image
	 */
	private String imageFileName(MTile t) {
		String str = "tiles/";
		if (t == null) {
			return "tiles/0.GIF";
		}
		if(t.get_status() == 'X'){
			return "tiles/flag.GIF";
		}
		if(t.get_status() == ' '){
			return "tiles/unturned.GIF";
		}
		if(t.get_status() == 'M'){
			return "tiles/mine.GIF";
		}
		str += t.get_status();
		str += ".GIF";
		return str;
	}

	/**
	 * Respond to a button click (on either the "auto solver" button
	 * or the "Flag" button or "Restart" button).
	 * @param e the button click action event
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(restartButton)){

			board = reset(false);
			getRootPane().setDefaultButton(autosolverButton);
			winMsg.setVisible(false);
			lossMsg.setVisible(false);

			repaint();
		}
		else if(board.get_gameOver()){signalError();}
		else if (e.getSource().equals(autosolverButton)) {
			board.solver();
			if(board.get_gameOverType() == 2){signalWin();}
			repaint();
		} else if (e.getSource().equals(flagButton)) {
			selection = !selection;
			if(selection){
				guessMsg.setVisible(true);
				flagMsg.setVisible(false);
			}else{
				guessMsg.setVisible(false);
				flagMsg.setVisible(true);
			}
		} else {
			signalError();
			return;
		}
	}

	/**
	 * Display a win.
	 */
	private void signalWin() {
		getRootPane().setDefaultButton(flagButton);
		winMsg.setVisible(true);
		totalWins++;
		totalGames++;
	}

	/**
	 * Display a loss.
	 */
	private void signalLoss() {
		getRootPane().setDefaultButton(flagButton);
		lossMsg.setVisible(true);
		totalGames++;
	}

	private MBoard reset(boolean first){
		boolean debugMode = false;
		String size;
		String input;
		int row = 0;
		int column = 0;

		if(first) {
			size = JOptionPane.showInputDialog("What size of game do you want to play(Small, Medium or Large):");
			if (size == null) {
				System.exit(0);
			}
			boolean invalid = true;
			while (invalid) {
				if (!(size.toLowerCase().equals("small") || size.toLowerCase().equals("s") || size.toLowerCase().equals("medium") ||
						size.toLowerCase().equals("m") || size.toLowerCase().equals("large") || size.toLowerCase().equals("l") || size.toLowerCase().equals("x"))) {
					JOptionPane.showMessageDialog(null, "Error invalid size");
					size = JOptionPane.showInputDialog("Enter the desired board size (small, medium or large):");
					if (size == null) {
						System.exit(0);
					}
				} else {
					invalid = false;
				}
			}
		}else {
			if(board.get_num_columns() == 10){
				size = "s";
			}else if(board.get_num_columns() == 14){
				size = "m";
			}else{
				size = "l";
			}
		}
		MBoard board = new MBoard(size, debugMode);

		input = JOptionPane.showInputDialog("Enter first guess for row:");
		if(input == null){
			System.exit(0);
		}
		boolean invalid = true;
		while(invalid){
			if(isNumeric(input)){
				row = Integer.parseInt(input);
				if((row < 0) || (row > (board.get_num_rows() - 1))){
					JOptionPane.showMessageDialog(null, "Invalid Entry: Guess out of bounds");
					input = JOptionPane.showInputDialog("Enter first guess for row:");
					if(input == null){
						System.exit(0);
					}
				}
				else{invalid = false;}

			}else {
				JOptionPane.showMessageDialog(null,"Error please enter integer");
				input = JOptionPane.showInputDialog("Enter first guess for row:");
				if(input == null){
					System.exit(0);
				}
			}
		}

		input = JOptionPane.showInputDialog("Enter first guess for column:");
		if(input == null){
			System.exit(0);
		}
		invalid = true;
		while(invalid){
			if(isNumeric(input)){
				column = Integer.parseInt(input);
				if((column < 0) || (column > (board.get_num_columns() - 1))){
					JOptionPane.showMessageDialog(null, "Invalid Entry: Guess out of bounds");
					input = JOptionPane.showInputDialog("Enter first guess for column:");
					if(input == null){
						System.exit(0);
					}
				}
				else{invalid = false;}

			}else {
				JOptionPane.showMessageDialog(null,"Error please enter integer");
				input = JOptionPane.showInputDialog("Enter first guess for column:");
				if(input == null){
					System.exit(0);
				}
			}
		}

		invalid = true;
		while(invalid){
			board.guess(row,column, true);
			if(board.firstGood(row,column)){
				invalid = false;
			}
			else{
				board = new MBoard(size, debugMode);
			}
		}

		return board;
	}



	private boolean isNumeric(String strNum) {
		if (strNum == null) {
			return false;
		}
		try {
			int i = Integer.parseInt(strNum);
		} catch (NumberFormatException nfe) {
			return false;
		}
		return true;
	}

	/**
	 * Receives and handles mouse clicks.  Other mouse events are ignored.
	 */
	private class MyMouseListener implements MouseListener {

		/**
		 * Handle a mouse click on a card by toggling its "selected" property.
		 * Each card is represented as a label.
		 * @param e the mouse event.
		 */
		public void mouseClicked(MouseEvent e) {
			if(board.get_gameOver()){signalError();}
			else {
				for (int k = 0; k < board.size(); k++) {
					if (e.getSource().equals(displayTiles[k])
							&& board.MTileAt(k / board.get_num_rows(), k % board.get_num_columns()) != null) {
						if (selection) {
							boolean found = board.guessUnhidden(k / board.get_num_rows(), k % board.get_num_columns());
							if (!found) {
								board.guess(k / board.get_num_rows(), k % board.get_num_columns(), false);
							}
						} else {
							if (board.MTileAt(k / board.get_num_rows(), k % board.get_num_columns()).get_status() == 'X') {
								board.unflagMine(k / board.get_num_rows(), k % board.get_num_columns());
							} else if(board.MTileAt(k / board.get_num_rows(), k % board.get_num_columns()).get_status() == ' '){
								board.flagMine(k / board.get_num_rows(), k % board.get_num_columns());
							}
						}
						if (board.get_gameOverType() == 1) {
							signalLoss();
						} else if (board.get_gameOverType() == 2) {
							signalWin();
						}
						repaint();
						return;
					}
				}
			}
			signalError();
		}

		/**
		 * Ignore a mouse exited event.
		 * @param e the mouse event.
		 */
		public void mouseExited(MouseEvent e) {
		}

		/**
		 * Ignore a mouse released event.
		 * @param e the mouse event.
		 */
		public void mouseReleased(MouseEvent e) {
		}

		/**
		 * Ignore a mouse entered event.
		 * @param e the mouse event.
		 */
		public void mouseEntered(MouseEvent e) {
		}

		/**
		 * Ignore a mouse pressed event.
		 * @param e the mouse event.
		 */
		public void mousePressed(MouseEvent e) {
		}

	}
}
