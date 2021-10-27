Formulation of Spring Core Project Implement Spring app (Salary
Emulator) and Junit tests for it. This application will be an internal
company salary application with Object Model: Employee, Position, Salary
and a few services - EmployeeService: hire/fire new Employees for the
specific position. - PositionService: CRUD over the list of available
positions in the company. - SalaryService: bind salary to position based
on yearly salary changes, inflation, \$ course and other company events
(choose only one). Emulate a few years of company life via console
output and duplicate it to a log file.

1.  Configure beans via XML or annotation-based or java-based approach.
2.  Implement 3 services using different types of autowiring.
3.  Pass bean references, string constants and primitive types as
    constructor parameters.
4.  Use setter approach for passing another bean parameter.
5.  Use SpEL to inject values (inline lists or math operations) with
    custom parser configuration.
6.  Provide tests for invalid data.

