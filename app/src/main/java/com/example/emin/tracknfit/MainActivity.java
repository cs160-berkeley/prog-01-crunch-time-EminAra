/**
 * Created by Emin on 2/4/16.
 */



package com.example.emin.tracknfit;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.app.AlertDialog;
import android.widget.EditText;
import android.text.InputType;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.text.Editable;
import android.text.TextWatcher;
import java.util.ArrayList;
import java.util.List;



public class MainActivity extends Activity {


    private List<myExercise> myActivities = new ArrayList<myExercise>();
    private String weight_variable_key = "15";



    private int index_of_selected_exc = 0;

    private boolean calories_not_selected = false;

    private double user_input_value = 0;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }



    //Importing the data in a structured way using JAVA classes (our class name is myExercise)
    private void populate_my_exercises()

    {

        myActivities.add(new myExercise("Pushup",R.drawable.push_ups_app,true,350,0) );

        myActivities.add(new myExercise("Situp",R.drawable.sit_ups_app,true,200,0) );

        myActivities.add(new myExercise("Squats",R.drawable.squat_app,true,225,0) );

        myActivities.add(new myExercise("Leg-lift",R.drawable.leg_lifts_app,false,0,25) );

        myActivities.add(new myExercise("Plank", R.drawable.plank_app, false, 0, 25));

        myActivities.add(new myExercise("Jumping Jacks",R.drawable.jumping_jacks_app,false,0,10) );

        myActivities.add(new myExercise("Pullup", R.drawable.pull_up_app, true, 100, 0));

        myActivities.add(new myExercise("Cycling", R.drawable.cycling_app, false, 0, 12));

        myActivities.add(new myExercise("Walking", R.drawable.walking_app, false, 0, 20));

        myActivities.add(new myExercise("Jogging", R.drawable.jogging_app, false, 0, 12));

        myActivities.add(new myExercise("Swimming", R.drawable.swimming_app, false, 0, 13));

        myActivities.add(new myExercise("Stair-Climbing", R.drawable.climbing_stairs_app, false, 0, 15));

    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.weight_btn:
                display_fragment_weight();
                break;
        }

        return true;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);


        populate_my_exercises();

        refreshSpinner();

        refreshTable();

        final EditText txt_enter = (EditText) findViewById(R.id.txtEnter);

        txt_enter.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {

                if (txt_enter.getText().toString().length() > 0)
                    user_input_value = Double.parseDouble(txt_enter.getText().toString());

                refreshTable();


            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });


    }


    private void refreshSpinner(){
        Spinner myspn = (Spinner) findViewById(R.id.spnSpinner);



        ArrayList<String> itemsA = getExercisesNames();

        String[] items = itemsA.toArray(new String[itemsA.size()]);

        ArrayAdapter aa = new ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item,
                items);




        aa.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);
        myspn.setAdapter(aa);

        myspn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {

                if (pos == 0) {

                    calories_not_selected = false;
                    Toast.makeText(MainActivity.this, "Calories selected", Toast.LENGTH_SHORT).show();
                } else {
                    calories_not_selected = true;
                    index_of_selected_exc = pos - 1;
                    Toast.makeText(MainActivity.this, myActivities.get(pos - 1).getName() + " selected", Toast.LENGTH_SHORT).show();

                }

                refreshTable();

            }

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void refreshTable() {
        ArrayAdapter<myExercise> adapter= new MyListAdapter();
        GridView list = (GridView) findViewById(R.id.myListView);
        list.setAdapter(adapter);

    }



    private class MyListAdapter extends ArrayAdapter<myExercise>{
        public MyListAdapter()
        {
            super(MainActivity.this, R.layout.track_item, myActivities );

        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View itemView = convertView;


            if (itemView == null){
                itemView = getLayoutInflater().inflate(R.layout.track_item,parent,false);
            }



            myExercise currentActivity = myActivities.get(position);


            ImageView imgView = (ImageView) itemView.findViewById(R.id.myImg);
            imgView.setImageResource(currentActivity.getIconID());

            String type_u = " Mins";
            if (currentActivity.isReps())
                type_u = " Reps";


            if (calories_not_selected) {

                double calories_conv = reps_equi_cal(index_of_selected_exc, user_input_value);

                TextView txtCal = (TextView) itemView.findViewById(R.id.exc_cal);
                txtCal.setText(String.format("%.1f", calories_conv) + " Cal");


                double equ_reps = equi_reps(user_input_value, index_of_selected_exc, position);


                TextView txt_r = (TextView) itemView.findViewById(R.id.exc_reps);
                txt_r.setText(String.format("%.1f", equ_reps) + type_u);

                TextView txt_n = (TextView) itemView.findViewById(R.id.exc_name);
                txt_n.setText(currentActivity.getName());

            }else{


                double reps_cov = calories_equi_reps(user_input_value,position);

                TextView txt_n = (TextView) itemView.findViewById(R.id.exc_name);
                txt_n.setText(currentActivity.getName());

                TextView txt_r = (TextView) itemView.findViewById(R.id.exc_reps);
                txt_r.setText(String.format("%.1f", reps_cov) + type_u);

                TextView txt_c = (TextView) itemView.findViewById(R.id.exc_cal);
                txt_c.setText(String.format("%.1f", user_input_value) + " Cal");

            }



            return itemView;



        }
    }






    public void display_fragment_weight()
    {


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Please enter your weight (lbs)");

        final EditText input = new EditText(this);
        input.setText(String.valueOf(getWeight()));

        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        builder.setView(input);




        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                set_weight(input.getText().toString());
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();


    }

    private double getWeight(){

        SharedPreferences mPrefs = getSharedPreferences("0", 0);
        return Double.parseDouble(mPrefs.getString(weight_variable_key, "150"));

    }


    private void set_weight(String weight){


        SharedPreferences mPrefs = getSharedPreferences("0", 0);
        SharedPreferences.Editor mEditor = mPrefs.edit();
        mEditor.putString(weight_variable_key, weight).commit();

        Toast.makeText(this, "Weight saved", Toast.LENGTH_SHORT).show();

    }



    private double equi_reps(double repsOrMinutes, int pos_0, int pos_1)
    {

        if (repsOrMinutes == 0)
            return 0;

        myExercise activity_selected = myActivities.get(pos_0);

        myExercise activity_convertTo = myActivities.get(pos_1);


        double calories = reps_equi_cal(pos_0, repsOrMinutes);

        double var_reps_min = calories_equi_reps(calories, pos_1);

        if (var_reps_min>0)
            return var_reps_min;
        else
            return 0;

    }



    private double reps_equi_cal(int pos, double repsOrMinutes)
    {

        double male_r = 0.09036;
        double female_r = 0.08736;

        if (repsOrMinutes == 0)
            return 0;

        myExercise currentActivity = myActivities.get(pos);

        if (currentActivity.isReps())
        {
            double weight = getWeight();
            int rate = currentActivity.getRepsRate();



            double result =  ((repsOrMinutes/(double)rate) * 100.0 - (weight - 150.0 ) * (male_r + female_r)/2.0 );

            if (result>0)
                return result ;

            else
                return 0;


        }else

        {

            double weight = getWeight();
            int rate = currentActivity.getDurationRate();
            double result =  ((repsOrMinutes/(double)rate) * 100.0 - (weight - 150.0 ) *  (male_r + female_r)/2.0) ;

            if (result>0)
                return result ;

            else
                return 0;
        }

    }



    private double calories_equi_reps(double calories, int pos_1){

        double male_r = 0.09036;
        double female_r = 0.08736;


        if (calories == 0)
            return 0;


        myExercise currentActivity = myActivities.get(pos_1);
        double weight_user = getWeight();
        if (currentActivity.isReps())
        {
            int rate_exercise = currentActivity.getRepsRate();

            double result = ((  (calories - (weight_user - 150.0 ) * (male_r + female_r )/2.0) )/(double)100) * rate_exercise  ;

            if (result>0)
                return result ;

            else
                return 0;

        }else

        {

            int rate_exercise = currentActivity.getDurationRate();


            double result = ((  (calories - (weight_user - 150.0 ) * (male_r + female_r )/2.0 ) )/(double)100) * rate_exercise  ;

            if (result>0)
                return result ;

            else
                return 0;

        }

    }

    public ArrayList getExercisesNames()
    {

        ArrayList namesArray = new ArrayList<String>();
        namesArray.add("Calories");
        for (int i = 0;i < myActivities.size();i++)
        {

            String reps;

            if (myActivities.get(i).isReps())
            {

                reps = "Rep";

            }else
            {

                reps = "Min";

            }

            namesArray.add( reps +" | " + myActivities.get(i).getName()  );

        }

        return namesArray;

    }

}
