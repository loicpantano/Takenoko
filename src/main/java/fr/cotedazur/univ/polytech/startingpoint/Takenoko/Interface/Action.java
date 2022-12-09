package fr.cotedazur.univ.polytech.startingpoint.Takenoko.Interface;
import fr.cotedazur.univ.polytech.startingpoint.Takenoko.HexagoneBox;
import main.java.fr.cotedazur.univ.polytech.startingpoint.Takenoko.Interface.Color;
import main.java.fr.cotedazur.univ.polytech.startingpoint.Takenoko.Interface.Special;
import java.util.concurrent.ThreadLocalRandom;

public class Action {

    public static HexagoneBox drawTile() {
        int random = ThreadLocalRandom.current().nextInt(0,3);
        Color color = switch (random) {
            case 1 -> Color.Vert;
            case 2 -> Color.Rouge;
            default -> Color.Jaune;
        };
        return new HexagoneBox(0,0,0,color,Special.Classic);
    }

}