package elements;

import java.util.ArrayList;
import java.util.PriorityQueue;

public class Market {
	/**
	 * sellingOrders holds selling orders of traders
	 * buyingOrders holds buying orders of traders
	 * transactions holds transactions between traders
	 * fee the fee that the system takes in every transaction
	 */
	private PriorityQueue<SellingOrder> sellingOrders = new PriorityQueue<SellingOrder>();
	private PriorityQueue<BuyingOrder> buyingOrders = new PriorityQueue<BuyingOrder>();
	private ArrayList<Transaction> transactions = new ArrayList<Transaction>();
	
	int fee;
	
	/**
	 * add order to sellingOrders
	 * @param order
	 */
	public void giveSellOrder(SellingOrder order) {
		sellingOrders.add(order);
	}
	/**
	 * add order to buyingOrders
	 * @param order
	 */
	public void giveBuyOrder(BuyingOrder order) {
		buyingOrders.add(order);
	}
	/**
	 * Trader#0 (The System) gives buying or selling orders for setting the current price of PQoin to the given price
	 * until conditions below are satisfied.
	 * price of buyingOrders.peek()< price given by the system 
	 * price of sellingOrders.peek() > price given by the system
	 * 
	 * @param price  the current price of PQoin will be set to the given price
	 * @param traderlist
	 */
	public void makeOpenMarketOperation(double price,ArrayList<Trader> traderlist) {
		if(!buyingOrders.isEmpty()) {
			while(buyingOrders.peek().getPrice()>= price) {
			sellingOrders.add(new SellingOrder(0, buyingOrders.peek().getAmount(), buyingOrders.peek().getPrice()));
			checkTransactions(traderlist);
			}
		}
		if(!sellingOrders.isEmpty()) {
			while(sellingOrders.peek().getPrice()<=price) {
			buyingOrders.add(new BuyingOrder(0, sellingOrders.peek().getAmount(), sellingOrders.peek().getPrice()));
			checkTransactions(traderlist);
			}
		}		
	}
	
	/**
	 * As long as the price of the top of the buying PQ is greater or equals the price of the top of the selling PQ,
	 * it makes transaction according to the amount comparison.
	 * @param traders
	 */
	public void checkTransactions(ArrayList<Trader> traders) {
		if(!buyingOrders.isEmpty() && !sellingOrders.isEmpty()) {
			while(buyingOrders.peek().getPrice()>=sellingOrders.peek().getPrice()) {
				SellingOrder sold = sellingOrders.peek();
				BuyingOrder bought = buyingOrders.peek();
				transactions.add(new Transaction(sold,bought));  
				if(bought.getPrice()>sold.getPrice()) {    
					if(bought.getAmount() > sold.getAmount()) {
						traders.get(sold.traderID).getWallet().add("d", sold.getAmount()*sold.getPrice()*(1.0-(double)fee/1000.0));
						traders.get(sold.traderID).getWallet().subtract("bc", sold.getAmount());
						traders.get(bought.traderID).getWallet().add("c", sold.getAmount());
						traders.get(bought.traderID).getWallet().subtract("bd", bought.getPrice()*sold.getAmount());
						traders.get(bought.traderID).getWallet().add("d", (bought.getPrice()-sold.getPrice())*sold.getAmount());
						buyingOrders.peek().setAmount(bought.getAmount() - sold.getAmount()); 
						sellingOrders.poll();

					}
					else if(bought.getAmount()<sold.getAmount()) {
						traders.get(sold.traderID).getWallet().add("d", bought.getAmount()*sold.getPrice()*(1.0-(double)fee/1000.0));
						traders.get(sold.traderID).getWallet().subtract("bc", bought.getAmount());
						traders.get(bought.traderID).getWallet().add("c", bought.getAmount());
						traders.get(bought.traderID).getWallet().subtract("bd", bought.getPrice()*bought.getAmount());
						traders.get(bought.traderID).getWallet().add("d", (bought.getPrice()-sold.getPrice())*bought.getAmount());
						sellingOrders.peek().setAmount(sold.getAmount() - bought.getAmount()); 
						buyingOrders.poll();
					}
					else {
						traders.get(sold.traderID).getWallet().add("d", bought.getAmount()*sold.getPrice()*(1.0-(double)fee/1000.0));
						traders.get(sold.traderID).getWallet().subtract("bc", bought.getAmount());
						traders.get(bought.traderID).getWallet().add("c", bought.getAmount());
						traders.get(bought.traderID).getWallet().subtract("bd", bought.getPrice()*bought.getAmount());
						traders.get(bought.traderID).getWallet().add("d", (bought.getPrice()-sold.getPrice())*sold.getAmount());
						sellingOrders.poll();
						buyingOrders.poll();
					}
				}
				else if(bought.getPrice()==sold.getPrice()) {
					if(bought.getAmount() > sold.getAmount()) {
						traders.get(sold.traderID).getWallet().add("d", sold.getAmount()*sold.getPrice()*(1.0-(double)fee/1000.0));
						traders.get(sold.traderID).getWallet().subtract("bc", sold.getAmount());
						traders.get(bought.traderID).getWallet().add("c", sold.getAmount());
						traders.get(bought.traderID).getWallet().subtract("bd", sold.getPrice()*sold.getAmount());
						buyingOrders.peek().setAmount(bought.getAmount() - sold.getAmount()); 
						sellingOrders.poll();
					}
					else if(bought.getAmount()<sold.getAmount()) {
						traders.get(sold.traderID).getWallet().add("d", bought.getAmount()*sold.getPrice()*(1.0-(double)fee/1000.0));
						traders.get(sold.traderID).getWallet().subtract("bc", bought.getAmount());
						traders.get(bought.traderID).getWallet().add("c", bought.getAmount());
						traders.get(bought.traderID).getWallet().subtract("bd", sold.getPrice()*bought.getAmount());
						sellingOrders.peek().setAmount(sold.getAmount() - bought.getAmount()); 
						buyingOrders.poll();
					}
					else {
						traders.get(sold.traderID).getWallet().add("d", bought.getAmount()*sold.getPrice()*(1.0-(double)fee/1000.0));
						traders.get(sold.traderID).getWallet().subtract("bc", bought.getAmount());
						traders.get(bought.traderID).getWallet().add("c", bought.getAmount());
						traders.get(bought.traderID).getWallet().subtract("bd", sold.getPrice()*bought.getAmount());
						sellingOrders.poll();
						buyingOrders.poll();
					}
				}
			}
		}
	}
	/**
	 * constructor of the class
	 * @param fee
	 */
	public Market(int fee) {
		this.fee = fee;
	}
	
	
	/**
	 * @return the sellingOrders
	 */
	public PriorityQueue<SellingOrder> getSellingOrders() {
		return sellingOrders;
	}
	/**
	 * @return the buyingOrders
	 */
	public PriorityQueue<BuyingOrder> getBuyingOrders() {
		return buyingOrders;
	}
	
	/**
	 * @return the size of transactions
	 */
	public int getTransactionsSize() {
		return transactions.size();
	}
	
	/**
	 * According to s, it calculates total amount of coins in sellingOrders or total dollars in buyingOrders.
	 * @param s  determines queue for which the current market size is calculated.
	 * @return total  current market size
	 */
	public double currentMarketSize(String s) {
		double total = 0;
		if(s.equals("sell")) {
			for(SellingOrder sellord: sellingOrders) {
			total += sellord.getAmount();
			}
		}
		else if(s.equals("buy")) {
			for(BuyingOrder buyord: buyingOrders) {
				total += buyord.getPrice()*buyord.getAmount();
			}
		}
		return total;
			
	}
	/**
	 * calculates current price by checking if the queues are empty or prices of top values of queues are 0 
	 * @return current price
	 */
	public double CurrentPrice() {
		if((buyingOrders.peek().getPrice()==0 || buyingOrders.isEmpty()) && !sellingOrders.isEmpty()) {
			return sellingOrders.peek().getPrice();
		}
		else if((sellingOrders.peek().getPrice()==0 || sellingOrders.isEmpty()) && !buyingOrders.isEmpty()) {
			return buyingOrders.peek().getPrice();
		}
		else if(!sellingOrders.isEmpty() && !buyingOrders.isEmpty()) {
			return (sellingOrders.peek().getPrice()+buyingOrders.peek().getPrice())/2.0;
		}
		return 0;
	}
	
}
