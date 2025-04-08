package seedu.cookingaids.collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seedu.cookingaids.exception.OverflowQuantityException;
import seedu.cookingaids.items.Ingredient;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class IngredientStorageTest {

    private Ingredient ingredient1;
    private Ingredient ingredient2;
    private Ingredient ingredient3;
    private Ingredient expiredIngredient;

    @BeforeEach
    public void setUp() {
        IngredientStorage.clear();

        this.ingredient1 = new Ingredient( "Sugar", "2025-12-31", 500);
        this.ingredient2 = new Ingredient( "Salt", "2025-06-30", 300);
        this.ingredient3 = new Ingredient( "Sugar", "2025-12-31", 200);

        // Already expired (yesterday)
        String yesterday = LocalDate.now().minusDays(1).toString();
        this.expiredIngredient = new Ingredient( "Butter", yesterday, 50);
    }

    @Test
    public void testAddToStorage_newIngredient() throws OverflowQuantityException {
        IngredientStorage.addToStorage(ingredient1);

        assertTrue(IngredientStorage.contains("Sugar"));
        List<Ingredient> sugarList = IngredientStorage.getIngredients("Sugar");
        assertEquals(1, sugarList.size());
        assertEquals(500, sugarList.get(0).getQuantity());
    }

    @Test
    public void testAddToStorage_mergeQuantity() throws OverflowQuantityException {
        IngredientStorage.addToStorage(ingredient1);
        IngredientStorage.addToStorage(ingredient3);

        List<Ingredient> sugarList = IngredientStorage.getIngredients("Sugar");
        assertEquals(1, sugarList.size());
        assertEquals(700, sugarList.get(0).getQuantity());
    }

    @Test
    public void testAddToStorage_differentExpiryDates() throws OverflowQuantityException {
        Ingredient differentExpiry = new Ingredient( "Sugar", "2026-01-01", 200);

        IngredientStorage.addToStorage(ingredient1);
        IngredientStorage.addToStorage(differentExpiry);

        List<Ingredient> sugarList = IngredientStorage.getIngredients("Sugar");
        assertEquals(2, sugarList.size());

        assertEquals("2025/12/31", sugarList.get(0).getExpiryDate().toString());
        assertEquals("2026/01/01", sugarList.get(1).getExpiryDate().toString());
    }

    @Test
    public void testRemoveIngredient() throws OverflowQuantityException {
        IngredientStorage.addToStorage(ingredient1);
        assertTrue(IngredientStorage.contains("Sugar"));

        IngredientStorage.removeIngredient("Sugar");
        assertFalse(IngredientStorage.contains("Sugar"));
    }

    @Test
    public void testGetIngredients_nonExistent() {
        List<Ingredient> nonExistent = IngredientStorage.getIngredients("Flour");
        assertTrue(nonExistent.isEmpty());
    }

    @Test
    public void testUseIngredients_partialUse() throws OverflowQuantityException {
        IngredientStorage.addToStorage(ingredient1);

        IngredientStorage.useIngredients("Sugar", 300);

        List<Ingredient> sugarList = IngredientStorage.getIngredients("Sugar");
        assertEquals(1, sugarList.size());
        assertEquals(200, sugarList.get(0).getQuantity());
    }

    @Test
    public void testUseIngredients_fullUse() throws OverflowQuantityException {
        IngredientStorage.addToStorage(ingredient1);

        IngredientStorage.useIngredients("Sugar", 500);

        assertFalse(IngredientStorage.contains("Sugar"));
    }

    @Test
    public void testUseIngredients_moreThanAvailable() throws OverflowQuantityException {
        IngredientStorage.addToStorage(ingredient1);

        IngredientStorage.useIngredients("Sugar", 600);

        assertFalse(IngredientStorage.contains("Sugar"));
    }

    @Test
    public void testRemoveExpiredIngredients() throws OverflowQuantityException {
        IngredientStorage.addToStorage(expiredIngredient);

        // Triggering cleanup
        IngredientStorage.useIngredients("Butter", 0);

        assertFalse(IngredientStorage.contains("Butter"));
    }

    @Test
    public void testUseIngredients_handlesMultipleExpiry() throws OverflowQuantityException {
        Ingredient earlyExpiry = new Ingredient( "Sugar", "2025/01/01", 200);

        IngredientStorage.addToStorage(earlyExpiry);
        IngredientStorage.addToStorage(ingredient1);

        IngredientStorage.useIngredients("Sugar", 200);

        List<Ingredient> sugarList = IngredientStorage.getIngredients("Sugar");
        assertEquals(1, sugarList.size());
        assertEquals(300, sugarList.get(0).getQuantity());

        assertEquals("2025/12/31", sugarList.get(0).getExpiryDate().toString());
    }

    @Test
    public void testGetStorage_returnsCopy() throws OverflowQuantityException {
        IngredientStorage.addToStorage(ingredient1);
        assertEquals(1, IngredientStorage.getStorage().size());
    }

    @Test
    public void testClearStorage() throws OverflowQuantityException {
        IngredientStorage.addToStorage(ingredient1);
        IngredientStorage.clear();

        assertTrue(IngredientStorage.getStorage().isEmpty());
    }

    @Test
    public void testGetUnexpiredIngredients_multipleIngredients() throws OverflowQuantityException {
        IngredientStorage.addToStorage(ingredient1);
        IngredientStorage.addToStorage(ingredient2);
        IngredientStorage.addToStorage(ingredient3);

        int unexpiredSugar = IngredientStorage.getUnexpiredIngredients("Sugar");
        int unexpiredSalt = IngredientStorage.getUnexpiredIngredients("Salt");

        assertEquals(700, unexpiredSugar); // 500 + 200
        assertEquals(300, unexpiredSalt); // 300 for Salt
    }

    @Test
    public void testGetUnexpiredIngredients_onlyExpired() throws OverflowQuantityException {
        IngredientStorage.addToStorage(expiredIngredient);

        int unexpiredButter = IngredientStorage.getUnexpiredIngredients("Butter");

        assertEquals(0, unexpiredButter); // The butter is expired
    }

    @Test
    public void testGetUnexpiredIngredients_noIngredients() {
        int unexpiredFlour = IngredientStorage.getUnexpiredIngredients("Flour");

        assertEquals(0, unexpiredFlour); // Flour doesn't exist in storage
    }

    @Test
    public void testGetUnexpiredIngredients_afterUsingIngredients() throws OverflowQuantityException {
        IngredientStorage.addToStorage(ingredient1);
        IngredientStorage.addToStorage(ingredient2);

        IngredientStorage.useIngredients("Sugar", 300);

        int unexpiredSugar = IngredientStorage.getUnexpiredIngredients("Sugar");
        int unexpiredSalt = IngredientStorage.getUnexpiredIngredients("Salt");

        assertEquals(200, unexpiredSugar); // After using 300, 200 remains
        assertEquals(300, unexpiredSalt); // Salt is still intact
    }
}
