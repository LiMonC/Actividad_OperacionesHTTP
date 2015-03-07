package com.macc.mobile.retrofit.Objetos;

import android.app.Activity;
import android.app.ProgressDialog;

/**
 * Created by mrodriguez on 06/03/2015.
 */
public class Utilidades {

    public static ProgressDialog loading;
    public static void ShowProgress(Activity acti){
        if(Utilidades.loading != null){
            if(!Utilidades.loading.isShowing()){
                Utilidades.loading = ProgressDialog.show( acti,"", "Por favor, espere..", true);
                Utilidades.loading.show();
            }
        }else{
            Utilidades.loading = ProgressDialog.show(acti, "", "Por favor, espere..", true);
            Utilidades.loading.show();
        }
    }

    public static void DismissProgress(){
		/*if(Utilidades.loading != null){
			if(Utilidades.loading.isShowing()){
				Utilidades.loading.dismiss();
			}
		}*/
        try {
            if ((Utilidades.loading != null) && Utilidades.loading.isShowing()) {
                Utilidades.loading.dismiss();
            }
        } catch (final IllegalArgumentException e) {
            // Handle or log or ignore
        } catch (final Exception e) {
            // Handle or log or ignore
        } finally {
            Utilidades.loading = null;
        }

    }
}
