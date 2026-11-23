package com.example.tamar_final_project_android;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.tamar_final_project_android.databinding.ActivityDiscoverBinding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * AI-generated / assisted class structure (documented for Ministry of Education guidelines).
 * מסך 3: מציאת שותפים לפי תחביבים משותפים, כפתורי דחייה/אהבתי, ורשימת התאמות ב-RecyclerView.
 */
public class DiscoverActivity extends AppCompatActivity {

    public static final String EXTRA_PARTNER = "extra_partner";

    private ActivityDiscoverBinding binding;
    private TripMateDbHelper dbHelper;
    private String currentUsername;
    private List<String> userHobbies = new ArrayList<>();
    private final List<TravelPartner> discoveryQueue = new ArrayList<>();
    private int currentIndex = 0;
    private boolean filterBySharedOnly = false;
    private PartnerAdapter likedAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDiscoverBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        dbHelper = new TripMateDbHelper(this);
        currentUsername = getIntent().getStringExtra(LoginActivity.EXTRA_USERNAME);
        if (currentUsername == null || currentUsername.isEmpty()) {
            currentUsername = "אורח";
        }

        setupRecyclerView();
        loadPartnersAndHobbies();
        setupListeners();
        showDiscoverTab();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadPartnersAndHobbies();
        refreshCurrentCard();
        refreshLikedList();
    }

    private void setupRecyclerView() {
        likedAdapter = new PartnerAdapter(new ArrayList<>(), this::openPartnerProfile);
        binding.rvLikedPartners.setLayoutManager(new LinearLayoutManager(this));
        binding.rvLikedPartners.setAdapter(likedAdapter);
    }

    private void loadPartnersAndHobbies() {
        String rawHobbies = dbHelper.getUserHobbies(currentUsername);
        userHobbies = new ArrayList<>();
        if (!rawHobbies.isEmpty()) {
            userHobbies.addAll(Arrays.asList(rawHobbies.split(",")));
        }

        List<TravelPartner> all = dbHelper.getAllPartners();
        Collections.sort(all, (a, b) -> Integer.compare(
                b.calculateSharedHobbiesCount(userHobbies),
                a.calculateSharedHobbiesCount(userHobbies)
        ));

        discoveryQueue.clear();
        for (TravelPartner p : all) {
            if (!filterBySharedOnly || p.calculateSharedHobbiesCount(userHobbies) > 0) {
                discoveryQueue.add(p);
            }
        }
        if (currentIndex >= discoveryQueue.size()) {
            currentIndex = 0;
        }
    }

    private void setupListeners() {
        binding.fabDislike.setOnClickListener(v -> {
            if (discoveryQueue.isEmpty()) return;
            currentIndex = (currentIndex + 1) % discoveryQueue.size();
            refreshCurrentCard();
        });

        binding.fabLike.setOnClickListener(v -> {
            if (discoveryQueue.isEmpty()) return;
            TravelPartner current = discoveryQueue.get(currentIndex);
            current.setLiked(true);
            dbHelper.setPartnerLiked(current.getId(), true);
            Toast.makeText(this, "סימנת שאהבת את " + current.getName(), Toast.LENGTH_SHORT).show();
            openPartnerProfile(current);
        });

        binding.cardPartner.setOnClickListener(v -> {
            if (!discoveryQueue.isEmpty()) {
                openPartnerProfile(discoveryQueue.get(currentIndex));
            }
        });

        binding.btnFilterByHobbies.setOnClickListener(v -> {
            filterBySharedOnly = !filterBySharedOnly;
            currentIndex = 0;
            loadPartnersAndHobbies();
            refreshCurrentCard();
            String msg = filterBySharedOnly
                    ? "מציג רק מועמדים עם תחביבים משותפים"
                    : "מציג את כל המועמדים";
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        });

        binding.btnNavDiscover.setOnClickListener(v -> showDiscoverTab());
        binding.btnNavMatches.setOnClickListener(v -> showMatchesTab());
        binding.btnNavProfile.setOnClickListener(v -> {
            Intent intent = new Intent(this, QuestionnaireActivity.class);
            intent.putExtra(LoginActivity.EXTRA_USERNAME, currentUsername);
            startActivity(intent);
        });
    }

    private void showDiscoverTab() {
        binding.cardPartner.setVisibility(View.VISIBLE);
        binding.layoutActionButtons.setVisibility(View.VISIBLE);
        binding.rvLikedPartners.setVisibility(View.GONE);
        refreshCurrentCard();
    }

    private void showMatchesTab() {
        binding.cardPartner.setVisibility(View.GONE);
        binding.layoutActionButtons.setVisibility(View.GONE);
        binding.rvLikedPartners.setVisibility(View.VISIBLE);
        refreshLikedList();
    }

    private void refreshCurrentCard() {
        if (discoveryQueue.isEmpty()) {
            binding.tvPartnerNameAge.setText("אין הצעות כרגע");
            binding.tvPartnerCity.setText("נסי לשנות מסנן תחביבים");
            binding.tvMatchScore.setText("");
            return;
        }
        TravelPartner partner = discoveryQueue.get(currentIndex);
        binding.tvPartnerNameAge.setText(partner.getHeaderText());
        binding.tvPartnerCity.setText("מיקום: " + partner.getCity());
        binding.ivPartnerPhoto.setImageResource(partner.getAvatarResId());

        int sharedCount = partner.calculateSharedHobbiesCount(userHobbies);
        binding.tvMatchScore.setText("התאמה: " + sharedCount + " תחביבים משותפים");
    }

    private void refreshLikedList() {
        List<TravelPartner> liked = new ArrayList<>();
        for (TravelPartner p : dbHelper.getAllPartners()) {
            if (p.isLiked()) {
                liked.add(p);
            }
        }
        likedAdapter.updateData(liked);
        if (liked.isEmpty()) {
            Toast.makeText(this, "עדיין לא סימנת פרופילים שאהבת", Toast.LENGTH_SHORT).show();
        }
    }

    private void openPartnerProfile(TravelPartner partner) {
        Intent intent = new Intent(this, ProfileDetailActivity.class);
        intent.putExtra(EXTRA_PARTNER, partner);
        intent.putExtra(LoginActivity.EXTRA_USERNAME, currentUsername);
        startActivity(intent);
    }
}