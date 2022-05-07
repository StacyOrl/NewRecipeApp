package com.stasyorl.recipeapp.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.radiobutton.MaterialRadioButton;
import com.stasyorl.recipeapp.Listeners.CategoryListener;
import com.stasyorl.recipeapp.Models.CategoryModel;
import com.stasyorl.recipeapp.R;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryViewHolder> {
    @NonNull

    Context context;
    private CategoryListener itemListener;
    private ArrayList<CategoryModel> categories;

    public CategoryAdapter(@NonNull Context context, ArrayList<CategoryModel> categoryNames, CategoryListener listener) {
        this.context = context;
        this.categories = categoryNames;
        this.itemListener = listener;
    }

    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CategoryViewHolder(LayoutInflater.from(context).inflate(R.layout.category_item, parent, false));
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        holder.txt.setText(categories.get(position).getName());
       if(categories.get(position).isChecked()){
           holder.category_item.setCardBackgroundColor(ContextCompat.getColor(context, R.color.dark_green));
           holder.txt.setTextColor(ContextCompat.getColor(context, R.color.white));

       }else {
           holder.category_item.setCardBackgroundColor(ContextCompat.getColor(context, R.color.grey_bg));
           holder.txt.setTextColor(R.color.black);
       }
        holder.category_item.setOnClickListener(view -> {

            categories.get(position).setChecked(true);
            uncheckedAnother(position);
            notifyDataSetChanged();
            itemListener.onCategoryClicked(position);

        });
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }


    private void uncheckedAnother(int position) {
        for (int i = 0; i < categories.size(); i++) {
            if (position != i) categories.get(i).setChecked(false);
        }
    }


}

class CategoryViewHolder extends RecyclerView.ViewHolder {
    MaterialCardView category_item;
    TextView txt;

    public CategoryViewHolder(@NonNull View itemView) {
        super(itemView);
        category_item = itemView.findViewById(R.id.category_item);
        txt = itemView.findViewById(R.id.btn_text);
    }

}



