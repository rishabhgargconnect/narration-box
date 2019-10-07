package edu.tamu.narrationbox.engine;

import edu.tamu.narrationbox.model.ExpressionDistribution;
import javafx.util.Pair;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ExpressionGeneratorImpl implements ExpressionGenerator {

    private Map<String, ExpressionDistribution> mapExpressionDistribution;

    @Autowired
    private MathComponent mathComponent;

    public ExpressionGeneratorImpl(){

        mapExpressionDistribution = new HashMap<>();

        //Build tree for Anger
        ExpressionDistribution angerDistribution = new ExpressionDistribution();

        //Expressive Distribution List
        List<Pair<Double, String>> expressiveDistributionList = new ArrayList<>();
        expressiveDistributionList.add(new Pair<>(0.5, "anger"));
        expressiveDistributionList.add(new Pair<>(0.5, "rage"));
        angerDistribution.setExpressiveDistributionVector(expressiveDistributionList);

        //UnExpressive Distribution List
        List<Pair<Double, String>> unExpressiveDistributionList = new ArrayList<>();
        unExpressiveDistributionList.add(new Pair<>(0.3, "sad"));
        unExpressiveDistributionList.add(new Pair<>(0.3, "concern"));
        unExpressiveDistributionList.add(new Pair<>(0.4, "default"));
        angerDistribution.setUnExpressiveDistributionVector(unExpressiveDistributionList);

        mapExpressionDistribution.put("anger",angerDistribution);


        //Build tree for Happiness
        ExpressionDistribution happyDistribution = new ExpressionDistribution();

        //Expressive Distribution List
        expressiveDistributionList = new ArrayList<>();
        expressiveDistributionList.add(new Pair<>(0.6, "laugh"));
        expressiveDistributionList.add(new Pair<>(0.4, "sinceresmile"));
        happyDistribution.setExpressiveDistributionVector(expressiveDistributionList);

        //UnExpressive Distribution List
        unExpressiveDistributionList = new ArrayList<>();
        unExpressiveDistributionList.add(new Pair<>(0.5, "fakesmile"));
        unExpressiveDistributionList.add(new Pair<>(0.5, "default"));
        happyDistribution.setUnExpressiveDistributionVector(unExpressiveDistributionList);

        mapExpressionDistribution.put("happy",happyDistribution);


        //Build tree for Shame
        ExpressionDistribution shameDistribution = new ExpressionDistribution();

        //Expressive Distribution List
        expressiveDistributionList = new ArrayList<>();
        expressiveDistributionList.add(new Pair<>(0.2, "cry"));
        expressiveDistributionList.add(new Pair<>(0.3, "terror"));
        expressiveDistributionList.add(new Pair<>(0.5, "shame"));
        shameDistribution.setExpressiveDistributionVector(expressiveDistributionList);

        //UnExpressive Distribution List
        unExpressiveDistributionList = new ArrayList<>();
        unExpressiveDistributionList.add(new Pair<>(0.5, "sad"));
        unExpressiveDistributionList.add(new Pair<>(0.5, "default"));
        shameDistribution.setUnExpressiveDistributionVector(unExpressiveDistributionList);
        mapExpressionDistribution.put("shame",shameDistribution);

        //Build tree for Surprise
        ExpressionDistribution surpriseDistribution = new ExpressionDistribution();

        //Expressive Distribution List
        expressiveDistributionList = new ArrayList<>();
        expressiveDistributionList.add(new Pair<>(1.0, "surprise"));
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

        double[] expressiveArray = {expressiveness, 1- expressiveness};
        int i = mathComponent.getIndexOfNextStateFromProbabilityVector(
                    mathComponent.normalizeVector(expressiveArray));

        ExpressionDistribution dist = mapExpressionDistribution.get(stateValue);
        List<Pair<Double, String>> expressionVector = (i==0)?
                dist.getExpressiveDistributionVector():
                    dist.getUnExpressiveDistributionVector();

        double[] probabilityDistribution = ArrayUtils.
                toPrimitive(expressionVector.stream().
                        map(Pair::getKey).toArray(Double[]::new));

        probabilityDistribution = mathComponent.normalizeVector(probabilityDistribution);
        int index = mathComponent.getIndexOfNextStateFromProbabilityVector(probabilityDistribution);
        return expressionVector.get(index).getValue();
    }
}