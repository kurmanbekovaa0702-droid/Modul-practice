graph TD
    %% 1-ші ТАПСЫРМА: ДИЯГРАММА ДЕЯТЕЛЬНОСТИ
    subgraph "Задание №1: Процесс выдачи книг (Activity Diagram)"
        Start1([Начало]) --> CheckCard{Есть билет?}
        
        %% Ветвление: Проверка билета 
        CheckCard -- Нет --> Register[Оформить билет]
        Register --> SelectBook
        CheckCard -- Да --> SelectBook[Читатель выбирает книгу]
        
        SelectBook --> CheckStock{Книга доступна?}
        
        %% Ветвление: Проверка доступности [cite: 14, 15, 16]
        CheckStock -- Нет --> SelectBook
        CheckStock -- Да --> SystemReg[Система регистрирует выдачу]
        
        SystemReg --> GetBook[Читатель получает книгу]
        GetBook --> End1([Конец процесса])
    end

    %% 2-ші ТАПСЫРМА: ДИЯГРАММА ПОСЛЕДОВАТЕЛЬНОСТИ (Логикалық схемасы)
    subgraph "Задание №2: Оформление заказа (Sequence Diagram Logic)"
        User((Пользователь)) -- "1. Вход и выбор товара" --> System[Система]
        System -- "2. Проверка склада" --> Warehouse[Склад]
        Warehouse -- "3. Доступность" --> System
        
        %% Условный блок для оплаты 
        System -- "4. Если онлайн-платеж" --> Gateway[Платежный шлюз]
        Gateway -- "5. Подтверждение" --> System
        
        System -- "6. Уведомление о сборке" --> Warehouse
    end
