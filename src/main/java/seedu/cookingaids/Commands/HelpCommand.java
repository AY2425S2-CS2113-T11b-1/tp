package seedu.cookingaids.Commands;

public class HelpCommand {

    public final static String COMMAND_WORD = "help";

    public static void showHelp() {
        System.out.println("Available commands: list, display, help");
    }

}