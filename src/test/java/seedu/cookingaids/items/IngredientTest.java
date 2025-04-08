package seedu.cookingaids.items;

import org.junit.jupiter.api.Test;
import seedu.cookingaids.exception.OverflowQuantityException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class IngredientTest {

    @Test
    void constructor_inputNameQuantity_expectReceived() {
        Ingredient ingredient = new Ingredient( "Tomato", 5);

        assertEquals("Tomato", ingredient.getName());
        assertEquals(5, ingredient.getQuantity());
        assertEquals("None", ingredient.getExpiryDate().toString());
    }

    @Test
    void constructor_inputNameExpiryQuantity_expectReceived() {
        Ingredient ingredient = new Ingredient( "Milk", "2025-04-15", 10);

        assertEquals("Milk", ingredient.getName());
        assertEquals(10, ingredient.getQuantity());
        assertEquals("2025/04/15", ingredient.getExpiryDate().toString());
    }

    @Test
    void addQuantity_positiveValue_expectIncreasedQuantity() throws OverflowQuantityException {
        Ingredient ingredient = new Ingredient( "Eggs", 12);
        ingredient.addQuantity(8);
        assertEquals(20, ingredient.getQuantity());
    }

    @Test
    void removeQuantity_positiveValue_expectDecreasedQuantity() {
        Ingredient ingredient = new Ingredient( "Butter", 15);
        ingredient.removeQuantity(5);
        assertEquals(10, ingredient.getQuantity());
    }

    @Test
    void removeQuantity_exceedingAmount_expectNegativeQuantity() {
        Ingredient ingredient = new Ingredient( "Flour", 3);
        ingredient.removeQuantity(10);
        assertEquals(-7, ingredient.getQuantity());
    }

    @Test
    void toString_withExpiry_expectCorrectFormat() {
        Ingredient ingredient = new Ingredient( "Sugar", "2025/06/30", 25);
        assertEquals("Sugar (25g, Expiry: 2025/06/30, Expiring Soon: No, Expired: No)", ingredient.toString());
    }

    @Test
    void toString_withoutExpiry_expectCorrectFormat() {
        Ingredient ingredient = new Ingredient("Salt", 50);
        assertEquals("Salt (50g, Expiry: None, Expiring Soon: No, Expired: No)", ingredient.toString());
    }

    @Test
    void displayInfo_expectCorrectOutput() {
        Ingredient ingredient = new Ingredient("Cheese", "2025/12/01", 7);
        ingredient.displayInfo();
        // This will print to console, no assertion needed for manual checking
    }

    @Test
    void setExpiringSoon_trueValue_expectFlagSet() {
        Ingredient ingredient = new Ingredient( "Yogurt", "2025/11/01", 12);
        ingredient.setExpiringSoon(true);
        assertTrue(ingredient.toString().contains("Expiring Soon: Yes"));
    }

    @Test
    void setExpiringSoon_falseValue_expectFlagUnset() {
        Ingredient ingredient = new Ingredient( "Bread", "2025/08/15", 6);
        ingredient.setExpiringSoon(false);
        assertTrue(ingredient.toString().contains("Expiring Soon: No"));
    }

    @Test
    void toString_expiringSoonTrue_expectCorrectFormat() {
        Ingredient ingredient = new Ingredient( "Milk", "2025/05/10", 20);
        ingredient.setExpiringSoon(true);
        assertEquals("Milk (20ml, Expiry: 2025/05/10, Expiring Soon: Yes, Expired: No)", ingredient.toString());
    }

    @Test
    void toString_expiringSoonFalse_expectCorrectFormat() {
        Ingredient ingredient = new Ingredient( "Butter", "2025/05/10", 15);
        ingredient.setExpiringSoon(false);
        assertEquals("Butter (15g, Expiry: 2025/05/10, Expiring Soon: No, Expired: No)", ingredient.toString());
    }

    @Test
    void setExpired_trueValue_expectFlagSet() {
        Ingredient ingredient = new Ingredient( "Cheese", "2025/01/01", 7);
        ingredient.setExpired(true);
        assertTrue(ingredient.toString().contains("Expired: Yes"));
    }

    @Test
    void setExpired_falseValue_expectFlagUnset() {
        Ingredient ingredient = new Ingredient( "Butter", "2025/12/31", 5);
        ingredient.setExpired(false);
        assertTrue(ingredient.toString().contains("Expired: No"));
    }
}
