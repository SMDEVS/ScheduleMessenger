package com.example.schedulemessenger;

import android.Manifest;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.schedulemessenger.databinding.FragmentSmsScheduleBinding;

import java.util.Calendar;

public class SmsScheduleFragment extends Fragment {

    private static final String TAG = "In SmsScheduleFragment";
    private FragmentSmsScheduleBinding smsScheduleBinding;

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
        ComponentName componentName = new ComponentName(getContext(), SmsJobService.class);
        JobInfo jobInfo = new JobInfo.Builder(123, componentName)
                .setRequiresDeviceIdle(false)
                .setRequiresCharging(false)
                .setPersisted(true)
                .setMinimumLatency(1000)
                .build();
        JobScheduler jobScheduler = (JobScheduler) getActivity().getSystemService(Context.JOB_SCHEDULER_SERVICE);
        int resultCode = jobScheduler.schedule(jobInfo);

        if(resultCode == JobScheduler.RESULT_SUCCESS) {
            Log.v(TAG, "Job was successfully scheduled.");
        } else {
            Log.v(TAG, "Job was unsuccessfully scheduled.");
        }
    }

    //To allow us to take input in necessary format for time
    private void timeInputHandler() {
        Calendar currentTime = Calendar.getInstance();

        int currentHour = currentTime.get(Calendar.HOUR);
        int currentMinutes = currentTime.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                Toast.makeText(getContext(), "Time has been set!", Toast.LENGTH_LONG).show();
            }
        }, currentHour, currentMinutes, true);

        timePickerDialog.show();
    }

    //To allow us to take input in necessary format for a date
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void dateInputHandler() {
        Calendar currentDate = Calendar.getInstance();

        int currentYear = currentDate.get(Calendar.YEAR);
        int currentMonth = currentDate.get(Calendar.MONTH);
        int currentDay = currentDate.get(Calendar.DATE);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Toast.makeText(getContext(), "Date has been set!", Toast.LENGTH_LONG).show();
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