package seedu.cookingaids.collections;


import seedu.cookingaids.exception.OverflowQuantityException;
import seedu.cookingaids.items.Ingredient;

import java.util.ArrayList;
import java.util.List;


public class ShoppingList {
    private static ArrayList<Ingredient> shoppingList = new ArrayList<>();
    public static void initializeShoppingList(List<Ingredient> ingredients) {
        shoppingList.addAll(ingredients);
    }
    public static ArrayList<Ingredient> getShoppingList() {
        return shoppingList;
    }

    public static int getIngredientQuantity(String ingredientName) {
        for (Ingredient item : shoppingList) {
            if (item.getName().equals(ingredientName)) {// Update quantity
                return item.getQuantity();
            }

        }
        return 0;

    }



    public static void addToShoppingList(Ingredient ingredient) {
        for (Ingredient item : shoppingList) {
            if (item.getName().equals(ingredient.getName())) {
                try {
                    item.addQuantity(ingredient.getQuantity()); // Update quantity
                    return;
                } catch (OverflowQuantityException e) {
                    System.out.println("Maximum " + ingredient.getName() + " added into sh" +
                            "opping list, value will be capped at " + Integer.MAX_VALUE +
                            " stop trying to break my program!:<");
                    return;
                }
            }
        }

        shoppingList.add(ingredient);
    }

    public static boolean contains(String ingredientName) {
        return shoppingList.stream().anyMatch(ingredient -> ingredient.getName().equalsIgnoreCase(ingredientName));
    }

    public static Ingredient removeFromShoppingList(Ingredient ingredient) {
        for (Ingredient item : shoppingList) {
            if (item.getName().equals(ingredient.getName())) {
                if (item.getQuantity() > ingredient.getQuantity()) {
                    item.removeQuantity(ingredient.getQuantity()); // Update quantity
                    return null;

                }
                if (item.getQuantity() == ingredient.getQuantity()) {
                    shoppingList.remove(item);
                    return null;
                }
                shoppingList.remove(item);
                ingredient.removeQuantity(item.getQuantity());

            }

        }
        return ingredient;

    }
}

