package com.example.dogedex.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.navigation.findNavController
import com.example.dogedex.main.MainActivity
import com.example.dogedex.R
import com.example.dogedex.api.ApiResponseStatus
import com.example.dogedex.databinding.ActivityLoginBinding
import com.example.dogedex.models.User

class LoginActivity : AppCompatActivity(), LoginFragment.LoginFragmentActions,
    SingUpFragment.SingUpFragmentActions {

    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var progressBar = binding.loadingProgress

        authViewModel.status.observe(this) { status ->
            when (status) {
                is ApiResponseStatus.Error -> {
                    progressBar.visibility = View.GONE
                    showErrorDialog(status.message)
                }
                is ApiResponseStatus.Loading -> progressBar.visibility = View.VISIBLE

                is ApiResponseStatus.Success -> progressBar.visibility = View.GONE
            }
        }
        authViewModel.user.observe(this) { user ->
            if (user != null) {
                User.setLoggedInUser(this, user)
                startMainActivity()
            }
        }
    }

    private fun startMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    override fun onRegisterButtonClick() {
        findNavController(R.id.nav_host_fragment).navigate(LoginFragmentDirections.actionLoginFragmentToSingUpFragment())
    }

    override fun onLoginFieldsValidated(email: String, password: String) {
        authViewModel.login(email, password)
    }

    override fun onSignUpFieldsValidated(
        email: String,
        password: String,
        passwordConfirmation: String
    ) {
        authViewModel.signUp(email, password, passwordConfirmation)
    }

    private fun showErrorDialog(mesageId: Int) {
        AlertDialog.Builder(this)
            .setTitle(R.string.there_was_an_error)
            .setMessage(mesageId)
            .setPositiveButton(android.R.string.ok) { _, _ -> /**  Dismiss **/ }
            .create()
            .show()
    }
}