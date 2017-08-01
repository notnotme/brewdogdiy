package com.notnotme.brewdog_recipes.controller.application.api.retrofit;

import android.util.Log;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.notnotme.brewdog_recipes.model.FoodPairing;

import io.realm.RealmList;
import io.realm.internal.IOException;

/**
 * A food pairing is just a string in the Api point of view,
 * but because {@link io.realm.Realm} can't handle a list of String we have
 * to wrap the {@link String} into a container object like {@link FoodPairing}.
 *
 * Because of that a custom type adapter is needed for Gson to be able to serialize/deserialize correctly
 * the list of string into a {@link RealmList} of {@link FoodPairing}
 *
 * This break the abstraction layers a bit but that should be not hard to handle if some
 * change are required
 */
final class FoodPairingTypeAdapter extends TypeAdapter<RealmList<FoodPairing>> {

    private static final String TAG = FoodPairingTypeAdapter.class.getSimpleName();

    @Override
    public void write(JsonWriter out, RealmList<FoodPairing> value) throws IOException {
        try {
            out.beginArray();
            for (FoodPairing foodPairing : value) {
                out.value(foodPairing.getValue());
            }
            out.endArray();
        } catch (Exception e) {
            Log.e(TAG, "Error while serialize to json: " + e.getMessage());
        }
    }

    @Override
    public RealmList<FoodPairing> read(JsonReader in) throws IOException {
        RealmList<FoodPairing> list = new RealmList<>();

        try {
            in.beginArray();
            while (in.hasNext()) {
                list.add(new FoodPairing(in.nextString()));
            }
            in.endArray();
        } catch (Exception e) {
            Log.e(TAG, "Error while deserialize from json: " + e.getMessage());
        }

        return list;
    }

}