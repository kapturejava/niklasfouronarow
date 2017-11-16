package be.kapture.fouronarow;

import java.util.*;
import java.util.stream.IntStream;

import static be.kapture.fouronarow.Colour.RED;

/**
 * Created by missika on 15/11/2017.
 * @noinspection WeakerAccess
 */
public class Board {
    static final int NUMBER_OF_COLUMNS = 7;
    static final int NUMBER_OF_ROWS = 6;
    private final Colour startColour;

    List<List<Colour>> content = new ArrayList<>();

    public Board(Colour startColour) {
        this.startColour = startColour;
        this.content = new ArrayList<>();
        IntStream.range(0, NUMBER_OF_COLUMNS).forEach(ignored -> content.add(new ArrayList<>()));
    }

    public void put(int nr, Colour colour) {
        checkValidColumn(nr);

        if (content.get(nr).size() >= NUMBER_OF_ROWS) {
            throw new IllegalArgumentException("");
        }

        content.get(nr).add(colour);
    }

    public Optional<Integer> getPlace(int column) {
        checkValidColumn(column);
        return Optional.of(content.get(column).size());
    }

    private void checkValidColumn(int column) {
        if (column < 0 || column > 6) {
            throw new IllegalArgumentException("Please enter a positive number");
        }
    }

    public Optional<Colour> getColourAt(int column, int row){
        if (row >= content.get(column).size()) {
            return Optional.empty();
        }
        return Optional.of(content.get(column).get(row));
    }

    public Colour getActivePlayer(){
        long totalPlayed = content.stream().flatMap(Collection::stream).filter(Objects::nonNull).count();
        if (totalPlayed == NUMBER_OF_COLUMNS*NUMBER_OF_ROWS) {
            throw new IllegalArgumentException("board is full!");
        }
        if (totalPlayed % 2 == 0) {
            return startColour;
        }
        return startColour.getOther();
    }

    public boolean hasWon() {
        int counter = 0;

        for (int i = 0; i < NUMBER_OF_ROWS; i++) {
            for (List<Colour> colours : content) {
                if (colours.size() > i && colours.get(i) == getActivePlayer().getOther()) {
                    counter++;
                } else {
                    counter = 0;
                }
                if (counter == 4) {
                    return true;
                }
            }
        }


        return false;
    }
}
