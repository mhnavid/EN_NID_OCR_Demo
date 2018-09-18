package nvd.hasan.ennidocrdemo;

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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OldNidProcess extends AppCompatActivity {

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
            String resultText;
            @Override
            public void onPictureTaken(byte[] jpeg) {
                Intent in = new Intent(OldNidProcess.this, ResultShow.class);
                bmp = ByteArrayToBitmap(jpeg);
                image = FirebaseVisionImage.fromBitmap(bmp);
                textRecognizer = FirebaseVision.getInstance().getOnDeviceTextRecognizer();
                textRecognizer.processImage(image).addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
                    Intent in = new Intent(OldNidProcess.this, ResultShow.class);
                            @Override
                            public void onSuccess(FirebaseVisionText result) {
                                resultText = result.getText();
//                                Log.d("resultNID", resultText);
                                try {
                                    String[] partOne = resultText.split(Pattern.quote("Name"));
                                    String[] name = partOne[1].split(Pattern.quote("\n"));

                                    String[] partTwo = resultText.split(Pattern.quote("Birth"));
                                    String[] dob = partTwo[1].split(Pattern.quote("\n"));
                                    String nid;

                                    in.putExtra("name", String.valueOf(name[0]+" "+name[1]));
                                    Log.d("nameNID", String.valueOf(name[0]+" "+name[1]));
                                    in.putExtra("dob", dob[0]);
                                    Log.d("dobNID", dob[0]);

                                    Pattern patternNew = Pattern.compile("(([0-9][0-9][0-9])\\s([0-9][0-9][0-9])\\s([0-9][0-9][0-9][0-9]))");
                                    Pattern patternOld = Pattern.compile("([0-9]{13})");
                                    Matcher matcherNew = patternNew.matcher(resultText);
                                    Matcher matcherOld = patternOld.matcher(resultText);
                                    if (matcherNew.find())
                                    {
//                                        System.out.println(matcher.group(1));

                                        nid = matcherNew.group(1);
                                        Log.d("NID", nid);
//                                        Log.d("nid", nid);
                                        in.putExtra("nid", nid);
                                    }
                                    else if (matcherOld.find()){
                                        nid = matcherOld.group(1);
                                        in.putExtra("nid", nid);
                                    }

//                                    Log.d("resultText", resultText);
//                                    Log.d("dob", dob[0]);

                                    startActivity(in);
                                }
                                catch (Exception e){
                                    in.putExtra("error", "Please try again.");
                                    startActivity(in);
                                }
                            }
                        })
                        .addOnFailureListener(
                                new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                    }
                                });
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
