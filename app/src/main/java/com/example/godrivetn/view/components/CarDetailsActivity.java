
package com.example.godrivetn.view.components;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.godrivetn.R;
import com.example.godrivetn.model.Car;
import com.example.godrivetn.model.Owner;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.shape.ShapeAppearanceModel;

import de.hdodenhof.circleimageview.CircleImageView;

public class CarDetailsActivity extends AppCompatActivity {

    // UI Components
    private TextView tvCarName, tvTransmission, tvFuel,tvBrand,tvAvailability,tvNbPlace,tvPricePerDay, tvLocation;
    private ImageView imgCarImage;
    private RatingBar ratingBar;
    private Button rentButton;

    // Owner UI Components
    private TextView tvOwnerName, tvOwnerEmail;
    private CircleImageView imgOwnerProfile;
    private ImageButton btnSendSms, btnCallPhone;

    // Car and Owner objects
    private Car car;
    private Owner owner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_car_details);


        initializeViews();
        retrieveCarDetails();
        setupWindowInsets();
        setupClickListeners();

        rentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(CarDetailsActivity.this, Rev1Activity.class);
                i.putExtra("carName", car.getName());
                i.putExtra("carImage", car.getImageResource());
                i.putExtra("carPrice" , car.getPricePerDay());
                startActivity(i);


            }
        });
    }

    private void initializeViews() {
        // Car Details Views
        tvCarName = findViewById(R.id.tvCarName);
        tvTransmission = findViewById(R.id.tvTransmission);
        tvPricePerDay = findViewById(R.id.tvPricePerDay);
        tvFuel = findViewById(R.id.tvFuel);
        tvBrand = findViewById(R.id.tvBrand);
        tvNbPlace = findViewById(R.id.tvNbPlace);
        tvAvailability = findViewById(R.id.tvAvailability);
        rentButton =findViewById(R.id.rentButton);

        //tvLocation = findViewById(R.id.tvLocation);
        imgCarImage = findViewById(R.id.imgCarImage);
        ratingBar = findViewById(R.id.ratingBar);

        // Owner Details Views
        tvOwnerName = findViewById(R.id.tvOwnerName);
        tvOwnerEmail = findViewById(R.id.tvOwnerEmail);
        imgOwnerProfile = findViewById(R.id.imgOwnerProfile);
        btnSendSms = findViewById(R.id.btnSendSms);
        btnCallPhone = findViewById(R.id.btnCallPhone);
    }


    private void retrieveCarDetails() {
        // Retrieve the Car object passed from the previous activity
        if (getIntent().hasExtra("CAR_DETAILS")) {
            car = (Car) getIntent().getSerializableExtra("CAR_DETAILS");

            // Populate Car Details
            if (car != null) {
                // Set car details
                tvCarName.setText(car.getName());
                tvTransmission.setText(car.getTransmission());
                tvPricePerDay.setText(String.format("$%d/day", car.getPricePerDay()));


                tvFuel.setText(car.getFuel());

                tvBrand.setText(car.getBrand());

                tvNbPlace.setText(String.format("%d seats", car.getNbPlace()));

                tvAvailability.setText(car.getAvailability());


                ratingBar.setRating((float) car.getRating());

                int resourceId = getResources().getIdentifier(car.getImageResource(), "drawable", getPackageName());

                Glide.with(this)
                        .load(resourceId)
                        .placeholder(R.drawable.mesarati_car)
                        .into(imgCarImage);



                owner = car.getOwner();
                if (owner != null) {
                    tvOwnerName.setText(owner.getName());
                    tvOwnerEmail.setText(owner.getEmail());

                    // Load owner profile image
                    Glide.with(this)
                            .load(owner.getImageUrl())
                            .placeholder(R.drawable.profile)
                            .into(imgOwnerProfile);
                } else {

                    tvOwnerName.setText("Unknown Owner");
                    tvOwnerEmail.setText("No contact information");
                }
            }
        }
    }


    /*
    private void retrieveCarDetails() {
        // Retrieve the Car object passed from the previous activity
        if (getIntent().hasExtra("CAR_DETAILS")) {
            car = (Car) getIntent().getSerializableExtra("CAR_DETAILS");

            // Populate Car Details
            if (car != null) {
                // Set car details
                tvCarName.setText(car.getName());
                tvTransmission.setText(car.getTransmission());
                tvPricePerDay.setText(String.format("$%d/day", car.getPricePerDay()));

                // Set location if available
                //if (car.getLocation() != null) {
                //    tvLocation.setText(car.getLocation());
                //} else {
                 //   tvLocation.setText("Location not specified");
                //}



                ratingBar.setRating((float) car.getRating());

                // Resolve drawable resource ID
                int resourceId = getResources().getIdentifier(car.getImageResource(), "drawable", getPackageName());

                Glide.with(this)
                        .load(resourceId) // Load the resolved resource ID
                        .placeholder(R.drawable.mesarati_car)
                        .into(imgCarImage);



                owner = car.getOwner();
                if (owner != null) {
                    tvOwnerName.setText(owner.getName());
                    tvOwnerEmail.setText(owner.getEmail());

                    // Load owner profile image
                    Glide.with(this)
                            .load(owner.getImageUrl())
                            .placeholder(R.drawable.profile)
                            .into(imgOwnerProfile);
                } else {
                    // Handle case where owner is null
                    tvOwnerName.setText("Unknown Owner");
                    tvOwnerEmail.setText("No contact information");
                }
            }
        }
    }
    */



    private void setupWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.carDetails), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void setupClickListeners() {
        // Back button
        findViewById(R.id.btnBack).setOnClickListener(v -> onBackPressed());

        // Favorite button (you can implement favorite logic here)
        findViewById(R.id.btnFavorite).setOnClickListener(v -> {
            // Toggle favorite status
            car.setFavorite(!car.isFavorite());
        });

        // SMS and Phone buttons
        if (owner != null) {
            btnSendSms.setOnClickListener(v -> {
                Intent smsIntent = new Intent(Intent.ACTION_SENDTO,
                        Uri.parse("smsto:" + owner.getPhoneNumber()));
                startActivity(smsIntent);
            });

            btnCallPhone.setOnClickListener(v -> {
                Intent phoneIntent = new Intent(Intent.ACTION_DIAL,
                        Uri.parse("tel:" + owner.getPhoneNumber()));
                startActivity(phoneIntent);
            });
        }
    }
}