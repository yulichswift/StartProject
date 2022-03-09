package com.jeff.startproject.ui.login

import android.content.Intent
import android.content.IntentSender
import android.os.Bundle
import android.widget.Toast
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.common.api.ApiException
import com.jeff.startproject.R
import com.jeff.startproject.databinding.ActivityGoogleSignInBinding
import com.log.JFLog
import com.ui.base.BaseActivity
import com.utils.extension.repeatAnimation

class GoogleSignInActivity : BaseActivity<ActivityGoogleSignInBinding>() {

    companion object {
        const val REQ_ONE_TAP = 100
    }

    private lateinit var oneTapClient: SignInClient
    private lateinit var signInRequest: BeginSignInRequest

    override fun getViewBinding(): ActivityGoogleSignInBinding {
        return ActivityGoogleSignInBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupOneTapClient()
        setupToolBar()
    }

    private fun setupOneTapClient() {
        oneTapClient = Identity.getSignInClient(this)
        signInRequest = BeginSignInRequest.builder()
            .setPasswordRequestOptions(
                BeginSignInRequest.PasswordRequestOptions.builder()
                    .setSupported(true)
                    .build()
            )
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    // Your server's client ID, not your Android client ID.
                    .setServerClientId("564470664025-io1gighceql1q4m565j1v0j8bdfj91h4.apps.googleusercontent.com")

                    // For some reason the same error code and error message will also be used if you specify setFilterByAuthorizedAccounts(true)
                    // and you as a user don't have any Google accounts that are already authorized to sign in to your application.
                    //
                    // You could first use setFilterByAuthorizedAccounts(true) to help the user pick the same account as the last time
                    // and then setFilterByAuthorizedAccounts(false) to make it possible to create a new user for the app.
                    .setFilterByAuthorizedAccounts(false)
                    .build()
            )
            // Automatically sign in when exactly one credential is retrieved.
            // .setAutoSelectEnabled(true)
            .build()

        binding.signInBtn.setOnClickListener {
            oneTapClient.beginSignIn(signInRequest)
                .addOnSuccessListener(this) { result ->
                    try {
                        startIntentSenderForResult(
                            result.pendingIntent.intentSender, REQ_ONE_TAP,
                            null, 0, 0, 0, null
                        )
                    } catch (e: IntentSender.SendIntentException) {
                        JFLog.e("Couldn't start One Tap UI: ${e.localizedMessage}")
                    }
                }
                .addOnFailureListener(this) { e ->
                    // No saved credentials found. Launch the One Tap sign-up flow, or
                    // do nothing and continue presenting the signed-out UI.
                    JFLog.e(e.localizedMessage)
                }
        }
    }

    private fun setupToolBar() {
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }

        binding.toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.action_share -> {
                    Toast.makeText(this, R.string.text_google_sign_in, Toast.LENGTH_SHORT).show()
                    true
                }
                else -> false
            }
        }

        binding.toolbar.navigationIcon?.repeatAnimation(binding.toolbar)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            REQ_ONE_TAP -> {
                try {
                    val credential = oneTapClient.getSignInCredentialFromIntent(data)
                    val idToken = credential.googleIdToken
                    val username = credential.id
                    val password = credential.password

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

                    if (username.isNotEmpty()) {
                        JFLog.d("Username: $username")
                        binding.editEmail.setText(username)
                    }

                    if (password != null) {
                        JFLog.d("Password: $password")
                        binding.editPassword.setText(password)
                    }
                } catch (e: ApiException) {
                    JFLog.e(e.localizedMessage)
                }
            }
        }
    }
}