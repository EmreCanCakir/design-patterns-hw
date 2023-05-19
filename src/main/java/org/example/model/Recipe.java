package org.example.model;

import org.example.observer.RecipeRatingObserver;
import org.example.observer.RecipeRatingSubject;

import java.util.ArrayList;
import java.util.List;

public class Recipe implements RecipeRatingSubject {
    private String name;
    private List<Ingredient> ingredients;
    private String cookingInstructions;
    private int servingSize;
    private List<RecipeCategory> categories;
    private List<RecipeTag> tags;
    private double impactProperty;

    private List<Integer> ratings;
    private List<RecipeRatingObserver> observers;

    public Recipe(String name, List<Ingredient> ingredients, String cookingInstructions, int servingSize,
                  List<RecipeCategory> categories, List<RecipeTag> tags) {
        this.name = name;
        this.ingredients = ingredients;
        this.cookingInstructions = cookingInstructions;
        this.servingSize = servingSize;
        this.categories = categories;
        this.tags = tags;
        this.impactProperty = 0.0;
        this.ratings = new ArrayList<>();
        this.observers = new ArrayList<>();
    }

    public void addRating(int rating) {
        ratings.add(rating);
        notifyObservers(this);
    }

    public int getTotalRatingsCount() {
        return ratings.size();
    }

    public double getTotalRatings() {
        double sum = 0;
        for (int rating : ratings) {
            sum += rating;
        }
        return sum;
    }

    public double getAverageRating() {
        if (ratings.isEmpty()) {
            return 0.0;
        }
        return getTotalRatings() / ratings.size();
    }

    public double getImpactProperty() {
        return impactProperty;
    }

    public void setImpactProperty(double impactProperty) {
        this.impactProperty = impactProperty;
    }

    @Override
    public void addObserver(RecipeRatingObserver observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(RecipeRatingObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(Recipe recipe) {
        for (RecipeRatingObserver observer : observers) {
            observer.updateRating(recipe, ratings.get(ratings.size() - 1));
        }
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCookingInstructions(String cookingInstructions) {
        this.cookingInstructions = cookingInstructions;
    }

    public String getName() {
        return name;
    }

    public List<RecipeCategory> getCategories() {
        return categories;
    }

    public List<RecipeTag> getTags() {
        return tags;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public String getCookingInstructions() {
        return cookingInstructions;
    }
}
