# FliteTrakr challenge

In this challenge, we need to optimize out travel expenses looking for a way how to get the best out of our money and search for an optimized travel plan.  Main offices are located in Herzogenaurach (NUE), Canton, MA (BOS), Portland, OR (PDX), Amsterdam (AMS) and Hong Kong (HKG). Unfortunately not all airports are reachable from any other airport directly or stop-over flights are cheaper so that you also have to consider Frankfurt (FRA), London-Heathrow (LHR) and Dubai (DXB) cause in all of those there is a comfortable Lounge.


The current application reads in the current connection table including prices as the first line and being able to answer questions with every following line.

The format of each connection will be defined by  `<code-of-departure-airport>-<code-of-arrival-airport>-<price-in-euro>`  so e.g.  `AMS-PDX-617`. Multiple values will be separated by a comma and an optional whitespace. The line containing the price list will have the prefix  `Connections:`

`Connections: AMS-PDX-617,NUE-AMS-123, AMS-LHR-43,LHR-HKG-1235, NUR-FRA-61, FRA-HKG-1087`

Questions have the form:

 - _What is the price of the connection  **NUE-FRA-DXB-HKG-PDX-LHR**?_
  -   _What is the cheapest connection from  **AMS**  to  **DXB**?_
   -   _How many different connections with (maximum|minimum|exactly) 3 stops exist between  **PDX**  and  **BOS**?_
    - _Find all connections from  **AMS**  to  **LHR**  below 2500 Euros!_

## Preconditions

This project uses **Java 8**, and many functional features defined in the language.
It also uses **gradle** to build the project.

## Building and running the project

In order to build the project you can run

`gradlew build`

This will generate a jar file under `build/libs`

In order to run the project, make sure you have java 8 installed:

`java -version`

Then you can run it by specifying an input file which contains a connection table in the first line, and one or more questions after that.

`java -jar build/libs/Flitetrakr-0.1.0.jar input.txt`

The files `input.txt` and `input1.txt` can be used to try this example.

## Design Considerations

The problem statement is clearly mapped to building a graph, where nodes are airports, and edges are connections between those airports. This graph is implemented in the model package, in the `ConnectionGraph` class
Questions are done in natural language, so the simplest solution is to have Regex patterns to match those questions, and instantiate a `Question` subclass that knows how to process such question. This is done by the `QuestionFactory` class. This is extensible, since in case we want to support variations for a given question, we just need to add patterns in this class. For example, if besides _What is the price of the connection ...?_  we want to support _How much does it cost the connection ...?_, we just need to add a pattern for the later, and also instantiate a `ConnectionPriceQuestion` class in the factory.

The problem statement establishes that, for some questions, doing a stop-over at one place multiple times are allowed. For this to work, we need to make sure we can stop doing stop-overs. Some questions have such conditions, for example _Find all connections from  **AMS**  to  **LHR**  below 2500 Euros!_. However, if instead of `below` we used `above` then we could iterate forever.

This happens with the following question: _How many different connections with minimum 1 stop exist between  **FRA**  and  **LHR**?_
Therefore, questions with _minimum number of stops_ are currently discarded. They should have another condition to ensure we stop iterating, such as maximum price.


