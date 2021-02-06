package guru.springframework.controllers;

import static org.junit.Assert.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;

import guru.springframework.domain.Recipe;
import guru.springframework.services.RecipeService;

public class IndexControllerTest {

	@Mock
	RecipeService recipeService;

	@Mock
	Model model;

	IndexController controller;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		controller = new IndexController(recipeService);
	}

	@Test
	public void testMockMVC() throws Exception {
		MockMvc mock = MockMvcBuilders.standaloneSetup(controller).build();

		mock.perform(get("/")).andExpect(status().isOk()).andExpect(view().name("index"));
	}

	@Test
	public void testGetIndexPage() {
		// Given
		Set<Recipe> recipes = new HashSet<Recipe>();

		Recipe recipe = new Recipe();
		recipe.setId(1L);

		recipes.add(recipe);
		recipes.add(new Recipe());

		when(recipeService.getRecipes()).thenReturn(recipes);

		ArgumentCaptor<Set<Recipe>> recipeSetData = ArgumentCaptor.forClass(Set.class);
		ArgumentCaptor<String> stringData = ArgumentCaptor.forClass(String.class);

		// When
		String viewPage = controller.getIndexPage(model);

		// Then
		assertEquals("index", viewPage);

		verify(recipeService, times(1)).getRecipes();
		verify(model, times(1)).addAttribute(stringData.capture(), recipeSetData.capture());

		Set<Recipe> setInController = recipeSetData.getValue();
		String stringInController = stringData.getValue();

		assertEquals(2, setInController.size());
		assertEquals("recipes", stringInController);
	}

}
