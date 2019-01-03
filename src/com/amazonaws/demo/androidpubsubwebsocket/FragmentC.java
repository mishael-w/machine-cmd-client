package com.amazonaws.demo.androidpubsubwebsocket;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class FragmentC extends Fragment{

    private static final String TAG = "FragmentC" ;
    int count  = 50;


    TextView title;
    TextView reportTextView;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.v(TAG,"onCreateView");
        View v = inflater.inflate(R.layout.fragment_c, container, false);
        reportTextView = v.findViewById(R.id.reports_field);
        reportTextView.setMovementMethod(new ScrollingMovementMethod());
        title = v.findViewById(R.id.textView);
        title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                count++;
                Log.i(TAG , "count = " + count);
            }
        });

        ConstraintLayout constraintLayout = v.findViewById(R.id.bg);
        constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                count--;
                Log.i(TAG , "count = " + count);
            }
        });
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }



    public void publishReport(final String report){
        final String str = reportTextView.getText() + report + "\n";
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                reportTextView.setText(str);
                int lineCount =reportTextView.getLineCount();
                int screenLength = (reportTextView.getHeight()/reportTextView.getLineHeight());
                int scrollToPage = lineCount / (screenLength);
                if (scrollToPage > 0)
                    reportTextView.scrollTo(0, (scrollToPage * screenLength - 3) * reportTextView.getLineHeight());
            }
        });
    }


}
