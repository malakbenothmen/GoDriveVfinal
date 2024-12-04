package com.example.godrivetn.view.pages;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.godrivetn.R;
import com.example.godrivetn.controller.AdapterCar;
import com.example.godrivetn.controller.BrandAdapter;
import com.example.godrivetn.model.Brand;
import com.example.godrivetn.model.Car;
import com.example.godrivetn.view.components.CarDetailsActivity;
import com.example.godrivetn.view.components.FilterActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private RecyclerView recyclerView1;
    private RecyclerView recyclerView2;
    private AdapterCar carAdapter;
    private List<Car> carList;
    private RecyclerView recyclerViewBrand;
    private BrandAdapter brandAdapter;
    private List<Brand> brandList;
    private DatabaseReference databaseReference;
    private DatabaseReference databaseReferenceCar;
    private DatabaseReference databaseReferenceBrand;

    private View GoToHome;
    private  View GoToProfile;
    private View GoToReservation;
    private ImageButton filterbtn;




    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.home), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReferenceBrand = databaseReference.child("brands");
        databaseReferenceCar = databaseReference.child("cars");


        recyclerViewBrand= findViewById(R.id.brandRecyclerView);
        recyclerView1= findViewById(R.id.recycleNearCar);
        recyclerView2 = findViewById(R.id.recyclePopularCar);
        recyclerViewBrand.setNestedScrollingEnabled(false);
        recyclerView1.setNestedScrollingEnabled(false);
        recyclerView2.setNestedScrollingEnabled(false);


        GoToHome = findViewById(R.id.nav_home);
        GoToProfile =  findViewById(R.id.nav_profile);
        GoToReservation = findViewById(R.id.nav_reservation);

        filterbtn = findViewById(R.id.filterbtn);

        filterbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this , FilterActivity.class);
                startActivity(intent);
            }
        });
        GoToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });

        GoToProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });

        GoToReservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, RevListActivity.class);
                startActivity(intent);
            }
        });


        brandList =new ArrayList<>();


        recyclerViewBrand.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));



        // initializeBrands();
        brandAdapter = new BrandAdapter(brandList);
        recyclerViewBrand.setAdapter(brandAdapter);


        // Charger les données depuis Firebase
        loadBrandsFromFirebase();
        // Charger les voitures depuis Firebase
        loadCarsFromFirebase();



        // Initialize car list
        carList = new ArrayList<>();

        carAdapter = new AdapterCar(this, carList, new AdapterCar.OnCarClickListener() {
            @Override
            public void onCarClick(Car car) {
                // Create an intent to navigate to CarDetailsActivity
                Intent intent = new Intent(HomeActivity.this, CarDetailsActivity.class);

                // Pass the car details to the CarDetailsActivity
                intent.putExtra("CAR_DETAILS", car);

                // Start the CarDetailsActivity
                startActivity(intent);
            }
        });

        recyclerView1.setAdapter(carAdapter);
        recyclerView2.setAdapter(carAdapter);

        recyclerView1.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerView2.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));



    }



    private void loadBrandsFromFirebase() {
        databaseReferenceBrand.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                brandList.clear();
                for (DataSnapshot brandSnapshot : snapshot.getChildren()) {
                    Brand brand = brandSnapshot.getValue(Brand.class);
                    if (brand != null) {
                        brandList.add(brand);
                    }
                }
                brandAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(HomeActivity.this, "Erreur de chargement: " + error.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void loadCarsFromFirebase() {
        databaseReferenceCar.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                carList.clear();
                for (DataSnapshot carSnapshot : snapshot.getChildren()) {
                    Car car = carSnapshot.getValue(Car.class);
                    if (car != null) {
                        carList.add(car);
                    }
                }
                carAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(HomeActivity.this, "Erreur de chargement: " +
                        error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}