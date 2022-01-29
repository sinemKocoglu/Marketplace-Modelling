package executable;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import elements.Market;
import elements.Trader;

public class Main {
	public static Random myRandom;
	public static void main(String[] args) throws FileNotFoundException {
		Scanner in = new Scanner(new File(args[0]));
		PrintStream out = new PrintStream(new File(args[1]));
		
		/*The first line of the input file specifies the random seed that you have to use to
		generate random numbers in the project.: A*/
		int A = Integer.parseInt(in.nextLine());
		myRandom = new Random(A);
		/*The second line of the input file specifies the initial transaction fee of the
		marketplace, the number of users ,and number of queries: B C D*/
		String[] line2 = in.nextLine().split(" ");
		int B = Integer.parseInt(line2[0]);
		Market market = new Market(B);
		int C = Integer.parseInt(line2[1]);
		Trader.numberOfUsers = C;
		int D = Integer.parseInt(line2[2]);
		
		/*The next C lines represent the initial dollars and PQoins asset in each trader’s wallet.
		<dollar_amount> <PQoin_amount>
		…
		<dollar_amount> <PQoin_amount>*/
		ArrayList<Trader> traders = new ArrayList<Trader>();
		int invalidQuery = 0;
		for(int i=0;i<C;i++) {  
			String[] lineInCpart = in.nextLine().split(" ");
			Trader t = new Trader(Double.parseDouble(lineInCpart[0]),Double.parseDouble(lineInCpart[1]));
			traders.add(t);
			t.setId(i);
		}
		
		/*The next D lines are the queries. There are exactly 14 types of event*/
		for(int j=0;j<D;j++) {
			String[] lineInD = in.nextLine().split(" ");
			String eventType = lineInD[0];
			//Trader queries
			if(eventType.equals("10")) {
//		     give buying order of specific price:
//			 10 <trader_id> <price> <amount>
				if(traders.get(Integer.parseInt(lineInD[1])).control("Buy",Double.parseDouble(lineInD[3]), Double.parseDouble(lineInD[2]))){
					traders.get(Integer.parseInt(lineInD[1])).buy(Double.parseDouble(lineInD[3]), Double.parseDouble(lineInD[2]), market);
				}
				else {
					invalidQuery++;
				}
				market.checkTransactions(traders);
			}
			
			else if(eventType.equals("11")) {
//			 give buying order of market price:
//			 11 <trader_id> <amount>
//			 Note: This is similar to Query#10, but it takes the current selling price as
//			 price.
//			 Note: If there is no current selling price then increment the number of invalid
//			 queries.
				if(!market.getSellingOrders().isEmpty()) {
					if(traders.get(Integer.parseInt(lineInD[1])).control("Buy",Double.parseDouble(lineInD[2]), market.getSellingOrders().peek().getPrice())){
						if(market.getSellingOrders().peek().getPrice() != 0) {
							traders.get(Integer.parseInt(lineInD[1])).sell(Double.parseDouble(lineInD[2]), market.getSellingOrders().peek().getPrice(), market);
						}
						else {
							invalidQuery++;
						}
					}
				}
				else {
					invalidQuery++;
				}
				market.checkTransactions(traders);
			}
			
			else if(eventType.equals("20")) {
//			 give selling order of specific price:
//			 20 <trader_id> <price> <amount>
				if(traders.get(Integer.parseInt(lineInD[1])).control("Sell",Double.parseDouble(lineInD[3]), Double.parseDouble(lineInD[2]))){
					traders.get(Integer.parseInt(lineInD[1])).sell(Double.parseDouble(lineInD[3]), Double.parseDouble(lineInD[2]), market);
				}
				else {
					invalidQuery++;
				}
				market.checkTransactions(traders);
			}
			
			else if(eventType.equals("21")) {
//		    give selling order of market price:
//			21 <trader_id> <amount>
//			Note: This is similar to Query#20, but it takes the current buying price as
//			price.
				if(!market.getBuyingOrders().isEmpty()) {
					if(traders.get(Integer.parseInt(lineInD[1])).control("Sell",Double.parseDouble(lineInD[2]), market.getBuyingOrders().peek().getPrice())){
						if(market.getBuyingOrders().peek().getPrice() != 0) {
							traders.get(Integer.parseInt(lineInD[1])).sell(Double.parseDouble(lineInD[2]), market.getBuyingOrders().peek().getPrice(), market);
						}
						else {
							invalidQuery++;
						}
					}
				}
				else {
					invalidQuery++;
				}
				
				market.checkTransactions(traders);
			}
			
			else if(eventType.equals("3")) {
//			deposit a certain amount of dollars to wallet:
//			3 <trader_id> <amount>
				traders.get(Integer.parseInt(lineInD[1])).getWallet().add("d", Double.parseDouble(lineInD[2]));
			}
			
			else if(eventType.equals("4")) {
//			withdraw a certain amount of dollars from wallet:
//			4 <trader_id> <amount>
				if(traders.get(Integer.parseInt(lineInD[1])).getWallet().getDollars()>= Double.parseDouble(lineInD[2])) {
					traders.get(Integer.parseInt(lineInD[1])).getWallet().subtract("d", Double.parseDouble(lineInD[2]));
				}
				else {
					invalidQuery++;
				}
			}
			
			else if(eventType.equals("5")) {
//			print wallet status:
//			5 <trader_id>
//			Prints “Trader <traderID>: <trader_s_dollars>$ <trader_s_PQoins>PQ”
//			Example: Trader 5: 34.0$ 23.0PQ
				out.println("Trader " + Integer.parseInt(lineInD[1]) + ": " + traders.get(Integer.parseInt(lineInD[1])).getWallet().total("d") + "$ " + traders.get(Integer.parseInt(lineInD[1])).getWallet().total("c") + "PQ");
				//market.checkTransactions(traders);
			}
			
			//System Queries
			else if(eventType.equals("777")) {
//			give rewards to all traders:
//			When this query is read, the system creates and distributes random amounts of PQoins to all traders.
//			for each trader add myRandom.nextDouble()*10 coins to the trader’s wallet.
				for(int m=0;m<C;m++) { 
					traders.get(m).getWallet().add("c", myRandom.nextDouble()*10); 
				}
			}
			
			else if(eventType.equals("666")) {
//			make open market operation:
//			666 <price>When this query is read, the system compensates buying or selling orders in
//			order to set the price of PQoin to the given price.
//			This query can increase or decrease the total amount of PQoin in the market.
				market.makeOpenMarketOperation(Integer.parseInt(lineInD[1]), traders);
				market.checkTransactions(traders);
			}
			
			else if(eventType.equals("500")) {
//			print the current market size:
//			Prints “Current market size: <total_$_in_buying_pq> <total_PQoin_in_selling_pq>” to the output file.
				out.println("Current market size: " + String.format("%.5f %.5f", market.currentMarketSize("buy"), market.currentMarketSize("sell")));
			}
			
			else if(eventType.equals("501")) {
//			print number of successful transactions:
//			Prints “Number of successful transactions: <num_of_successful_transaction>” to the output file.
				out.println("Number of successful transactions: " + market.getTransactionsSize());
			}
			
			else if(eventType.equals("502")) {
//			print the number of invalid queries:
//			Prints <number_of_invalid_queries> to the output file.
//			Note: If a trader is unable to satisfy the query’s liabilities, then the number of invalid queries is incremented by one.
				out.println("Number of invalid queries: " + invalidQuery);
			}
			
			else if(eventType.equals("505")) {
//			print the current prices:
//			Prints; “Current prices: <cp_buying> <cp_selling> <cp_average> to the output file.
//			Note: if one of the PriorityQueues is empty then the price related to it, is not included in the average current price.
				if(market.getBuyingOrders().isEmpty() && !market.getSellingOrders().isEmpty()) {
					out.println("Current prices: 0.00000 " +String.format("%.5f %.5f", market.getSellingOrders().peek().getPrice(), market.CurrentPrice()));
				}
				else if(!market.getBuyingOrders().isEmpty() && market.getSellingOrders().isEmpty()) {
					out.println("Current prices: " +String.format("%.5f 0.00000 %.5f", market.getBuyingOrders().peek().getPrice(), market.CurrentPrice()));
				}
				else if(market.getBuyingOrders().isEmpty() && market.getSellingOrders().isEmpty()){
					out.println("Current prices: 0.00000 0.00000 0.00000");
				}
				else {
					out.println("Current prices: " +String.format("%.5f %.5f %.5f", market.getBuyingOrders().peek().getPrice(), market.getSellingOrders().peek().getPrice(), market.CurrentPrice()));
				}
			}
			else if(eventType.equals("555")) {
//			print all traders’ wallet status
//			Prints “<trader_s_dollars>$ <trader_s_PQoins>PQ” of all traders
				for(int n=0;n<traders.size();n++) {
					out.println("Trader " + n + String.format(": %.5f$ %.5fPQ",traders.get(n).getWallet().total("d"), traders.get(n).getWallet().total("c")));
				}
			}
		}
		in.close();
		out.close();
	}

}

