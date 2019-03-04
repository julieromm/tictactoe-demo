# TicTacToe Demo App

Minimal [Spring Boot](http://projects.spring.io/spring-boot/) web application.

## Requirements

For building and running the application you need:

- JDK 11
- Maven 3

## Usage

Clone the project

```
$ git clone https://github.com/julieromm/tictactoe-demo
```

Import the project into an IDE and run the following mvn command to package and run the application:

```
$ mvn clean package spring-boot:run
```

Open `http://localhost:8090` and enjoy!

## Overview

The grid is represented as a continuous numeric sequence of cells, starting from 0 to 8 (inclusive).  The cells can be thought of as a sequence of concatenated rows of a 3x3 matrix, visualized as follows:
```
   0 | 1 | 2
   3 | 4 | 5
   6 | 7 | 8
```

## Exposed endpoints

The service exposes the following endpoints:

`GET /`

* Returns a game greeting.

`GET /grid`

* Returns current state of the grid.  Example:

```
$ curl -X GET http://localhost:8090/grid | jq
{
  "cells": ".........",
  "availableCells": [
    0,
    1,
    2,
    3,
    4,
    5,
    6,
    7,
    8
  ],
  "markedXCells": [],
  "markedOCells": [],
  "gameStatus": "NEW",
  "winningRow": null,
  "winningTag": ".",
  "errorMsg": null
}

```

`POST /X/{cell}`

* This POST request makes an X move on behalf of the user (for now, the user always uses X).  The path variable `cell` indicates the position of the cell that should be marked with 'X'.
* Once the designated move is validated and executed, the services executes a move on behalf of the "computer".  Currently, the computer plays using a random strategy, i.e. selecting any available cell and marking it as 'O'.
* Response: current grid state, indicating whether the game is still in progress, or whether it is over. If former, the POST request should be re-executed. In the latter case (i.e. game over), a winning player and a winning row is included in the response if available (i.e. unless it's a draw).

``` 
$ curl -X POST http://localhost:8090/X/1 | jq
...
$ curl -X POST http://localhost:8090/X/4 | jq
...
$ curl -X POST http://localhost:8090/X/7 | jq
{
  "cells": ".XO.X.OX.",
  "availableCells": [
    0,
    3,
    5,
    8
  ],
  "markedXCells": [
    1,
    4,
    7
  ],
  "markedOCells": [
    2,
    6
  ],
  "gameStatus": "GAME_OVER",
  "winningRow": {
    "cells": [
      1,
      4,
      7
    ]
  },
  "winningTag": "X",
  "errorMsg": null
}
```

`POST /init`

* (Re)initialize the game board. Should be called in order to start a new game. Example:

``` 
$ curl -X POST http://localhost:8090/init | jq
{
  "cells": ".........",
  "availableCells": [
    0,
    1,
    2,
    3,
    4,
    5,
    6,
    7,
    8
  ],
  "markedXCells": [],
  "markedOCells": [],
  "gameStatus": "NEW",
  "winningRow": null,
  "winningTag": ".",
  "errorMsg": null
}
```

