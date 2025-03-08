package seedu.cookingaids.Items;

import java.util.ArrayList;

public class Recipe {
    private String recipeName;
    private ArrayList<String> ingredients;

    public Recipe(String recipeName, ArrayList<String> ingredients) {
        this.recipeName = recipeName;
        this.ingredients = ingredients;
    }

    public Recipe(String recipeName) {
        this.recipeName = recipeName;
        this.ingredients = new ArrayList<>();
    }

    public String getRecipeName() {
        return this.recipeName;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    public ArrayList<String> getIngredients() {
        return this.ingredients;
    }

    public void setIngredients(ArrayList<String> ingredients) {
        this.ingredients = ingredients;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return "Recipe named '" + recipeName + "'" +
                "calls for ingredients" + ingredients ;
    }
}