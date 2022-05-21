package com.stasyorl.recipeapp.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.squareup.picasso.Picasso;
import com.stasyorl.recipeapp.Models.Recipe;
import com.stasyorl.recipeapp.R;

public class FavouritesAdapter extends FirebaseRecyclerAdapter<Recipe, FavouritesAdapter.FavouritesViewHolder> {


    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public FavouritesAdapter(@NonNull FirebaseRecyclerOptions<Recipe> options) {


        super(options);
    }
    @Override
    protected void onBindViewHolder(@NonNull FavouritesViewHolder holder, int position, @NonNull Recipe model) {
        holder.textView_title.setText(model.getTitle());
        holder.textView_title.setSelected(true);
        holder.textView_likes.setText(String.valueOf(model.getAggregateLikes()));
        holder.textView_servings.setText(String.valueOf(model.getServings()));
        holder.textView_time.setText(String.valueOf(model.getReadyInMinutes()));
        Picasso.get().load(model.getImage()).into(holder.imageView_food);

        holder.fav_image.setImageResource(R.drawable.ic_checked);

    }

    @NonNull
    @Override
    public FavouritesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_random_recipe, parent, false);
        return new FavouritesAdapter.FavouritesViewHolder(view);
    }

    public class FavouritesViewHolder extends RecyclerView.ViewHolder{

        CardView random_list_container;
        TextView textView_title, textView_servings, textView_likes, textView_time;
        ImageView imageView_food,fav_image;
        CardView fvrt_button;

        public FavouritesViewHolder(@NonNull View itemView) {
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
}


