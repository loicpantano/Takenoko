package fr.cotedazur.univ.polytech.startingpoint.Takenoko.gameArchitecture;


import fr.cotedazur.univ.polytech.startingpoint.Takenoko.allInterface.Color;

import java.util.*;


public class CrestGestionnary {

    private HashMap<Integer,ArrayList<Crest>> rangeFromIrrigated;
    private HashMap<Crest,Integer> rangeFromIrrigatedReversed;
    private HashMap<Crest, ArrayList<Crest>> linkCrestChildrenToCrestParent;
    private HashMap<Crest, ArrayList<Crest>> linkCrestParentToCrestChildren;
    private ArrayList<Crest> parentChildless; // parent with no children
    private ArrayList<HexagoneBox> alreadyIrrigated; // for the hexagoneBox not place but that are
                                                    // already irrigated if they were placed
    private ArrayList<Crest> listOfCrestOneRangeToIrrigated;

    public CrestGestionnary(){
        this.linkCrestParentToCrestChildren = new HashMap<>();
        this.linkCrestChildrenToCrestParent = new HashMap<>();
        this.rangeFromIrrigatedReversed = new HashMap<>();
        this.parentChildless = new ArrayList<>();
        this.alreadyIrrigated = new ArrayList<>();
        this.listOfCrestOneRangeToIrrigated = new ArrayList<>();
    }

    public ArrayList<HexagoneBox> getAlreadyIrrigated() {
        return alreadyIrrigated;
    }

    public void launchUpdatingCrestWithAddingNewBox(HexagoneBox box){
        this.updateCrestVariableWithNewBoxAdded(box);
    }


    /**
     * TODO put parent value to 0, to do in the method that call this one
     * Method use to change the genealogy of Crest issue from the parent that has been change to irrigated
     * Method recursive :
     *      - call itself until we reach the end of a genealogy branch
     * @param parent : the new Irrigation that has been placed
     */
    private void rewriteRangeToIrrigatedAfterNewIrrigation(Crest parent){
        if (linkCrestParentToCrestChildren.containsKey(parent) && !linkCrestParentToCrestChildren.get(parent).isEmpty()){
            ArrayList<Crest> children = this.linkCrestParentToCrestChildren.get(parent);
            for (int i = 0; i<children.size();i++){
                Crest child = children.get(i);
                int candidateNewRange = rangeFromIrrigatedReversed.get(parent)+1;
                updateChildRangeIfLessOrEqualsThanBefore(parent, child, candidateNewRange);
                rewriteRangeToIrrigatedAfterNewIrrigation(child);
            }
        }
    }

    /**
     * Method use to change the child Crest if the potential new range to Irrigation is less or equals than before :
     *      - if equals :
     *              - add the parent to the list of parent in linkCrestChildrenToCrestParent
     *      - if less :
     *              - check if the parent range is 0 (he is irrigated) :
     *                      - add the child to the list of the next crest that can be irrigated
     *              - change rangeFromIrrigatedReversed with the new range
     *              - change linkCrestChildrenToCrestParent with only this parent associated
     * @param parent : the Crest parent
     * @param child : the Crest child
     * @param candidateNewValue : the new range that may be added to the child if the condition are passed
     */
    private void updateChildRangeIfLessOrEqualsThanBefore(Crest parent, Crest child, int candidateNewValue) {
        if (candidateNewValue <this.rangeFromIrrigatedReversed.get(child)){
            if (rangeFromIrrigatedReversed.get(parent) == 0 && !this.listOfCrestOneRangeToIrrigated.contains(child)){ //second condition should never be false
                this.listOfCrestOneRangeToIrrigated.add(parent);
            }
            this.rangeFromIrrigatedReversed.put(child, candidateNewValue);
            this.linkCrestChildrenToCrestParent.put(child, new ArrayList<Crest>(Arrays.asList(parent)));
        } else if (candidateNewValue == this.rangeFromIrrigatedReversed.get(child)){
            ArrayList<Crest> listOfParent = this.linkCrestChildrenToCrestParent.get(child);
            listOfParent.add(parent);
            this.linkCrestChildrenToCrestParent.put(child,listOfParent);
        }
    }


    /**
     * Method use to add to the different Hashmap about Crest the last Crest add thanks to the newBox placed.
     * In order to do that :
     *      - The method add new Crest from the parent crest with their children not Implemented
     *      until the new list of Crest is Implemented
     *      - The Hashmap to link the parent to the children is updated
     *      - The Hashmap to link the Crest to its range to the irrigation is updated
     *
     * @param newCrestToImplement : the listOfCrest link to the last box add
     *                            and that have to be implemented in the different Hashmap of Crest
     */
    private void actualizeCrestVariable(ArrayList<Crest> newCrestToImplement){
        Set<Crest> allCrestImplemented = this.linkCrestParentToCrestChildren.keySet();
        boolean doesAllTheNewCrestToImplementAreExisting = !this.linkCrestParentToCrestChildren.keySet().containsAll(newCrestToImplement);
        while (doesAllTheNewCrestToImplementAreExisting){
            ArrayList<Crest> newParentChildless = new ArrayList<>();
            for(int i = 0; i< this.parentChildless.size(); i++){
                newParentChildless = createAndImplementTheChildCrestOfTheParent(allCrestImplemented, newParentChildless, i);
            }
            this.parentChildless = newParentChildless;
        }
    }

    private ArrayList<Crest> createAndImplementTheChildCrestOfTheParent(Set<Crest> allCrestImplemented, ArrayList<Crest> newParentChildless, int i) {
        Crest parent = this.parentChildless.get(i);
        ArrayList<Crest> listOfChildrenForParent = new ArrayList<>();
        for (int k = 0;k<parent.getListOfCrestChildren().size();k++){
            ArrayList<Integer> listOfParametersOfChild = parent.getListOfCrestChildren().get(k);
            Crest child = new Crest(listOfParametersOfChild.get(0),listOfParametersOfChild.get(1),listOfParametersOfChild.get(2));
            listOfChildrenForParent.add(child);
            newParentChildless = makeImplementationNeededForChildCrest(allCrestImplemented, newParentChildless, parent, listOfChildrenForParent, child);
        }
        this.linkCrestParentToCrestChildren.put(parent,listOfChildrenForParent);
        return newParentChildless;
    }

    private ArrayList<Crest> makeImplementationNeededForChildCrest(Set<Crest> allCrestImplemented, ArrayList<Crest> newParentChildless, Crest parent, ArrayList<Crest> listOfChildrenForParent, Crest child) {
        if (allCrestImplemented.contains(child)){
            int candidateNewValue = this.rangeFromIrrigatedReversed.get(parent)+1;
            updateChildRangeIfLessOrEqualsThanBefore(parent, child, candidateNewValue);
            listOfChildrenForParent.remove(child);
        } else {
            this.linkCrestChildrenToCrestParent.put(child,new ArrayList<>(Arrays.asList(parent)));
            int rangeOfParent = this.rangeFromIrrigatedReversed.get(parent);
            this.rangeFromIrrigatedReversed.put(child,rangeOfParent + 1);
            this.linkCrestParentToCrestChildren.put(child,new ArrayList<>());
            newParentChildless.add(child);
        }
        return newParentChildless;
    }

    private void updateCrestVariableWithNewBoxAdded(HexagoneBox box){
        if (box.getColor() == Color.Lac){
            for (int i=0;i<box.getListOfCrestAroundBox().size();i++){
                linkCrestParentToCrestChildren.put(box.getListOfCrestAroundBox().get(i), new ArrayList<>());
                parentChildless.add(box.getListOfCrestAroundBox().get(i));
                listOfCrestOneRangeToIrrigated.add(box.getListOfCrestAroundBox().get(i));
            }
        } else {
            actualizeCrestVariable(box.getListOfCrestAroundBox());
        }
    }


    /**
     * Ne gère pas le cas où une des 2 tuiles n'est pas placé
     * @param crest
     */
    public void placeIrrigation(Crest crest, HashMap<Integer,HexagoneBox> placedBox){
        if (this.listOfCrestOneRangeToIrrigated.contains(crest)){
            crest.setIrrigated(true);
            this.rangeFromIrrigatedReversed.put(crest,0);
            this.listOfCrestOneRangeToIrrigated.remove(crest);
            rewriteRangeToIrrigatedAfterNewIrrigation(crest);
            for (int i = 0; i<2;i++) {
                if (placedBox.containsKey(crest.getIdOfAdjacentBox()[i])) {
                    placedBox.get(crest.getIdOfAdjacentBox()[i]).setIrrigate(true);
                } else {
                    this.alreadyIrrigated.add(placedBox.get(crest.getIdOfAdjacentBox()[i]));
                }
            }
        } else {
            // TODO throw an error because impossible to place an Irrigation where there is no box
        }

    }
}
