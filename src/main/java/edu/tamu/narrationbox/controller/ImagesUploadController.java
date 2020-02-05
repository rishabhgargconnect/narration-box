package edu.tamu.narrationbox.controller;


import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.util.function.Consumer;

@RestController
@RequestMapping(value = "/data-store/")
@Api(description = "The descriptions of the characters in the story")
//put on hold trying python one first
public class ImagesUploadController {


    public void fetchFiles(File dir, Consumer<File> fileConsumer) {

        if (dir.isDirectory()) {
            for (File file1 : dir.listFiles()) {
                fetchFiles(file1, fileConsumer);
            }
        } else {
            fileConsumer.accept(dir);
        }
    }
}
