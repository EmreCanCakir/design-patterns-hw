package org.example.observer;

import org.example.model.Recipe;

public interface RecipeRatingObserver {

    void updateRating(Recipe recipe, int newRating);
}
