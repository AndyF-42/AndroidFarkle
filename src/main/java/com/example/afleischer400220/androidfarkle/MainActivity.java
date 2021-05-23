package com.example.afleischer400220.androidfarkle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Program plays a dice game called Farkle,
 * where user rolls dice, selects which ones
 * to score, and can stop the scoring.
 * Author: Andy Fleischer
 * Date: 2/21/2020
 */


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    ImageButton[] buttons = new ImageButton[6];
    int[] buttonState = new int[6];
    int[] dieImages = new int[6];
    int[] dieValue = new int[6];
    final int HOT_DIE = 0;
    final int SCORE_DIE = 1;
    final int LOCKED_DIE = 2;
    Button roll;
    Button score;
    Button stop;
    TextView currentScoreTV;
    TextView totalScoreTV;
    TextView currentRoundTV;
    int currentScore;
    int totalScore;
    int currentRound = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //set up dice buttons
        buttons[0] = (ImageButton)this.findViewById(R.id.imageButton1);
        buttons[1] = (ImageButton)this.findViewById(R.id.imageButton2);
        buttons[2] = (ImageButton)this.findViewById(R.id.imageButton3);
        buttons[3] = (ImageButton)this.findViewById(R.id.imageButton4);
        buttons[4] = (ImageButton)this.findViewById(R.id.imageButton5);
        buttons[5] = (ImageButton)this.findViewById(R.id.imageButton6);
        for (int a = 0; a < buttons.length; a++) {
            buttons[a].setOnClickListener(this);
            buttons[a].setEnabled(false);
            buttons[a].setBackgroundColor(Color.LTGRAY);
        }
        //roll, score, and stop buttons
        roll = (Button)this.findViewById(R.id.button1);
        roll.setOnClickListener(this);
        roll.setBackgroundResource(android.R.drawable.btn_default);
        roll.setBackgroundColor(Color.GREEN);
        score = (Button)this.findViewById(R.id.button2);
        score.setOnClickListener(this);
        score.setEnabled(false);
        score.setBackgroundResource(android.R.drawable.btn_default);
        stop = (Button)this.findViewById(R.id.button3);
        stop.setOnClickListener(this);
        stop.setEnabled(false);
        stop.setBackgroundResource(android.R.drawable.btn_default);
        currentScoreTV = (TextView)this.findViewById(R.id.textView1);
        totalScoreTV = (TextView)this.findViewById(R.id.textView2);
        currentRoundTV = (TextView)this.findViewById(R.id.textView3);
        //map the images to the dice images array
        dieImages[0] = R.drawable.one;
        dieImages[1] = R.drawable.two;
        dieImages[2] = R.drawable.three;
        dieImages[3] = R.drawable.four;
        dieImages[4] = R.drawable.five;
        dieImages[5] = R.drawable.six;

    }

    @Override
    public void onClick(View v) {
        //if roll button clicked, randomize all dice
        if (v.equals(roll)) {
            roll.setBackgroundResource(android.R.drawable.btn_default);
            for (int a = 0; a < buttons.length; a++) {
                if (buttonState[a] == HOT_DIE) {
                    int choice = (int)(Math.random() * 6);
                    dieValue[a] = choice;
                    buttons[a].setImageResource(dieImages[choice]);
                    buttons[a].setEnabled(true);
                    roll.setEnabled(false);
                    score.setEnabled(true);
                    stop.setEnabled(false);
                }
            }
        }
        //if score button clicked, score each dice based on farkle rules
        else if (v.equals(score)) {
            int[] valueCount = new int[7];
            for (int a = 0; a < buttonState.length; a++) {
                if (buttonState[a] == SCORE_DIE) {
                    valueCount[dieValue[a] + 1]++;
                }
            }
            //bad dice
            if ((valueCount[2] > 0 && valueCount[2] < 3) ||
                    (valueCount[3] > 0 && valueCount[3] < 3) ||
                    (valueCount[4] > 0 && valueCount[4] < 3) ||
                    (valueCount[6] > 0 && valueCount[6] < 3)) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setTitle("Bad Dice Selected!");
                alertDialogBuilder
                        .setMessage("Some of the dice you chose are not scorable!")
                        .setCancelable(false)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.dismiss();
                            }
                        });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

            }
            //no dice selected
            else if (valueCount[1] == 0 && valueCount[2] == 0 &&
                    valueCount[3] == 0 && valueCount[4] == 0 &&
                    valueCount[5] == 0 && valueCount[6] == 0) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setTitle("No Dice Selected!");
                alertDialogBuilder
                        .setMessage("Forfeit score and go to next round?")
                        .setCancelable(false)
                        .setPositiveButton("Yes :(",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                currentScore = 0;
                                currentRound++;
                                currentScoreTV.setText("Current Score: " + currentScore + "      ");
                                currentRoundTV.setText("Current Round: " + currentRound);
                                resetDice();
                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton("No",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
            //triples or singles of ones and fives
            else {
                if(valueCount[1] < 3) {
                    currentScore += (valueCount[1] * 100);
                }
                if(valueCount[5] < 3) {
                    currentScore += (valueCount[5] * 50);
                }
                if(valueCount[1] >= 3) {
                    currentScore += 1000 * (valueCount[1] - 2);
                }
                if(valueCount[2] >= 3) {
                    currentScore += 200 * (valueCount[2] - 2);
                }
                if(valueCount[3] >= 3) {
                    currentScore += 300 * (valueCount[3] - 2);
                }
                if(valueCount[4] >= 3) {
                    currentScore += 400 * (valueCount[4] - 2);
                }
                if(valueCount[5] >= 3) {
                    currentScore += 500 * (valueCount[5] - 2);
                }
                if(valueCount[6] >= 3) {
                    currentScore += 600 * (valueCount[6] - 2);
                }
                currentScoreTV.setText("Current Score: " + currentScore + "      ");
                for (int a = 0; a < buttons.length; a++) {
                    if (buttonState[a] == SCORE_DIE) {
                        buttonState[a] = LOCKED_DIE;
                        buttons[a].setBackgroundColor(Color.BLUE);
                        buttons[a].setEnabled(false);
                    }
                }
                int lockedCount = 0;
                for (int a = 0; a < buttons.length; a++) {
                    if (buttonState[a] == LOCKED_DIE) {
                        lockedCount++;
                    }
                }
                if (lockedCount == 6) {
                    roll.setEnabled(false);
                    score.setEnabled(false);
                    stop.setEnabled(true);
                    return;
                }

                roll.setEnabled(true);
                score.setEnabled(false);
                stop.setEnabled(true);
            }

        }
        //if stop is clicked, transfer score into total score and keep playing
        else if (v.equals(stop)) {
            totalScore += currentScore;
            currentScore = 0;
            currentScoreTV.setText("Current Score: " + currentScore + "      ");
            totalScoreTV.setText("Total Score: " + totalScore + "      ");
            currentRound++;
            currentRoundTV.setText("Current Round: " + currentRound);
            resetDice();
        }
        //otherwise, find which die was clicked and set it to a score_die
        else {
            for (int a = 0; a < buttons.length; a++) {
                if (v.equals(buttons[a])) {
                    if (buttonState[a] == HOT_DIE) {
                        buttons[a].setBackgroundColor(Color.RED);
                        buttonState[a] = SCORE_DIE;
                    }
                    else {
                        buttons[a].setBackgroundColor(Color.LTGRAY);
                        buttonState[a] = HOT_DIE;
                    }
                }
            }
        }
    }

    //Sets all dice back to hot and sets up the buttons
    private void resetDice() {
        for (int a = 0; a < buttons.length; a++) {
            buttons[a].setEnabled(false);
            buttonState[a] = HOT_DIE;
            buttons[a].setBackgroundColor(Color.LTGRAY);
        }
        roll.setEnabled(true);
        roll.setBackgroundColor(Color.GREEN);
        score.setEnabled(false);
        stop.setEnabled(false);
    }
}
