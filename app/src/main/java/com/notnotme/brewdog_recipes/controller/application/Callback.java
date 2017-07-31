package com.notnotme.brewdog_recipes.controller.application;

public interface Callback<T,U> {

    void success(T success);
    void error(U error);

}



















