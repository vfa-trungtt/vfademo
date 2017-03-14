package vfa.vfdemo.fragments.hdiapp;

import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import vfa.vfdemo.R;
import vfa.vflib.fragments.VFFragment;


public class FragLogin extends VFFragment {

    public static final int LOGIN_SUCCESSFUL    = 0;
    public static final int LOGIN_INVALID_AUTH  = 1;
    public static final int LOGIN_ERROR_NETWORK = 2;

    EditText edtEmail;
    EditText edtPass;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    public int onGetRootLayoutId() {
        return R.layout.frag_login;
    }

    @Override
    public void onViewLoaded() {

        rootView.findViewById(R.id.btLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doLogin();
            }
        });
        edtEmail = (EditText) rootView.findViewById(R.id.editEmail);
        edtPass  = (EditText) rootView.findViewById(R.id.editPass);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
//                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
//                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // [START_EXCLUDE]
//                updateUI(user);
                // [END_EXCLUDE]
            }
        };
    }

    private void doLogin(){

        String email    = edtEmail.getText().toString();
        String password     = edtPass.getText().toString();

//        mAuth.
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            onLoginSuccessful();
                        }else {
                            onLoginFails(LOGIN_ERROR_NETWORK);
                        }
//                        Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
//                            Log.w(TAG, "signInWithEmail:failed", task.getException());
                            Toast.makeText(getContext(), "auth fails", Toast.LENGTH_SHORT).show();
                        }

                        // [START_EXCLUDE]
                        if (!task.isSuccessful()) {
//                            mStatusTextView.setText(R.string.auth_failed);
                        }
//                        hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
        // [END sign_in_with_email]

    }

    public void onLoginSuccessful(){

    }

    public void onLoginFails(int error){

    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseApp.initializeApp(getActivity());
        mAuth.addAuthStateListener(mAuthListener);
    }

    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}
