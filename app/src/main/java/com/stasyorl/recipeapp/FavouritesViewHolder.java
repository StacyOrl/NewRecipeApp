package com.stasyorl.recipeapp;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class FavouritesViewHolder extends RecyclerView.ViewHolder{
    CardView random_list_container;
    TextView textView_title, textView_servings, textView_likes, textView_time;
    ImageView imageView_food,fav_image;
    public CardView fvrt_button;
    DatabaseReference databaseReference;



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




//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    mClickListener.onItemClick(v, getAdapterPosition());
//
//                }
//            });
    }
    public void setItem(String title, String likes, String servings, String time, String image){
        textView_title.setText(title);
        textView_title.setSelected(true);
        textView_likes.setText(likes);
        textView_servings.setText(servings);
        textView_time.setText(time);
        Picasso.get().load(image).into(imageView_food);
        fav_image.setImageResource(R.drawable.ic_checked);

    }

    public void favouriteChecker(String id, String currentuserId) {
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(currentuserId).child("SavedRecipes").hasChild(id)){
                    fav_image.setImageResource(R.drawable.ic_checked);
                }else{
                    fav_image.setImageResource(R.drawable.ic_unchecked);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
//        FavouritesViewHolder.ClickListener mClickListener;

    //Interface to send callbacks...
//        public interface ClickListener{
//             void onItemClick(View view, int position);
//        }
//
//        public void setOnClickListener(FavouritesViewHolder.ClickListener clickListener){
//            mClickListener = clickListener;
//        }
}
