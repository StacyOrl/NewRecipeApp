package com.stasyorl.recipeapp.Listeners;

import com.google.firebase.auth.FirebaseUser;

public interface ChangeUser {
    void onUserChanged(FirebaseUser mUser, String newUserId);
}
