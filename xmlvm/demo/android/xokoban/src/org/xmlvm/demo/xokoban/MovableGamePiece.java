package org.xmlvm.demo.xokoban;

import org.xmlvm.iphone.XMLVMNoAutoReleasePool;

/**
 * A MovableGamePiece is a special kind of {@link GamePiece} that can be moved
 * on the screen.
 */
@XMLVMNoAutoReleasePool
public class MovableGamePiece extends GamePiece {
    private int counter;
    private int dx;
    private int dy;
    
    private int animationFactor;

    private int px;
    private int py;

    protected MovableGamePiece(GameView view, int resourceID, int tileSize, int x, int y) {
        super(view, resourceID, tileSize ,x, y, true);
    }

    /**
     * Initiates a movement in the given direction.
     * 
     * @param dx
     *            How many pixels to move horizontally.
     * @param dy
     *            How many pixels to move vertically.
     */
    public void startMoving(int dx, int dy) {
        view.getMover().moveGamePiece(this);
        this.dx = dx;
        this.dy = dy;
        px = 0;
        py = 0;
        animationFactor = 2;//getTileSize() / 10;
        counter = getTileSize() / animationFactor;
    }

    /**
     * Moves the game piece on step in the direction given to
     * {@link #startMoving(int, int)}.
     * 
     * @return TODO
     */
    public boolean moveOneStep() {
        px += dx * animationFactor;
        py += dy * animationFactor;
        updatePosition(px, py);
        counter--;
        if (counter == 0) {
            x += dx;
            y += dy;
            return true;
        }
        return false;
    }
    
    public boolean isMoving() {
        return view.getMover().isMoving();
    }
}
