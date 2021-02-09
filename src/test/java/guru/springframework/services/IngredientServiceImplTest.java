package guru.springframework.services;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import guru.springframework.commands.IngredientCommand;
import guru.springframework.converters.IngredientToIngredientCommand;
import guru.springframework.converters.UnitOfMeasureToUnitOfMeasureCommand;
import guru.springframework.domain.Ingredient;
import guru.springframework.domain.Recipe;
import guru.springframework.repositories.RecipeRepository;

public class IngredientServiceImplTest {

	IngredientService ingredientService;

	IngredientToIngredientCommand ingredientToCommand;

	@Mock
	RecipeRepository recipeRepository;

	public IngredientServiceImplTest() {
		this.ingredientToCommand = new IngredientToIngredientCommand(new UnitOfMeasureToUnitOfMeasureCommand());
	}

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);

		ingredientService = new IngredientServiceImpl(ingredientToCommand, recipeRepository);
	}

	@Test
	public void testFindByRecipeIdAndIngredientId() {
		Recipe recipe = new Recipe();
		recipe.setId(1L);

		Ingredient ingredient1 = new Ingredient();
		ingredient1.setId(1L);

		Ingredient ingredient2 = new Ingredient();
		ingredient2.setId(2L);

		Ingredient ingredient3 = new Ingredient();
		ingredient3.setId(3L);

		recipe.addIngredient(ingredient1);
		recipe.addIngredient(ingredient2);
		recipe.addIngredient(ingredient3);

		Optional<Recipe> recipeOptional = Optional.of(recipe);

		when(recipeRepository.findById(org.mockito.Mockito.anyLong())).thenReturn(recipeOptional);

		IngredientCommand ingredientCommand = ingredientService.findByRecipeIdAndIngredientId(1L, 2L);

		assertEquals(Long.valueOf(2L), ingredientCommand.getId());
		assertEquals(Long.valueOf(1L), ingredientCommand.getRecipeId());
		verify(recipeRepository, times(1)).findById(org.mockito.Mockito.anyLong());
	}

}
