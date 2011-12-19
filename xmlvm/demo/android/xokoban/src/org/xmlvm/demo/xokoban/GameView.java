/* Copyright (c) 2002-2011 by XMLVM.org
 *
 * Project Info:  http://www.xmlvm.org
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 2.1 of the License, or
 * (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
 * License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
 * USA.
 */

package org.xmlvm.demo.xokoban;

import android.content.Context;
import android.view.View;

/**
 * The GameView class wraps everything that is required for displaying a game.
 */
public class GameView {

    /** The view which is the board for our moving sprites. */
    private NonLayoutingLayout boardView;

    /** The GameController controlling the game. */
    private GameController     gameController;

    /** The helper used to animate the man's moves. */
    private GamePieceMover     mover;

    /** The boards Y offset from the display's top left corner. */
    private int                offsetTop;

    /** The boards X offset from the display's top left corner. */
    private int                offsetLeft;


    /**
     * Constructor to create a GameActivity and associate it with the
     * application's activity.
     * 
     * @param boardView
     *            The view which will contain the game sprites.
     */
    public GameView(NonLayoutingLayout boardView) {
        this.boardView = boardView;
        this.mover = new GamePieceMover();
    }

    public void addViewToBoard(View view) {
        boardView.addView(view);
    }

    public void addViewToBoard(View view, int index) {
        boardView.addView(view, index);
    }

    public Context getContext() {
        return boardView.getContext();
    }

    /**
     * Displays the given game board.
     * 
     * @param board
     *            The board to display.
     */
    public void displayBoard(Board board) {
        int width = board.getWidth();
        int height = board.getHeight();
        int tileSize = determineTileSize(width, height);

        offsetTop = (boardView.getHeight() - (height * tileSize)) / 2;
        offsetLeft = (boardView.getWidth() - (width * tileSize)) / 2;

        // Start with an empty display.
        boardView.removeAllViews();

        Ball ball;
        Goal goal;
        Man man;

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                switch (board.getBoardPiece(x, y)) {
                case Board.GOAL:
                    goal = new Goal(this, tileSize, x, y);
                    gameController.addGoal(goal);
                    break;
                case Board.BALL:
                    ball = new Ball(this, tileSize, x, y);
                    gameController.addBall(ball);
                    break;
                case Board.BALL_IN_GOAL:
                    goal = new Goal(this, tileSize, x, y);
                    gameController.addGoal(goal);
                    ball = new Ball(this, tileSize, x, y);
                    gameController.addBall(ball);
                    break;
                case Board.MAN:
                    man = new Man(this, tileSize, x, y);
                    gameController.setMan(man);
                    break;
                case Board.MAN_ON_GOAL:
                    goal = new Goal(this, tileSize, x, y);
                    gameController.addGoal(goal);
                    man = new Man(this, tileSize, x, y);
                    gameController.setMan(man);
                    break;
                case Board.WALL:
                    new Wall(this, tileSize, x, y);
                    break;
                }
                if (board.isFloor(x, y)) {
                    new Floor(this, tileSize, x, y);
                }
            }
        }
    }

    public GameController getGameController() {
        return this.gameController;
    }

    public void setGameController(GameController gameController) {
        this.gameController = gameController;
        mover.setMoveFinishedHandler(gameController);
    }

    public int getOffsetLeft() {
        return this.offsetLeft;
    }

    public int getOffsetTop() {
        return this.offsetTop;
    }

    public GamePieceMover getMover() {
        return this.mover;
    }

    /**
     * Depending on the screen- and board size, this method figures out which
     * size of tiles to use.
     * 
     * @param boardWidth
     *            the width of the board, measured in tiles.
     * @param boardHeight
     *            the height of the board, measured in tiles.
     * 
     * @return the size of the tiles.
     */
    private int determineTileSize(int boardWidth, int boardHeight) {
        int maxTileWidth = boardView.getWidth() / boardWidth;
        int maxTileHeight = boardView.getHeight() / boardHeight;
        int maxTileSize = Math.min(maxTileWidth, maxTileHeight);

        // Higher resultion devices to a great job scaling to any size.
        if (boardView.getWidth() >= 400) {
            return maxTileSize;
        } else {
            if (maxTileSize < GamePiece.SIZE_THRESHOLD) {
                return 20;
            } else {
                return 30;
            }
        }
    }
}
