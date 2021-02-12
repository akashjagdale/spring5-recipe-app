package guru.springframework.controllers;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import guru.springframework.commands.RecipeCommand;
import guru.springframework.services.ImageService;
import guru.springframework.services.RecipeService;

@Controller
public class ImageController {

	private final ImageService imageService;

	private final RecipeService recipeService;

	public ImageController(ImageService imageService, RecipeService recipeService) {
		this.imageService = imageService;
		this.recipeService = recipeService;
	}

	@GetMapping("/recipe/{id}/image")
	public String showImageUploadForm(@PathVariable String id, Model model) {
		model.addAttribute("recipe", recipeService.findCommandById(Long.valueOf(id)));

		return "recipe/imageuploadform";
	}

	@PostMapping("/recipe/{id}/image")
	public String handleImage(@PathVariable String id, @RequestParam("imagefile") MultipartFile imageFile) {
		imageService.saveImageFile(Long.valueOf(id), imageFile);

		return "redirect:/recipe/" + id + "/show";

	}

	@GetMapping("/recipe/{id}/recipeimage")
	public void renderImageFormDB(@PathVariable String id, HttpServletResponse response) throws IOException {
		RecipeCommand recipeCommand = recipeService.findCommandById(Long.valueOf(id));

		byte[] byteArray = new byte[recipeCommand.getImage().length];

		int i = 0;
		for (Byte b : recipeCommand.getImage()) {
			byteArray[i++] = b; // Auto unboxing
		}

		response.setContentType("image/jpeg");
		InputStream is = new ByteArrayInputStream(byteArray);
		IOUtils.copy(is, response.getOutputStream());
	}

}
