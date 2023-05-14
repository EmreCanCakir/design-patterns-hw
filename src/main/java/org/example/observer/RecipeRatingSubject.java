package org.example.observer;

import org.example.model.Recipe;

public interface RecipeRatingSubject {
    void addObserver(RecipeRatingObserver observer);
    void removeObserver(RecipeRatingObserver observer);
    void notifyObservers(Recipe recipe);
}
