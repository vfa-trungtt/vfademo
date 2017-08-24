package com.asai24.golf.view;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;

/**
 * Created by CanNC on 11/11/15.
 */
public class OnDatePickerDialogKeyListener implements DialogInterface.OnKeyListener {

    @Override
    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {

        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            // Support for DatePickerDialog
            if (dialog instanceof DatePickerDialog) {
                View view = ((DatePickerDialog) dialog).getCurrentFocus();
                if (view != null) {
                    if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
                        int decrementId = Resources.getSystem().getIdentifier("decrement", "id", "android");
                        int incrementId = Resources.getSystem().getIdentifier("increment", "id", "android");
                        int viewId = view.getId();
                        if (viewId == decrementId || viewId == incrementId) {
                            view.performClick();
                            return true;
                        }
                    } else if (view instanceof EditText) {
                        int inputId = Resources.getSystem().getIdentifier("numberpicker_input", "id", "android");
                        if (view.getId() == inputId) {

                            int year = Resources.getSystem().getIdentifier("year", "id", "android");
                            int month = Resources.getSystem().getIdentifier("month", "id", "android");
                            int day = Resources.getSystem().getIdentifier("day", "id", "android");

                            EditText input = (EditText) view;
                            int end = input.getSelectionEnd();
                            int start = input.getSelectionStart();

                            if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
                                if (end == start && start == 0) {

                                    View parent = (View) input.getParent();
                                    int parentId = parent.getId();
                                    if (parentId == month) {
                                        requestFocusInput((DatePickerDialog) dialog, year, inputId);
                                    } else if (parentId == day) {
                                        requestFocusInput((DatePickerDialog) dialog, month, inputId);
                                    }
                                    return true;
                                }
                            } else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
                                int length = input.length();
                                if (end == start && start == length) {

                                    View parent = (View) input.getParent();
                                    int parentId = parent.getId();
                                    if (parentId == year) {
                                        requestFocusInput((DatePickerDialog) dialog, month, inputId);
                                    } else if (parentId == month) {
                                        requestFocusInput((DatePickerDialog) dialog, day, inputId);
                                    }
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    private void requestFocusInput(DatePickerDialog root, int parentId, int inputId) {

        try {
            View parent = root.findViewById(parentId);
            EditText input = (EditText) parent.findViewById(inputId);
            input.requestFocus();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
