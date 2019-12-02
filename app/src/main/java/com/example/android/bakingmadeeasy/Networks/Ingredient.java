package com.example.android.bakingmadeeasy.Networks;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Rebecca on 5/24/2017.
 */

public class Ingredient implements Parcelable {

    public Float quantity;

    public String measure;

    public String ingredient;

    public float getQuantity() {
        return quantity;
    }
    public void setQuantity(float quantity) {
        this.quantity = quantity;
    }

    public String getMeasure() {
        return measure;
    }
    public void setMeasure(String measure) {
        this.measure = measure;
    }

    public String getIngredient() {
        return ingredient;
    }
    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }

    @Override
    public String toString() {
        return "quantity = " + quantity + "measure = " + measure + "ingredient = " + ingredient +"\n";
    }

    protected Ingredient(Parcel in) {
        quantity = in.readByte() == 0x00 ? null : in.readFloat();
        measure = in.readString();
        ingredient = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (quantity == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeFloat(quantity);
        }
        dest.writeString(measure);
        dest.writeString(ingredient);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Ingredient> CREATOR = new Parcelable.Creator<Ingredient>() {
        @Override
        public Ingredient createFromParcel(Parcel in) {
            return new Ingredient(in);
        }

        @Override
        public Ingredient[] newArray(int size) {
            return new Ingredient[size];
        }
    };

}