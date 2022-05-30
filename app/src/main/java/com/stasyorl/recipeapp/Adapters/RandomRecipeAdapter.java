package com.stasyorl.recipeapp.Adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.stasyorl.recipeapp.Listeners.AddToFavListener;
import com.stasyorl.recipeapp.Listeners.RecipeClickListener;
import com.stasyorl.recipeapp.Models.Recipe;
import com.stasyorl.recipeapp.R;

import java.util.List;

public class RandomRecipeAdapter extends RecyclerView.Adapter<RandomRecipeViewHolder>{
    Context context;
    List<Recipe> list;
    RecipeClickListener listener;
    AddToFavListener favListener;
    String userId;
    DatabaseReference favDatabaseReference;


    public RandomRecipeAdapter(Context context, List<Recipe> list, RecipeClickListener listener, AddToFavListener favListener, String userId, DatabaseReference favDatabaseReference) {
        this.context = context;
        this.list = list;
        this.listener = listener;
        this.favListener = favListener;
        this.userId = userId;
        this.favDatabaseReference = favDatabaseReference;
    }

    @NonNull
    @Override
    public RandomRecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RandomRecipeViewHolder(LayoutInflater.from(context).inflate(R.layout.list_random_recipe, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RandomRecipeViewHolder holder, int position) {
        holder.textView_title.setText(list.get(position).title);
        holder.textView_title.setSelected(true);
        holder.textView_likes.setText(list.get(position).aggregateLikes+" Likes");
        holder.textView_servings.setText(list.get(position).servings+" Servings");
        holder.textView_time.setText(list.get(position).readyInMinutes+" Minutes");
        Picasso.get().load(list.get(position).image).into(holder.imageView_food);
        if (holder.imageView_food.getDrawable() == null){
            holder.imageView_food.setImageResource(R.mipmap.ic_no_photo_foreground);
        }

        holder.random_list_container.setOnClickListener(view -> listener.onRecipeClicked(String.valueOf(list.get(holder.getAdapterPosition()).id)));

        holder.fvrt_button.setOnClickListener(view -> {
            if(userId!=null){

                favDatabaseReference.child(userId).child("SavedRecipes").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.hasChild(list.get(position).id)){
                            holder.fav_image.setImageResource(R.drawable.ic_checked);
                        }else{
                            holder.fav_image.setImageResource(R.drawable.ic_unchecked);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }else{
                holder.fav_image.setImageResource(R.drawable.ic_unchecked);
            }


            favListener.onButtonClicked(holder.textView_title.getText(),
                    holder.textView_likes.getText(), holder.textView_servings.getText(),
                    holder.textView_time.getText(), list.get(position).image,
                    String.valueOf(list.get(holder.getAdapterPosition()).id));
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

}
class RandomRecipeViewHolder extends RecyclerView.ViewHolder {

    CardView random_list_container;
    TextView textView_title, textView_servings, textView_likes, textView_time;
    ImageView imageView_food;
    CardView fvrt_button;
    ImageView fav_image;


    public RandomRecipeViewHolder(@NonNull View itemView) {
        super(itemView);
        random_list_container = itemView.findViewById(R.id.random_list_container);
        textView_title = itemView.findViewById(R.id.textView_title);
        textView_servings = itemView.findViewById(R.id.textView_servings);
        textView_likes = itemView.findViewById(R.id.textView_likes);
        textView_time = itemView.findViewById(R.id.textView_time);
        imageView_food = itemView.findViewById(R.id.imageView_food);
        fvrt_button = itemView.findViewById(R.id.fvrt_button);
        fav_image = itemView.findViewById(R.id.fav_image);
    }
}