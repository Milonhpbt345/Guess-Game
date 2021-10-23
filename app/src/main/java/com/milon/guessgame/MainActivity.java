package com.milon.guessgame;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Locale;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private EditText inputEdt;
    private Button doneBtn, playBtn, resetBtn;
    private TextView timerTv, scoreTv, totalPlayTv,
            totalWinTv, lostTv, scoreBoardTv, welComeTv, loserTv, timeUpTv, lastGuessTv, alertTv,gameLevelTv;
    private LinearLayout winnerLL, loserLL, welComeLL, timeUpLL, alertLL, startLL;

    private static final long START_TIME_IN_MILLIS = 60000;
    private CountDownTimer countDownTimer;
    private long mLeftInMillis = START_TIME_IN_MILLIS;

    Animation topAnim, bottomAnim;

    private Random random;
    int randomNumber;
    boolean timeFinish;
    int score, scoreBoard, totalPlay, totalWin, totalLost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        winnerLL = findViewById(R.id.winnerLL);
        loserLL = findViewById(R.id.loserLL);
        welComeLL = findViewById(R.id.welComeLL);
        timeUpLL = findViewById(R.id.timeUpLL);
        alertLL = findViewById(R.id.alertLL);
        startLL = findViewById(R.id.startLL);

        timerTv = findViewById(R.id.timerTv);
        scoreTv = findViewById(R.id.scoreTv);
        inputEdt = findViewById(R.id.inputEdt);
        playBtn = findViewById(R.id.playBtn);
        resetBtn = findViewById(R.id.resetBtn);
        doneBtn = findViewById(R.id.doneBtn);
        totalPlayTv = findViewById(R.id.totalPlayTv);
        totalWinTv = findViewById(R.id.totalWinTv);
        scoreBoardTv = findViewById(R.id.scoreBoardTv);
        lostTv = findViewById(R.id.totalLostTv);
        lastGuessTv = findViewById(R.id.lastGuessTv);
        loserTv = findViewById(R.id.loserTv);
        timeUpTv = findViewById(R.id.timeUpTv);
        welComeTv = findViewById(R.id.welComeTv);
        alertTv = findViewById(R.id.alertTv);
        gameLevelTv = findViewById(R.id.gameLevelTv);

        //Animation init
        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);

        //winnerLL, loserLL, welComeLL, timeUpLL
        welComeLL.setVisibility(View.VISIBLE);
        winnerLL.setVisibility(View.GONE);
        loserLL.setVisibility(View.GONE);
        timeUpLL.setVisibility(View.GONE);
        alertLL.setVisibility(View.GONE);
        startLL.setVisibility(View.GONE);

        randomNumberGenerate();
        addListener();
        updateTimer();

    }

    private void addListener() {

        gameLevelTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               showGameLevelDialog();
            }
        });

        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (timeFinish) {
                    String inputString = inputEdt.getText().toString().trim();

                    if (inputString.isEmpty()) {
                        inputEdt.setError("Enter number");
                    } else {
                        int userNumber = Integer.parseInt(inputString);
                        inputCheck(userNumber);
                    }
                } else {
                    welComeLL.setVisibility(View.GONE);
                    alertLL.setVisibility(View.VISIBLE);
                    startLL.setVisibility(View.GONE);
                    alertTv.setText("Click Play");
                    alertTv.setTextColor(getResources().getColor(R.color.white));
                    winnerLL.setVisibility(View.GONE);
                    loserLL.setVisibility(View.GONE);
                    timeUpLL.setVisibility(View.GONE);
                }


            }
        });

        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (timeFinish) {
                    pauseTimer();
                } else {
                    startTimer();
                }

            }
        });
        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (timeFinish) {
                    welComeLL.setVisibility(View.GONE);
                    alertLL.setVisibility(View.VISIBLE);
                    alertTv.setText("Click Pause");
                    alertTv.setTextColor(getResources().getColor(R.color.red));
                    winnerLL.setVisibility(View.GONE);
                    startLL.setVisibility(View.GONE);
                    loserLL.setVisibility(View.GONE);
                    timeUpLL.setVisibility(View.GONE);
                } else {
                    mLeftInMillis = START_TIME_IN_MILLIS;
                    updateTimer();
                    reSet();

                    welComeLL.setVisibility(View.VISIBLE);
                    alertLL.setVisibility(View.GONE);
                    startLL.setVisibility(View.GONE);
                    winnerLL.setVisibility(View.GONE);
                    loserLL.setVisibility(View.GONE);
                    timeUpLL.setVisibility(View.GONE);
                }

            }

        });
    }

    private void showGameLevelDialog() {
        Dialog dialog=new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.game_level);
        dialog.show();
    }

    private void inputCheck(int userNumber) {

        totalPlay = totalPlay + 1;
        if (userNumber == randomNumber) {
            randomNumberGenerate();
            score = 10;
            scoreBoard = scoreBoard + score;
            totalWin = totalWin + 1;
            scoreTv.setText(String.valueOf(score));
            scoreBoardTv.setText(String.valueOf(scoreBoard));
            totalWinTv.setText(String.valueOf(totalWin));

            welComeLL.setVisibility(View.GONE);
            winnerLL.setVisibility(View.VISIBLE);
            alertLL.setVisibility(View.GONE);
            loserLL.setVisibility(View.GONE);
            startLL.setVisibility(View.GONE);
            timeUpLL.setVisibility(View.GONE);
        } else if (userNumber < randomNumber) {
            score = 0;
            totalLost = totalLost + 1;

            scoreTv.setText(String.valueOf(score));
            lostTv.setText(String.valueOf(totalLost));

            welComeLL.setVisibility(View.GONE);
            winnerLL.setVisibility(View.GONE);
            alertLL.setVisibility(View.GONE);
            loserLL.setVisibility(View.VISIBLE);
            timeUpLL.setVisibility(View.GONE);
            startLL.setVisibility(View.GONE);
            loserTv.setText("Lower");
            loserTv.setTextColor(getResources().getColor(R.color.purple));
        } else if (userNumber > randomNumber) {
            score = 0;
            totalLost = totalLost + 1;
            lostTv.setText(String.valueOf(totalLost));
            scoreTv.setText(String.valueOf(score));

            welComeLL.setVisibility(View.GONE);
            winnerLL.setVisibility(View.GONE);
            alertLL.setVisibility(View.GONE);
            loserLL.setVisibility(View.VISIBLE);
            startLL.setVisibility(View.GONE);
            timeUpLL.setVisibility(View.GONE);
            loserTv.setText("Higher");
            loserTv.setTextColor(getResources().getColor(R.color.orange));
        }
        totalPlayTv.setText(String.valueOf(totalPlay));
        lastGuessTv.setText(String.valueOf(userNumber));
        inputEdt.setText("");

    }

    private void randomNumberGenerate() {
        random = new Random();
        randomNumber = random.nextInt(10);
        inputEdt.setText("");
    }

    private void startTimer() {
        countDownTimer = new CountDownTimer(mLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mLeftInMillis = millisUntilFinished;
                updateTimer();
            }

            @Override
            public void onFinish() {
                timeFinish = false;
                inputEdt.setText("");
                scoreTv.setText("0");
                playBtn.setText("Play");
                timerTv.setTextColor(getResources().getColor(R.color.white));
                timerTv.setText("00:00");

                welComeLL.setVisibility(View.GONE);
                winnerLL.setVisibility(View.GONE);
                loserLL.setVisibility(View.GONE);
                timeUpLL.setVisibility(View.VISIBLE);
                alertLL.setVisibility(View.GONE);
                startLL.setVisibility(View.GONE);
                timeUpTv.setText("Click Reset");
                timeUpTv.setTextColor(getResources().getColor(R.color.white));
            }
        }.start();
        timeFinish = true;
        playBtn.setText("Pause");

        welComeLL.setVisibility(View.GONE);
        winnerLL.setVisibility(View.GONE);
        loserLL.setVisibility(View.GONE);
        timeUpLL.setVisibility(View.GONE);
        alertLL.setVisibility(View.GONE);
        startLL.setVisibility(View.VISIBLE);
    }

    private void pauseTimer() {
        countDownTimer.cancel();
        timeFinish = false;
        playBtn.setText("Play");

        welComeLL.setVisibility(View.GONE);
        winnerLL.setVisibility(View.GONE);
        loserLL.setVisibility(View.GONE);
        timeUpLL.setVisibility(View.GONE);
        alertLL.setVisibility(View.VISIBLE);
        startLL.setVisibility(View.GONE);
        alertTv.setText("Game Pause");
        alertTv.setTextColor(getResources().getColor(R.color.white));
    }

    private void updateTimer() {
        int minutes = (int) (mLeftInMillis / 1000) / 60;
        int seconds = (int) (mLeftInMillis / 1000) % 60;
        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);

        if (mLeftInMillis <= 20000) {
            timerTv.setTextColor(getResources().getColor(R.color.red_1));
        } else {
            timerTv.setTextColor(getResources().getColor(R.color.white));
        }
        timerTv.setText(timeLeftFormatted);
    }

    private void reSet() {
        score = 0;
        totalPlay = 0;
        totalWin = 0;
        totalLost = 0;
        scoreTv.setText("0");
        lastGuessTv.setText("0");
        lostTv.setText("0");
        totalPlayTv.setText("0");
        totalWinTv.setText("0");
        scoreBoardTv.setText("0");

        welComeLL.setVisibility(View.VISIBLE);
        winnerLL.setVisibility(View.GONE);
        loserLL.setVisibility(View.GONE);
        timeUpLL.setVisibility(View.GONE);
        startLL.setVisibility(View.GONE);
        welComeTv.setText("Click Play");
        welComeTv.setTextColor(getResources().getColor(R.color.white));
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you want to Exit this app?");
        builder.setCancelable(false);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MainActivity.super.onBackPressed();
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        }).show();

    }
}