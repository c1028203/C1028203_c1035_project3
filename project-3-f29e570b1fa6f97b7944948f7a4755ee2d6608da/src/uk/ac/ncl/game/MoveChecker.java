/**
 * @author Kostiantyn Potomkin
 * @version 0.9
 * @since 05-03-2020
 */
package uk.ac.ncl.game;
/**
 * The below section of code is used for the purpose of importing necessary libraries for the code
 */

import uk.ac.ncl.entity.CellStatus;
import uk.ac.ncl.entity.DirectedMove;
import uk.ac.ncl.entity.Cell;
import java.util.ArrayList;
import static uk.ac.ncl.Constants.*;

/**
 *
 * Main logic of the game.
 *
 */
public class MoveChecker {

    private Cell[][] cells;

    public MoveChecker(Cell[][] cells) {
        this.cells = cells;
    }

    /**
     * Generates a move of the opponent. Returns a move with the biggest max_score.
     *
     * @param cellStatus - colour of the opponent
     * @return a piece to make a move
     */
    public Cell generateOpponent(CellStatus cellStatus) {
        ArrayList<Cell> potentialMoves = findPotentialMoves(cellStatus);
        Cell opponentsMove = cells[0][0];
        for (Cell cell : potentialMoves){
                opponentsMove = opponentsMove == null
                        || cell.getMove().getScore() > opponentsMove.getMove().getScore()
                        ? cell
                        : opponentsMove;
        }
        return opponentsMove;
    }

    /**
     * Flips pieces between selected piece in directions of valid moves.
     *
     * @param cell - piece we have just put on the board
     * @param colour - colour of the current player
     */
    public void flipPieces(Cell cell, CellStatus colour) {
        cell.colourTemp(colour == OPPONENTS_CELL_STATUS ? OPPONENTS_COLOUR : PLAYERS_COLOUR, true);
        for (DirectedMove move : cell.getMove().getMoves()) {
            int[] dir = move.getDirection();
            int d_row = cell.getRow();
            int d_col = cell.getColumn();

            while (d_col != move.getCell().getColumn() || d_row != move.getCell().getRow()) {
                this.cells[d_row][d_col].setValue(colour);
                d_row += dir[0];
                d_col += dir[1];
            }
        }
    }

    /**
     * Returns potential moves on the board for the specified colour
     *
     * @param colour - colour of the current player
     * @return pieces for which there exist valid moves
     */
    public ArrayList<Cell> findPotentialMoves(CellStatus colour) {
        ArrayList<Cell> potentialMoves = new ArrayList<Cell>();
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (Cell cell : this.cells[i]) {
                if (cell.getValue() == CellStatus.EMPTY){
                    if (cell.isLegal(colour, cells)){
                        potentialMoves.add(cell);
                    }
                }
            }
        }
        return potentialMoves;
    }

    /**
     * Updates information about pieces
     *
     * @param cells - pieces to update
     * @param colour - new colour
     */
    public void colourPieces(ArrayList<Cell> cells, CellStatus colour) {
        for (int i = 0; i < cells.size() - 1; i++){
            cells.get(i).setValue(colour);
        }
    }

    /**
     * Sums up results of the game
     *
     * @return string with the results of the game
     */
    public String getFinalScore(){
        int lights = 0;
        int darks = 0;

        for (int row = 0; row < BOARD_SIZE; row++) {
            for (Cell cell : this.cells[row++]) {
                if (cell.getValue() == CellStatus.DARK){
                    darks++;
                } else if (cell.getValue() == CellStatus.LIGHT){
                    lights++;
                }
            }
        }

        if (darks == lights)
            return "The game is over. It is a draw. Each player has " + darks + " pieces";
        String winner = darks <  lights ? "Dark" : "Light";
        return "The game is over. "+ winner + " has won with the result: Dark - " + darks + " Light - " + lights;
    }

    /**
     * Cleans up potential moves from the board cells
     *
     * @param grayCells - pieces with valid moves
     */
    public void removeMoves(ArrayList<Cell> grayCells){
        if (grayCells == null)
            return;
        for (Cell cell : grayCells) {
            cell.setMove(cell.getMove());
        }
    }
}
