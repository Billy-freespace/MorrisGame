package com.example.millgame;

import com.example.millgame.boards.BoardPanel;
import com.example.millgame.logging.TraceLogger;
import com.example.millgame.misc.Constants;

import javax.swing.*;
import java.awt.*;
import java.util.logging.Level;

public class GameGUIMinimal extends JFrame {
    /*
     * Minimal GUI version of game (only GamePanel)
     */

    private BoardPanel boardPanel;

    private Color defaultColor = new Color(128, 64, 32);

    public GameGUIMinimal(){
        super();
        //TraceLogger.log(Level.INFO, "Initializing GameGUIMinimal");
        setTitle(Constants.title);
        Container mainPanel = getContentPane();
        mainPanel.setPreferredSize(new Dimension(Constants.WIDTH, Constants.HEIGHT));
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    public GameGUIMinimal setGame(MillGame game){
        boardPanel = game.getBoardPanel();
        boardPanel.setBackground(defaultColor);
        Container mainPanel = getContentPane();
        mainPanel.add(boardPanel, BorderLayout.CENTER);
        return this;
    }

    public GameGUIMinimal setBoardBackground(Color color){
        boardPanel.setBackground(color);
        return this;
    }
}