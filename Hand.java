
/**
 * The Hand class is a subclass of the CardList class, and is used to model a hand of cards. 
 * It has a private instance variable for storing the player who plays this hand. 
 * It also has methods for getting the player of this hand, checking if it is a valid hand, getting 
 * the type of this hand, getting the top card of this hand, 
 * and checking if it beats a specified hand
 * @author Li Jianxiang
 *
 */
public abstract class Hand extends CardList {
	/**
	 * a constructor for building a hand with the specified player and list of cards.
	 * @param player
	 * @param cards
	 */
	public Hand(CardGamePlayer player, CardList cards){
		this.player = player;
		for (int i = 0; i< cards.size(); i++){
			this.addCard(cards.getCard(i));
		}
	}
	
	private CardGamePlayer player;
	
	/**
	 * a method for retrieving the player of this hand.
	 * @return
	 */
	public CardGamePlayer getPlayer(){return player;}
	/**
	 * a method for retrieving the top card of this hand.
	 * @return
	 */
	public Card getTopCard(){
			Card card = this.getCard(0);
			for (int i = 1; i < this.size(); i++){
				if (this.getCard(i).compareTo(card)>0)
					card = this.getCard(i);
			}
		return card;
	}
	/**
	 * a method for checking if this hand beats a specified hand.
	 * @param hand
	 * @return
	 */
	public boolean beats(Hand hand){
		if (this.getType()==hand.getType()){
			if (this.getTopCard().compareTo(hand.getTopCard())>0)
				return true;
			else return false;
		}
		else if (this.size()==hand.size()){
			if (this.getType()=="StraightFlush")
				return true;
			else if (hand.getType()=="StraightFlush")
				return false;
			else if (this.getType()=="Straight")
				return false;
			else if (hand.getType()=="Straight")
				return true;
			else if (this.getType()=="Flush")
				return false;
			else if (hand.getType()=="Flush")
				return true;
			else if (this.getType()=="FullHouse")
				return false;
			else if (hand.getType()=="FullHouse")
				return true;
		}
		return false;
	}
	
	/**
	 * a method for checking if this is a valid hand.
	 * @return
	 */
	public abstract boolean isValid();
	/**
	 * a method for returning a string specifying the type of this hand.
	 * @return
	 */
	public abstract String getType();
	
	
}
