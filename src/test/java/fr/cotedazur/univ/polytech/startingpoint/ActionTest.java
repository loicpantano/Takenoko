package fr.cotedazur.univ.polytech.startingpoint;


import fr.cotedazur.univ.polytech.startingpoint.Takenoko.Interface.Action;
import org.junit.jupiter.api.Test;

import fr.cotedazur.univ.polytech.startingpoint.Takenoko.HexagoneBox;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ActionTest {
    @Test
    void drawTile() {
        HexagoneBox hexagoneBox = Action.drawTile();
        assertNotNull(hexagoneBox.getColor());
        assertNotNull(hexagoneBox.getSpecial());

    }
}
