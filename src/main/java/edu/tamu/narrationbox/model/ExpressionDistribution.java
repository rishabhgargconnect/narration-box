package edu.tamu.narrationbox.model;

import javafx.util.Pair;
import lombok.Data;

import java.util.List;

@Data
public class ExpressionDistribution {
    private List<Pair<Double, String>> expressiveDistributionVector;
    private List<Pair<Double, String>> unExpressiveDistributionVector;
}
