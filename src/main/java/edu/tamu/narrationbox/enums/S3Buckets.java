package edu.tamu.narrationbox.enums;

/**
 * @author Rishabh Garg
 */

public enum S3Buckets {

    CHARACTERS("characterBucketName");

    private final String val;

    S3Buckets(String val) {
        this.val = val;
    }

    public String getValue() {
        return this.val;
    }

}
