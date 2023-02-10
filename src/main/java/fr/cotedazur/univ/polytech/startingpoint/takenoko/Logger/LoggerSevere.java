package fr.cotedazur.univ.polytech.startingpoint.takenoko.Logger;

import fr.cotedazur.univ.polytech.startingpoint.takenoko.exception.TakenokoException;

import java.util.logging.Level;

public class LoggerSevere extends LoggerMain {

    public LoggerSevere(boolean IsOn) {
        super(Level.SEVERE, LogInfoStats.class.getName());
        if (!IsOn){
            this.setOff();
        }
    }

    public void logErrorTitle(TakenokoException e){
        super.addLog("\n  -> An error has occurred : " + e.getErrorTitle() + "\n");
    }
}