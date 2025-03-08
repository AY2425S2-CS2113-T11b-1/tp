package seedu.cookingaids.Commands;

import seedu.cookingaids.Collections.DishCalendar;
import seedu.cookingaids.Collections.RecipeBank;
import seedu.cookingaids.Items.Dish;
import seedu.cookingaids.Items.Recipe;
import seedu.cookingaids.Ui.Ui;

import java.util.ArrayList;

public class DisplayCommand {
    public final static String COMMAND_WORD = "list";

    public static void displayDishCalendar(){
        ArrayList<Dish> listOfDish = DishCalendar.getDishCalendar();
        Ui.printDishListView(listOfDish);

    }

    public static void displayRecipeBank(){
        // This should ideally be a singleton or accessed through a proper controller
//        RecipeBank recipeBank = new RecipeBank();
        ArrayList<Recipe> listOfRecipes = RecipeBank.getRecipeBank();
        Ui.printRecipeListView(listOfRecipes);
    }
}

