# Модуль 11: Практикалық жұмыс - Дизайн үлгілері

Бұл құжатта Университетті басқару жүйесінің кластар диаграммасы және Онлайн-банкинг жүйесінің компоненттер диаграммасы біріктірілген.

---

## 1. Университетті басқару жүйесі (Class Diagram)

[cite_start]**Мақсаты:** Университет құрылымын, нысандар арасындағы байланыстарды және мұрагерлікті көрсету[cite: 4, 5].

```mermaid
classDiagram
    [cite_start]%% Мұрагерлік (Inheritance) [cite: 28, 40, 43]
    class User {
        +String name
        +String email
        +login()
        +logout()
    }
    class Student {
        +String studentId
        +List courses
        +registerForCourse() [cite: 42]
        +viewGrades() [cite: 42]
    }
    class Professor {
        +String position
        +List courses
        +conductLesson() [cite: 45]
        +setGrade() [cite: 45]
    }
    class Admin {
        +manageStructures() [cite: 21]
        +manageUsers() [cite: 21]
    }

    User <|-- Student
    User <|-- Professor
    User <|-- Admin

    %% Құрылымдық кластар [cite: 7, 31-39]
    class University {
        +String title
        +String address
        +addFaculty()
    }
    class Faculty {
        +String name
        +addDepartment()
    }
    class Department {
        +String name
        +addProfessor()
        +addCourse()
    }

    University "1" *-- "many" Faculty : композиция [cite: 56]
    Faculty "1" *-- "many" Department : композиция [cite: 57]
    Department "1" o-- "many" Professor : агрегация [cite: 58]

    %% Оқу процесі [cite: 46-54]
    class Course {
        +String title
        +addStudent()
    }
    class Schedule {
        +Date date
        +Time time
        +String room
    }
    class Exam {
        +Date date
        +assignExam()
    }
    class Grade {
        +int value
    }

    %% Байланыстар [cite: 59-62]
    Department "1" *-- "many" Course : композиция
    Course "many" -- "many" Professor : жүргізеді
    Course "many" -- "many" Student : тіркеледі
    Schedule "1" -- "1" Course : кесте
    Exam "1" -- "1" Course : емтихан
    Exam "1" -- "many" Grade : бағалар
    Student "1" -- "many" Grade : иеленеді

    %% Қосымша тапсырма [cite: 68-70]
    class Scholarship {
        +double amount
    }
    Student "1" -- "0..1" Scholarship : алады
componentDiagram
    %% Негізгі компоненттер [cite: 83-91]
    component [Frontend (Web/Mobile)] as UI <<Component>>
    component [Backend Server] as BE <<Component>>
    database "Database" as DB <<Storage>>
    component [Auth Service] as Auth <<Security>>
    component [Payment Gateway] as Pay <<External API>>
    component [Notification System] as SMS <<Service>>

    %% Интерфейстер мен байланыстар [cite: 93-99]
    UI --( REST_API : "HTTPS/JSON" [cite: 105]
    REST_API -- BE
    
    BE --( SQL_Interface : "CRUD" [cite: 119]
    SQL_Interface -- DB
    
    BE --( Token_Auth : "JWT/OAuth" [cite: 125]
    Token_Auth -- Auth
    
    BE --( External_API : "Payment Protocol" [cite: 122]
    External_API -- Pay
    
    BE ..> SMS : "Trigger notifications" [cite: 97]
    
    note bottom of BE
      Backend логиканы өңдеуге, 
      маршрутизацияға жауапты [cite: 117]
    end note
