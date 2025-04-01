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

            ArrayList<Ingredient> ingredients = new ArrayList<>();

            if (!ingredientsString.isEmpty()) {
                String[] pairsArray = ingredientsString.split(",");

                // Check if we have an even number of elements (pairs of ingredient and quantity)
                if (pairsArray.length % 2 != 0) {
                    throw new InvalidInputException();
                }

                // Process pairs of ingredient name and quantity
                for (int i = 0; i < pairsArray.length; i += 2) {
                    String ingredientName = pairsArray[i].trim();
                    String quantityStr = pairsArray[i + 1].trim();

                    try {
                        int quantity = Integer.parseInt(quantityStr);
                        // Assuming a static counter or method to generate IDs
                        Ingredient ingredient = new Ingredient(ingredientName, quantity);
                        ingredients.add(ingredient);
                    } catch (NumberFormatException e) {
                        throw new InvalidInputException();
                    }
                }
            }
            else {
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

            System.out.println("Invalid format, recipe should have at least one ingredient, with ingredients and quantities in pairs" +
                    " (use -needs=ingredient_1,quantity_1,ingredient_2,quantity_2)");

//            System.out.println("Invalid format, " +
//                    "recipe should have at least one ingredient (use -needs=ingredientName)");
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
        try {
            String inputs = removeCommandWord(receivedText);
            HashMap<String, String> ingredientFields = Parser.parseIngredient(inputs);
            if (ingredientFields == null) {
                System.out.println("Invalid format. Please make sure your flags are spelled correctly, and date is in "
                        + "YYYY/MM/DD format");
                return;
            }
            assert ingredientFields.containsKey("ingredient") : "Missing 'ingredient' key";
            assert ingredientFields.containsKey("expiry_date") : "Missing 'expiry_date' key";
            assert ingredientFields.containsKey("quantity") : "Missing 'quantity' key";
            String ingredientName = ingredientFields.get("ingredient");
            if (ingredientName.isEmpty()) {
                throw new IllegalArgumentException("Ingredient name cannot be empty");
            }
            String expiryDate = ingredientFields.get("expiry_date");
            if (expiryDate.isEmpty()) {
                throw new IllegalArgumentException("Expiry date cannot be empty");
            }
            int quantity;
            try {
                quantity = Integer.parseInt(ingredientFields.get("quantity"));
                if (quantity < 0) {
                    throw new IllegalArgumentException("Quantity must be a positive number");
                }
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Quantity must be a valid number");
            }
            assert quantity > 0 : "Quantity should be greater than zero";
            Ingredient ingredient = new Ingredient( ingredientName, expiryDate, quantity);
            IngredientStorage.addToStorage(ingredient);
            System.out.println("Added Ingredient: " + ingredient);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }
    private static String replaceSpaceWithUnderscore(String input){
        return input.replace(" ", "_");
    }
}
