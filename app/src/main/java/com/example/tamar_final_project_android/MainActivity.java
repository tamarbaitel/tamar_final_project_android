package com.example.tamar_final_project_android;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

/**
 * AI-generated / assisted class structure (documented for Ministry of Education guidelines).
 * מסך פתיחה המנתב למסך ההתחברות וההרשמה של TripMate.
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}