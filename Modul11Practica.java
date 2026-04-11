# Модуль 11: Практикалық жұмыс - Дизайн үлгілері

Бұл құжатта Университетті басқару жүйесінің кластар диаграммасы және Онлайн-банкинг жүйесінің компоненттер диаграммасы орындалған.

---

## 1. Университетті басқару жүйесі (Class Diagram)

**Мақсаты:** Университет құрылымын, нысандар арасындағы байланыстарды және мұрагерлікті көрсету.

```mermaid
classDiagram
    %% Мұрагерлік (Inheritance)
    class User {
        +String name
        +String email
        +login()
        +logout()
    }
    class Student {
        +String studentId
        +List courses
        +registerForCourse()
        +viewGrades()
    }
    class Professor {
        +String position
        +List courses
        +conductLesson()
        +setGrade()
    }
    class Admin {
        +manageStructures()
        +manageUsers()
    }

    User <|-- Student
    User <|-- Professor
    User <|-- Admin

    %% Құрылымдық кластар
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

    University "1" *-- "many" Faculty : композиция
    Faculty "1" *-- "many" Department : композиция
    Department "1" o-- "many" Professor : агрегация

    %% Оқу процесі
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

    %% Байланыстар
    Department "1" *-- "many" Course : композиция
    Course "many" -- "many" Professor : жүргізеді
    Course "many" -- "many" Student : тіркеледі
    Schedule "1" -- "1" Course : кесте
    Exam "1" -- "1" Course : емтихан
    Exam "1" -- "many" Grade : бағалар
    Student "1" -- "many" Grade : иеленеді

    %% Қосымша тапсырма
    class Scholarship {
        +double amount
    }
    Student "1" -- "0..1" Scholarship : алады
componentDiagram
    %% Негізгі компоненттер
    component [Frontend (Web/Mobile)] as UI <<Component>>
    component [Backend Server] as BE <<Component>>
    database "Database" as DB <<Storage>>
    component [Auth Service] as Auth <<Security>>
    component [Payment Gateway] as Pay <<External API>>
    component [Notification System] as SMS <<Service>>

    %% Интерфейстер мен байланыстар
    UI --( REST_API : "HTTPS/JSON"
    REST_API -- BE
    
    BE --( SQL_Interface : "CRUD"
    SQL_Interface -- DB
    
    BE --( Token_Auth : "JWT/OAuth"
    Token_Auth -- Auth
    
    BE --( External_API : "Payment Protocol"
    External_API -- Pay
    
    BE ..> SMS : "Trigger notifications"
    
    note bottom of BE
      Backend логиканы өңдеуге, 
      маршрутизацияға жауапты
    end note
