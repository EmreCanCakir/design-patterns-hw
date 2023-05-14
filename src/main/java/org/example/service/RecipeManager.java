package org.example.service;

import org.example.model.Ingredient;
import org.example.model.Recipe;
import org.example.model.RecipeCategory;
import org.example.model.RecipeTag;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class RecipeManager {
    private static RecipeManager instance;

    private List<Recipe> recipes;

    private RecipeManager() {
        recipes = new ArrayList<>();
    }

    public static RecipeManager getInstance() {
        if (instance == null) {
            instance = new RecipeManager();
        }
        return instance;
    }

    public void addRecipe(Recipe recipe) {
        recipes.add(recipe);
    }

    public List<Recipe> getRecipes() {
        return recipes;
    }

    public void removeRecipe(Recipe recipe) {
        recipes.remove(recipe);
    }

    public Recipe getRecipe(String name) {
        return recipes.stream().filter(recipe -> recipe.getName().contains(name)).findFirst().orElse(null);
    }

    public List<Recipe> searchByCategory(String categoryName) {
        List<Recipe> resultList = new ArrayList<>();
        for (Recipe recipe : recipes) {
            for (RecipeCategory category : recipe.getCategories()) {
                if (category.categoryName.equals(categoryName)) {
                    resultList.add(recipe);
                }
            }
        }
        return resultList;
    }

    public boolean isRecipeHasCategory(Recipe recipe, String categoryName) {
        for (RecipeCategory category : recipe.getCategories()) {
            if (category.categoryName.toLowerCase(Locale.ROOT).contains(categoryName)) {
                return true;
            }
        }
        return false;
    }

    public List<Recipe> searchByTag(String tagName) {
        List<Recipe> resultList = new ArrayList<>();
        for (Recipe recipe : recipes) {
            for (RecipeTag tag : recipe.getTags()) {
                if (tag.tagName.equals(tagName)) {
                    resultList.add(recipe);
                }
            }
        }
        return resultList;
    }

    public boolean isRecipeHasTag(Recipe recipe, String tagName) {
        for (RecipeTag tag : recipe.getTags()) {
            if (tag.tagName.toLowerCase(Locale.ROOT).contains(tagName)) {
                return true;
            }
        }
        return false;
    }

    public List<Recipe> searchByIngredient(String ingredientName) {
        List<Recipe> resultList = new ArrayList<>();
        for (Recipe recipe : recipes) {
            for (Ingredient ingredient : recipe.getIngredients()) {
                if (ingredient.ingredientName.toLowerCase().contains(ingredientName.toLowerCase())) {
                    resultList.add(recipe);
                }
            }
        }
        return resultList;
    }

    public boolean isRecipeHasIngredient(Recipe recipe, String ingredientName) {
        for (Ingredient ingredient : recipe.getIngredients()) {
            if (ingredient.ingredientName.toLowerCase(Locale.ROOT).contains(ingredientName)) {
                return true;
            }
        }
        return false;
    }
}