# SO2 Lab 3 – Inheritance, Polymorphism, Collections, Patterns, Exceptions (Java)

All Lab 3 tasks are implemented as separate, well‑commented console apps under `src/lab3/taskX/` with clear, student‑friendly explanations in code.

## Folder layout
- Task 1: `src/lab3/task1/Task1StudentManagementApp.java` (Person → Student inheritance, MAX_ANZAHL, menu)
- Task 2: `src/lab3/task2/VehicleApp.java` (Vehicle base class, Car/Motorcycle/Truck, polymorphism)
- Task 3: `src/lab3/task3/AnimalApp.java` (abstract Animal, Flyable/Swimmable interfaces)
- Task 4: `src/lab3/task4/StudentRegistryApp.java` (ArrayList, Set, Map; sorting & searching)
- Task 5: `src/lab3/task5/PatternsDemoApp.java` (Singleton, Observer, Factory, Strategy)
- Task 6: `src/lab3/task6/ExceptionAndFileIOApp.java` (custom exceptions, CSV/JSON IO, logging)

Each file contains extensive inline comments explaining the purpose of classes, methods, and key Java/OOP ideas.

## Prerequisites
- Java 8+ on PATH (javac/java)
- Windows Command Prompt (cmd.exe) or compatible terminal

## Compile (Lab 3 only)
Run this from the project root: `c:\Users\kamel\OneDrive\Desktop\Aufgabe.java`

```
if not exist bin mkdir bin && javac -d bin ^
  src\lab3\task1\*.java ^
  src\lab3\task2\*.java ^
  src\lab3\task3\*.java ^
  src\lab3\task4\*.java ^
  src\lab3\task5\*.java ^
  src\lab3\task6\*.java
```

Notes:
- This compiles only Lab 3 packages to avoid conflicts with any other Person/Student classes in other folders.
- Keep the folder structure aligned with the `package lab3.taskX;` declarations.

## Run
Pick any app to run after compiling:

- Task 1 – Student Management (inheritance):
```
java -cp bin lab3.task1.Task1StudentManagementApp
```

- Task 2 – Vehicle hierarchy (polymorphism):
```
java -cp bin lab3.task2.VehicleApp
```

- Task 3 – Animal classification (abstract + interfaces):
```
java -cp bin lab3.task3.AnimalApp
```

- Task 4 – Student registry (Collections, sorting/searching):
```
java -cp bin lab3.task4.StudentRegistryApp
```

- Task 5 – Design patterns demo (Singleton, Observer, Factory, Strategy):
```
java -cp bin lab3.task5.PatternsDemoApp
```

- Task 6 – Exceptions & File I/O (CSV/JSON + logging):
```
java -cp bin lab3.task6.ExceptionAndFileIOApp
```

Task 6 will create `students.csv` and `students.json` in your current working directory.

## Tips
- If your IDE complains about package mismatches, ensure the file is in a path matching its `package` statement (e.g., `src/lab3/task2/VehicleApp.java` must start with `package lab3.task2;`).
- You can recompile a single task by pointing `javac` to just that task’s folder.
- For any issues or suggestions, feel free to use the /reportbug command.
