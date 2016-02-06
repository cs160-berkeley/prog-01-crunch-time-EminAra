package com.example.emin.tracknfit;

/**
 * Created by Emin on 2/4/16.
 */

public class myExercise {
    private String name;
    private int icon_id;
    private boolean is_reps;
    private int reps_rate;
    private int duration_rate;

    public String getName() {
        return name;
    }

    public int getIconID() {
        return icon_id;
    }

    public boolean isReps() {
        return is_reps;
    }

    public int getRepsRate() {
        return reps_rate;
    }

    public int getDurationRate() {
        return duration_rate;
    }


    public myExercise(String ExerciseName, int ExerciseiconID, boolean ExerciseisReps, int Exerciserate, int ExercisedurationRate)
    {

        super();
        this.name = ExerciseName;
        this.is_reps = ExerciseisReps;
        this.icon_id = ExerciseiconID;
        this.reps_rate = Exerciserate;
        this.duration_rate = ExercisedurationRate;



    }






}

