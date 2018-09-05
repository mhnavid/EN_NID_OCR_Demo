package nvd.hasan.ennidocrdemo;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
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
import java.util.regex.Pattern;

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
            int count = 0;
            String elementText, lineText, resultText;
            @Override
            public void onPictureTaken(byte[] jpeg) {
                Log.d("arr", String.valueOf(jpeg.length));
                Intent in = new Intent(CameraShow.this, ResultShow.class);
                bmp = ByteArrayToBitmap(jpeg);
                image = FirebaseVisionImage.fromBitmap(bmp);
                textRecognizer = FirebaseVision.getInstance().getOnDeviceTextRecognizer();
                textRecognizer.processImage(image).addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
                    Intent in = new Intent(CameraShow.this, ResultShow.class);
                            @Override
                            public void onSuccess(FirebaseVisionText result) {
                                resultText = result.getText();
                                count++;
//                                for (FirebaseVisionText.TextBlock block: result.getTextBlocks()) {
//                                    if (block.getText() != null){
//                                        blockText[count] = block.getText();
//                                        count++;
//                                    }
//                                    Log.d("blockText", blockText[5] + blockText[6]);
//                                    for (FirebaseVisionText.Line line: block.getLines()) {
//                                        lineText = line.getText();
//                                        Log.d("lineText", lineText);
//                                        for (FirebaseVisionText.Element element: line.getElements()) {
//                                            elementText = element.getText();
//                                            Log.d("elementText", elementText);
//                                        }
//                                    }
//                                }
                                try {
                                    String[] parts = resultText.split(Pattern.quote("Name"));
                                    String[] name = parts[1].split(Pattern.quote("\n"));

                                    Log.d("arr", String.valueOf(bmp.getWidth()+","+bmp.getHeight()));
                                    Log.d("resultText", String.valueOf(name[0]));
                                    in.putExtra("result", name[0]+" "+name[1]);
                                }
                                catch (Exception e){
                                    in.putExtra("error", "Please try again.");
                                }
                            }
                        })
                        .addOnFailureListener(
                                new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                    }
                                });
                startActivity(in);
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
