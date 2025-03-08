package seedu.cookingaids.Collections;

import java.util.ArrayList;

import seedu.cookingaids.Items.Recipe;

public class RecipeBank {

    static ArrayList<Recipe> recipeBank = new ArrayList<Recipe>();

    // c

    // shld it take in a fully-formed object?
    public void addRecipeToRecipeBank(Recipe recipe) {
        recipeBank.add(recipe);
    }

    public static ArrayList<Recipe> getRecipeBank() {
        return recipeBank;
    }

    // d

    public void removeRecipeFromRecipeBank(String recipeName) {
        for (int i = 0; i < recipeBank.size(); i++) {
            if (recipeBank.get(i).getRecipeName() == recipeName) {
                recipeBank.remove(i);
                return;
            }
        }

    }
}