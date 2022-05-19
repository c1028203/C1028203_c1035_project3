/**
 * @author Kostiantyn Potomkin
 * @version 0.9
 * @since 05-03-2020
 */
package uk.ac.ncl.entity;
/**
 * The below block of code is used to import necessary packages to allow the code to run
 */

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.ArrayList;

import static uk.ac.ncl.Constants.BOARD_SIZE;

/**
 * This class represents each cell on the board and is used to store information about them
 */
public class Cell {

    /**
     * This represents the current status of a cell whether that be light, dark or empty.
     */
    private CellStatus value;
    /**
     * Links cell to the corresponding UI element.
     */
    private JButton jButton;
    /**
     * This value represent the row in which a cell resides.
     */
    private int row;
    /**
     *  This value represents the column in which a cell resides.
     */
    private int column;

    /**
     *  This values shows the potential moves of a piece
     */
    private Move move;

    public Cell(CellStatus value, JButton jButton, int row, int column){
        this.value = value;
        this.jButton = jButton;
        this.row = row;
        this.column = column;
        this.move = null;
    }

    public CellStatus getValue() {
        return value;
    }

    /**
     *   Changes button's design to have an effect of the "pressed" button
     */
    public void colourTemp(Color colour, boolean isPressed){
        this.jButton.setBackground(colour);
        if (isPressed) {
            this.jButton.setBorderPainted(true);
            this.jButton.setBorder(new LineBorder(Color.RED));
        }
        else {
            this.jButton.setBorderPainted(false);
        }
    }

    /**
     *   This piece of code is used for the purpose of updating the status of a cell changing it to black if it is a dark cell,
     *   To white if it is a light cell,
     *   To the colour of the green background if it is empty
     *   Or to grey if a cell can be used for a potential move
     */
    public void setValue(CellStatus value) {
        switch (value) {
            case DARK -> this.jButton.setBackground(Color.BLACK);
            case LIGHT -> this.jButton.setBackground(Color.white);
            case EMPTY -> this.jButton.setBackground(new Color(820000));
            case GRAY -> this.jButton.setBackground(Color.GRAY);
        }
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public Move getMove() {
        return move;
    }

    public void setMove(Move move) {
        this.move = move;
    }

    /**
     * Checks whether there exists a legal move for the piece.
     * If such a move exists, returns true and adds information to the piece.
     * @param colour - colour of the current player
     * @param cells - current information about the board
     * @return whether move is possible for the piece. If this is the case, then possible moves are stored in Piece.
     */
    public boolean isLegal(CellStatus colour, Cell[][] cells){
        CellStatus opponent = colour == CellStatus.LIGHT ? CellStatus.DARK : CellStatus.LIGHT;
        int score = 0;
        ArrayList<DirectedMove> moves = new ArrayList<DirectedMove>();
        int[][] DIRS = {{-1,-1}, {-1,0}, {-1,1}, {0,1}, {0,-1}, {1,1}, {1,0}, {1,-1}};

        for (int[] dir : DIRS){
            Cell cell = null;
            int temp_score = 0;
            try{
                cell = IsOnBoard(this.getRow() + dir[0], this.getColumn() + dir[1]) ? cells[this.getRow() + dir[0]][this.getColumn() + dir[1]] : null;
            }
            catch(ArrayIndexOutOfBoundsException e){
                System.out.println("Out-of-Bounds error");
            }
            if(cell != null)
                if (cell != null
                        && cell.getValue() != CellStatus.EMPTY
                        && cell.getValue() == opponent) {
                    while (true) {
                        cell = IsOnBoard(cell.row + dir[0], cell.column + dir[1]) ? cells[cell.row + dir[0]][cell.column + dir[1]] : null;
                        temp_score += 1;
                        if ((cell != null && cell.getValue() == colour)) {
                            score += temp_score;
                            moves.add(new DirectedMove(cell, dir));
                        } else {
                            break;
                        }
                    }
                }

        }
        /**
         * This piece of code is responsible for returning valid moves
         */
        Move move = new Move(moves, score);
        this.setMove(move);
        return !moves.isEmpty();
    }

    /**
     * This section of code is used to determine whether a cell is within the boundaries of the board by comparing the coordinates of the cell to the dimensions of the board.
     *
     * @return true if the cell index is inside board boundaries
     */
    private boolean IsOnBoard(int row, int column){
        return 1 <= column &&  column < BOARD_SIZE && 0 <=row && row < BOARD_SIZE;
    }
}
