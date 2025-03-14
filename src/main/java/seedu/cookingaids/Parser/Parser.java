package seedu.cookingaids.Parser;

import seedu.cookingaids.Commands.AddCommand;
import seedu.cookingaids.Commands.DeleteCommand;
import seedu.cookingaids.Commands.DisplayCommand;
import seedu.cookingaids.Commands.HelpCommand;
import seedu.cookingaids.Ui.Ui;
import java.util.HashMap;
import java.util.regex.Pattern;

public class Parser {
    private static final String UNKNOWN_COMMAND_STR = "Unknown command: %s";
    private static final String RECIPE_FLAG = "-recipe=";
    private static final String CALLS_FOR_FLAG = "-callsfor=";
    private static final String DISH_FLAG = "-dish=";
    private static final String WHEN_FLAG = "-when=";
    private static final String INGREDIENT_FLAG = "-ingredient=";

    public static void decipherCommand(String receivedText) {
        String command = receivedText.strip().split(" ")[0];

        switch (command) {
        case DisplayCommand.COMMAND_WORD -> handleDisplayCommand(receivedText);
        case AddCommand.COMMAND_WORD -> handleAddCommand(receivedText);
        case DeleteCommand.COMMAND_WORD -> handleDeleteCommand(receivedText);
        case HelpCommand.COMMAND_WORD -> HelpCommand.showHelp();
        default -> {
            System.out.println(String.format(UNKNOWN_COMMAND_STR, receivedText));
            System.out.println("Type \"help\" to see available commands.");
        }
        }
        Ui.printLineDivider();
    }

    private static void handleDisplayCommand(String receivedText) {
        if (receivedText.contains("-recipe")) {
            DisplayCommand.displayRecipeBank();
        } else if (receivedText.contains("-dish")) {
            DisplayCommand.displayDishCalendar();
        } else {
            System.out.printf((UNKNOWN_COMMAND_STR) + "%n", receivedText);
        }
    }

    private static void handleAddCommand(String receivedText) {
        if (receivedText.contains(RECIPE_FLAG)) {
            AddCommand.addRecipe(receivedText);
        } else if (receivedText.contains(DISH_FLAG)) {
            AddCommand.addDish(receivedText);
        } else {
            System.out.println("I DO NOT UNDERSTAND " + receivedText);
        }
    }

    private static void handleDeleteCommand(String receivedText) {
        if (receivedText.contains(RECIPE_FLAG)) {
            DeleteCommand.deleteRecipe(parseRecipeForDeletion(receivedText));
        } else if (receivedText.contains(DISH_FLAG) && receivedText.contains(WHEN_FLAG)) {
            String[] data = parseDishAndWhenForDeletion(receivedText);
            DeleteCommand.deleteDishWithWhen(receivedText);
        } else if (receivedText.contains(DISH_FLAG)) {
            DeleteCommand.deleteDish(parseDishNameForDeletion(receivedText));
        } else if (receivedText.contains(WHEN_FLAG)) {
            DeleteCommand.deleteDishByWhen(parseWhenForDeletion(receivedText));
        } else if (receivedText.contains(INGREDIENT_FLAG)) {
            DeleteCommand.deleteIngredient(parseIngredientForDeletion(receivedText));
        } else {
            System.out.println("Invalid delete command: " + receivedText);
        }
    }

    public static String parseDishNameForDeletion(String receivedText) {
        if (!receivedText.contains(DISH_FLAG)) {
            return "";
        }
        int startIndex = receivedText.indexOf(DISH_FLAG) + DISH_FLAG.length();
        int endIndex = receivedText.indexOf(" ", startIndex);
        if (endIndex == -1) endIndex = receivedText.length();

        return receivedText.substring(startIndex, endIndex).trim();
    }

    public static String parseWhenForDeletion(String receivedText) {
        if (!receivedText.contains(WHEN_FLAG)) {
            return "";
        }
        int startIndex = receivedText.indexOf(WHEN_FLAG) + WHEN_FLAG.length();
        int endIndex = receivedText.indexOf(" ", startIndex);
        if (endIndex == -1) endIndex = receivedText.length();

        return receivedText.substring(startIndex, endIndex).trim();
    }

    public static String[] parseDishAndWhenForDeletion(String receivedText) {
        String dishName = parseDishNameForDeletion(receivedText);
        String when = parseWhenForDeletion(receivedText);
        return new String[]{dishName, when};
    }

    public static String parseIngredientForDeletion(String receivedText) {
        if (!receivedText.contains(INGREDIENT_FLAG)) {
            return "";
        }
        int startIndex = receivedText.indexOf(INGREDIENT_FLAG) + INGREDIENT_FLAG.length();
        int endIndex = receivedText.indexOf(" ", startIndex);
        if (endIndex == -1) endIndex = receivedText.length();

        return receivedText.substring(startIndex, endIndex).trim();
    }

    public static String parseRecipeForDeletion(String receivedText) {
        if (!receivedText.contains(RECIPE_FLAG)) {
            return "";
        }
        int startIndex = receivedText.indexOf(RECIPE_FLAG) + RECIPE_FLAG.length();
        int endIndex = receivedText.indexOf(" ", startIndex);
        if (endIndex == -1) endIndex = receivedText.length();

        return receivedText.substring(startIndex, endIndex).trim();
    }

    public static String[] parseDish(String receivedText) {
        String[] returnedArray = {"1", "none", "none"};

        if (!receivedText.contains("-dish=")) {
            return returnedArray; // If no dish flag, return default
        }

        int startIndex = receivedText.indexOf("-dish=") + "-dish=".length();
        int endIndex = receivedText.indexOf(" ", startIndex); // Look for the next space
        if (endIndex == -1) endIndex = receivedText.length(); // If no space, take the rest

        String dishName = receivedText.substring(startIndex, endIndex).trim();
        returnedArray[1] = dishName;

        return returnedArray;
    }

    public static String[] parseRecipe(String receivedText) {
        String[] returnedArray = {"none", ""};

        try {
            if (receivedText.contains(RECIPE_FLAG)) {
                int recipeStartIndex = receivedText.indexOf(RECIPE_FLAG) + RECIPE_FLAG.length();
                int recipeEndIndex = receivedText.contains(CALLS_FOR_FLAG)
                        ? receivedText.indexOf(CALLS_FOR_FLAG)
                        : receivedText.length();
                returnedArray[0] = receivedText.substring(recipeStartIndex, recipeEndIndex).trim();
            }
        } catch (Exception ignored) {}

        try {
            if (receivedText.contains(CALLS_FOR_FLAG)) {
                int ingredientsStartIndex = receivedText.indexOf(CALLS_FOR_FLAG) + CALLS_FOR_FLAG.length();
                if (ingredientsStartIndex < receivedText.length()) {
                    returnedArray[1] = receivedText.substring(ingredientsStartIndex).trim();
                }
            }
        } catch (Exception ignored) {}

        return returnedArray;
    }

    public static HashMap<String, String> parseIngredient(String command) {
        HashMap<String, String> data = new HashMap<>();
        String[] parts = command.split(" ");
        Pattern quantityPattern = Pattern.compile("-\\d+");

        for (String part : parts) {
            if (quantityPattern.matcher(part).matches()) {
                data.put("quantity", part.substring(1));
            } else if (part.startsWith("-")) {
                String key = isDate(part.substring(1)) ? "expiry_date" : "ingredient";
                data.put(key, part.substring(1));
            }
        }

        data.putIfAbsent("expiry_date", "None");
        return data;
    }

    public static boolean isDate(String input) {
        return input.matches("\\d{4}-\\d{2}-\\d{2}") || input.matches("\\d{1,2}.*[A-Za-z]+.*");
    }
}