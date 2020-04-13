package com.bsuuv.grocerymanager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;


public class NetworkFragment extends Fragment {
    private static final String TAG = "NetworkFragment";
    private static final String URL_KEY = "UrlKey";

    private DownloadCallback mCallback;
    private DownloadTask mDownloadTask;
    private String mUrlString;

    public NetworkFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param manager System service that manages fragments.
     * @param url     The URL this instance is going to connect.
     * @return A new instance of fragment NetworkFragment.
     */
    public static NetworkFragment getInstance(FragmentManager manager, String url) {
        NetworkFragment networkFragment = (NetworkFragment) manager.findFragmentByTag(TAG);

        if (networkFragment == null) {
            networkFragment = new NetworkFragment();

            Bundle args = new Bundle();

            //Fragmentin luomisargumentteihin laitetaan URL_KEY:tä vastaavaksi arvoksi
            // kiinnostuksen kohteena oleva url.
            args.putString(URL_KEY, url);
            networkFragment.setArguments(args);

            manager.beginTransaction().add(networkFragment, TAG).commit();
        }

        return networkFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        if (getArguments() != null) mUrlString = getArguments().getString(URL_KEY);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        TextView textView = new TextView(getActivity());
        textView.setText(R.string.hello_blank_fragment);
        return textView;
    }
}
