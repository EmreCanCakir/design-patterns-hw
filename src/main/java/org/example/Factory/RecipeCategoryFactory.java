package org.example.Factory;

import org.example.model.RecipeCategory;

public class RecipeCategoryFactory {
    public RecipeCategory createRecipeCategory(String categoryName) {
        return new RecipeCategory(categoryName);
    }
}
