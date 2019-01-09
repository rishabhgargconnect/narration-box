package edu.tamu.narrationbox.model;


import lombok.Data;

/* The identity of characters has been separated from the personality
*  This has been done mainly to accommodate the requirement to have
*  flexibility to associate different characters such as shark, octopus, etc.
*  to an individual personality in the narration */
@Data
public class Identity {
    private String Id;
    private String title;
    private String path;
}
