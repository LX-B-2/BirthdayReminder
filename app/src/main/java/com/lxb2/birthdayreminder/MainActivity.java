package com.lxb2.birthdayreminder;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Bundle;
import android.transition.ChangeBounds;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

public class MainActivity extends AppCompatActivity {

    private static Point screenSize;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // get screen stats (mainly size)
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        screenSize = new Point(metrics.widthPixels, metrics.heightPixels);

        // call necessary setup functions
        createNotificationChannel();
        addReminderAt(System.currentTimeMillis());
    }


    /**
     * Sets up the reminder functionality.
     * @param time The desired dateTime for the notification in the millisecond format.
     */
    private void addReminderAt(long time) {
        findViewById(R.id.entry_list_scroll).setOnClickListener(v -> {
            Toast.makeText(this, "Timer added.", Toast.LENGTH_SHORT).show();        // todo: replace hard coded string with string resource

            Intent intent = new Intent(MainActivity.this, ReminderBroadcast.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

            alarmManager.set(AlarmManager.RTC_WAKEUP,
                    time,
                    pendingIntent);
        });
    }


    /**
     * Creates the necessary notification channel (necessary since API 26 or something)
     */
    private void createNotificationChannel() {
        CharSequence name = "BossChannel";
        String description = "Channel for the boss";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel("boss", name, importance);
        channel.setDescription(description);

        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }


    /**
     * Creates the effects of the bottom menu bar and opens the appropriate activity.
     * @param view The View this effect is called upon
     */
    public void clickEffect(View view) {
        if (view instanceof TextView) {
            // setup moving transition
            Transition transition = new ChangeBounds();
            TransitionManager.beginDelayedTransition(findViewById(R.id.main_layout_root), transition);

            // make the text of the selected card bold and colored
            if (view.getId() == R.id.button_list) {
                ((TextView) view).setTextColor(getColor(R.color.design_default_color_primary));
                ((TextView) view).setTypeface(null, Typeface.BOLD);
                ((TextView) findViewById(R.id.button_calendar)).setTextColor(getColor(R.color.text_grey));
                ((TextView) findViewById(R.id.button_calendar)).setTypeface(null, Typeface.NORMAL);
                ((TextView) findViewById(R.id.button_settings)).setTextColor(getColor(R.color.text_grey));
                ((TextView) findViewById(R.id.button_settings)).setTypeface(null, Typeface.NORMAL);
            }
            else if (view.getId() == R.id.button_calendar) {
                ((TextView) view).setTextColor(getColor(R.color.design_default_color_primary));
                ((TextView) view).setTypeface(null, Typeface.BOLD);
                ((TextView) findViewById(R.id.button_list)).setTextColor(getColor(R.color.text_grey));
                ((TextView) findViewById(R.id.button_list)).setTypeface(null, Typeface.NORMAL);

                ((TextView) findViewById(R.id.button_settings)).setTextColor(getColor(R.color.text_grey));
                ((TextView) findViewById(R.id.button_settings)).setTypeface(null, Typeface.NORMAL);
            }
            else if (view.getId() == R.id.button_settings) {
                ((TextView) view).setTextColor(getColor(R.color.design_default_color_primary));
                ((TextView) view).setTypeface(null, Typeface.BOLD);
                ((TextView) findViewById(R.id.button_calendar)).setTextColor(getColor(R.color.text_grey));
                ((TextView) findViewById(R.id.button_calendar)).setTypeface(null, Typeface.NORMAL);
                ((TextView) findViewById(R.id.button_list)).setTextColor(getColor(R.color.text_grey));
                ((TextView) findViewById(R.id.button_list)).setTypeface(null, Typeface.NORMAL);
            }

            // make the changes to the layout which will be applied with the moving transition specified above
            ConstraintLayout root_layout = (ConstraintLayout) findViewById(R.id.main_layout_root);
            ConstraintSet constraintSet = new ConstraintSet();
            constraintSet.clone(root_layout);
            int target_id = view.getId();
            constraintSet.connect(R.id.animated_bar_bottom, ConstraintSet.END, target_id, ConstraintSet.END, 0);
            constraintSet.connect(R.id.animated_bar_bottom, ConstraintSet.START, target_id, ConstraintSet.START, 0);
            constraintSet.applyTo(root_layout);

            // todo: open the right activity
        }
    }
}
