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
                bmp = ByteArrayToBitmap(jpeg);
                image = FirebaseVisionImage.fromBitmap(bmp);
                textRecognizer = FirebaseVision.getInstance().getOnDeviceTextRecognizer();
                textRecognizer.processImage(image).addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
                    Intent in = new Intent(OldNidProcess.this, ResultShow.class);
                            @Override
                            public void onSuccess(FirebaseVisionText result) {
                                resultText = result.getText();
                                Log.d("resultNID", resultText);
                                try {
                                    String nid, dob;

                                    String[] partOne = resultText.split(Pattern.quote("Name:"));
                                    String[] name = partOne[1].split(Pattern.quote("\n"));
                                    in.putExtra("name", String.valueOf(name[0]));

//                                    String[] partTwo = resultText.split(Pattern.quote("Birth"));
//                                    String[] dob = partTwo[1].split(Pattern.quote("\n"));
                                    Pattern dobPatten = Pattern.compile("([0-9]{2}\\s(Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec)\\s[0-9]{4})");
                                    Matcher dobMatcher = dobPatten.matcher(resultText);

                                    if (dobMatcher.find()){
                                        dob = dobMatcher.group(1);
                                        in.putExtra("dob", dob);
                                    }

                                    Pattern patternOld = Pattern.compile("([0-9]{13})");
                                    Matcher matcherOld = patternOld.matcher(resultText);

                                    if (matcherOld.find()){
                                        nid = matcherOld.group(1);
                                        in.putExtra("nid", nid);
                                    }


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

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(OldNidProcess.this, MainActivity.class);
        startActivity(intent);
    }

    public Bitmap ByteArrayToBitmap(byte[] byteArray)
    {
        ByteArrayInputStream arrayInputStream = new ByteArrayInputStream(byteArray);
        Bitmap bitmap = BitmapFactory.decodeStream(arrayInputStream);
        return bitmap;
    }
}
