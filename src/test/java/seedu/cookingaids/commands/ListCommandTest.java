package seedu.cookingaids.commands;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seedu.cookingaids.collections.DishCalendar;
import seedu.cookingaids.collections.IngredientStorage;
import seedu.cookingaids.collections.RecipeBank;
import seedu.cookingaids.exception.InvalidInputException;
import seedu.cookingaids.exception.OverflowQuantityException;
import seedu.cookingaids.items.Dish;
import seedu.cookingaids.items.Ingredient;
import seedu.cookingaids.items.Recipe;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ListCommandTest {

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
    void execute_listDishes_displaysDishList() throws InvalidInputException {
        Dish testDish = new Dish("spaghetti", "2025/03/29");
        DishCalendar.addDishToCalendar(testDish);

        ListCommand.displayDishList("list -dish");

        String commandOutput = outputStream.toString().trim();
        assertTrue(commandOutput.contains("spaghetti"));

        DishCalendar.clear();
    }

    @Test
    void execute_listDishesByMonth_displaysMonthlyDishList() throws InvalidInputException {
        Dish marchDish = new Dish("pancakes", "2025/03/15");
        DishCalendar.addDishToCalendar(marchDish);

        ListCommand.displayDishList("list -dish");

        String commandOutput = outputStream.toString().trim();
        assertTrue(commandOutput.contains("pancakes"));

        DishCalendar.clear();
    }

    @Test
    void execute_listIngredients_displaysIngredientList() throws OverflowQuantityException {
        Ingredient tomato = new Ingredient("tomato");
        IngredientStorage.addToStorage(tomato);

        ListCommand.displayIngredients();

        String commandOutput = outputStream.toString().trim();
        assertTrue(commandOutput.contains("tomato"));

        IngredientStorage.clear();
    }

    @Test
    void execute_listRecipes_displaysRecipeList() {
        ArrayList<Ingredient> ingredients = new ArrayList<>();
        ingredients.add(new Ingredient("bread", 2));
        ingredients.add(new Ingredient("tomato", 1));
        ingredients.add(new Ingredient("egg", 1));
        Recipe sandwich = new Recipe("sandwich", ingredients);
        RecipeBank.addRecipeToRecipeBank(sandwich);

        ListCommand.displayRecipeBank();

        String commandOutput = outputStream.toString().trim();
        assertTrue(commandOutput.contains("sandwich"));

        RecipeBank.clear();
    }
}
