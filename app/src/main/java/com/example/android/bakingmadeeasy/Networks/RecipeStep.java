package com.example.android.bakingmadeeasy.Networks;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Rebecca on 5/24/2017.
 */

public class RecipeStep implements Parcelable {

    private Integer id;

    public String ingredientsLabel;

    public String recipeStepsLabel;

    public String shortDescription;

    public String description;

    public String videoURL;

    public String thumbnailURL;

    public Integer getId() {
        return id;
    }
    public void setId(Integer name) {
        this.id = id;
    }

    public String getShortDescription() {
        return shortDescription;
    }
    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getIngredientsLabel() {
        return ingredientsLabel;
    }
    public void setIngredientsLabel(String ingredientsLabel) {
        this.ingredientsLabel = ingredientsLabel;
    }

    public String getRecipeStepsLabel() {
        return recipeStepsLabel;
    }
    public void setRecipeStepsLabel(String recipeStepsLabel) {
        this.recipeStepsLabel = recipeStepsLabel;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public String getVideoURL() {
        return videoURL;
    }
    public void setVideoURL(String videoURL) {
        this.videoURL = videoURL;
    }

    public String getThumbnailURL() {
        return thumbnailURL;
    }
    public void setThumbnailURL(String thumbnailURL) {
        this.thumbnailURL = thumbnailURL;
    }

    protected RecipeStep(Parcel in) {
        id = in.readByte() == 0x00 ? null : in.readInt();
        ingredientsLabel = in.readString();
        recipeStepsLabel = in.readString();
        shortDescription = in.readString();
        description = in.readString();
        videoURL = in.readString();
        thumbnailURL = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(id);
        }
        dest.writeString(ingredientsLabel);
        dest.writeString(recipeStepsLabel);
        dest.writeString(shortDescription);
        dest.writeString(description);
        dest.writeString(videoURL);
        dest.writeString(thumbnailURL);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<RecipeStep> CREATOR = new Parcelable.Creator<RecipeStep>() {
        @Override
        public RecipeStep createFromParcel(Parcel in) {
            return new RecipeStep(in);
        }

        @Override
        public RecipeStep[] newArray(int size) {
            return new RecipeStep[size];
        }
    };
}