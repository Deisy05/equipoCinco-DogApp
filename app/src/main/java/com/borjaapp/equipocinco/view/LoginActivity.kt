package com.borjaapp.equipocinco.view

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.viewModels
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.borjaap.equipocinco.view.fragment.login.CIPHERTEXT_WRAPPER
import com.borjaap.equipocinco.view.fragment.login.CryptographyManager
import com.borjaap.equipocinco.view.fragment.login.SHARED_PREFS_FILENAME
import com.borjaapp.equipocinco.R
import com.borjaapp.equipocinco.databinding.ActivityLoginBinding
import com.borjaapp.equipocinco.databinding.ActivityMainBinding
import com.borjaapp.equipocinco.view.fragment.login.BiometricPromptUtils
import com.borjaapp.equipocinco.view.fragment.login.EnableBiometricLoginActivity
import com.borjaapp.equipocinco.view.fragment.login.LoginViewModel
import com.borjaapp.equipocinco.view.fragment.login.SampleAppUser
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class LoginActivity : AppCompatActivity() {
    private val TAG = "LoginActivity"
    private lateinit var biometricPrompt: BiometricPrompt
    private val cryptographyManager = CryptographyManager()
    private val ciphertextWrapper
        get() = cryptographyManager.getCiphertextWrapperFromSharedPrefs(
            applicationContext,
            SHARED_PREFS_FILENAME,
            Context.MODE_PRIVATE,
            CIPHERTEXT_WRAPPER
        )
    private lateinit var binding: ActivityLoginBinding
    private val loginWithPasswordViewModel by viewModels<LoginViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val animacionHuella = binding.animacionHuella
        animacionHuella.setAnimationFromUrl("https://lottie.host/e8c55d59-cc8e-4b06-944c-19909f699fb9/XUOmBqXu24.json")
        animacionHuella.playAnimation()

        val canAuthenticate = BiometricManager.from(applicationContext).canAuthenticate()
        if (canAuthenticate == BiometricManager.BIOMETRIC_SUCCESS) {
            binding.animacionHuella.setOnClickListener {
                if (ciphertextWrapper != null) {
                    showBiometricPromptForDecryption()
                } else {
                    startActivity(Intent(this, EnableBiometricLoginActivity::class.java))
                }
            }
        } else {
            // binding.useBiometrics.visibility = View.INVISIBLE
        }


    }

    /**
     * The logic is kept inside onResume instead of onCreate so that authorizing biometrics takes
     * immediate effect.
     */
    override fun onResume() {
        super.onResume()

        if (ciphertextWrapper != null) {
            if (SampleAppUser.fakeToken == null) {
                showBiometricPromptForDecryption()
            } else {
                // The user has already logged in, so proceed to the rest of the app
                // this is a todo for you, the developer
                updateApp(getString(R.string.already_signedin))
            }
        }
    }

    // BIOMETRICS SECTION

    private fun showBiometricPromptForDecryption() {
        ciphertextWrapper?.let { textWrapper ->
            val secretKeyName = getString(R.string.secret_key_name)
            val cipher = cryptographyManager.getInitializedCipherForDecryption(
                secretKeyName, textWrapper.initializationVector
            )
            biometricPrompt =
                BiometricPromptUtils.createBiometricPrompt(
                    this,
                    ::decryptServerTokenFromStorage
                )
            val promptInfo = BiometricPromptUtils.createPromptInfo(this)
            biometricPrompt.authenticate(promptInfo, BiometricPrompt.CryptoObject(cipher))
        }
    }

    private fun decryptServerTokenFromStorage(authResult: BiometricPrompt.AuthenticationResult) {
        ciphertextWrapper?.let { textWrapper ->
            authResult.cryptoObject?.cipher?.let {
                val plaintext =
                    cryptographyManager.decryptData(textWrapper.ciphertext, it)
                SampleAppUser.fakeToken = plaintext
                // Now that you have the token, you can query server for everything else
                // the only reason we call this fakeToken is because we didn't really get it from
                // the server. In your case, you will have gotten it from the server the first time
                // and therefore, it's a real token.
                //val navController = findNavController(R.id.mainActivity)
                //navController.navigate(R.id.action_mainFragment_to_homeAppointmentFragment)
                startActivity(Intent(this, MainActivity::class.java))

                updateApp(getString(R.string.already_signedin))
            }
        }
    }

    // USERNAME + PASSWORD SECTION

    private fun updateApp(successMsg: String) {
        //binding.success.text = successMsg
    }
}