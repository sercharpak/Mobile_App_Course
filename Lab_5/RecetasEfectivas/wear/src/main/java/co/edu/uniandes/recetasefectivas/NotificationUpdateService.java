/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package co.edu.uniandes.recetasefectivas;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.WearableListenerService;

import org.json.JSONException;
import org.json.JSONObject;

import static com.google.android.gms.wearable.PutDataRequest.WEAR_URI_SCHEME;

/**
 * A {@link com.google.android.gms.wearable.WearableListenerService} that will be invoked when a
 * DataItem is added or deleted. The creation of a new DataItem will be interpreted as a request to
 * create a new notification and the removal of that DataItem is interpreted as a request to
 * dismiss that notification.
 */
public class NotificationUpdateService extends WearableListenerService
        implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        ResultCallback<DataApi.DeleteDataItemsResult> {

    private static final String TAG = "NotificationUpdate";
    private GoogleApiClient mGoogleApiClient;

    @Override
    public void onCreate() {
        super.onCreate();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mGoogleApiClient.connect();
        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public void onDestroy(){
        mGoogleApiClient.disconnect();
        super.onDestroy();
    }
    @Override
    public void onDataChanged(DataEventBuffer dataEvents) {
        for (DataEvent dataEvent : dataEvents) {
            Log.d(TAG, "Cambio");
            if (dataEvent.getType() == DataEvent.TYPE_CHANGED) {
                Log.d(TAG, "Se creó");
                DataMap dataMap = DataMapItem.fromDataItem(dataEvent.getDataItem()).getDataMap();
                String content = dataMap.getString(Constants.KEY_CONTENT);
                String title = dataMap.getString(Constants.KEY_TITLE);
                buildWearableOnlyNotification(title, content);

            } else if (dataEvent.getType() == DataEvent.TYPE_DELETED) {
                if (Log.isLoggable(TAG, Log.DEBUG)) {
                    Log.d(TAG, "DataItem deleted: " + dataEvent.getDataItem().getUri().getPath());
                }
                if (Constants.BOTH_PATH.equals(dataEvent.getDataItem().getUri().getPath())) {
                    // Dismiss the corresponding notification
                    ((NotificationManager) getSystemService(NOTIFICATION_SERVICE))
                            .cancel(Constants.WATCH_ONLY_ID);
                }
            }
        }
    }

    /**
     * Builds a simple notification on the wearable.
     */
    private void buildWearableOnlyNotification(String title, String content) {
        Notification.Builder builder = new Notification.Builder(this)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle(title)
                .setContentText(content);

        ((NotificationManager) getSystemService(NOTIFICATION_SERVICE))
                .notify(Constants.WATCH_ONLY_ID, builder.build());
    }

    @Override
    public void onConnected(Bundle bundle) {
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
    }

    @Override
    public void onResult(DataApi.DeleteDataItemsResult deleteDataItemsResult) {

      //  mGoogleApiClient.disconnect();
    }

    public void onMessageReceived(MessageEvent messageEvent) {
        super.onMessageReceived(messageEvent);
        String message = new String(messageEvent.getData());

        if(!message.equals("") || !message.isEmpty()){
            Log.d("Wear", "JSON Received: " + message);
            try {
                JSONObject json = new JSONObject(message);
                String action = json.getString("action");
                Log.d("Wear","La accion es: "+action);

                //si la accion tiene algo que ver con "realizar mercado"
                if((action.equalsIgnoreCase(Constants.ACTION_PROCESS_PRODUCT)
                        || action.equalsIgnoreCase(Constants.ACTION_EMPTY_LIST)
                        || action.equalsIgnoreCase(Constants.ACTION_BUY_NOW))
                        )
                {

                    Intent intent = new Intent("my-event");
                    intent.putExtra(Constants.ARGUMENTS,json.toString());
                    LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
                }
            }catch (JSONException exception){

            }


        }
    }


}
