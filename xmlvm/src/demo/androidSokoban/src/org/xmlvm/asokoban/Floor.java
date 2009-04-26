package org.xmlvm.asokoban;

/**
 * A floor is painted inside the levels. Everywhere, the {@link Man} can go.
 */
public class Floor extends GamePiece {
    public Floor(GameView view, int x, int y) {
        super(view, R.drawable.floor, x, y, false);
    }
}
