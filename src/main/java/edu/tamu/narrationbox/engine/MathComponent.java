package edu.tamu.narrationbox.engine;

import org.springframework.stereotype.Component;

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
            a [i] = 1.0d - random.nextDouble();
            a [i] = -1 * Math.log(a[i]);
            s += a[i];
        }
        double sum = 0.0d;
        for (int i = 0; i < size-1; i++)
        {
            a [i] /= s;
            sum+=a[i];
        }
        a[size-1] = 1-sum;
        return a;
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

    public double[] getNextState(double[][] matrix, double[] value) {
        return value;
    }

    public double[] addStateComponent(double[] currentState, double impactWeight, double[] stateComponent) {
        return  currentState;
    }

    public double[] normalizeProbablityVector(double[] value) {
        return value;
    }
}
