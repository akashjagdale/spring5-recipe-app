package guru.springframework.controllers;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import guru.springframework.domain.Recipe;
import guru.springframework.services.RecipeService;

public class RecipeControllerTest {

	@Mock
	RecipeService recipeService;

	RecipeController controller;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);

		controller = new RecipeController(recipeService);
	}

	@Test
	public void testShowById() throws Exception {
		Recipe recipe = new Recipe();
		recipe.setId(1L);

		MockMvc mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

		when(recipeService.getRecipeById(org.mockito.Mockito.anyLong())).thenReturn(recipe);

		mockMvc.perform(get("/recipe/show/1")).andExpect(status().isOk()).andExpect(view().name("recipe/show"));
	}

}
