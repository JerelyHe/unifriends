package com.example.unifriends;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Signup3 extends AppCompatActivity {
    private static final String TAG = "Signup3";
    private Spinner degreeSpinner;
    private Spinner majorSpinner;

    private EditText nameEditText;
    private EditText uniEditText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup3);

        nameEditText = findViewById(R.id.editTextName);
        uniEditText = findViewById(R.id.editTextUniName);

        degreeSpinner = findViewById(R.id.degreeSpinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> degreeAdapter = ArrayAdapter.createFromResource(this,
                R.array.degree_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        degreeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        degreeSpinner.setAdapter(degreeAdapter);

        majorSpinner = findViewById(R.id.majorSpinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> majorAdapter = ArrayAdapter.createFromResource(this,
                R.array.major_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        majorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        majorSpinner.setAdapter(majorAdapter);
    }

    public void updateUserInfo(View view){
        findViewById(R.id.loadingPanel).setVisibility(View.VISIBLE);

        String degree = degreeSpinner.getSelectedItem().toString();
        String major = majorSpinner.getSelectedItem().toString();
        String name = nameEditText.getText().toString();
        String uni = uniEditText.getText().toString().toUpperCase();

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String id = mAuth.getUid();


        Log.d(TAG, id);

        Map<String, Object> update = new HashMap<>();
        update.put("degree", degree);
        update.put("major", major);
        update.put("name", name);
        update.put("uni", uni);
        update.put("photo", "usersImage/" + FirebaseAuth.getInstance().getUid() + ".jpg");
        update.put("facialID", getIntent().getStringExtra("facialID"));

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("users").document(id)
                .update(update)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                        findViewById(R.id.loadingPanel).setVisibility(View.GONE);

                        startActivity(intent);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        findViewById(R.id.loadingPanel).setVisibility(View.GONE);

                        Log.w(TAG, "Error writing document", e);
                    }
                });

    }

    public void onLoginClick(View view) {
        startActivity(new Intent(this, LoginActivity.class));
        overridePendingTransition(R.anim.slide_in_left, R.anim.stay);
    }


}
