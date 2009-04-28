package org.xmlvm.asokoban;

import android.app.Activity;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AbsoluteLayout;
import android.widget.ImageView;

/**
 * The GameView class wraps everything that is required for displaying a game.
 */
public class GameView {

    /** The Activity associated with this GameView. */
    private Activity       activity;

    /** The content view used to display the game. */
    private AbsoluteLayout layout;

    /** The GameController controlling the gane. */
    private GameController gameController;

    /** The helper used to animate the man's moves. */
    private GamePieceMover mover;

    /** The background image. */
    private ImageView      backgroundImage;

    /** The Info logo image. */
    private ImageView      infoImage;

    /** The size of a single game piece. */
    private int            tileSize;

    /** The display's X resolution. */
    private int            displayWidth;

    /** The display's Y resolution. */
    private int            displayHeight;

    /** The boards Y offset from the display's top left corner. */
    private int            offsetTop;

    /** The boards X offset from the display's top left corner. */
    private int            offsetLeft;

    /**
     * Constructor to create a GameActivity and associate it with the
     * application's activity.
     * 
     * @param activity
     *            The application's activity.
     */
    public GameView(Activity activity, GamePieceMover mover) {
        // Initialization
        tileSize = 20;
        this.mover = mover;
        this.activity = activity;

        // Connect view to activity and create background
        layout = new AbsoluteLayout(activity);
        activity.setContentView(layout);
        backgroundImage = new ImageView(activity);
        backgroundImage.setImageResource(R.drawable.background);
        infoImage = new ImageView(activity);
        infoImage.setImageResource(R.drawable.info);
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
        offsetTop = (displayHeight - (height * tileSize)) / 2;
        offsetLeft = (displayWidth - (width * tileSize)) / 2;

        // Start with an empty display and show background image
        layout.removeAllViews();

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                switch (board.getBoardPiece(x, y)) {
                case Board.BALL:
                    Ball ball = new Ball(this, x, y);
                    gameController.addBall(ball);
                    break;
                case Board.GOAL:
                    Goal goal = new Goal(this, x, y);
                    gameController.addGoal(goal);
                    break;
                case Board.MAN:
                    Man man = new Man(this, x, y);
                    gameController.setMan(man);
                    break;
                case Board.WALL:
                    new Wall(this, x, y);
                    break;
                }
                if (board.isFloor(x, y)) {
                    new Floor(this, x, y);
                }
            }
        }

        layout.addView(backgroundImage, 0);

        AbsoluteLayout.LayoutParams p = new AbsoluteLayout.LayoutParams(
                AbsoluteLayout.LayoutParams.WRAP_CONTENT, AbsoluteLayout.LayoutParams.WRAP_CONTENT,
                displayWidth - tileSize - 1, displayHeight - tileSize);
        layout.addView(infoImage, p);
    }

    public GameController getGameController() {
        return this.gameController;
    }

    public void setGameController(GameController gameController) {
        this.gameController = gameController;
    }

    public Activity getActivity() {
        return this.activity;
    }

    public AbsoluteLayout getLayout() {
        return this.layout;
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

    public void setDisplayWidth(int displayWidth) {
        this.displayWidth = displayWidth;
    }

    public void setDisplayHeight(int displayHeight) {
        this.displayHeight = displayHeight;
    }

    /**
     * Registers a listener that gets called when a touch event occurs.
     */
    public void setOnTouchListener(OnTouchListener listener) {
        layout.setOnTouchListener(listener);
    }

    /**
     * Accessor to retrieve a tile's size in pixels
     * 
     * @return The tile's pixel size.
     */
    public int getTileSize() {
        return tileSize;
    }

    /**
     * Adds a view to be displayed
     * 
     * @param view
     *            The View to add.
     */
    public void addView(View view) {
        layout.addView(view);
    }

    /**
     * Removes a view from being shown.
     * 
     * @param view
     *            The View to remove.
     */
    public void removeView(View view) {
        layout.removeView(view);
    }

    /**
     * Tests whether a given coordinate is inside the info logo.
     * 
     * @param x
     *            The x part of the coordinate to be tested.
     * 
     * @param y
     *            The y part of the coordinate to be tested.
     * 
     * @return true if the coordinate is inside of the logo, false otherwise.
     */
    public boolean isInsideInfoLogo(float x, float y) {
        return (int) x > displayWidth - 30 && (int) y > displayHeight - 30;
    }
}
