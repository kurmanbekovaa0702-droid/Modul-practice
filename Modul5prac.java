import java.io.*;
import java.time.LocalDateTime;
import java.util.*;

// ==========================================
// 1. SINGLETON: Logger & LogReader
// ==========================================
enum LogLevel {
    INFO(1), WARNING(2), ERROR(3);
    private final int priority;
    LogLevel(int priority) { this.priority = priority; }
    public int getPriority() { return priority; }
}

class Logger {
    private static volatile Logger instance;
    private LogLevel currentLevel;
    private String logFilePath;

    private Logger() {
        this.currentLevel = LogLevel.INFO;
        this.logFilePath = "practice_log.txt";
    }

    public static Logger getInstance() {
        if (instance == null) {
            synchronized (Logger.class) {
                if (instance == null) {
                    instance = new Logger();
                }
            }
        }
        return instance;
    }

    public void setLogLevel(LogLevel level) {
        this.currentLevel = level;
    }

    public synchronized void log(String message, LogLevel level) {
        if (level.getPriority() >= currentLevel.getPriority()) {
            try (PrintWriter out = new PrintWriter(new FileWriter(logFilePath, true))) {
                out.println(level + " [" + LocalDateTime.now() + "]: " + message);
            } catch (IOException e) {
                System.err.println("Ошибка записи лога: " + e.getMessage());
            }
        }
    }
}

class LogReader {
    public static void readLogs(String filePath, LogLevel filterLevel) {
        System.out.println("--- Чтение логов (Фильтр: " + filterLevel + ") ---");
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith(filterLevel.name())) {
                    System.out.println(line);
                }
            }
        } catch (IOException e) {
            System.out.println("Файл логов пока пуст или не найден.");
        }
    }
}

// ==========================================
// 2. BUILDER: Система отчетов
// ==========================================
class ReportStyle {
    public String bgColor, fontColor, fontSize;
    public ReportStyle(String bg, String fc, String fs) {
        this.bgColor = bg; this.fontColor = fc; this.fontSize = fs;
    }
}

class Report {
    public String header, content, footer;
    public Map<String, String> sections = new LinkedHashMap<>();
    public ReportStyle style;

    public void export(String format) {
        System.out.println("\nЭкспорт отчета в формат: " + format);
        System.out.println("Стиль: Фон=" + style.bgColor + ", Шрифт=" + style.fontColor);
        System.out.println("Заголовок: " + header);
        System.out.println("Контент: " + content);
        sections.forEach((k, v) -> System.out.println("Секция [" + k + "]: " + v));
        System.out.println("Подвал: " + footer);
    }
}

interface IReportBuilder {
    void setHeader(String header);
    void setContent(String content);
    void setFooter(String footer);
    void addSection(String sectionName, String sectionContent);
    void setStyle(ReportStyle style);
    Report getReport();
}

class TextReportBuilder implements IReportBuilder {
    private Report report = new Report();
    public void setHeader(String h) { report.header = "=== " + h + " ==="; }
    public void setContent(String c) { report.content = c; }
    public void setFooter(String f) { report.footer = "--- " + f + " ---"; }
    public void addSection(String n, String c) { report.sections.put(n, c); }
    public void setStyle(ReportStyle s) { report.style = s; }
    public Report getReport() { return report; }
}

class HtmlReportBuilder implements IReportBuilder {
    private Report report = new Report();
    public void setHeader(String h) { report.header = "<h1>" + h + "</h1>"; }
    public void setContent(String c) { report.content = "<p>" + c + "</p>"; }
    public void setFooter(String f) { report.footer = "<footer>" + f + "</footer>"; }
    public void addSection(String n, String c) { report.sections.put(n, "<div><h2>" + n + "</h2><p>" + c + "</p></div>"); }
    public void setStyle(ReportStyle s) { report.style = s; }
    public Report getReport() { return report; }
}

class ReportDirector {
    public void constructReport(IReportBuilder builder, ReportStyle style) {
        builder.setStyle(style);
        builder.setHeader("Ежемесячный отчет");
        builder.setContent("Основная информация за текущий месяц.");
        builder.addSection("Графики", "Данные графиков...");
        builder.setFooter("Сгенерировано автоматически");
    }
}

// ==========================================
// 3. PROTOTYPE: Клонирование персонажей
// ==========================================
class Weapon implements Cloneable {
    public String name;
    public int damage;
    public Weapon(String name, int damage) { this.name = name; this.damage = damage; }
    @Override protected Weapon clone() {
        try { return (Weapon) super.clone(); }
        catch (CloneNotSupportedException e) { return null; }
    }
}

class Armor implements Cloneable {
    public String name;
    public int defense;
    public Armor(String name, int defense) { this.name = name; this.defense = defense; }
    @Override protected Armor clone() {
        try { return (Armor) super.clone(); }
        catch (CloneNotSupportedException e) { return null; }
    }
}

class Skill implements Cloneable {
    public String name;
    public Skill(String name) { this.name = name; }
    @Override protected Skill clone() {
        try { return (Skill) super.clone(); }
        catch (CloneNotSupportedException e) { return null; }
    }
}

class Character implements Cloneable {
    public String name;
    public int hp, strength, agility, intel;
    public Weapon weapon;
    public Armor armor;
    public List<Skill> skills = new ArrayList<>();

    public Character(String name) { this.name = name; }

    @Override
    public Character clone() {
        try {
            Character cloned = (Character) super.clone();
            cloned.weapon = this.weapon != null ? this.weapon.clone() : null;
            cloned.armor = this.armor != null ? this.armor.clone() : null;
            cloned.skills = new ArrayList<>();
            for (Skill s : this.skills) cloned.skills.add(s.clone());
            return cloned;
        } catch (CloneNotSupportedException e) { return null; }
    }

    public void printInfo() {
        System.out.println("Персонаж: " + name + " | HP: " + hp + " | Оружие: " + weapon.name + " | Навыков: " + skills.size());
    }
}

// ==========================================
// ОСНОВНОЙ КЛАСС ДЛЯ ЗАПУСКА ПРАКТИКИ
// ==========================================
public class Modul5prac {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== ТЕСТ SINGLETON (LOGGER) ===");
        Logger logger = Logger.getInstance();
        logger.setLogLevel(LogLevel.WARNING);

        Thread t1 = new Thread(() -> Logger.getInstance().log("Ошибка в потоке 1", LogLevel.ERROR));
        Thread t2 = new Thread(() -> Logger.getInstance().log("Инфо в потоке 2", LogLevel.INFO)); // Не запишется
        t1.start(); t2.start();
        t1.join(); t2.join();
        LogReader.readLogs("practice_log.txt", LogLevel.ERROR);

        System.out.println("\n=== ТЕСТ BUILDER (REPORT) ===");
        ReportDirector director = new ReportDirector();
        ReportStyle style = new ReportStyle("Белый", "Черный", "12px");

        IReportBuilder htmlBuilder = new HtmlReportBuilder();
        director.constructReport(htmlBuilder, style);
        htmlBuilder.getReport().export("HTML");

        System.out.println("\n=== ТЕСТ PROTOTYPE (CHARACTER) ===");
        Character baseHero = new Character("Рыцарь");
        baseHero.hp = 100;
        baseHero.weapon = new Weapon("Меч", 15);
        baseHero.armor = new Armor("Щит", 10);
        baseHero.skills.add(new Skill("Удар щитом"));

        Character clonedHero = baseHero.clone();
        clonedHero.name = "Клонированный Рыцарь";
        clonedHero.weapon.name = "Отравленный Меч"; // Проверка глубокого копирования

        baseHero.printInfo();
        clonedHero.printInfo();
    }
}