package edu.tamu.narrationbox.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Random;

/* The base class for the state of a character. It can be
emotional,physical, existential etc.*/
@Data
@Document(collection = "state")
public class State {

    @Id
    private String id;

    private List<String> indices;

    public double[] generateSample()
    {
        //TODO: Revisit for mathematical accuracy
        int n = indices.size();
        double a[] = new double[n];
        double s = 0.0d;
        Random random = new Random();
        for (int i = 0; i < n; i++)
        {
            a [i] = 1.0d - random.nextDouble();
            a [i] = -1 * Math.log(a[i]);
            s += a[i];
        }
        double sum = 0.0d;
        for (int i = 0; i < n-1; i++)
        {
            a [i] /= s;
            sum+=a[i];
        }
        a[n-1] = 1-sum;
        return a;
    }
}
