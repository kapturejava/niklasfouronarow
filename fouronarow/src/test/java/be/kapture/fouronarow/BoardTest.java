package be.kapture.fouronarow;

import org.junit.Before;
import org.junit.Test;

import java.util.Optional;
import java.util.stream.IntStream;

import static be.kapture.fouronarow.Board.NUMBER_OF_COLUMNS;
import static be.kapture.fouronarow.Board.NUMBER_OF_ROWS;
import static be.kapture.fouronarow.Colour.RED;
import static be.kapture.fouronarow.Colour.YELLOW;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

public class BoardTest {

    private Board board;

    @Before
    public void setUp() throws Exception {
        board = new Board(Colour.RED);
    }

    @Test
    public void put_TooLow() throws Exception {
        assertThatCode(() -> board.put(-1, RED)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void put_TooHigh() throws Exception {
        Board board = new Board(Colour.RED);
        assertThatCode(() -> board.put(NUMBER_OF_COLUMNS, RED)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void put() throws Exception {
        int column = 0;
        Board board = new Board(Colour.RED);

        board.put(column, RED);
        assertThat(board.getColourAt(column,0)).isEqualTo(Optional.of(RED));

        board.put(column, YELLOW);
        assertThat(board.getColourAt(column,1)).isEqualTo(Optional.of(YELLOW));
    }

    @Test
    public void getPlace() throws Exception {
        int column = 0;
        Board board = new Board(Colour.RED);

        IntStream.range(column, NUMBER_OF_COLUMNS).forEach(c -> assertThat(board.getPlace(c)).isEqualTo(Optional.of(0)));

        board.put(column, RED);

        assertThat(board.getPlace(column)).isEqualTo(Optional.of(1));
        IntStream.range(0,NUMBER_OF_COLUMNS).filter(x -> x != column).forEach(c -> assertThat(board.getPlace(c)).isEqualTo(Optional.of(0)));

        board.put(column, YELLOW);
        assertThat(board.getColourAt(column,1)).isEqualTo(Optional.of(YELLOW));
    }

    @Test
    public void getColourAt() throws Exception {
        assertThat(board.getColourAt(0,0)).isEqualTo(Optional.empty());
    }

    @Test
    public void put_TooManyTimes() throws Exception {
        int column = 0;

        IntStream.range(column,NUMBER_OF_ROWS).forEach(ignored -> board.put(column, board.getActivePlayer()));

        assertThatCode(() -> board.put(column, RED)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void getPlace_TooLow() throws Exception {
        assertThatCode(() -> board.getPlace(-1)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void getPlace_TooHigh() throws Exception {
        assertThatCode(() -> board.getPlace(NUMBER_OF_COLUMNS)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void getActivePlayer_AtEnd() throws Exception {
        IntStream.range(0, NUMBER_OF_COLUMNS).forEach(column -> fillColumn(board, column));

        assertThatCode(board::getActivePlayer).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void getActivePlayer_StartRed() throws Exception {
        assertThat(board.getActivePlayer()).isEqualTo(RED);

        board.put(0,RED);
        assertThat(board.getActivePlayer()).isEqualTo(YELLOW);
    }

    @Test
    public void getActivePlayer_StartYellow() throws Exception {
        Board board = new Board(YELLOW);

        assertThat(board.getActivePlayer()).isEqualTo(YELLOW);

        board.put(0,YELLOW);
        assertThat(board.getActivePlayer()).isEqualTo(RED);
    }

    @Test
    public void hasWon_Row() throws Exception {
        IntStream.range(2, 5).forEach(c ->  {
            board.put(c, RED);
            board.put(c, YELLOW);
        });
        assertThat(board.hasWon()).isFalse();

        board.put(6,RED);
        board.put(5,YELLOW);
        board.put(6,RED);
        board.put(5,YELLOW);
        assertThat(board.hasWon()).isTrue();
    }

    @Test
    public void hasWon_Row_Interrupted() throws Exception {
        IntStream.range(0, 2).forEach(c ->  {
            board.put(c, RED);
            board.put(c, YELLOW);
        });

        IntStream.range(4, 6).forEach(c ->  {
            board.put(c, RED);
            board.put(c, YELLOW);
        });

        assertThat(board.hasWon()).isFalse();
    }

    @Test
    public void hasWon_Row_AtEnd() throws Exception {
        for (int i = 0; i < NUMBER_OF_COLUMNS; i++) {
            for (int j = 0; j < NUMBER_OF_ROWS - 1; j++) {
                board.put(i, (i + j) % 2 == 0 ? RED : YELLOW);
            }
        }

        putAlternating(board, 0, 4, 1, 5, 2, 6, 3);

        assertThat(board.hasWon()).isTrue();
    }

    @Test
    public void hasWon_Empty() throws Exception {
        assertThat(board.hasWon()).isFalse();
    }

    @Test
    public void hasWon_vertical() throws Exception {
        Board board = new Board(YELLOW);

        board.put(1, YELLOW);
        IntStream.range(0, 3)
                .forEach(i -> {
                    board.put(1, RED);
                    board.put(2, YELLOW);
                });
        board.put(1, RED);

        assertThat(board.hasWon()).isTrue();
    }

    /**
     * _ _ _ _ <b>R</b> <p>
     * _ _ _ <b>R</b> Y <p>
     * _ _ <b>R</b> R Y <p>
     * _ <b>R</b> Y Y Y <p>
     * _ Y R Y R <p>
     */
    @Test
    public void hasWon_mainNorthWestDiagonal() throws Exception {
        Board board = new Board(YELLOW);
        putAlternating(board, 1, 2, 2, 1, 3, 4, 3, 2, 4, 3, 4, 3, 4);

        assertThat(board.hasWon()).isFalse();
        board.put(4, RED);

        assertThat(board.hasWon()).isTrue();
    }

    /**
     * _ _ _ _ <b>R</b> <p>
     * _ _ _ <b>R</b> Y <p>
     * _ _ <b>R</b> Y Y <p>
     * _ <b>R</b> Y R Y <p>
     */
    @Test
    public void hasWon_diagonal_notOnMainNorthWestDiagonal() throws Exception {
        Board board = new Board(YELLOW);
        putAlternating(board, 2, 3, 3, 1, 4, 2, 4, 3, 4);

        assertThat(board.hasWon()).isFalse();
        board.put(4, RED);

        assertThat(board.hasWon()).isTrue();
    }

    /**
     * _ _ _ <b>R</b> _ _ _ <p>
     * _ _ _ R <b>R</b> _ _ <p>
     * _ _ _ Y Y <b>R</b> _ <p>
     * _ _ Y Y R Y <b>R</b> <p>
     */
    @Test
    public void hasWon_diagonal_mainNorthEastDiagonal() throws Exception {
        putAlternating(board, 6, 5, 5, 3, 4, 4, 4, 3, 3, 2);
        assertThat(board.hasWon()).isFalse();
        board.put(3, RED);

        assertThat(board.hasWon()).isTrue();
    }

    /**
     * _ _ <b>R</b> _ _ _ _ <p>
     * _ _ R <b>R</b> _ _ _ <p>
     * _ _ Y Y <b>R</b> _ _ <p>
     * _ _ R Y Y <b>R</b> _ <p>
     * _ _ Y R R Y _ <p>
     * _ Y R Y Y R _ <p>
     */
    @Test
    public void hasWon_diagonal_notOnMainNorthEastDiagonal() throws Exception {
        putAlternating(board, 5, 5, 5, 4, 4, 4, 4, 3, 3, 3, 2, 3, 3, 2, 2, 2, 2, 1);
        assertThat(board.hasWon()).isFalse();
        board.put(2, RED);

        assertThat(board.hasWon()).isTrue();
    }

    @Test
    public void doNotAllowOutOfTurn() throws Exception {
        assertThatCode(() -> board.put(0, YELLOW)).isInstanceOf(IllegalArgumentException.class);
    }

    private void fillColumn(Board board, int column) {
        IntStream.range(0, NUMBER_OF_ROWS).forEach(row -> board.put(column, row %2==0? RED: YELLOW));
    }

    private void putAlternating(Board board, int... columns) {
        for (int column : columns) {
            board.put(column, board.getActivePlayer());
        }
    }
}