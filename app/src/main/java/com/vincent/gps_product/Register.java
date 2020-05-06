package com.vincent.gps_product;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.recyclerview.extensions.AsyncListDiffer;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Register extends AppCompatActivity {

    private Button btnImg;
    private EditText myId,myPw;
    private TextView txtWarn,txtWarn2,call;
    private Spinner spin;
    private ImageView myImg;
    private ImageButton Login;

    private Uri imageUri;
    private String imagePath;
    private String TempName,TempPassword,TempCheckImg,TempSex,name;
    private int x,checkImg = 0;
    private int check_once = 0;

    public static final int CHOOSE_PHOTO = 2;

    BackgroundTask backgroundTask;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        findView();
    }
    protected void onResume(){
        super.onResume();


        try{
            SharedPreferences info = getApplicationContext().getSharedPreferences("Info",0);
            String check_login = info.getString("Name","0");
            if (check_login.equals("0")){

            }else{
                check_once = 1;
                startActivity(new Intent(this,MapsActivity.class));
                finish();
            }
        }catch (Exception e){}


    }

    private void findView(){
        storageReference = FirebaseStorage.getInstance().getReference("image");
        Login = findViewById(R.id.Login);
        Login.setOnClickListener(loginListener);
        myImg = findViewById(R.id.myImg);
        spin = findViewById(R.id.spin);
        call = findViewById(R.id.call);
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it =new Intent(Intent.ACTION_VIEW, Uri.parse("tel:0922992514"));
                startActivity(it);
            }
        });
        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (checkImg == 0) {
                    if (position == 1){
                        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.boy);
                        RoundedBitmapDrawable circleDrawable = RoundedBitmapDrawableFactory.create(getResources(), BitmapFactory.decodeResource(getResources(), R.mipmap.boy));
                        circleDrawable.getPaint().setAntiAlias(true);
                        circleDrawable.setCornerRadius(Math.max(bitmap.getWidth(), bitmap.getHeight()));
                        myImg.setImageDrawable(circleDrawable);
                    } else if (position == 2){
                        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.girl);
                        RoundedBitmapDrawable circleDrawable = RoundedBitmapDrawableFactory.create(getResources(), BitmapFactory.decodeResource(getResources(), R.mipmap.girl));
                        circleDrawable.getPaint().setAntiAlias(true);
                        circleDrawable.setCornerRadius(Math.max(bitmap.getWidth(), bitmap.getHeight()));
                        myImg.setImageDrawable(circleDrawable);
                    }
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });

        txtWarn = findViewById(R.id.txtWarn);
        txtWarn2 = findViewById(R.id.txtWarn2);
        btnImg = findViewById(R.id.btnImg);
        btnImg.setOnClickListener(imgListen);
        myId = findViewById(R.id.myId);
        myPw = findViewById(R.id.myPw);

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.boy);
        RoundedBitmapDrawable circleDrawable = RoundedBitmapDrawableFactory.create(getResources(), BitmapFactory.decodeResource(getResources(), R.mipmap.boy));
        circleDrawable.getPaint().setAntiAlias(true);
        circleDrawable.setCornerRadius(Math.max(bitmap.getWidth(), bitmap.getHeight()));
        myImg.setImageDrawable(circleDrawable);
    }

    public void btnLogin(View view){
        Intent it  = new Intent(getApplicationContext(),Login.class);
        startActivity(it);
        finish();
    }

   private ImageButton.OnClickListener loginListener = new ImageButton.OnClickListener(){
       public void onClick(View v) {

           x = spin.getSelectedItemPosition();
           TempName = myId.getText().toString();
           TempPassword = myPw.getText().toString();
           TempCheckImg = Integer.toString(checkImg);
           TempSex = Integer.toString(x);

           if (TempName.equals("") && TempPassword.equals("")){
               txtWarn.setText("");
               txtWarn2.setText("");
               txtWarn.setText("暱稱不能為空");
               txtWarn2.setText("密碼不能為空");
           } else if(TempName.length() > 6 && TempPassword.length() < 6){
               txtWarn.setText("");
               txtWarn2.setText("");
               txtWarn.setText("暱稱不能超過7個字");
               txtWarn2.setText("密碼過短");
           }else if(TempPassword.length() > 20){
               txtWarn2.setText("密碼不能超過20個字");
           }else if(TempName.length() > 6){
               txtWarn.setText("");
               txtWarn2.setText("");
               txtWarn.setText("暱稱不能超過7個字");
           }
           else if(TempPassword.length() < 6){
               txtWarn.setText("");
               txtWarn2.setText("");
               txtWarn2.setText("密碼需至少要6個字");
           }else if(TempName.equals("")){
               txtWarn.setText("");
               txtWarn2.setText("");
               txtWarn.setText("暱稱不能為空");
           }else if(TempPassword.equals("")){
               txtWarn.setText("");
               txtWarn2.setText("");
               txtWarn.setText("密碼不能為空");
           }else if(x == 0)
               Toast.makeText(getApplicationContext(),"請選擇性別",Toast.LENGTH_SHORT).show();
           else{
               if (TempCheckImg.equals("1")){
                   upload(TempName);
               }else{
                   ProgressDialogUtil.showProgressDialog(Register.this);
                   String type = "register";

                   backgroundTask = new BackgroundTask(getApplicationContext(),Register.this,TempSex,TempCheckImg);
                   backgroundTask.execute(type,TempName,TempPassword,TempCheckImg,TempSex);
               }

           }
       }
    };

    Button.OnClickListener imgListen = new Button.OnClickListener(){
        @Override
        public void onClick(View v) {
            File outputImage  = new File(Environment.getExternalStorageDirectory(), "test.jpg");
            try {
                if (outputImage.exists()) {
                    outputImage.delete();
                }
                outputImage.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            imageUri = Uri.fromFile(outputImage);
            //動態申請對SD卡讀寫的許可權
            if (ContextCompat.checkSelfPermission(Register.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(Register.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            } else {
                openAlbum();
            }
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (permissions[0]) {
            case android.Manifest.permission.WRITE_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openAlbum();
                } else {
                    Toast.makeText(this, "請確認權限", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    private void openAlbum() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        intent.putExtra("crop", "true");//设置为裁切
        intent.putExtra("aspectX", 1);//裁切的宽比例
        intent.putExtra("aspectY", 1);//裁切的高比例
        intent.putExtra("outputX", 600);//裁切的宽度
        intent.putExtra("outputY", 600);//裁切的高度
        intent.putExtra("scale", true);//支持缩放
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);//将裁切的结果输出到指定的Uri
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());//裁切成的图片的格式
        intent.putExtra("noFaceDetection", true); // no face detection
        startActivityForResult(intent,CHOOSE_PHOTO);  //開啟相簿
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case RESULT_OK:
                if (requestCode == CHOOSE_PHOTO)
                    handleImageOnKitKat();
                break;
            case RESULT_CANCELED:
                x = spin.getSelectedItemPosition();
                if (x == 1) {
                    Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.boy);
                    RoundedBitmapDrawable circleDrawable = RoundedBitmapDrawableFactory.create(getResources(), BitmapFactory.decodeResource(getResources(), R.mipmap.boy));
                    circleDrawable.getPaint().setAntiAlias(true);
                    circleDrawable.setCornerRadius(Math.max(bitmap.getWidth(), bitmap.getHeight()));
                    myImg.setImageDrawable(circleDrawable);
                }
                else if (x == 2){
                    Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.girl);
                    RoundedBitmapDrawable circleDrawable = RoundedBitmapDrawableFactory.create(getResources(), BitmapFactory.decodeResource(getResources(), R.mipmap.boy));
                    circleDrawable.getPaint().setAntiAlias(true);
                    circleDrawable.setCornerRadius(Math.max(bitmap.getWidth(), bitmap.getHeight()));
                    myImg.setImageDrawable(circleDrawable);
                }
                checkImg = 0;
                break;
        }
    }

    private void handleImageOnKitKat() {   //處理圖片
        imagePath = null;
        if (DocumentsContract.isDocumentUri(this,imageUri)) {     //如果是document型別的Uri,則通過document id處理
            String docId = DocumentsContract.getDocumentId(imageUri);
            if ("com.android.providers.media.documents".equals(imageUri.getAuthority())) {
                String id = docId.split(":")[1];   //解析出數字格式的id
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.downloads.documents".equals(imageUri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(imageUri.getScheme())) {  //如果是cntent型別的Uri,則使用普通方式處理
            imagePath = getImagePath(imageUri, null);
        } else if ("file".equalsIgnoreCase(imageUri.getScheme())) {   //如果是file型別的Uri,直接獲取圖片路徑即可
            imagePath = imageUri.getPath();
        }
        displayImage(imagePath);
    }

    private String getImagePath(Uri uri, String selection) {   //通過Uri和selection來獲取真實的圖片路徑
        String path = null;
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    private void displayImage(String imagePath) {   //顯示圖片
        if (imagePath != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            RoundedBitmapDrawable circleDrawable = RoundedBitmapDrawableFactory.create(getResources(), BitmapFactory.decodeFile(imagePath));
            circleDrawable.getPaint().setAntiAlias(true);
            circleDrawable.setCornerRadius(Math.max(bitmap.getWidth(), bitmap.getHeight()));
            myImg.setImageDrawable(circleDrawable);
            SharedPreferences info = getSharedPreferences("Info",0);
            info.edit().putString("Path",imagePath).commit();

            checkImg = 1;
        } else {
            Toast.makeText(this, "找不到照片", Toast.LENGTH_SHORT).show();
        }
    }

    private void upload(String name){
        final StorageReference fileReference = storageReference.child(name);
        fileReference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>(){
                    @Override
                    public void onSuccess(Uri uri) {
                        String imageUri = uri.toString();
                        Log.i("TAK",imageUri);

                        TempCheckImg = Integer.toString(checkImg);
                        TempSex = Integer.toString(x);

                        SharedPreferences info = getSharedPreferences("Info",0);
                        info.edit().putString("Image",imageUri).commit();

                        String type = "register01";
                        backgroundTask = new BackgroundTask(getApplicationContext(),Register.this,TempSex,TempCheckImg);
                        backgroundTask.execute(type,TempName,TempPassword,TempCheckImg,TempSex,imageUri);
                    }
                });
            }
        })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        ProgressDialogUtil.showProgressDialog(Register.this);
                    }
                });
    }

}
