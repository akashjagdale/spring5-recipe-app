package guru.springframework.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import guru.springframework.commands.IngredientCommand;
import guru.springframework.commands.RecipeCommand;
import guru.springframework.commands.UnitOfMeasureCommand;
import guru.springframework.services.IngredientService;
import guru.springframework.services.RecipeService;
import guru.springframework.services.UnitOfMeasureService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class IngredientController {

	private final RecipeService recipeService;

	private final IngredientService ingredientService;

	private final UnitOfMeasureService uomService;

	public IngredientController(RecipeService recipeService, IngredientService ingredientService,
			UnitOfMeasureService uomService) {
		this.recipeService = recipeService;
		this.ingredientService = ingredientService;
		this.uomService = uomService;
	}

	@GetMapping("/recipe/{recipeId}/ingredients")
	public String listIngredients(@PathVariable String recipeId, Model model) {
		log.debug("Getting ingredients list for recipe id :: " + recipeId);
		model.addAttribute("recipe", recipeService.findCommandById(Long.valueOf(recipeId)));

		return "recipe/ingredient/list";
	}

	@GetMapping("/recipe/{recipeId}/ingredients/{id}/show")
	public String showRecipeIngredient(@PathVariable String recipeId, @PathVariable String id, Model model) {
		model.addAttribute("ingredient",
				ingredientService.findByRecipeIdAndIngredientId(Long.valueOf(recipeId), Long.valueOf(id)));
		return "recipe/ingredient/show";
	}

	@GetMapping("/recipe/{recipeId}/ingredient/new")
	public String newRecipeIngredient(@PathVariable String recipeId, Model model) {
		Long recipeIdWrapper = Long.valueOf(recipeId);
		RecipeCommand recipeCommand = recipeService.findCommandById(recipeIdWrapper);

		IngredientCommand ingredientCommand = new IngredientCommand();
		ingredientCommand.setRecipeId(recipeIdWrapper);

		model.addAttribute("ingredient", ingredientCommand);

		ingredientCommand.setUom(new UnitOfMeasureCommand());

		model.addAttribute("uomList", uomService.listAllUoms());

		return "recipe/ingredient/ingredientform";
	}

	@GetMapping("/recipe/{recipeId}/ingredients/{id}/update")
	public String updateRecipeIngredient(@PathVariable String recipeId, @PathVariable String id, Model model) {
		model.addAttribute("ingredient",
				ingredientService.findByRecipeIdAndIngredientId(Long.valueOf(recipeId), Long.valueOf(id)));
		model.addAttribute("uomList", uomService.listAllUoms());
		return "recipe/ingredient/ingredientform";
	}

	@RequestMapping("/recipe/{recipeId}/ingredient/save")
	public String saveOrUpdate(@ModelAttribute IngredientCommand command) {
		IngredientCommand savedCommand = ingredientService.saveOrUpdateIngredient(command);

		log.debug("recipeid :: ", command.getRecipeId());
		log.debug("ingredientId :: ", command.getId());

		return "redirect:/recipe/" + savedCommand.getRecipeId() + "/ingredients/" + savedCommand.getId() + "/show";
	}

	@GetMapping("/recipe/{recipeId}/ingredient/{id}/delete")
	public String deleteRecipeById(@PathVariable String recipeId, @PathVariable String id) {
		ingredientService.deleteIngredientById(Long.valueOf(recipeId), Long.valueOf(id));
		log.debug("Deleted ingredient with id :: " + id + " recipe with id :: " + recipeId);
		return "redirect:/recipe/" + recipeId + "/ingredients";
	}

}
