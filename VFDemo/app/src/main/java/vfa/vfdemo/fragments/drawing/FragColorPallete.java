package vfa.vfdemo.fragments.drawing;

import android.graphics.Color;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import vfa.vfdemo.R;
import vn.hdisoft.hdilib.fragments.VFFragment;


/**
 * Created by Vitalify on 3/16/17.
 */

public class FragColorPallete extends VFFragment {

    SeekBar seekBarRed;
    SeekBar seekBarGreen;
    SeekBar seekBarBlue;
    SeekBar seekBarAlpha;

    EditText edtRedValue;
    EditText edtGreenValue;
    EditText edtBlueValue;
    EditText edtAlphaValue;

    EditText edtIntValue;
    EditText edtHexValue;

    TextView edtSelectColor;

    int redValue = 0;
    int greenValue = 0;
    int blueValue = 0;
    int alphaValue = 255;

    int selectColor = 0;
    @Override
    public int onGetRootLayoutId() {
        return R.layout.frag_color_pallete;
    }

    @Override
    public void onViewLoaded() {

        edtIntValue     = (EditText) rootView.findViewById(R.id.edtIntegerValue);
        edtHexValue     = (EditText) rootView.findViewById(R.id.edtHexValue);
        edtSelectColor  = (TextView) rootView.findViewById(R.id.edtSelectColor);

        edtRedValue     = (EditText) rootView.findViewById(R.id.edtRedValue);

        seekBarRed = (SeekBar) rootView.findViewById(R.id.seekBarRed);
        seekBarRed.setMax(255);

        seekBarRed.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                LogUtils.debug("value red:"+progress);
                redValue = progress;
                edtRedValue.setText(""+redValue);

                getColor();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        edtGreenValue     = (EditText) rootView.findViewById(R.id.edtGreenValue);
        seekBarGreen = (SeekBar) rootView.findViewById(R.id.seekBarGreen);
        seekBarGreen.setMax(255);

        seekBarGreen.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                LogUtils.debug("value red:"+progress);
                greenValue = progress;
                edtGreenValue.setText(""+greenValue);

                getColor();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        edtBlueValue     = (EditText) rootView.findViewById(R.id.edtBlueValue);
        seekBarBlue = (SeekBar) rootView.findViewById(R.id.seekBarBlue);
        seekBarBlue.setMax(255);

        seekBarBlue.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                LogUtils.debug("value red:"+progress);
                blueValue = progress;
                edtBlueValue.setText(""+blueValue);

                getColor();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        edtAlphaValue     = (EditText) rootView.findViewById(R.id.edtAlphaValue);
        seekBarAlpha = (SeekBar) rootView.findViewById(R.id.seekBarAlpha);
        seekBarAlpha.setMax(255);

        seekBarAlpha.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                LogUtils.debug("value red:"+progress);
                alphaValue = progress;
                edtAlphaValue.setText(""+alphaValue);

                getColor();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void getColor(){
        selectColor = Color.argb(alphaValue,redValue,greenValue,blueValue);
        edtSelectColor.setBackgroundColor(selectColor);

        edtIntValue.setText(""+selectColor);
        edtHexValue.setText(getHexColor());
    }

    public String getHexColor(){
        String hexColor = String.format("#%06X", (0xFFFFFF & selectColor));
        return hexColor;
    }
}
