package be.kapture.fouronarow;

import java.util.*;
import java.util.function.IntFunction;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;

/**
 * Created by missika on 15/11/2017.
 * @noinspection WeakerAccess
 */
public class Board {
    static final int NUMBER_OF_COLUMNS = 7;
    static final int NUMBER_OF_ROWS = 6;
    private final Colour startColour;

    private List<List<Colour>> content = new ArrayList<>();

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
        if (isInvalidColumn(column)) {
            throw new IllegalArgumentException("Please enter a positive number");
        }
    }

    private boolean isInvalidColumn(int column) {
        return column < 0 || column >= NUMBER_OF_COLUMNS;
    }

    public Optional<Colour> getColourAt(int column, int row){
        if (isInvalidColumn(column) || row >= content.get(column).size()) {
            return Optional.empty();
        }
        return Optional.of(content.get(column).get(row));
    }

    public Colour getActivePlayer(){
        return getActivePlayer(true);
    }

    private Colour getActivePlayer(boolean checkForFullBoard) {
        long totalPlayed = content.stream().flatMap(Collection::stream).filter(Objects::nonNull).count();
        if (checkForFullBoard) {
            checkBoardFull(totalPlayed);
        }
        if (totalPlayed % 2 == 0) {
            return startColour;
        }
        return startColour.getOther();
    }

    private void checkBoardFull(long totalPlayed) {
        if (totalPlayed == NUMBER_OF_COLUMNS*NUMBER_OF_ROWS) {
            throw new IllegalArgumentException("board is full!");
        }
    }

    public boolean hasWon() {
        final Colour colourToCheck = getActivePlayer(false).getOther();

        return checkHorizontal(colourToCheck) ||
                checkVertical(colourToCheck) ||
                checkDiagonal(this::getNorthEastDiagonal, colourToCheck) ||
                checkDiagonal(this::getNorthWestDiagonal, colourToCheck);

    }

    private boolean checkHorizontal(Colour colourToCheck) { // TODO convert to using streams
        int counter = 0;

        for (int i = 0; i < NUMBER_OF_ROWS; i++) {
            for (List<Colour> colours : content) {
                if (colours.size() > i && colours.get(i) == colourToCheck) {
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

    private boolean checkVertical(Colour colourToCheck) {
        OptionalInt longestVerticalSequence = content.stream()
                .mapToInt(col -> SequenceFinder.findLongestSequence(toOptionals(col), colourToCheck))
                .max();

        return longestVerticalSequence.getAsInt() >= 4;
    }

    private boolean checkDiagonal(IntFunction<List<Optional<Colour>>> getDiagonal, Colour colourToCheck) {
        IntStream offsets = IntStream.range(1 - NUMBER_OF_ROWS, NUMBER_OF_COLUMNS);

        OptionalInt longestDiagonalSequence = offsets.mapToObj(getDiagonal)
                .mapToInt(diagonal -> SequenceFinder.findLongestSequence(diagonal, colourToCheck))
                .max();

        return longestDiagonalSequence.isPresent() && longestDiagonalSequence.getAsInt() >= 4;
    }

    private List<Optional<Colour>> getNorthEastDiagonal(int offset) {
        return IntStream.range(0, NUMBER_OF_ROWS).mapToObj(row -> getColourAt(row + offset, row))
                .collect(toList());
    }

    private List<Optional<Colour>> getNorthWestDiagonal(int offset) {
        return IntStream.range(0, NUMBER_OF_ROWS).mapToObj(row -> getColourAt(NUMBER_OF_COLUMNS - 1 - (row + offset), row))
                .collect(toList());
    }

    private List<Optional<Colour>> toOptionals(List<Colour> colours) {
        return colours.stream()
                .map(Optional::of)
                .collect(toList());
    }
}
