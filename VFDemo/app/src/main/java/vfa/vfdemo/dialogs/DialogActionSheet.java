package vfa.vfdemo.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import vfa.vfdemo.R;
import vfa.vfdemo.utils.ViewHelper;


public class DialogActionSheet extends Dialog{
    public interface DialogActionSheetListener{
        public void onSelectAction(int action);
    }

    public class ActionSheetEntity{
        public int actionId;
        public String actionText;
    }

    private DialogActionSheetListener _listener;

    public DialogActionSheet(@NonNull Context context) {
        super(context);
    }

    public DialogActionSheet(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }

    protected DialogActionSheet(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_action_sheet);

        this.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        this.getWindow().setGravity(Gravity.CENTER);
        this.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);


        findViewById(R.id.buttonCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        findViewById(R.id.button_camera).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        findViewById(R.id.button_gallery).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    public void setActionList(){

    }

}
