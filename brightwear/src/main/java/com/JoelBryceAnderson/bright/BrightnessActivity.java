package com.JoelBryceAnderson.bright;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.Locale;

public class BrightnessActivity extends Activity {

    private ImageView closeButton;
    private TextView title;
    private SeekBar seekBar;
    private int position;
    private ImageView percentageIndicatorFab;
    private FrameLayout percentageIndicatorWhole;
    private TextView percentageIndicatorText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brightness);
        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                closeButton = (ImageView) stub.findViewById(R.id.close_button);
                title = (TextView) stub.findViewById(R.id.brightness_title);

                percentageIndicatorFab = (ImageView)
                        stub.findViewById(R.id.percentage_indicator_fab);
                percentageIndicatorWhole = (FrameLayout)
                        stub.findViewById(R.id.percentage_indicator_whole);
                percentageIndicatorText = (TextView)
                        stub.findViewById(R.id.percentage_indicator_text);

                percentageIndicatorWhole.setScaleX(0);
                percentageIndicatorWhole.setScaleY(0);

                seekBar = (SeekBar) stub.findViewById(R.id.brightness_bar);
                seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        float percentage = (progress * 100.0f) / 250;
                        int intPercentage = (int) percentage;
                        if (intPercentage == 0) {
                            intPercentage += 1;
                        }
                        percentageIndicatorText
                                .setText(String.format(Locale.getDefault(), "%d%%", intPercentage));


                        int floatingPosition = (int) (seekBar.getX()
                                + seekBar.getThumbOffset() / 2
                                + (seekBar).getThumb().getBounds().exactCenterX());
                        percentageIndicatorWhole.setX(floatingPosition
                                - (seekBar.getPaddingLeft()));
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                        percentageIndicatorWhole.animate().scaleX(1).scaleY(1).setDuration(200).start();
                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        percentageIndicatorWhole.animate().scaleX(0).scaleY(0).setDuration(200).start();
                    }
                });
                title.setText(getIntent().getStringExtra("groupName"));
                position = getIntent().getIntExtra("position", 1);
                closeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent returnIntent = new Intent();
                        returnIntent.putExtra("brightness", seekBar.getProgress());
                        returnIntent.putExtra("position", position);
                        setResult(Activity.RESULT_OK,returnIntent);
                        BrightnessActivity.this.finish();
                        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                    }
                });
            }
        });
    }
}
