# Task 1

## ChatGPT 4o
- _Prompt 1_: The following files I'm showing to you are from an OOP exam. Follow the instructions on 
TimetableFactoryTest (the comment on the top) make all tests pass. 
  <br />_Result_: 
  - Produced an average-level implementation with some pros and cons:
    - **pros**: Tried to maintain immutability for Timetable data structure, used advanced mechanism of 
    language like streams.
    - **cons**: Some point of implementation are too complex
    , for example:
      ```java
      this.data = data.entrySet().stream().collect(Collectors.toMap(
                Map.Entry::getKey,
                e -> new HashMap<>(e.getValue())
        ));
      ```
      could be write as
      ```java
      this.data = Map.copyOf(data);
      ```
      
      Also too many nested for loops, the solution is suboptimal.The last test doesn't pass.
  
  - _Prompt 2_ (Chain of thought): The following files I'm showing to you are from an OOP exam. Follow the instructions on
    TimetableFactoryTest (the comment on the top) make all tests pass.
    In order to obtain the solution follow these steps:

    1) Analyze first the interface provided Timetable and TimetableFactory
    2) Analyze all the test in order to obtain the exact behaviour of the application. It's required that all test should pass
    3) Design a solution that will make the code readable and without repetition
    4) Develop the solution

    <br />_Result_:
    - Slightly better implementation than the one obtained in the first prompt
    - Code is more readable, succinct.
    - All the test pass

## Claude Sonnet 4
- _Prompt 1_ (Zero shot): The following files I'm showing to you are from an OOP exam. Follow the instructions on
  TimetableFactoryTest (the comment on the top) make all tests pass.
  <br />_Result_: 
  
  - Produce a generally good implementation.
  - **pros**: Usage of Java lang advanced features like records, good code subdivision (at function level), good 
  readability.
  - **cons**: Not all test are passing: needed a new prompt "The last test doesn't pass" to make it work 
    correctly. 

- _Prompt 2_ (Chain of thought): The following files I'm showing to you are from an OOP exam. Follow the instructions on
  TimetableFactoryTest (the comment on the top) make all tests pass.
  In order to obtain the solution follow these steps:

  1) Analyze first the interface provided Timetable and TimetableFactory
  2) Analyze all the test in order to obtain the exact behaviour of the application. It's required that all test should pass
  3) Design a solution that will make the code readable and without repetition
  4) Develop the solution

  <br />_Result_: 
    - Produce an implementation similar to the first one. Test doesn't pass so need another prompt for correcting it. 
      The cons and the pros are equals to the first one 

## Junie

Note: Junie is integrated inside the IDE of IntelliJ IDEA the prompt is slightly different from the ones that are presented
before. this tool will automatically analyze the project context using files and other information that are present
inside the working folder, create a set of goal to achieve and execute a set of instruction: 

_Prompt _ (Zero shot): Given the instruction present on TimetableFactoryTest.java proceed to implement the interfaces 
of Timetable.java and TimetableFactory.java in order to implement the methods in the test files. 
Put the code in a new package under io.github.asmd23 named "junie".

Plan Generated
1. Create the new package structure io.github.asmd23.junie
2. Analyze the Timetable interface and design an implementation
3. Analyze the TimetableFactory interface and design an implementation
4. Implement TimetableImpl class in the junie package
5. Implement TimetableFactoryImpl class in the junie package
6. Run the tests to verify the implementation works correctly
7. Make necessary adjustments to ensure all tests pass

The code generated is generally good, we have some problems similar to the ones encountered with ChatGPT where at some 
point the implementation is too complex. The code that has generated on the first try failed on the last test, then given the feedback
of the test result, it corrects the implementation with a solution that satisfies all the requirements


# Result of the task
All the three alternatives produced a low/medium quality code; they sometimes need some more prompt on making them pass all 
tests (ChatGPT prompt1, Claude). 
With any hint they haven't tried to produce code that presents some architectural pattern all the three candidates tried 
to produce a straightforward solution. 
Claude and ChatGPT presented some hallucinations during the generation with methods that doesn't exists. 





