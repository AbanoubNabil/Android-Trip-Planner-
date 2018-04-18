package com.example.m1.tripproj.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.m1.tripproj.DataBase.DatabaseAdapter;
import com.example.m1.tripproj.HelperClasses.SessionManager;
import com.example.m1.tripproj.Models.User;
import com.example.m1.tripproj.R;
import com.example.m1.tripproj.customfonts.MyEditText;
import com.rey.material.widget.Switch;

public class Profile extends AppCompatActivity
{
    public static final int PICK_IMAGE = 1;
    ImageView proImageView;
    ImageView wallImageView;
    Bitmap bitmap;
    SharedPreferences sharedPref;
    MyEditText name, mail, pass;
    Switch editSwitcher;
    Button save;
    private DatabaseAdapter databaseAdapter;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        proImageView = (ImageView)findViewById(R.id.profileimg);
        wallImageView = (ImageView)findViewById(R.id.wallimg);

        proImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            getChooser(1);
            }
        });
        proImageView.setOnHoverListener(new View.OnHoverListener() {
            @Override
            public boolean onHover(View v, MotionEvent event) {

                return false;
            }
        });
        wallImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            getChooser(2);
            }
        });

        sharedPref = this.getSharedPreferences(SessionManager.PREF_NAME, SessionManager.PRIVATE_MODE);
        name = (MyEditText) findViewById(R.id.nameEdt);
        mail = (MyEditText) findViewById(R.id.emailEdt);
        pass = (MyEditText) findViewById(R.id.passwordEdt);
        name.setText(sharedPref.getString(SessionManager.KEY_NAME, "user name"));
        mail.setText(sharedPref.getString(SessionManager.KEY_EMAIL, "user mail"));
        pass.setText(sharedPref.getString(SessionManager.KEY_PASS, "user pass"));

        editSwitcher = (Switch) findViewById(R.id.switcher);
        save = (Button) findViewById(R.id.saveBtn);
        name.setEnabled(false);
        mail.setEnabled(false);
        pass.setEnabled(false);
        save.setEnabled(false);
        editSwitcher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!editSwitcher.isChecked()) {
                    name.setEnabled(false);
                    mail.setEnabled(false);
                    pass.setEnabled(false);
                    save.setEnabled(false);
                }
                else {
                    name.setEnabled(true);
                    mail.setEnabled(true);
                    pass.setEnabled(true);
                    save.setEnabled(true);
                }
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseAdapter = new DatabaseAdapter(Profile.this);
                user = new User();
                user.setId(sharedPref.getInt(SessionManager.KEY_ID, 0));
                user.setName(name.getText().toString());
                user.setEmail(mail.getText().toString());
                user.setPassword(pass.getText().toString());
                databaseAdapter.updateUserData(user);
                Log.i("userId: ", user.getId() + "");
                Log.i("userName: ", user.getName());
                Log.i("userMail: ", user.getEmail());
                Toast.makeText(Profile.this, "Data Saved Successfully", Toast.LENGTH_LONG).show();
                save.setEnabled(false);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == 1 || requestCode == 2) && (resultCode == RESULT_OK ) && (data != null)) {

            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            bitmap = BitmapFactory.decodeFile(picturePath);
            proImageView.setImageBitmap(bitmap);
            Log.i("test", "in the end of the if : " );
        } else {
            Log.i("test", "resultCode: " + resultCode);
            switch (resultCode) {
                case 0:
                    Log.i("test", "User cancelled");
                    break;
                case -1:
//                    onPhotoTaken();
                    Log.i("test", "User -1");
                    break;
            }
        }
    }

    public void getChooser( int code){
        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getIntent.setType("image/*");
        Intent pickIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");
        Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});
        startActivityForResult(chooserIntent, code);
    }
}
