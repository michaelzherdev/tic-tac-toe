package com.mzherdev.tictactoe.controller;

import com.mzherdev.tictactoe.service.TicTacToeService;
import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class TicTacToeControllerTest {

    @Value("${local.server.port}")
    private int serverPort;
    @Autowired
    TicTacToeService ticTacToeService;

    @Before
    public void setUp() {
        RestAssured.port = serverPort;
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    private static RequestSpecification whenApiCall() {
        return RestAssured.given()
                .log().all()
                .contentType(MediaType.APPLICATION_JSON.toString());
    }

    @Test
    public void testGetGame_Success() {
        createTestGame();

        whenApiCall()
                .pathParam("gameId", 1)
                .get("/game/{gameId}")
                .then()
                .log().all()
                .statusCode(HttpStatus.OK.value())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body("id", is(1))
                .body("activePlayerId", is(1))
                .body("board", is("123456789"))
                .body("finished", is(false))
                .body("result", is(nullValue()));
    }

    @Test
    public void testGetGame_Error_GameNotExists() {
        whenApiCall()
                .pathParam("gameId", 1)
                .get("/game/{gameId}")
                .then()
                .log().all()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body("message", is("Game 1 does not exist"));
    }

    @Test
    public void testCreateGame_Success() {
        whenApiCall()
                .body("{ \"player1Id\" : 1, \"player2Id\" : 2 }")
                .post("/game")
                .then()
                .log().all()
                .statusCode(HttpStatus.OK.value())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body("id", is(1))
                .body("activePlayerId", is(1))
                .body("board", is("123456789"))
                .body("finished", is(false))
                .body("result", is(nullValue()));
    }

    @Test
    public void testCreateGame_Error_UnexistedPlayer() {
        whenApiCall()
                .body("{ \"player1Id\" : 1, \"player2Id\" : 3 }")
                .post("/game")
                .then()
                .log().all()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body("message", is("Player 3 doesn't exist"));
    }

    @Test
    public void testDoMove() {
        createTestGame();

        whenApiCall()
                .pathParam("gameId", 1)
                .pathParam("playerId", 1)
                .param("cell", 1)
                .put("/game/{gameId}/player/{playerId}")
                .then()
                .log().all()
                .statusCode(HttpStatus.OK.value())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body("id", is(1))
                .body("activePlayerId", is(2))
                .body("board", is("X23456789"))
                .body("finished", is(false))
                .body("result", is(nullValue()));
    }

    @Test
    public void testDoMove_Success_DrawResult() {
        createTestGame();

        whenApiCall()
                .pathParam("gameId", 1)
                .pathParam("playerId", 1)
                .param("cell", 1)
                .put("/game/{gameId}/player/{playerId}")
                .then()
                .log().all()
                .statusCode(HttpStatus.OK.value())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body("id", is(1))
                .body("activePlayerId", is(2))
                .body("board", is("X23456789"))
                .body("finished", is(false))
                .body("result", is(nullValue()));

        whenApiCall()
                .pathParam("gameId", 1)
                .pathParam("playerId", 2)
                .param("cell", 2)
                .put("/game/{gameId}/player/{playerId}")
                .then()
                .log().all()
                .statusCode(HttpStatus.OK.value())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body("id", is(1))
                .body("activePlayerId", is(1))
                .body("board", is("X03456789"))
                .body("finished", is(false))
                .body("result", is(nullValue()));

        whenApiCall()
                .pathParam("gameId", 1)
                .pathParam("playerId", 1)
                .param("cell", 3)
                .put("/game/{gameId}/player/{playerId}")
                .then()
                .log().all()
                .statusCode(HttpStatus.OK.value())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body("id", is(1))
                .body("activePlayerId", is(2))
                .body("board", is("X0X456789"))
                .body("finished", is(false))
                .body("result", is(nullValue()));

        whenApiCall()
                .pathParam("gameId", 1)
                .pathParam("playerId", 2)
                .param("cell", 4)
                .put("/game/{gameId}/player/{playerId}")
                .then()
                .log().all()
                .statusCode(HttpStatus.OK.value())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body("id", is(1))
                .body("activePlayerId", is(1))
                .body("board", is("X0X056789"))
                .body("finished", is(false))
                .body("result", is(nullValue()));

        whenApiCall()
                .pathParam("gameId", 1)
                .pathParam("playerId", 1)
                .param("cell", 6)
                .put("/game/{gameId}/player/{playerId}")
                .then()
                .log().all()
                .statusCode(HttpStatus.OK.value())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body("id", is(1))
                .body("activePlayerId", is(2))
                .body("board", is("X0X05X789"))
                .body("finished", is(false))
                .body("result", is(nullValue()));

        whenApiCall()
                .pathParam("gameId", 1)
                .pathParam("playerId", 2)
                .param("cell", 5)
                .put("/game/{gameId}/player/{playerId}")
                .then()
                .log().all()
                .statusCode(HttpStatus.OK.value())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body("id", is(1))
                .body("activePlayerId", is(1))
                .body("board", is("X0X00X789"))
                .body("finished", is(false))
                .body("result", is(nullValue()));

        whenApiCall()
                .pathParam("gameId", 1)
                .pathParam("playerId", 1)
                .param("cell", 7)
                .put("/game/{gameId}/player/{playerId}")
                .then()
                .log().all()
                .statusCode(HttpStatus.OK.value())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body("id", is(1))
                .body("activePlayerId", is(2))
                .body("board", is("X0X00XX89"))
                .body("finished", is(false))
                .body("result", is(nullValue()));

        whenApiCall()
                .pathParam("gameId", 1)
                .pathParam("playerId", 2)
                .param("cell", 9)
                .put("/game/{gameId}/player/{playerId}")
                .then()
                .log().all()
                .statusCode(HttpStatus.OK.value())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body("id", is(1))
                .body("activePlayerId", is(1))
                .body("board", is("X0X00XX80"))
                .body("finished", is(false))
                .body("result", is(nullValue()));

        whenApiCall()
                .pathParam("gameId", 1)
                .pathParam("playerId", 1)
                .param("cell", 8)
                .put("/game/{gameId}/player/{playerId}")
                .then()
                .log().all()
                .statusCode(HttpStatus.OK.value())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body("id", is(1))
                .body("activePlayerId", is(2))
                .body("board", is("X0X00XXX0"))
                .body("finished", is(true))
                .body("result", is("Draw"));
    }

    @Test
    public void testDoMove_Success_WinnerPlayer2() {
        createTestGame();

        whenApiCall()
                .pathParam("gameId", 1)
                .pathParam("playerId", 1)
                .param("cell", 1)
                .put("/game/{gameId}/player/{playerId}")
                .then()
                .log().all()
                .statusCode(HttpStatus.OK.value())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body("id", is(1))
                .body("activePlayerId", is(2))
                .body("board", is("X23456789"))
                .body("finished", is(false))
                .body("result", is(nullValue()));

        whenApiCall()
                .pathParam("gameId", 1)
                .pathParam("playerId", 2)
                .param("cell", 2)
                .put("/game/{gameId}/player/{playerId}")
                .then()
                .log().all()
                .statusCode(HttpStatus.OK.value())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body("id", is(1))
                .body("activePlayerId", is(1))
                .body("board", is("X03456789"))
                .body("finished", is(false))
                .body("result", is(nullValue()));

        whenApiCall()
                .pathParam("gameId", 1)
                .pathParam("playerId", 1)
                .param("cell", 3)
                .put("/game/{gameId}/player/{playerId}")
                .then()
                .log().all()
                .statusCode(HttpStatus.OK.value())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body("id", is(1))
                .body("activePlayerId", is(2))
                .body("board", is("X0X456789"))
                .body("finished", is(false))
                .body("result", is(nullValue()));

        whenApiCall()
                .pathParam("gameId", 1)
                .pathParam("playerId", 2)
                .param("cell", 5)
                .put("/game/{gameId}/player/{playerId}")
                .then()
                .log().all()
                .statusCode(HttpStatus.OK.value())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body("id", is(1))
                .body("activePlayerId", is(1))
                .body("board", is("X0X406789"))
                .body("finished", is(false))
                .body("result", is(nullValue()));

        whenApiCall()
                .pathParam("gameId", 1)
                .pathParam("playerId", 1)
                .param("cell", 6)
                .put("/game/{gameId}/player/{playerId}")
                .then()
                .log().all()
                .statusCode(HttpStatus.OK.value())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body("id", is(1))
                .body("activePlayerId", is(2))
                .body("board", is("X0X40X789"))
                .body("finished", is(false))
                .body("result", is(nullValue()));

        whenApiCall()
                .pathParam("gameId", 1)
                .pathParam("playerId", 2)
                .param("cell", 8)
                .put("/game/{gameId}/player/{playerId}")
                .then()
                .log().all()
                .statusCode(HttpStatus.OK.value())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body("id", is(1))
                .body("activePlayerId", is(1))
                .body("board", is("X0X40X709"))
                .body("finished", is(true))
                .body("result", is("Winner is 2"));

    }

    @Test
    public void testDoMove_Error_IllegalCellNumber() {
        createTestGame();

        whenApiCall()
                .pathParam("gameId", 1)
                .pathParam("playerId", 1)
                .param("cell", 0)
                .put("/game/{gameId}/player/{playerId}")
                .then()
                .log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body("message", is("Parameter cell should be between 1 and 9"));

        whenApiCall()
                .pathParam("gameId", 1)
                .pathParam("playerId", 1)
                .param("cell", 10)
                .put("/game/{gameId}/player/{playerId}")
                .then()
                .log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body("message", is("Parameter cell should be between 1 and 9"));

    }

    @Test
    public void testDoMove_Error_GameFinished() {
        testDoMove_Success_WinnerPlayer2();

        whenApiCall()
                .pathParam("gameId", 1)
                .pathParam("playerId", 1)
                .param("cell", 1)
                .put("/game/{gameId}/player/{playerId}")
                .then()
                .log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body("message", is("Game is finished. No more moves allowed"));

    }

    @Test
    public void testDoMove_Error_WrongPlayer() {
        createTestGame();

        whenApiCall()
                .pathParam("gameId", 1)
                .pathParam("playerId", 2)
                .param("cell", 1)
                .put("/game/{gameId}/player/{playerId}")
                .then()
                .log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body("message", is("It is not your turn!"));

    }

    @Test
    public void testDoMove_Error_UnexistedPlayer() {
        createTestGame();

        whenApiCall()
                .pathParam("gameId", 1)
                .pathParam("playerId", 3)
                .param("cell", 1)
                .put("/game/{gameId}/player/{playerId}")
                .then()
                .log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body("message", is("Player doesn't exist or doesn't play this game"));
    }

    @Test
    public void testDoMove_Error_SameCellTwice() {
        createTestGame();

        whenApiCall()
                .pathParam("gameId", 1)
                .pathParam("playerId", 1)
                .param("cell", 1)
                .put("/game/{gameId}/player/{playerId}")
                .then()
                .log().all()
                .statusCode(HttpStatus.OK.value())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body("id", is(1))
                .body("activePlayerId", is(2))
                .body("board", is("X23456789"))
                .body("finished", is(false))
                .body("result", is(nullValue()));

        whenApiCall()
                .pathParam("gameId", 1)
                .pathParam("playerId", 2)
                .param("cell", 1)
                .put("/game/{gameId}/player/{playerId}")
                .then()
                .log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body("message", is("Cell already busy"));
    }

    private void createTestGame() {
        ticTacToeService.createGame(1,2);
    }
}
