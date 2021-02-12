package guru.springframework.services;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import guru.springframework.domain.Recipe;
import guru.springframework.repositories.RecipeRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ImageServiceImpl implements ImageService {

	private final RecipeRepository recipeRepository;

	public ImageServiceImpl(RecipeRepository recipeRepository) {
		this.recipeRepository = recipeRepository;
	}

	@Transactional
	@Override
	public void saveImageFile(Long recipeId, MultipartFile imageFile) {
		try {
			Optional<Recipe> recipeOptional = recipeRepository.findById(recipeId);

			if (recipeOptional.isPresent()) {
				Byte[] byteObjects = new Byte[imageFile.getBytes().length];

				int i = 0;
				for (Byte b : imageFile.getBytes()) {
					byteObjects[i++] = b;
				}

				Recipe recipe = recipeOptional.get();
				recipe.setImage(byteObjects);
				recipeRepository.save(recipe);
			}
		} catch (Exception e) {
			log.error("Error occured while saving image file");
			e.printStackTrace();
		}

	}

}
