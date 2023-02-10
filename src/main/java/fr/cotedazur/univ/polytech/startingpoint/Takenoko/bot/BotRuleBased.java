package fr.cotedazur.univ.polytech.startingpoint.Takenoko.bot;

import fr.cotedazur.univ.polytech.startingpoint.Takenoko.Logger.LogInfoDemo;
import fr.cotedazur.univ.polytech.startingpoint.Takenoko.MeteoDice;
import fr.cotedazur.univ.polytech.startingpoint.Takenoko.gameArchitecture.board.Board;
import fr.cotedazur.univ.polytech.startingpoint.Takenoko.gameArchitecture.crest.Crest;
import fr.cotedazur.univ.polytech.startingpoint.Takenoko.gameArchitecture.hexagoneBox.HexagoneBox;
import fr.cotedazur.univ.polytech.startingpoint.Takenoko.gameArchitecture.hexagoneBox.HexagoneBoxPlaced;
import fr.cotedazur.univ.polytech.startingpoint.Takenoko.gameArchitecture.hexagoneBox.enumBoxProperties.Color;
import fr.cotedazur.univ.polytech.startingpoint.Takenoko.objectives.GestionObjectives;
import fr.cotedazur.univ.polytech.startingpoint.Takenoko.objectives.Objective;
import fr.cotedazur.univ.polytech.startingpoint.Takenoko.objectives.TypeObjective;
import fr.cotedazur.univ.polytech.startingpoint.Takenoko.searching.RetrieveBoxIdWithParameters;
import fr.cotedazur.univ.polytech.startingpoint.Takenoko.searching.pathIrrigation.GenerateAWayToIrrigateTheBox;

import java.util.*;
//TODO : changer car class action n'existe plus'

public class BotRuleBased extends BotRandom {

    int currentScore;
    int irrigationInHand;
    int objectivesInHand;


    public BotRuleBased(String name, Board board, Random random, GestionObjectives gestionObjectives, RetrieveBoxIdWithParameters retrieveBoxIdWithParameters, HashMap<Color,Integer> bambooEated, LogInfoDemo logInfoDemo) {
        super(name, board, random, gestionObjectives, retrieveBoxIdWithParameters, bambooEated,logInfoDemo);
        this.irrigationInHand = 0;
        this.objectivesInHand = 0;
        this.currentScore = 0;
    }

    @Override
    public void playTurn(MeteoDice.Meteo meteo, String arg) {
        possibleActions = PossibleActions.getAllActions();
        this.objectives = getObjectives();
        switch (meteo) {
            case VENT -> {
                //2 fois la même action possible
                logInfoDemo.addLog("Le dé a choisi : VENT");
                launchAction(arg);
                resetPossibleAction();
                launchAction(arg);
            }
            case PLUIE -> {
                //peut faire pousser sur une parcelle irriguée
                logInfoDemo.addLog("Le dé a choisi : PLUIE");
                growBambooRain(arg);
                launchAction(arg);
                launchAction(arg);
            }
            case NUAGES -> {
                //peut prendre un aménagement
                logInfoDemo.addLog("Le dé a choisi : NUAGES");
                placeAugment(arg);
                launchAction(arg);
                launchAction(arg);
            }
            case ORAGE -> {
                //peut placer le panda n'importe où et manger un bambou
                logInfoDemo.addLog("Le dé a choisi : ORAGE");
                movePandaStorm();
                launchAction(arg);
                launchAction(arg);
            }
            case SOLEIL -> {
                //3 actions possible
                logInfoDemo.addLog("Le dé à choisi : SOLEIL");
                launchAction(arg);
                launchAction(arg);
                launchAction(arg);
            }
            default -> {
                // hasard "?"
                logInfoDemo.addLog("Le dé a choisi : HASARD");
                placeAugment(arg);
                launchAction(arg);
                launchAction(arg);

            }
        }
        if (this.getScore() > this.currentScore) {
            this.currentScore = this.getScore();
            this.objectivesInHand--;
        }
        resetPossibleAction();
    }

    protected void launchAction(String arg) {
        if (this.possibleActions.contains(PossibleActions.DRAW_OBJECTIVE) && objectivesInHand < 5 && !(gestionObjectives.getParcelleObjectifs().isEmpty() || gestionObjectives.getJardinierObjectifs().isEmpty() || gestionObjectives.getPandaObjectifs().isEmpty())) {
            drawObjective(arg);
            this.possibleActions.remove(PossibleActions.DRAW_OBJECTIVE);
        }
        else if (choseMoveForPanda().length == 0 || !(this.possibleActions.contains(PossibleActions.MOVE_PANDA))) {
            doAction(arg);
        } else {
            movePanda(arg);
            this.possibleActions.remove(PossibleActions.MOVE_PANDA);
        }
    }

    protected PossibleActions chooseAction() {
        PossibleActions acp = possibleActions.get(random.nextInt(possibleActions.size()));
        //Check if the action is possible
        if (isObjectiveIllegal(acp))
            return chooseAction();
        possibleActions.remove(acp);
        return acp;
    }

    protected List<HexagoneBoxPlaced> hexagoneBoxWithBamboos() {
        List<HexagoneBoxPlaced> hexagoneBoxWithBamboos = new ArrayList<>();
        for (HexagoneBoxPlaced box : this.board.getAllBoxPlaced()) {
            if (box.getHeightBamboo() != 0) {
                hexagoneBoxWithBamboos.add(box);
            }
        }
        return hexagoneBoxWithBamboos;
    }

    protected int[] choseMoveForPanda() {
        List<HexagoneBoxPlaced> hexagoneBoxWithBamboos = hexagoneBoxWithBamboos();
        List<int[]> possibleMoves = Bot.possibleMoveForGardenerOrPanda(this.board, board.getPandaCoords());
        for (int[] possiblePandaCoords : possibleMoves) {
            for (HexagoneBoxPlaced boxWithBamboos : hexagoneBoxWithBamboos) {
                if (Arrays.equals(possiblePandaCoords, boxWithBamboos.getCoordinates())) {
                    return possiblePandaCoords;
                }
            }
        }
        return new int[0];
    }

    protected void doAction(String arg) {
        PossibleActions action = chooseAction();
        logInfoDemo.displayTextAction(action);
        switch (action) {
            case DRAW_AND_PUT_TILE -> {
                logInfoDemo.addLog("Le bot a choisi : PiocherPoserTuile");
                placeTile(arg);
            }
            case MOVE_GARDENER -> {
                logInfoDemo.addLog("Le bot a choisi : BougerJardinier");
                moveGardener(arg);
            }
            case DRAW_OBJECTIVE -> {
                logInfoDemo.addLog("Le bot a choisi : PiocherObjectif");
                drawObjective(arg);
            }
            case TAKE_IRRIGATION -> {
                logInfoDemo.addLog("Le bot a choisi : PrendreIrrigation");
                this.nbIrrigation++;
                placeIrrigation();
            }
            case MOVE_PANDA -> {
                movePanda(arg);
            }
            default -> {
            }
        }
    }

    @Override
    protected void placeTile(String arg){
        //Init
        List<HexagoneBox> list = new ArrayList<>();
        //Get all the available coords
        List<int[]> availableTilesList = board.getAvailableBox().stream().toList();
        //Draw three tiles
        for(int i = 0; i < 3; i++)
            list.add(board.getElementOfTheBoard().getStackOfBox().getFirstBox());
        //Choose a random tile from the tiles drawn
        int placedTileIndex = random.nextInt(0, 3);
        HexagoneBox tileToPlace = list.get(placedTileIndex);
        //Choose a random available space
        int[] placedTileCoords = availableTilesList.get(random.nextInt(0, availableTilesList.size()));
        //Set the coords of the tile
        HexagoneBoxPlaced placedTile = new HexagoneBoxPlaced(placedTileCoords[0],placedTileCoords[1],placedTileCoords[2],tileToPlace,retrieveBoxIdWithParameters,board);
        //Add the tile to the board
        board.addBox(placedTile);
        logInfoDemo.addLog(this.name + " a placé une tuile " + tileToPlace.getColor() + " en " + Arrays.toString(placedTile.getCoordinates()));
        board.getElementOfTheBoard().getStackOfBox().addNewBox(list.get((placedTileIndex + 1) % 3));
        board.getElementOfTheBoard().getStackOfBox().addNewBox(list.get((placedTileIndex + 2) % 3));
    }

    @Override
    protected void moveGardener(String arg){
        List<int[]> possibleMoves = Bot.possibleMoveForGardenerOrPanda(board, board.getGardenerCoords());
        board.setGardenerCoords(possibleMoves.get(random.nextInt(0, possibleMoves.size())));
        logInfoDemo.addLog(this.name + " a déplacé le jardinier en " + Arrays.toString(board.getGardenerCoords()));
    }

    @Override
    public void drawObjective(String arg){
        int i = random.nextInt(0, 3);
        gestionObjectives.rollObjective(this, arg, i);
        this.objectivesInHand++;
        this.possibleActions.remove(PossibleActions.DRAW_OBJECTIVE);
    }




    @Override
    public void movePanda(String arg){
        if (choseMoveForPanda().length != 0) {
            board.setPandaCoords(choseMoveForPanda(),this);
        }
        else {
            List<int[]> possibleMoves = Bot.possibleMoveForGardenerOrPanda(board, board.getPandaCoords());
            board.setPandaCoords(possibleMoves.get(random.nextInt(0, possibleMoves.size())),this);
        }
        logInfoDemo.displayMovementPanda(arg,board);
    }


    protected void placeIrrigation() {
        if(random.nextInt(0,2) == 0) {
            List<GenerateAWayToIrrigateTheBox> tmp = new ArrayList<>();
            GenerateAWayToIrrigateTheBox temp;
            for (HexagoneBoxPlaced box : board.getPlacedBox().values()) {
                if (!box.isIrrigate()) {
                    try {
                        temp = new GenerateAWayToIrrigateTheBox(box);
                        if (temp.getPathToIrrigation().size() <= this.nbIrrigation)
                            tmp.add(temp);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            if(!tmp.isEmpty()) {
                temp = tmp.get(random.nextInt(0, tmp.size()));
                for (ArrayList<Crest> path : temp.getPathToIrrigation()) {
                    logInfoDemo.addLog("Le bot a placé une irrigation en " + Arrays.toString(path.get(0).getCoordinates()));
                    board.placeIrrigation(path.get(0));
                    nbIrrigation--;
                }
            }
        }
    }

    protected ArrayList<Objective> getObjectivesOfType(TypeObjective typeObj) {
        ArrayList<Objective> objectivesOfType = new ArrayList<>();
        for (Objective obj : this.objectives) {
            if (obj.getType().equals(typeObj)) {
                objectivesOfType.add(obj);
            }
        }
        return objectivesOfType;
    }


    public void movePandaStorm() {
        List<Objective> pandaObjectives = getObjectivesOfType(TypeObjective.PANDA);
        List<HexagoneBoxPlaced> boxWithBamboos = hexagoneBoxWithBamboos();
        if (!pandaObjectives.isEmpty()) {
            for (HexagoneBoxPlaced box : boxWithBamboos) {
                for (Objective obj : pandaObjectives) {
                    for (Color c : obj.getColors()) {
                        if (box.getColor().equals(c) && box.getHeightBamboo() > 0 && this.bambooEaten.get(c) < obj.getPattern().getHauteurBambou()) {
                            board.setPandaCoords(box.getCoordinates(), this);
                            return;
                        }
                    }
                }
            }
        }
        else if (!boxWithBamboos.isEmpty()) {
            board.setPandaCoords(boxWithBamboos.get(random.nextInt(0, boxWithBamboos.size())).getCoordinates(),this);
        }
    }

    public HexagoneBoxPlaced getHighestBamboosOfColor(Color color) {
        int highest = 0;
        HexagoneBoxPlaced highestBox = null;
        for (HexagoneBoxPlaced box : this.board.getAllBoxPlaced()) {
            if (box.getColor().equals(color) && box.getHeightBamboo() < 4 && box.getHeightBamboo() > highest) {
                highest = box.getHeightBamboo();
                highestBox = box;
            }
        }
        return highestBox;
    }

    @Override
    public void growBambooRain(String arg) {
        List<Objective> gardenerObj = getObjectivesOfType(TypeObjective.JARDINIER);
        for (Objective obj : gardenerObj) {
            for (Color c : obj.getColors()) {
                HexagoneBoxPlaced box = getHighestBamboosOfColor(c);
                if (box != null) {
                    box.growBamboo();
                    return;
                }
            }
        }
        List<HexagoneBoxPlaced> allBox = this.board.getAllBoxPlaced();
        if (!allBox.isEmpty()) {
            HexagoneBoxPlaced box = allBox.get(random.nextInt(0,allBox.size()));
            box.growBamboo();
        }
    }

}

