package edu.tamu.narrationbox.engine;

public interface ObservationGenerator {
    public String getObservation(Character observer, Character observee, Double closeness);
}
