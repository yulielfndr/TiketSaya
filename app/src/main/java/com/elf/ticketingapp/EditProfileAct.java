package com.elf.ticketingapp;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class EditProfileAct extends AppCompatActivity {

    TextView et_nama_edit_profile, et_bio_edit_profile, et_username_edit_profile, et_password_edit_profile, et_email_edit_profile;
    ImageView pic_photo_register;
    Button btn_add_photo, btn_save_profile;
    LinearLayout btn_back;

    DatabaseReference reference;
    StorageReference storage;

    Uri photo_location;
    Integer photo_max = 1;

    String USERNAME_KEY = "usernamekey";
    String username_key = "";
    String username_key_new = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        pic_photo_register =findViewById(R.id.pic_photo_register);
        et_nama_edit_profile =findViewById(R.id.et_nama_edit_profile);
        et_bio_edit_profile =findViewById(R.id.et_bio_edit_profile);
        et_username_edit_profile =findViewById(R.id.et_username_edit_profile);
        et_password_edit_profile =findViewById(R.id.et_password_edit_profile);
        et_email_edit_profile =findViewById(R.id.et_email_edit_profile);
        btn_add_photo =findViewById(R.id.btn_add_photo);
        btn_back =findViewById(R.id.btn_back);
        btn_save_profile =findViewById(R.id.btn_save_profile);


        getUsernameLocal();

        reference = FirebaseDatabase.getInstance().getReference().child("Users")
                .child(username_key_new);

        storage = FirebaseStorage.getInstance().getReference().child("Photousers").child(username_key_new);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                et_nama_edit_profile.setText(dataSnapshot.child("full_name").getValue().toString());
                et_bio_edit_profile.setText(dataSnapshot.child("bio").getValue().toString());
                et_username_edit_profile.setText(dataSnapshot.child("username").getValue().toString());
                et_email_edit_profile.setText(dataSnapshot.child("email_address").getValue().toString());
                et_password_edit_profile.setText(dataSnapshot.child("password").getValue().toString());

                Picasso.with(EditProfileAct.this).load(dataSnapshot.child("url_photo_profile")
                        .getValue().toString()).centerCrop().fit().into(pic_photo_register);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btn_save_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_save_profile.setEnabled(false);
                btn_save_profile.setText("Loading...");
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        dataSnapshot.getRef().child("full_name").setValue(et_nama_edit_profile.getText().toString());
                        dataSnapshot.getRef().child("bio").setValue(et_bio_edit_profile.getText().toString());
                        dataSnapshot.getRef().child("username").setValue(et_username_edit_profile.getText().toString());
                        dataSnapshot.getRef().child("email_address").setValue(et_email_edit_profile.getText().toString());
                        dataSnapshot.getRef().child("password").setValue(et_password_edit_profile.getText().toString());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                if (photo_location != null) {
                    StorageReference storageReference1 = storage.child(System.currentTimeMillis() +
                            "." + getFileExtension(photo_location));
                    storageReference1.putFile(photo_location)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    Task<Uri> task = taskSnapshot.getMetadata().getReference().getDownloadUrl();
                                    task.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            String uri_photo = uri.toString();
                                            reference.getRef().child("url_photo_profile").setValue(uri_photo);
                                        }
                                    });
                                }
                            }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            Intent cont = new Intent(EditProfileAct.this, ProfileAct.class);
                            startActivity(cont);
                            //finish();
                        }
                    });
                }
            }
        });

        btn_add_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findPhoto();
            }
        });

        }

    String getFileExtension(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    public void findPhoto(){
        Intent pic = new Intent();
        pic.setType("image/*");
        pic.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(pic, photo_max);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == photo_max && resultCode == RESULT_OK && data != null && data.getData() != null)
            ;
        {

            photo_location = data.getData();
            Picasso.with(this).load(photo_location).centerCrop().fit().into(pic_photo_register);

        }
    }

    public void getUsernameLocal(){
        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
        username_key_new = sharedPreferences.getString(username_key, "");
    }
}
