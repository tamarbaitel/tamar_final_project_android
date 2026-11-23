package com.example.tamar_final_project_android;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * AI-generated / assisted class structure (documented for Ministry of Education guidelines).
 * מחלקת מודל המייצגת מועמד/ת לשותפות לטיול הגדול ומחשבת התאמה לפי תחביבים.
 */
public class TravelPartner implements Serializable {
    private int id;
    private String name;
    private int age;
    private String city;
    private String bio;
    private String hobbiesCsv;
    private int avatarResId;
    private boolean liked;

    public TravelPartner(int id, String name, int age, String city, String bio,
                         String hobbiesCsv, int avatarResId, boolean liked) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.city = city;
        this.bio = bio;
        this.hobbiesCsv = hobbiesCsv;
        this.avatarResId = avatarResId;
        this.liked = liked;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public String getCity() {
        return city;
    }

    public String getBio() {
        return bio;
    }

    public String getHobbiesCsv() {
        return hobbiesCsv;
    }

    public int getAvatarResId() {
        return avatarResId;
    }

    public boolean isLiked() {
        return liked;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }

    public List<String> getHobbiesList() {
        List<String> result = new ArrayList<>();
        if (hobbiesCsv == null || hobbiesCsv.trim().isEmpty()) {
            return result;
        }
        String[] parts = hobbiesCsv.split(",");
        for (String p : parts) {
            String trimmed = p.trim();
            if (!trimmed.isEmpty()) {
                result.add(trimmed);
            }
        }
        return result;
    }

    public int calculateSharedHobbiesCount(List<String> userHobbies) {
        if (userHobbies == null || userHobbies.isEmpty()) {
            return 0;
        }
        //4
        List<String> partnerHobbies = getHobbiesList();
        int count = 0;
        for (String hobby : userHobbies) {
            if (partnerHobbies.contains(hobby)) {

            }
        }
        return count;
    }

    public String getHeaderText() {
        return name + ", " + age;
    }

    public static List<String> getAllAvailableHobbies() {
        return Arrays.asList(
                "טיולים",
                "מוזיקה",
                "כושר",
                "סרטים וסדרות",
                "בעלי חיים",
                "קריאה"
        );
    }
}