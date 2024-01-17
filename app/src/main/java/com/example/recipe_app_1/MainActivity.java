package com.example.recipe_app_1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.recipe_app_1.Adapters.RandomRecipeAdapter;
import com.example.recipe_app_1.Listeners.RandomRecipeResponseListener;
import com.example.recipe_app_1.Listeners.RecipeClickListener;
import com.example.recipe_app_1.Models.RandomRecipeApiResponse;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    ProgressDialog dialog;
    Button logout;
    RequestManager manager;
    RandomRecipeAdapter randomRecipeAdapter;
    RecyclerView recyclerView;
    Spinner spinner;
    List<String> tags = new ArrayList<>();
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dialog =  new ProgressDialog(this);
        dialog.setTitle("Loading...");
        firebaseAuth = FirebaseAuth.getInstance();
        searchView = findViewById(R.id.searchView_home);
        logout=findViewById(R.id.logout);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Confirm Logout")
                        .setMessage("Are you sure you want to log out?")
                        .setPositiveButton("OK", (dialog, which) -> {
                            FirebaseAuth.getInstance().signOut();
                            Toast.makeText(MainActivity.this, "Logged out Successfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(MainActivity.this,LoginActivity.class));
                            finish();
                        })
                        .setNegativeButton("Cancel", (dialog, which) -> {
                        })
                        .show();
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                tags.clear();
                tags.add(query);
                manager.getRandomRecipes(randomRecipeResponseListener, tags);
                dialog.show();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        spinner = findViewById(R.id.spinner_tags);
        ArrayAdapter arrayAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.tags,
                R.layout.spinner_text
        );
        arrayAdapter.setDropDownViewResource(R.layout.spinner_inner_text);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(spinnerSelectedListener);

        manager = new RequestManager(this);
//        manager.getRandomRecipes(randomRecipeResponseListener);
//        dialog.show();
    }

    private final RandomRecipeResponseListener randomRecipeResponseListener = new RandomRecipeResponseListener() {
        @Override
        public void didFetch(RandomRecipeApiResponse response, String message) {
            dialog.dismiss();
            recyclerView = findViewById(R.id.recycler_random);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this,1));
            randomRecipeAdapter = new RandomRecipeAdapter(MainActivity.this, response.recipes,recipeClickListener);
            recyclerView.setAdapter(randomRecipeAdapter);
        }

        @Override
        public void didError(String message) {
            Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT);
        }
    };
    private final AdapterView.OnItemSelectedListener spinnerSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i , long l) {
            tags.clear();
            tags.add(adapterView.getSelectedItem().toString());
            manager.getRandomRecipes(randomRecipeResponseListener,tags);
            dialog.show();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };
    private final RecipeClickListener recipeClickListener = new RecipeClickListener() {
        @Override
        public void onRecipeClicked(String id) {
            //Toast.makeText(MainActivity.this,id, Toast.LENGTH_SHORT);
            startActivity(new Intent(MainActivity.this, RecipeDetailsActivity.class)
                    .putExtra("id",id)
            );

        }
    };
}