package guru.springframework.services;

import org.springframework.web.multipart.MultipartFile;

public interface ImageService {

	public void saveImageFile(Long recipeId, MultipartFile imageFile);
}
