package com.notnotme.brewdog_recipes.controller.application;

import android.content.Context;
import android.os.Bundle;

public interface Controller {

    void onCreate(Context context, Bundle savedInstanceState);
    void onSaveInstanceState(Bundle outState);
    void onDestroy();
    void onStart();
    void onStop();

}
