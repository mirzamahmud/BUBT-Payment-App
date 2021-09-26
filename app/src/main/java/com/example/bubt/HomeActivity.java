package com.example.bubt;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity extends AppCompatActivity
{

    private CircleImageView profileImageView;
    private CardView paymentCard;
    private CardView bubtAnnexCard;
    private CardView bubtCard;

    private DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        getSupportActionBar().hide();
        getWindow().setStatusBarColor(ContextCompat.getColor(HomeActivity.this, R.color.colorPrimary));

        // reference widgets
        profileImageView = (CircleImageView) findViewById(R.id.profile_image_logo);
        paymentCard = (CardView) findViewById(R.id.payment_card);
        bubtAnnexCard = (CardView) findViewById(R.id.bubt_annex_card);
        bubtCard = (CardView) findViewById(R.id.bubt_card);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");

        // add event with bubtCard
        bubtCard.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(HomeActivity.this, BubtActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        // add event with bubtAnnexCard
        bubtAnnexCard.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(HomeActivity.this, AnnexActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        // add event with paymentCard
        paymentCard.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(HomeActivity.this, PaymentActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

            }
        });

        // add event with profile imageview
        profileImageView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(HomeActivity.this, UploadImageActivity.class);
                startActivity(intent);
            }
        });

        getUserInfo();

    }

    private void getUserInfo() {

        databaseReference.child(firebaseAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0)
                {
                    if(dataSnapshot.hasChild("image"))
                    {
                        String image = dataSnapshot.child("image").getValue().toString();
                        Picasso.get().load(image).into(profileImageView);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {

            }
        });
    }
}