package com.example.tamar_final_project_android;

import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tamar_final_project_android.databinding.ActivityProfileDetailBinding;
import com.google.android.material.chip.Chip;

import java.util.List;

/**
 * AI-generated / assisted class structure (documented for Ministry of Education guidelines).
 * מסך 4: פרופיל אישי מפורט של השותף/ה ושליחת הודעה השמורה בבסיס הנתונים.
 */
public class ProfileDetailActivity extends AppCompatActivity {

    private ActivityProfileDetailBinding binding;
    private TripMateDbHelper dbHelper;
    private TravelPartner partner;
    private String currentUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        dbHelper = new TripMateDbHelper(this);
        currentUsername = getIntent().getStringExtra(LoginActivity.EXTRA_USERNAME);
        if (currentUsername == null || currentUsername.isEmpty()) {
            currentUsername = "אורח";
        }

        Object rawPartner = getIntent().getSerializableExtra(DiscoverActivity.EXTRA_PARTNER);
        if (rawPartner instanceof TravelPartner) {
            partner = (TravelPartner) rawPartner;
        } else {
            partner = new TravelPartner(1, "נועה", 24, "תל אביב",
                    "אוהבת טיולים, מוזיקה וכלבים", "טיולים,מוזיקה,כושר,סרטים וסדרות,בישול,חברים",
                    android.R.drawable.sym_def_app_icon, true);
        }

        bindPartnerData();

        binding.btnBackProfile.setOnClickListener(v -> finish());
        binding.btnProfileOptions.setOnClickListener(v ->
                Toast.makeText(this, "שותף מומלץ לטיול: " + partner.getName(), Toast.LENGTH_SHORT).show());
        binding.btnSendMessage.setOnClickListener(v -> showSendMessageDialog());
    }

    private void bindPartnerData() {
        binding.tvDetailNameAge.setText(partner.getHeaderText());
        binding.tvDetailCity.setText("מיקום: " + partner.getCity());
        binding.tvDetailBio.setText(partner.getBio());
        binding.ivDetailAvatar.setImageResource(partner.getAvatarResId());

        binding.chipGroupInterests.removeAllViews();
        List<String> hobbies = partner.getHobbiesList();
        for (String hobby : hobbies) {
            Chip chip = new Chip(this);
            chip.setText(hobby);
            chip.setClickable(false);
            chip.setCheckable(false);
            chip.setChipBackgroundColorResource(R.color.tm_teal_light);
            chip.setTextColor(getColor(R.color.tm_text_dark));
            binding.chipGroupInterests.addView(chip);
        }
    }

    private void showSendMessageDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("שליחת הודעה ל" + partner.getName());

        final EditText input = new EditText(this);
        input.setHint("כתבי הודעה לגבי הטיול הגדול...");
        input.setPadding(40, 32, 40, 32);
        builder.setView(input);

        builder.setPositiveButton("שלח", (dialog, which) -> {
            String text = input.getText().toString().trim();
            if (text.isEmpty()) {
                Toast.makeText(this, "לא ניתן לשלוח הודעה ריקה", Toast.LENGTH_SHORT).show();
                return;
            }
            dbHelper.insertMessage(currentUsername, partner.getName(), text);
            Toast.makeText(this, "ההודעה נשלחה ל" + partner.getName() + " ונשמרה!", Toast.LENGTH_SHORT).show();
        });
        builder.setNegativeButton("ביטול", (dialog, which) -> dialog.cancel());
        builder.show();
    }
}