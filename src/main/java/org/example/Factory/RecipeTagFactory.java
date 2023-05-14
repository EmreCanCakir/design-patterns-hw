package org.example.Factory;

import org.example.model.RecipeTag;

public class RecipeTagFactory {
    public RecipeTag createRecipeTag(String tagName) {
        return new RecipeTag(tagName);
    }
}
