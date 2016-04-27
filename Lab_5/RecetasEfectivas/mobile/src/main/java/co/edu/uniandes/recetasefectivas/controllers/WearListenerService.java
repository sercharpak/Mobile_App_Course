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

package co.edu.uniandes.recetasefectivas.controllers;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.analytics.ecommerce.Product;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.WearableListenerService;
import com.google.android.gms.wearable.WearableStatusCodes;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collection;
import java.util.HashSet;

import co.edu.uniandes.recetasefectivas.helpers.Constants;
import co.edu.uniandes.recetasefectivas.mundo.CantidadIngrediente;
import co.edu.uniandes.recetasefectivas.mundo.RecetasEfectivas;

import static com.google.android.gms.wearable.PutDataRequest.WEAR_URI_SCHEME;

/**
 * A {@link com.google.android.gms.wearable.WearableListenerService} that is invoked when certain
 * notifications are dismissed from either the phone or watch.
 */
public class WearListenerService extends WearableListenerService
        implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        ResultCallback<DataApi.DeleteDataItemsResult> {

    private static final String TAG = "WearListenerService";
    private GoogleApiClient mGoogleApiClient;

    @Override
    public void onCreate() {
        super.onCreate();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        mGoogleApiClient.connect();

    }

    @Override
    public void onDataChanged(DataEventBuffer dataEvents) {
        for (DataEvent dataEvent : dataEvents) {
            if (dataEvent.getType() == DataEvent.TYPE_DELETED) {
                if (Constants.BOTH_PATH.equals(dataEvent.getDataItem().getUri().getPath())) {
                    // notification on the phone should be dismissed
                    NotificationManagerCompat.from(this).cancel(Constants.BOTH_ID);
                }
            }
        }
    }
    @Override
    public void onDestroy(){
        mGoogleApiClient.disconnect();
        super.onDestroy();
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        mGoogleApiClient.connect();

        return super.onStartCommand(intent, flags, startId);
    }


    @Override // ConnectionCallbacks
    public void onConnected(Bundle bundle) {
        Log.d("Mobile", "here");
        if(mGoogleApiClient.isConnected()) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    NodeApi.GetConnectedNodesResult nodes = Wearable.NodeApi.getConnectedNodes(mGoogleApiClient).await();
                    for (Node node : nodes.getNodes()) {
                        MessageApi.SendMessageResult result = Wearable.MessageApi.sendMessage(mGoogleApiClient, node.getId(), Constants.PATH, "Hello World".getBytes()).await();
                        if (!result.getStatus().isSuccess()) {
                            Log.e("test", "error");
                        } else {
                            Log.i("test", "success!! sent to: " + node.getDisplayName());
                        }
                    }
                }
            }).start();
        }
    }

    @Override // ConnectionCallbacks
    public void onConnectionSuspended(int i) {
    }

    @Override // OnConnectionFailedListener
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.e(TAG, "Failed to connect to the Google API client");
    }

    @Override // ResultCallback<DataApi.DeleteDataItemsResult>
    public void onResult(DataApi.DeleteDataItemsResult deleteDataItemsResult) {
        if (!deleteDataItemsResult.getStatus().isSuccess()) {
            Log.e(TAG, "dismissWearableNotification(): failed to delete DataItem");
        }
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        super.onMessageReceived(messageEvent);

        String message = new String(messageEvent.getData());
        Log.d("Mobile", "JSON Received: " + message);
        try {
            JSONObject json = new JSONObject(message);
            if (json.getString("action").equalsIgnoreCase(Constants.ACTION_BUY_NOW)) {
                Log.d("Mobile", "Enviar lista productos");
                RecetasEfectivas re = RecetasEfectivas.darInstancia();
                if (re.pendientes.size() > 0) {
                    Log.d("Mobile", "Enviar lista ");
                    sendProduct2Wear(re.pendientes.get(0));
                } else {
                    Log.d("Mobile", "Enviar vacio");
                    sendEmptyList2Wear();
                }

            }
            else if (json.getString("action").equalsIgnoreCase(Constants.ACTION_PROCESSED_PRODUCT)) {
                String id = json.getJSONObject("data").getString("id");
                String result = json.getJSONObject("data").getString("result");
                RecetasEfectivas re = RecetasEfectivas.darInstancia();
                CantidadIngrediente cI=re.findAndDelete(id);
                Log.d("Mobile", "Elemento "+id);
                if (cI != null) {

                    if (result.equalsIgnoreCase(Constants.STATUS_BUY)) {
                        Log.d("ADDED", "To bought -> " + id);
                        re.addBought(cI);

                    }
                    else if(result.equalsIgnoreCase(Constants.STATUS_MISS)) {
                        Log.d("ADDED", "To missing -> " + id);
                        re.addMissing(cI);
                    }
                    else
                    {
                        Log.d("ADDED","To end of products -> "+id);
                        re.addToBuy(cI);
                    }

                }

                if (re.pendientes.size() > 0) {
                    Log.d("Mobile", "Enviar lista ");
                    sendProduct2Wear(re.pendientes.get(0));
                } else {
                    Log.d("Mobile", "Enviar vacio");
                    sendEmptyList2Wear();
                }
            }
        }catch (JSONException exception){

        }
    }

    private void sendEmptyList2Wear() throws JSONException {
        JSONObject json = new JSONObject();
        json.put("action", Constants.ACTION_EMPTY_LIST);
        json.put("data", null);
        sendMessage(json.toString());
    }

    private void sendProduct2Wear(CantidadIngrediente p) throws JSONException {
        System.out.println("Got here");
        JSONObject json = new JSONObject();
        json.put("action", Constants.ACTION_PROCESS_PRODUCT);
        json.put("data", p.toJSON());
        Log.d("Mobile", "Sending from phone: " + json.toString());
        new SendMessage().execute(json.toString());
    }

    public void sendMessage(final String message) {
        PendingResult<NodeApi.GetConnectedNodesResult> nodes = Wearable.NodeApi.getConnectedNodes(mGoogleApiClient);
        Log.d("Mobile", "Sending: right here");
        nodes.setResultCallback(new ResultCallback<NodeApi.GetConnectedNodesResult>() {
            @Override
            public void onResult(NodeApi.GetConnectedNodesResult result) {
                Log.d("Mobile", ""+ result.getNodes().size());
                for (int i = 0; i < result.getNodes().size(); i++) {
                    Node node = result.getNodes().get(i);
                    Log.d("Mobile", "Here!! ");
                    PendingResult<MessageApi.SendMessageResult> messageResult = Wearable.MessageApi.sendMessage(mGoogleApiClient, node.getId(), Constants.PATH, message.getBytes());
                    messageResult.setResultCallback(new ResultCallback<MessageApi.SendMessageResult>() {
                        @Override
                        public void onResult(MessageApi.SendMessageResult sendMessageResult) {
                            Status status = sendMessageResult.getStatus();
                            Log.d("Mobile", "Status: " + status.toString());
                            if (status.getStatusCode() != WearableStatusCodes.SUCCESS) {
                                Log.d("Mobile", "Mensaje Enviado Satisfactoriamente");
                            }
                        }
                    });
                }
            }
        });
    }


    private class SendMessage extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... msgs) {
            String message= msgs[0];
            Log.e("Mobile", "Enviendo-> " + message);
            for(String id: getNodes()) {
                Log.e("Mobile", "aca-> ");
                MessageApi.SendMessageResult sendMessageResult=Wearable.MessageApi.sendMessage(mGoogleApiClient, id,
                        Constants.PATH, message.getBytes()).await();
                com.google.android.gms.common.api.Status status = sendMessageResult.getStatus();

                if (status.getStatusCode() == WearableStatusCodes.SUCCESS) {
                    Log.d("Mobile", "Se recibió el mensaje");
                }
            }

            return "Enviado";
        }

        protected void onProgressUpdate(Void... progress) {
        }

        protected void onPostExecute(String result) {
            Toast.makeText(getApplicationContext(), "Elemento enviado",
                    Toast.LENGTH_LONG).show();
        }


        private Collection<String> getNodes() {
            HashSet<String> results = new HashSet<String>();
            NodeApi.GetConnectedNodesResult nodes =
                    Wearable.NodeApi.getConnectedNodes(mGoogleApiClient).await();
            for (Node node : nodes.getNodes()) {
                results.add(node.getId());
            }
            Log.e("Mobile", "Enviendo-> " + results.size());
            return results;
        }
    }
}
