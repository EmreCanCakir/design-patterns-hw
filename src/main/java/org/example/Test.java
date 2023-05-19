package org.example;

import org.example.Factory.RecipeCategoryFactory;
import org.example.Factory.RecipeFactory;
import org.example.Factory.RecipeTagFactory;
import org.example.model.Ingredient;
import org.example.model.Recipe;
import org.example.model.RecipeCategory;
import org.example.model.RecipeTag;
import org.example.service.RecipeManager;
import org.example.service.RecipeRatingManager;

import java.util.*;

public class Test {
    private RecipeManager recipeManager;
    private RecipeRatingManager ratingManager;
    private RecipeFactory recipeFactory;
    private RecipeCategoryFactory recipeCategoryFactory;
    private RecipeTagFactory recipeTagFactory;

    private boolean isRecipeModuleShowed = true;

    public Test() {
        recipeManager = RecipeManager.getInstance();
        ratingManager = new RecipeRatingManager();
        recipeFactory = new RecipeFactory();
        recipeCategoryFactory = new RecipeCategoryFactory();
        recipeTagFactory = new RecipeTagFactory();
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);

        while (isRecipeModuleShowed) {
            System.out.println("Select an option:");
            System.out.println("1. Create recipe");
            System.out.println("2. Search recipes");
            System.out.println("3. Rate recipe");
            System.out.println("4. Modify recipe");
            System.out.println("5. Quit program");
            System.out.print("Enter your choice: ");

            int choice = getIntegerValueWithScanner(scanner, "Invalid input. Please enter a valid integer between 1 and 4.");
            switch (choice) {
                case 1:
                    createRecipe(scanner);
                    break;
                case 2:
                    searchRecipes(scanner);
                    writeRecipeDetails(scanner);
                    break;
                case 3:
                    rateRecipe(scanner);
                    break;
                case 4:
                    modifyRecipe(scanner);
                    break;
                case 5:
                    isRecipeModuleShowed = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please enter a number from 1 to 4.");
            }

            System.out.println();
        }
    }

    private void createRecipe(Scanner scanner) {
        System.out.print("Enter recipe name: ");
        String name = scanner.nextLine();

        List<Ingredient> ingredients = new ArrayList<>();
        while (true) {
            System.out.print("Enter ingredient name (or \"done\" to finish): ");
            String ingredientName = scanner.nextLine();

            if (ingredientName.equals("done")) {
                break;
            }

            System.out.print("Enter ingredient amount: ");
            double amount = scanner.nextDouble();
            scanner.nextLine();

            System.out.print("Enter ingredient unit: ");
            String unit = scanner.nextLine();

            Ingredient ingredient = new Ingredient(ingredientName, amount);
            ingredients.add(ingredient);
        }

        System.out.print("Enter cooking instructions: ");
        String cookingInstructions = scanner.nextLine();

        System.out.print("Enter serving size: ");
        int servingSize = getIntegerValueWithScanner(scanner, "Invalid choice. Please enter a number");

        List<RecipeCategory> categories = new ArrayList<>();
        while (categories.size() < 3) {
            System.out.print("Enter category name (or \"done\" to finish): ");
            String categoryName = scanner.nextLine();

            if (categoryName.equals("done")) {
                break;
            }

            RecipeCategory category = recipeCategoryFactory.createRecipeCategory(categoryName);
            categories.add(category);
        }

        List<RecipeTag> tags = new ArrayList<>();
        while (tags.size() < 3) {
            System.out.print("Enter tag name (or \"done\" to finish): ");
            String tagName = scanner.nextLine();

            if (tagName.equals("done")) {
                break;
            }

            RecipeTag tag = recipeTagFactory.createRecipeTag(tagName);
            tags.add(tag);
        }

        Recipe recipe = recipeFactory.createRecipe(name, ingredients, cookingInstructions, servingSize, categories, tags);

        System.out.print("Enter save to recipe saving or enter cancel to discard changes: ");
        String decision = scanner.nextLine().toLowerCase(Locale.ROOT);

        if (decision.toLowerCase(Locale.ROOT).equals("save")) {
            Recipe recipe1 = recipeManager.getRecipes().stream().filter(recip -> recip.getName().equals(recipe.getName())).findFirst().orElse(null);
            if (recipe1 == null) {
                recipeManager.addRecipe(recipe);
                System.out.println("Recipe added successfully");
            } else {
                System.out.println("Recipe name already exist please try again");
            }
        } else if (decision.toLowerCase(Locale.ROOT).equals("cancel"))
            System.out.println("Recipe not saved");
    }

    private void modifyRecipe(Scanner scanner) {
        searchRecipes(scanner);

        System.out.println("You can select recipe to get details, or update process. Please enter recipe name (or \"done\" to finish): ");
        String decision = scanner.nextLine().toLowerCase(Locale.ROOT);

        if (decision.equals("done")) return;

        Recipe selectedRecipe = recipeManager.getRecipe(decision);
        if (selectedRecipe == null) return;
        System.out.println("Recipe details: ");

        System.out.println("Name: " + (selectedRecipe.getName()));
        String updatedName = scanner.nextLine();
        if (!updatedName.isEmpty()) selectedRecipe.setName(updatedName);

        for (int i = 0; i < selectedRecipe.getCategories().size(); i++) {
            System.out.println("Category " + (i + 1) + ": " + selectedRecipe.getCategories().get(i).categoryName);
            String updatedCategory = scanner.nextLine();
            if (!updatedCategory.isEmpty()) selectedRecipe.getCategories().get(i).categoryName = updatedCategory;
        }
        if (selectedRecipe.getCategories().size() != 3){
            for (int i = selectedRecipe.getCategories().size(); i < 3; i++) {
                System.out.println("Category " + (i + 1) + ": ");
                String updatedCategory = scanner.nextLine();
                if (!updatedCategory.isEmpty()) selectedRecipe.getCategories().add(new RecipeCategory(updatedCategory));
            }
        }

        for (int i = 0; i < selectedRecipe.getTags().size(); i++) {
            System.out.println("Tag " + (i + 1) + ": " + selectedRecipe.getTags().get(i).tagName);
            String updatedTag = scanner.nextLine();
            if (!updatedTag.isEmpty()) selectedRecipe.getTags().get(i).tagName = updatedTag;
        }

        if (selectedRecipe.getTags().size() != 3){
            for (int i = selectedRecipe.getTags().size(); i < 3; i++) {
                System.out.println("Tag " + (i + 1) + ": ");
                String updatedTag = scanner.nextLine();
                if (!updatedTag.isEmpty()) selectedRecipe.getTags().add(new RecipeTag(updatedTag));
            }
        }

        System.out.println("Cooking Instructions: " + selectedRecipe.getCookingInstructions());
        String updatedCookingIns = scanner.nextLine();
        if (!updatedCookingIns.isEmpty()) selectedRecipe.setCookingInstructions(updatedCookingIns);
    }

    private void searchRecipes(Scanner scanner) {
        System.out.println("Enter recipe name, tags, ingredients, or categories.");
        String searchText = scanner.nextLine().toLowerCase(Locale.ROOT);

        List<Recipe> foundRecipes = new ArrayList<>();
        for (Recipe recipe : recipeManager.getRecipes()) {
            if (recipe.getName().toLowerCase(Locale.ROOT).contains(searchText)
                    || recipeManager.isRecipeHasCategory(recipe, searchText)
                    || recipeManager.isRecipeHasIngredient(recipe, searchText)
                    || recipeManager.isRecipeHasTag(recipe, searchText)
            ) {
                foundRecipes.add(recipe);
            }
        }
        foundRecipes.forEach(recipe -> System.out.print("---" + recipe.getName() + "---"));
    }

    private void writeRecipeDetails(Scanner scanner) {
        System.out.println("You can select recipe to get details, Please enter recipe name (or \"done\" to finish): ");
        String decision = scanner.nextLine().toLowerCase(Locale.ROOT);

        if (decision.equals("done")) return;

        Recipe selectedRecipe = recipeManager.getRecipe(decision);
        if (selectedRecipe == null) return;
        System.out.println("Recipe details: ");
        System.out.println("Name: " + (selectedRecipe.getName()));
        for (int i = 0; i < selectedRecipe.getCategories().size(); i++) {
            System.out.println("Category " + (i + 1) + ": " + selectedRecipe.getCategories().get(i).categoryName);
        }
        for (int i = 0; i < selectedRecipe.getTags().size(); i++) {
            System.out.println("Tag " + (i + 1) + ": " + selectedRecipe.getTags().get(i).tagName);
        }
        System.out.println("Cooking Instructions: " + selectedRecipe.getCookingInstructions());
        System.out.println("Impact Property: " + selectedRecipe.getImpactProperty());
    }

    private void rateRecipe(Scanner scanner) {
        searchRecipes(scanner);

        System.out.println("You can select recipe to get details, Please enter recipe name (or \"done\" to finish): ");
        String decision = scanner.nextLine().toLowerCase(Locale.ROOT);

        if (decision.equals("done")) return;

        Recipe selectedRecipe = recipeManager.getRecipe(decision);
        if (selectedRecipe == null) return;

        System.out.println("Please enter rating between 1 to 5");
        int rating = scanner.nextInt();

        selectedRecipe.addRating(rating);
        System.out.println("Average rating that recipe is:  "+ selectedRecipe.getAverageRating());
    }

    private int getIntegerValueWithScanner(Scanner scanner, String errorMessage) {
        int choice = 0;
        try {
            choice = scanner.nextInt();
            scanner.nextLine();
        } catch (InputMismatchException e) {
            System.out.println(errorMessage);
            scanner.nextLine();
        }
        return choice;
    }
}
