package seedu.cookingaids.commands;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import seedu.cookingaids.collections.DishCalendar;
import seedu.cookingaids.collections.IngredientStorage;
import seedu.cookingaids.collections.RecipeBank;
import seedu.cookingaids.collections.ShoppingList;
import seedu.cookingaids.exception.InvalidInputException;
import seedu.cookingaids.exception.OverflowQuantityException;
import seedu.cookingaids.items.Dish;
import seedu.cookingaids.items.Ingredient;
import seedu.cookingaids.items.Recipe;
import seedu.cookingaids.logger.LoggerFactory;
import static seedu.cookingaids.parser.Parser.DISH_FLAG;
import static seedu.cookingaids.parser.Parser.INGREDIENT_FLAG;
import static seedu.cookingaids.parser.Parser.RECIPE_FLAG;
import static seedu.cookingaids.parser.Parser.parseDish;
import static seedu.cookingaids.parser.Parser.parseIngredient;
import static seedu.cookingaids.parser.Parser.parseRecipe;
import seedu.cookingaids.storage.Storage;

public class AddCommand {
    public static final String COMMAND_WORD = "add";
    private static final int SPACE = 1;
    private static final Logger LOGGER = LoggerFactory.getLogger(AddCommand.class);

    /**
     * Removes the command word from the received input string.
     *
     * @param receivedText The full command input from the user.
     * @return The input string without the command word.
     */
    public static String removeCommandWord(String receivedText) {
        assert receivedText != null : "Received text should not be null";
        assert receivedText.length() > COMMAND_WORD.length() + SPACE : "Received text is too short to process";

        return receivedText.substring(COMMAND_WORD.length() + SPACE);
    }

    public static boolean isValidDate(String dateString) {
        switch (dateString) {
        case "tdy", "td", "today", "tomorrow", "tmr":
            return true;
        default:
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu/MM/dd")
                        .withResolverStyle(ResolverStyle.STRICT);
                LocalDate date = LocalDate.parse(dateString, formatter);
                if (date.isBefore(LocalDate.now())){
                    return false;
                }
                return true;
            } catch (DateTimeParseException e) {
                return false;
            }
        }
    }

    private static boolean hasConflictingFlags(String input, String... flags) {
        for (String flag : flags) {
            if (input.contains(flag)) {
                return true;
            }
        }
        return false;
    }

    private static void validateDishFlags(String text) {
        Pattern pattern = Pattern.compile("-(\\w+)=");
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            String key = matcher.group(1);
            if (!(key.equals("dish") || key.equals("when"))) {
                throw new IllegalArgumentException("Unexpected key: " + key);
            }
        }
    }

    private static void printDishResult(Dish dish, String inputDate) {
        String date = dish.getDishDate().toString();
        if (date.equals("None") && !inputDate.isEmpty()) {
            System.out.println("Could not recognise date! Saving without date");
        }
        System.out.println("Added Dish: " + dish.getName() +
                (date.equals("None") ? ", No scheduled date yet" : ", Scheduled for: " + date));
    }

    private static ArrayList<Ingredient> parseIngredients(String[] fields) throws InvalidInputException {
        ArrayList<Ingredient> ingredients = new ArrayList<>();
        for (int i = 0; i < fields.length; i += 2) {
            try {
                ingredients.add(new Ingredient(fields[i].trim(), Integer.parseInt(fields[i + 1].trim())));
            } catch (NumberFormatException e) {
                throw new InvalidInputException();
            }
        }
        return ingredients;
    }

    private static void saveAll() {
        LOGGER.info("Saving to file now");
        Storage.storeData(DishCalendar.getDishCalendar(),
                RecipeBank.getRecipeBank(), IngredientStorage.getStorage(),
                ShoppingList.getShoppingList());
    }

    /**
     * Replaces spaces in a string with underscores.
     *
     * @param input The input string.
     * @return The modified string with spaces replaced by underscores.
     */
    private static String replaceSpaceWithUnderscore(String input) {
        return input.replace(" ", "_");
    }

    /**
     * Adds a dish to the DishCalendar.
     *
     * @param receivedText The user input containing dish details.
     */
    public static void addDish(String receivedText) {
        try {
            if (hasConflictingFlags(receivedText, RECIPE_FLAG, INGREDIENT_FLAG)) {
                System.out.println("Other commands found, I can only process one at a time");
                return;
            }

            receivedText = removeCommandWord(receivedText);
            validateDishFlags(receivedText);

            String[] dishFields = parseDish(receivedText);
            if (!dishFields[1].isEmpty() && !isValidDate(dishFields[1])) {
                throw new InvalidInputException();
            }
            String dishName = dishFields[0];
            String dishDate = dishFields[1];
            // check if there are matching recipes. If not, warn the user that
            // needed ingredients for this might not be added to shopping list
            List<Recipe> matchingNames = RecipeBank.getRecipeByName(dishName);

            if (matchingNames.isEmpty()) {
                LOGGER.warning("No matching recipe found for name: " + dishName + "," +
                        " its ingredients may not be added to shopping list" );
            }

            Dish dish = new Dish(dishName, dishDate);
            DishCalendar.addDishToCalendar(dish);
            printDishResult(dish, dishFields[1]);
            saveAll();

        } catch (InvalidInputException e) {
            System.out.println("Invalid format. Use: add -dish=dish_name -when=YYYY/MM/DD " +
                    "\ndish name should be in lower_snake_case"+"\nonly dates starting from" +
                    " today onwards will be accepted");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Adds a recipe to the RecipeBank.
     *
     * @param receivedText The user input containing recipe details.
     */
    public static void addRecipe(String receivedText) {
        try {
            if (hasConflictingFlags(receivedText, DISH_FLAG, INGREDIENT_FLAG)) {
                System.out.println("Other commands found, I can only process one at a time");
                return;
            }

            receivedText = removeCommandWord(receivedText);
            String[] recipeFields = parseRecipe(receivedText);
            String[] pairsArray = recipeFields[1].split(",");

            if (pairsArray.length % 2 != 0 || pairsArray.length == 0) {
                throw new InvalidInputException();
            }

            ArrayList<Ingredient> ingredients = parseIngredients(pairsArray);
            Recipe recipe = new Recipe(replaceSpaceWithUnderscore(recipeFields[0]), ingredients);

            if (RecipeBank.getRecipeByName(recipeFields[0]).isEmpty()) {
                RecipeBank.addRecipeToRecipeBank(recipe);
                System.out.println("Added Recipe: " + recipeFields[0]);
                System.out.println("Ingredients: " + recipe.getIngredientsString());
                saveAll();
            } else {
                System.out.println("Recipe already exists: " + recipeFields[0]);
            }

        } catch (InvalidInputException e) {
            System.out.println("Invalid format, try add -recipe=recipeName -needs=ingredient_1,quantity_1");
        }
    }

    /**
     * Adds an ingredient to the IngredientStorage.
     *
     * @param receivedText The user input containing ingredient details.
     */
    public static void addIngredient(String receivedText) {
        try {
            String inputs = removeCommandWord(receivedText);
            HashMap<String, String> ingredientFields = parseIngredient(inputs);
            if (ingredientFields == null) {
                System.out.println("Invalid format. Use: add -ingredient=ingredient_name -expiry=YYYY/MM/DD " +
                        "-quantity=quantity, the only dashes should be for the flags");
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

            if (!expiryDate.equals("None") && !isValidDate(expiryDate)) {
                throw new IllegalArgumentException("Expiry date is invalid, \nensure that date is in" +
                        " YYYY/MM/DD format and only dates starting from today onwards will be accepted");
            }
            int quantity;
            try {
                quantity = Integer.parseInt(ingredientFields.get("quantity"));
                if (quantity < 0) {
                    throw new IllegalArgumentException("Quantity must be a positive integer");
                }
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Quantity must be a positive integer and less than " +
                        Integer.MAX_VALUE);
            }
            assert quantity > 0 : "Quantity should be greater than zero";
            Ingredient ingredient = new Ingredient(ingredientName, expiryDate, quantity);
            try {
                IngredientStorage.addToStorage(ingredient);
                System.out.println("Added Ingredient: " + ingredient);
            } catch (OverflowQuantityException e) {
                System.out.println("Maximum " + ingredient.getName() + " added, value will be capped at "
                        + Integer.MAX_VALUE +
                        " please go use them :D");
            }


            LOGGER.info("saving to file now:");
            Storage.storeData(DishCalendar.getDishCalendar(),
                    RecipeBank.getRecipeBank(), IngredientStorage.getStorage(),
                    ShoppingList.getShoppingList());
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }
}
