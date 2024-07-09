# ♕ BYU CS 240 Chess

This project demonstrates mastery of proper software design, client/server architecture, networking using HTTP and WebSocket, database persistence, unit testing, serialization, and security.

## 10k Architecture Overview

The application implements a multiplayer chess server and a command line chess client.

[![Sequence Diagram](10k-architecture.png)](https://sequencediagram.org/index.html#initialData=C4S2BsFMAIGEAtIGckCh0AcCGAnUBjEbAO2DnBElIEZVs8RCSzYKrgAmO3AorU6AGVIOAG4jUAEyzAsAIyxIYAERnzFkdKgrFIuaKlaUa0ALQA+ISPE4AXNABWAexDFoAcywBbTcLEizS1VZBSVbbVc9HGgnADNYiN19QzZSDkCrfztHFzdPH1Q-Gwzg9TDEqJj4iuSjdmoMopF7LywAaxgvJ3FC6wCLaFLQyHCdSriEseSm6NMBurT7AFcMaWAYOSdcSRTjTka+7NaO6C6emZK1YdHI-Qma6N6ss3nU4Gpl1ZkNrZwdhfeByy9hwyBA7mIT2KAyGGhuSWi9wuc0sAI49nyMG6ElQQA)

## Sequence Diagram

You can view the interactive sequence diagram [here](https://sequencediagram.org/index.html?presentationMode=readOnly#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWDABLBoAmCtu+hx7ZhWqEUdPo0EwAIsDDAAgiBAoAzqswc5wAEbBVKGBx2ZM6MFACeq3ETQBzGAAYAdAE5M9qBACu2GADEaMBUljAASij2SKoWckgQaIEA7gAWSGBiiKikALQAfOSUNFAAXDAA2gAKAPJkACoAujAA9D4GUAA6aADeAETtlMEAtih9pX0wfQA0U7jqydAc45MzUyjDwEgIK1MAvpjCJTAFrOxclOX9g1AjYxNTs33zqotQyw9rfRtbO58HbE43FgpyOonKUCiMUyUAAFJForFKJEAI4+NRgACUh2KohOhVk8iUKnU5XsKDAAFUOrCbndsYTFMo1Kp8UYdKUAGJITgwamURkwHRhOnAUaYRnElknUG4lTlNA+BAIHEiFRsyXM0kgSFyFD8uE3RkM7RS9Rs4ylBQcDh8jqM1VUPGnTUk1SlHUoPUKHxgVKw4C+1LGiWmrWs06W622n1+h1g9W5U6Ai5lCJQpFQSKqJVYFPAmWFI6XGDXDp3SblVZPQN++oQADW6ErU32jsohfgyHM5QATE4nN0y0MxWMYFXHlNa6l6020C3Vgd0BxTF5fP4AtB2OSYAAZCDRJIBNIZLLdvJF4ol6p1JqtAzqBJoIei0afWYvN4cf7tkGFfMlsOtyjrsXyfksoEAucBaymqKDlAgB48rC+6Hqi6KxNiCaGC6YZumSFIGrS5ajiaRLhhaHLcryBqCsKMCvmIrrSkml5weUtHaI6zoEnhLIerqmSxv604hsx5qRhy0YwMJ8ZyjhybQSWqE8tmuaYABIKwSUVwDCRowLpOfTTrOzbjq2fQHMWWmFNkPYwP2g69HpI4GeZ1ZTkGpnzu5bbLqu3h+IEXgoOge4Hr4zDHukmSYHZF5FNQ17SAAoruKX1ClzQtA+qhPt0JmNugv5spp5SFXOGlKX+iVOvKMCIfYkUBl5RVoFh8kanxpIwOSYDCS1dZtWRTJupRXI8jGQZ0WEFXFeJEbaeCMnTVx2FdeR+EcCg3BCUGg3BtoI1mhGhSWtIO0UoYslrZ1rFnECykRb6akIHm1WdtZVxWVeNldjkYB9gOQ5LpwAXroEkK2ru0IwAA4qOrLRaecXnsw2nXnDGXZfYo4Fa1lXWaV1XlQTxWaWy2EEWACOjKoB3eR1cEbaN-G9RSA1ze1oabSxZ1UZNK1xtoQqzWTaA86zElLfVN3yNxia8bzpLILEtNqLCTN1Qp-4k-DFLq6or3vY9f1faWUy43T4yVP0VsoAAktINsAIy9gAzAALE8J6ZAaFYTF8OgIKADb+yBgdPPbAByo6B3sjQ-Ulf3xYDDnA70luIzbFR26OTuux73tTL7+r6fcfRByHIBh+XKxfDHceVwnoMrp4gUbtgPhQNg3DwIJhjqykMVngDlO-eUN4NDjePBOLQ6N2+i4lYppuk0Nc4L6OsdL22FMy-BMBoCgyTq7C0cn+r6EYlrPEyN17qenqZ-q2JD-jdJr9cQrOsPam5QX1PqOY2VVTafQnqWFywE3ITi+FzQylkV6nFTkDJyQEKy+TgeLBBrdwZBQCJYHaiFkgwAAFIQB5PDUcgRg6h1RmPDGaZqiUjvC0e2+MN7oCHHMEOhCoBwAgIhKAKwADqLAHaZRaAAIV3AoOAABpBu+dnblDdl7GASdjir3-jALm3Dni8MoAIoRojxGSJkXIxRUdlGF3UVBMBB9ygACsKFoDPuQnkV8UBohvj-FmJ1qZn3tk7Y6FFJITV5F-eQoterKMlidcB7EqGjDkszXCyt3R+C0JkM+WTn6jjfhk8alJsDZMHgU26aTtHAnKB4txwC1DqX3ow76SDbJo1QSDMwYN24QwCF4YAYREBelgMAbAvdCDxESMPFGqdx7J0nqldKmVsrGBXrrNeGxsBgEsKA1M8ztblBANwPAChlSaz8ekqW7oYDHJGURUJY1wkXV2oYYAyo7QCkqdrfx4YPQnKgGfR5fN2SlBeVdXRHyonAEuUra5-yRkDWBRJfmYLLqZEhQgIWh15brXumVfWNNEYgOaWxHSFs+j21UDnPOowC6qKLj7Ee4cDKRymLQmuLKK5KNGDveCzdE5tP+vZRy+iqU0spTYhlXsmWni5fXJ4HLa6uW5dY3lTdLKCv8kAA).

![Sequence Diagram](sequence-diagram.png)

## Modules

The application has three modules.

- **Client**: The command line program used to play a game of chess over the network.
- **Server**: The command line program that listens for network requests from the client and manages users and games.
- **Shared**: Code that is used by both the client and the server. This includes the rules of chess and tracking the state of a game.

## Starter Code

As you create your chess application you will move through specific phases of development. This starts with implementing the moves of chess and finishes with sending game moves over the network between your client and server. You will start each phase by copying course provided [starter-code](starter-code/) for that phase into the source code of the project. Do not copy a phases' starter code before you are ready to begin work on that phase.

## IntelliJ Support

Open the project directory in IntelliJ in order to develop, run, and debug your code using an IDE.

## Maven Support

You can use the following commands to build, test, package, and run your code.

| Command                    | Description                                     |
| -------------------------- | ----------------------------------------------- |
| `mvn compile`              | Builds the code                                 |
| `mvn package`              | Run the tests and build an Uber jar file        |
| `mvn package -DskipTests`  | Build an Uber jar file                          |
| `mvn install`              | Installs the packages into the local repository |
| `mvn test`                 | Run all the tests                               |
| `mvn -pl shared test`      | Run all the shared tests                        |
| `mvn -pl client exec:java` | Build and run the client `Main`                 |
| `mvn -pl server exec:java` | Build and run the server `Main`                 |

These commands are configured by the `pom.xml` (Project Object Model) files. There is a POM file in the root of the project, and one in each of the modules. The root POM defines any global dependencies and references the module POM files.

## Running the program using Java

Once you have compiled your project into an uber jar, you can execute it with the following command.

```sh
java -jar client/target/client-jar-with-dependencies.jar

♕ 240 Chess Client: chess.ChessPiece@7852e922
```
