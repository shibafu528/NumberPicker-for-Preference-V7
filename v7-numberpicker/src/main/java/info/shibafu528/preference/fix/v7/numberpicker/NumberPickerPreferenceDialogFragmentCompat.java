package info.shibafu528.preference.fix.v7.numberpicker;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.preference.PreferenceDialogFragmentCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;

public class NumberPickerPreferenceDialogFragmentCompat extends PreferenceDialogFragmentCompat implements NumberPicker.OnValueChangeListener {
    private int value;

    public static NumberPickerPreferenceDialogFragmentCompat newInstance(String key) {
        NumberPickerPreferenceDialogFragmentCompat fragment = new NumberPickerPreferenceDialogFragmentCompat();
        Bundle b = new Bundle(1);
        b.putString(PreferenceDialogFragmentCompat.ARG_KEY, key);
        fragment.setArguments(b);
        return fragment;
    }

    @Override
    protected void onPrepareDialogBuilder(AlertDialog.Builder builder) {
        super.onPrepareDialogBuilder(builder);

        NumberPickerPreference preference = getNumberPickerPreference();
        value = preference.getValue();

        View view = LayoutInflater.from(getContext()).inflate(R.layout.preference_numberpicker, null);

        NumberPicker numberPicker = view.findViewById(android.R.id.input);
        numberPicker.setMinValue(preference.getMinValue());
        numberPicker.setMaxValue(preference.getMaxValue());
        numberPicker.setValue(value);
        numberPicker.setOnValueChangedListener(this);

        ViewGroup.LayoutParams lp = numberPicker.getLayoutParams();
        lp.height = preference.getHeight();
        numberPicker.setLayoutParams(lp);

        builder.setView(view)
                .setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        NumberPickerPreferenceDialogFragmentCompat.this.onDismiss(dialog);
                    }
                })
                .setNegativeButton(android.R.string.cancel, this)
                .setPositiveButton(android.R.string.ok, this);
    }

    @Override
    public void onDialogClosed(boolean positiveResult) {
        NumberPickerPreference preference = getNumberPickerPreference();

        if (positiveResult) {
            if (preference.callChangeListener(value)) {
                preference.setValue(value);
            }
        }
    }

    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
        value = newVal;
    }

    private NumberPickerPreference getNumberPickerPreference() {
        return (NumberPickerPreference) getPreference();
    }
}
