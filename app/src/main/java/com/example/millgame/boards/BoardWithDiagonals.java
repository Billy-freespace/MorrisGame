package com.example.millgame.boards;

import com.example.millgame.Board;
import com.example.millgame.MillGame;
import com.example.millgame.Piece;
import com.example.millgame.PieceRadar;
import com.example.millgame.exceptions.InvalidMill;
import com.example.millgame.exceptions.InvalidMillSize;
import com.example.millgame.logging.TraceLogger;
import com.example.millgame.misc.CardinalDirection;

import java.util.ArrayList;
import java.util.List;

public abstract class BoardWithDiagonals extends Board {

    public BoardWithDiagonals(MillGame.GameVariant variant){
        super(variant);
    }
    @Override
    public List<Board.Mill> getMills(Piece piece) {
        List<Board.Mill> mills = new ArrayList<Board.Mill>();
        PieceRadar radar = new PieceRadar(this);

        radar.map(piece);

        int northCount = radar.getCount(CardinalDirection.N);
        int southCount = radar.getCount(CardinalDirection.S);
        int eastCount = radar.getCount(CardinalDirection.E);
        int westCount = radar.getCount(CardinalDirection.W);

        if(northCount == 2){
            List<Piece> pieces = radar.getPieces(CardinalDirection.N);
            pieces.add(0, piece);
            Mill mill = createMill(pieces);
            if(mill != null){
                mills.add(mill);
            }
        }

        if(southCount == 2){
            List<Piece> pieces = radar.getPieces(CardinalDirection.S);
            pieces.add(piece);
            Mill mill = createMill(pieces);
            if(mill != null){
                mills.add(mill);
            }
        }

        if(eastCount == 2){
            List<Piece> pieces = radar.getPieces(CardinalDirection.E);
            pieces.add(0, piece);
            Mill mill = createMill(pieces);
            if(mill != null){
                mills.add(mill);
            }
        }

        if(westCount == 2){
            List<Piece> pieces = radar.getPieces(CardinalDirection.W);
            pieces.add(piece);
            Mill mill = createMill(pieces);
            if(mill != null){
                mills.add(mill);
            }
        }

        if(westCount + eastCount == 2){ // piece is the central piece of the mill
            List<Piece> pieces = new ArrayList<Piece>();

            pieces.addAll(radar.getPieces(CardinalDirection.W));
            pieces.add(piece);
            pieces.addAll(radar.getPieces(CardinalDirection.E));

            Mill mill = createMill(pieces);
            if(mill != null){
                mills.add(mill);
            }
        }

        if(northCount + southCount == 2){ // piece is the central piece of the mill
            List<Piece> pieces = new ArrayList<Piece>();

            pieces.addAll(radar.getPieces(CardinalDirection.S));
            pieces.add(piece);
            pieces.addAll(radar.getPieces(CardinalDirection.N));

            Mill mill = createMill(pieces);
            if(mill != null){
                mills.add(mill);
            }
        }

        // CHECK DIAGONAL (NW, NE, SW, SE)

        return mills;
    }

    @Override
    public Mill createMill(List<Piece> pieces) {
        Mill mill = null;
        try{
            mill = new Mill(pieces);
        } catch (InvalidMill | InvalidMillSize error){
            TraceLogger.log(error);
        }

        return mill;
    }

    public class Mill extends Board.Mill {

        public Mill(List<Piece> pieces) throws InvalidMill, InvalidMillSize {
            super(pieces);
        }

        @Override
        protected boolean isValid(List<Piece> pieces){
            return true;
        }
    }
}