package fr.cotedazur.univ.polytech.startingpoint;

import fr.cotedazur.univ.polytech.startingpoint.Takenoko.Board;
import fr.cotedazur.univ.polytech.startingpoint.Takenoko.HexagoneBox;
import fr.cotedazur.univ.polytech.startingpoint.Takenoko.Objectifs.GestionObjectifs;
import fr.cotedazur.univ.polytech.startingpoint.Takenoko.bot.*;
import fr.cotedazur.univ.polytech.startingpoint.Takenoko.*;
import fr.cotedazur.univ.polytech.startingpoint.Takenoko.bot.Action;
import fr.cotedazur.univ.polytech.startingpoint.Takenoko.bot.Bot;

import java.util.*;

public class Main {

    public static String hello() {
        return "Hello World!";
    }

    public static void printBoardState(Board board) {
        System.out.println("Voici l'état du board : ");
        ArrayList<HexagoneBox> placedBox = board.getPlacedBox();
        for (HexagoneBox box : placedBox) {
            System.out.println(Arrays.toString(box.getCoordinates()) + " : bamboo de hauteur " + box.getHeightBamboo());
        }
        System.out.println(" ");
    }

    public static void main(String... args) {
        RetrieveBoxIdWithParameters retrieving = new RetrieveBoxIdWithParameters();
        UniqueObjectCreated.setRetrieveBoxIdWithParameters(retrieving);
        Board board = new Board();
        Random random = new Random();
        ElementOfTheGame elementOfTheGame = new ElementOfTheGame();
        UniqueObjectCreated.setElementOfTheGame(elementOfTheGame);
        UniqueObjectCreated.setBoard(board);
        GestionObjectifs gestionnaire = new GestionObjectifs();
        gestionnaire.initialize();
        MeteoDice meteoDice = new MeteoDice();
        Bot bot1 = new BotRandom("Bot1",board,random, meteoDice,gestionnaire);
        Bot bot2 = new BotRandom("Bot2",board,random, meteoDice,gestionnaire);
        System.out.println("Que la partie commence !");
        boolean playing = true;
        int turn = 0;


        while (playing) {
            meteoDice.roll();
            if (turn == 0) {
                if(gestionnaire.checkIfBotCanDrawAnObjective(bot1)){
                    gestionnaire.rollObjective(bot1);
                }
                gestionnaire.checkObjectives(bot1);
                bot1.playTurn();
            }
            else {
                if(gestionnaire.checkIfBotCanDrawAnObjective(bot2)){
                    gestionnaire.rollObjective(bot2);
                }
                gestionnaire.checkObjectives(bot2);
                bot2.playTurn();
            }
            turn = 1 - turn;
            printBoardState(board);
            if (board.getNumberBoxPlaced() > 10) {
                playing = false;
            }
            System.out.println("------------------------------------------");
        }
    }


}
