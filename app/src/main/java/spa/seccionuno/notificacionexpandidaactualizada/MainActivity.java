package spa.seccionuno.notificacionexpandidaactualizada;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RemoteViews;

import java.text.DateFormat;
import java.util.Date;


public class MainActivity extends ActionBarActivity {
    NotificationManager nm;
    Notification notification;
    NotificationCompat.Builder builder;
    RemoteViews contentView;

    int notifyID=1;
    private String TAG="Error : ";
    String textoActual="";
    String hora="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClick(View v){
        creaNotificacion();
    }

    private void setNotification(String textoVista,String textoHora){
        contentView.setTextViewText(R.id.textView,textoVista);
        contentView.setTextViewText(R.id.txtHora,textoHora);

        notification.contentView=contentView;
    }
    private void setNotificationExpandida(String textoVista,String textoHora){
        if(Build.VERSION.SDK_INT>=16){
            RemoteViews expandedView=new RemoteViews(getPackageName(),R.layout.notificacion_expandida);
            expandedView.setTextViewText(R.id.txtMensajes,textoVista);
            expandedView.setTextViewText(R.id.txtHora,textoHora);

            notification.bigContentView=expandedView;
        }
    }

    private void creaNotificacion(){
        builder= new NotificationCompat.Builder(this);

        Intent i=new Intent(this,MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent intent=PendingIntent.getActivity(this,0,i,PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(intent);

        builder.setTicker(getResources().getString(R.string.etiqueta_notificacion));
        builder.setSmallIcon(R.drawable.ic_stat_custom);
        builder.setAutoCancel(true);

        notification=builder.build();

        contentView=new RemoteViews(getPackageName(),R.layout.notificacion);

        textoActual="0 nuevos mensajes.";
        hora= DateFormat.getTimeInstance().format(new Date()).toString();
        setNotification(textoActual,hora);
        setNotificationExpandida(textoActual, hora);

        nm=(NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        nm.notify(notifyID,notification);

        //Creamos un hilo
        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        int incr;
                        String textoActualExpandido="";
                        for(incr=0;incr<=6;incr+=1){
                            textoActual=incr+ " nuevos mensajes.";
                            textoActualExpandido=textoActualExpandido + " mensaje " + incr + " : detalle mensaje "
                                    + incr + "\n";
                            hora=DateFormat.getTimeInstance().format(new Date()).toString();

                            setNotification(textoActual,hora);
                            setNotificationExpandida(textoActualExpandido,hora);

                            nm.notify(notifyID,notification);
                            try{
                                Thread.sleep(2*1000);
                            }catch (InterruptedException e){
                                Log.d(TAG," sleep error");
                            }
                        }
                    }
                }
        ).start();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
