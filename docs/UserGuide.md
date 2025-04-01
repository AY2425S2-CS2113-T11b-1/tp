# **CookingAids User Guide**  

## **Table of Contents**  
- [Introduction](#introduction-)
- [Notes About the Command Format](#notes-about-the-command-format-)
- [Features](#features-)
  1. [Viewing Help: `help`](#1-viewing-help-help-)
  2. [Adding Dish to Schedule: `add -dish`](#2-adding-dish-to-schedule-add--dishdishname-)
  3. [Adding an Ingredient: `add -ingredient`](#3-adding-an-ingredient-add--ingredientingredientname-)  
  4. [Adding Recipe to Recipe Bank: `add -recipe`](#4-adding-recipe-to-recipe-bank-add--reciperecipename)
  5. [View Scheduled Dishes: `list -dish`](#5-view-scheduled-dishes-list--dish)  
  6. [View Available Ingredients: `list -ingredient`](#6-view-available-ingredients-list--ingredient) 
  7. [View Shopping List: `list -shopping`](#7-view-shopping-list-list--shopping)
  8. [View Dishes for the Month: `view -month=`](#8-view-dishes-for-the-month-view--month1-12blank)
  9. [View Available Recipes: `list -recipe`](#9-view-available-recipes-list--recipe)
  10. [Delete Dish from Schedule: `delete -dish`](#10-delete-dish-from-schedule-delete--dishdishname-)
  11. [Delete Ingredient: `delete -ingredient`](#11-delete-ingredient-delete--ingredientingredientname-)
  12. [Delete Recipe from Recipe Bank: `delete -recipe`](#12-delete-recipe-from-recipe-bank-delete--reciperecipename-)
  13. [Suggest Dishes: `suggest`](#13-suggest-dishes-suggest)
- [Command List](#command-list-)

---

## **Introduction**  

CookingAids is an app designed to help busy students living overseas on a budget save time and effort when planning and cooking meals.  
With CookingAids, you can:  
- Input your current ingredients.  
- Schedule dishes from a list of recipes.  
- View full recipes and cooking methods.  
- Track remaining ingredients and scheduled dishes.  

---

## **Notes About the Command Format**  

> **Command Format Notes:**  
> - Items in square brackets are optional.  
>   - Example: `add -dish={dishName} [-when{when}]` can be used as `add -dish={dishName}`.  
> - Parameters can be in any order.  
>   - Example: `add -dish={dishName} -when{when}` is equivalent to `add -when{when} -dish={dishName}`.  
> - Extraneous parameters for commands that do not take parameters (e.g., `help`) will be ignored.  
>   - Example: `help 123` will be interpreted as `help`.  
> - Recipes, dishes and ingredients should be named in snake case (ie lowercase words with underscores)
>   - Example: `chicken_rice` instead of `ChickenRice`

---

## **Features**  

### **1. Viewing Help: `help`** 

Displays a link to this guide. 
**Format:**  
```plaintext
help
```

**Expected Output:**  \
![alt text](images/help_image.png)

---

### **2. Adding Dish to Schedule: `add -dish={dishName}`** 

Adds a dish to the calendar. If no date is specified, the date is set to today.  

**Format:**  
```plaintext
add -dish={dishName}  
add -dish={dishName} [-when{when}]
```

**Expected Output:**  
- `add -dish={dishName}`:  
  ```plaintext
  {dishName} has been added to today’s cooking list!
  ```
- `add -dish={dishName} -when{when}`:  
  ```plaintext
  {dishName} has been added to your cooking list on {when}!
  ```

---

### **3. Adding an Ingredient: `add -ingredient={ingredientName}`** 

Adds an ingredient to the ingredient database. If no quantity is specified, it defaults to 1.  

> **Warning:** Ingredients will not expire if no expiry date is set.  

**Format:**  
```plaintext
add -ingredient={ingredientName} [-expiry={expiryDate}] [-qty={quantity}]
```

**Expected Output:**  
- `add -ingredient={ingredientName}`:  
  ```plaintext
  Added Ingredient: {ingredientName} (1, Expiry: None, Expiring Soon: No, Expired: No)
  ```
- `add -ingredient={ingredientName} -qty{quantity}`:  
  ```plaintext
  Added Ingredient: {ingredientName} ({quantity}, Expiry: None, Expiring Soon: No, Expired: No)
  ```
- `add -ingredient={ingredientName} -expiry={expiryDate}`:  
  ```plaintext
  Added Ingredient: {ingredientName} (1, Expiry: {expiryDate}, Expiring Soon: Yes/No depends on expiryDate, Expired: Yes
  /No depends on expiryDate)
  ```

---

### **4. Adding Recipe to Recipe Bank: `add -recipe={recipeName}`**

Adds a recipe to the recipe bank.

**Format:**
```plaintext
add -recipe={recipeName} -needs=ingredient_1,quantity_1,ingredient_2,quantity_2
```

**Expected Output:**

- `add -recipe={recipeName} -needs=ingredient_1,quantity_1,ingredient_2,quantity_2`:
  ```plaintext
  Added Recipe: {recipeName} 
  Ingredients: [ingredient_1 (quantity_1), ingredient_2 (quantity_2)]
  ```

---

### **5. View Scheduled Dishes: `list -dish`**

Displays a list of scheduled dishes.  

**Format:**  
```plaintext
list -dish  
list -dish={dishName}  
list -dish 
```

**Expected Output:**  
- `list -dish`:  
  ```plaintext
  All dishes:  
  1. {dishName1} Scheduled for {when1}  
  2. {dishName2} Scheduled for {when2}
  3. {dishName2} Scheduled for {when3}
  
  Unscheduled
  1. {dishName1}   
  2. {dishName2} 
  3. {dishName2}   
  ```

---

### **6. View Available Ingredients: `list -ingredient`**

Displays a list of available ingredients.  

**Format:**  
```plaintext
list -ingredient
```

**Expected Output:**  
```plaintext
{ingredientName1}
  List of ingredients with {ingredientName1} and different expiry dates
{ingredientName2}
  List of ingredients with {ingredientName2} and different expiry dates  
{ingredientName3}
  List of ingredients with {ingredientName3} and different expiry dates
```

---

### **7. View Shopping List: `list -shopping`**

Displays Shopping List.

**Format:**
```plaintext
list -shopping
```

**Expected Output:**
```plaintext

1 - {ingredientName1}  
2 - {ingredientName2}  
3 - {ingredientName3}
```

---
### **8. View Dishes for the Month: `view -month={1-12/BLANK}`**

Displays a list of available ingredients.

**Format:**
```plaintext
view -month=1  (for January)
view -month=11 (for November)
view -month=   (for current month)
```

**Expected Output:**

![viewMonth.png](images/viewMonth.png)

---

### **9. View Available Recipes: `list -recipe`**

Displays a list of available recipes and/or their ingredients.

**Format:**
```plaintext
list -recipe  
list -recipe={recipeName}
```

**Expected Output:**
- `list -recipe`:
  ```plaintext
  Recipes:  
  - recipeName1  
  - recipeName2  
  - ...
  ```
- `list -recipe={recipeName}`:
  ```plaintext
  recipeName:  
  - ingredientName1  
  - ingredientName2  
  - ...
  ```

---

### **10. Delete Dish from Schedule: `delete -dish={dishName}`** 

Deletes a dish from the schedule.  

**Format:**  
```plaintext
delete -dish={dishName}  
delete -when={when}  
delete -dish={dishName} -when={when}
```

**Expected Output:**  
- If multiple dishes are scheduled:  
  ```plaintext
  1. {when1} - {dishName}  
  2. {when2} - {dishName}  
  Which would you like to delete?  
  Input a number.  
  {when} - {dishName} Successfully deleted!
  ```
- If only one dish is scheduled:  
  ```plaintext
  {when} - {dishName} Successfully deleted!
  ```

---

### **11. Delete Ingredient: `delete -ingredient={ingredientName}`** 

Deletes ingredient from the list of available ingredients.  

**Format:**  
```plaintext
delete -ingredient={ingredientName}
```

**Expected Output:**  
```plaintext
Deleted {ingredientName} from the list of available ingredients.
```

---

### **12. Delete Recipe from Recipe Bank: `delete -recipe={recipeName}`** 

Deletes a recipe from the recipe bank.  

**Format:**  
```plaintext
delete -recipe={recipeName}
```

**Expected Output:**  
```plaintext
{recipeName} has been deleted from the recipe bank!
```

---

### **13. Suggest Dishes: `suggest`**
Suggests dishes based on available ingredients. 
**Format:**
```
suggest
``` 
**Expected Output:**
```
You have enough ingredients to make: 
1: carbonara
2: garlic_bread
```

## **Command List**  

| **Action** | **Format, Examples**                                                                                                                                                                                 |  
| :---- |:-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|  
| **Add** | `add -ingredient`, `add -dish`, `add -recipe` <br> Example: `add -ingredient=tomato -expiry=today -quantity=2`, `add -dish=tomato_soup -when=tmr`, `add -recipe=tomato_soup -needs=tomato,5,onion,2` |  
| **Delete** | `delete -ingredient`, `delete -dish`, `delete -recipe` <br> Example: `delete -ingredient=tomato`, `delete -dish=tomato soup`                                                                         |  
| **List** | `list -ingredient`, `list -dish`, `list -recipe`,`list -shopping`, <br> Example: `list -ingredient`                                                                                                  |  
|**View**| `view -month` <br> Example:`view -month=2`, `view -month=`                                                                                                                                           |
| **Help** | `help`                                                                                                                                                                                               |


