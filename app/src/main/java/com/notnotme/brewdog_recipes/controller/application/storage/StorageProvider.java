package com.notnotme.brewdog_recipes.controller.application.storage;


import com.notnotme.brewdog_recipes.controller.application.Callback;
import com.notnotme.brewdog_recipes.controller.application.Controller;
import com.notnotme.brewdog_recipes.model.Beer;

import java.util.List;

public interface StorageProvider {

    interface StorageController extends Controller {

        void saveFavoriteBeer(Beer beer, Callback<Beer, Throwable> callback);
        void deleteFavoriteBeer(Beer beer, Callback<Beer, Throwable> callback);
        long getFavoriteBeerCount();
        boolean isFavoriteBeer(Beer beer);
        void saveBeers(List<Beer> beers, Callback<List<Beer>, Throwable> callback);
        void getBeers(Callback<List<Beer>, Throwable> callback);
        void getFavoriteBeers(Callback<List<Beer>, Throwable> callback);
        long getBeerCount();
        Beer getRandomBeer();

    }

    StorageController getStorageController();

}
