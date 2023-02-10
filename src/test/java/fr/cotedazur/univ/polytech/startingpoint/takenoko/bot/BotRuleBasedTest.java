package fr.cotedazur.univ.polytech.startingpoint.takenoko.bot;

import fr.cotedazur.univ.polytech.startingpoint.takenoko.Logger.LogInfoDemo;
import fr.cotedazur.univ.polytech.startingpoint.takenoko.Logger.LoggerError;
import fr.cotedazur.univ.polytech.startingpoint.takenoko.Logger.LoggerSevere;
import fr.cotedazur.univ.polytech.startingpoint.takenoko.MeteoDice;
import fr.cotedazur.univ.polytech.startingpoint.takenoko.gameArchitecture.board.Board;
import fr.cotedazur.univ.polytech.startingpoint.takenoko.gameArchitecture.hexagoneBox.HexagoneBoxPlaced;
import fr.cotedazur.univ.polytech.startingpoint.takenoko.gameArchitecture.hexagoneBox.enumBoxProperties.Color;
import fr.cotedazur.univ.polytech.startingpoint.takenoko.gameArchitecture.hexagoneBox.enumBoxProperties.Special;
import fr.cotedazur.univ.polytech.startingpoint.takenoko.objectives.GestionObjectives;
import fr.cotedazur.univ.polytech.startingpoint.takenoko.searching.RetrieveBoxIdWithParameters;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

class BotRuleBasedTest {
    BotRuleBased botRB;
    Board board;
    Random r;
    MeteoDice meteoDice;
    RetrieveBoxIdWithParameters retrieveBoxIdWithParameters;
    final String arg = "demo";

    GestionObjectives gestionObjectives;
    private static LogInfoDemo logInfoDemo;

    @BeforeEach
    void setUp() {
        this.retrieveBoxIdWithParameters = new RetrieveBoxIdWithParameters();
        board = new Board(retrieveBoxIdWithParameters, 1, 2, new LoggerSevere(true));
        gestionObjectives = new GestionObjectives(board, retrieveBoxIdWithParameters, new LoggerError(true));
        gestionObjectives.initialize(
                gestionObjectives.ListOfObjectiveParcelleByDefault(),
                gestionObjectives.ListOfObjectiveJardinierByDefault(),
                gestionObjectives.ListOfObjectivePandaByDefault());
        r = mock(Random.class);
        meteoDice = mock(MeteoDice.class);
        botRB = new BotRuleBased("testBot", board, r, gestionObjectives, retrieveBoxIdWithParameters, new HashMap<Color, Integer>(), logInfoDemo);
        botRB.logInfoDemo = new LogInfoDemo(false);
    }

    @Test
    void choseMoveForPandaWithoutObjective() {
        //tous les bambous de hauteur 0 sauf 1, le panda doit y aller
        HexagoneBoxPlaced hexagoneBoxPlaced = new HexagoneBoxPlaced(1, -1, 0, Color.Jaune, Special.Classique, retrieveBoxIdWithParameters, board);
        HexagoneBoxPlaced hexagoneBoxPlaced2 = new HexagoneBoxPlaced(1, 0, -1, Color.Rouge, Special.Classique, retrieveBoxIdWithParameters, board);
        HexagoneBoxPlaced hexagoneBoxPlaced3 = new HexagoneBoxPlaced(0, 1, -1, Color.Jaune, Special.Classique, retrieveBoxIdWithParameters, board);
        HexagoneBoxPlaced hexagoneBoxPlaced4 = new HexagoneBoxPlaced(-1, 1, 0, Color.Vert, Special.Classique, retrieveBoxIdWithParameters, board);
        HexagoneBoxPlaced hexagoneBoxPlaced5 = new HexagoneBoxPlaced(-1, 0, 1, Color.Rouge, Special.Classique, retrieveBoxIdWithParameters, board);
        HexagoneBoxPlaced hexagoneBoxPlaced6 = new HexagoneBoxPlaced(0, -1, 1, Color.Jaune, Special.Classique, retrieveBoxIdWithParameters, board);
        board.addBox(hexagoneBoxPlaced);
        board.addBox(hexagoneBoxPlaced2);
        board.addBox(hexagoneBoxPlaced3);
        board.addBox(hexagoneBoxPlaced4);
        board.addBox(hexagoneBoxPlaced5);
        board.addBox(hexagoneBoxPlaced6);
        hexagoneBoxPlaced.setHeightBamboo(0);
        hexagoneBoxPlaced2.setHeightBamboo(0);
        hexagoneBoxPlaced3.setHeightBamboo(0);
        hexagoneBoxPlaced4.setHeightBamboo(0);
        hexagoneBoxPlaced6.setHeightBamboo(0);
        assertEquals(Arrays.toString((new int[]{-1, 0, 1})), Arrays.toString(botRB.choseMoveForPanda()));
    }

    @Test
    void choseMoveForPandaWithObjective() {
        //manger 2 verts, seule case verte : [-1, 1, 0], le panda doit y aller
        HexagoneBoxPlaced hexagoneBoxPlaced = new HexagoneBoxPlaced(1, -1, 0, Color.Jaune, Special.Classique, retrieveBoxIdWithParameters, board);
        HexagoneBoxPlaced hexagoneBoxPlaced2 = new HexagoneBoxPlaced(1, 0, -1, Color.Rouge, Special.Classique, retrieveBoxIdWithParameters, board);
        HexagoneBoxPlaced hexagoneBoxPlaced3 = new HexagoneBoxPlaced(0, 1, -1, Color.Jaune, Special.Classique, retrieveBoxIdWithParameters, board);
        HexagoneBoxPlaced hexagoneBoxPlaced4 = new HexagoneBoxPlaced(-1, 1, 0, Color.Vert, Special.Classique, retrieveBoxIdWithParameters, board);
        HexagoneBoxPlaced hexagoneBoxPlaced5 = new HexagoneBoxPlaced(-1, 0, 1, Color.Rouge, Special.Classique, retrieveBoxIdWithParameters, board);
        HexagoneBoxPlaced hexagoneBoxPlaced6 = new HexagoneBoxPlaced(0, -1, 1, Color.Jaune, Special.Classique, retrieveBoxIdWithParameters, board);
        board.addBox(hexagoneBoxPlaced);
        board.addBox(hexagoneBoxPlaced2);
        board.addBox(hexagoneBoxPlaced3);
        board.addBox(hexagoneBoxPlaced4);
        board.addBox(hexagoneBoxPlaced5);
        board.addBox(hexagoneBoxPlaced6);
        botRB.objectives.add(gestionObjectives.getPandaObjectifs().get(0));
        assertEquals(Arrays.toString((new int[]{-1, 1, 0})), Arrays.toString(botRB.choseMoveForPanda()));
    }

    @Test
    void movePandaStormTestOneColor() {
        HexagoneBoxPlaced hexagoneBoxPlaced = new HexagoneBoxPlaced(1, -1, 0, Color.Jaune, Special.Classique, retrieveBoxIdWithParameters, board);
        HexagoneBoxPlaced hexagoneBoxPlaced2 = new HexagoneBoxPlaced(1, 0, -1, Color.Rouge, Special.Classique, retrieveBoxIdWithParameters, board);
        HexagoneBoxPlaced hexagoneBoxPlaced3 = new HexagoneBoxPlaced(0, 1, -1, Color.Jaune, Special.Classique, retrieveBoxIdWithParameters, board);
        HexagoneBoxPlaced hexagoneBoxPlaced4 = new HexagoneBoxPlaced(-1, 1, 0, Color.Vert, Special.Classique, retrieveBoxIdWithParameters, board);
        HexagoneBoxPlaced hexagoneBoxPlaced5 = new HexagoneBoxPlaced(-1, 0, 1, Color.Rouge, Special.Classique, retrieveBoxIdWithParameters, board);
        HexagoneBoxPlaced hexagoneBoxPlaced6 = new HexagoneBoxPlaced(0, -1, 1, Color.Jaune, Special.Classique, retrieveBoxIdWithParameters, board);
        board.addBox(hexagoneBoxPlaced);
        board.addBox(hexagoneBoxPlaced2);
        board.addBox(hexagoneBoxPlaced3);
        board.addBox(hexagoneBoxPlaced4);
        board.addBox(hexagoneBoxPlaced5);
        board.addBox(hexagoneBoxPlaced6);
        //manger 2 verts
        botRB.objectives.add(gestionObjectives.getPandaObjectifs().get(0));
        botRB.movePandaStorm();
        assertEquals(0, hexagoneBoxPlaced4.getHeightBamboo());
    }

    @Test
    void movePandaStormTestTricolor() {
        HexagoneBoxPlaced hexagoneBoxPlaced = new HexagoneBoxPlaced(1, -1, 0, Color.Jaune, Special.Classique, retrieveBoxIdWithParameters, board);
        HexagoneBoxPlaced hexagoneBoxPlaced2 = new HexagoneBoxPlaced(1, 0, -1, Color.Jaune, Special.Classique, retrieveBoxIdWithParameters, board);
        HexagoneBoxPlaced hexagoneBoxPlaced3 = new HexagoneBoxPlaced(0, 1, -1, Color.Jaune, Special.Classique, retrieveBoxIdWithParameters, board);
        HexagoneBoxPlaced hexagoneBoxPlaced4 = new HexagoneBoxPlaced(-1, 1, 0, Color.Vert, Special.Classique, retrieveBoxIdWithParameters, board);
        HexagoneBoxPlaced hexagoneBoxPlaced5 = new HexagoneBoxPlaced(-1, 0, 1, Color.Rouge, Special.Classique, retrieveBoxIdWithParameters, board);
        HexagoneBoxPlaced hexagoneBoxPlaced6 = new HexagoneBoxPlaced(0, -1, 1, Color.Jaune, Special.Classique, retrieveBoxIdWithParameters, board);
        board.addBox(hexagoneBoxPlaced);
        board.addBox(hexagoneBoxPlaced2);
        board.addBox(hexagoneBoxPlaced3);
        board.addBox(hexagoneBoxPlaced4);
        board.addBox(hexagoneBoxPlaced5);
        board.addBox(hexagoneBoxPlaced6);
        //manger tricolore
        botRB.objectives.add(gestionObjectives.getPandaObjectifs().get(13));
        botRB.movePandaStorm();
        botRB.movePandaStorm();
        botRB.movePandaStorm();
        assertEquals(0, hexagoneBoxPlaced.getHeightBamboo());
        assertEquals(0, hexagoneBoxPlaced4.getHeightBamboo());
        assertEquals(0, hexagoneBoxPlaced5.getHeightBamboo());

    }

    @Test
    void growBambooRainTest() {
        HexagoneBoxPlaced hexagoneBoxPlaced = new HexagoneBoxPlaced(1, -1, 0, Color.Jaune, Special.Classique, retrieveBoxIdWithParameters, board);
        HexagoneBoxPlaced hexagoneBoxPlaced2 = new HexagoneBoxPlaced(1, 0, -1, Color.Rouge, Special.Classique, retrieveBoxIdWithParameters, board);
        HexagoneBoxPlaced hexagoneBoxPlaced3 = new HexagoneBoxPlaced(0, 1, -1, Color.Jaune, Special.Classique, retrieveBoxIdWithParameters, board);
        HexagoneBoxPlaced hexagoneBoxPlaced4 = new HexagoneBoxPlaced(-1, 1, 0, Color.Vert, Special.Classique, retrieveBoxIdWithParameters, board);
        HexagoneBoxPlaced hexagoneBoxPlaced5 = new HexagoneBoxPlaced(-1, 0, 1, Color.Rouge, Special.Classique, retrieveBoxIdWithParameters, board);
        HexagoneBoxPlaced hexagoneBoxPlaced6 = new HexagoneBoxPlaced(0, -1, 1, Color.Jaune, Special.Classique, retrieveBoxIdWithParameters, board);
        board.addBox(hexagoneBoxPlaced);
        board.addBox(hexagoneBoxPlaced2);
        board.addBox(hexagoneBoxPlaced3);
        board.addBox(hexagoneBoxPlaced4);
        board.addBox(hexagoneBoxPlaced5);
        board.addBox(hexagoneBoxPlaced6);
        hexagoneBoxPlaced5.setHeightBamboo(2);
        //planter 2 bambous rouges
        botRB.objectives.add(gestionObjectives.getJardinierObjectifs().get(12));
        botRB.growBambooRain(arg);
        assertEquals(3, hexagoneBoxPlaced5.getHeightBamboo());
        botRB.growBambooRain(arg);
        assertEquals(4, hexagoneBoxPlaced5.getHeightBamboo());
        botRB.growBambooRain(arg);
        assertEquals(2, hexagoneBoxPlaced2.getHeightBamboo());
    }

    @Test
    void moveGardener() {
        HexagoneBoxPlaced hexagoneBoxPlaced = new HexagoneBoxPlaced(1, -1, 0, Color.Jaune, Special.Classique, retrieveBoxIdWithParameters, board);
        HexagoneBoxPlaced hexagoneBoxPlaced2 = new HexagoneBoxPlaced(1, 0, -1, Color.Rouge, Special.Classique, retrieveBoxIdWithParameters, board);
        HexagoneBoxPlaced hexagoneBoxPlaced3 = new HexagoneBoxPlaced(0, 1, -1, Color.Jaune, Special.Classique, retrieveBoxIdWithParameters, board);
        HexagoneBoxPlaced hexagoneBoxPlaced4 = new HexagoneBoxPlaced(-1, 1, 0, Color.Vert, Special.Classique, retrieveBoxIdWithParameters, board);
        HexagoneBoxPlaced hexagoneBoxPlaced5 = new HexagoneBoxPlaced(-1, 0, 1, Color.Rouge, Special.Classique, retrieveBoxIdWithParameters, board);
        HexagoneBoxPlaced hexagoneBoxPlaced6 = new HexagoneBoxPlaced(0, -1, 1, Color.Jaune, Special.Classique, retrieveBoxIdWithParameters, board);
        board.addBox(hexagoneBoxPlaced);
        board.addBox(hexagoneBoxPlaced2);
        board.addBox(hexagoneBoxPlaced3);
        board.addBox(hexagoneBoxPlaced4);
        board.addBox(hexagoneBoxPlaced5);
        board.addBox(hexagoneBoxPlaced6);
        hexagoneBoxPlaced5.setHeightBamboo(2);
        //planter 2 bambous rouges
        botRB.objectives.add(gestionObjectives.getJardinierObjectifs().get(12));
        botRB.moveGardener(arg);
        assertEquals(3, hexagoneBoxPlaced5.getHeightBamboo());
        botRB.moveGardener(arg);
        assertEquals(2, hexagoneBoxPlaced2.getHeightBamboo());
        botRB.moveGardener(arg);
        assertEquals(4, hexagoneBoxPlaced5.getHeightBamboo());
    }

    @Test
    void launchActionTest() {
        HexagoneBoxPlaced hexagoneBoxPlaced = new HexagoneBoxPlaced(1, -1, 0, Color.Jaune, Special.Classique, retrieveBoxIdWithParameters, board);
        HexagoneBoxPlaced hexagoneBoxPlaced2 = new HexagoneBoxPlaced(1, 0, -1, Color.Rouge, Special.Classique, retrieveBoxIdWithParameters, board);
        HexagoneBoxPlaced hexagoneBoxPlaced3 = new HexagoneBoxPlaced(0, 1, -1, Color.Jaune, Special.Classique, retrieveBoxIdWithParameters, board);
        HexagoneBoxPlaced hexagoneBoxPlaced4 = new HexagoneBoxPlaced(-1, 1, 0, Color.Vert, Special.Classique, retrieveBoxIdWithParameters, board);
        HexagoneBoxPlaced hexagoneBoxPlaced5 = new HexagoneBoxPlaced(-1, 0, 1, Color.Rouge, Special.Classique, retrieveBoxIdWithParameters, board);
        HexagoneBoxPlaced hexagoneBoxPlaced6 = new HexagoneBoxPlaced(0, -1, 1, Color.Jaune, Special.Classique, retrieveBoxIdWithParameters, board);
        board.addBox(hexagoneBoxPlaced);
        board.addBox(hexagoneBoxPlaced2);
        board.addBox(hexagoneBoxPlaced3);
        board.addBox(hexagoneBoxPlaced4);
        board.addBox(hexagoneBoxPlaced5);
        board.addBox(hexagoneBoxPlaced6);
        hexagoneBoxPlaced5.setHeightBamboo(2);
        //planter 2 bambous rouges
        botRB.objectives.add(gestionObjectives.getJardinierObjectifs().get(12));
        //manger 2 verts
        botRB.objectives.add(gestionObjectives.getPandaObjectifs().get(0));
        //on suppose que sa main est pleine avec d'autres objectis parcelles
        botRB.objectivesInHand = 5;

        //le bot doit manger le bambou vert en [-1,1,0]
        botRB.launchAction("test");
        assertEquals(0, hexagoneBoxPlaced4.getHeightBamboo());
        //le bot doit faire pousser les bambous rouges
        botRB.launchAction("test");
        botRB.resetPossibleAction();
        assertEquals(3, hexagoneBoxPlaced5.getHeightBamboo());
        botRB.launchAction("test");;
        botRB.resetPossibleAction();
        assertEquals(2, hexagoneBoxPlaced2.getHeightBamboo());
        botRB.launchAction("test");;
        botRB.resetPossibleAction();
        assertEquals(4, hexagoneBoxPlaced5.getHeightBamboo());
        botRB.launchAction("test");
        botRB.resetPossibleAction();
        assertEquals(3, hexagoneBoxPlaced2.getHeightBamboo());
    }
}