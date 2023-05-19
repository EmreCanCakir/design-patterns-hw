package org.example.service;

import org.example.model.Recipe;
import org.example.observer.RecipeRatingObserver;
import org.example.observer.RecipeRatingSubject;

import java.util.ArrayList;
import java.util.List;

public class RecipeRatingManager implements RecipeRatingObserver {
    private List<RecipeRatingSubject> subjects;

    public RecipeRatingManager() {
        subjects = new ArrayList<>();
    }

    public void registerSubject(RecipeRatingSubject subject) {
        subjects.add(subject);
        subject.addObserver(this);
    }

    public void unregisterSubject(RecipeRatingSubject subject) {
        subjects.remove(subject);
        subject.removeObserver(this);
    }

    @Override
    public void updateRating(Recipe recipe, int newRating) {
        // Compute the impact property of the recipe based on the new rating
        // and update the recipe's impact property
        int totalRatings = recipe.getTotalRatingsCount();
        double averageRating = recipe.getAverageRating();
        double newAverageRating = (averageRating * totalRatings + newRating) / (totalRatings + 1);
        recipe.setImpactProperty(newAverageRating);

        // Notify all the subjects that the recipe has been updated
        for (RecipeRatingSubject subject : subjects) {
            subject.notifyObservers(recipe);
        }
    }
}