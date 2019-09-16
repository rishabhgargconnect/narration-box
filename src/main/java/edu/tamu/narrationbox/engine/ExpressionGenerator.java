package edu.tamu.narrationbox.engine;

public interface ExpressionGenerator {
    String getExpressionFromState(String stateValue, Double expressiveness);
}
