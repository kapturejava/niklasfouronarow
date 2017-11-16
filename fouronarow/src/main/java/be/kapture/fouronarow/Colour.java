package be.kapture.fouronarow;

/**
 * Created by missika on 15/11/2017.
 */
public enum Colour {
    RED, YELLOW;

    Colour getOther() {
        return this == RED ? YELLOW : RED;
    }
}
