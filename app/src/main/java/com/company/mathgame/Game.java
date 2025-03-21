package com.company.mathgame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;
import java.util.Random;

public class Game extends AppCompatActivity {

    TextView score,life,time,question;
    EditText answer;
    Button ok,next;

    //"Random" class generates random numbers(we can generate integer,double,long numbers using "Random" class)
    Random random = new Random();
    int number1,number2;
    int userAnswer;
    int realAnswer;
    int userScore = 0, userLife = 3;
    //class from java to count down the time
    CountDownTimer timer;
    //we create final long container to determine the initial value of time
    private static final long START_TIMER_IN_MILLIS = 20000;/*you must define the time in milliseconds, the
    final value should be in capital letters, in fact java works that way*/
    //we need a boolean value to determine that the time is running or not
    Boolean timer_running;
    long time_left_in_millis = START_TIMER_IN_MILLIS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        score = findViewById(R.id.textViewScore);
        life = findViewById(R.id.textViewLife);
        time = findViewById(R.id.textViewTime);
        question = findViewById(R.id.textViewQuestion);
        answer = findViewById(R.id.editTextAnswer);
        ok = findViewById(R.id.buttonOk);
        next = findViewById(R.id.buttonNext);

        gameContinue();

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //we need to covert it to string
                userAnswer =Integer.valueOf(answer.getText().toString());

                pauseTimer();
           
                if ( userAnswer == realAnswer ) {

                    userScore = userScore + 10;
                    score.setText("" + userScore);
                    question.setText("Congratulations, Your answer is true.");

                }
                else {

                    userLife--;
                    life.setText("" + userLife);
                    question.setText("Sorry, Your answer is false.");

                }
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (userLife <= 0){
                    Toast.makeText(Game.this, "Game Over", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Game.this,Result.class);
                    intent.putExtra("score",userScore);
                    startActivity(intent);
                    finish();//when the "Result" activity opens, the "Game" activity will be closed
                }
                else {
                    resetTimer();
                    answer.setText("");
                    gameContinue();
                }

            }
        });
    }
    //i'll generate numbers in this method
    public void gameContinue()
    {
        number1 = random.nextInt(100); /*the "random" class will generate an integer number randomly
         and assign it to "number1" container*/
        number2 = random.nextInt(150)+ 50;//number2 is between 50 and 200
        //number2 = random.nextInt(upperBound-lowerBound) + lowerBound;
        realAnswer = number1 + number2;

        question.setText(number1 + "+" + number2);
        startTimer();
    }
    public void startTimer()
    {
        timer = new CountDownTimer(time_left_in_millis,1000) {
            @Override// in this method you'll define a time such as one second and what we want it to do every second
            public void onTick(long millisUntilFinished) {

                time_left_in_millis = millisUntilFinished;//"onTick" method will work 60 seconds to finish
                updateTimeText();
            }

            @Override// in this method you'll write what you want to do when the time finishes
            public void onFinish() {
                timer_running = false;//when the time is up, the timer should stop
                pauseTimer();
                resetTimer();
                updateTimeText();
                userLife = userLife - 1;
                life.setText("" + userLife );
                question.setText("Sorry! Time is up!");
            }
        }.start();
        //if it doesn't go to the "onFinish" method, the time of running should stay true
        timer_running = true;
    }

    public void updateTimeText()
    {
        int second = (int) (time_left_in_millis / 1000) % 60;
        //we will write the second integer value on the time text by converting this integer value to string
        //"String.format" is to show the time in a specific format
        String time_left = String.format(Locale.getDefault(),"%02d",second);
        //we'll write the string value on to the time text
        time.setText(time_left);

    }

    public void pauseTimer()
    {
        timer.cancel();
        timer_running = false;
    }

    public void resetTimer()
    {
        time_left_in_millis = START_TIMER_IN_MILLIS;
        updateTimeText();
    }
}