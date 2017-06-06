package com.bojinzhang.android.Util;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.bojinzhang.android.Business.ShoppingCartBuss;
import com.bojinzhang.android.Interface.AlertDialogReturnInterface;
import com.bojinzhang.android.restaurantmenu.R;

/**
 * Created by zhangbojin on 6/06/17.
 */

public class AlertDialogUtil {
    public static AlertDialog getClearAllAlertDialog(final Context context, final AlertDialogReturnInterface rInterface, String title, String info) {
        final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.show();
        Window window = alertDialog.getWindow();
        window.setContentView(R.layout.dialog_clear_all_items);

        TextView dialogTitle = (TextView) window.findViewById(R.id.dialog_clear_all_title);
        dialogTitle.setText(title);

        TextView message = (TextView) window.findViewById(R.id.dialog_clear_all_message);
        message.setText(info);

        Button cancelButton = (Button) window.findViewById(R.id.dialog_clear_all_cancel_button);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        Button confirmButton = (Button) window.findViewById(R.id.dialog_clear_all_confirm_button);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShoppingCartBuss.getInstance().clearItems();
                rInterface.RefreshActivity();
                alertDialog.dismiss();
            }
        });

        return alertDialog;
    }
}
