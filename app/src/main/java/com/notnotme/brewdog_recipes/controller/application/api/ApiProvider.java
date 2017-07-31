package com.notnotme.brewdog_recipes.controller.application.api;


import com.notnotme.brewdog_recipes.controller.application.Callback;
import com.notnotme.brewdog_recipes.controller.application.Controller;
import com.notnotme.brewdog_recipes.model.Beer;

import java.util.List;

public interface ApiProvider {

    interface ApiController extends Controller {

        void getBeers(int page, Callback<List<Beer>, Throwable> callback);

    }

    ApiController getApiController();

}
