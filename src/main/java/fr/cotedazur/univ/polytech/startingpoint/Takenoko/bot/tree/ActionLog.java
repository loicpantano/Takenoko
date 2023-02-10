package fr.cotedazur.univ.polytech.startingpoint.Takenoko.bot.MCTS;

import fr.cotedazur.univ.polytech.startingpoint.Takenoko.bot.PossibleActions;

/**
 * Class that represents an action and its parameters
 */
public class ActionLog {
    /**
     * The action
     */
    PossibleActions action;
    /**
     * The parameters of the action
     */
    int[] parameters;


    public ActionLog(PossibleActions action,int... parameters) {
        this.action = action;
        this.parameters = parameters;
    }

    public PossibleActions getAction() {
        return action;
    }

    public int[] getParameters() {
        return parameters;
    }

    public String toString(){
        StringBuilder s = new StringBuilder(action.toString());
        for (int parameter : parameters) {
            s.append(" ").append(parameter);
        }
        return s.toString();
    }

}