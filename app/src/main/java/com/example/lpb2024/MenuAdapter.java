package com.example.lpb2024;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MenuViewHolder> {

    private Context context;
    private List<Menu> menus;
    private OnMenuClickListener onMenuClickListener;

    public interface OnMenuClickListener {
        void onMenuClick(Menu menu);
    }

    public MenuAdapter(Context context, List<Menu> menus, OnMenuClickListener onMenuClickListener) {
        this.context = context;
        this.menus = menus;
        this.onMenuClickListener = onMenuClickListener;
    }

    @NonNull
    @Override
    public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.card_menu_item, parent, false);
        return new MenuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuViewHolder holder, int position) {
        Menu menu = menus.get(position);
        holder.menuTitle.setText(menu.getMenuName());

        // Load image using Glide
        Glide.with(context)
                .load(menu.getMenuImage())
                .into(holder.menuImage);

        holder.itemView.setOnClickListener(v -> onMenuClickListener.onMenuClick(menu));
    }

    @Override
    public int getItemCount() {
        return menus.size();
    }

    public static class MenuViewHolder extends RecyclerView.ViewHolder {
        ImageView menuImage;
        TextView menuTitle;

        public MenuViewHolder(@NonNull View itemView) {
            super(itemView);
            menuImage = itemView.findViewById(R.id.menuImage);
            menuTitle = itemView.findViewById(R.id.menuTitle);
        }
    }
}
