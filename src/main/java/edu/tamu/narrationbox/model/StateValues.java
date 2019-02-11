package edu.tamu.narrationbox.model;

import lombok.Data;

@Data
public class StateValues {
    private String StateDescriptorId;
    private double[] value;
    private String valueAtIndexOfLargestComponent;
}
