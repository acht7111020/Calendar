package com.example.jeanlee.calendar;


import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.graphics.Color;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class AlbumActivity extends ActionBarActivity {

 /*   static final int REQUEST_IMAGE_CAPTURE = 1;
    private ImageView mImageView;*/

    /**request Code 从相册选择照片并裁切**/
    private final static int SELECT_PIC=123;
    /**request Code 从相册选择照片不裁切**/
    private final static int SELECT_ORIGINAL_PIC=126;
    /**request Code 拍取照片并裁切**/
    private final static int TAKE_PIC=124;
    /**request Code 拍取照片不裁切**/
    private final static int TAKE_ORIGINAL_PIC=127;
    /**request Code 裁切照片**/
    private final static int CROP_PIC=125;
    private Uri imageUri;
    private ImageView imgShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_albm);
        //set background
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.pink));

        imageUri=Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "test.jpg"));
        imgShow=(ImageView)findViewById(R.id.imgShow);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_album, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
       /* switch (item.getItemId()) {

            case R.id.take_photo:
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE);
                // Hide the "empty" view since there is now at least one item in the list.
                return true;
        }*/
        switch (item.getItemId()) {
            case R.id.btnCropFromGallery://从相册选择照片进行裁剪
                cropFromGallery();
                break;
            case R.id.take_photo://从相机拍取照片进行裁剪
                cropFromTake();
                break;
           /* case R.id.btnOriginal://从相册选择照片不裁切
                selectFromGallery();
                break;
            case R.id.btnTakeOriginal://从相机拍取照片不裁剪
                selectFromTake();
                break;*/
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        switch (requestCode) {
            case SELECT_PIC:
                if (resultCode==RESULT_OK) {//从相册选择照片并裁切
                    try {
                        Bitmap bitmap=BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));//将imageUri对象的图片加载到内存
                        ByteArrayOutputStream out = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                      //  byte bytes[] = out.toByteArray();
                        ContentValues cv = new ContentValues();
                        cv.put("picture",out.toByteArray());

                        imgShow.setImageBitmap(bitmap);
                        bitmap.recycle();
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                break;
            case SELECT_ORIGINAL_PIC:
                if (resultCode==RESULT_OK) {//从相册选择照片不裁切
                    try {
                        Uri selectedImage = data.getData(); //获取系统返回的照片的Uri
                        String[] filePathColumn = { MediaStore.Images.Media.DATA };
                        Cursor cursor = getContentResolver().query(selectedImage,
                                filePathColumn, null, null, null);//从系统表中查询指定Uri对应的照片
                        cursor.moveToFirst();
                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        String picturePath = cursor.getString(columnIndex);  //获取照片路径
                        cursor.close();
                        Bitmap bitmap= BitmapFactory.decodeFile(picturePath);
                        imgShow.setImageBitmap(bitmap);
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                break;
            case TAKE_PIC://拍取照片,并裁切
                if (resultCode==RESULT_OK) {
                    cropImageUri(imageUri, 600, 600, CROP_PIC);
                }
            case TAKE_ORIGINAL_PIC://拍取照片
                if (resultCode==RESULT_OK) {
                    String imgPath=imageUri.getPath();//获取拍摄照片路径
                }
                break;
            case CROP_PIC://拍取照片
                if (resultCode==RESULT_OK) {
                    try {
                        Bitmap bitmap=BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));//将imageUri对象的图片加载到内存
                        imgShow.setImageBitmap(bitmap);
                    } catch (FileNotFoundException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    /**
     * 裁剪指定uri对应的照片
     * @param imageUri：uri对应的照片
     * @param outputX：裁剪宽
     * @param outputY：裁剪高
     * @param requestCode：请求码
     */
    private void cropImageUri(Uri imageUri, int outputX, int outputY, int requestCode){
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(imageUri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", outputX);
        intent.putExtra("outputY", outputY);
        intent.putExtra("scale", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        intent.putExtra("return-data", false);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true); // no face detection
        startActivityForResult(intent, requestCode);
    }


    /**
     * 从相册选择原生的照片（不裁切）
     */
    private void selectFromGallery() {
        // TODO Auto-generated method stub
        Intent intent=new Intent();
        intent.setAction(Intent.ACTION_PICK);//Pick an item from the data
        intent.setType("image/*");//从所有图片中进行选择
        startActivityForResult(intent, SELECT_ORIGINAL_PIC);
    }
    /**
     * 从相册选择照片进行裁剪
     */
    private void cropFromGallery() {
        // TODO Auto-generated method stub
        Intent intent=new Intent();
        intent.setAction(Intent.ACTION_PICK);//Pick an item from the data
        intent.setType("image/*");//从所有图片中进行选择
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
        startActivityForResult(intent, SELECT_PIC);
    }
    /**
     * 拍取照片不裁切
     */
    private void selectFromTake() {
        // TODO Auto-generated method stub
        Intent intent=new Intent();
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);//设置Action为拍照
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);//将拍取的照片保存到指定URI
        startActivityForResult(intent, TAKE_ORIGINAL_PIC);
    }
    /**
     * 从相机拍取照片进行裁剪
     */
    private void cropFromTake() {
        // TODO Auto-generated method stub
        Intent intent=new Intent();
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);//设置Action为拍照
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);//将拍取的照片保存到指定URI
        startActivityForResult(intent, TAKE_PIC);
    }
 /*   private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }


    String mCurrentPhotoPath;*/
    /**
     * a method that returns a unique file name for a new photo using a date-time stamp:
     */
 /*   private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  // prefix
                ".jpg",         //suffix
                storageDir     // directory
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }*/

/*    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }*/



 /*   private void setPic() {
        // Get the dimensions of the View
        int targetW = mImageView.getWidth();
        int targetH = mImageView.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        mImageView.setImageBitmap(bitmap);
    }*/


  /*  @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            mImageView.setImageBitmap(imageBitmap);
        }
    }*/

}
