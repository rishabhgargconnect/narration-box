package edu.tamu.narrationbox.model;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class Image {
    String identity;
    String emotion;
    String path;
    String file;
    String type;
    MultipartFile uploadFile;
}

