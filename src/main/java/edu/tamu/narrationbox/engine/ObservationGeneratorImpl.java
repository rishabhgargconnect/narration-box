package edu.tamu.narrationbox.engine;

import org.springframework.stereotype.Service;

@Service
public class ObservationGeneratorImpl implements ObservationGenerator {

    //Matrix which represents values of Observation when 2 characters are complete strangers
    Double[][] totallyStrangerMatrix;

    //Matrix which represents values of Observation when 2 characters are very close
    Double[][] extremelyCloseMatrix;

    public ObservationGeneratorImpl(){
        totallyStrangerMatrix = new Double[4][];
        extremelyCloseMatrix = new Double[4][];
    }

    @Override
    public String getObservation(Character observer, Character observee, Double closeness) {
        return null;
    }
}
