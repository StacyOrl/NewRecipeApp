package com.stasyorl.recipeapp;

import android.app.Application;

public class App extends Application {



    public static boolean activityVisible;

    public static boolean isActivityVisible() {
        return activityVisible;
    }

    public static void activityResumed() {
        activityVisible = true;

    }

    public static void activityPaused() {
        activityVisible = false;

    }
}
