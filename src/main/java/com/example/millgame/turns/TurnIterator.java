package com.example.millgame.turns;

import com.example.millgame.Player;
import com.example.millgame.logging.TraceLogger;

import javax.swing.event.EventListenerList;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;
import java.util.logging.Level;

public class TurnIterator extends CircularIterator<Player> {

    private EventListenerList turnListeners;

    public TurnIterator(boolean random) {
        super(new ArrayList<Player>(), random);
        turnListeners = new EventListenerList();
    }

    public TurnIterator(List<Player> collection, boolean random) {
        super(collection, random);
        turnListeners = new EventListenerList();
    }

    @Override
    public Player next(){
        Player player = super.next();

        ActionEvent event = new ActionEvent(player, -1, "Active Player");
        fireTurnListeners(event);

        TraceLogger.log(Level.INFO, "Active turn: " + player);
        return player;
    }

    public void addPlayer(Player player){
        addIterationState(player);
    }

    public void addTurnListener(EventListener turnListener){
        turnListeners.add(EventListener.class, turnListener);
    }

    private void fireTurnListeners(ActionEvent actionEvent){
        for(ActionListener listener : turnListeners.getListeners(ActionListener.class)){
            listener.actionPerformed(actionEvent);
        }
    }
}
