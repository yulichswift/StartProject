package com.jeff.startproject.ui.login

import android.content.Intent
import android.os.Bundle
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.jeff.startproject.databinding.ActivityGoogleSignInBinding
import com.log.JFLog
import com.ui.base.BaseActivity

class GoogleSignIn2Activity : BaseActivity<ActivityGoogleSignInBinding>() {

    companion object {
        private const val REQ_GOOGLE_SIGN_IN = 1001
    }

    override fun getViewBinding(): ActivityGoogleSignInBinding {
        return ActivityGoogleSignInBinding.inflate(layoutInflater)
    }

    private var googleSignInOpts: GoogleSignInOptions? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        googleSignInOpts = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestIdToken("564470664025-io1gighceql1q4m565j1v0j8bdfj91h4.apps.googleusercontent.com")
            .build()

        binding.signInBtn.setOnClickListener {
            googleSignInOpts?.also {
                val sendSignIn = {
                    val googleSignInClient = GoogleSignIn.getClient(this, it)
                    val signInIntent = googleSignInClient.signInIntent
                    startActivityForResult(signInIntent, REQ_GOOGLE_SIGN_IN)
                }

                val last = GoogleSignIn.getLastSignedInAccount(this)
                if (last != null) {
                    GoogleSignIn.getClient(this, it).signOut().addOnCompleteListener {
                        sendSignIn()
                    }
                } else {
                    sendSignIn()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            REQ_GOOGLE_SIGN_IN -> {
                try {
                    val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                    task.addOnSuccessListener { credential ->

                        val idToken = credential.idToken
                        val username = credential.email
                        val password = credential.id

                        when {
                            idToken != null -> {
                                // Got an ID token from Google. Use it to authenticate
                                // with your backend.
                                JFLog.d("Got ID token.")
                                binding.tokenLabel.text = idToken
                            }
                            password != null -> {
                                // Got a saved username and password. Use them to authenticate
                                // with your backend.
                            }
                            else -> {
                                // Shouldn't happen.
                                JFLog.d("No ID token or password!")
                            }
                        }

                        if (username != null) {
                            JFLog.d("Username: $username")
                            binding.editEmail.setText(username)
                        }

                        if (password != null) {
                            JFLog.d("Password: $password")
                            binding.editPassword.setText(password)
                        }
                    }
                } catch (e: ApiException) {
                    JFLog.e(e.localizedMessage)
                }
            }
        }
    }
}
