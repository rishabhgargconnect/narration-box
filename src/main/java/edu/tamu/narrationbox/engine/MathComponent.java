package edu.tamu.narrationbox.engine;

import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Random;

@Component
public class MathComponent {

    /*Generate a One Dimensional vector, where all elements sum up to One*/
    //TODO: Get a better, more mathematically appropriate Logic
    public double[] generateProbabilityVector(int size){
        double a[] = new double[size];
        double s = 0.0d;
        Random random = new Random();
        for (int i = 0; i < size; i++)
        {
            a [i] = random.nextDouble();
        }
        return roundOffVector(normalizeVector(a), 2);//We truncate beyond 2 decimal points
    }

    /*Generate a One Dimensional vector, where all elements sum up to One*/
    //TODO: Get a better, more mathematically appropriate Logic
    public double[][] generateTransitionMatrix(int rows, int columns){
        double[][] transitionMatrix = new double[rows][columns];
        for (int i = 0; i < columns; i++) {
            transitionMatrix[i] = generateProbabilityVector(rows);
        }
        return transitionMatrix;
    }

    /*Logic:
    *
    * Let us assume we have the current state as a probability vector [S1, S2, S3]
    * And the transition matrix has value:
    *           [[S11, S12, S13],
    *            [S21, S22, S23],
    *            [S31, S32, S33]]
    *
    * Here, each row represents the markov probabilities for transition from state
    * at Si in the probability vector.Each row adds upto 1.
    *
    * We take num_of_iterations as a configurable value of the number of iterations
    * we use to calculate the next state.
    *
    * For num_of_iterations times we generate a new index from the current state.
    * Now we chose the row in the transition vector for this index in the state.
    * And from the probability matrix in this row, we get a new state.
    *
    * We thus count the number of times each state got generated, and normalize it to form a probability vector,
    * representing the next state
    */
    public double[] getNextStateFromTransitionMatrix(double[][] matrix, double[] value, int num_of_iterations) {
        double[] nextState = new double[value.length];

        for (int i = 0; i < num_of_iterations; i++) {
            int indexOfProbability
                    = getIndexOfNextStateFromProbabilityVector(value);
            int indexOfSampleInNextState
                    = getIndexOfNextStateFromProbabilityVector(matrix[indexOfProbability]);
            nextState[indexOfSampleInNextState]++;
        }
        return normalizeVector(nextState);//We round off before normalizing
    }

    public double[] roundOffVector(double[] vector, int digits){
        double sum = 0;
        double multiplier = Math.pow(10, digits);
        for (int i = 0; i <vector.length-1 ; i++) {
            vector[i] = Math.round(multiplier * vector[i])/multiplier;
            sum += vector[i];
        }
        vector[vector.length-1] = Math.round((1.0 - sum)* multiplier) / multiplier;
        return vector;
    }

    public int getIndexOfNextStateFromProbabilityVector(double[] probabilityVector){
        double cumulativeSum = 0.0d;
        double newRandomNumber = new Random().nextDouble();
        for(int i = 0; i < probabilityVector.length; i++){
            cumulativeSum += probabilityVector[i];
            if(newRandomNumber <= cumulativeSum){
                return i;
            }
        }
        throw new MathematicalFallacyException("Probability vector values don't add upto 1");
    }

    public double[] addStateComponent(double[] currentState, double impactWeight, double[] stateComponent) {
        for (int i = 0; i < currentState.length; i++){
            currentState[i] += impactWeight * stateComponent[i];
        }
        return currentState;
    }

    public double[] normalizeVector(double[] vector) {
        double sum = Arrays.stream(vector).sum();
        return Arrays.stream(vector).map(operand -> operand/sum).toArray();
    }

    public int getIndexOfLargestComponent(double[] vector){
        int indexOfLargestElement = 0;
        for (int i = 0; i < vector.length; i++) {
            indexOfLargestElement =
                    (vector[i] > vector[indexOfLargestElement]) ? i : indexOfLargestElement;
        }
        return indexOfLargestElement;
    }
}
