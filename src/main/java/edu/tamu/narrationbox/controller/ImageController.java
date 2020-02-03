package edu.tamu.narrationbox.controller;

import edu.tamu.narrationbox.model.Image;
import edu.tamu.narrationbox.model.TreeNode;
import edu.tamu.narrationbox.repository.ImageRepository;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.MessageFormat;
import java.util.Base64;
import java.util.List;

@RestController
@RequestMapping(value = "/images/")
public class ImageController {

    @Autowired
    public ImageRepository imageRepository;

    @RequestMapping(value = "default", method = RequestMethod.GET, produces = "application/json")
    @ApiOperation("Get the default pic of all the images registered in the system.")
    public Image[] getDefaultImages() {
        return imageRepository.findDefaultImageOfAllCharacters().toArray(new Image[0]);
    }

    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    @ApiOperation("Get all the images registered in the system.")
    public Image[] getAllImages() {
        return imageRepository.findAll().toArray(new Image[0]);
    }

    @RequestMapping(value = "{identityId}",
            method = RequestMethod.GET,
            produces = MediaType.IMAGE_PNG_VALUE)
    @ApiOperation("Get a image registered in the system.")
    public ResponseEntity<byte[]> getImage(@PathVariable("identityId") String identityId,
                                           @RequestParam(value = "emotion", required = false) String emotion) {
        List<Image> images = imageRepository.findByImageMatchingAttributes(identityId, emotion);
        if (images.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        String image = images.get(0).getFile();
        return new ResponseEntity<>(Base64.getDecoder().decode
                (image.substring(2, image.length() - 1)), HttpStatus.OK); //Remove /b which gets added from the python script

    }

    @RequestMapping(value = "{identityId}",
            method = RequestMethod.DELETE,
            produces = MediaType.IMAGE_PNG_VALUE)
    @ApiOperation("Delete a specific emotion of an image or entire image from the system.")
    public String deleteImage(@PathVariable("identityId") String identityId,
                              @RequestParam(value = "emotion", required = false) String emotion) {
        if (emotion != null && emotion != "") {
            imageRepository.deleteImageMatchingAttributes(identityId, emotion);
        } else {
            imageRepository.deleteImageIdentity(identityId);
        }
        return "Success";
    }

    @RequestMapping(value = "ids", method = RequestMethod.GET, produces = "application/json")
    @ApiOperation("Get the ids of images registered in the system.")
    public String[] getAllImageIds() {
        return imageRepository.findAll().stream().map(x -> x.getIdentity()).distinct().toArray(String[]::new);
    }

    @RequestMapping(value = "node", method = RequestMethod.GET, produces = "application/json")
    @ApiOperation("Get the node in the system.")
    public TreeNode getNode(@RequestParam(value = "category", required = false) String path) {
        System.out.println("Hello");

        if (path == null) {
            path = "";
            String[] a = {"as", "qw"};
            TreeNode node = new TreeNode();
            node.setCategories(a);
            return node;
        }

        String regexPath = MessageFormat.format("^{0}\\/[\\w-]+(?!\\/)$", path);
        System.out.println("regexPath = " + regexPath);
        TreeNode node = new TreeNode();
        node.setCategories(imageRepository.getImagesOnPath(regexPath, "category").stream().map(x -> (x.getPath())).toArray(String[]::new));
        node.setCharacters(imageRepository.getImagesOnPath(regexPath, "character").stream().map(x -> getNameOfCharacter(x.getPath())).toArray(String[]::new));
        String[] a = {"as", "qw"};
        node.setCategories(a);
        return node;
    }

    private String getNameOfCharacter(String s) {
        return s.substring(s.lastIndexOf('/') + 1).trim();
    }

    @RequestMapping(method = RequestMethod.POST)
    @ApiOperation("Register a image in the system.")
    public String createImage(@RequestBody Image image) {
        imageRepository.save(image);
        return "Success";
    }
}
