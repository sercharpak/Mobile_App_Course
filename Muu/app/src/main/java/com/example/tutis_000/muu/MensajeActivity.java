package com.example.tutis_000.muu;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.messenger.MessengerThreadParams;
import com.facebook.messenger.MessengerUtils;
import com.facebook.messenger.ShareToMessengerParams;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.MessageDialog;

import Mundo.Participante;
import Mundo.Muu;

/**
 * Created by tutis_000 on 02/03/2016.
 */
public class MensajeActivity extends Activity {
    Participante par=null;
    private String numeroTelefonico;



    //For the Messenger
    //Facebook SDK 4.xxx
    private static final int REQUEST_CODE_SHARE_TO_MESSENGER = 1;
    //private Toolbar mToolbar;
    private View mMessengerButton;
    private MessengerThreadParams mThreadParams;
    private boolean mPicking;
    CallbackManager callbackManager;



    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Initialize the Facebook Messenger
        FacebookSdk.sdkInitialize(this.getApplicationContext());

        setContentView(R.layout.activity_mensaje);

        Intent i = getIntent();
        String nombre = i.getStringExtra("par");
        Log.d("impr:","participante "+ nombre);

        par = Muu.darInstancia().darParticipante(nombre);

        Log.d("impr:","participante "+ par);

        if (par != null) {
            TextView txtNombre = (TextView) findViewById(R.id.txtNombreP);
            txtNombre.setText(nombre);
            numeroTelefonico=par.getNumero();
            Log.d("impr:", "numero "+numeroTelefonico);
        }

        callbackManager = CallbackManager.Factory.create();
        //mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mMessengerButton = findViewById(R.id.buttonFacebook);
        //xml sin modificar   <include layout="@layout/messenger_button_send_blue_large" />
        // Otra opcion <include layout="@layout/messenger_button_send_blue_round" />

        //mToolbar.setTitle(R.string.app_name);

        // If we received Intent.ACTION_PICK from Messenger, we were launched from a composer shortcut
        // or the reply flow.
        Intent intent = getIntent();
        if (Intent.ACTION_PICK.equals(intent.getAction())) {
            mThreadParams = MessengerUtils.getMessengerThreadParamsForIntent(intent);
            mPicking = true;

            // Note, if mThreadParams is non-null, it means the activity was launched from Messenger.
            // It will contain the metadata associated with the original content, if there was content.
        }

        mMessengerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onMessengerButtonClicked();
            }
        });

    }

    //For the Messenger
    private void onMessengerButtonClicked() {

        //Uri uri = Uri.parse("android.resource://com.numetriclabz.messengerdemo/" + R.drawable.com_facebook_send_button_icon);
        String mensaje = " ";
        // Create the parameters for what we want to send to Messenger.
        if (par != null) {
            mensaje = "Debes a la vaca: " + par.getDebe();
        }
        //Test
        else{
            mensaje = "Esto es una prueba";
        }
        String contentTitle = "Muu app (Recordatorio)";
        String contentDescription = mensaje;
        String url = "wwww.google.com";
        try {
            Log.d("Messenger", "Esta a punto de enviar el mensaje.");

            ShareLinkContent shareLinkContent = new ShareLinkContent.Builder()
                    .setContentTitle(contentTitle)
                    .setContentDescription(contentDescription)
                    .setContentUrl(Uri.parse(url))
                    .build();
            // shareLinkContentBuilder.setImageUrl(Uri.parse(imageUrl));
            MessageDialog messageDialog = new MessageDialog(this);
            //messageDialog.registerCallback(callbackManager, null);
            messageDialog.show(shareLinkContent);
            Log.d("Messenger", "Envio el mensaje.");
        }
        catch (Exception e){
            e.printStackTrace();
        }
        /*
         //Messenger Utils DOES NOT support sharing text.
        ShareToMessengerParams shareToMessengerParams =
                ShareToMessengerParams.newBuilder(uri, "image/jpeg")
                        .setMetaData("{ \"image\" : \"tree\" }")
                        .build();
        if (mPicking) {
            // If we were launched from Messenger, we call MessengerUtils.finishShareToMessenger to return
            // the coif (par != null) {ntent to Messenger.
            MessengerUtils.finishShareToMessenger(this, shareToMessengerParams);
        }
        else {
            // Otherwise, we were launched directly (for example, user clicked the launcher icon). We
            // initiate the broadcast flow in Messenger. If Messenger is not installed or Messenger needs
            // to be upgraded, this will direct the user to the play store.
            MessengerUtils.shareToMessenger(
                    this,
                    REQUEST_CODE_SHARE_TO_MESSENGER,
                    shareToMessengerParams);
        }
        */
    }

    public void enviarSMS(View v) {

        if( numeroTelefonico!= null ) {



            String mensaje = "Debes a la vaca: " + par.getDebe();
            SmsManager manejador = SmsManager.getDefault();
            try {
                manejador.sendTextMessage(numeroTelefonico, null, mensaje, null, null);
                Log.d("SMS", "Se envió el mensaje correctamente");
            } catch (Exception e) {
                Log.d("SMS", "No se envió el mensaje, excepción: " + e.getMessage());
            }

        }
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

        Log.e("Test", "Yes!");
    }


}
