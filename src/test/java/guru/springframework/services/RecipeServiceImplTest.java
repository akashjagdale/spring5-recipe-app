package guru.springframework.services;

import static org.junit.Assert.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import guru.springframework.domain.Recipe;
import guru.springframework.repositories.RecipeRepository;

public class RecipeServiceImplTest {
	RecipeServiceImpl recipeService;

	@Mock
	RecipeRepository recipeRepo;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		recipeService = new RecipeServiceImpl(recipeRepo);
	}
	
	@Test
	public void testGetRecipes() {
		Recipe recipe = new Recipe();
		HashSet<Recipe> recipeData = new HashSet<Recipe>();
		recipeData.add(recipe);
		
		when(recipeService.getRecipes()).thenReturn(recipeData);
		
		Set<Recipe> recipies = recipeService.getRecipes();
		assertEquals(recipies.size(), 1);
		verify(recipeRepo,times(1)).findAll();
		
	}

}
