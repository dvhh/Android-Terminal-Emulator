package jackpal.androidterm.preferences;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

/**
 * based on http://stackoverflow.com/questions/1974193/slider-on-my-preferencescreen
 */

public class SliderPreference extends DialogPreference implements SeekBar.OnSeekBarChangeListener,SeekBar.OnClickListener {
    private static final String androidns="http://schemas.android.com/apk/res/android";

    protected Context  mContext=null;
    protected TextView mSplashText;
    protected CharSequence mDialogMessage;
    protected TextView mValueText;
    protected SeekBar mSeekBar;
    protected int mValue;
    protected int mDefault;
    protected int mMax;

    protected OnSeekBarChangeListener mOnSeekBarChangeListener=null;

    public static abstract class OnSeekBarChangeListener{
        public abstract void  onProgressChanged(SliderPreference target,int progress,boolean fromUser);
    }


    public void setOnSeekBarChangeListener(OnSeekBarChangeListener onSeekBarChangeListener) {
        mOnSeekBarChangeListener=onSeekBarChangeListener;
    }

    protected static String getAttributeString(Context context, AttributeSet attrs,String namespace,String attribute,String defaultValue) {
        String result=defaultValue;
        int resourceID=attrs.getAttributeResourceValue(namespace, attribute, 0);
        if(resourceID==0) {
            result=attrs.getAttributeValue(namespace,attribute);
        }else{
            result=context.getString(resourceID);
        }
        return result;
    }

    public SliderPreference(Context context, AttributeSet attrs) {
        super(context,attrs);
        mContext=context;

        mDialogMessage=getDialogMessage();
        mDefault = attrs.getAttributeIntValue(androidns,"defaultValue",0);
        mMax = attrs.getAttributeIntValue(androidns,"max",100);
    }

    @Override
    protected View onCreateDialogView() {
        LinearLayout.LayoutParams params;
        LinearLayout layout = new LinearLayout(mContext);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(6,6, 6, 6);

        mSplashText = new TextView(mContext);
        mSplashText.setPadding(30, 10, 30, 10);
        if (mDialogMessage != null)
            mSplashText.setText(mDialogMessage);
        layout.addView(mSplashText);

        mValueText = new TextView(mContext);
        mValueText.setGravity(Gravity.CENTER_HORIZONTAL);
        mValueText.setTextSize(32);
        params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.FILL_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        layout.addView(mValueText, params);

        mSeekBar = new SeekBar(mContext);
        mSeekBar.setOnSeekBarChangeListener(this);
        layout.addView(mSeekBar, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        if (shouldPersist())
            mValue = getPersistedInt(mDefault);

        mSeekBar.setMax(mMax);
        mSeekBar.setProgress(mValue);

        return layout;
    }

    @Override
    protected void onBindDialogView(View v) {
        super.onBindDialogView(v);
        mSeekBar.setMax(mMax);
        mSeekBar.setProgress(mValue);
    }

    public void setValueText(String text) {
        mValueText.setText(text);
    }

    @Override
    public void onProgressChanged(SeekBar seek, int value, boolean fromTouch)
    {
        String t = String.valueOf(value);
        setValueText(t);

        if(mOnSeekBarChangeListener!=null) {
            mOnSeekBarChangeListener.onProgressChanged(this, value, fromTouch);
        }

    }

    @Override
    public void onStartTrackingTouch(SeekBar seek) {}
    @Override
    public void onStopTrackingTouch(SeekBar seek) {}

    public void setMax(int max) { mMax = max; }
    public int getMax() { return mMax; }

    public void setProgress(int progress) {
        mValue = progress;
        if (mSeekBar != null)
            mSeekBar.setProgress(progress);
    }
    public int getProgress() { return mValue; }

    @Override
    public void showDialog(Bundle state) {

        super.showDialog(state);

        Button positiveButton = ((AlertDialog) getDialog()).getButton(AlertDialog.BUTTON_POSITIVE);
        positiveButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        if (shouldPersist()) {

            mValue = mSeekBar.getProgress();
            persistInt(mSeekBar.getProgress());
            callChangeListener(Integer.valueOf(mSeekBar.getProgress()));
        }

        ((AlertDialog) getDialog()).dismiss();
    }
}
