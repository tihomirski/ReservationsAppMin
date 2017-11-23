package broadcastReceivers;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.tivachkov.reservations.reservations.R;

import helpers.DatabaseHandler;

import static android.content.Context.NOTIFICATION_SERVICE;

public class RemoveReservations extends BroadcastReceiver {


    Intent emailIntent;
    private Thread mPollThread;
    private Context mContext;
    private int alarmID;

    public void onReceive(Context context, Intent intent) {
        Log.d("=-=-=-=-=-=-=-=-=", "onReceive().");
        emailIntent = intent;
        this.mContext = context;
        removeReservation(context);
        alarmID = intent.getIntExtra("alarmID", 0);
    }



    private void removeReservation(Context context) {

        //Needs a separate thread. Otherwise throws exception because there are networks tasks in main GUI thread
        mPollThread = new Thread() {
            public void run() {

                    try {
                        DatabaseHandler dbHelper = new DatabaseHandler(mContext);
                        dbHelper.deleteAllReservations(alarmID);
                        //dbHelper.deleteAlarm(alarmID);
                        sendNotification(mContext);
                        Log.e("= = = = = = = =","RESERVATIONS DELETED!");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

            }
        };
        mPollThread.start();

    }

    private void sendNotification(Context context) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);

        Notification notification  = new Notification.Builder(mContext)
                .setContentTitle("Reservations deleted")
                .setContentText("All reservations have been deleted after 15 minutes")
                .setSmallIcon(R.drawable.quandoo_logo_transparent_36px).build();
        notificationManager.notify(0, notification);
    }

}
