package guru.springframework.controllers;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.any;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import guru.springframework.commands.RecipeCommand;
import guru.springframework.services.ImageService;
import guru.springframework.services.RecipeService;

public class ImageControllerTest {

	@Mock
	ImageService imageService;

	@Mock
	RecipeService recipeService;

	ImageController imageController;

	MockMvc mockMvc;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		imageController = new ImageController(imageService, recipeService);
		mockMvc = MockMvcBuilders.standaloneSetup(imageController).build();
	}

	@Test
	public void testShowImageUploadForm() throws Exception {
		RecipeCommand recipeComand = new RecipeCommand();
		recipeComand.setId(1L);

		when(recipeService.findCommandById(anyLong())).thenReturn(recipeComand);

		mockMvc.perform(get("/recipe/1/image")).andExpect(status().isOk()).andExpect(model().attributeExists("recipe"));

		verify(recipeService, times(1)).findCommandById(anyLong());
	}

	@Test
	public void testHandleImage() throws Exception {
		MockMultipartFile multiPartFile = new MockMultipartFile("imagefile", "testing.txt", "text/plain",
				"Test Multipart File..!".getBytes());
		mockMvc.perform(multipart("/recipe/1/image").file(multiPartFile)).andExpect(status().is3xxRedirection())
				.andExpect(header().string("Location", "/recipe/1/show"));

		verify(imageService, times(1)).saveImageFile(anyLong(), any());
	}

	@Test
	public void testRenderImageFormDB() throws Exception {

		RecipeCommand recipeCommand = new RecipeCommand();
		recipeCommand.setId(1L);

		String s = "some string";
		Byte[] byteArray = new Byte[s.getBytes().length];

		int i = 0;
		for (byte b : s.getBytes()) {
			byteArray[i++] = b; // Auto unboxing
		}

		recipeCommand.setImage(byteArray);

		when(recipeService.findCommandById(anyLong())).thenReturn(recipeCommand);

		MockHttpServletResponse response = mockMvc.perform(get("/recipe/1/recipeimage")).andExpect(status().isOk())
				.andReturn().getResponse();

		byte[] responseBytes = response.getContentAsByteArray();

		assertEquals(s.getBytes().length, responseBytes.length);
	}

}
