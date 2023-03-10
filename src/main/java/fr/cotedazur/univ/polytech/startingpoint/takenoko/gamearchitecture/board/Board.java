package fr.cotedazur.univ.polytech.startingpoint.takenoko.gamearchitecture.board;


import fr.cotedazur.univ.polytech.startingpoint.takenoko.logger.LoggerSevere;
import fr.cotedazur.univ.polytech.startingpoint.takenoko.gamearchitecture.ElementOfTheBoard;
import fr.cotedazur.univ.polytech.startingpoint.takenoko.gamearchitecture.ElementOfTheBoardCheated;
import fr.cotedazur.univ.polytech.startingpoint.takenoko.gamearchitecture.crest.Crest;
import fr.cotedazur.univ.polytech.startingpoint.takenoko.gamearchitecture.crest.CrestGestionnary;
import fr.cotedazur.univ.polytech.startingpoint.takenoko.gamearchitecture.hexagonebox.enumBoxProperties.Special;
import fr.cotedazur.univ.polytech.startingpoint.takenoko.gamearchitecture.hexagonebox.enumBoxProperties.Color;
import fr.cotedazur.univ.polytech.startingpoint.takenoko.bot.Bot;
import fr.cotedazur.univ.polytech.startingpoint.takenoko.exception.crest.ImpossibleToPlaceIrrigationException;
import fr.cotedazur.univ.polytech.startingpoint.takenoko.gamearchitecture.hexagonebox.HexagoneBox;
import fr.cotedazur.univ.polytech.startingpoint.takenoko.gamearchitecture.hexagonebox.HexagoneBoxPlaced;
import fr.cotedazur.univ.polytech.startingpoint.takenoko.searching.RetrieveBoxIdWithParameters;

import java.util.*;


public class Board implements Cloneable {

    /**
     * True if all the box are by default irrigated, false if not
     */
    protected final boolean allIrrigated;

    /**
     * Return the number of box placed in the board
     * WARNING : the lake is counted in the number of box placed
     */
    protected int numberBoxPlaced ;
    
    /**
     * PlacedBox is a Hashmap that contain in key all the box's id already place in the board
     * and associate for each one the HexagoneBoxPlaced associated
     * Type :
     *      - Integer : id of the placed box
     *      - HexagoneBoxPlaced : the box associated
     */
    protected HashMap<Integer, HexagoneBoxPlaced> placedBox;
    
    /**
     * AvailableBox is a ArrayList that contain all the coodinate where a new box can be place :
     *      -> A new box can only place in a coordinate from this list.
     */
    protected ArrayList<int[]> AvailableBox;

    /**
     * Must be >0.
     * Use to the hashcode if different board are use
     */
    protected final int idOfTheBoard;
    protected int[] gardenerCoords;
    protected int[] pandaCoords;
    protected RetrieveBoxIdWithParameters retrieveBoxIdWithParameters;
    protected CrestGestionnary crestGestionnary;
    protected ElementOfTheBoard elementOfTheBoard;
    protected LoggerSevere loggerSevere;
    protected int numberOfPlayers;





    public Board(RetrieveBoxIdWithParameters retrieveBoxIdWithParameters, boolean allIrrigated, int id,ElementOfTheBoard elementOfTheBoard, int numberOfPlayers, LoggerSevere loggerSevere){
        this.idOfTheBoard = id;
        this.allIrrigated = allIrrigated;
        this.retrieveBoxIdWithParameters = retrieveBoxIdWithParameters;
        this.elementOfTheBoard = elementOfTheBoard;
        this.placedBox = new HashMap<>();
        this.crestGestionnary = new CrestGestionnary();
        this.AvailableBox = new ArrayList<>();
        this.gardenerCoords = new int[]{0,0,0};
        this.pandaCoords = new int[]{0,0,0};
        this.loggerSevere = loggerSevere;
        this.numberOfPlayers = numberOfPlayers;
        this.generateLac();
    }
    public Board(RetrieveBoxIdWithParameters retrieveBoxIdWithParameters, boolean allIrrigated, int id, int numberOfPlayers, LoggerSevere loggerSevere){
        this(retrieveBoxIdWithParameters,allIrrigated,id,new ElementOfTheBoard(loggerSevere), numberOfPlayers, loggerSevere);
    }
    public Board(RetrieveBoxIdWithParameters retrieveBoxIdWithParameters, int id, int numberOfPlayers, LoggerSevere loggerSevere){
        //TODO set allIrrigated to false when irrigation add to the game
        this(retrieveBoxIdWithParameters,false,id, numberOfPlayers, loggerSevere);
    }
    public Board(RetrieveBoxIdWithParameters retrieveBoxIdWithParameters, int id, ElementOfTheBoardCheated elementOfTheBoardCheated, int numberOfPlayers, LoggerSevere loggerSevere){
        //TODO set allIrrigated to false when irrigation add to the game
        this(retrieveBoxIdWithParameters,false,id,elementOfTheBoardCheated, numberOfPlayers, loggerSevere);
    }

    public boolean isAllIrrigated() {
        return allIrrigated;
    }
    public RetrieveBoxIdWithParameters getRetrieveBoxIdWithParameters() {
        return retrieveBoxIdWithParameters;
    }
    public CrestGestionnary getCrestGestionnary() {
        return crestGestionnary;
    }
    public boolean getAllIrrigated(){
        return this.allIrrigated;
    }
    public int[] getGardenerCoords() {
        return this.gardenerCoords;
    }
    public int[] getPandaCoords() {
        return this.pandaCoords;
    }
    public int getNumberBoxPlaced() {
        return numberBoxPlaced;
    }
    public HashMap<Integer, HexagoneBoxPlaced> getPlacedBox() {
        return placedBox;
    }
    public ArrayList<int[]> getAvailableBox(){
        return this.AvailableBox;
    }
    public int getIdOfTheBoard(){
        return this.idOfTheBoard;
    }
    public ElementOfTheBoard getElementOfTheBoard() {
        return elementOfTheBoard;
    }
    public LoggerSevere getLoggerSevere() { return loggerSevere; }
    public int getNumberOfPlayers(){
        return this.numberOfPlayers;
    }


    public ArrayList<Integer> getCrestGestionnaryAlreadyIrrigated(){
        return crestGestionnary.getAlreadyIrrigated();
    }
    public HexagoneBoxPlaced getBoxWithCoordinates(int[] coords) {
        return this.placedBox.get(HexagoneBox.generateID(coords));
    }
    public ArrayList<HexagoneBoxPlaced> getAllBoxPlaced() {
        return new ArrayList<>(this.placedBox.values());
    }
    public boolean isCoordinateInBoard(int[] Coord) {
        return this.placedBox.containsKey(HexagoneBox.generateID(Coord));
    }





    /**
     * Method use to :
     *      Add the HexagoneBox entered into the Hashmap PlacedBox.
     *      Update the ArrayList AvaiableBox with the new box avalaible
     *      Delete the new box add in AvailableBox.
     *      Add the new adjacent box from the box placed and any other box placed before in the ArrayList AvailableBox
     * @param box : the new Hexagone box to add to the board
     */
    public void addBox(HexagoneBoxPlaced box, Bot bot) {
        int[] coord = box.getCoordinates();
        int[] newCoord1, newCoord2;
        UpdateAvaiableBoxAndPlacedBox(box);
        for (int i=1;i<7;i++){
            int[] adjacentCoord = box.getAdjacentBoxOfIndex(i);
            if (isCoordinateInBoard(adjacentCoord)) {
                generateNewAdjacentBox(coord, adjacentCoord);
            }
        }
        crestGestionnary.launchUpdatingCrestWithAddingNewBox(box);
        box.launchIrrigationChecking();
        for (Crest crest : box.getListOfCrestAroundBox()){
            if (crestGestionnary.getListOfCrestIrrigated().contains(crest)){
                box.setIrrigate(true);
            }
        }
        bot.setLastBoxPlaced(box.getCoordinates());
    }
    public void placeIrrigation(Crest crest){
        try {
            crestGestionnary.placeIrrigation(crest,this.placedBox);
            crest.setIrrigated(true);
        } catch (ImpossibleToPlaceIrrigationException e) {
            System.err.println("\n  -> An error has occurred : " + e.getErrorTitle() + "\n");
            throw new RuntimeException(e);
        }
    }
    public void setPandaCoords(int[] newCoords, Bot bot) {
        this.pandaCoords = newCoords;
        HexagoneBoxPlaced box = getBoxWithCoordinates(newCoords);
        if (box.getSpecial()!=Special.PROTEGER && box.getHeightBamboo()>0) {
            Optional<Color> bambooEatedColor = box.eatBamboo();
            if (bambooEatedColor.isPresent()){
                bot.addBambooEaten(bambooEatedColor.get());
            }
            bot.setLastCoordPanda(newCoords);
        }
    }
    public void setGardenerCoords(int[] coords, Bot bot) {
        this.gardenerCoords = coords;
        growAfterMoveOfTheGardener(getBoxWithCoordinates(coords));
        bot.setLastCoordGardener(coords);
    }





    /**
     * Method use to :
     *      generate the adjacent box of 2 box (that are put as parameters in this method)
     *      check if the nex box can be added in the ArrayList AvailableBox
     * @param coord the coordinate of a box from which we want the new adjacent box
     * @param adjacentCoord the coordinate of the second box in order to get the adjacent box
     */
    private void generateNewAdjacentBox(int[] coord, int[] adjacentCoord) {
        int[] newCoord1;
        int[] newCoord2;
        //look for every adjacent box to the one we are placing in the board
        int x = coord[0], y = coord[1], z = coord[2];
        int x1 = adjacentCoord[0], y1 = adjacentCoord[1], z1 = adjacentCoord[2];
        
        if (x==x1) {
            newCoord1 = new int[]{x+1,Math.min(y,y1),Math.min(z,z1)};
            newCoord2 = new int[]{x-1,Math.max(y,y1),Math.max(z,z1)};
        }
        else if (y==y1) {
            newCoord1 = new int[]{Math.min(x,x1),y+1,Math.min(z,z1)};
            newCoord2 = new int[]{Math.max(x,x1),y-1,Math.max(z,z1)};
        }
        else {
            newCoord1 = new int[]{Math.min(x,x1),Math.min(y,y1),z+1};
            newCoord2 = new int[]{Math.max(x,x1),Math.max(y,y1),z-1};
        }
        if (!placedBox.containsKey(HexagoneBox.generateID(newCoord1))){
            addNewBoxInAvailableBox(newCoord1);
        }
        if (!placedBox.containsKey(HexagoneBox.generateID(newCoord2))){
            addNewBoxInAvailableBox(newCoord2);
        }
    }

    /**
     * Method use to add in AvailableBox a box if it is possible
     * @param newCoord the coordinate of the box we want to add in AvailableBox
     */
    private void addNewBoxInAvailableBox(int[] newCoord) {
        if (!(isCoordinateInBoard(newCoord)) && !(AvailableBox.contains(newCoord))) {
            if (getSommeAbsolu(newCoord)<8){
                AvailableBox.add(newCoord);
            }
        }
    }

    /**
     * Method use to calculate the absolute add of the coordinate,
     * in order to limitate the range to the lake
     * @param coord
     * @return
     */
    private int getSommeAbsolu(int[] coord){
        int sommeAbsolu = 0;
        for (int i : coord){
            if (i<0) i = i*-1;
            sommeAbsolu = sommeAbsolu + i;
        }
        return sommeAbsolu;
    }

    /**
     * Method use to grow the bamboo in the adjacentBox of the one where the gardener is placed
     * @param box where the gardener is placed
     */
    private void growAfterMoveOfTheGardener(HexagoneBoxPlaced box){
        if (box.isIrrigate() &&
                !Arrays.equals(box.getCoordinates(), new int[]{0,0,0}) &&
                box.getHeightBamboo()<4) box.growBamboo();
        HashMap<Integer, int[]> adjacentBox = box.getAdjacentBox();
        for (int[] coordinate : adjacentBox.values()) {
            if (isCoordinateInBoard(coordinate)) {
                HexagoneBoxPlaced newBox = getBoxWithCoordinates(coordinate);
                if (this.placedBox.containsKey(newBox.getId()) &&
                        newBox.isIrrigate() &&
                        newBox.getColor() == box.getColor() &&
                        newBox.getHeightBamboo() < 4) newBox.growBamboo();

            }
        }
    }

    public void growAfterRain(HexagoneBoxPlaced box){
        if (box.isIrrigate() &&
                !Arrays.equals(box.getCoordinates(), new int[]{0,0,0}) &&
                box.getHeightBamboo()<4) box.growBamboo();
    }

    /**
     * Check if the number of box placed is equals to 2
     * (correspond to the case when the players add the first HexagoneBox to the booard (the first is the lake)
     * Then remove in the Hasmap AvailableBox the box that we just place now and add the id of this new box into the Hasmap PlacedBox
     * @param box : box that we place in the board.
     */
    private void UpdateAvaiableBoxAndPlacedBox(HexagoneBoxPlaced box) {
        this.numberBoxPlaced = this.numberBoxPlaced +1;
        if (this.numberBoxPlaced == 2) {
            AvailableBox.clear();
        } else {
            AvailableBox.remove(box.getCoordinates());
        }
        placedBox.put(box.getId(),box);
    }

    /**
     * Method use to initialize the lake and thus the board
     */
    private void generateLac(){
        HexagoneBoxPlaced lac = new HexagoneBoxPlaced(0,0,0, Color.LAC, Special.CLASSIQUE, retrieveBoxIdWithParameters,this);
        this.numberBoxPlaced = 1;
        for (int i=1;i<7;i++){
            this.AvailableBox.add(lac.getAdjacentBoxOfIndex(i));
        }
        this.placedBox.put(lac.getId(),lac);
        crestGestionnary.launchUpdatingCrestWithAddingNewBox(lac);
        lac.launchIrrigationChecking();
        lac.setIrrigate(true);
    }



    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
