import java.awt.*;
import java.awt.color.*;
import java.awt.event.*;

import javax.swing.*;

/**
 * The BigTwoTable class implements the CardGameTable interface. 
 * It is used to build a GUI for the Big Two card game and handle all user actions.
 * @author Li Jianxiang
 */
public class BigTwoTable implements CardGameTable{
	/**
	 * a constructor for creating a BigTwoTable. 
	 * The parameter game is a reference to a card game associates with this table.
	 * @param game
	 */
	public BigTwoTable(CardGame game){
		this.game = game;
		cardImages = new Image[13][4];
		avatars = new Image[4];
		
		for (int i=0; i<13; i++){
			for (int j=0; j<4; j++){
				if(j==0)
					cardImages[i][j] = new ImageIcon(""+(i+1)+"d.jpg").getImage();
				if(j==1)
					cardImages[i][j] = new ImageIcon(""+(i+1)+"c.jpg").getImage();
				if(j==2)
					cardImages[i][j] = new ImageIcon(""+(i+1)+"h.jpg").getImage();
				if(j==3)
					cardImages[i][j] = new ImageIcon(""+(i+1)+"s.jpg").getImage();
			}
		}
		cardBackImage = new ImageIcon("cardback.jpg").getImage();
		winImage = new ImageIcon("win.png").getImage();
		avatars[0] = new ImageIcon("1.png").getImage();
		avatars[1] = new ImageIcon("4.png").getImage();
		avatars[2] = new ImageIcon("2.png").getImage();
		avatars[3] = new ImageIcon("3.png").getImage();
		frame = new JFrame();
		frame.setSize(1280, 720);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		bigTwoPanel = new BigTwoPanel();
		bigTwoPanel.addMouseListener((MouseListener) bigTwoPanel);
		playButton = new JButton("PLAY");
		playButton.addActionListener(new PlayButtonListener());
		passButton = new JButton("PASS");
		passButton.addActionListener(new PassButtonListener());
		JPanel textPanel = new JPanel();
		textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
		msgArea = new JTextArea(50,25);
		msgArea.setLineWrap(true);
		JScrollPane scroller = new JScrollPane(msgArea);
		scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		textPanel.add(scroller);
		
		chatArea = new JTextArea(50,25);
		chatArea.setLineWrap(true);
		JScrollPane scroller1= new JScrollPane(chatArea);
		scroller1.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scroller1.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		textPanel.add(scroller1);
		
		field = new JTextField(20);
		field.addActionListener(new ChatMsgListener());
		JLabel label = new JLabel("Message:");
		JPanel msgPanel = new JPanel();
		msgPanel.add(label);
		msgPanel.add(field);
		textPanel.add(msgPanel);
		
		frame.add(textPanel,BorderLayout.EAST);
		
		
		
		frame.add(bigTwoPanel);
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.add(playButton);
		buttonPanel.add(passButton);
		
		frame.add(buttonPanel,BorderLayout.SOUTH);
		
		menuBar = new JMenuBar();
		menu = new JMenu("Game");
		messageMenu = new JMenu("Message");
		connect = new JMenuItem("Connect");
		quit = new JMenuItem("Quit");
		clearChatMsg = new JMenuItem("Clear Chat");
		clearMsg = new JMenuItem("Clear Game Messages");
		messageMenu.add(clearMsg);
		messageMenu.add(clearChatMsg);
		menu.add(connect);
		menu.add(quit);
		menuBar.add(menu);
		menuBar.add(messageMenu);
		frame.setJMenuBar(menuBar);
		connect.addActionListener(new ConnectMenuItemListener());
		quit.addActionListener(new QuitMenuItemListener());
		clearMsg.addActionListener(new MsgMenuItemListener());
		clearChatMsg.addActionListener(new ChatMenuItemListener());
		frame.setVisible(true);
		msgArea.setVisible(true);
		playButton.setVisible(true);
		passButton.setVisible(true);
		
		
		
	}
	
	private CardGame game;
	private boolean[] selected;
	private int activePlayer;
	private JFrame frame;
	private JPanel bigTwoPanel;
	private JButton playButton;
	private JButton passButton;
	private JTextArea msgArea;
	private JTextArea chatArea;
	private JTextField field;
	private Image[][] cardImages; 
	private Image cardBackImage;
	private Image[] avatars;
	private Image winImage;
	private boolean panelEnabled;
	private JMenuBar menuBar;
	private JMenu menu;
	private JMenuItem connect;
	private JMenuItem quit;
	private JMenu messageMenu;
	private JMenuItem clearChatMsg;
	private JMenuItem clearMsg;
	
	//CardGameTable interface methods:
	/**
	 * a method for setting the index of the active player (i.e., the current player).
	 */
	public void setActivePlayer(int activePlayer){this.activePlayer=activePlayer;}
	/**
	 * a method for getting an array of indices of the cards selected.
	 */
	public int[] getSelected(){
		int j=0;
		for(int i=0; i<selected.length;i++){
			if (selected[i]){
				j++;
			}
		}
		if(j==0)
			return null;
		int[] a = new int[j];
		j=0;
		for(int i=0;i<selected.length;i++){
			if(selected[i]){
				a[j] = i;
				j++;
			}
		}
		return a;
	}
	/**
	 * a method for resetting the list of selected cards.
	 */
	public void resetSelected(){
		selected = new boolean[13];
		for(int i=0; i< selected.length;i++){
			selected[i]=false;
		}
	}
	/**
	 * a method for repainting the GUI.
	 */
	public void repaint(){ bigTwoPanel.repaint(); }
	/**
	 * a method for printing the specified string to the message area of the GUI.
	 */
	public void printMsg(String msg){ 
		msgArea.append(msg);
		msgArea.setCaretPosition(msgArea.getDocument().getLength());
	}
	/**
	 * a method for printing the specified string to the chat area of the GUI.
	 */
	public void printChat(String msg){ 
		chatArea.append(msg);
		chatArea.setCaretPosition(chatArea.getDocument().getLength());
	}
	/**
	 * a method for clearing the message area of the GUI.
	 */
	public void clearMsgArea(){ msgArea.setText(null);}
	/**
	 * a method for clearing the chat area of the GUI.
	 */
	public void clearChat(){ chatArea.setText(null);}
	/**
	 * a method for resetting the GUI.
	 */
	public void reset(){
		this.clearMsgArea();
		this.repaint();		
	}
	/**
	 * a method for enabling user interactions with the GUI.
	 */
	public void enable(){
		panelEnabled=true;
		playButton.setEnabled(true);
		passButton.setEnabled(true);
	}
	/**
	 * a method for disabling user interactions with the GUI.
	 */
	public void disable(){
		panelEnabled=false;
		playButton.setEnabled(false);
		passButton.setEnabled(false);
	}
	/**
	 * a method for enabling connect menu item.
	 */
	public void enableConnect(){
		connect.setEnabled(true);
	}
	/**
	 * a method for disabling connect menu item.
	 */
	public void disableConnect(){
		connect.setEnabled(false);
	}
	//inner classes:
	/**
	 * an inner class that extends the JPanel class and 
	 * implements the MouseListener interface. 
	 * Overrides the paintComponent() method inherited from the JPanel class 
	 * to draw the card game table. 
	 * Implements the mouseClicked() method from the MouseListener interface 
	 * to handle mouse click events.
	 */
	public class BigTwoPanel extends JPanel implements MouseListener{
		private int[][] cardLeft;
		private int[][] cardUp;
		private int width;
		private int height;
		/**
		 * an overriding method to paint the background of BigTwoPanel to be green.
		 */
		public void paintComponent(Graphics g){
			if (((BigTwoClient)game).isPreviousGameDisplay()){
				Image image = new ImageIcon("digibg.jpg").getImage();
				g.drawImage(image,0, 0, getWidth(), getHeight(),this);
				width = this.getWidth()/15;
				height = this.getHeight()/6;
				cardLeft = new int[4][13];
				cardUp = new int[4][13];
				g.drawImage(avatars[0],this.getWidth()/100,this.getHeight()/30, height, height, bigTwoPanel);
				g.drawImage(avatars[1],this.getWidth()/100,this.getHeight()/30+this.getHeight()/5, height, height, bigTwoPanel);
				g.drawImage(avatars[2],this.getWidth()/100,this.getHeight()/30+2*this.getHeight()/5,height, height, bigTwoPanel);
				g.drawImage(avatars[3],this.getWidth()/100,this.getHeight()/30+3*this.getHeight()/5,height, height, bigTwoPanel);
				for (int j=0; j<4; j++){
					if (j==game.getCurrentIdx())
						continue;
					for(int i=0;i<game.getPlayerList().get(j).getNumOfCards();i++){
						cardLeft[j][i]=this.getWidth()/30+i*this.getWidth()/20+width;
						cardUp[j][i]= this.getHeight()/30+j*this.getHeight()/5;
						g.drawImage(cardImages[game.getPlayerList().get(j).getCardsInHand().getCard(i).getRank()][game.getPlayerList().get(j).getCardsInHand().getCard(i).getSuit()],cardLeft[j][i],cardUp[j][i],width, height, this);
					}
				}
				if(game.getHandsOnTable().isEmpty()){
					Font font = new Font("serif",Font.BOLD,16*this.getHeight()/480);
					g.setFont(font);
					g.setColor(Color.BLUE);
					g.drawString("Table is empty", this.getWidth()/30, 4*this.getHeight()/5+this.getHeight()/9);
				}else{
					for(int i=0; i < game.getHandsOnTable().get(game.getHandsOnTable().size()-1).size();i++){
						g.drawImage(cardImages[game.getHandsOnTable().get(game.getHandsOnTable().size()-1).getCard(i).getRank()][game.getHandsOnTable().get(game.getHandsOnTable().size()-1).getCard(i).getSuit()],(i+1)*(this.getWidth()/50+width)+this.getWidth()/25, this.getHeight()/50+4*this.getHeight()/5, width, height, bigTwoPanel);
					}
					Font font = new Font("serif",Font.BOLD,16*this.getHeight()/480);
					g.setFont(font);
					g.setColor(Color.BLUE);
					g.drawString("Played by", this.getWidth()/30, 4*this.getHeight()/5+this.getHeight()/9);
					g.drawString(game.getPlayerList().get(((BigTwoClient) game).getTablePlayerIdx()).getName(), this.getWidth()/30, 4*this.getHeight()/5+this.getHeight()/7);
				}
				for (int i=0; i<4; i++){
					Font font = new Font("serif",Font.BOLD,16*this.getHeight()/480);
					g.setFont(font);
					g.setColor(Color.BLUE);
					g.drawString("Player"+i, this.getWidth()/50, i*this.getHeight()/5+this.getHeight()/17+height);
				}
				cardLeft[game.getCurrentIdx()][0]=this.getWidth()/30+width;
				cardUp[game.getCurrentIdx()][0]=this.getHeight()/30 + game.getCurrentIdx()*this.getHeight()/5;
				g.drawImage(winImage,this.getWidth()/3,cardUp[game.getCurrentIdx()][0],2*height,height, this);
			}else{
			Image image = new ImageIcon("digibg.jpg").getImage();
			g.drawImage(image,0, 0, getWidth(), getHeight(),this);
			width = this.getWidth()/15;
			height = this.getHeight()/6;
			cardLeft = new int[4][13];
			cardUp = new int[4][13];
			Font font = new Font("serif",Font.BOLD,16*this.getHeight()/480);
			g.setFont(font);
			g.setColor(Color.BLUE);
			
			for (int i=0; i<4; i++){
				if(game.getPlayerList().get(i).getName()!=null){
					g.drawImage(avatars[i],this.getWidth()/300,this.getHeight()/30+i*this.getHeight()/5, height, height, bigTwoPanel);
					g.setColor(Color.BLUE);
					g.drawString(game.getPlayerList().get(i).getName(), this.getWidth()/50, i*this.getHeight()/5+this.getHeight()/17+height);
				}
			}
			if(((BigTwoClient)game).isGameInProgress()){
				for (int i=0; i<4; i++){
					if(game.getPlayerList().get(i).getName()!=null){
						if(game.getCurrentIdx()!=i){
							g.drawImage(avatars[i],this.getWidth()/300,this.getHeight()/30+i*this.getHeight()/5, height, height, bigTwoPanel);
							g.setColor(Color.BLUE);
							g.drawString(game.getPlayerList().get(i).getName(), this.getWidth()/50, i*this.getHeight()/5+this.getHeight()/17+height);
						}
						else{
							g.drawImage(avatars[i],this.getWidth()/300,this.getHeight()/30+i*this.getHeight()/5, height, height, bigTwoPanel);
							g.setColor(Color.YELLOW);
							g.drawString(game.getPlayerList().get(i).getName(), this.getWidth()/50, i*this.getHeight()/5+this.getHeight()/17+height);
						}
					}
				}
				for (int j=0; j<4; j++){
					if (j==activePlayer)
						continue;
					for(int i=0;i<game.getPlayerList().get(j).getNumOfCards();i++){
						cardLeft[j][i]=this.getWidth()/30+i*this.getWidth()/20+width;
						cardUp[j][i]= this.getHeight()/30+j*this.getHeight()/5;
						g.drawImage(cardBackImage,cardLeft[j][i],cardUp[j][i],width, height, this);
					}
				}
				for (int i=0;i<game.getPlayerList().get(activePlayer).getNumOfCards();i++){
					cardLeft[activePlayer][i]=this.getWidth()/30+i*this.getWidth()/20+width;
					if (selected[i])
						cardUp[activePlayer][i]= activePlayer*this.getHeight()/5;
					else cardUp[activePlayer][i]=this.getHeight()/30 + activePlayer*this.getHeight()/5;
					g.drawImage(cardImages[game.getPlayerList().get(activePlayer).getCardsInHand().getCard(i).getRank()][game.getPlayerList().get(activePlayer).getCardsInHand().getCard(i).getSuit()],cardLeft[activePlayer][i],cardUp[activePlayer][i],width, height, this);
				}
				if(game.getHandsOnTable().isEmpty()){
					font = new Font("serif",Font.BOLD,16*this.getHeight()/480);
					g.setFont(font);
					g.setColor(Color.BLUE);
					g.drawString("Table is empty", this.getWidth()/30, 4*this.getHeight()/5+this.getHeight()/9);
				}else{
					for(int i=0; i < game.getHandsOnTable().get(game.getHandsOnTable().size()-1).size();i++){
						g.drawImage(cardImages[game.getHandsOnTable().get(game.getHandsOnTable().size()-1).getCard(i).getRank()][game.getHandsOnTable().get(game.getHandsOnTable().size()-1).getCard(i).getSuit()],(i+1)*(this.getWidth()/50+width)+this.getWidth()/25, this.getHeight()/50+4*this.getHeight()/5, width, height, bigTwoPanel);
					}
					font = new Font("serif",Font.BOLD,16*this.getHeight()/480);
					g.setFont(font);
					g.setColor(Color.BLUE);
					g.drawString("Played by", this.getWidth()/30, 4*this.getHeight()/5+this.getHeight()/9);
					g.drawString(game.getPlayerList().get(((BigTwoClient) game).getTablePlayerIdx()).getName(), this.getWidth()/30, 4*this.getHeight()/5+this.getHeight()/7);
				}
			}
			}
		}
		/**
		 * an implementing method to handle mouse click events to select cards.
		 */
		public void mouseClicked(MouseEvent e) {
			if (panelEnabled){
			if (e.getX()>cardLeft[activePlayer][game.getPlayerList().get(activePlayer).getNumOfCards()-1] 
					&& cardLeft[activePlayer][game.getPlayerList().get(activePlayer).getNumOfCards()-1]+width>e.getX() 
					&& e.getY()>cardUp[activePlayer][game.getPlayerList().get(activePlayer).getNumOfCards()-1] 
					&& cardUp[activePlayer][game.getPlayerList().get(activePlayer).getNumOfCards()-1]+height>e.getY()){
				selected[game.getPlayerList().get(activePlayer).getNumOfCards()-1] = !selected[game.getPlayerList().get(activePlayer).getNumOfCards()-1];
				this.repaint();
			}else{
			for (int i=0; i<game.getPlayerList().get(activePlayer).getNumOfCards()-1;i++){
				if (e.getX()>cardLeft[activePlayer][i] 
						&& cardLeft[activePlayer][i]+this.getWidth()/20>e.getX() 
						&& e.getY()>=cardUp[activePlayer][i] 
						&& cardUp[activePlayer][i]+height>e.getY()){
					selected[i] = !selected[i];
					this.repaint();
				}
				else if (e.getX()>cardLeft[activePlayer][i] 
						&& cardLeft[activePlayer][i]+width>e.getX() 
						&& e.getY()>cardUp[activePlayer][i] 
						&& cardUp[activePlayer][i]+this.getHeight()/30>e.getY()
						&& selected[i] 
						&& selected[i+1]==false){
					selected[i] = !selected[i];
					this.repaint();
				}
				else if (e.getX()>cardLeft[activePlayer][i] 
						&& cardLeft[activePlayer][i]+width>e.getX() 
						&& e.getY()>cardUp[activePlayer][i]+height -this.getHeight()/30
						&& cardUp[activePlayer][i]+height>e.getY()
						&& selected[i] == false
						&& selected[i+1]){
					selected[i] = !selected[i];
					this.repaint();
				}
			}}}
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
		public void mouseReleased(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}
	}
	/**
	 * an inner class that implements the ActionListener interface. 
	 * Implements the actionPerformed() method from the ActionListener interface 
	 * to handle button-click events for the ¡°Play¡± button.
	 */
	public class PlayButtonListener implements ActionListener{
		/**
		 * an implementing method to handle the event when play button is clicked.
		 */
		public void actionPerformed(ActionEvent e) {
			if(game.getCurrentIdx()==((BigTwoClient)game).getPlayerID()){
				if(((BigTwoClient)game).localMoveCheck(getSelected())){
					((BigTwoClient)game).makeMove(game.getCurrentIdx(), getSelected());
				}
			}
		}
	}
	/**
	 * an inner class that implements the ActionListener interface. 
	 * Implements the actionPerformed() method from the ActionListener interface 
	 * to handle button-click events for the ¡°Pass¡± button.
	 */
	public class PassButtonListener implements ActionListener{
		/**
		 * an implementing method to handle the event when pass button is clicked.
		 */
		public void actionPerformed(ActionEvent e) {
			if (game.getCurrentIdx()==((BigTwoClient)game).getPlayerID()){
				if (((BigTwoClient) game).getStarter()){
					printMsg(" ==> Starter cannot pass!\n");
				}
				else if (((BigTwoClient) game).getCurrentIdx()==((BigTwoClient) game).getTablePlayerIdx()){
					printMsg(" ==> You are free to play any hand!\n");
				}
				else{
					for(int i=0;i<selected.length;i++){
						selected[i]=false;
					}
					repaint();
					((BigTwoClient) game).makeMove(game.getCurrentIdx(), null);
				}
			}
		}
	}
	/**
	 * an inner class that implements the ActionListener interface. 
	 * Implements the actionPerformed() method from the ActionListener interface 
	 * to handle message sent by a player.
	 */
	public class ChatMsgListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			CardGameMessage message = new CardGameMessage(CardGameMessage.MSG,((BigTwoClient)game).getPlayerID(),field.getText());
			((BigTwoClient)game).sendMessage(message);
			field.setText("");
		}
		
	}
	/**
	 * an inner class that implements the ActionListener interface. 
	 * Implements the actionPerformed() method from the ActionListener interface 
	 * to handle menu-item-click events for the ¡°Connect¡± menu item.
	 */
	public class ConnectMenuItemListener implements ActionListener{
		/**
		 * implements actionPeformed method to handle the event of connect menu item being clicked.
		 */
		public void actionPerformed(ActionEvent e) {
			((BigTwoClient)game).resetAllLists();
			repaint();
			((BigTwoClient)game).makeConnection();
		}
	}
	/**
	 * an inner class that implements the ActionListener interface. 
	 * Implements the actionPerformed() method from the ActionListener interface 
	 * to handle menu-item-click events for the ¡°Quit¡± menu item.
	 *
	 */
	public class QuitMenuItemListener implements ActionListener{
		/**
		 * implements actionPeformed method to handle the event of quit menu item being clicked.
		 */
		public void actionPerformed(ActionEvent e) {
			System.exit(0);
		}
	}
	/**
	 * an inner class that implements the ActionListener interface. 
	 * Implements the actionPerformed() method from the ActionListener interface 
	 * to handle menu-item-click events for the ¡°Clear Chat¡± menu item.
	 */
	public class ChatMenuItemListener implements ActionListener{
		/**
		 * implements actionPeformed method to handle the event of clearChat menu item being clicked.
		 */
		public void actionPerformed(ActionEvent e) {
			clearChat();
		}
	}
	/**
	 * an inner class that implements the ActionListener interface. 
	 * Implements the actionPerformed() method from the ActionListener interface 
	 * to handle menu-item-click events for the ¡°Clear Game Message¡± menu item.
	 */
	public class MsgMenuItemListener implements ActionListener{
		/**
		 * implements actionPeformed method to handle the event of clearMsg menu item being clicked.
		 */
		public void actionPerformed(ActionEvent e) {
			clearMsgArea();
		}
	}
}
