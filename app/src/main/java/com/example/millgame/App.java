package com.example.millgame;

import java.awt.*;

import com.example.millgame.logging.TraceLogger;
import com.example.millgame.logging.TraceMode;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.impl.Arguments;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;


public class App {
    public static ArgumentParser getCmdParser(){
        ArgumentParser parser = ArgumentParsers.newFor("MillGame").build()
                .defaultHelp(true)
                .description("Mill Game - Debug options");
        parser.addArgument("-D", "--debug")
                .action(Arguments.storeTrue())
                .help("Enable console logger");
        parser.addArgument("--log-file")
                .dest("logfile")
                .setDefault("millgame.log")
                .help("Output logging file (default location: app/millgame.log)");
        parser.addArgument("-n", "--logger-name")
                .dest("traceLoggerName")
                .setDefault("millgame")
                .help("Trace logger name");

        String verboseHelp = "Verbose mode\n\n" +
                "Verbose Level (Number of 'v's):\n" +
                "* 1: curious mode - traced logging levels: WARNING, SEVERE\n" +
                "* 2: developer mode - traced logging levels: CONFIG, INFO, WARNING, SEVERE\n" +
                "* 3: paranoid mode - traced logging levels: ALL";

        parser.addArgument("-v")
                .action(Arguments.count())
                .setDefault(0)
                .dest("verbose")
                .type(Integer.class)
                .help(verboseHelp);

        return parser;
    }

    public static void main(String[] args){
        Runnable runner = new Runnable(){
            @Override
            public void run(){
                ArgumentParser parser = getCmdParser();

                try{
                    Namespace ns = parser.parseArgs(args);

                    String logfile = ns.getString("logfile");
                    boolean debug = ns.getBoolean("debug");

                    TraceMode traceMode;
                    int verboseLevel = ns.getInt("verbose");
                    switch (verboseLevel){
                        case 2:
                            traceMode = TraceMode.DEVELOPER;
                            break;
                        case 3:
                            traceMode = TraceMode.PARANOID;
                            break;
                        default:
                            traceMode = TraceMode.CURIOUS;
                            break;
                    }

                    String name = ns.getString("traceLoggerName");
                    TraceLogger traceLogger = TraceLogger.initTraceLogger(name, logfile, debug);
                    traceLogger.setTraceMode(traceMode);


                    GameGUI gameGUI = new GameGUI();

                } catch (ArgumentParserException error){
                    parser.handleError(error);
                    System.exit(1);
                }catch (Exception error){
                    System.err.println(error.getMessage());
                    System.exit(1);
                }
            }
        };

        EventQueue.invokeLater(runner);
    }
}