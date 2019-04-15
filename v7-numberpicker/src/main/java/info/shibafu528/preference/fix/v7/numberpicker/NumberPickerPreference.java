package info.shibafu528.preference.fix.v7.numberpicker;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.content.res.TypedArrayUtils;
import android.support.v7.preference.DialogPreference;
import android.util.AttributeSet;

import com.takisoft.fix.support.v7.preference.PreferenceFragmentCompat;

public class NumberPickerPreference extends DialogPreference {
    private int value;
    private int minValue;
    private int maxValue;

    static {
        PreferenceFragmentCompat.registerPreferenceFragment(NumberPickerPreference.class, NumberPickerPreferenceDialogFragmentCompat.class);
    }

    public NumberPickerPreference(Context context) {
        this(context, null);
    }

    @SuppressLint("RestrictedApi")
    public NumberPickerPreference(Context context, AttributeSet attrs) {
        this(context, attrs, TypedArrayUtils.getAttr(context, R.attr.dialogPreferenceStyle,
                android.R.attr.dialogPreferenceStyle));
    }

    public NumberPickerPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public NumberPickerPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.NumberPickerPreference, defStyleAttr, 0);
        minValue = a.getInteger(R.styleable.NumberPickerPreference_pref_minValue, 0);
        maxValue = a.getInteger(R.styleable.NumberPickerPreference_pref_maxValue, 1);
        a.recycle();
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        setInternalValue(value, false);
    }

    public int getMinValue() {
        return minValue;
    }

    public void setMinValue(int minValue) {
        this.minValue = minValue;
    }

    public int getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(int maxValue) {
        this.maxValue = maxValue;
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return a.getInteger(index, 0);
    }

    @Override
    protected void onSetInitialValue(@Nullable Object defaultValue) {
        setInternalValue(getPersistedInt(defaultValue == null ? 0 : (int) defaultValue), true);
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        final Parcelable superState = super.onSaveInstanceState();
        if (isPersistent()) {
            return superState;
        }

        final SavedState myState = new SavedState(superState);
        myState.value = getValue();

        return myState;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state == null || !state.getClass().equals(SavedState.class)) {
            super.onRestoreInstanceState(state);
            return;
        }

        SavedState myState = (SavedState) state;
        super.onRestoreInstanceState(myState.getSuperState());
        setValue(myState.value);
    }

    private void setInternalValue(int value, boolean force) {
        int oldValue = getPersistedInt(0);

        boolean changed = value != oldValue;

        if (changed || force) {
            this.value = value;

            persistInt(value);

            notifyChanged();
        }
    }

    private static class SavedState extends BaseSavedState {
        private int value;

        public SavedState(Parcel source) {
            super(source);
            value = source.readInt();
        }

        public SavedState(Parcelable superState) {
            super(superState);
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeInt(value);
        }

        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() {
            @Override
            public SavedState createFromParcel(Parcel source) {
                return new SavedState(source);
            }

            @Override
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }
}
