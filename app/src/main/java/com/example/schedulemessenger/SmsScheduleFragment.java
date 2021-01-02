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

import android.provider.Settings;
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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.schedulemessenger.Model.Message;
import com.example.schedulemessenger.ViewModel.MessageViewModel;
import com.example.schedulemessenger.databinding.FragmentSmsScheduleBinding;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.content.Context.ACCESSIBILITY_SERVICE;
import static android.content.Context.ALARM_SERVICE;

public class SmsScheduleFragment extends Fragment {

    private ArrayList<Message> mMessages =new ArrayList<>();

    private static final String TAG = "In SmsScheduleFragment";
    private FragmentSmsScheduleBinding smsScheduleBinding;
    private String scheduledDateTime;

    private MessageViewModel messageViewModel;

    public SmsScheduleFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment, using ViewBinding
        smsScheduleBinding = FragmentSmsScheduleBinding.inflate(inflater, container, false);
        return smsScheduleBinding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

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

                Message message1 = new Message();
                message1.setPhoneNumber("1111111111");
                message1.setMessageType(1);
                message1.setMessageStatus("Pending");
                message1.setMessageText("Hello, this is Neha Binwal.");
                message1.setImageUri("");
                message1.setInstaUsername("");
                message1.setTimeInterval(600000);
                message1.setTimeString("1 JAN, 2021, 19:17");

                messageViewModel.insertMessage(message1);

                scheduleSmsJobService();

            }
        });

        smsScheduleBinding.goToHistoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_smsScheduleFragment_to_historyFragment);
            }
        });

    }

    private void scheduleSmsJobService() {
        Intent intent = new Intent(getActivity(), MyBroadcastReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), 1,
                intent, 0);
        long timeInterval = calculateTimeInterval()+System.currentTimeMillis();
        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, timeInterval, pendingIntent);

    }

    private long calculateTimeInterval() {

        String format = "MM/dd/yyyy hh:mm:ss a";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date currentDateObject = null;
        Date scheduledDateObject = null;
        Calendar calendar=Calendar.getInstance();
        int hour=calendar.get(Calendar.HOUR);
        int minute=calendar.get(Calendar.MINUTE);
        int year=calendar.get(Calendar.YEAR);
        int month=calendar.get(Calendar.MONTH);
        int day=calendar.get(Calendar.DATE);
        Calendar calendar1=Calendar.getInstance();
        calendar1.set(Calendar.HOUR,hour);
        calendar1.set(Calendar.MINUTE,minute);
        calendar1.set(Calendar.YEAR,year);
        calendar1.set(Calendar.MONTH,month);
        calendar1.set(Calendar.DATE,day);
        CharSequence charSequence= DateFormat.format("MM/dd/yyyy hh:mm:ss a",calendar1);

        try {
            currentDateObject=sdf.parse(charSequence.toString());
            scheduledDateObject=sdf.parse(scheduledDate+" "+ScheduledTime);
        } catch (ParseException e) {
            e.printStackTrace();

        }

        Toast.makeText(getContext(), scheduledDateTime, Toast.LENGTH_SHORT).show();


        // To obtain difference between scheduled time and current time, in milliseconds
        Log.d("HHHH",String.valueOf(scheduledDateObject.getTime() - currentDateObject.getTime()));
        return scheduledDateObject.getTime() - currentDateObject.getTime();
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

                Calendar calendar1=Calendar.getInstance();
                calendar1.set(Calendar.HOUR_OF_DAY,hourOfDay);
                calendar1.set(Calendar.MINUTE,minute);
                calendar1.set(Calendar.SECOND,00);
                CharSequence charSequence= DateFormat.format("hh:mm:ss a",calendar1);
                ScheduledTime=charSequence.toString();

            }
        }, currentHour, currentMinutes, true);

        timePickerDialog.show();
    }

    //To allow us to take input in necessary format for a date
    String scheduledDate,ScheduledTime;
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

                Calendar calendar1=Calendar.getInstance();
                calendar1.set(Calendar.YEAR,year);
                calendar1.set(Calendar.MONTH,month);
                calendar1.set(Calendar.DATE,dayOfMonth);
                CharSequence charSequence= DateFormat.format("MM/dd/yyyy",calendar1);
                scheduledDate=charSequence.toString();

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

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        messageViewModel= new ViewModelProvider(getActivity()).get(MessageViewModel.class);
        messageViewModel.getAllMessages().observe(getViewLifecycleOwner(), new Observer<List<Message>>() {
            @Override
            public void onChanged(List<Message> messages) {
                mMessages = (ArrayList<Message>) messages;
            }
        });
    }
}
