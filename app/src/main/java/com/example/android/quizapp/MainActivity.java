package com.example.android.quizapp;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import static android.widget.Toast.makeText;

/**
 * A quiz app name Marine Engineering Quiz
 *
 * @author Ogheneovo S. Akperhe
 * @version 1.0
 *          the code try to display relevant content by calling the method setVisibility when needed
 *          since most of the user operation are handle by one activity file
 */
public class MainActivity extends AppCompatActivity {
    int score;
    int currentQueNum;
    int maxNumOfQues = 10;//this is the maximum number of question in the resources folder value/string
    String userName;
    String[] userAnswer = new String[maxNumOfQues];
    int noOfSkippedQues;
    String oops; // message to inform the user that he/she didn't reach the end of test before submitting

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);//this hide the keyboard on start of app
        LinearLayout checkGroup = (LinearLayout) findViewById(R.id.check_group);
        checkGroup.setVisibility(View.GONE);
        LinearLayout nextAndSubmitButtons = (LinearLayout) findViewById(R.id.next_and_submit_buttons);
        nextAndSubmitButtons.setVisibility(View.GONE);
        EditText inputText = (EditText) findViewById(R.id.input_text);
        inputText.setVisibility(View.GONE);
        LinearLayout radioButton = (LinearLayout) findViewById(R.id.radio_group);
        radioButton.setVisibility(View.GONE);
        TextView questionsPane = (TextView) findViewById(R.id.questions_pane);
        questionsPane.setVisibility(View.GONE);
    }

    //there is need to override the onConfigurationChanged method to ensure the app handle technical challenge of restarting
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    //this method login is execute when the user click login which is mandatory to access test
    public void loginButton(View view) {
        EditText userNameInput = (EditText) findViewById(R.id.user_name);

        if (userNameInput.getText().toString().trim().length() != 0) {
            LinearLayout loginPage = (LinearLayout) findViewById(R.id.login_page);
            loginPage.setVisibility(View.GONE);
            TextView questionsPane = (TextView) findViewById(R.id.questions_pane);
            questionsPane.setVisibility(View.VISIBLE);
            LinearLayout nextAndSubmitButtons = (LinearLayout) findViewById(R.id.next_and_submit_buttons);
            nextAndSubmitButtons.setVisibility(View.VISIBLE);
            Button nextButton = (Button) findViewById(R.id.next_button);
            nextButton.setEnabled(false);
            userName = userNameInput.getText().toString();
        }

        //tells the user that he/she need to provides his/her name
        else {
            Toast userIdNeeded = Toast.makeText(getApplicationContext(), "USERNAME REQUIRED", Toast.LENGTH_SHORT);
            userIdNeeded.show();
        }
    }

    //this method displays the next question
    public void nextButton(View view) {
        Button submit = (Button) findViewById(R.id.start_and_submit_button);
        RadioGroup radioButtons = (RadioGroup) findViewById(R.id.radio_group);
        LinearLayout checkGroup = (LinearLayout) findViewById(R.id.check_group);
        EditText inputText = (EditText) findViewById(R.id.input_text);
        getUserAnswer();

        if (currentQueNum < maxNumOfQues) {
            currentQueNum += 1;
            optionsSelect();

            //this set question no 4 to checkbox view UI reply as required
            if (currentQueNum == 4) {
                radioButtons.setVisibility(View.GONE);
                checkGroup.setVisibility(View.VISIBLE);
                inputText.setVisibility(View.GONE);
                String que[] = getResources().getStringArray(R.array.questions);
                TextView questionsPane = (TextView) findViewById(R.id.questions_pane);
                questionsPane.setText(que[currentQueNum - 1]);
            }

            //this below block of code will set question number 7 and 10 to textView UI reply
            else if (currentQueNum == 7 || currentQueNum == 10) {
                radioButtons.setVisibility(View.GONE);
                inputText.setVisibility(View.VISIBLE);
                checkGroup.setVisibility(View.GONE);
                String que[] = getResources().getStringArray(R.array.questions);
                TextView quesDisp = (TextView) findViewById(R.id.questions_pane);
                quesDisp.setText(que[currentQueNum - 1]);
            }

            // this below block of code will set the view with appropriate UI for answers
            else {
                radioButtons.setVisibility(View.VISIBLE);
                inputText.setVisibility(View.GONE);
                checkGroup.setVisibility(View.GONE);
                String que[] = getResources().getStringArray(R.array.questions);
                TextView questionsPane = (TextView) findViewById(R.id.questions_pane);
                questionsPane.setText(que[currentQueNum - 1]);
            }
        }

        //notify the user that he/she have reach the end of test he should submit?
        else {
            Toast endOfQuiz = makeText(getApplicationContext(), "YOU HAVE REACH THE END OF TEST, SUBMIT?", Toast.LENGTH_SHORT);
            endOfQuiz.show();
            submit.setEnabled(true);
        }
        if (currentQueNum != maxNumOfQues) {
            inputText.setText("");//clear the text field for subsequent use
            radioButtons.clearCheck();//this clear the check state of radioButtons since same object will be re used in next question
        }
    }

    //this method functions as to start or submit test when appropriate
    public void startOrSubmitButton(View view) {
        Button nextButton = (Button) findViewById(R.id.next_button);
        nextButton.setEnabled(false);
        String solutions[] = getResources().getStringArray(R.array.solutions);
        Button startAndSubmitButton = (Button) findViewById(R.id.start_and_submit_button);
        RadioGroup radioButton = (RadioGroup) findViewById(R.id.radio_group);
        LinearLayout checkGroup = (LinearLayout) findViewById(R.id.check_group);
        EditText inputText = (EditText) findViewById(R.id.input_text);

        if (currentQueNum != 0) {
            getUserAnswer();//get the user's answer or response as the instance of submit
            startAndSubmitButton.setEnabled(true);
            for (int i = 0; i < currentQueNum; ++i) {
                if (userAnswer[i].equalsIgnoreCase(solutions[i]))
                    score += 1;
                if (userAnswer[i].equalsIgnoreCase("skipped"))
                    noOfSkippedQues += 1;
                if (currentQueNum != maxNumOfQues)
                    oops = "\nYou didn't reach the end of test";
            }
            disableCheckBox();
            disableInputText();
            disableRadioButtons();
            disableStartOrSubmitButton();
            disableNextButton();
            // give toast of the overall performance
            Toast displayResult = Toast.makeText(getApplicationContext(), userName.toUpperCase() + " you have score " + String.valueOf(score) + "\nout of " + String.valueOf(maxNumOfQues) + " questions attempted.", Toast.LENGTH_LONG);
            displayResult.show();

            //using another activity to display  the quiz result and summary
            Intent intent = new Intent(MainActivity.this, QuizSummaryActivity.class);
            intent.putExtra("score", String.valueOf(score));
            intent.putExtra("userName", userName);
            intent.putExtra("oops", oops);
            intent.putExtra("noOfSkippedQues", String.valueOf(noOfSkippedQues));
            intent.putExtra("currentQueNum", String.valueOf(currentQueNum));

            startActivity(intent);
        } else {
            currentQueNum += 1;
            radioButton.setVisibility(View.VISIBLE);
            inputText.setVisibility(View.GONE);
            checkGroup.setVisibility(View.GONE);
            String que[] = getResources().getStringArray(R.array.questions);
            TextView questionsPane = (TextView) findViewById(R.id.questions_pane);
            questionsPane.setText(que[currentQueNum - 1]);
            optionsSelect();
            startAndSubmitButton.setText(R.string.submit_button);
            nextButton.setEnabled(true);
        }
    }

    //these below methods are to set options for respective questions
    private void ans1Option() {
        RadioButton radioButtonA = (RadioButton) findViewById(R.id.radio_button_A);
        RadioButton radioButtonB = (RadioButton) findViewById(R.id.radio_button_B);
        RadioButton radioButtonC = (RadioButton) findViewById(R.id.radio_Button_C);
        RadioButton radioButtonD = (RadioButton) findViewById(R.id.radio_Button_D);
        radioButtonA.setText(R.string.que1Opt1);
        radioButtonB.setText(R.string.que1Opt2);
        radioButtonC.setText(R.string.que1Opt3);
        radioButtonD.setText(R.string.que1Opt4);
    }

    private void ans2Option() {
        RadioButton radioButtonA = (RadioButton) findViewById(R.id.radio_button_A);
        RadioButton radioButtonB = (RadioButton) findViewById(R.id.radio_button_B);
        RadioButton radioButtonC = (RadioButton) findViewById(R.id.radio_Button_C);
        RadioButton radioButtonD = (RadioButton) findViewById(R.id.radio_Button_D);
        radioButtonA.setText(R.string.que2Opt1);
        radioButtonB.setText(R.string.que2Opt2);
        radioButtonC.setText(R.string.que2Opt3);
        radioButtonD.setText(R.string.que2Opt4);
    }

    private void ans3Option() {
        RadioButton radioButtonA = (RadioButton) findViewById(R.id.radio_button_A);
        RadioButton radioButtonB = (RadioButton) findViewById(R.id.radio_button_B);
        RadioButton radioButtonC = (RadioButton) findViewById(R.id.radio_Button_C);
        RadioButton radioButtonD = (RadioButton) findViewById(R.id.radio_Button_D);
        radioButtonA.setText(R.string.que3Opt1);
        radioButtonB.setText(R.string.que3Opt2);
        radioButtonC.setText(R.string.que3Opt3);
        radioButtonD.setText(R.string.que3Opt4);
    }

    private void ans4Option() {
        CheckBox checkbox1 = (CheckBox) findViewById(R.id.checkbox_1);
        CheckBox checkbox2 = (CheckBox) findViewById(R.id.checkbox_2);
        CheckBox checkbox3 = (CheckBox) findViewById(R.id.checkbox_3);
        CheckBox checkbox4 = (CheckBox) findViewById(R.id.checkbox_4);
        checkbox1.setText(R.string.que4Opt1);
        checkbox2.setText(R.string.que4Opt2);
        checkbox3.setText(R.string.que4Opt3);
        checkbox4.setText(R.string.que4Opt4);
    }

    private void ans5Option() {
        RadioButton radioButtonA = (RadioButton) findViewById(R.id.radio_button_A);
        RadioButton radioButtonB = (RadioButton) findViewById(R.id.radio_button_B);
        RadioButton radioButtonC = (RadioButton) findViewById(R.id.radio_Button_C);
        RadioButton radioButtonD = (RadioButton) findViewById(R.id.radio_Button_D);
        radioButtonA.setText(R.string.que5Opt1);
        radioButtonB.setText(R.string.que5Opt2);
        radioButtonC.setText(R.string.que5Opt3);
        radioButtonD.setText(R.string.que5Opt4);
    }

    private void ans6Option() {
        RadioButton radioButtonA = (RadioButton) findViewById(R.id.radio_button_A);
        RadioButton radioButtonB = (RadioButton) findViewById(R.id.radio_button_B);
        RadioButton radioButtonC = (RadioButton) findViewById(R.id.radio_Button_C);
        RadioButton radioButtonD = (RadioButton) findViewById(R.id.radio_Button_D);
        radioButtonA.setText(R.string.que6Opt1);
        radioButtonB.setText(R.string.que6Opt2);
        radioButtonC.setText(R.string.que6Opt3);
        radioButtonD.setText(R.string.que6Opt4);
    }

    private void ans7Option() {
        EditText inputText = (EditText) findViewById(R.id.input_text);
        inputText.getText();
    }

    private void ans8Option() {
        RadioButton radioButtonA = (RadioButton) findViewById(R.id.radio_button_A);
        RadioButton radioButtonB = (RadioButton) findViewById(R.id.radio_button_B);
        RadioButton radioButtonC = (RadioButton) findViewById(R.id.radio_Button_C);
        RadioButton radioButtonD = (RadioButton) findViewById(R.id.radio_Button_D);
        radioButtonA.setText(R.string.que8Opt1);
        radioButtonB.setText(R.string.que8Opt2);
        radioButtonC.setText(R.string.que8Opt3);
        radioButtonD.setText(R.string.que8Opt4);
    }

    private void ans9Option() {
        RadioButton radioButtonA = (RadioButton) findViewById(R.id.radio_button_A);
        RadioButton radioButtonB = (RadioButton) findViewById(R.id.radio_button_B);
        RadioButton radioButtonC = (RadioButton) findViewById(R.id.radio_Button_C);
        RadioButton radioButtonD = (RadioButton) findViewById(R.id.radio_Button_D);
        radioButtonA.setText(R.string.que9Opt1);
        radioButtonB.setText(R.string.que9Opt2);
        radioButtonC.setText(R.string.que9Opt3);
        radioButtonD.setText(R.string.que9Opt4);
    }

    private void ans10Option() {
        ans7Option(); // since they are using same field of edit text
    }

    //this method will select the options to be display for respective questions
    private void optionsSelect() {
        switch (currentQueNum) {
            case 1:
                ans1Option();
                break;
            case 2:
                ans2Option();
                break;
            case 3:
                ans3Option();
                break;
            case 4:
                ans4Option();
                break;
            case 5:
                ans5Option();
                break;
            case 6:
                ans6Option();
                break;
            case 7:
                ans7Option();
                break;
            case 8:
                ans8Option();
                break;
            case 9:
                ans9Option();
                break;
            case 10:
                ans10Option();
                break;
        }
    }

    /**
     * this method getUserAnswer returns the answer of the user, chosen option,
     * else if user skipped it return skipped
     *
     * @return a String data type
     */
    private String getUserAnswer() {
        CheckBox checkBox1 = (CheckBox) findViewById(R.id.checkbox_1);
        CheckBox checkBox2 = (CheckBox) findViewById(R.id.checkbox_2);
        CheckBox checkBox3 = (CheckBox) findViewById(R.id.checkbox_3);
        CheckBox checkBox4 = (CheckBox) findViewById(R.id.checkbox_4);
        EditText inputText = (EditText) findViewById(R.id.input_text);
        RadioButton radioButtonA = (RadioButton) findViewById(R.id.radio_button_A);
        RadioButton radioButtonB = (RadioButton) findViewById(R.id.radio_button_B);
        RadioButton radioButtonC = (RadioButton) findViewById(R.id.radio_Button_C);
        RadioButton radioButtonD = (RadioButton) findViewById(R.id.radio_Button_D);

        if (radioButtonA.isChecked())
            userAnswer[currentQueNum - 1] = radioButtonA.getText().toString();
        else if (radioButtonB.isChecked())
            userAnswer[currentQueNum - 1] = radioButtonB.getText().toString();
        else if (radioButtonC.isChecked())
            userAnswer[currentQueNum - 1] = radioButtonC.getText().toString();
        else if (radioButtonD.isChecked())
            userAnswer[currentQueNum - 1] = radioButtonD.getText().toString();

            //the below block of code can be modify if needed that, to record the state of checkbox
            //and compare with set down cases
            //but in this situation this is the only condition that a correct answer is chosen
        else if (checkBox1.isChecked() && checkBox2.isChecked() && checkBox3.isChecked() && !checkBox4.isChecked()) {
            userAnswer[currentQueNum - 1] = checkBox1.getText().toString() + "," + checkBox2.getText().toString() + "," + checkBox3.getText().toString();

        } else if (inputText.getText().toString().trim().length() != 0) {
            userAnswer[currentQueNum - 1] = inputText.getText().toString();
            inputText.setText("");// this will set the text field back to empty for subsequent use
        } else
            userAnswer[currentQueNum - 1] = "skipped";//the string when user skipped question
        return userAnswer[currentQueNum - 1];
    }

    // these sets of methods are to disable the buttons when appropriate
    private void disableRadioButtons() {
        RadioButton radioButtonA = (RadioButton) findViewById(R.id.radio_button_A);
        radioButtonA.setEnabled(false);
        RadioButton radioButtonB = (RadioButton) findViewById(R.id.radio_button_B);
        radioButtonB.setEnabled(false);
        RadioButton radioButtonC = (RadioButton) findViewById(R.id.radio_Button_C);
        radioButtonC.setEnabled(false);
        RadioButton radioButtonD = (RadioButton) findViewById(R.id.radio_Button_D);
        radioButtonD.setEnabled(false);
    }

    private void disableCheckBox() {
        CheckBox checkBox1 = (CheckBox) findViewById(R.id.checkbox_1);
        checkBox1.setEnabled(false);
        CheckBox checkBox2 = (CheckBox) findViewById(R.id.checkbox_2);
        checkBox2.setEnabled(false);
        CheckBox checkBox3 = (CheckBox) findViewById(R.id.checkbox_3);
        checkBox3.setEnabled(false);
        CheckBox checkBox4 = (CheckBox) findViewById(R.id.checkbox_4);
        checkBox4.setEnabled(false);
    }

    private void disableInputText() {
        EditText inputText = (EditText) findViewById(R.id.input_text);
        inputText.setEnabled(false);
    }

    private void disableStartOrSubmitButton() {
        Button startOrSubmitButton = (Button) findViewById(R.id.start_and_submit_button);
        startOrSubmitButton.setEnabled(false);
    }

    private void disableNextButton() {
        Button nextButton = (Button) findViewById(R.id.next_button);
        nextButton.setEnabled(false);
    }

}

