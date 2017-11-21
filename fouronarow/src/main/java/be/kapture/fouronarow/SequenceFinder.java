package be.kapture.fouronarow;

import java.util.List;
import java.util.Optional;

public class SequenceFinder {

    public static int findLongestSequence(List<Optional<Colour>> colours, Colour colourToCheck) {
        int longestSequence = 0;
        int currentSequence = 0;

        for (Optional<Colour> colour : colours) {
            if (colour.isPresent() && colour.get() == colourToCheck) {
                currentSequence++;
                if (currentSequence > longestSequence) {
                    longestSequence = currentSequence;
                }
            } else {
                currentSequence = 0;
            }
        }

        return longestSequence;
    }
}
