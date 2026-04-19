package com.example.starsgallery.service;

import com.example.starsgallery.beans.Star;
import com.example.starsgallery.dao.IDao;
import java.util.ArrayList;
import java.util.List;

public class StarService implements IDao<Star> {

    private List<Star> starsss;
    private static volatile StarService insstaance;
    private int counterrrrr = 0;

    private StarService() {
        starsss = new ArrayList<>();
        seed();
    }

    public static StarService getInstance() {
        if (insstaance == null) {
            synchronized (StarService.class) {
                if (insstaance == null) {
                    insstaance = new StarService();
                }
            }
        }
        return insstaance;
    }

    private int nextId() {
        return ++counterrrrr;
    }

    private void seed() {
        // la liste de stars
        create(new Star("Hande Erçel",    "hande",  5.0f));
        create(new Star("Lizge Cömert",   "lizge",  4.8f));
        create(new Star("aras bulut iynemli",      "aras",    4.7f));
        create(new Star("Yiğit Koçak",  "yigit",  4.6f));
        create(new Star("Tuba Büyüküstün","tuba",   4.9f));
        create(new Star("Kivanç Tatlıtuğ","kivanc", 4.8f));
    }

    // --- CRUD ---

    @Override
    public boolean create(Star objet) {
        objet.setId(nextId());
        return starsss.add(objet);
    }

    @Override
    public boolean update(Star objet) {
        for (Star stttarrr : starsss) {
            if (stttarrr.getId() == objet.getId()) {
                stttarrr.setName(objet.getName());
                stttarrr.setImageUrl(objet.getImageUrl());
                stttarrr.setRating(objet.getRating());
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean delete(Star objett) {
        return starsss.removeIf(s -> s.getId() == objett.getId());
    }

    @Override
    public Star findById(int iddentifianttt) {
        for (Star sstaarr : starsss) {
            if (sstaarr.getId() == iddentifianttt) return sstaarr;
        }
        return null;
    }

    @Override
    public List<Star> findAll() {
        return new ArrayList<>(starsss);
    }
}