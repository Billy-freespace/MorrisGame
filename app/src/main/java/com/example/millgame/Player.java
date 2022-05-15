package com.example.millgame;

import com.example.millgame.exceptions.InvalidPositionCoordinate;
import com.example.millgame.exceptions.NoEmptyPosition;
import com.example.millgame.exceptions.NoPiecesError;
import com.example.millgame.exceptions.RankedException;
import com.example.millgame.logging.TraceLogger;
import com.example.millgame.players.PlayerType;
import com.example.millgame.pieces.PieceColor;
import com.example.millgame.pieces.PieceFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public abstract class Player {
    private List<Piece> pieces;
    public int npieces;
    private int placedPieces;

    private PlayerType playerType;

    private PieceColor pieceColor;
    protected MillGame game;

    private Board board;


    public Player(PlayerType playerType, PieceColor color, MillGame game) {
        this.game = game;
        board = game.getBoard();

        npieces = board.getNumberPlayerPieces();
        this.playerType = playerType;
        pieceColor = color;
        pieces = new ArrayList<Piece>(); // no pieces were placed to board
        placedPieces = 0;
    }

    public Player(PlayerType playerType, PieceColor color, Board board){
        game = null;
        this.board = board;
        npieces = board.getNumberPlayerPieces();
        this.playerType = playerType;
        pieceColor = color;
        pieces = new ArrayList<Piece>(); 
        placedPieces = 0;
    }

    public int getPlacedPieces(){ return placedPieces; }

    public void placePiece(char x, int y) throws NoEmptyPosition, NoPiecesError, InvalidPositionCoordinate {
        // raise NoPiecesError exception if the player
        // has no piece to positioning in the POSITIONING game stage
        //System.out.println(toString());
        /*if(false) {
            throw new InvalidPositionCoordinate(x, y);
        }*/

        if(placedPieces >= board.getNumberPlayerPieces()) {
            throw new NoPiecesError(pieceColor, MillGame.GameStage.POSITIONING, Level.WARNING);
        }

        Piece piece = PieceFactory.create(pieceColor);
        board.placePiece(piece, x, y);
        pieces.add(piece);
        placedPieces += 1;

        TraceLogger.log(Level.INFO, this + " placed a piece in (" + x + ", " + y + ") position");
    }

    public void placePiece(Position position) throws NoEmptyPosition, NoPiecesError, InvalidPositionCoordinate {
        // raise NoPiecesError exception if the player
        // has no piece to positioning in the POSITIONING game stage
        char xLabel = position.getXLabel();
        int yLabel = position.getYLabel();

        placePiece(xLabel, yLabel);
    }

    public void movePiece(Piece piece, char x, int y) throws RankedException{ // sprint 2 - RECHECK
        
         //CODIGO DE PRUEBA
        Position position = piece.getPosition();
        board.removePiece(piece);
        System.out.println("Piexa origen 01: " + piece.getPosition());
        board.placePiece(piece, x, y);
        System.out.println("Piexa origen: 02 " + piece.getPosition());
        //FIN DE PRUEBA

    }

    public void movePiece(Piece piece, Position position) throws RankedException{ // sprint 2 - RECHECK
/*
        char xLabel = position.getXLabel();
        int yLabel = position.getYLabel();

        movePiece(piece, xLabel, yLabel);
 */
    }
    public void removePiece(Piece piece) throws InvalidPositionCoordinate{ // sprint 2 - RECHECK

        /*Position position = piece.getPosition();
        board.removePiece(piece, position.getXLabel(), position.getYLabel());
        pieces.remove(piece);*/
 
    }
    public List<Mill> getMills(){ return null; } //sprint 2
    public Piece getPiece(char x, char y) throws InvalidPositionCoordinate {
        Position position = board.getPosition(x, y);
        Piece piece = null;

        if(position != null){
            piece = position.getPiece();
        }

        return piece;
    }

    @Override
    public String toString() {
        String out = "Player(color:" + pieceColor + ", type: " + playerType +
                ", placedPieces: " + placedPieces + ", boardPieces: " + pieces.size() + ")";
        return out;
    }
}