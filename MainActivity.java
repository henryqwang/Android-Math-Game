package com.example.android.braintrainer;

import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

public class MainActivity extends AppCompatActivity {

    //Views variables
    Button startButton;
    RelativeLayout gameLayout;

    TextView sumTextView;
    TextView answerTextView;
    TextView pointsTextView;
    TextView timerTextView;

    Button button0;
    Button button1;
    Button button2;
    Button button3;

    Button playAgainButton;

    //Button location related variables
    ArrayList<Integer> answers = new ArrayList<Integer>();;
    int locationOfCorrectAnswer;

    //Score related variables
    int score = 0;
    int numberOfQuestionAsked = 0;
    int countDownInterval = 1000;

    //Timer related variable
    final int countdownTimeInSeconds = 10;
    final int timerBias = 100;

    //Flag for toggling game mechanics
    boolean gameIsActive = false;


    public void chooseAnswer(View view){
        if(gameIsActive) {
            if (view.getTag().equals(Integer.toString(locationOfCorrectAnswer))) {
                //Tapped location and correct answer location match; add 1 to current score and total number of questions
                score++;
                numberOfQuestionAsked++;
                answerTextView.setText("Correct!");

            } else {
                //Tapped location and correct answer location match; add 1 only to total number of questions
                numberOfQuestionAsked++;
                answerTextView.setText("Wrong!");
            }
            //Update points and generate new question
            pointsTextView.setText("Pts:\n"+Integer.toString(score) + "/" + Integer.toString(numberOfQuestionAsked));
            generateNewQuestion();
        }
    }

    public void generateNewQuestion(){
        //Generate 2 numbers in the range [0, 20]
        Random rand = new Random();
        int a = rand.nextInt(21);
        int b = rand.nextInt(21);

        //Update the addition problem (top of layout) to a + b
        sumTextView.setText(Integer.toString(a)+" + "+Integer.toString(b));

        //Generate at random the location of the correct answer, indexed with an integer ranging from 0 to 3
        locationOfCorrectAnswer = rand.nextInt(4);

        //Append correct & incorrect answers in the array list, with the location of the correct answer matching its position in the array list
        int incorrectAnswer;
        int correctAnswer = a + b;
        answers.clear(); //Clear previous answers if present

        for(int i = 0; i < 4; i++){
            if(i == locationOfCorrectAnswer){
                answers.add(correctAnswer);
            }else{
                incorrectAnswer = rand.nextInt(41);
                //Re-roll if one of the incorrect answer happens to be the same as the correct answer
                while(incorrectAnswer == correctAnswer){
                    incorrectAnswer = rand.nextInt(41);
                }
                answers.add(incorrectAnswer);
            }
        }
        //Update buttons
        button0.setText(Integer.toString(answers.get(0)));
        button1.setText(Integer.toString(answers.get(1)));
        button2.setText(Integer.toString(answers.get(2)));
        button3.setText(Integer.toString(answers.get(3)));
    }

    public void newGame(View view){
        gameIsActive = true;
        score = 0;
        numberOfQuestionAsked = 0;
        timerTextView.setText("Time:\n"+countdownTimeInSeconds);
        pointsTextView.setText("Score:\n"+"0/0");
        answerTextView.setText("");
        playAgainButton.setAlpha(0f);
        playAgainButton.setEnabled(false);

        generateNewQuestion();

        new CountDownTimer(countdownTimeInSeconds*1000 + timerBias, countDownInterval){//+100 is the bias that counters the fact counter has a slight delay

            @Override
            public void onTick(long secondsRemaining) {
                timerTextView.setText("Time:\n"+Integer.toString((int)secondsRemaining/1000));
            }

            @Override
            public void onFinish() {
                gameIsActive = false;
                playAgainButton.setAlpha(1f);
                playAgainButton.setEnabled(true);
                timerTextView.setText("Time:\n0");
                answerTextView.setText("Your score: " + Integer.toString(score)+"/"+Integer.toString(numberOfQuestionAsked));
            }
        }.start();

    }
    public void start(View view){
        gameIsActive = true;
        startButton.setVisibility(INVISIBLE);
        startButton.setEnabled(false);
        gameLayout.setVisibility(RelativeLayout.VISIBLE);
        newGame(findViewById(R.id.playAgainButton));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startButton = (Button)findViewById(R.id.startButton);
        gameLayout = (RelativeLayout)findViewById(R.id.gameLayout);

        sumTextView = (TextView)findViewById(R.id.sumTextView);
        answerTextView = (TextView)findViewById(R.id.answerTextView);
        pointsTextView = (TextView)findViewById(R.id.pointsTextView);

        button0 = (Button)findViewById(R.id.button0);
        button1 = (Button)findViewById(R.id.button1);
        button2 = (Button)findViewById(R.id.button2);
        button3 = (Button)findViewById(R.id.button3);

        timerTextView = (TextView)findViewById(R.id.timerTextView);
        playAgainButton = (Button)findViewById(R.id.playAgainButton);
    }
}
