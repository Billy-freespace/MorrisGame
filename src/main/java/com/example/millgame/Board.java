package com.example.millgame;

import com.example.millgame.exceptions.InvalidPositionCoordinate;
import com.example.millgame.exceptions.NotEmptyPosition;
import com.example.millgame.boards.BoardDimension;
import com.example.millgame.exceptions.*;
import com.example.millgame.misc.Color;
import java.util.*;

public abstract class Board implements BoardDimension {
    protected Position origin;
    protected Map<Character, Map<Integer, Position>> positions;
    public final BoardVariant variant;
    protected Map<Color, List<Mill>> mills;

    protected PieceRadar radar;

    public Board (BoardVariant variant) {
        this.variant = variant;
        this.positions = new HashMap<Character, Map<Integer, Position>>();
        this.origin = null;

        mills =  new HashMap<Color, List<Mill>>();

        for(Color color : Color.values()){
            mills.put(color, new ArrayList<Mill>());
        }
    }

    public ArrayList<Position> getEmptyPositions(){
        ArrayList<Position> emptyPositions = new ArrayList<Position>();
        Position position;

        for(Character x : positions.keySet()){
            Map<Integer, Position> inner = positions.get(x);
            for(Integer y : inner.keySet()){
                position = inner.get(y);

                if(!position.hasPiece()){
                    emptyPositions.add(position);
                }
            }
        }

        return emptyPositions;
    }

    public void placePiece(Piece piece, char x, int y)
            throws NotEmptyPosition, InvalidPositionCoordinate {
        Position position = null;
        
        position = this.getPosition(x, y);

        Piece positionPiece = position.getPiece();

        if(positionPiece != null) {
            throw new NotEmptyPosition(x, y);
        }
        position.setPiece(piece);
        piece.setPosition(position);

        List<Mill> pieceMills = getMills(piece);
        List<Mill> colorMills = mills.get(piece.getColor());
        colorMills.addAll(pieceMills);
    }

    public void removePiece(char x, int y) throws InvalidPositionCoordinate, EmptyPositionError{
        Position position = getPosition(x, y);

        Piece piece = position.getPiece();
        if(piece == null){
            throw new EmptyPositionError(position);
        }

        // remove all mills where piece belongs
        List<Mill> colorMills = mills.get(piece.getColor());
        ArrayList<Mill> colorMillsCopy = new ArrayList<Mill>(colorMills); // making a copy
        for(Mill mill : colorMillsCopy){
            if(mill.hasPiece(piece)){
                colorMills.remove(mill);
            }
        }

        piece.setPosition(null);
        position.setPiece(null);
    }

    public List<Mill> getMills(Color color){ return mills.get(color); }
    public abstract List<Mill> getMills(Piece piece);
    public BoardVariant getVariant() {
        return variant;
    }

    public Position getPosition(char x, int y) throws InvalidPositionCoordinate {
        if(!positions.containsKey(x)){
            throw new InvalidPositionCoordinate(x, y);
        }

        Map<Integer, Position> inner = positions.get(x);
        if(!inner.containsKey(y)){
            throw new InvalidPositionCoordinate(x, y);
        }

        return inner.get(y);
    }

    public int countPieces(Color color){
        int count = 0;

        for(Character x : positions.keySet()){
            Map<Integer, Position> inner = positions.get(x);
            for(int y : positions.keySet()){
                Position position = inner.get(y);
                Piece piece = position.getPiece();
                if(piece != null && piece.getColor() == color){
                    count += 1;
                }
            }
        }

        return count;
    }

    public int countPositions(){
        int count =0;
        for(Map<Integer, Position> inner : positions.values()){
            count += inner.size();
        }

        return count;
    }

    public void setOrigin(Position origin){ this.origin = origin;}
    public Position getOrigin(){ return origin; }
    public void unmark(){
        for(Character xLabel : positions.keySet()){
            Map<Integer, Position> inner = positions.get(xLabel);
            for(Integer yLabel : inner.keySet()){
                Position position = inner.get(yLabel);
                position.mark = false;
            }
        }
    } // unmark all positions of board


    public void addPosition(Position position){
        char xLabel = position.getXLabel();
        int yLabel = position.getYLabel();

        if(!positions.containsKey(xLabel)){
            positions.put(xLabel, new HashMap<Integer, Position>());
        }

        Map<Integer, Position> inner = positions.get(xLabel);
        inner.put(yLabel, position);
    }

    public abstract Mill createMill(List<Piece> pieces);

    /*
     * inner classes
     */
    public abstract class Mill {
        private List<Piece> pieces;
        private Color color;

        public Mill(List<Piece> pieces) throws InvalidMill, InvalidMillSize, InvalidMillColor {
            if(pieces.size() != 3){
                throw  new InvalidMillSize(pieces);
            }

            color = pieces.get(0).getColor();

            for(Piece piece : pieces){
                if(piece.getColor() != color){
                    throw new InvalidMillColor(pieces);
                }
            }

            if(!isValid(pieces)){
                throw new InvalidMill(pieces);
            }
            this.pieces = pieces;
        }

        protected abstract boolean isValid(List<Piece> pieces);
        //public void addPiece(Piece piece) { pieces.add(piece); }
        public boolean hasPiece (Piece piece) { return pieces.contains(piece); }

        @Override
        public String toString() {
            String out="";

            out += "Mill(color=" + color + ", Positions=[";
            for(Piece piece : pieces){
                out += piece.getPosition() + ", ";
            }
            out += "])";

            return out;
        }
    }

    /*
     * Inner enumerations
     */

    public enum BoardVariant {
        THREE_MEN_MORRIS,
        SIX_MEN_MORRIS,
        SEVEN_MEN_MORRIS,
        NINE_MEN_MORRIS,
        TWELVE_MEN_MORRIS
    }
}