package nvd.hasan.ennidocrdemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

public class ResultShow extends AppCompatActivity {

    Button backBtn;
    TextView resultText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_show);

        backBtn = findViewById(R.id.backBtn);
        resultText = findViewById(R.id.resultText);

        String result = "Name: " + getIntent().getStringExtra("result");
        String error = getIntent().getStringExtra("error");
        if (error != null){
            resultText.setText(error);
        }else {
            resultText.setText(result);
        }

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(ResultShow.this, CameraShow.class);
                startActivity(in);
            }
        });
    }

    @Override
    public void onBackPressed() {
    }
}
