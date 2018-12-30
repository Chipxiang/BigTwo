import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.JOptionPane;


/**
 * The BigTwoClient class implements the CardGame interface and NetworkGame interface. 
 * It is used to model a Big Two card game that supports 4 players playing over the internet.
 * @author Li Jianxiang
 *
 */
public class BigTwoClient implements CardGame, NetworkGame{
	//public constructor:
		/**
		 * a constructor for creating a Big Two client. 
		 * It creates 4 players and add them to the list of players;
		 * It creates a Big Two table which builds the GUI for the game and handles user actions;
		 * It makes a connection to the game server by calling the makeConnection() method from the NetworkGame interface.
		 */
		public BigTwoClient(){
			gameInProgress = false;
			previousGameDisplay = false;
			this.handsOnTable = new ArrayList<Hand>();
			playerList = new ArrayList<CardGamePlayer>();
			CardGamePlayer playerA = new CardGamePlayer();
			playerA.setName(null);
			playerList.add(playerA);
			CardGamePlayer playerB = new CardGamePlayer();
			playerB.setName(null);
			playerList.add(playerB);
			CardGamePlayer playerC = new CardGamePlayer();
			playerC.setName(null);
			playerList.add(playerC);
			CardGamePlayer playerD = new CardGamePlayer();
			playerD.setName(null);
			playerList.add(playerD);
			this.table = new BigTwoTable(this);
			this.deck = new BigTwoDeck();
			this.makeConnection();
		}
	//private instance variables:
		private int numOfPlayers;
		private Deck deck;
		private ArrayList<CardGamePlayer> playerList;
		private ArrayList<Hand> handsOnTable;
		private int playerID;
		private String playerName;
		private String serverIP;
		private int serverPort;
		private Socket sock;
		private ObjectOutputStream oos;
		private ObjectInputStream ois;
		private int currentIdx;
		private BigTwoTable table;
		private boolean starter;
		private int tablePlayerIdx;
		private boolean gameInProgress;
		private boolean previousGameDisplay;
	
		
		//CardGame interface methods:
		/**
		 * a method for getting the game status.
		 */
		public boolean isGameInProgress() {
			return gameInProgress;
		}
		/**
		 * a method for setting the game status.
		 */
		public void setGameInProgress(boolean gameInProgress) {
			this.gameInProgress = gameInProgress;
		}
		/**
		 * a method for getting the instance deciding if the previous game should be displayed on the table.
		 */
		public boolean isPreviousGameDisplay() {
			return previousGameDisplay;
		}
		/**
		 * a method for setting the instance deciding if the previous game should be displayed on the table.
		 */
		public void setPreviousGameDisplay(boolean previousGameDisplay) {
			this.previousGameDisplay = previousGameDisplay;
		}
		/**
		 * a method for getting the number of players.
		 */
		public int getNumOfPlayers(){return numOfPlayers;}
		/**
		 * a method for getting the deck of cards being used.
		 */
		public Deck getDeck(){return deck;}
		/**
		 * a method for getting the list of players.
		 */
		public ArrayList<CardGamePlayer> getPlayerList(){return playerList;}
		/**
		 * a method for getting the list of hands played on the table.
		 */
		public ArrayList<Hand> getHandsOnTable(){return handsOnTable;}
		/**
		 * a method for getting the index of the player for the current turn.
		 */
		public int getCurrentIdx(){return currentIdx;}
		/**
		 * a method for getting if the current player is a starter.
		 * @return
		 */
		public boolean getStarter(){return starter;}
		/**
		 * a method for getting the index of the player of hand on the table played.
		 * @return
		 */
		public int getTablePlayerIdx(){return tablePlayerIdx;}
		/**
		 * a method for passing to next player to make the game proceed.
		 * @return
		 */
		public void updateCurrentIdx(){
			currentIdx++;
			if (currentIdx > 3)
				currentIdx=0;
		}
		/**
		 * a method to reset all the arraylists.
		 */
		public void resetAllLists(){
			for (int i = 0;i<4;i++){
				playerList.get(i).removeAllCards();
			}
			this.handsOnTable.clear();
			this.handsOnTable = new ArrayList<Hand>();
		}
		/**
		 * a method for double checking if the card index sent by another player is legal or not.
		 * If the card index is illegal, there's suspect of cheaters.
		 * The client will cut down the connection to protect the local player.
		 */
		public boolean doubleCheck(int playerID,int[] cardIdx){
			CardList cards = new CardList();
			if (cardIdx==null){
				if(starter||playerID==this.getTablePlayerIdx()){
					table.printMsg("Double check failed!!!\nCheating might exist!!!\n");
					try {
						sock.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
					oos = null;
					ois = null;
					serverIP = null;
					playerName = null;
					table.enableConnect();
					table.repaint();
					return false;
				}else return true;
			}
			else {
			for(int i=0;i<cardIdx.length;i++){
				cards.addCard(playerList.get(currentIdx).getCardsInHand().getCard(cardIdx[i]));
			}
			Hand hand = BigTwoClient.composeHand(this.getPlayerList().get(getCurrentIdx()), cards);
			if(hand==null){
				table.printMsg("Double check failed!!!\nCheating might exist!!!\n");
				try {
					sock.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				oos = null;
				ois = null;
				serverIP = null;
				playerName = null;
				table.enableConnect();
				table.repaint();
				return false;
			}
			else{
				hand.sort();
				if(starter){
					if (hand.getCard(0).getRank()!=2 || hand.getCard(0).getSuit()!=0){
						table.printMsg("Double check failed!!!\nCheating might exist!!!\n");
						try {
							sock.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
						oos = null;
						ois = null;
						serverIP = null;
						playerName = null;
						table.enableConnect();
						table.repaint();
						return false;
					}
					else {
						return true;
					}
				}
				else if (!hand.beats(this.getHandsOnTable().get(this.getHandsOnTable().size()-1))&&this.tablePlayerIdx!=this.currentIdx){
					table.printMsg("Double check failed!!!\nCheating might exist!!!\n");
					try {
						sock.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
					oos = null;
					ois = null;
					serverIP = null;
					playerName = null;
					table.enableConnect();
					table.repaint();
					return false;
				}
				else {
					return true;
				}
			}}
		}
		/**
		 * a method for starting/restarting the game with a given shuffled deck of cards. 
		 * It removes all the cards from the players as well as from the table;
		 * It distributes the cards to the players;
		 * It identifies the player who holds the 3 of Diamonds; 
		 * It sets the currentIdx of the BigTwoClient instance to the playerID (i.e., index) 
		 * of the player who holds the 3 of Diamonds; 
		 * It sets the activePlayer of the BigTwoTable instance to the playerID (i.e., index) of the local
		 * player (i.e., only shows the cards of the local player and the local player can only select
		 * cards from his/her own hand).
		 */
		public void start(Deck deck){
			for (int i = 0; i <13 ; i++){
				this.getPlayerList().get(0).addCard(deck.getCard(i));
			}
			this.getPlayerList().get(0).sortCardsInHand();
			for (int i = 13; i <26 ; i++){
				this.getPlayerList().get(1).addCard(deck.getCard(i));
			}
			this.getPlayerList().get(1).sortCardsInHand();
			for (int i = 26; i <39 ; i++){
				this.getPlayerList().get(2).addCard(deck.getCard(i));
			}
			this.getPlayerList().get(2).sortCardsInHand();
			for (int i = 39; i <52 ; i++){
				this.getPlayerList().get(3).addCard(deck.getCard(i));
			}
			this.getPlayerList().get(3).sortCardsInHand();
			for (int i=0; i<4; i++){
				if (this.getPlayerList().get(i).getCardsInHand().getCard(0).getRank() == 2){
					if (this.getPlayerList().get(i).getCardsInHand().getCard(0).getSuit() == 0){
						currentIdx=i;
						starter = true;
						break;
					}
				}
			}
			table.enable();
			table.setActivePlayer(playerID);
			table.resetSelected();
			table.printMsg(this.getPlayerList().get(currentIdx).getName()+"(Player "+ this.getCurrentIdx()+ ")" + "'s turn:\n");
			table.repaint();
		}
		/**
		 * a method for making a move by a player with the specified playerID 
		 * using the cards specified by the list of indices. 
		 * This method should be called from the BigTwoTable when the local player presses either the 
		 * ¡°Play¡± or ¡°Pass¡± button.
		 * It creates a CardGameMessage object of the type MOVE, with the playerID and data in this message 
		 * being -1 and cardIdx, respectively, and send it to the game server using the sendMessage() method 
		 * from the NetworkGame	interface.
		 */
		public void makeMove(int playerID, int[] cardIdx){
			CardGameMessage message = new CardGameMessage(CardGameMessage.MOVE,-1, cardIdx);
			this.sendMessage(message);
		}
		/**
		 * To check the if the move made by the local player is legal or not and decide whether to send a 
		 * MOVE message to the server.
		 * @return
		 */
		public boolean localMoveCheck(int[] cardIdx){
			CardList cards = new CardList();
			if (table.getSelected()==null){
				String string = "";
				table.printMsg(string + " ==> Please select the card you want to play!\n");
				table.resetSelected();
				table.repaint();
				return false;
			}
			else {
			for(int i=0;i<table.getSelected().length;i++){
				cards.addCard(playerList.get(currentIdx).getCardsInHand().getCard(cardIdx[i]));
			}
			
			
			Hand hand = BigTwoClient.composeHand(this.getPlayerList().get(getCurrentIdx()), cards);
			
			if(hand==null){
				String string = "";
				for (int i=0; i<cards.size();i++){
					string = string + "["+ cards.getCard(i).toString() + "]";
				}
				table.printMsg(string + "\n ==> Not a legal move!!!\n");
				table.resetSelected();
				table.repaint();
				return false;
			}
			else{
				hand.sort();
				if(starter){
					if (hand.getCard(0).getRank()!=2 || hand.getCard(0).getSuit()!=0){
						String string = "";
						string = string + "{" + hand.getType() + "}";
						for (int i=0; i<hand.size();i++){
							string = string + "["+ hand.getCard(i).toString() + "]";
						}
						table.printMsg(string + "\n ==> Please include " + '\u2666' +"3 in your move!\n");
						table.resetSelected();
						table.repaint();
						return false;
					}
					else {
						return true;
					}
				}
				else if (!hand.beats(this.getHandsOnTable().get(this.getHandsOnTable().size()-1))&&this.tablePlayerIdx!=this.currentIdx){
					String string="";
					string = string + "{" + hand.getType() + "}";
					for(int i=0;i<hand.size();i++){
						string = string + "["+ hand.getCard(i).toString() + "]";
					}
					table.printMsg(string + "\n ==> This move cannot beat the hand on table!\n");
					table.resetSelected();
					table.repaint();
					return false;
				}
				else {
					return true;
				}
			}}
		}
		/** 
		 * a method for checking a move made by a player. This method should be called from the parseMessage()
		 * method from the NetworkGame interface when a message of the type MOVE is received from the game server. 
		 * The playerID and data in this message give the playerID of the player who makes the move and a reference 
		 * to a regular array of integers specifying the indices of the selected cards, respectively. 
		 * These are used as the arguments in calling the checkMove() method.
		 */
		public void checkMove(int playerID, int[] cardIdx){
			if (doubleCheck(playerID, cardIdx)){
				CardList cards = new CardList();
				if (cardIdx==null){
						table.printMsg("{Pass}\n");
						this.updateCurrentIdx();
						table.printMsg(this.getPlayerList().get(this.getCurrentIdx()).getName()+"(Player "+ this.getCurrentIdx()+ ")"+"'s turn:\n");
				}else{
					if(starter){
						starter = false;
					}
					for(int i=0;i<cardIdx.length;i++){
						cards.addCard(playerList.get(currentIdx).getCardsInHand().getCard(cardIdx[i]));
					}
					Hand hand = BigTwoClient.composeHand(this.getPlayerList().get(getCurrentIdx()), cards);
					this.getPlayerList().get(getCurrentIdx()).removeCards(hand);
					this.getHandsOnTable().add(hand);
					String string="";
					string = string + "{" + hand.getType() + "}";
					for(int i=0;i<hand.size();i++){
						string = string + "["+ hand.getCard(i).toString() + "]";
					}
					table.printMsg(string+"\n");
					table.repaint();
					if (this.endOfGame()){
						table.printMsg("Game Over! "+this.getPlayerList().get(currentIdx).getName()+"(Player "+ this.getCurrentIdx()+ ")"+" Wins!\n");
						table.resetSelected();
						table.disable();
						table.repaint();
						gameInProgress = false;
						String msg="";
						for(int i=0;i<4;i++){
							if(i!=this.getCurrentIdx()){
								msg+=this.getPlayerList().get(i).getName()+"(Player "+ i + ") has " + this.getPlayerList().get(i).getNumOfCards()+" cards in hand.\n"; 
							}
						}
						msg+=this.getPlayerList().get(this.getCurrentIdx()).getName()+"(Player "+this.getCurrentIdx()+") wins the game!!!";
						JOptionPane.showMessageDialog(null,"Game Result:\n"+ msg);
						CardGameMessage readyMsg = new CardGameMessage(CardGameMessage.READY, -1,null);
						sendMessage(readyMsg);

					}
					else{
						tablePlayerIdx=this.getCurrentIdx();
						this.updateCurrentIdx();
						table.resetSelected();
						table.printMsg(this.getPlayerList().get(currentIdx).getName()+"(Player "+ this.getCurrentIdx()+ ")"+"'s turn:\n");
						table.repaint();
					}
				}
			}
		}
		/**
		 * a method for checking if the game ends.
		 */
		public boolean endOfGame(){
			if(this.getPlayerList().get(currentIdx).getCardsInHand().isEmpty()&& gameInProgress){
				previousGameDisplay = true;
				return true;
			}
			else return false;
		}
	//NetworkGame interface methods:
		/**
		 * a method for getting the playerID (i.e., index) of the local player.
		 */
		public int getPlayerID(){return this.playerID;}
		/**
		 * a method for setting the playerID (i.e., index) of the local player. 
		 * This method should be called from the parseMessage() method when a message 
		 * of the type PLAYER_LIST is received from the game server.
		 */
		public void setPlayerID(int playerID){this.playerID=playerID;}
		/**
		 * a method for getting the name of the local player.
		 */
		public String getPlayerName(){return this.playerName;}
		/**
		 * a method for setting the name of the local player.
		 */
		public void setPlayerName(String playerName){this.playerName = playerName;}
		/**
		 * a method for getting the IP address of the game server.
		 */
		public String getServerIP(){return this.serverIP;}
		/**
		 * a method for setting the IP address of the game server.
		 */
		public void setServerIP(String serverIP){this.serverIP = serverIP;}
		/**
		 * a method for getting the TCP port of the game server.
		 */
		public int getServerPort(){return this.serverPort;}
		/**
		 * a method for setting the TCP port of the game server.
		 * @param serverPort
		 */
		public void setServerPort(int serverPort){this.serverPort=serverPort;}
		/**
		 * a method for making a socket connection with the game server. Upon successful connection,
		 * It creates an ObjectOutputStream for sending messages to the game server; 
		 * It creates a thread for receiving messages from the game server; 
		 * It sends a message of the type JOIN to the game server, with playerID being -1 
		 * and data being a reference to a string representing the name of the local player; 
		 * It sends a message of the type READY to the game server, with playerID and data being -1 and null, respectively.
		 */
		public void makeConnection(){
			serverIP = JOptionPane.showInputDialog("Server IP","127.0.0.1");
			playerName = JOptionPane.showInputDialog("Your Name","Player ");
			while(playerName == null)
				playerName = JOptionPane.showInputDialog("Your Name","Player ");
			try {
				sock = new Socket(serverIP, 2396);
				oos = new ObjectOutputStream(sock.getOutputStream());
				Runnable threadJob = new ServerHandler();
				Thread messageReceiver = new Thread(threadJob);
				messageReceiver.start();
				CardGameMessage message1 = new CardGameMessage(CardGameMessage.JOIN,-1, playerName);
				this.sendMessage(message1);
				CardGameMessage message2 = new CardGameMessage(CardGameMessage.READY,-1, null);
				this.sendMessage(message2);
			} catch (Exception ex){
				table.printMsg("Cannot connect to the server!!!\nPlease try again!!!\n");
				ex.printStackTrace();
			}
		}
		/**
		 * a method for parsing the messages received from the game server. 
		 * This method should be called from the thread responsible for receiving messages from the game server. 
		 * Based on the message type, different actions will be carried out.
		 */
		public synchronized void parseMessage(GameMessage message){
			switch (message.getType()) {
			case CardGameMessage.PLAYER_LIST:
				playerID = message.getPlayerID();
				for(int i=0;i<((String[])message.getData()).length;i++){
					this.playerList.get(i).setName(((String[])message.getData())[i]);
				}
				break;
			case CardGameMessage.JOIN:
				this.playerList.get(message.getPlayerID()).setName((String)message.getData());
				break;
			case CardGameMessage.FULL:
				table.enableConnect();
				table.printMsg("Server is full! Unable to join.\n");
				try {
					sock.close();
					oos = null;
					ois = null;
					serverIP = null;
					playerName = null;
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			case CardGameMessage.QUIT:
				this.playerList.get(message.getPlayerID()).setName(null);
				table.printMsg("Player" + message.getPlayerID() + "(" + (String)message.getData()+")  Leaves the game!\n");
				if(gameInProgress){
					gameInProgress = false;
					table.resetSelected();
					this.resetAllLists();
					table.disable();
					CardGameMessage msg = new CardGameMessage(CardGameMessage.READY,playerID,null);
					this.sendMessage(msg);
				}
				break;
			case CardGameMessage.READY:
				previousGameDisplay = false;
				table.disableConnect();
				this.resetAllLists();
				table.printMsg(this.getPlayerList().get(message.getPlayerID()).getName()+"(Player "+message.getPlayerID()+") is ready!\n");
				break;
			case CardGameMessage.START:
				gameInProgress = true;
				table.printMsg("All players are ready! Game Start!\n");;
				this.start((BigTwoDeck)message.getData());
				break;
			case CardGameMessage.MOVE:
				this.checkMove(message.getPlayerID(), (int[])message.getData());
				break;
			case CardGameMessage.MSG:
				table.printChat((String)message.getData()+"\n");
				break;
			}
			table.repaint();
		}
		/**
		 * a method for sending the specified message to the game server. 
		 * This method should be called whenever the client wants to communicate with the game server or other clients.
		 */
		public void sendMessage(GameMessage message){
			try {
				oos.writeObject(message);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		
		//inner class:
		/**
		 * an inner class that implements the Runnable interface. 
		 * It implements the run() method from the Runnable interface and create a thread with an instance 
		 * of this class as its job in the makeConnection() method from the NetworkGame interface for receiving 
		 * messages from the game server. Upon receiving a message, the parseMessage() method from the 
		 * NetworkGame interface should be called to parse the messages accordingly.
		 */
		public class ServerHandler implements Runnable{
			public void run() {
				CardGameMessage message;
				try {
					ois = new ObjectInputStream(sock.getInputStream());
					while (ois!= null&&(message = (CardGameMessage) ois.readObject()) != null) {
						parseMessage(message);
					}
				}catch (Exception ex) {
					table.enableConnect();
					table.printMsg("Lost Connection!\nPlease reconnect to the server!!!\n");
					try {
						sock.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
					oos = null;
					ois = null;
					serverIP = null;
					playerName = null;
					ex.printStackTrace();
					gameInProgress = false;
					table.resetSelected();
					resetAllLists();
					for(int i=0;i<4;i++){
						playerList.get(i).setName(null);
					}
					table.disable();
					table.repaint();
				}
			}
		} 
		
		//public static methods:
		/**
		 * a method for creating an instance of BigTwoClient.
		 * @param args
		 */
		public static void main(String[] args){
			BigTwoClient client = new BigTwoClient();
		}
		/**
		 * a method for returning a valid hand from the specified list of cards of the player. 
		 * Returns null is no valid hand can be composed from the specified list of cards.
		 * @param player
		 * @param cards
		 * @return
		 */
		public static Hand composeHand(CardGamePlayer player, CardList cards){
			Hand hand;
			hand = new StraightFlush(player,cards);
			if(hand.isValid())
				return hand;
			hand = new Single(player,cards);
			if(hand.isValid())
				return hand;
			hand = new Pair(player,cards);
			if(hand.isValid())
				return hand;
			hand = new Triple(player,cards);
			if(hand.isValid())
				return hand;
			hand = new Straight(player,cards);
			if(hand.isValid())
				return hand;
			hand = new Flush(player,cards);
			if(hand.isValid())
				return hand;
			hand = new FullHouse(player,cards);
			if(hand.isValid())
				return hand;
			hand = new Quad(player,cards);
			if(hand.isValid())
				return hand;
			return null;
		
		}
		
}
