import java.util.ArrayList;
import java.util.List;

// ============================================================================
// 1-БӨЛІМ: ФАСАД ПАТТЕРНІ (Қонақ үйді басқару жүйесі) [cite: 61, 67]
// ============================================================================

// Ішкі жүйелер (Subsystems) [cite: 70]
class RoomBookingSystem {
    void bookRoom(String type) { System.out.println("RoomBooking: " + type + " бөлмесі брондалды. [cite: 71]"); }
    void checkAvailability() { System.out.println("RoomBooking: Бос бөлмелер тексерілуде... [cite: 71]"); }
    void cancelBooking() { System.out.println("RoomBooking: Брондау тоқтатылды. [cite: 71]"); }
}

class RestaurantSystem {
    void reserveTable() { System.out.println("Restaurant: Үстел брондалды. [cite: 72]"); }
    void orderFood(String dish) { System.out.println("Restaurant: Тағамға тапсырыс берілді: " + dish + " [cite: 72]"); }
    void callTaxi() { System.out.println("Restaurant: Такси шақырылды. [cite: 79]"); }
}

class EventManagementSystem {
    void bookHall() { System.out.println("Events: Конференц-зал брондалды. [cite: 73]"); }
    void orderEquipment(String equip) { System.out.println("Events: Жабдыққа тапсырыс берілді: " + equip + " [cite: 73]"); }
}

class CleaningService {
    void scheduleCleaning() { System.out.println("Cleaning: Тазалау кестесі құрылды. [cite: 74]"); }
    void performCleaning() { System.out.println("Cleaning: Тазалау жұмыстары жүріп жатыр... [cite: 74]"); }
}

// Фасад класы [cite: 75, 85]
class HotelFacade {
    private RoomBookingSystem rooms = new RoomBookingSystem();
    private RestaurantSystem restaurant = new RestaurantSystem();
    private EventManagementSystem events = new EventManagementSystem();
    private CleaningService cleaning = new CleaningService();

    // Бөлме + Тағам + Тазалық [cite: 77, 87]
    public void bookFullService(String roomType, String dish) {
        System.out.println("\n--- Сценарий: Бөлме және қызметтерді брондау ---");
        rooms.bookRoom(roomType);
        restaurant.orderFood(dish);
        cleaning.scheduleCleaning();
    }

    // Іс-шара + Жабдық + Бөлмелер [cite: 78, 88]
    public void organizeEventWithRooms(String equipment, int roomCount) {
        System.out.println("\n--- Сценарий: Іс-шара ұйымдастыру ---");
        events.bookHall();
        events.orderEquipment(equipment);
        for(int i=1; i<=roomCount; i++) rooms.bookRoom("Стандарт (" + i + ")");
    }

    // Ресторан + Такси [cite: 79, 89]
    public void bookTableAndTaxi() {
        System.out.println("\n--- Сценарий: Ресторан және такси ---");
        restaurant.reserveTable();
        restaurant.callTaxi();
    }

    // Бронды тоқтату және сұраныс бойынша тазалау [cite: 90]
    public void cancelAllAndClean() {
        System.out.println("\n--- Сценарий: Бәрін тоқтату және тазалау ---");
        rooms.cancelBooking();
        cleaning.performCleaning();
    }
}

// ============================================================================
// 2-БӨЛІМ: КОМПОНОВЩИК ПАТТЕРНІ (Корпоративтік құрылым) [cite: 92, 95]
// ============================================================================

// Ортақ компонент [cite: 102]
abstract class OrganizationComponent {
    protected String name;
    public OrganizationComponent(String name) { this.name = name; }
    public abstract void display(String indent);
    public abstract double getBudget(); // [cite: 99]
    public abstract int getEmployeeCount(); // [cite: 100]
    public abstract OrganizationComponent findEmployee(String name); // [cite: 109]
}

// Қызметкер класы [cite: 103]
class Employee extends OrganizationComponent {
    private String position;
    private double salary;

    public Employee(String name, String position, double salary) {
        super(name);
        this.position = position;
        this.salary = salary;
    }

    public void setSalary(double newSalary) { this.salary = newSalary; } // [cite: 108]

    @Override
    public void display(String indent) {
        System.out.println(indent + "👤 Қызметкер: " + name + " [" + position + "], Жалақы: " + salary);
    }

    @Override
    public double getBudget() { return salary; }

    @Override
    public int getEmployeeCount() { return 1; }

    @Override
    public OrganizationComponent findEmployee(String empName) {
        return this.name.equalsIgnoreCase(empName) ? this : null;
    }
}

// Контрактор (Бюджетке кірмейді) [cite: 110]
class Contractor extends Employee {
    public Contractor(String name, String position, double fixedPay) {
        super(name, position, fixedPay);
    }
    @Override
    public double getBudget() { return 0; } // Жалақы бюджетке қосылмайды [cite: 110]
}

// Бөлім класы (Композит) [cite: 104]
class Department extends OrganizationComponent {
    private List<OrganizationComponent> components = new ArrayList<>();

    public Department(String name) { super(name); }

    public void add(OrganizationComponent comp) { components.add(comp); } // [cite: 97]

    @Override
    public void display(String indent) {
        System.out.println(indent + "📂 Бөлім: " + name);
        for (OrganizationComponent comp : components) comp.display(indent + "   "); // [cite: 98, 111]
    }

    @Override
    public double getBudget() {
        double total = 0;
        for (OrganizationComponent comp : components) total += comp.getBudget(); // [cite: 99, 105]
        return total;
    }

    @Override
    public int getEmployeeCount() {
        int count = 0;
        for (OrganizationComponent comp : components) count += comp.getEmployeeCount(); // [cite: 100, 105]
        return count;
    }

    @Override
    public OrganizationComponent findEmployee(String empName) {
        for (OrganizationComponent comp : components) {
            OrganizationComponent result = comp.findEmployee(empName);
            if (result != null) return result;
        }
        return null;
    }
}

// ============================================================================
// НЕГІЗГІ КЛАСС [cite: 86, 106]
// ============================================================================
public class Modul9Pract {
    public static void main(String[] args) {
        
        // 1. ФАСАД ТЕСТІЛЕУ [cite: 86]
        System.out.println("========== 1-БӨЛІМ: ФАСАД (ОТЕЛЬ) ==========");
        HotelFacade hotel = new HotelFacade();
        hotel.bookFullService("Люкс", "Паста");
        hotel.organizeEventWithRooms("Проектор", 2);
        hotel.bookTableAndTaxi();
        hotel.cancelAllAndClean();

        System.out.println("\n" + "=".repeat(50) + "\n");

        // 2. КОМПОНОВЩИК ТЕСТІЛЕУ [cite: 106]
        System.out.println("========== 2-БӨЛІМ: КОМПОНОВЩИК (КОРПОРАЦИЯ) ==========");
        Department company = new Department("Tech Solutions");
        Department devDept = new Department("IT Бөлімі");
        
        Employee dev1 = new Employee("Асхат", "Senior Developer", 200000);
        Employee dev2 = new Employee("Әлия", "Junior Developer", 100000);
        Contractor contractor = new Contractor("Дархан", "Security Specialist", 50000);

        company.add(devDept);
        devDept.add(dev1);
        devDept.add(dev2);
        devDept.add(contractor);

        company.display(""); // [cite: 98]
        System.out.println("\nЖалпы бюджет (Контракторсыз): " + company.getBudget()); // [cite: 105]
        System.out.println("Жалпы қызметкерлер саны: " + company.getEmployeeCount()); // [cite: 105]

        // Іздеу және жалақыны өзгерту [cite: 108, 109]
        OrganizationComponent found = company.findEmployee("Асхат");
        if (found instanceof Employee) {
            System.out.println("\n🔍 Табылды: " + found.name + ". Жалақыны көтеру...");
            ((Employee) found).setSalary(250000);
            System.out.println("Жаңа жалпы бюджет: " + company.getBudget());
        }
    }
}
