package seedu.cookingaids.items;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import seedu.cookingaids.config.UnitConfig;
import seedu.cookingaids.exception.OverflowQuantityException;

import java.time.LocalDate;

/**
 * represents an Ingredient class
 * attributes include expiryDate as ExpiryDate type, quantity as an integer, name as a string
 * expiringSoon and expired flags as boolean
 * hold methods necessary to manipulate task Date
 */
public class Ingredient extends Food {
    public ExpiryDate expiryDate;
    private int quantity;
    private boolean expiringSoon = false;
    private boolean expired = false;
    private String name;

    public Ingredient(String name) {
        super(name);
        this.name = name;
    }

    public Ingredient(String name, int quantity) {
        super(name);
        this.quantity = quantity;
        expiryDate = new ExpiryDate("None");
        this.name = name;
    }

    /**
     * creates a new instance of Ingredient
     * saves string input as name
     * calls ExpiryDate constructor to convert second string input into ExpiryDate object and save it to expiryDate
     * saves integer input as quantity
     *
     * @param name       is the name of Ingredient
     * @param expiryDate is the expiry date of the ingredient
     * @param quantity   is the amount of ingredients added
     */
    @JsonCreator
    public Ingredient(@JsonProperty("name") String name,
                      @JsonProperty("expiry") String expiryDate, @JsonProperty("quantity") int quantity) {
        super(name);

        this.expiryDate = new ExpiryDate(expiryDate);
        this.quantity = quantity;
        this.name = name;
        expiringSoon();
        expired();
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getQuantity() {
        return quantity;
    }

    public ExpiryDate getExpiryDate() {
        return expiryDate;
    }

    public String getName() {
        return name;
    }

    public void addQuantity(int quantity) throws OverflowQuantityException {
        if (quantity > Integer.MAX_VALUE - this.quantity) {
            this.quantity = Integer.MAX_VALUE;
            throw new OverflowQuantityException();
        } else {
            this.quantity += quantity;
        }
    }

    public void removeQuantity(int quantity) {
        assert quantity > 0 : "Quantity should be more than 0";
        this.quantity -= quantity;
    }

    public void setExpiringSoon(boolean b) {
        this.expiringSoon = b;
    }

    private void expiringSoon() {
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        LocalDate today = LocalDate.now();

        if (expiryDate.getDateLocalDate() != null) {
            if (expiryDate.getDateLocalDate().equals(tomorrow) || expiryDate.getDateLocalDate().equals(today)) {
                this.expiringSoon = true;
            }
        } else {
            this.expiringSoon = false;
        }
    }

    private void expired() {
        LocalDate today = LocalDate.now();
        if (expiryDate.getDateLocalDate() != null) {
            if (expiryDate.getDateLocalDate().isBefore(today)) {
                this.expired = true;
            }
        } else {
            this.expired = false;
        }
    }

    public void setExpired(boolean b) {
        this.expired = b;
    }


    @Override
    public void displayInfo() {
        System.out.println(" Name: " + name + ", Expiring in:" + expiryDate.toString());
    }

    @Override
    public String toString() {
        String unit = UnitConfig.getUnit(name);
        return name + " (" + quantity + unit + ", Expiry: " + expiryDate + ", Expiring Soon: " +
                (expiringSoon ? "Yes" : "No") + ", Expired: " + (expired ? "Yes" : "No") + ")";
    }

    public void setExpiryDate(ExpiryDate expiryDate) {
        this.expiryDate = expiryDate;
        expiringSoon();
        expired();
    }
}
