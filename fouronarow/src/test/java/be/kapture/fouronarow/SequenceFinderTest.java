package be.kapture.fouronarow;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Optional;

import static be.kapture.fouronarow.Colour.RED;
import static be.kapture.fouronarow.Colour.YELLOW;
import static org.assertj.core.api.Assertions.assertThat;

public class SequenceFinderTest {

    @Test
    public void findLongestSequence_empty() throws Exception {
        int longestSequence = SequenceFinder.findLongestSequence(new ArrayList<>(), RED);

        assertThat(longestSequence).isEqualTo(0);
    }

    @Test
    public void findLongestSequence_oneElement() throws Exception {
        ArrayList<Optional<Colour>> colours = new ArrayList<>();
        colours.add(Optional.of(RED));

        int longestSequence = SequenceFinder.findLongestSequence(colours, RED);

        assertThat(longestSequence).isEqualTo(1);
    }

    @Test
    public void findLongestSequence_otherElement() throws Exception {
        ArrayList<Optional<Colour>> colours = new ArrayList<>();
        colours.add(Optional.of(YELLOW));

        int longestSequence = SequenceFinder.findLongestSequence(colours, RED);

        assertThat(longestSequence).isEqualTo(0);
    }

    @Test
    public void findLongestSequence_RRRYRR() throws Exception {
        ArrayList<Optional<Colour>> colours = new ArrayList<>();
        colours.add(Optional.of(RED));
        colours.add(Optional.of(RED));
        colours.add(Optional.of(RED));
        colours.add(Optional.of(YELLOW));
        colours.add(Optional.of(RED));
        colours.add(Optional.of(RED));

        int longestSequence = SequenceFinder.findLongestSequence(colours, RED);

        assertThat(longestSequence).isEqualTo(3);
    }

    @Test
    public void findLongestSequence_blankInBetween() throws Exception {
        ArrayList<Optional<Colour>> colours = new ArrayList<>();
        colours.add(Optional.of(RED));
        colours.add(Optional.empty());
        colours.add(Optional.of(RED));

        int longestSequence = SequenceFinder.findLongestSequence(colours, RED);

        assertThat(longestSequence).isEqualTo(1);
    }
}