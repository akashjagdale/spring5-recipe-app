package guru.springframework.services;

import guru.springframework.commands.IngredientCommand;

public interface IngredientService {

	IngredientCommand findByRecipeIdAndIngredientId(Long recipeId, Long ingredientId);

	IngredientCommand saveOrUpdateIngredient(IngredientCommand command);

	void deleteIngredientById(Long recipeId, Long id);
}
