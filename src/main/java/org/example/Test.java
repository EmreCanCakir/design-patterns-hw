package org.example;

import org.example.Factory.RecipeFactory;
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

    private boolean isRecipeModuleShowed = true;

    public Test() {
        recipeManager = RecipeManager.getInstance();
        ratingManager = new RecipeRatingManager();
        recipeFactory = new RecipeFactory();
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
                    break;
                case 3:
                    //rateRecipe(scanner);
                    break;
                case 4:
                    //modifyRecipe(scanner);
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
            scanner.nextLine(); // consume the newline character

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

            RecipeCategory category = new RecipeCategory(categoryName);
            categories.add(category);
        }

        List<RecipeTag> tags = new ArrayList<>();
        while (tags.size() < 3) {
            System.out.print("Enter tag name (or \"done\" to finish): ");
            String tagName = scanner.nextLine();

            if (tagName.equals("done")) {
                break;
            }

            RecipeTag tag = new RecipeTag(tagName);
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
        System.out.println("You can select recipe to get details, Please enter recipe name (or \"done\" to finish): ");
        String decision = scanner.nextLine().toLowerCase(Locale.ROOT);

        if(!decision.equals("done")){
            Recipe selectedRecipe = recipeManager.getRecipe(decision);
            if (selectedRecipe == null) return;
            System.out.println("Recipe details: ");
            System.out.println("Name: " + (selectedRecipe.getName()));
            for (int i = 0; i<selectedRecipe.getCategories().size(); i++){
                System.out.println("Category " + (i+1) + ": " + selectedRecipe.getCategories().get(i).categoryName);
            }
            for (int i = 0; i<selectedRecipe.getTags().size(); i++){
                System.out.println("Tag " + (i+1) + ": " + selectedRecipe.getTags().get(i).tagName);
            }
            System.out.println("Cooking Instructions: " + selectedRecipe.getCookingInstructions());
            System.out.println("Impact Property: " + selectedRecipe.getImpactProperty());
        }
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
