package com.example.starsgallery.adapter;

import android.app.AlertDialog;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.starsgallery.R;
import com.example.starsgallery.beans.Star;
import com.example.starsgallery.service.StarService;
import de.hdodenhof.circleimageview.CircleImageView;

import java.util.ArrayList;
import java.util.List;

public class StarAdapter extends RecyclerView.Adapter<StarAdapter.StarViewHolder>
        implements Filterable {

    private final List<Star> starsAll;
    private List<Star> starsFiltered;

    public StarAdapter(List<Star> stars) {
        this.starsAll      = new ArrayList<>(stars);
        this.starsFiltered = new ArrayList<>(stars);
    }

    @NonNull
    @Override
    public StarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.star_item, parent, false);
        return new StarViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull StarViewHolder holder, int position) {
        Star s = starsFiltered.get(position);

        holder.name.setText(s.getName().toUpperCase());
        holder.rating.setRating(s.getRating());
        holder.idText.setText("#" + s.getId());

        int resId = holder.itemView.getContext()
                .getResources()
                .getIdentifier(
                        s.getImageUrl(),
                        "drawable",
                        holder.itemView.getContext().getPackageName()
                );

        Glide.with(holder.itemView.getContext())
                .load(resId != 0 ? resId : R.mipmap.ic_launcher)
                .apply(new RequestOptions()
                        .placeholder(R.mipmap.ic_launcher)
                        .error(R.mipmap.ic_launcher)
                        .circleCrop())
                .into(holder.img);

        holder.itemView.setOnClickListener(v ->
                afficherPopupNotation(holder, s)
        );
    }

    private void afficherPopupNotation(StarViewHolder holder, Star star) {
        View popup = LayoutInflater.from(holder.itemView.getContext())
                .inflate(R.layout.dialog_rating, null, false);

        TextView        popuuppNom  = popup.findViewById(R.id.tvvvName);
        TextView        popuuppValuuee = popup.findViewById(R.id.tvScore);
        CircleImageView popuuppImgg   = popup.findViewById(R.id.imgStar);
        RatingBar       popuupBaarr   = popup.findViewById(R.id.ratingBar);
        Button          btnCancel  = popup.findViewById(R.id.btnCancel);
        Button          btnValidate = popup.findViewById(R.id.btnValidate);

        popuuppNom.setText(star.getName());
        popuupBaarr.setRating(star.getRating());

        if (popuuppValuuee != null) {
            popuuppValuuee.setText(star.getRating() + " / 5.0");
        }

        // --- ANIMATIONS ---
        popuuppImgg.setAlpha(0f);
        popuuppImgg.setTranslationY(-80f);
        popuuppImgg.animate().alpha(1f).translationY(0f).setDuration(600).setStartDelay(100).start();

        popuuppNom.setAlpha(0f);
        popuuppNom.animate().alpha(1f).setDuration(600).setStartDelay(300).start();

        popuupBaarr.setAlpha(0f);
        popuupBaarr.setTranslationY(100f);
        popuupBaarr.animate()
                .alpha(1f)
                .translationY(0f)
                .setDuration(800)
                .setStartDelay(400)
                .setInterpolator(new OvershootInterpolator())
                .start();

        popuupBaarr.setOnRatingBarChangeListener((ratingBar, rating, fromUser) -> {
            if (popuuppValuuee != null) {
                popuuppValuuee.setText(String.format("%.1f / 5.0", rating));
            }
        });

        int resIdPopup = holder.itemView.getContext()
                .getResources()
                .getIdentifier(
                        star.getImageUrl(),
                        "drawable",
                        holder.itemView.getContext().getPackageName()
                );

        Glide.with(holder.itemView.getContext())
                .load(resIdPopup != 0 ? resIdPopup : R.mipmap.ic_launcher)
                .apply(new RequestOptions()
                        .placeholder(R.mipmap.ic_launcher)
                        .error(R.mipmap.ic_launcher)
                        .circleCrop())
                .into(popuuppImgg);

        AlertDialog dialog = new AlertDialog.Builder(holder.itemView.getContext())
                .setView(popup)
                .create();

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dialog.getWindow().setWindowAnimations(android.R.style.Animation_Dialog);
        }

        btnCancel.setOnClickListener(v -> dialog.dismiss());
        btnValidate.setOnClickListener(v -> {
            float nouvelleNote = popuupBaarr.getRating();
            star.setRating(nouvelleNote);
            StarService.getInstance().update(star);
            notifyItemChanged(holder.getAdapterPosition());
            dialog.dismiss();
        });

        dialog.show();
    }

    @Override
    public int getItemCount() {
        return starsFiltered.size();
    }

    public void updateData(List<Star> newStars) {
        starsAll.clear();
        starsAll.addAll(newStars);
        starsFiltered = new ArrayList<>(starsAll);
        notifyDataSetChanged();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence query) {
                List<Star> result = new ArrayList<>();
                if (query == null || query.length() == 0) {
                    result.addAll(starsAll);
                } else {
                    String pattern = normalise(query.toString().trim());
                    for (Star s : starsAll) {
                        String[] words = normalise(s.getName()).split(" ");
                        for (String word : words) {
                            if (word.startsWith(pattern)) {
                                result.add(s);
                                break;
                            }
                        }
                    }
                }
                FilterResults fr = new FilterResults();
                fr.values = result;
                fr.count  = result.size();
                return fr;
            }

            @Override
            @SuppressWarnings("unchecked")
            protected void publishResults(CharSequence query, FilterResults results) {
                if (results.values instanceof List) {
                    starsFiltered = (List<Star>) results.values;
                    notifyDataSetChanged();
                }
            }
        };
    }

    private String normalise(String text) {
        return text.toLowerCase()
                .replace("ı", "i").replace("İ", "i")
                .replace("ğ", "g").replace("Ğ", "g")
                .replace("ş", "s").replace("Ş", "s")
                .replace("ç", "c").replace("Ç", "c")
                .replace("ö", "o").replace("Ö", "o")
                .replace("ü", "u").replace("Ü", "u");
    }

    public static class StarViewHolder extends RecyclerView.ViewHolder {
        public CircleImageView img;
        public TextView        name;
        public RatingBar       rating;
        public TextView        idText;

        public StarViewHolder(View itemView) {
            super(itemView);
            img    = itemView.findViewById(R.id.img);
            name   = itemView.findViewById(R.id.name);
            rating = itemView.findViewById(R.id.stars);
            idText = itemView.findViewById(R.id.ids);
        }
    }
}