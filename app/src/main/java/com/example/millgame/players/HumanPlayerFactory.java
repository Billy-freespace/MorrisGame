package com.example.millgame.players;

import com.example.millgame.Board;
import com.example.millgame.Piece;
import com.example.millgame.Player;

import com.example.millgame.pieces.PieceColor;

public class HumanPlayerFactory implements PlayerFactory{
    public Player create(PieceColor color, Board board){return null;}
    public Player createByLevel(PieceColor color, Board board, PlayerLevel level){return null;}
}
