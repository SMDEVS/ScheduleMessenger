package com.example.schedulemessenger;

import android.Manifest;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.telephony.SmsManager;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.schedulemessenger.databinding.FragmentSmsScheduleBinding;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class SmsScheduleFragment extends Fragment {

    private static final String TAG = "In SmsScheduleFragment";
    private FragmentSmsScheduleBinding smsScheduleBinding;
    private String scheduledDateTime;

    public SmsScheduleFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment, using ViewBinding
        smsScheduleBinding = FragmentSmsScheduleBinding.inflate(inflater, container, false);
        View smsView = smsScheduleBinding.getRoot();
        return smsView;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        smsScheduleBinding.dateButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                dateInputHandler();
            }
        });

        smsScheduleBinding.timeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeInputHandler();
            }
        });

        smsScheduleBinding.sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Send button is responsive!",
                        Toast.LENGTH_SHORT).show();

                ActivityCompat.requestPermissions(getActivity(), new String[]
                        {Manifest.permission.SEND_SMS, Manifest.permission.READ_SMS}, PackageManager.PERMISSION_GRANTED);

                scheduleSmsJobService();

            }
        });

    }

    private void scheduleSmsJobService() {
        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(
                getActivity().ALARM_SERVICE);
        Intent intent = new Intent(getActivity(), MyBroadcastReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), 0,
                intent, 0);
        long timeInterval = System.currentTimeMillis() + calculateTimeInterval();
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, timeInterval, pendingIntent);

    }

    private long calculateTimeInterval() {

        String dateFormat = "MM/dd/yyyy HH:mm";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
        Date currentDateObject = null;
        Date scheduledDateObject = null;

        // To obtain number of millis passed, since epoch, for current date and time
        Calendar currDateTime = Calendar.getInstance();
        String currentDateTime = DateFormat.format(dateFormat, currDateTime).toString();
        try {
            currentDateObject = simpleDateFormat.parse(currentDateTime);
        } catch (ParseException e) {
            e.printStackTrace();

        }

        Toast.makeText(getContext(), scheduledDateTime, Toast.LENGTH_SHORT).show();

        // To obtain number of millis passed, since epoch, for scheduled date and time
        try {
            scheduledDateObject = simpleDateFormat.parse(scheduledDateTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // To obtain difference between scheduled time and current time, in milliseconds
        long timeInterval = scheduledDateObject.getTime() - currentDateObject.getTime();

        return timeInterval;
    }

    //To allow us to take input in necessary format for time
    private void timeInputHandler() {

        //To get current time in hours and minutes
        Calendar currentTime = Calendar.getInstance();
        int currentHour = currentTime.get(Calendar.HOUR);
        int currentMinutes = currentTime.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                Toast.makeText(getContext(), "Time has been set!", Toast.LENGTH_LONG).show();

                Calendar selectedTime = Calendar.getInstance();
                selectedTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                selectedTime.set(Calendar.MINUTE, minute);

                scheduledDateTime = scheduledDateTime + " " + DateFormat.format("HH:mm",
                        selectedTime).toString();

            }
        }, currentHour, currentMinutes, true);

        timePickerDialog.show();
    }

    //To allow us to take input in necessary format for a date
    private void dateInputHandler() {

        //To get current date
        Calendar currentDate = Calendar.getInstance();
        int currentYear = currentDate.get(Calendar.YEAR);
        int currentMonth = currentDate.get(Calendar.MONTH);
        int currentDay = currentDate.get(Calendar.DATE);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Toast.makeText(getContext(), "Date has been set!", Toast.LENGTH_LONG).show();

                Calendar selectedDate = Calendar.getInstance();
                selectedDate.set(Calendar.YEAR, year);
                selectedDate.set(Calendar.MONTH, month);
                selectedDate.set(Calendar.DATE, dayOfMonth);

                scheduledDateTime = DateFormat.format("MM/dd/yyyy", selectedDate).toString();

            }
        }, currentYear, currentMonth, currentDay);

        datePickerDialog.show();
    }

    //To avoid memory leaks
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        smsScheduleBinding = null;
    }

}
