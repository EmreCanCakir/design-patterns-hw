package org.example.Factory;

import org.example.model.Ingredient;
import org.example.model.Recipe;
import org.example.model.RecipeCategory;
import org.example.model.RecipeTag;

import java.util.List;

public class RecipeFactory {
    public Recipe createRecipe(String name, List<Ingredient> ingredients, String cookingInstructions, int servingSize,
                               List<RecipeCategory> categories, List<RecipeTag> tags) {
        return new Recipe(name, ingredients, cookingInstructions, servingSize, categories, tags);
    }
}
