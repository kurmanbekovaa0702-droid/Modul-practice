import java.util.*;

// ============================================================================
// ЧАСТЬ 1: СИСТЕМА ОТЧЕТНОСТИ (ПАТТЕРН ДЕКОРАТОР)
// ============================================================================

// Базовый интерфейс для отчетов
interface IReport {
    String generate();
}

// Отчет по продажам (Базовый класс)
class SalesReport implements IReport {
    public String generate() {
        return "Данные отчета по продажам: [Товар А: 500$, Товар Б: 300$]";
    }
}

// Отчет по пользователям (Базовый класс)
class UserReport implements IReport {
    public String generate() {
        return "Данные отчета по пользователям: [Admin: online, Guest: offline]";
    }
}

// Абстрактный декоратор
abstract class ReportDecorator implements IReport {
    protected IReport report;
    public ReportDecorator(IReport report) {
        this.report = report;
    }
    public String generate() {
        return report.generate();
    }
}

// Декоратор: Фильтрация по датам
class DateFilterDecorator extends ReportDecorator {
    public DateFilterDecorator(IReport report) { super(report); }
    @Override
    public String generate() {
        return report.generate() + " | [Фильтр: по датам]";
    }
}

// Декоратор: Сортировка данных
class SortingDecorator extends ReportDecorator {
    public SortingDecorator(IReport report) { super(report); }
    @Override
    public String generate() {
        return report.generate() + " | [Сортировка: по сумме]";
    }
}

// Декоратор: Экспорт в CSV
class CsvExportDecorator extends ReportDecorator {
    public CsvExportDecorator(IReport report) { super(report); }
    @Override
    public String generate() {
        return report.generate() + " -> (Экспорт в CSV)";
    }
}

// Декоратор: Экспорт в PDF
class PdfExportDecorator extends ReportDecorator {
    public PdfExportDecorator(IReport report) { super(report); }
    @Override
    public String generate() {
        return report.generate() + " -> (Экспорт в PDF)";
    }
}

// Доп. задание: Декоратор фильтрации по сумме
class AmountFilterDecorator extends ReportDecorator {
    public AmountFilterDecorator(IReport report) { super(report); }
    @Override
    public String generate() {
        return report.generate() + " | [Фильтр: сумма > 400$]";
    }
}

// ============================================================================
// ЧАСТЬ 2: СИСТЕМА ЛОГИСТИКИ (ПАТТЕРНЫ АДАПТЕР И ФАБРИКА)
// ============================================================================

// Интерфейс внутренней службы доставки
interface IInternalDeliveryService {
    void deliverOrder(String orderId);
    String getDeliveryStatus(String orderId);
    double calculateCost(String orderId); // Доп. задание: расчет стоимости
}

// Внутренняя служба доставки
class InternalDeliveryService implements IInternalDeliveryService {
    public void deliverOrder(String orderId) {
        System.out.println("Внутренняя служба: Доставка заказа " + orderId);
    }
    public String getDeliveryStatus(String orderId) {
        return "Статус заказа " + orderId + ": В пути (Внутренняя)";
    }
    public double calculateCost(String orderId) {
        return 15.0;
    }
}

// Сторонняя служба логистики A
class ExternalLogisticsServiceA {
    public void shipItem(int itemId) {
        System.out.println("Service A: Товар #" + itemId + " отправлен.");
    }
    public String trackShipment(int shipmentId) {
        return "Service A: Груз " + shipmentId + " в аэропорту.";
    }
}

// Сторонняя служба логистики B
class ExternalLogisticsServiceB {
    public void sendPackage(String info) {
        System.out.println("Service B: Посылка ушла (" + info + ")");
    }
    public String checkPackageStatus(String code) {
        return "Service B: Посылка " + code + " доставлена.";
    }
}

// Адаптер для службы A
class LogisticsAdapterA implements IInternalDeliveryService {
    private ExternalLogisticsServiceA serviceA = new ExternalLogisticsServiceA();

    public void deliverOrder(String orderId) {
        serviceA.shipItem(Integer.parseInt(orderId));
    }
    public String getDeliveryStatus(String orderId) {
        return serviceA.trackShipment(Integer.parseInt(orderId));
    }
    public double calculateCost(String orderId) {
        return 25.0;
    }
}

// Адаптер для службы B
class LogisticsAdapterB implements IInternalDeliveryService {
    private ExternalLogisticsServiceB serviceB = new ExternalLogisticsServiceB();

    public void deliverOrder(String orderId) {
        serviceB.sendPackage("Заказ ID:" + orderId);
    }
    public String getDeliveryStatus(String orderId) {
        return serviceB.checkPackageStatus(orderId);
    }
    public double calculateCost(String orderId) {
        return 35.5;
    }
}

// Фабрика для выбора службы доставки
class DeliveryServiceFactory {
    public static IInternalDeliveryService getService(String type) {
        if (type.equalsIgnoreCase("internal")) return new InternalDeliveryService();
        if (type.equalsIgnoreCase("externalA")) return new LogisticsAdapterA();
        if (type.equalsIgnoreCase("externalB")) return new LogisticsAdapterB();
        throw new IllegalArgumentException("Тип службы не поддерживается!");
    }
}

// ============================================================================
// ГЛАВНЫЙ КЛАСС (ДОЛЖЕН НАЗЫВАТЬСЯ Modul8Pract.java)
// ============================================================================
public class Modul8Pract {
    public static void main(String[] args) {
        
        // --- 1. ТЕСТИРОВАНИЕ ДЕКОРАТОРА (ОТЧЕТЫ) ---
        System.out.println("=== СИСТЕМА ОТЧЕТОВ (ДЕКОРАТОР) ===");
        
        IReport myReport = new SalesReport();           // Создаем отчет
        myReport = new DateFilterDecorator(myReport);   // Добавляем дату
        myReport = new AmountFilterDecorator(myReport); // Добавляем фильтр суммы
        myReport = new PdfExportDecorator(myReport);    // Экспортируем в PDF
        
        System.out.println("Результат: " + myReport.generate());
        System.out.println();

        // --- 2. ТЕСТИРОВАНИЕ АДАПТЕРА И ФАБРИКИ (ЛОГИСТИКА) ---
        System.out.println("=== СИСТЕМА ЛОГИСТИКИ (АДАПТЕР И ФАБРИКА) ===");
        
        // Выбираем внешнюю службу B через фабрику
        IInternalDeliveryService delivery = DeliveryServiceFactory.getService("externalB");
        
        delivery.deliverOrder("12345");
        System.out.println(delivery.getDeliveryStatus("12345"));
        System.out.println("Стоимость доставки: $" + delivery.calculateCost("12345"));
    }
}
