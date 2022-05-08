package com.example.millgame;

import com.example.millgame.actions.EventAction;

import javax.swing.*;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

public class Position extends JButton {
    public static ImageIcon NORMAL_ICON = new ImageIcon("textures/nmm_point-normal.png");
    public static ImageIcon PRESSED_ICON = new ImageIcon("textures/nmm_point-pressed.png");

    private char xLabel;
    private int yLabel;
    private Piece piece;
    private ArrayList<Position> neighbours;
    public boolean mark = false;

    private EventAction eventAction = null;

    public Position(char xLabel, int yLabel) {
        super(xLabel + Integer.toString(yLabel), Position.NORMAL_ICON);
        this.xLabel = xLabel;
        this.yLabel = yLabel;
        neighbours = new ArrayList<Position>();
        piece = null;
        mark = false;

        PropertyChangeListener eventActionChangeListener = new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
                String property = propertyChangeEvent.getPropertyName();
                if(property.equals("eventAction")){
                    for(Position position : neighbours){
                        if(!position.mark){
                            position.setEventAction(eventAction);
                        }
                    }
                }
            }
        };

        addPropertyChangeListener(eventActionChangeListener);
    }

    public Position(Point point, char xLabel, int yLabel){}
    public void addNeighbour(Position position){
        neighbours.add(position);
    }
    public ArrayList<Position> getNeighbours(){
        return neighbours;
    }
    public void setPiece(Piece piece) {
        this.piece = piece;
    }
    public Piece getPiece() {
        return piece;
    }

    public boolean hasPiece(){ return !(piece == null) ; }
    public void setPoint(Point point){}
    public void setMark(boolean value){}

    public char getXLabel(){ return xLabel; }
    public int getYLabel(){ return yLabel; }

    public void setEventAction(EventAction eventAction){
        this.eventAction = eventAction;
    }
}
