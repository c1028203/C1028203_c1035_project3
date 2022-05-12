/**
 * @author Kostiantyn Potomkin
 * @version 0.9
 * @since 05-03-2020
 */
package uk.ac.ncl.entity;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.ArrayList;

import static uk.ac.ncl.Constants.BOARD_SIZE;

/**
 * Represents each cell of the playing board.
 */
public class Cell {

    /**
     * Current cell status.
     */
    private CellStatus value;
    /**
     * Links cell to the corresponding UI element.
     */
    private JButton jButton;
    /**
     * Cell's row
     */
    private int row;
    /**
     *  Cell's column
     */
    private int column;

    /**
     *  Potential moves of the piece
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
     *   Updates the status of the cell
     */
    public void setValue(CellStatus value) {
        value = this.value;
        switch (value){
            case EMPTY:
                this.jButton.setBackground(new Color(820000));
                break;
            case LIGHT:
                this.jButton.setBackground(Color.WHITE);
                break;
            case DARK:
                this.jButton.setBackground(Color.BLACK);
                break;
            case GRAY:
                this.jButton.setBackground(Color.GRAY);
                break;
            default:
                break;
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
            int temp_score = 0;
            Cell cell = IsOnBoard(this.getRow() + dir[1], this.getColumn() + dir[1]) ? cells[this.getRow() + dir[1]][this.getColumn() + dir[1]] : null;
            if (cell != null
                    && cell.getValue() != CellStatus.EMPTY
                    && cell.getValue() == opponent) {
                while (true){
                    cell = IsOnBoard(cell.row + dir[0],cell.column + dir[1]) ? cells[cell.row + dir[0]][cell.column + dir[1]] :  null;
                    temp_score += 1;
                    if (!(cell != null
                            && cell.getValue() != CellStatus.EMPTY)){
                        if (cell.getValue() == colour) {
                            score += temp_score;
                            moves.add(new DirectedMove(cell, dir));
                        }
                    } else {
                        break;
                    }
                }
            }
        }

        Move move = new Move(moves, score);
        this.setMove(move);
        return !moves.isEmpty();
    }

    /**
     * Checks whether the cell is on board.
     *
     * @return true if the cell index is inside board boundaries
     */
    private boolean IsOnBoard(int row, int column){
        return 1 <= column &&  column < BOARD_SIZE && 0 <=row && row < BOARD_SIZE;
    }
}
