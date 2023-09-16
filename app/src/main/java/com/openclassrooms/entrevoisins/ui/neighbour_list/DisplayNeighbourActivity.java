package com.openclassrooms.entrevoisins.ui.neighbour_list;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.openclassrooms.entrevoisins.R;
import com.openclassrooms.entrevoisins.di.DI;
import com.openclassrooms.entrevoisins.model.Neighbour;
import com.openclassrooms.entrevoisins.service.NeighbourApiService;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DisplayNeighbourActivity extends AppCompatActivity {

    private Neighbour neighbour;
    @BindView(R.id.avatar)
    ImageView avatar;
    @BindView(R.id.nameWhite)
    TextView nameWhite;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.phoneNumber)
    TextView phone;
    @BindView(R.id.address)
    TextView address;
    @BindView(R.id.socialNetWork)
    TextView socialNetwork;
    @BindView(R.id.aboutMe)
    TextView aboutMe;
    @BindView(R.id.add_favorite_neighbour)
    ImageView favoriteButton;

    private NeighbourApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_neighbour);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(android.R.color.transparent)));

        apiService = DI.getNeighbourApiService();
        neighbour = (Neighbour) getIntent().getSerializableExtra("neighbour");
        this.setFavoriteStarsColor();
        Glide.with(this).load(neighbour.getAvatarUrl()).into(this.avatar);
        this.nameWhite.setText(neighbour.getName());
        this.name.setText(neighbour.getName());
        this.phone.setText(neighbour.getPhoneNumber());
        this.address.setText(neighbour.getAddress());
        this.socialNetwork.setText("www.facebook.fr/" + neighbour.getName().toLowerCase());
        this.aboutMe.setText(neighbour.getAboutMe());

        favoriteButton.setOnClickListener(v -> {
            //add or remove from favorite localy
            neighbour.setFavorite();
            //Apply change to the database via the API
            apiService.updateIsFavorite(neighbour);
            setFavoriteStarsColor();
        });
    }

    /**
     * Used to navigate to this activity
     * @param activity
     * @param neighbour
     */
    public static void navigate(FragmentActivity activity, Neighbour neighbour) {
        Intent intent = new Intent(activity, DisplayNeighbourActivity.class);
        intent.putExtra("neighbour", neighbour);
        activity.startActivity(intent);
    }

    /**
     * Used to set the color of the favorite Button
     */
    private void setFavoriteStarsColor()
    {
        if(neighbour.isFavorite()){
            favoriteButton.setImageResource(R.drawable.ic_star_white_24dp);
            favoriteButton.setColorFilter(ContextCompat.getColor(DisplayNeighbourActivity.this, R.color.gold), PorterDuff.Mode.SRC_ATOP);
        }
        else{
            favoriteButton.setImageResource(R.drawable.ic_star_border_white_24dp);
            favoriteButton.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(DisplayNeighbourActivity.this, R.color.gold)));
        }
      }
}
