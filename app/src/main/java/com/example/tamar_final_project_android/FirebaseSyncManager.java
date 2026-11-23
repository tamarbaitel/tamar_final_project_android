package com.example.tamar_final_project_android;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

/**
 * AI-generated / assisted class structure (documented for Ministry of Education guidelines).
 * סנכרון נתוני משתמש מול Firebase Realtime Database.
 */
public class FirebaseSyncManager {

    private final DatabaseReference usersRef;

    public FirebaseSyncManager() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        this.usersRef = database.getReference("users");
    }

    public void syncUserHobbies(String username, String hobbiesCsv) {
        if (username == null || username.trim().isEmpty()) {
            return;
        }
        String safeKey = username.trim().replaceAll("[.#$\\[\\]]", "_");
        Map<String, Object> updates = new HashMap<>();
        updates.put("username", username.trim());
        updates.put("hobbiesCsv", hobbiesCsv);
        updates.put("updatedAt", System.currentTimeMillis());

        usersRef.child(safeKey).updateChildren(updates);
    }
}