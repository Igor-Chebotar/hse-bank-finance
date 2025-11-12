# HSE Bank Finance — модуль учёта финансов

---

# Отчёт по реализации

## A. Общая идея решения
Приложение — консольный модуль учёта личных финансов со следующими возможностями:
- учёт счетов (`BankAccount`) с балансом;
- учёт категорий (`Category`) двух типов: доход и расход;
- фиксация операций (`Operation`) с типами `INCOME`/`EXPENSE`, пересчёт балансов;
- импорта/экспорта данных в CSV/JSON;
- вариант запуска с простым DI-контейнером.

Структура на уровнях:
- **Домен**: сущности и типы (`BankAccount`, `Category`, `Operation`, `OperationType`).
- **Фабрики**: создание доменных объектов с валидацией (`BankAccountFactory`, `CategoryFactory`, `OperationFactory`).
- **Репозитории (in-memory)**: хранение данных (`InMemory*Repository`).
- **Фасады/Сервисы**: сценарии CRUD и бизнес-операции (`*Facade`, `ImportService`, `ExportService`).
- **Команды**: атомарные пользовательские действия (`Create*Command`).
- **Интеграции ввода/вывода**: импортёры/экспортёры CSV/JSON, консольный UI.
- **DI-вариант**: `DIContainer`, аннотации `@Component`, `@Inject`.
- **Точки входа**: `com.hsebank.finance.Main` и `MainWithDI`.

## B. SOLID и GRASP

### SOLID
- **Single Responsibility (SRP)** — каждый слой решает свой класс задач: фабрики отвечают за корректное создание объектов (`*Factory`), фасады — за координацию сценариев (`*Facade`), репозитории — за хранение (`InMemory*Repository`), импорт/экспорт — за форматы (`*Importer`, `*Exporter`).
- **Open/Closed (OCP)** — добавление новых форматов импорта/экспорта не требует изменения существующего кода: достаточно новой реализации интерфейсов `DataImporter`/`DataExportVisitor` и регистрации в DI/конфигурации.
- **Liskov Substitution (LSP)** — взаимозаменяемость реализаций импортёров/экспортёров и репозиториев через общие абстракции (контракты `DataImporter`, `Exporter`, `Repository`).
- **Interface Segregation (ISP)** — разделение контрактов: репозитории, импорт/экспорт и команды имеют узкоспециализированные интерфейсы; пользователь не зависит от лишних методов.
- **Dependency Inversion (DIP)** — высокоуровневые компоненты (`ConsoleApplication`, фасады, сервисы) зависят от абстракций (интерфейсов импортёров, репозиториев, экспортёров). Внедрение — через DI-контейнер (`@Inject`) или конструкторы.

### GRASP
- **Controller** — `ConsoleApplication` и команды (`CreateBankAccountCommand`, `CreateIncomeCommand`, и т.п.) принимают вход и инициируют действия домена.
- **Creator** — фабрики (`*Factory`) создают доменные объекты, так как обладают знаниями о корректной инициализации.
- **Information Expert** — расчёт/валидация на тех классах, где есть данные (например, применение операции к счёту происходит рядом с `BankAccount`/фасадом).
- **Low Coupling / High Cohesion** — разделение по слоям: объекты связаны через интерфейсы; каждый модуль решает компактную задачу.
- **Polymorphism** — выбор поведения по типу формата на основе реализаций интерфейсов импортёров/экспортёров.
- **Indirection** — фасады и DI-контейнер вводят промежуточный уровень между UI и доменом.
- **Protected Variations** — смена форматов файлов/источников (`CSV`, `JSON`, потенциально БД) изолирована за интерфейсами импортёров/репозиториев.

## C. Реализованные паттерны GoF
- **Command** — классы `CreateBankAccountCommand`, `CreateCategoryCommand`, `CreateIncomeCommand`, `CreateExpenseCommand`. Обоснование: команда инкапсулирует действие и параметры, упрощая историю операций, логирование и измерение времени.
- **Decorator** — `TimingDecorator` оборачивает выполнение команды для измерения длительности. Обоснование: не меняем бизнес-логику, добавляем нефункциональные аспекты.
- **Template Method** — абстрактный `DataImporter` задаёт общий алгоритм импорта; конкретные импортёры (CSV/JSON) переопределяют шаги. Обоснование: единый процесс с вариациями.
- **Visitor** — `DataExportVisitor` отделяет формат вывода от структуры домена; реализации для CSV/JSON оформляют сериализацию. Обоснование: добавление новых форматов без изменения доменных классов.
- **Facade** — `BankAccountFacade`, `CategoryFacade`, `OperationFacade` дают упрощённый интерфейс к репозиториям и бизнес-правилам. Обоснование: минимизируем связность UI с инфраструктурой.
- **Factory Method** — фабрики `*Factory` создают валидные экземпляры сущностей, централизуя правила построения. Обоснование: единая точка контроля инвариантов.

> Примечание: репозитории — общеупотребимый архитектурный паттерн (не GoF), применён для изоляции хранения.

---

# Что реализовано (кратко)
- Доменная модель: `BankAccount`, `Category` (доход/расход), `Operation`, `OperationType`.
- Фасады/сервисы: `BankAccountFacade`, `CategoryFacade`, `OperationFacade`, `ImportService`, `ExportService`.
- In-memory репозитории: `InMemoryBankAccountRepository`, `InMemoryCategoryRepository`, `InMemoryOperationRepository`.
- Фабрики: `BankAccountFactory`, `CategoryFactory`, `OperationFactory`.
- Команды: `CreateBankAccountCommand`, `CreateCategoryCommand`, `CreateIncomeCommand`, `CreateExpenseCommand`.
- Декоратор времени: `TimingDecorator`.
- Импорт CSV/JSON: `DataImporter` + реализации.
- Экспорт CSV/JSON: `DataExportVisitor` + реализации.
- DI: `DIContainer`, `@Component`, `@Inject`.
- Консольный UI: `ConsoleApplication`.
- Точки входа: `com.hsebank.finance.Main`, `com.hsebank.finance.MainWithDI`.

---

# Требования
- **Java 25+** (JDK)
- **Maven 3.6+** (рекомендуется для сборки и запуска)

---

# Запуск приложения

## Способ 1: Через Maven (рекомендуется)

> Этот способ работает одинаково на всех операционных системах

### 1) Сборка проекта
```bash
cd hse-bank-finance
mvn clean compile
```

### 2) Запуск приложения

**Обычная версия:**
```bash
mvn exec:java -Dexec.mainClass="com.hsebank.finance.Main"
```

**Версия с DI-контейнером:**
```bash
mvn exec:java -Dexec.mainClass="com.hsebank.finance.MainWithDI"
```

### 3) Создание JAR-файла (опционально)
```bash
mvn clean package
java -jar target/hse-bank-finance-1.0-SNAPSHOT.jar
```

### 4) Запуск тестов
```bash
mvn test
```

---

# Запуск без Maven

> Каталог проекта: `hse-bank-finance/`

## 1) Компиляция

**Linux/macOS:**
```bash
cd hse-bank-finance
mkdir -p target/classes
find src/main/java -name "*.java" > sources.txt
javac -d target/classes @sources.txt
```

**Windows (PowerShell):**
```powershell
cd hse-bank-finance
New-Item -ItemType Directory -Force target/classes | Out-Null
Get-ChildItem -Recurse src/main/java -Filter *.java | ForEach-Object FullName > sources.txt
javac -d target/classes @sources.txt
```

## 2) Запуск

**Обычная версия:**
```bash
java -cp target/classes com.hsebank.finance.Main
```

**Версия с DI-контейнером:**
```bash
java -cp target/classes com.hsebank.finance.MainWithDI
```

## Windows (UTF-8)
При проблемах с кириллицей:
```powershell
chcp 65001
java -Dfile.encoding=UTF-8 -cp target/classes com.hsebank.finance.Main
```
