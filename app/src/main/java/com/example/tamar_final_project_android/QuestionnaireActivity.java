package com.example.tamar_final_project_android;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tamar_final_project_android.databinding.ActivityQuestionnaireBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * AI-generated / assisted class structure (documented for Ministry of Education guidelines).
 * מסך 2: שאלון אישי לבחירת תחביבים מרובים ושמירתם בבסיס הנתונים.
 */
public class QuestionnaireActivity extends AppCompatActivity {

    private ActivityQuestionnaireBinding binding;
    private TripMateDbHelper dbHelper;
    private String currentUsername;
    private final List<String> selectedHobbies = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityQuestionnaireBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        dbHelper = new TripMateDbHelper(this);
        currentUsername = getIntent().getStringExtra(LoginActivity.EXTRA_USERNAME);
        if (currentUsername == null || currentUsername.isEmpty()) {
            currentUsername = "אורח";
        }

        setupHobbyCards();
        preloadExistingHobbies();

        binding.btnBackQuestionnaire.setOnClickListener(v -> finish());
        binding.btnNextQuestionnaire.setOnClickListener(v -> saveAndContinue());
    }

    private void setupHobbyCards() {
        bindHobbyToggle(binding.cardTravel, "טיולים");
        bindHobbyToggle(binding.cardMusic, "מוזיקה");
        bindHobbyToggle(binding.cardFitness, "כושר");
        bindHobbyToggle(binding.cardMovies, "סרטים וסדרות");
        bindHobbyToggle(binding.cardPets, "בעלי חיים");
        bindHobbyToggle(binding.cardReading, "קריאה");
    }

    private void bindHobbyToggle(View cardView, String hobbyName) {
        cardView.setOnClickListener(v -> {
            boolean nowSelected = !cardView.isSelected();
            cardView.setSelected(nowSelected);
            if (nowSelected) {
                if (!selectedHobbies.contains(hobbyName)) {
                    selectedHobbies.add(hobbyName);
                }
            } else {
                selectedHobbies.remove(hobbyName);
            }
            updateProgressIndicator();
        });
    }

    private void preloadExistingHobbies() {
        String existing = dbHelper.getUserHobbies(currentUsername);
        if (existing.isEmpty()) {
            updateProgressIndicator();
            return;
        }
        String[] parts = existing.split(",");
        for (String h : parts) {
            String hobby = h.trim();
            if ("טיולים".equals(hobby)) selectCard(binding.cardTravel, hobby);
            else if ("מוזיקה".equals(hobby)) selectCard(binding.cardMusic, hobby);
            else if ("כושר".equals(hobby)) selectCard(binding.cardFitness, hobby);
            else if ("סרטים וסדרות".equals(hobby)) selectCard(binding.cardMovies, hobby);
            else if ("בעלי חיים".equals(hobby)) selectCard(binding.cardPets, hobby);
            else if ("קריאה".equals(hobby)) selectCard(binding.cardReading, hobby);
        }
        updateProgressIndicator();
    }

    private void selectCard(View card, String hobby) {
        card.setSelected(true);
        if (!selectedHobbies.contains(hobby)) {
            selectedHobbies.add(hobby);
        }
    }

    private void updateProgressIndicator() {
        int stepValue = Math.min(6, Math.max(1, selectedHobbies.size() + 1));
        binding.progressQuestionnaire.setProgress(stepValue);
        binding.tvStepCounter.setText(stepValue + " / 6");
    }

    private void saveAndContinue() {
        if (selectedHobbies.isEmpty()) {
            Toast.makeText(this, "בחרי לפחות תחביב אחד כדי למצוא שותפות מתאימות", Toast.LENGTH_SHORT).show();
            return;
        }
        String hobbiesCsv = TextUtils.join(",", selectedHobbies);
        dbHelper.updateUserHobbies(currentUsername, hobbiesCsv);

        Intent intent = new Intent(this, DiscoverActivity.class);
        intent.putExtra(LoginActivity.EXTRA_USERNAME, currentUsername);
        startActivity(intent);
    }
}