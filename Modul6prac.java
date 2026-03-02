import java.util.ArrayList;
import java.util.List;

interface ICostCalculationStrategy {
    double calculateCost(double distance, int passengers);
}

class PlaneStrategy implements ICostCalculationStrategy {
    public double calculateCost(double distance, int passengers) {
        return distance * 10.0 * passengers;
    }
}

class TrainStrategy implements ICostCalculationStrategy {
    public double calculateCost(double distance, int passengers) {
        return distance * 5.0 * passengers;
    }
}

class BusStrategy implements ICostCalculationStrategy {
    public double calculateCost(double distance, int passengers) {
        return distance * 2.0 * passengers;
    }
}

class TravelBookingContext {
    private ICostCalculationStrategy strategy;

    public void setStrategy(ICostCalculationStrategy strategy) {
        this.strategy = strategy;
    }

    public double executeCalculation(double distance, int passengers) {
        return strategy.calculateCost(distance, passengers);
    }
}

interface IObserver {
    void update(String stock, double price);
}

interface ISubject {
    void addObserver(IObserver o);
    void removeObserver(IObserver o);
    void notifyObservers(String stock, double price);
}

class StockExchange implements ISubject {
    private List<IObserver> observers = new ArrayList<>();

    public void setPrice(String stock, double price) {
        notifyObservers(stock, price);
    }

    public void addObserver(IObserver o) {
        observers.add(o);
    }

    public void removeObserver(IObserver o) {
        observers.remove(o);
    }

    public void notifyObservers(String stock, double price) {
        for (IObserver o : observers) {
            o.update(stock, price);
        }
    }
}

class Trader implements IObserver {
    public void update(String stock, double price) {
        System.out.println("Trader: " + stock + " -> " + price);
    }
}

class TradingRobot implements IObserver {
    public void update(String stock, double price) {
        if (price < 100) {
            System.out.println("Robot buys: " + stock);
        }
    }
}

public class Modul6prac {
    public static void main(String[] args) {
        TravelBookingContext context = new TravelBookingContext();
        context.setStrategy(new PlaneStrategy());
        System.out.println(context.executeCalculation(1000, 2));

        StockExchange exchange = new StockExchange();
        exchange.addObserver(new Trader());
        exchange.addObserver(new TradingRobot());
        exchange.setPrice("AAPL", 95.0);
    }
}