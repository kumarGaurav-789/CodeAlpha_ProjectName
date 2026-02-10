import java.util.*;

class Stock {
    String symbol;
    double price;

    Stock(String symbol, double price) {
        this.symbol = symbol;
        this.price = price;
    }
}

class Transaction {
    String type;
    String symbol;
    int quantity;
    double price;

    Transaction(String type, String symbol, int quantity, double price) {
        this.type = type;
        this.symbol = symbol;
        this.quantity = quantity;
        this.price = price;
    }
}

class User {
    String name;
    double balance;
    Map<String, Integer> portfolio = new HashMap<>();
    List<Transaction> history = new ArrayList<>();

    User(String name, double balance) {
        this.name = name;
        this.balance = balance;
    }

    void buyStock(Stock stock, int quantity) {
        double cost = stock.price * quantity;
        if (quantity <= 0) {
            System.out.println("Quantity must be positive.");
            return;
        }
        if (balance >= cost) {
            balance -= cost;
            portfolio.put(stock.symbol, portfolio.getOrDefault(stock.symbol, 0) + quantity);
            history.add(new Transaction("BUY", stock.symbol, quantity, stock.price));
            System.out.println("Stock purchased successfully.");
        } else {
            System.out.println("Insufficient balance.");
        }
    }

    void sellStock(Stock stock, int quantity) {
        int owned = portfolio.getOrDefault(stock.symbol, 0);
        if (quantity <= 0) {
            System.out.println("Quantity must be positive.");
            return;
        }
        if (owned >= quantity) {
            balance += stock.price * quantity;
            portfolio.put(stock.symbol, owned - quantity);
            history.add(new Transaction("SELL", stock.symbol, quantity, stock.price));
            System.out.println("Stock sold successfully.");
        } else {
            System.out.println("Not enough shares to sell.");
        }
    }

    void viewPortfolio(Map<String, Stock> market) {
        System.out.println("\n--- Portfolio Summary ---");
        double totalValue = balance;

        if (portfolio.isEmpty()) {
            System.out.println("No stocks owned.");
        } else {
            for (String sym : portfolio.keySet()) {
                int qty = portfolio.get(sym);
                double value = qty * market.get(sym).price;
                totalValue += value;
                System.out.println(sym + " : " + qty + " shares | Value: ₹" + value);
            }
        }

        System.out.println("Cash Balance: ₹" + balance);
        System.out.println("Total Portfolio Value: ₹" + totalValue);
    }

    void viewTransactions() {
        System.out.println("\n--- Transaction History ---");
        if (history.isEmpty()) {
            System.out.println("No transactions recorded.");
            return;
        }
        for (Transaction t : history) {
            System.out.println(t.type + " | " + t.symbol + " | Qty: " + t.quantity + " | Price: ₹" + t.price);
        }
    }
}

public class StockTradingPlatform {

    static int getIntInput(Scanner sc) {
        while (!sc.hasNextInt()) {
            System.out.println("Please enter a valid number.");
            sc.next();
        }
        return sc.nextInt();
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        Map<String, Stock> market = new HashMap<>();
        market.put("ABC", new Stock("ABC", 100));
        market.put("XYZ", new Stock("XYZ", 250));
        market.put("DEF", new Stock("DEF", 180));

        User user = new User("Trader", 5000);

        while (true) {
            System.out.println("\n===== Stock Trading Platform =====");
            System.out.println("1. View Market Data");
            System.out.println("2. Buy Stock");
            System.out.println("3. Sell Stock");
            System.out.println("4. View Portfolio");
            System.out.println("5. View Transaction History");
            System.out.println("6. Exit");
            System.out.print("Enter your choice: ");

            int choice = getIntInput(sc);

            switch (choice) {
                case 1:
                    System.out.println("\n--- Market Data ---");
                    for (Stock s : market.values()) {
                        System.out.println(s.symbol + " : ₹" + s.price);
                    }
                    break;

                case 2:
                    System.out.print("Enter stock symbol: ");
                    String buySym = sc.next().toUpperCase();

                    if (!market.containsKey(buySym)) {
                        System.out.println("Invalid stock symbol.");
                        break;
                    }

                    System.out.print("Enter quantity: ");
                    int buyQty = getIntInput(sc);
                    user.buyStock(market.get(buySym), buyQty);
                    break;

                case 3:
                    System.out.print("Enter stock symbol: ");
                    String sellSym = sc.next().toUpperCase();

                    if (!market.containsKey(sellSym)) {
                        System.out.println("Invalid stock symbol.");
                        break;
                    }

                    System.out.print("Enter quantity: ");
                    int sellQty = getIntInput(sc);
                    user.sellStock(market.get(sellSym), sellQty);
                    break;

                case 4:
                    user.viewPortfolio(market);
                    break;

                case 5:
                    user.viewTransactions();
                    break;

                case 6:
                    System.out.println("Thank you for trading!");
                    sc.close();
                    return;

                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }
}
