package org.xmlvm.demo.xokoban;

/**
 * A ball is a sub-class of {@link MovableGamePiece} that represents the piece
 * to be pushed by the man.
 */
public class Ball extends MovableGamePiece {
    public Ball(GameView view, int x, int y) {
        super(view, R.drawable.ball_20, x, y);
    }
}
