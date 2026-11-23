package com.example.tamar_final_project_android;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * AI-generated / assisted class structure (documented for Ministry of Education guidelines).
 * מתאם RecyclerView להצגת רשימת שותפים מועדפים/מותאמים ולמעבר לפרופיל המלא.
 */
public class PartnerAdapter extends RecyclerView.Adapter<PartnerAdapter.PartnerViewHolder> {

    public interface OnPartnerClickListener {
        void onPartnerClick(TravelPartner partner);
    }

    private final List<TravelPartner> partners;
    private final OnPartnerClickListener listener;

    public PartnerAdapter(List<TravelPartner> partners, OnPartnerClickListener listener) {
        this.partners = partners;
        this.listener = listener;
    }

    public void updateData(List<TravelPartner> newList) {
        this.partners.clear();
        this.partners.addAll(newList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PartnerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_partner_card, parent, false);
        return new PartnerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PartnerViewHolder holder, int position) {
        TravelPartner partner = partners.get(position);
        holder.tvNameAge.setText(partner.getHeaderText());
        String hobbiesDisplay = partner.getHobbiesCsv().replace(",", ", ");
        holder.tvCityHobbies.setText(partner.getCity() + " - " + hobbiesDisplay);
        holder.ivAvatar.setImageResource(partner.getAvatarResId());

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onPartnerClick(partner);
            }
        });
    }

    @Override
    public int getItemCount() {
        return partners.size();
    }

    static class PartnerViewHolder extends RecyclerView.ViewHolder {
        ImageView ivAvatar;
        TextView tvNameAge;
        TextView tvCityHobbies;

        PartnerViewHolder(@NonNull View itemView) {
            super(itemView);
            ivAvatar = itemView.findViewById(R.id.ivItemAvatar);
            tvNameAge = itemView.findViewById(R.id.tvItemNameAge);
            tvCityHobbies = itemView.findViewById(R.id.tvItemCityAndHobbies);
        }
    }
}