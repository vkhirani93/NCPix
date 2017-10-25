package in.netcore.ncpix.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import in.netcore.ncpix.R;

/**
 * Created by vrajesh on 10/25/17.
 */

public class UploadFragment extends Fragment{
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        view = inflater.inflate(R.layout.fragment_upload, container, false);

        Toast.makeText(getActivity(), "Upload", Toast.LENGTH_LONG).show();

        return view;
    }
}
