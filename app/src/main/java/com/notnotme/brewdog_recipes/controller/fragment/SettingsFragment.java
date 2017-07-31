package com.notnotme.brewdog_recipes.controller.fragment;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.notnotme.brewdog_recipes.R;
import com.notnotme.brewdog_recipes.controller.services.SynchronizationReceiver;
import com.notnotme.brewdog_recipes.controller.services.SynchronizationService;

import java.text.SimpleDateFormat;
import java.util.Date;

public final class SettingsFragment extends BaseFragment {

    private static final String TAG = SettingsFragment.class.getSimpleName();

    private Button mSynchronizeButton;
    private TextView mLastSynchronizationText;
    private SynchronizationService mSynchronizationService;

    public ServiceConnection mServiceConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder binder) {
            mSynchronizationService = ((SynchronizationService.ServiceBinder) binder).getService();
            mSynchronizeButton.setEnabled(!mSynchronizationService.isStarted());
        }

        public void onServiceDisconnected(ComponentName className) {
            mSynchronizationService = null;
        }
    };

    private SynchronizationReceiver mSynchronizationReceiver =
            new SynchronizationReceiver() {
                @Override
                public void onSynchronizationDone() {
                    if (isDetached()) return;
                    mSynchronizeButton.setEnabled(true);

                    Date syncDate = new Date();
                    getSettingsManager().setLastSyncDate(syncDate);

                    String dateText = getString(R.string.last_synchronization_date, SimpleDateFormat.getInstance().format(syncDate));
                    mLastSynchronizationText.setText(dateText);
                }

                @Override
                public void onSynchronizationFailure(String error) {
                    if (isDetached()) return;
                    mSynchronizeButton.setEnabled(true);
                    String message = "Beers synchronization error: " + error;
                    Log.e(TAG, message);
                    Snackbar.make(getActivity().findViewById(R.id.coordinator_layout),
                            message, BaseTransientBottomBar.LENGTH_INDEFINITE).show();
                }
            };

    private View.OnClickListener mOnSynchronizationClickListener =
            new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mSynchronizeButton.setEnabled(false);
                    getActivity().startService(new Intent(getContext(), SynchronizationService.class));
                }
            };

    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LocalBroadcastManager.getInstance(getContext())
                .registerReceiver(mSynchronizationReceiver, new IntentFilter(SynchronizationService.INTENT_ACTION));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mSynchronizeButton = view.findViewById(R.id.synchronize_button);
        mSynchronizeButton.setOnClickListener(mOnSynchronizationClickListener);
        mSynchronizeButton.setEnabled(false);

        String dateText = getString(R.string.last_synchronization_date, SimpleDateFormat.getInstance().format(getSettingsManager().getLastSyncDate()));
        mLastSynchronizationText = view.findViewById(R.id.text_last_synchronization);
        mLastSynchronizationText.setText(dateText);

        getActivity().bindService(new Intent(getContext(), SynchronizationService.class), mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unbindService(mServiceConnection);
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(mSynchronizationReceiver);
    }

}
