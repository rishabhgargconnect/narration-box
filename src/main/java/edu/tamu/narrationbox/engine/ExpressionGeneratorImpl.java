package edu.tamu.narrationbox.engine;

import edu.tamu.narrationbox.model.ExpressionDistribution;
import javafx.util.Pair;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ExpressionGeneratorImpl implements ExpressionGenerator {

    private Map<String, ExpressionDistribution> mapExpressionDistribution;

    @Autowired
    private MathComponent mathComponent;

    public ExpressionGeneratorImpl(){

        mapExpressionDistribution = new HashMap<>();

        //Build tree for Anger
        ExpressionDistribution angerDistribution = new ExpressionDistribution();
        angerDistribution.setE(0.7);

        //Expressive Distribution List
        List<Pair<Double, String>> expressiveDistributionList = new ArrayList<>();
        expressiveDistributionList.add(new Pair<>(0.5, "anger"));
        expressiveDistributionList.add(new Pair<>(0.5, "rage"));
        angerDistribution.setExpressiveDistributionVector(expressiveDistributionList);

        //UnExpressive Distribution List
        List<Pair<Double, String>> unExpressiveDistributionList = new ArrayList<>();
        unExpressiveDistributionList.add(new Pair<>(0.3, "subtle anger"));
        unExpressiveDistributionList.add(new Pair<>(0.3, "default"));
        unExpressiveDistributionList.add(new Pair<>(0.4, "fake smile"));
        angerDistribution.setUnExpressiveDistributionVector(unExpressiveDistributionList);

        mapExpressionDistribution.put("anger",angerDistribution);


        //Build tree for Happiness
        ExpressionDistribution happyDistribution = new ExpressionDistribution();
        happyDistribution.setE(0.6);

        //Expressive Distribution List
        expressiveDistributionList = new ArrayList<>();
        expressiveDistributionList.add(new Pair<>(0.6, "Laughing"));
        expressiveDistributionList.add(new Pair<>(0.4, "Smiling"));
        happyDistribution.setExpressiveDistributionVector(expressiveDistributionList);

        //UnExpressive Distribution List
        unExpressiveDistributionList = new ArrayList<>();
        unExpressiveDistributionList.add(new Pair<>(0.3, "subtle smile"));
        unExpressiveDistributionList.add(new Pair<>(0.5, "default"));
        unExpressiveDistributionList.add(new Pair<>(0.2, "indifferent"));
        happyDistribution.setUnExpressiveDistributionVector(unExpressiveDistributionList);

        mapExpressionDistribution.put("happy",happyDistribution);


        //Build tree for Shame
        ExpressionDistribution shameDistribution = new ExpressionDistribution();
        shameDistribution.setE(0.4);

        //Expressive Distribution List
        expressiveDistributionList = new ArrayList<>();
        expressiveDistributionList.add(new Pair<>(0.7, "crying"));
        expressiveDistributionList.add(new Pair<>(0.3, "guilty"));
        shameDistribution.setExpressiveDistributionVector(expressiveDistributionList);

        //UnExpressive Distribution List
        unExpressiveDistributionList = new ArrayList<>();
        unExpressiveDistributionList.add(new Pair<>(0.5, "sad"));
        unExpressiveDistributionList.add(new Pair<>(0.5, "default"));
        shameDistribution.setUnExpressiveDistributionVector(unExpressiveDistributionList);
        mapExpressionDistribution.put("shame",shameDistribution);

        //Build tree for Anger
        ExpressionDistribution surpriseDistribution = new ExpressionDistribution();
        surpriseDistribution.setE(0.6);

        //Expressive Distribution List
        expressiveDistributionList = new ArrayList<>();
        expressiveDistributionList.add(new Pair<>(0.2, "shocked"));
        expressiveDistributionList.add(new Pair<>(0.8, "amazed"));
        surpriseDistribution.setExpressiveDistributionVector(expressiveDistributionList);

        //UnExpressive Distribution List
        unExpressiveDistributionList = new ArrayList<>();
        unExpressiveDistributionList.add(new Pair<>(1.0, "default"));
        surpriseDistribution.setUnExpressiveDistributionVector(unExpressiveDistributionList);

        mapExpressionDistribution.put("surprise",surpriseDistribution);
    }

    @Override
    public String getExpressionFromState(String stateValue, Double expressiveness) {
        if(!mapExpressionDistribution.containsKey(stateValue))
            return "No distribution available for state";

        ExpressionDistribution dist = mapExpressionDistribution.get(stateValue);
        List<Pair<Double, String>> expressionVector = expressiveness<= dist.getE()? dist.getExpressiveDistributionVector():
                dist.getUnExpressiveDistributionVector();
        double[] probabilityDistribution = ArrayUtils.toPrimitive(expressionVector.stream().map(x->x.getKey()).toArray(Double[]::new));
        probabilityDistribution = mathComponent.normalizeVector(probabilityDistribution);
        int index = mathComponent.getIndexOfNextStateFromProbabilityVector(probabilityDistribution);
        return expressionVector.get(index).getValue();
    }
}