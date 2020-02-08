package edu.tamu.narrationbox.controller;

import edu.tamu.narrationbox.service.aws.S3UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Rishabh Garg
 */

@RestController
@RequestMapping("/storage/")
public class BucketController {

    private S3UploadService s3UploadService;

    @Autowired
    BucketController(S3UploadService s3UploadService) {
        this.s3UploadService = s3UploadService;
    }

    @PostMapping("/uploadFile")
    public String uploadFile(@RequestPart(value = "files") MultipartFile file) {
        System.out.println("HELLO!WHATUP");
        return this.s3UploadService.uploadFile(file);
    }

    @DeleteMapping("/deleteFile")
    public String deleteFile(@RequestPart(value = "url") String fileUrl) {
        return this.s3UploadService.deleteFileFromS3Bucket(fileUrl);
    }
}