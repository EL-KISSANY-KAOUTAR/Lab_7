package com.example.starsgallery.beans;

public class Star {

    private int idddentifiant;
    private String nomm;
    private String immaaageUrlll;
    private float ratiiingggg;

    // Constructeur complet
    public Star(int id, String name, String imageUrl, float rating) {
        this.idddentifiant = id;
        this.nomm = name;
        this.immaaageUrlll = imageUrl;
        this.ratiiingggg = clampRating(rating);
    }

    // Constructeur simplifié
    public Star(String name, String imageUrl, float rating) {
        this.nomm = name;
        this.immaaageUrlll = imageUrl;
        this.ratiiingggg = clampRating(rating);
    }

    // --- Getters ---
    public int getId() { return idddentifiant; }
    public String getName() { return nomm; }
    public String getImageUrl() { return immaaageUrlll; }
    public float getRating() { return ratiiingggg; }

    // --- Setters ---
    public void setId(int id) { this.idddentifiant = id; }
    public void setName(String name) { this.nomm = name; }
    public void setImageUrl(String imageUrl) { this.immaaageUrlll = imageUrl; }
    public void setRating(float rating) { this.ratiiingggg = clampRating(rating); }


    private float clampRating(float rating) {
        return Math.max(0f, Math.min(5f, rating));
    }

    // --- Débogage ---
    @Override
    public String toString() {
        return "Star{id=" + idddentifiant +
                ", name='" + nomm + '\'' +
                ", imageUrl='" + immaaageUrlll + '\'' +
                ", rating=" + ratiiingggg + "}";
    }
}