package seedu.cookingaids.commands;

import seedu.cookingaids.collections.DishCalendar;
import seedu.cookingaids.collections.RecipeBank;
import seedu.cookingaids.collections.IngredientStorage;
import seedu.cookingaids.exception.InvalidInputException;
import seedu.cookingaids.items.Dish;
import seedu.cookingaids.items.Recipe;
import seedu.cookingaids.items.Ingredient;
import seedu.cookingaids.parser.Parser;

import java.util.ArrayList;
import java.util.HashMap;

public class AddCommand {
    public static final String COMMAND_WORD = "add";
    private static final int SPACE = 1;
    private static final String INGREDIENT_SEPARATOR = ",";

    public static String removeCommandWord(String receivedText) {
        assert receivedText != null : "Received text should not be null";
        assert receivedText.length() > COMMAND_WORD.length() + SPACE : "Received text is too short to process";

        return receivedText.substring(COMMAND_WORD.length() + SPACE);
    }

    public static void addDish(String receivedText) {
        try {
            receivedText = removeCommandWord(receivedText);
            String[] dishFields = Parser.parseDish(receivedText);

            assert dishFields != null : "Dish fields should not be null";
            assert dishFields.length == 2 : "Dish fields should contain exactly two elements";

            Dish dish = new Dish(dishFields[0], dishFields[1]);
            DishCalendar.addDishToCalendar(dish);

            String date = dish.getDishDate().toString();
            assert date != null : "Dish date should not be null";

            if (date.isEmpty()) {
                System.out.println("Added Dish: " + dish.getName() + ", No scheduled date yet");
            } else {
                System.out.println("Added Dish: " + dish.getName() + ", Scheduled for: " + date);
            }
        } catch (InvalidInputException e) {
            System.out.println("Invalid format. Use: add -dish=dish_name -when=YYYY-MM-DD");
        }
    }

    public static void addRecipe(String receivedText) {

        try {
            receivedText = removeCommandWord(receivedText);
            String[] recipeFields = Parser.parseRecipe(receivedText);

            assert recipeFields != null : "Recipe fields should not be null";
            assert recipeFields.length == 2 : "Recipe fields should contain exactly two elements";

            String recipeName = recipeFields[0];
            String ingredientsString = recipeFields[1];

            ArrayList<String> ingredients = new ArrayList<>();

            if (!ingredientsString.isEmpty()) {
                String[] ingredientArray = ingredientsString.split(",");
                for (String ingredient : ingredientArray) {
                    ingredients.add(ingredient.trim());
                }
            } else {
                throw new InvalidInputException();
            }

            assert recipeName != null && !recipeName.isEmpty() : "Recipe name should not be empty";
            Recipe recipe = ingredients.isEmpty()
                    ? new Recipe(replaceSpaceWithUnderscore(recipeName))
                    : new Recipe(replaceSpaceWithUnderscore(recipeName), ingredients);
            RecipeBank.addRecipeToRecipeBank(recipe);
            System.out.println("Added Recipe: " + recipeName);
            System.out.println("Ingredients: " + ingredients);
        } catch (InvalidInputException e) {
            System.out.println("Invalid format, recipe should have at least one ingredient (use -needs=ingredientName)");
        }

    }

    private static ArrayList<String> parseIngredients(String ingredientsString) {
        ArrayList<String> ingredients = new ArrayList<>();
        if (!ingredientsString.isEmpty()) {
            String[] ingredientArray = ingredientsString.split(INGREDIENT_SEPARATOR);
            for (String ingredient : ingredientArray) {
                ingredients.add(ingredient.trim());
            }
        }
        return ingredients;
    }

    public static void addIngredient(String receivedText) {
        String inputs = removeCommandWord(receivedText);
        HashMap<String, String> ingredientFields = Parser.parseIngredient(inputs);

        assert ingredientFields != null : "Parsed ingredient fields should not be null";
        assert ingredientFields.containsKey("ingredient") : "Missing 'ingredient' key";
        assert ingredientFields.containsKey("expiry_date") : "Missing 'expiry_date' key";
        assert ingredientFields.containsKey("quantity") : "Missing 'quantity' key";

        String ingredientName = ingredientFields.get("ingredient");
        String expiryDate = ingredientFields.get("expiry_date");
        int quantity = Integer.parseInt(ingredientFields.get("quantity"));

        assert ingredientName != null && !ingredientName.isEmpty() : "Ingredient name should not be empty";
        assert expiryDate != null && !expiryDate.isEmpty() : "Expiry date should not be empty";
        assert quantity > 0 : "Quantity should be greater than zero";

        Ingredient ingredient = new Ingredient(1, ingredientName, expiryDate, quantity);
        IngredientStorage.addToStorage(ingredient);

        System.out.println("Added Ingredient: " + ingredient);
    }

    private static String replaceSpaceWithUnderscore(String input){
        return input.replace(" ", "_");
    }
}
