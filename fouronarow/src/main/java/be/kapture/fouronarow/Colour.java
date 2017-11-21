package be.kapture.fouronarow;

/**
 * Created by missika on 15/11/2017.
 */
public enum Colour {
    RED, YELLOW, NONE;

    Colour getOther() {
        return this == RED ? YELLOW : RED;
    }

    @Override
    public String toString() {
        switch (this) {
            case RED:
                return "R";
            case YELLOW:
                return "Y";
            default:
                return "_";
        }
    }
}
