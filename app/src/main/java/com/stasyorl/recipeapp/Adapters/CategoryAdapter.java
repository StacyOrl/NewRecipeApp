package com.stasyorl.recipeapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.stasyorl.recipeapp.Listeners.CategoryListener;
import com.stasyorl.recipeapp.R;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryViewHolder> {
    @NonNull

    Context context;
    private String[] categoryNames;
    private CategoryListener itemListener;

    public CategoryAdapter(@NonNull Context context, String[] categoryNames) {
        this.context = context;
        this.categoryNames = categoryNames;
    }

    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CategoryViewHolder(LayoutInflater.from(context).inflate(R.layout.category_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        holder.category_item.setText(categoryNames[position]);
        holder.category_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(holder.category_item.isEnabled()){
//                    holder.category_item.setBackgroundColor(ContextCompat.getColor(context, R.color.dark_green));
                    holder.category_item.setEnabled(false);
                }
                else{
                    holder.category_item.setEnabled(true);
//                    holder.category_item.setBackgroundColor(ContextCompat.getColor(context, R.color.grey_bg));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return categoryNames.length;
    }

    public void setClickListener(CategoryListener categoryListener) {
        this.itemListener = categoryListener;
    }

    private void uncheckedAnother(int position){

    }


}
class CategoryViewHolder extends RecyclerView.ViewHolder{
    Button category_item;

    public CategoryViewHolder(@NonNull View itemView) {
        super(itemView);
        category_item = itemView.findViewById(R.id.category_item);
    }



}


