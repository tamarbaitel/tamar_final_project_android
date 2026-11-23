package com.example.tamar_final_project_android;

/**
 * AI-generated / assisted class structure (documented for Ministry of Education guidelines).
 * מחלקה אבסטרקטית המייצגת משתמש / פרופיל במערכת TripMate.
 */
public abstract class TripUser {

    protected String name;
    protected int age;
    protected String city;
    protected String hobbiesCsv;
    protected int avatarResId;

    public TripUser(String name, int age, String city, String hobbiesCsv, int avatarResId) {
        this.name = name;
        this.age = age;
        this.city = city;
        this.hobbiesCsv = hobbiesCsv;
        this.avatarResId = avatarResId;
    }

    // מתודה אבסטרקטית שכל מחלקה יורשת חייבת לממש
    public abstract String getProfileSummary();

    // מתודה משותפת לכל המשתמשים
    public String getHeaderText() {
        return name + ", " + age;
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

    public String getHobbiesCsv() {
        return hobbiesCsv;
    }

    public int getAvatarResId() {
        return avatarResId;
    }
}