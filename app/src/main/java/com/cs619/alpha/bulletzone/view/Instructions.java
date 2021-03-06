package com.cs619.alpha.bulletzone.view;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.cs619.alpha.bulletzone.R;

public class Instructions extends DialogFragment {

  public static Instructions newInstance() {

    Bundle args = new Bundle();

    Instructions fragment = new Instructions();
    fragment.setArguments(args);
    return fragment;
  }


  /**
   * The system calls this to get the DialogFragment's layout, regardless
   * of whether it's being displayed as a dialog or an embedded fragment.
   */
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout to use as dialog or embedded fragment
    View view = inflater.inflate(R.layout.fragment_instructions, container, false);

    view.findViewById(R.id.ok).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        getFragmentManager().popBackStack();
      }
    });

    return view;
  }

  /**
   * The system calls this only when creating the layout in a dialog.
   */
  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    // The only reason you might override this method when using onCreateView() is
    // to modify any dialog characteristics. For example, the dialog includes a
    // title by default, but your custom layout might not need it. So here you can
    // remove the dialog title, but you must call the superclass to get the Dialog.
    Dialog dialog = super.onCreateDialog(savedInstanceState);
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
    return dialog;
  }
}