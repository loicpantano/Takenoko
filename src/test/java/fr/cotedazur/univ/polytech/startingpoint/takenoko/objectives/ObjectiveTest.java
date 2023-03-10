package fr.cotedazur.univ.polytech.startingpoint.takenoko.objectives;

import fr.cotedazur.univ.polytech.startingpoint.takenoko.logger.LoggerError;
import fr.cotedazur.univ.polytech.startingpoint.takenoko.logger.LoggerSevere;
import fr.cotedazur.univ.polytech.startingpoint.takenoko.gamearchitecture.hexagonebox.enumBoxProperties.Color;
import fr.cotedazur.univ.polytech.startingpoint.takenoko.gamearchitecture.board.Board;
import fr.cotedazur.univ.polytech.startingpoint.takenoko.searching.RetrieveBoxIdWithParameters;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class ObjectiveTest {
    private static GestionObjectives gestionnaire;
    private static Board board;
    private static RetrieveBoxIdWithParameters retrieveBoxIdWithParameters;
    private static Objective objective1;
    private static Objective objective2;
    private static Objective objective3;
    private static Objective objective4;
    private static Objective objective5;
    private static Objective objective6;

    @BeforeAll
    public static void setUp(){
        retrieveBoxIdWithParameters = new RetrieveBoxIdWithParameters();
        board = new Board(retrieveBoxIdWithParameters, 1, 2,new LoggerSevere(true));
        gestionnaire = new GestionObjectives(board, retrieveBoxIdWithParameters,new LoggerError(true));
        objective1 = gestionnaire.poserTriangleVert;
        objective2 = gestionnaire.poserLigneJaune;
        objective3 = gestionnaire.planterSurProtegerBambouVert;
        objective4 = gestionnaire.planterSurEngraisBambouVert;
        objective5 = gestionnaire.poserLosangeRougeJaune;
        objective6 = gestionnaire.mangerDeuxJaunes1;}

    @Test
    void getValue() {
        assertEquals(2, objective1.getValue());
        assertEquals(3, objective2.getValue());
        assertEquals(4,objective3.getValue());
        assertEquals(3,objective4.getValue());
        assertEquals(5, objective5.getValue());
        assertEquals(4,objective6.getValue());
    }

    @Test
    void getPattern() {
        assertEquals(gestionnaire.poserTriangle,objective1.getPattern());
        assertEquals(gestionnaire.poserLigne,objective2.getPattern());
        assertEquals(gestionnaire.planterSurProteger,objective3.getPattern());
        assertEquals(gestionnaire.planterSurEngrais,objective4.getPattern());
        assertEquals(gestionnaire.poserLosange, objective5.getPattern());
        assertEquals(gestionnaire.mangerDeuxBambous, objective6.getPattern());
    }

    @Test
    void getColors() {
        assertEquals(new ArrayList<>(Arrays.asList( Color.VERT)), objective1.getColors());
        assertEquals(new ArrayList<>(Arrays.asList( Color.JAUNE)), objective2.getColors());
        assertEquals(new ArrayList<>(Arrays.asList( Color.VERT)), objective3.getColors());
        assertEquals(new ArrayList<>(Arrays.asList( Color.VERT)), objective4.getColors());
        assertEquals(new ArrayList<>(Arrays.asList( Color.ROUGE, Color.JAUNE)), objective5.getColors());
        assertEquals(new ArrayList<>(Arrays.asList( Color.JAUNE)), objective6.getColors());
    }

    @Test
    void getType() {
        assertEquals(TypeObjective.PARCELLE, objective1.getType());
        assertEquals(TypeObjective.PARCELLE, objective2.getType());
        assertEquals(TypeObjective.JARDINIER, objective3.getType());
        assertEquals(TypeObjective.JARDINIER, objective4.getType());
        assertEquals(TypeObjective.PARCELLE, objective5.getType());
        assertEquals(TypeObjective.PANDA, objective6.getType());
    }

    @Test
    void testToString() {
        assertEquals("Objectif : POSER_TRIANGLE_VERT, Valeur : 2", objective1.toString());
        assertEquals("Objectif : POSER_LIGNE_JAUNE, Valeur : 3", objective2.toString());
        assertEquals("Objectif : PLANTER_SUR_PROTEGER_BAMBOU_VERT, Valeur : 4", objective3.toString());
        assertEquals("Objectif : PLANTER_SUR_ENGRAIS_BAMBOU_VERT, Valeur : 3", objective4.toString());
        assertEquals("Objectif : POSER_LOSANGE_ROUGE_JAUNE, Valeur : 5", objective5.toString());
        assertEquals("Objectif : MANGER_DEUX_JAUNES_1, Valeur : 4", objective6.toString());
    }
}