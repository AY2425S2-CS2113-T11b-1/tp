package seedu.cookingaids.Parser;


import seedu.cookingaids.Commands.AddCommand;
import seedu.cookingaids.Commands.DisplayCommand;
import seedu.cookingaids.Commands.HelpCommand;
import seedu.cookingaids.Ui.Ui;
import java.util.HashMap;
import java.util.regex.Pattern;

/**
 * Holds methods that manipulate input received from user into useful data
 * that can be understood by other methods
 */
public class Parser {
    /**
     * deciphers users input for commands and calls methods to execute
     * if unable to decipher message display instructions that helps user give commands
     *
     * @param receivedText entire command input received
     */
    public static void decipherCommand(String receivedText) {//method to determine what kind of task to call

        String command = receivedText.strip().split(" ")[0];
//        switch (command) {
//        case DisplayCommand.COMMAND_WORD:
//            DisplayCommand.displayDishCalendar(); //create Todo task
//            Ui.printLineDivider();
//            break;
//
//        case AddCommand.COMMAND_WORD:
//
//            AddCommand.addDish(receivedText); //create Todo task
//            Ui.printLineDivider();
//            break;
//
//        default:
//            System.out.println("I DO NOT UNDERSTAND " + receivedText);
//            System.out.println("type \"help\" to see available commands");
//
//        }

        if (command.equals(DisplayCommand.COMMAND_WORD)) {
            if (receivedText.contains("-recipe")) {
//                AddCommand.addRecipe(receivedText); // Add a recipe
                DisplayCommand.displayRecipeBank();
            } else if (receivedText.contains("-dish")) {
//                AddCommand.addDish(receivedText); // Create Todo task
                DisplayCommand.displayDishCalendar();
            } else  {
                System.out.println("I DO NOT UNDERSTAND " + receivedText);
            }
//            DisplayCommand.displayDishCalendar(); //create Todo task
//            Ui.printLineDivider();
        } else if (command.equals(AddCommand.COMMAND_WORD)) {
            if (receivedText.contains("-recipe=")) {
                AddCommand.addRecipe(receivedText); // Add a recipe
            } else if (receivedText.contains("-dish=")) {
                AddCommand.addDish(receivedText); // Create Todo task
            } else {
                System.out.println("I DO NOT UNDERSTAND " + receivedText);
            }
//            Ui.printLineDivider();
        } else if (command.equals(HelpCommand.COMMAND_WORD)) {
            HelpCommand.showHelp();
        } else {
            System.out.println("I DO NOT UNDERSTAND " + receivedText);
            System.out.println("type \"help\" to see available commands");
        }

        Ui.printLineDivider();


    }

    public static String[] parseDish(String receivedText) {
        String[] returnedArray = new String[3];

        returnedArray[0] = "1";
        // saving text in front of /by as description, starting from after "deadline"
        try {

            returnedArray[1] = receivedText.split(" ")[0];
        } catch (ArrayIndexOutOfBoundsException e) {
            returnedArray[1] = "none";
        }
        try {
            returnedArray[2] = receivedText.split(" ")[1];
        } catch (ArrayIndexOutOfBoundsException e) {
            returnedArray[2] = "none";
        }
        return returnedArray;


    }

    public static String[] parseRecipe(String receivedText) {
        String[] returnedArray = new String[2];

        // Default values
        returnedArray[0] = "none"; // Recipe name
        returnedArray[1] = ""; // Ingredients string

        try {
            // Extract recipe name
            if (receivedText.contains("-recipe=")) {
                int recipeStartIndex = receivedText.indexOf("-recipe=") + 8;
                int recipeEndIndex;

                if (receivedText.contains("-callsfor=")) {
                    recipeEndIndex = receivedText.indexOf("-callsfor=");
                } else {
                    recipeEndIndex = receivedText.length();
                }

                returnedArray[0] = receivedText.substring(recipeStartIndex, recipeEndIndex).trim();
            }
        } catch (Exception e) {
            returnedArray[0] = "none";
        }

        try {
            // Extract ingredients string
            if (receivedText.contains("-callsfor=")) {
                int ingredientsStartIndex = receivedText.indexOf("-callsfor=") + 10;
                if (ingredientsStartIndex < receivedText.length()) {
                    returnedArray[1] = receivedText.substring(ingredientsStartIndex).trim();
                }
            }
        } catch (Exception e) {
            returnedArray[1] = "";
        }

        return returnedArray;
    }

    public static HashMap<String, String> parseIngredient(String command) {
        HashMap<String, String> data = new HashMap<>();
        String[] parts = command.split(" ");
        Pattern quantityPattern = Pattern.compile("-\\d+"); // Matches numbers like -5, -10

        for (String part : parts) {
            if (quantityPattern.matcher(part).matches()) {
                data.put("quantity", part.substring(1)); // Remove "-"
            } else if (part.startsWith("-")) {
                if (isDate(part.substring(1))) {
                    data.put("expiry_date", part.substring(1));
                } else {
                    data.put("ingredient", part.substring(1));
                }
            }
        }

        if (!data.containsKey("expiry_date")) {
            data.put("expiry_date", "None");  // Default value if no expiry date is provided
        }

        return data;
    }

    /**
     * Checks if the input could be a date in various formats.
     */
    public static boolean isDate(String input) {
        // Matches YYYY-MM-DD or date formats with alphabets like "31 Dec"
        return input.matches("\\d{4}-\\d{2}-\\d{2}") || input.matches("\\d{1,2}.*[A-Za-z]+.*");
    }



}