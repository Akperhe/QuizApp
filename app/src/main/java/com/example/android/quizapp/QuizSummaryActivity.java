package com.example.android.quizapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class QuizSummaryActivity extends AppCompatActivity {
    int score;
    String userName;
    String oops;
    int noOfSkippedQues;
    int currentQueNum;
    int maxNumOfQues = 10;//this is the maximum no of question corresponding to those in string resource folder


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_summary);
        gradeUser();
    }

    //this method grade the user on three scale NOVICE INTERMEDIATE and ADVANCE
    public void gradeUser() {
        TextView summary = (TextView) findViewById(R.id.summary);
        Intent intent = getIntent();//getIntent get the previous parameter passed to this activity
        score = Integer.parseInt(intent.getStringExtra("score"));
        noOfSkippedQues = Integer.parseInt(intent.getStringExtra("noOfSkippedQues"));
        oops = intent.getStringExtra("oops");
        userName = intent.getStringExtra("userName");
        currentQueNum = Integer.parseInt(intent.getStringExtra("currentQueNum"));
        ImageView proficiency = (ImageView) findViewById(R.id.proficiency);

        if (score < 5 && currentQueNum < maxNumOfQues) {
            summary.setText("Dear " + userName + " your final score is " + score + "\nLevel of proficiency is Novice\nNo. of skipped questions: " + noOfSkippedQues + oops + "\nWORK HARD");
            proficiency.setImageResource(R.drawable.novice);
        }

        if (score < 5 && currentQueNum == maxNumOfQues) {
            summary.setText("Dear " + userName + " your final score is " + score + "\nLevel of proficiency is Novice\nNo. of skipped questions: " + noOfSkippedQues + "\nWORK HARD");
            proficiency.setImageResource(R.drawable.novice);
        }

        if (score >= 5 && score < 8 && currentQueNum < maxNumOfQues) {
            summary.setText("Dear " + userName + " your final score is " + score + "\nLevel of proficiency is Intermediate\nNo. of skipped questions: " + noOfSkippedQues + oops + "\nKEEP IT UP");
            proficiency.setImageResource(R.drawable.intermediate);
        }

        if (score >= 5 && score < 8 && currentQueNum == maxNumOfQues) {
            summary.setText("Dear " + userName + " your final score is " + score + "\nLevel of proficiency is Intermediate\nNo. of skipped questions: " + noOfSkippedQues + "\nKEEP IT UP");
            proficiency.setImageResource(R.drawable.intermediate);
        }

        if (score >= 8 && currentQueNum < maxNumOfQues) {
            summary.setText("Dear " + userName + " your final score is " + score + "\nLevel of proficiency is Advance\nNo. of skipped questions: " + noOfSkippedQues + oops + "\nCONGRATULATION");
            proficiency.setImageResource(R.drawable.advanced);
        }

        if (score >= 8 && currentQueNum == maxNumOfQues) {
            summary.setText("Dear " + userName + " your final score is " + score + "\nLevel of proficiency is Advanced\nNo. of skipped questions: " + noOfSkippedQues + "\nCONGRATULATION");
            proficiency.setImageResource(R.drawable.advanced);
        }

    }

    //this will minimized
    public void exitApp(View view) {
        Intent homeIntent = new Intent(Intent.ACTION_MAIN);
        homeIntent.addCategory(Intent.CATEGORY_HOME);
        homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(homeIntent);

    }
}
