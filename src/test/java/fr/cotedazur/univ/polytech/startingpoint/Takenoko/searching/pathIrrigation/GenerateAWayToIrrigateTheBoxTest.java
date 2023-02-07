package fr.cotedazur.univ.polytech.startingpoint.Takenoko.searching.pathIrrigation;

import fr.cotedazur.univ.polytech.startingpoint.Takenoko.exception.crest.CrestNotRegistered;
import fr.cotedazur.univ.polytech.startingpoint.Takenoko.gameArchitecture.board.Board;
import fr.cotedazur.univ.polytech.startingpoint.Takenoko.gameArchitecture.board.BoardSimulation;
import fr.cotedazur.univ.polytech.startingpoint.Takenoko.gameArchitecture.crest.Crest;
import fr.cotedazur.univ.polytech.startingpoint.Takenoko.gameArchitecture.hexagoneBox.HexagoneBoxPlaced;
import fr.cotedazur.univ.polytech.startingpoint.Takenoko.gameArchitecture.hexagoneBox.HexagoneBoxSimulation;
import fr.cotedazur.univ.polytech.startingpoint.Takenoko.gameArchitecture.hexagoneBox.enumBoxProperties.Color;
import fr.cotedazur.univ.polytech.startingpoint.Takenoko.gameArchitecture.hexagoneBox.enumBoxProperties.Special;
import fr.cotedazur.univ.polytech.startingpoint.Takenoko.searching.RetrieveBoxIdWithParameters;
import fr.cotedazur.univ.polytech.startingpoint.Takenoko.searching.RetrieveSimulation;
import fr.cotedazur.univ.polytech.startingpoint.Takenoko.searching.pathIrrigation.GenerateAWayToIrrigateTheBox;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class GenerateAWayToIrrigateTheBoxTest {

    private static RetrieveBoxIdWithParameters retrieveBoxIdWithParameters;
    private static Board board;
    private static GenerateAWayToIrrigateTheBox generateAWayToIrrigateTheBox;
    private static HexagoneBoxSimulation hexagoneBoxPlaced1;
    private static HexagoneBoxSimulation hexagoneBoxPlaced2;
    private static HexagoneBoxSimulation hexagoneBoxPlaced3;
    private static Crest crest1;
    private static Crest crest2;
    private static BoardSimulation boardSimulation;
    private static RetrieveSimulation retrieveSimulation;

    @BeforeAll
    @Order(1)
    public static void setup() throws CrestNotRegistered, CloneNotSupportedException {
        retrieveBoxIdWithParameters = new RetrieveBoxIdWithParameters();
        board = new Board(retrieveBoxIdWithParameters,false,1);
        boardSimulation = new BoardSimulation(board);
        retrieveSimulation = boardSimulation.getRetrieveBoxIdWithParameters();
        hexagoneBoxPlaced1 = new HexagoneBoxSimulation(-1,1,0, Color.Vert, Special.Classique,retrieveSimulation,boardSimulation);
        hexagoneBoxPlaced2 = new HexagoneBoxSimulation(0,1,-1, Color.Vert, Special.Classique,retrieveSimulation,boardSimulation);
        hexagoneBoxPlaced3 = new HexagoneBoxSimulation(-1,2,-1, Color.Vert, Special.Classique,retrieveSimulation,boardSimulation);
        board.addBox(hexagoneBoxPlaced1);
        board.addBox(hexagoneBoxPlaced2);
        board.addBox(hexagoneBoxPlaced3);
        crest1 = new Crest(-5,15,1);
        crest2 = new Crest(-10,15,3);
        generateAWayToIrrigateTheBox = new GenerateAWayToIrrigateTheBox(hexagoneBoxPlaced3);
    }

    private static Stream<Arguments> provideCheckPath(){
        return Stream.of(
                Arguments.of(false,generateAWayToIrrigateTheBox.getPathToIrrigation().get(0).get(0)),
                Arguments.of(true,generateAWayToIrrigateTheBox.getPathToIrrigation().get(1).get(0))
        );
    }

    private static Stream<Arguments> provideCheckClosest(){
        return Stream.of(
                Arguments.of(crest1,generateAWayToIrrigateTheBox.getClosestCrestToIrrigatedOfTheBox()),
                Arguments.of(crest2,generateAWayToIrrigateTheBox.getClosestCrestToIrrigatedOfTheBox())
        );
    }

    @ParameterizedTest
    @MethodSource("provideCheckPath")
    void checkPath(boolean bool, Crest crest) {
        board.placeIrrigation(crest);
        assertEquals(bool,hexagoneBoxPlaced3.isIrrigate());
    }

    @ParameterizedTest
    @MethodSource("provideCheckClosest")
    void checkClosest(Crest crest,ArrayList<Crest> listClosest) {
        assertTrue(listClosest.contains(crest));
        assertEquals(2,listClosest.size());
    }
}