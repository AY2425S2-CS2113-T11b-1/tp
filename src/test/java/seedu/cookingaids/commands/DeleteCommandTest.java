package seedu.cookingaids.commands;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import seedu.cookingaids.collections.DishCalendar;
import seedu.cookingaids.collections.IngredientStorage;
import seedu.cookingaids.collections.RecipeBank;
import seedu.cookingaids.exception.OverflowQuantityException;
import seedu.cookingaids.items.Dish;
import seedu.cookingaids.items.Ingredient;
import seedu.cookingaids.items.Recipe;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DeleteCommandTest {

    private DishCalendar dishCalendar;

    @BeforeEach
    void setUp() {
        dishCalendar = new DishCalendar();
        Dish spaghetti = new Dish( "spaghetti", "20/03/2025");
        DishCalendar.addDishToCalendar(spaghetti);
    }

    @AfterEach
    void tearDown() {
        DishCalendar.clear();
        RecipeBank.clear();
    }

    @Test
    void execute_emptyDishCalendar_returnsDishNotFoundMessage() {
        assertDeletionFailsDueToNoSuchDish("pizza", new DishCalendar());
    }

    @Test
    void execute_invalidDish_returnsInvalidIndexMessage() {
        assertDeletionFailsDueToInvalidDish("NonExistentDish", dishCalendar);
    }

    @Test
    void execute_validDish_deletesSuccessfully() {
        assertDeletionSuccessful("spaghetti", dishCalendar);
    }

    @Test
    void execute_validIngredient_deletesSuccessfully() throws OverflowQuantityException {
        Ingredient tomato = new Ingredient("tomato");
        IngredientStorage.addToStorage(tomato);
        assertDeletionSuccessfulIngredient("tomato");
    }

    @Test
    void execute_nonExistentIngredient_returnsIngredientNotFoundMessage() {
        assertDeletionFailsDueToNoSuchIngredient("garlic");
    }

    @Test
    void execute_validRecipe_deletesSuccessfully() {
        ArrayList<Ingredient> ingredients = new ArrayList<>();
        ingredients.add(new Ingredient("bread", 2));
        ingredients.add(new Ingredient("tomato", 1));
        ingredients.add(new Ingredient("egg", 1));
        Recipe sandwich = new Recipe("sandwich", ingredients);
        Recipe lasagna = new Recipe("lasagna");
        RecipeBank.addRecipeToRecipeBank(sandwich);
        RecipeBank.addRecipeToRecipeBank(lasagna);

        assertDeletionSuccessfulRecipe("lasagna");

        assertTrue(RecipeBank.contains("sandwich"));
        assertEquals(1, RecipeBank.getRecipeBankSize());

        DeleteCommand.removeRecipe(sandwich);

        assertFalse(RecipeBank.contains("sandwich"));
        assertEquals(0, RecipeBank.getRecipeBankSize());

    }

    @Test
    void execute_multipleDishesWithSameName_promptsUserToChoose() {
        // Simulate user input (e.g., "1\n" to select first dish)
        ByteArrayInputStream testIn = new ByteArrayInputStream("1\n".getBytes());
        InputStream originalIn = System.in;
        System.setIn(testIn);

        // Capture console output
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        try {
            // Add a second dish also called spaghetti
            Dish spaghetti = new Dish( "spaghetti", "21/03/2025");
            DishCalendar.addDishToCalendar(spaghetti);

            assertEquals(2, DishCalendar.getAllDishes().size());

            // Run delete command that triggers the prompt
            DeleteCommand.deleteDish("delete -dish=spaghetti");

            // Check if prompt is printed
            String consoleOutput = outputStream.toString().trim();
            assertTrue(consoleOutput.contains("Which would you like to delete? Input a number."));

            assertEquals(1, DishCalendar.getAllDishes().size());

        } finally {
            System.setIn(originalIn);
            System.setOut(originalOut);
        }
    }

    @Test
    void execute_nonExistentRecipe_returnsRecipeNotFoundMessage() {
        assertDeletionFailsDueToNoSuchRecipe("NonExistentRecipe");
    }

    @Test
    void execute_afterDishCalendarCleared_returnsDishNotFoundMessage() {
        DishCalendar.clear();
        assertDeletionFailsDueToNoSuchDish("toast", dishCalendar);
    }

    @Test
    void execute_longDishName_deletesSuccessfully() {
        String longDishName =
                "a_very_long_dish_name_that_exceeds_normal_expectations_and_is_meant_to_test_the_limits_of_the_system";
        Dish longDish = new Dish( longDishName, "21/03/2025");
        DishCalendar.addDishToCalendar(longDish);
        assertDeletionSuccessful(longDishName, dishCalendar);
    }

    @Test
    void execute_deleteIngredientWhenFull_returnsIngredientNotFoundMessage() throws NullPointerException,
            OverflowQuantityException {
        IngredientStorage.clear();  // Simulate the full storage by clearing it first.
        Ingredient ingredient = new Ingredient( "lettuce");
        IngredientStorage.addToStorage(ingredient);
        assertDeletionSuccessfulIngredient("lettuce");
    }

    private void assertDeletionFailsDueToInvalidDish(String dishName, DishCalendar dishCalendar) {
        DeleteCommand.deleteDish("delete -dish=" + dishName);
        assertFalse(dishCalendar.containsDish(dishName));
    }

    private void assertDeletionFailsDueToNoSuchDish(String dishName, DishCalendar dishCalendar) {
        DeleteCommand.deleteDish("delete -dish=" + dishName);
        assertFalse(dishCalendar.containsDish(dishName));
    }

    private void assertDeletionSuccessful(String dishName, DishCalendar dishCalendar) {
        assertTrue(dishCalendar.containsDish(dishName));
        DeleteCommand.deleteDish("delete -dish=" + dishName);
        assertFalse(dishCalendar.containsDish(dishName));
    }

    private void assertDeletionSuccessfulIngredient(String ingredientName) {
        assertTrue(IngredientStorage.contains(ingredientName));
        DeleteCommand.deleteIngredient("delete -ingredient=" + ingredientName);
        assertFalse(IngredientStorage.contains(ingredientName));
    }

    private void assertDeletionSuccessfulRecipe(String recipeName) {
        assertTrue(RecipeBank.contains(recipeName));
        DeleteCommand.deleteRecipe("delete -recipe=" + recipeName);
        assertFalse(RecipeBank.contains(recipeName));
    }

    private void assertDeletionFailsDueToNoSuchIngredient(String ingredientName) {
        DeleteCommand.deleteIngredient("delete -ingredient=" + ingredientName);
        assertFalse(IngredientStorage.contains(ingredientName));
    }

    private void assertDeletionFailsDueToNoSuchRecipe(String recipeName) {
        DeleteCommand.deleteRecipe("delete -recipe=" + recipeName);
        assertFalse(RecipeBank.contains(recipeName));
    }
}
