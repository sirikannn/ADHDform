package adhd.sirikan.pimpicha.adhdform;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

public class DeleteActivity extends AppCompatActivity {
    ListView listView;
    private String[] loginString;
    private String tag = "31MarchV2";
    Button button;
    int tmpPosition;
    String tmpIdStrings ;
    String tmpNameChildStrings ;
    String tmpUserString ;
    String tmpGenderStrings ;
     String tmpAgeStrings ;
    String tmpNameChild;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete);
        listView = (ListView) findViewById(R.id.livDeleteChild);
        getValue();
    }
    private void getValue() {
        loginString = getIntent().getStringArrayExtra("Login");
    }
    @Override
    protected void onPostResume() {//**************************************
        super.onPostResume();
        createListView();
    }
    private void createListView() {


        try {
            GetAllData getAllData = new GetAllData(DeleteActivity.this);
            MyConstant myConstant = new MyConstant();
            getAllData.execute(myConstant.getUrlGetChild());

            String strJSON = getAllData.get();
            Log.d(tag, "JSoN ==>" + strJSON);

            String[] columnChileStrings = myConstant.getColumnChild();
            JSONArray jsonArray = new JSONArray(strJSON);
            final String[] idStrings = new String[jsonArray.length()];
            final String[] nameChildStrings = new String[jsonArray.length()];
            final String[] userString = new String[jsonArray.length()];
            final String[] genderStrings = new String[jsonArray.length()];
            final String[] ageStrings = new String[jsonArray.length()];
            String[] imageStrings = new String[jsonArray.length()];
            int j =0;
            for (int i = 0 ;i<jsonArray.length();i++) {


                JSONObject jsonObject = jsonArray.getJSONObject(i);
                if(jsonObject.getString(columnChileStrings[1]).equals(loginString[0])){
                    userString[j] = jsonObject.getString(columnChileStrings[1]);
                    idStrings[j] = jsonObject.getString(columnChileStrings[0]);
                    nameChildStrings[j] = jsonObject.getString(columnChileStrings[2]);
                    imageStrings[j] = jsonObject.getString(columnChileStrings[4]);
                    genderStrings[j] = jsonObject.getString(columnChileStrings[5]);
                    ageStrings[j] = jsonObject.getString(columnChileStrings[3]);
                    j++;
                }


            }

            final String[] countChildString = new String[j];
            //Build ListView
            DeleteAdapter deleteAdapter = new DeleteAdapter(DeleteActivity.this, imageStrings,
                    nameChildStrings,countChildString);
            listView.setAdapter(deleteAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                   //ALERT

                    tmpPosition = position;
                    tmpIdStrings = idStrings[tmpPosition];
                    tmpGenderStrings = idStrings[tmpPosition];
                    tmpAgeStrings = ageStrings[tmpPosition];
                    tmpNameChild = nameChildStrings[position];
                    alertMessage();

                    // ไม่ต้อง หยุด เพราะจะให้ย้อนกลับได้
                }


            });






        } catch (Exception e) {
            Log.d(tag, "e createListView ==>" + e.toString());
        }

    }

    public void alertMessage() {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                switch (which) { case DialogInterface.BUTTON_POSITIVE:
                    // Yes button clicked
                    Intent intent = new Intent(DeleteActivity.this, DeleteDoneActivity.class);
                    intent.putExtra("Login", loginString);
                    intent.putExtra("tmpIndex", tmpIdStrings);
                    intent.putExtra("gender", tmpGenderStrings);
                    intent.putExtra("age", tmpAgeStrings);
                    startActivity(intent);

                    break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        // No button clicked
                        // do nothing
                        Toast.makeText(DeleteActivity.this, "No Clicked",
                                Toast.LENGTH_LONG).show();
                        break;
                }
            }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("คุณกำลังจะลบ " + tmpNameChild +" ใช่หรือไม่")
                .setPositiveButton("ตกลง", dialogClickListener)
                .setNegativeButton("ไม่", dialogClickListener).show();
    }
}
