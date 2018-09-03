package nvd.hasan.ennidocrdemo;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.common.FirebaseVisionImageMetadata;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;
import com.otaliastudios.cameraview.CameraListener;
import com.otaliastudios.cameraview.CameraView;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;

public class CameraShow extends AppCompatActivity {

    private Button captureBtn;
    private CameraView cameraView;

    private Bitmap bmp;
    private FirebaseVisionImage image;
    private FirebaseVisionImageMetadata metadata;
    private FirebaseVisionTextRecognizer textRecognizer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_show);

        captureBtn = findViewById(R.id.captureBtn);
        cameraView = findViewById(R.id.camera);

        cameraView.addCameraListener(new CameraListener() {
            @Override
            public void onPictureTaken(byte[] jpeg) {
                Log.d("arr", String.valueOf(jpeg.length));
                bmp = ByteArrayToBitmap(jpeg);
                image = FirebaseVisionImage.fromBitmap(bmp);
//                metadata = new FirebaseVisionImageMetadata.Builder()
//                        .setWidth(jpeg.length)
//                        .setHeight(bmp.getHeight())
//                        .setFormat(FirebaseVisionImageMetadata.IMAGE_FORMAT_NV21)
//                        .setRotation(FirebaseVisionImageMetadata.ROTATION_90)
//                        .build();
//                image = FirebaseVisionImage.fromByteBuffer(ByteBuffer.wrap(jpeg), metadata);
                textRecognizer = FirebaseVision.getInstance().getOnDeviceTextRecognizer();
                textRecognizer.processImage(image).addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
                            @Override
                            public void onSuccess(FirebaseVisionText result) {
                                String resultText = result.getText();
                                Log.d("arr", String.valueOf(bmp.getWidth()+","+bmp.getHeight()));
                                Intent in = new Intent(CameraShow.this, ResultShow.class);
                                in.putExtra("result", resultText);
                                startActivity(in);
                            }
                        })
                        .addOnFailureListener(
                                new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                    }
                                });



//                Intent in = new Intent(cameraShow.this, CanvasDraw.class);
//                in.putExtra("path", path.toString());
//                startActivity(in);
            }
        });

        captureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraView.capturePicture();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        cameraView.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        cameraView.stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cameraView.destroy();
    }

    public Bitmap ByteArrayToBitmap(byte[] byteArray)
    {
        ByteArrayInputStream arrayInputStream = new ByteArrayInputStream(byteArray);
        Bitmap bitmap = BitmapFactory.decodeStream(arrayInputStream);
        return bitmap;
    }

//    public File savebitmap(Bitmap bmp) {
//        ContextWrapper cw = new ContextWrapper(getApplicationContext());
//        File directory = cw.getDir("ImageCrop", Context.MODE_PRIVATE);
//        if (!directory.exists()) {
//            directory.mkdir();
//        }
//        File mypath = new File(directory, "thumbnail.jpg");
//
//        FileOutputStream fos = null;
//        try {
//            fos = new FileOutputStream(mypath);
//            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
//            fos.close();
//        } catch (Exception e) {
//            Log.e("SAVE_IMAGE", e.getMessage(), e);
//        }
//        return  mypath;
//    }
}
