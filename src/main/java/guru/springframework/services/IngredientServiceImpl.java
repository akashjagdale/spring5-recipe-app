/**
 * 
 */
package guru.springframework.services;

import java.util.Optional;
import org.springframework.stereotype.Service;

import guru.springframework.commands.IngredientCommand;
import guru.springframework.converters.IngredientToIngredientCommand;
import guru.springframework.domain.Recipe;
import guru.springframework.repositories.RecipeRepository;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Akash J
 *
 */

@Slf4j
@Service
public class IngredientServiceImpl implements IngredientService {

	private final IngredientToIngredientCommand ingredientToCommand;

	private final RecipeRepository recipeRepository;

	public IngredientServiceImpl(IngredientToIngredientCommand ingredientToCommand, RecipeRepository recipeRepository) {
		this.ingredientToCommand = ingredientToCommand;
		this.recipeRepository = recipeRepository;
	}

	@Override
	public IngredientCommand findByRecipeIdAndIngredientId(Long recipeId, Long ingredientId) {

		Optional<Recipe> recipeOptional = recipeRepository.findById(recipeId);
		if (!recipeOptional.isPresent()) {
			log.error("Recipe with id = " + recipeId + " is not found");
		}

		Recipe recipe = recipeOptional.get();

		Optional<IngredientCommand> ingredientCommand = recipe.getIngredients().stream()
				.filter(ingredient -> ingredient.getId().equals(ingredientId))
				.map(ingredient -> ingredientToCommand.convert(ingredient)).findFirst();

		if (!ingredientCommand.isPresent()) {
			log.error("Ingredient with id::" + ingredientId + " for recipe with id::" + recipeId + " not found");
		}
		return ingredientCommand.get();
	}

}
