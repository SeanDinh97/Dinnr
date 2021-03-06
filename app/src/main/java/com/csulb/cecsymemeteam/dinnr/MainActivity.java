package com.csulb.cecsymemeteam.dinnr;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.sql.DatabaseMetaData;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DataStorage.generateRestaurants(DataStorage.listOfRestaurants);
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.main_restaurantPlaque);
        mDrawerLayout =(DrawerLayout) findViewById(R.id.drawer);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout,R.string.open, R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);
        changeProfile(DataStorage.restaurantIndex);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        Menu nav_menu = navigationView.getMenu();
        //////////////////////////////////////////////////////////////////////////
        //                      profile Plaque Listeners                        //
        //////////////////////////////////////////////////////////////////////////

        if(DataStorage.isLoggedIn()) {
            nav_menu.findItem(R.id.Account).setVisible(true);
            nav_menu.findItem(R.id.newAccount).setVisible(false);
            nav_menu.findItem(R.id.login).setVisible(false);
        }
        linearLayout.setOnTouchListener(new GestureListener(MainActivity.this)
        {
            @Override
            public void onSwipeRight()
            {
                if(DataStorage.listOfRestaurants.size() > 0){
                    DataStorage.listOfRestaurants.remove(0);
                    changeProfile(DataStorage.restaurantIndex);
                }
                else{
                    noMoreRestaurants();
                }
            }
            public void onSwipeLeft()
            {
                if(DataStorage.listOfRestaurants.size() > 0) {
                    DataStorage.listOfRestaurants.remove(0);
                    changeProfile(DataStorage.restaurantIndex);
                }
                else{
                    noMoreRestaurants();
                }
            }
        });
        //////////////////////////////////////////////////////////////////////////
        //                      profile Plaque Listeners                        //
        //////////////////////////////////////////////////////////////////////////

        //////////////////////////////////////////////////////////////////////////
        //                        like Button Listeners                         //
        //////////////////////////////////////////////////////////////////////////
        ImageButton like =(ImageButton) findViewById(R.id.main_rightBtn);
        like.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if(DataStorage.listOfRestaurants.size() > 1) {
                    DataStorage.listOfRestaurants.remove(0);
                    changeProfile(DataStorage.restaurantIndex);
                }
                else{
                    noMoreRestaurants();
                }
            }
        });
        //////////////////////////////////////////////////////////////////////////
        //                        like Button Listeners                         //
        //////////////////////////////////////////////////////////////////////////

        //////////////////////////////////////////////////////////////////////////
        //                      dislike Button Listeners                        //
        //////////////////////////////////////////////////////////////////////////
        ImageButton dislike =(ImageButton) findViewById(R.id.main_leftBtn);
        dislike.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if(DataStorage.listOfRestaurants.size() > 1) {
                    DataStorage.listOfRestaurants.remove(0);
                    changeProfile(DataStorage.restaurantIndex);
                }
                else{
                    noMoreRestaurants();
                }
            }
        });
        //////////////////////////////////////////////////////////////////////////
        //                      dislike Button Listeners                        //
        //////////////////////////////////////////////////////////////////////////

        //////////////////////////////////////////////////////////////////////////
        //                      Profile Button Listeners                        //
        //////////////////////////////////////////////////////////////////////////
        ImageButton profile = (ImageButton) findViewById(R.id.main_profileBtn);
        profile.setOnTouchListener(new GestureListener(MainActivity.this){
            @Override
            public void onSwipeRight()
            {
                if(DataStorage.listOfRestaurants.size() > 0) {
                    DataStorage.listOfRestaurants.remove(0);
                    changeProfile(DataStorage.restaurantIndex);
                }
                else{
                    noMoreRestaurants();
                }
            }
            public void onSwipeLeft() {
                if(DataStorage.listOfRestaurants.size() > 0) {
                    DataStorage.listOfRestaurants.remove(0);
                    changeProfile(DataStorage.restaurantIndex);
                }
                else{
                    noMoreRestaurants();
                }
            }

            @Override
            public void onTap() {
                if(DataStorage.listOfRestaurants.size() > 0) {
                    gotoProfile(findViewById(R.id.main_profileBtn));
                }
            }
        });
        //////////////////////////////////////////////////////////////////////////
        //                      Profile Button Listeners                        //
        //////////////////////////////////////////////////////////////////////////
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void changeProfile(int index){
        ImageView []setStar = {findViewById(R.id.main_star1),findViewById(R.id.main_star2),findViewById(R.id.main_star3),
                findViewById(R.id.main_star4),findViewById(R.id.main_star5)};
        ImageButton imgBtn = (ImageButton) findViewById(R.id.main_profileBtn);
        imgBtn.setImageResource(DataStorage.getListOfRestaurants().get(DataStorage.restaurantIndex).getRefToImg());
        TextView temp = (TextView) findViewById(R.id.main_restaurantName);
        temp.setText(DataStorage.getListOfRestaurants().get(DataStorage.restaurantIndex).getName());

        temp = findViewById(R.id.main_restaurantDistance);
        temp.setText(DataStorage.getListOfRestaurants().get(DataStorage.restaurantIndex).getDistance());


        for(int i = 0;i < DataStorage.getListOfRestaurants().get(DataStorage.restaurantIndex).getStarRating();i++)
        {
            setStar[i].setImageResource(android.R.drawable.btn_star_big_on);

        }
        if(DataStorage.getListOfRestaurants().get(DataStorage.restaurantIndex).getStarRating() <5)
        {
            for(int j=4;j>=DataStorage.getListOfRestaurants().get(DataStorage.restaurantIndex).getStarRating();j--)
            {
                setStar[j].setImageResource(android.R.drawable.btn_star_big_off);
            }
        }
       // ImageView star = (ImageView)findViewById(R.id.main_star1);
       // star.setImageResource(android.R.drawable.btn_star_big_on);

    }

    public void noMoreRestaurants(){
        ImageView []setStar = {findViewById(R.id.main_star1),findViewById(R.id.main_star2),findViewById(R.id.main_star3),
                findViewById(R.id.main_star4),findViewById(R.id.main_star5)};
        ImageButton imgBtn = (ImageButton) findViewById(R.id.main_profileBtn);
        if(DataStorage.listOfRestaurants.size() > 0){
            DataStorage.listOfRestaurants.remove(0);
        }
        imgBtn.setImageResource(R.drawable.no_more_restaurants);
        TextView temp = findViewById(R.id.main_restaurantName);
        temp.setText("");

        temp = findViewById(R.id.main_restaurantDistance);
        temp.setText("");

        for(int j = 0; j < 5; j++)
        {
            setStar[j].setImageResource(android.R.drawable.btn_star_big_off);
        }
    }

    public void gotoFavorites(View v){
        startActivity(new Intent(this, FavoritesActivity.class));
    }

    public void gotoProfile(View v){
        startActivity(new Intent(this, ProfileActivity.class));
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.login) {
            // Toast.makeText(this,"login",Toast.LENGTH_SHORT.show());
            startActivity(new Intent(this, LoginActivity.class));
        }
        if (id == R.id.newAccount) {
            // Toast.makeText(this,"login",Toast.LENGTH_SHORT.show());
            startActivity(new Intent(this, NewAccountActivity.class));
        }
        if (id == R.id.Favorite)
        {
            startActivity(new Intent(this, FavoritesActivity.class));
        }
        return false;
    }
}
