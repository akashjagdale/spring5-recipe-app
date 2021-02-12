/**
 * 
 */
package guru.springframework.services;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import guru.springframework.commands.IngredientCommand;
import guru.springframework.converters.IngredientCommandToIngredient;
import guru.springframework.converters.IngredientToIngredientCommand;
import guru.springframework.domain.Ingredient;
import guru.springframework.domain.Recipe;
import guru.springframework.repositories.RecipeRepository;
import guru.springframework.repositories.UnitOfMeasureRepository;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Akash J
 *
 */

@Slf4j
@Service
public class IngredientServiceImpl implements IngredientService {

	private final IngredientToIngredientCommand ingredientToCommand;

	private final IngredientCommandToIngredient commandToIngredient;

	private final RecipeRepository recipeRepository;

	private final UnitOfMeasureRepository uomReop;

	public IngredientServiceImpl(IngredientToIngredientCommand ingredientToCommand, RecipeRepository recipeRepository,
			UnitOfMeasureRepository uomReop, IngredientCommandToIngredient commandToIngredient) {
		this.ingredientToCommand = ingredientToCommand;
		this.commandToIngredient = commandToIngredient;
		this.recipeRepository = recipeRepository;
		this.uomReop = uomReop;
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

	@Transactional
	@Override
	public IngredientCommand saveOrUpdateIngredient(IngredientCommand command) {
		Optional<Recipe> recipeOptional = recipeRepository.findById(command.getRecipeId());

		if (!recipeOptional.isPresent()) {
			log.error("Recipe with id :: " + command.getRecipeId() + " is not found..!");
			return new IngredientCommand();
		} else {
			Recipe recipe = recipeOptional.get();
			Optional<Ingredient> optionalIngredient = recipe.getIngredients().stream()
					.filter(ingredient -> ingredient.getId().equals(command.getId())).findFirst();

			if (optionalIngredient.isPresent()) {
				Ingredient ingredientFound = optionalIngredient.get();
				ingredientFound.setDescription(command.getDescription());
				ingredientFound.setAmount(command.getAmount());
				ingredientFound.setUom(uomReop.findById(command.getUom().getId())
						.orElseThrow(() -> new RuntimeException("UOM NOT FOUND..!")));
			} else {
				Ingredient ingredient = commandToIngredient.convert(command);
				ingredient.setRecipe(recipe);
				recipe.addIngredient(ingredient);
			}

			Recipe saveRecipe = recipeRepository.save(recipe);
			Optional<Ingredient> savedIngredientOptional = saveRecipe.getIngredients().stream()
					.filter(ingredient -> ingredient.getId().equals(command.getId())).findFirst();

			if (!savedIngredientOptional.isPresent()) {
				savedIngredientOptional = saveRecipe.getIngredients().stream()
						.filter(ingredient -> ingredient.getDescription().equals(command.getDescription()))
						.filter(ingredient -> ingredient.getAmount().equals(command.getAmount()))
						.filter(ingredient -> ingredient.getUom().getId().equals(command.getUom().getId())).findFirst();
			}

			return ingredientToCommand.convert(savedIngredientOptional.get());
		}
	}

	@Override
	public void deleteIngredientById(Long recipeId, Long id) {
		Optional<Recipe> recipeOptional = recipeRepository.findById(recipeId);
		if (!recipeOptional.isPresent()) {
			log.error("Recipe Not found with id :: " + recipeId);
		} else {
			Recipe recipe = recipeOptional.get();
			Optional<Ingredient> optionalIngredient = recipe.getIngredients().stream()
					.filter(ingredient -> ingredient.getId().equals(id)).findFirst();
			if (optionalIngredient.isPresent()) {
				Ingredient ingredientToDelete = optionalIngredient.get();
				ingredientToDelete.setRecipe(null);
				recipe.getIngredients().remove(optionalIngredient.get());
				recipeRepository.save(recipe);
			}
		}

	}

}
