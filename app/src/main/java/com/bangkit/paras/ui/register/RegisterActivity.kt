package com.bangkit.paras.ui.register

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bangkit.paras.MainActivity
import com.bangkit.paras.R
import com.bangkit.paras.data.Result
import com.bangkit.paras.databinding.ActivityRegisterBinding
import com.bangkit.paras.ui.ViewModelFactory


class RegisterActivity : AppCompatActivity() {

    private lateinit var binding:ActivityRegisterBinding
    private val factory: ViewModelFactory = ViewModelFactory.getInstance(this)
    private val viewModel: RegisterViewModel by viewModels {
        factory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        binding.textViewLogin.setOnClickListener{
            finish()
        }
        binding.button.setOnClickListener {
            val username = binding.editTextTextUsername.text.toString()
            val email = binding.editTextTextEmailAddress.text.toString()
            val password = binding.editTextTextPassword.text.toString()

            when{
                username.isEmpty() -> {
                    binding.editTextTextUsername.error = getString(R.string.enter_username)
                }
                email.isEmpty() -> {
                    binding.editTextTextEmailAddress.error = getString(R.string.enter_email)
                }
                password.isEmpty() -> {
                    binding.editTextTextPassword.error = getString(R.string.enter_password)
                }
                else -> {
                    viewModel.register(username, email, password).observe(this){ result ->
                        if(result!=null){
                            when(result){
                                is Result.Loading -> {

                                }
                                is Result.Success -> {
                                    AlertDialog.Builder(this).apply {
                                        setTitle(getString(R.string.create_account_title))
                                        setMessage(getString(R.string.create_account_body))
                                        setPositiveButton(getString(R.string.create_account_button)) { _, _ ->
                                            finish()
                                        }
                                        create()
                                        show()
                                    }
                                }
                                is Result.Error -> {
                                    Toast.makeText(this, result.error, Toast.LENGTH_SHORT)
                                        .show()
                                }
                            }
                        }
                    }
                }
            }

        }
    }
}