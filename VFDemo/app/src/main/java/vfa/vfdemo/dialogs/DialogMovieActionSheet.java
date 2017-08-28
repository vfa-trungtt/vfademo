package vfa.vfdemo.dialogs;

import android.app.Dialog;
import android.content.Context;
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


public class DialogMovieActionSheet extends Dialog{
    public static final int ACTION_SHEET_CAMERA = 1;
    public static final int ACTION_SHEET_GALLERY = 2;
    public static final int ACTION_SHEET_CANCEL = 3;

    private int _selectAction = 3;



    private DialogActionSheet.DialogActionSheetListener _listener;

    public DialogMovieActionSheet(@NonNull Context context) {
        super(context);
    }

    public DialogMovieActionSheet(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }

    protected DialogMovieActionSheet(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
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
                _selectAction = ACTION_SHEET_CANCEL;
                if(_listener != null) _listener.onSelectAction(_selectAction);
                dismiss();
            }
        });

        findViewById(R.id.button_camera).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _selectAction = ACTION_SHEET_CAMERA;
                if(_listener != null) _listener.onSelectAction(_selectAction);
                dismiss();
            }
        });

        findViewById(R.id.button_gallery).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _selectAction = ACTION_SHEET_GALLERY;
                if(_listener != null) _listener.onSelectAction(_selectAction);
                dismiss();
            }
        });
    }

    public void show(DialogActionSheet.DialogActionSheetListener listener){
        _listener = listener;
        show();
    }

}
