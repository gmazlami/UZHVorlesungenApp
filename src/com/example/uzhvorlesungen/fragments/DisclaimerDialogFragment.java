package com.example.uzhvorlesungen.fragments;

import com.example.uzhvorlesungen.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

public class DisclaimerDialogFragment extends DialogFragment{
	
	private DisclaimerInterface listener = null;
	
	public interface DisclaimerInterface{
		public void onAccept();
		public void onReject();
	}

	@Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
		//create the dialog through a builder
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		
		//set title, button titles and on click actions for the dialog
		builder.setTitle(R.string.disclaimer_title);
		builder.setMessage(R.string.disclaimer_message);
		builder.setNegativeButton(R.string.disclaimer_reject, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				listener.onReject();
			}
		});
		builder.setPositiveButton(R.string.disclaimer_accept, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				listener.onAccept();
			}
		});
		return null;
	}
	
    // Override the Fragment.onAttach() method to instantiate the DisclaimerInterface
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            listener = (DisclaimerInterface) getActivity();
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement DisclmaimerInterface");
        }
    }
	
}
