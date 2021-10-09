package com.lxb2.birthdayreminder;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.transition.ChangeBounds;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private static DisplayMetrics displayMetrics;
    private ArrayList<Reminder> reminders;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // initiate variables
        reminders = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            reminders.add(new Reminder(Calendar.getInstance(), "Name " + i));
        }

        displayMetrics = getResources().getDisplayMetrics();

        setupListView();

        // call necessary setup functions
        createNotificationChannel();
    }


    /**
     * Sets up the reminder functionality.
     * @param time The desired dateTime for the notification in the millisecond format.
     */
    private void setReminderAt(long time) {
        Toast.makeText(this, "Timer added.", Toast.LENGTH_SHORT).show();        // todo: replace hard coded string with string resource

        Intent intent = new Intent(MainActivity.this, ReminderBroadcast.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        alarmManager.set(AlarmManager.RTC_WAKEUP, time, pendingIntent);
    }


    /**
     * Sets up the main activity for the list view.
     */
    private void setupListView() {
        setContentView(R.layout.activity_main);

        LinearLayout linearLayout = findViewById(R.id.entry_list);

        for (int i = 0; i < reminders.size(); i++) {
            LinearLayout row_wrapper = new LinearLayout(this);
            row_wrapper.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            row_wrapper.setPadding(dp_to_px(15), dp_to_px(15), dp_to_px(15), dp_to_px(15));
            row_wrapper.setGravity(Gravity.CENTER_VERTICAL);

            // get the root constraint layout
            ConstraintLayout root_layout = findViewById(R.id.main_layout_root);
            ConstraintSet constraintSet = new ConstraintSet();
            constraintSet.clone(root_layout);

            TextView clock = new TextView(this);
            clock.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0.0f));
            clock.setText(String.format("%1$td. %1$tb", reminders.get(i).getCalendar()));
            clock.setTextColor(getColor(R.color.text_grey));
            clock.setTextSize(TypedValue.COMPLEX_UNIT_SP, 40);
            row_wrapper.addView(clock);

            TextView textView = new TextView(this);
            textView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f));
            textView.setText(reminders.get(i).getName());
            textView.setGravity(Gravity.CENTER_HORIZONTAL);
            textView.setTextColor(getColor(R.color.text_grey));
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
            row_wrapper.addView(textView);


            ImageView edit_icon = new ImageView(this);
            edit_icon.setLayoutParams(new LinearLayout.LayoutParams(dp_to_px(25), dp_to_px(25), 0.0f));
            edit_icon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.settings_icon));
            edit_icon.setOnClickListener(v -> setReminderAt(System.currentTimeMillis()));
            row_wrapper.addView(edit_icon);

            linearLayout.addView(row_wrapper);
        }
    }


    /**
     * Opens the menu for
     */
    private void openReminderConfig() {

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
            ConstraintLayout root_layout = findViewById(R.id.main_layout_root);
            ConstraintSet constraintSet = new ConstraintSet();
            constraintSet.clone(root_layout);
            int target_id = view.getId();
            constraintSet.connect(R.id.animated_bar_bottom, ConstraintSet.END, target_id, ConstraintSet.END, 0);
            constraintSet.connect(R.id.animated_bar_bottom, ConstraintSet.START, target_id, ConstraintSet.START, 0);
            constraintSet.applyTo(root_layout);

            // todo: open the right activity
        }
    }


    /**
     * Converts given dp to pixels.
     * @param dp Device independent pixels.
     * @return Given dp converted to px.
     */
    public static int dp_to_px(int dp) {
        final float scale = displayMetrics.density;
        return  (int) (dp * scale + 0.5f);
    }
}
