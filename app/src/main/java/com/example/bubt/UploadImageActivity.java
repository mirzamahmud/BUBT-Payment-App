package com.example.bubt;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;


import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class UploadImageActivity extends AppCompatActivity
{
    private CircleImageView profileImageView;
    private Button uploadImageButton;
    private Button saveImageButton;

    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;

    private Uri imageUri;
    private String myUri = "";
    private StorageTask uploadTask;
    private StorageReference storageProfileImageReference;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_image);

        getSupportActionBar().hide();
        getWindow().setStatusBarColor(ContextCompat.getColor(UploadImageActivity.this, R.color.colorPrimary));

        // initialize
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
        storageProfileImageReference = FirebaseStorage.getInstance().getReference().child("profile picture");

        profileImageView = (CircleImageView) findViewById(R.id.profile_image);
        uploadImageButton = (Button) findViewById(R.id.upload_image_button);
        saveImageButton = (Button) findViewById(R.id.save_image_button);

        uploadImageButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                CropImage.activity().setAspectRatio(1, 1).start(UploadImageActivity.this);
            }
        });

        saveImageButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                uploadProfileImage();

                Intent intent = new Intent(UploadImageActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });

        getUserInfo();
    }

    // get user info from firebase database
    private void getUserInfo()
    {
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK && data != null)
        {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imageUri = result.getUri();

            profileImageView.setImageURI(imageUri);
        }
        else
        {
            Toast.makeText(UploadImageActivity.this, "Error, Try Again", Toast.LENGTH_SHORT).show();
        }
    }

    // upload profile image
    private void uploadProfileImage()
    {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Set Your Profile");
        progressDialog.setMessage("Please wait, while setting your data");
        progressDialog.show();

        if(imageUri != null)
        {
            final StorageReference fileReference = storageProfileImageReference.child(firebaseAuth.getCurrentUser().getUid() + ".jpg");
            uploadTask = fileReference.putFile(imageUri);

            uploadTask.continueWithTask(new Continuation()
            {
                @Override
                public Object then(@NonNull Task task) throws Exception
                {
                    if(!task.isSuccessful())
                    {
                        throw task.getException();
                    }
                    return fileReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>()
            {
                @Override
                public void onComplete(@NonNull Task<Uri> task)
                {
                    // image upload complete to database
                    if(task.isSuccessful())
                    {
                        Uri downloadUrl = task.getResult();
                        myUri = downloadUrl.toString();

                        HashMap<String, Object> userMap = new HashMap<>();
                        userMap.put("image", myUri);

                        databaseReference.child(firebaseAuth.getCurrentUser().getUid()).updateChildren(userMap);

                        progressDialog.dismiss();
                    }
                }
            });
        }
        else
        {
            progressDialog.dismiss();
            Toast.makeText(UploadImageActivity.this, "Image not Selected", Toast.LENGTH_SHORT).show();
        }
    }

}