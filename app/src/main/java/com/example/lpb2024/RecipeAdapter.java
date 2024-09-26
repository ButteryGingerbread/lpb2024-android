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

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {
    private List<Recipe> recipes;
    private Context context;
    private OnRecipeClickListener onRecipeClickListener;

    public RecipeAdapter(Context context, List<Recipe> recipes, OnRecipeClickListener onRecipeClickListener) {
        this.context = context;
        this.recipes = recipes;
        this.onRecipeClickListener = onRecipeClickListener;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.card_menu_item, parent, false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        Recipe recipe = recipes.get(position);
        holder.menuTitleTextView.setText(recipe.getMenuName());

        String imageUrl = recipe.getMenuImage();
        Glide.with(context)
                .load(imageUrl)
                .placeholder(R.drawable.sample_image)
                .into(holder.menuImageView);

        holder.itemView.setOnClickListener(v -> onRecipeClickListener.onRecipeClick(recipe));
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    public void updateData(List<Recipe> newRecipes) {
        recipes.clear();
        recipes.addAll(newRecipes);
        notifyDataSetChanged();
    }

    public interface OnRecipeClickListener {
        void onRecipeClick(Recipe recipe);
    }

    public static class RecipeViewHolder extends RecyclerView.ViewHolder {
        TextView menuTitleTextView;
        ImageView menuImageView;

        public RecipeViewHolder(@NonNull View itemView) {
            super(itemView);
            menuTitleTextView = itemView.findViewById(R.id.menuTitle);
            menuImageView = itemView.findViewById(R.id.menuImage);
        }
    }
}

