package edu.tamu.narrationbox.controller;

import edu.tamu.narrationbox.model.Image;
import edu.tamu.narrationbox.repository.ImageRepository;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;
import java.util.List;

@RestController
@RequestMapping(value = "/images/")
public class ImageController {

    @Autowired
    public ImageRepository imageRepository;

    @RequestMapping(value = "default", method = RequestMethod.GET, produces = "application/json")
    @ApiOperation("Get the default pic of all the images registered in the system.")
    public Image[] getDefaultImages(){
        return imageRepository.findDefaultImageOfAllCharacters().toArray(new Image[0]);
    }

    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    @ApiOperation("Get all the images registered in the system.")
    public Image[] getAllImages(){
        return imageRepository.findAll().toArray(new Image[0]);
    }

    @RequestMapping(value = "{identityId}",
            method = RequestMethod.GET,
            produces = MediaType.IMAGE_PNG_VALUE)
    @ApiOperation("Get a image registered in the system.")
    public byte[] getImage(@PathVariable("identityId") String identityId,
                                @RequestParam(value = "emotion", required = false) String emotion){
        List<Image> images = imageRepository.findByImageMatchingAttributes(identityId, emotion);
        if(images.isEmpty()){
            imageRepository.findByImageMatchingAttributes(identityId, "default");
        }
        String image = images.get(0).getFile();
        return Base64.getDecoder().decode
                (image.substring(2, image.length()-1)); //Remove /b which gets added from the python script

    }

    @RequestMapping(value = "{identityId}",
            method = RequestMethod.DELETE,
            produces = MediaType.IMAGE_PNG_VALUE)
    @ApiOperation("Delete a specific emotion of an image or entire image from the system.")
    public String deleteImage(@PathVariable("identityId") String identityId,
                           @RequestParam(value = "emotion", required = false) String emotion){
        if(emotion != null && emotion != ""){
            imageRepository.deleteImageMatchingAttributes(identityId, emotion);
        }
        else {
            imageRepository.deleteImageIdentity(identityId);
        }
        return "Success";
    }

    @RequestMapping(value = "ids", method = RequestMethod.GET, produces = "application/json")
    @ApiOperation("Get the ids of images registered in the system.")
    public String[] getAllImageIds(@RequestParam(value = "category", required = false) String category){
        if(category== null){
            return imageRepository.findAll().stream().map(  x->x.getIdentity()).distinct().toArray(String[]::new);
        }

        String[] images =  imageRepository.findAll().stream().filter(x->x.getPath().contains(category)).map(  x->x.getIdentity()).distinct().toArray(String[]::new);
        return images;
    }

    @RequestMapping(method = RequestMethod.POST)
    @ApiOperation("Register a image in the system.")
    public String createImage(@RequestBody Image image) {
        imageRepository.save(image);
        return "Success";
    }
}
