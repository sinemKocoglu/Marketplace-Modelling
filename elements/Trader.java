package elements;

public class Trader {
	/**
	 * id Trader ID
	 * wallet Wallet of trader
	 */
	private int id;
	private Wallet wallet;
	
	public Trader(double dollars, double coins) {
		this.wallet = new Wallet(dollars,coins);
	}
	/**
	 * trader's sell action
	 * @param amount amount of coin to be sold
	 * @param price  price of coin 
	 * @param market  
	 * @return 0  return is not important.
	 */
	public int sell(double amount, double price, Market market) {
		market.giveSellOrder(new SellingOrder(id, amount, price));
		wallet.subtract("c", amount);
		wallet.add("bc", amount);
		return 0;
		
	}
	/**
	 * trader's buy action
	 * @param amount amount of coin to buy
	 * @param price  price of coin
	 * @param market
	 * @return 0  return is not important.
	 */
	public int buy(double amount, double price, Market market) {
		market.giveBuyOrder(new BuyingOrder(id, amount, price));
		wallet.subtract("d", amount*price);
		wallet.add("bd", amount*price);
		return 0;
		
	}
	/**
	 * number of traders
	 */
	public static int numberOfUsers = 0;

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}
	
	/**
	 * @return the wallet
	 */
	public Wallet getWallet() {
		return wallet;
	}
	
	/**
	 * Controls if the wallet of the trader has enough assets for the action
	 * @param s determines action 
	 * @param amount  amount of coins
	 * @param price  price of coin
	 * @return true if conditions for the action satisfied
	 */
	public boolean control(String s, double amount, double price) {
		if(s.equals("Sell")){
			if(wallet.getCoins()>=amount) {
				return true;
			}
			else {
				return false;
			}
		}
		else if(s.equals("Buy")) {
			if(wallet.getDollars()>=amount*price) {
				return true;
			}
			else {
				return false;
			}
		}
		else {
			return false;
		}
	}
}
