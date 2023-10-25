package com.example.bulletinboard.ui.createPost;

import androidx.annotation.Nullable;

public class CreatePostFormState {

    @Nullable
    private Integer titleError;
    @Nullable
    private Integer creatorNameError;
    @Nullable
    private Integer dateError;
    @Nullable
    private Integer locationError;
    @Nullable
    private Integer descriptionError;

    private boolean isDataValid;

    public CreatePostFormState(@Nullable Integer titleError, @Nullable Integer creatorNameError, @Nullable Integer dateError, @Nullable Integer locationError, @Nullable Integer descriptionError) {
        this.titleError = titleError;
        this.creatorNameError = creatorNameError;
        this.dateError = dateError;
        this.locationError = locationError;
        this.descriptionError = descriptionError;
    }

    CreatePostFormState(boolean isDataValid) {
        this.titleError = null;
        this.creatorNameError = null;
        this.dateError = null;
        this.locationError = null;
        this.descriptionError = null;
        this.isDataValid = isDataValid;
    }

    @Nullable
    public Integer getTitleError() {
        return titleError;
    }

    @Nullable
    public Integer getCreatorNameError() {
        return creatorNameError;
    }

    @Nullable
    public Integer getDateError() {
        return dateError;
    }

    @Nullable
    public Integer getLocationError() {
        return locationError;
    }

    @Nullable
    public Integer getDescriptionError() {
        return descriptionError;
    }

    boolean isDataValid() {
        return isDataValid;
    }
}


