package com.stasyorl.recipeapp.Adapters;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.stasyorl.recipeapp.Listeners.RecipeClickListener;
import com.stasyorl.recipeapp.Models.Recipe;
import com.stasyorl.recipeapp.R;

import java.util.List;

public class RandomRecipeAdapter extends RecyclerView.Adapter<RandomRecipeViewHolder>{
    Context context;
    List<Recipe> list;
    RecipeClickListener listener;
//    ActivityOptions options;

    public RandomRecipeAdapter(Context context, List<Recipe> list, RecipeClickListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }
//
//    public ActivityOptions getOptions() {
//        return options;
//    }

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

//        options = ActivityOptions.makeSceneTransitionAnimation(
//                (Activity)context, new Pair<View, String>(holder.imageView_food, "animImage"));

        holder.random_list_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onRecipeClicked(String.valueOf(list.get(holder.getAdapterPosition()).id));


//                context.startActivity(intent, options.toBundle());
            }
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

    public RandomRecipeViewHolder(@NonNull View itemView) {
        super(itemView);
        random_list_container = itemView.findViewById(R.id.random_list_container);
        textView_title = itemView.findViewById(R.id.textView_title);
        textView_servings = itemView.findViewById(R.id.textView_servings);
        textView_likes = itemView.findViewById(R.id.textView_likes);
        textView_time = itemView.findViewById(R.id.textView_time);
        imageView_food = itemView.findViewById(R.id.imageView_food);



    }
}