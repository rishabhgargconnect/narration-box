package edu.tamu.narrationbox.controller;

import edu.tamu.narrationbox.model.Image;
import edu.tamu.narrationbox.repository.ImageRepository;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/images/")
public class ImageController {

    @Autowired
    public ImageRepository imageRepository;

    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    @ApiOperation("Get all the images registered in the system.")
    public Image[] getAllImages(){
        return imageRepository.findAll().toArray(new Image[0]);
    }

    @RequestMapping(value = "{identityId}",
            method = RequestMethod.GET,
            produces = "application/json")
    @ApiOperation("Get a image registered in the system.")
    public List<Image> getImage(@PathVariable("identityId") String identityId,
                                @RequestParam(value = "emotion", required = false) String emotion){
        return imageRepository.findByImageMatchingAttributes(identityId, emotion);
    }

    @RequestMapping(method = RequestMethod.POST)
    @ApiOperation("Register a image in the system.")
    public String createImage(@RequestBody Image image) {
        imageRepository.save(image);
        return "Success";
    }
}
