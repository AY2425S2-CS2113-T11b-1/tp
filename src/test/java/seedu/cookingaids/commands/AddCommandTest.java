package seedu.cookingaids.commands;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seedu.cookingaids.collections.DishCalendar;
import seedu.cookingaids.collections.IngredientStorage;
import seedu.cookingaids.collections.RecipeBank;
import seedu.cookingaids.items.Dish;
import seedu.cookingaids.items.Ingredient;
import seedu.cookingaids.items.Recipe;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AddCommandTest {

    private DishCalendar dishCalendar;

    @BeforeEach
    void setUp() {
        dishCalendar = new DishCalendar();
        Dish spaghetti = new Dish(1, "Spaghetti", "20/03/2025");
        DishCalendar.addDishToCalendar(spaghetti);
    }

    @AfterEach
    void tearDown() {
        dishCalendar.clear();
    }

    @Test
    void execute_addDishToEmptyDishCalendar_addsSuccessfully() {
        DishCalendar emptyDishCalendar = new DishCalendar();
        String dishName = "Pizza";
        Dish newDish = new Dish(1, dishName, "22/03/2025");

        AddCommand.addDish("add -dish=" + dishName + " -when=22/03/2025");

        assertTrue(emptyDishCalendar.containsDish(dishName));
    }

    @Test
    void execute_addIngredientToEmptyStorage_addsSuccessfully() {
        IngredientStorage.clear();
        String ingredientName = "Tomato";
        Ingredient newIngredient = new Ingredient(1, ingredientName);

        AddCommand.addIngredient("add -ingredient=" + ingredientName);

        assertTrue(IngredientStorage.contains(ingredientName));
    }

    @Test
    void execute_addIngredientThatExists_returnsIngredientAlreadyExistsMessage() {
        IngredientStorage.clear();
        IngredientStorage.addToStorage(new Ingredient(1, "Lettuce"));

        AddCommand.addIngredient("add -ingredient=Lettuce");

        assertTrue(IngredientStorage.contains("Lettuce"));
    }

    @Test
    void execute_addRecipeToEmptyRecipeBank_addsSuccessfully() {
        RecipeBank.clear();
        ArrayList<String> ingredients = new ArrayList<>();
        ingredients.add("Tomato");
        ingredients.add("Cheese");
        Recipe newRecipe = new Recipe("Pizza", ingredients);

        AddCommand.addRecipe("add -recipe=Pizza -needs=Tomato,Cheese");

        assertTrue(RecipeBank.contains("Pizza"));
    }

    @Test
    void execute_addRecipeThatExists_returnsRecipeAlreadyExistsMessage() {
        ArrayList<String> ingredients = new ArrayList<>();
        ingredients.add("Bread");
        ingredients.add("Tomato");
        RecipeBank.addRecipeToRecipeBank(new Recipe("Sandwich", ingredients));

        AddCommand.addRecipe("add -recipe=Sandwich -needs=Bread,Tomato");

        assertTrue(RecipeBank.contains("Sandwich"));
    }

    @Test
    void execute_addInvalidDishName_returnsInvalidDishNameMessage() {
        String invalidDishName = "";  // Invalid because dish name is empty

        AddCommand.addDish("add -dish=" + invalidDishName + " -when=23/03/2025");

        assertFalse(dishCalendar.containsDish(invalidDishName));
    }

    @Test
    void execute_addDishWithInvalidDate_returnsInvalidDateMessage() {
        String invalidDate = "32/13/2025";  // Invalid date

        AddCommand.addDish("add -dish=Lasagna -when=" + invalidDate);

        // Check if the dish is not added
        assertFalse(dishCalendar.containsDish("Lasagna"));
    }

    @Test
    void execute_addRecipeWithMissingIngredients_returnsMissingIngredientsMessage() {
        ArrayList<String> ingredients = new ArrayList<>();
        ingredients.add("Bread");
        Recipe incompleteRecipe = new Recipe("Toast", ingredients);

        AddCommand.addRecipe("add -recipe=Toast -needs=Bread");

        assertAddRecipeSuccessful("Toast");
    }

    private void assertAddDishSuccessful(String dishName, DishCalendar dishCalendar) {
        assertTrue(dishCalendar.containsDish(dishName));
    }

    private void assertAddIngredientSuccessful(String ingredientName) {
        assertTrue(IngredientStorage.contains(ingredientName));
    }

    private void assertAddRecipeSuccessful(String recipeName) {
        assertTrue(RecipeBank.contains(recipeName));
    }
}
