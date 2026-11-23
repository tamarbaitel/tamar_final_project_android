package com.example.tamar_final_project_android;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tamar_final_project_android.databinding.ActivityLoginBinding;

/**
 * AI-generated / assisted class structure (documented for Ministry of Education guidelines).
 * מסך 1: הרשמה והתחברות עם שמירה בבסיס נתונים והתחברות אוטומטית לשאלון.
 */
public class LoginActivity extends AppCompatActivity {

    public static final String EXTRA_USERNAME = "extra_username";
    private static final String PREFS_SESSION = "tripmate_session";
    private static final String KEY_ACTIVE_USER = "active_username";

    private ActivityLoginBinding binding;
    private TripMateDbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        dbHelper = new TripMateDbHelper(this);

        SharedPreferences prefs = getSharedPreferences(PREFS_SESSION, MODE_PRIVATE);
        String savedUser = prefs.getString(KEY_ACTIVE_USER, "");
        if (!savedUser.isEmpty()) {
            binding.etUsername.setText(savedUser);
        }

        binding.btnLogin.setOnClickListener(v -> handleLogin());
        binding.btnRegister.setOnClickListener(v -> handleRegister());
    }

    private void handleLogin() {
        String username = binding.etUsername.getText() != null
                ? binding.etUsername.getText().toString().trim() : "";
        String password = binding.etPassword.getText() != null
                ? binding.etPassword.getText().toString().trim() : "";

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "יש להזין שם משתמש וסיסמה", Toast.LENGTH_SHORT).show();
            return;
        }

        if (dbHelper.authenticateUser(username, password)) {
            saveActiveUser(username);
            navigateToQuestionnaire(username);
        } else {
            Toast.makeText(this, "שם משתמש או סיסמה שגויים. ניתן ללחוץ על הרשמה", Toast.LENGTH_SHORT).show();
        }
    }

    private void handleRegister() {
        String username = binding.etUsername.getText() != null
                ? binding.etUsername.getText().toString().trim() : "";
        String password = binding.etPassword.getText() != null
                ? binding.etPassword.getText().toString().trim() : "";

        if (username.length() < 2 || password.length() < 4) {
            Toast.makeText(this, "שם משתמש (לפחות 2 תווים) וסיסמה (לפחות 4 תווים)", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean created = dbHelper.registerUser(username, password);
        if (created) {
            saveActiveUser(username);
            Toast.makeText(this, "נרשמת בהצלחה! מתחבר אוטומטית...", Toast.LENGTH_SHORT).show();
            navigateToQuestionnaire(username);
        } else {
            Toast.makeText(this, "שם המשתמש כבר קיים, נסי להתחבר", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveActiveUser(String username) {
        getSharedPreferences(PREFS_SESSION, MODE_PRIVATE)
                .edit()
                .putString(KEY_ACTIVE_USER, username)
                .apply();
    }

    private void navigateToQuestionnaire(String username) {
        Intent intent = new Intent(this, QuestionnaireActivity.class);
        intent.putExtra(EXTRA_USERNAME, username);
        startActivity(intent);
    }
}