package com.example.dogedex.auth

import android.content.Context
import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.dogedex.R
import com.example.dogedex.databinding.FragmentSingUpBinding
import com.example.dogedex.isValidaEmail


class SingUpFragment : Fragment() {

    interface SingUpFragmentActions {
        fun onSignUpFieldsValidated(
            email: String,
            password: String,
            passwordConfirmation: String
        )
    }

    private lateinit var singUpFragmentActions: SingUpFragmentActions

    override fun onAttach(context: Context) {
        super.onAttach(context)
        singUpFragmentActions = try {
            context as SingUpFragmentActions
        } catch (e: ClassCastException) {
            throw  ClassCastException("$context must implement LoginFragmentActions")
        }
    }

    private lateinit var binding: FragmentSingUpBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSingUpBinding.inflate(inflater)
        setupSignUpButton()
        return binding.root
    }

    private fun setupSignUpButton() {
        binding.signUpButton.setOnClickListener {
            validateFields()
        }
    }

    private fun validateFields() {

        binding.emailInput.error = ""
        binding.passwordInput.error = ""
        binding.confirmPasswordInput.error = ""


        var email = binding.emailEdit.text.toString()

        if (!isValidaEmail(email)) {
            binding.emailInput.error = getString(R.string.email_is_not_valid)
            return
        }

        val password = binding.passwordEdit.text.toString()
        if (password.isEmpty()) {
            binding.passwordInput.error = getString(R.string.password_is_not_valida)
            return
        }

        val passwordConfirmation = binding.confirmPasswordEdit.text.toString()
        if (passwordConfirmation.isEmpty()) {
            binding.confirmPasswordInput.error = getString(R.string.password_is_not_valida)
            return
        }

        if (password != passwordConfirmation) {
            binding.passwordInput.error = getString(R.string.password_do_not_match)
            return
        }

        singUpFragmentActions.onSignUpFieldsValidated(email, password, passwordConfirmation)
    }


}