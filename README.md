# Tic Tac Toe
Java, SpringBoot, H2

# Getting Started
The goal of this task is to implement an API which can be used to play the well known game Tic Tac Toe.

The game has some basic rules:
   1. There are 2 players
   2. Every player is represented by a unique symbol, usually it's X and O
   3. The board consists of a 3x3 matrix. A player wins if they can align 3 of their markers in a vertical, horizontal or diagonal line
   4. If no more moves are possible, the game should finish

There are some things to be considered for the design of the API:
   * It might be possible that some calls need be protected against concurrent access
   * The game state should be persisted, use some DB backing for that
   * It is recommended to use Spring (Boot) for the implementation

#### running spring boot via maven
To build the project using maven execute:

* ``mvn clean install``

to start this application using maven, execute:

* ``mvn spring-boot:run``


