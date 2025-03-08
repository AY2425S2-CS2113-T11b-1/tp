package seedu.cookingaids.Commands;

import seedu.cookingaids.Collections.DishCalendar;
import seedu.cookingaids.Collections.RecipeBank;
import seedu.cookingaids.Collections.IngredientStorage;
import seedu.cookingaids.Items.Dish;
import seedu.cookingaids.Items.Recipe;
import seedu.cookingaids.Items.Ingredient;
import seedu.cookingaids.Parser.Parser;

import java.util.ArrayList;
import java.util.HashMap;

public class AddCommand {
    public final static String COMMAND_WORD = "add";
    final static int SPACE = 1;

    static public void addDish(String receivedText){
        receivedText = receivedText.substring(COMMAND_WORD.length() + SPACE);
        String[] dishFields = Parser.parseDish(receivedText);
        Dish dish = new Dish(Integer.parseInt(dishFields[0]),dishFields[1],dishFields[2]);
        DishCalendar.addDishToCalendar(dish);
        System.out.println("Added Dish: "+dish.getName() +", Scheduled for: "+ dish.getDishDate().toString());
    }

    static public void addRecipe(String receivedText){
        receivedText = receivedText.substring(COMMAND_WORD.length() + SPACE);
        String[] recipeFields = Parser.parseRecipe(receivedText);

        String recipeName = recipeFields[0];
        String ingredientsString = recipeFields[1];

        // Process ingredients string into ArrayList
        ArrayList<String> ingredients = new ArrayList<>();
        if (!ingredientsString.isEmpty()) {
            String[] ingredientArray = ingredientsString.split(",");
            for (String ingredient : ingredientArray) {
                ingredients.add(ingredient.trim());
            }
        }

        // Create Recipe object
        Recipe recipe;
        if (ingredients.isEmpty()) {
            recipe = new Recipe(recipeName);
        } else {
            recipe = new Recipe(recipeName, ingredients);
        }

        // Add to RecipeBank
        RecipeBank recipeBank = new RecipeBank();
        recipeBank.addRecipeToRecipeBank(recipe);

        // Print confirmation
        System.out.println("Added Recipe: " + recipeName);
        if (!ingredients.isEmpty()) {
            System.out.println("Ingredients: " + ingredients);
        } else {
            System.out.println("No ingredients specified.");
        }
    }



    public static void addIngredient(String receivedText) {
        String inputs = receivedText.substring(COMMAND_WORD.length() + SPACE);
        HashMap<String, String> ingredientFields = Parser.parseIngredient(inputs);
        Ingredient ingredient = new Ingredient(1,ingredientFields.get("ingredient"), ingredientFields.get("expiry_date"),
                Integer.parseInt(ingredientFields.get("quantity")));
        IngredientStorage.addToStorage(ingredient);
        System.out.println("Added Ingredient: " + ingredient);
    }
}
