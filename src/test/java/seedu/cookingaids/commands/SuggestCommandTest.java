package seedu.cookingaids.commands;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seedu.cookingaids.collections.RecipeBank;
import seedu.cookingaids.exception.OverflowQuantityException;
import seedu.cookingaids.items.Recipe;
import seedu.cookingaids.suggest.Suggest;
import seedu.cookingaids.collections.IngredientStorage;
import seedu.cookingaids.items.Ingredient;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;


import static org.junit.jupiter.api.Assertions.assertTrue;

class SuggestCommandTest {

    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    void setUpStreams() {
        System.setOut(new PrintStream(outputStream));
    }

    @AfterEach
    void restoreStreams() {
        System.setOut(originalOut);
    }

    @Test
    void execute_suggestRecipes_displaysSuggestedRecipes() throws OverflowQuantityException {
        // Arrange: Set up real data for testing
        IngredientStorage.clear();

        // Add ingredients to IngredientStorage
        Ingredient tomato = new Ingredient("tomato");
        Ingredient bread = new Ingredient("bread");
        Ingredient egg = new Ingredient("egg");
        IngredientStorage.addToStorage(tomato);
        IngredientStorage.addToStorage(bread);
        IngredientStorage.addToStorage(egg);

        // Initialise an unavailable ingredient
        Ingredient lettuce = new Ingredient("lettuce");

        ArrayList<Ingredient> sandwichIngredients = new ArrayList<>();
        sandwichIngredients.add(tomato);
        sandwichIngredients.add(bread);
        sandwichIngredients.add(egg);

        ArrayList<Ingredient> saladIngredients = new ArrayList<>();
        saladIngredients.add(lettuce);
        saladIngredients.add(tomato);

        // Add some recipes to suggest
        Recipe sandwich = new Recipe("sandwich", sandwichIngredients);
        Recipe salad = new Recipe("salad", saladIngredients);
        RecipeBank.addRecipeToRecipeBank(sandwich);
        RecipeBank.addRecipeToRecipeBank(salad);

        // Suggest recipes based on available ingredients
        Suggest.suggestRecipes();

        // Call SuggestCommand to display suggestions
        SuggestCommand.printSuggestions();

        // Verify that the correct recipes are suggested
        String commandOutput = outputStream.toString().trim();
        assertTrue(commandOutput.contains("You have enough ingredients to make:"));
    }

    @Test
    void execute_suggestRecipes_noAvailableRecipes() throws OverflowQuantityException {
        // Arrange: Set up with no available recipes
        IngredientStorage.clear();

        // Add insufficient ingredients to IngredientStorage
        Ingredient tomato = new Ingredient("tomato");
        Ingredient bread = new Ingredient("bread");
        Ingredient egg = new Ingredient("egg");
        IngredientStorage.addToStorage(tomato);

        ArrayList<Ingredient> sandwichIngredients = new ArrayList<>();
        sandwichIngredients.add(tomato);
        sandwichIngredients.add(bread);
        sandwichIngredients.add(egg);
        Recipe sandwich = new Recipe("sandwich", sandwichIngredients);
        RecipeBank.addRecipeToRecipeBank(sandwich);

        // Call the SuggestCommand to display suggestions
        SuggestCommand.printSuggestions();

        String commandOutput = outputStream.toString().trim();
        assertTrue(commandOutput.contains("Not enough ingredients"));
    }
}
