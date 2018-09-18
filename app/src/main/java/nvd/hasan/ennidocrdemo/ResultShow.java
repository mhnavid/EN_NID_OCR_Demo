package nvd.hasan.ennidocrdemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ResultShow extends AppCompatActivity {

    Button backBtn;
    TextView nameText, dobText, nidText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_show);

        backBtn = findViewById(R.id.backBtn);
        nameText = findViewById(R.id.nameText);
        dobText = findViewById(R.id.dobText);
        nidText = findViewById(R.id.nidText);

        String name = getIntent().getStringExtra("name");
        String dob = getIntent().getStringExtra("dob");
        String nid = getIntent().getStringExtra("nid");
        String error = getIntent().getStringExtra("error");
        if (error != null){
            nameText.setText(error);
        }else {
            nameText.setText(name);
            dobText.setText(dob);
            nidText.setText(nid);
        }

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(ResultShow.this, MainActivity.class);
                startActivity(in);
            }
        });
    }

    @Override
    public void onBackPressed() {
    }
}
