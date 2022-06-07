package com.bangkit.paras.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.bangkit.paras.MainActivity
import com.bangkit.paras.R
import com.bangkit.paras.data.Result
import com.bangkit.paras.databinding.ActivityLoginBinding
import com.bangkit.paras.di.DataStoreManager
import com.bangkit.paras.ui.ViewModelFactory
import com.bangkit.paras.ui.register.RegisterActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val factory: ViewModelFactory = ViewModelFactory.getInstance(this)
    private val viewModel: LoginViewModel by viewModels {
        factory
    }

    @Inject
    lateinit var dataStoreManager: DataStoreManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        binding.progressBar.visibility = View.INVISIBLE

        runBlocking {
            val user = dataStoreManager.getUser().first()
            if (!user.authorization.isNullOrEmpty()) {
                val myIntent = Intent(this@LoginActivity, MainActivity::class.java)
                this@LoginActivity.startActivity(myIntent)
            }
        }


        binding.textViewRegister.setOnClickListener {
            val myIntent = Intent(this@LoginActivity, RegisterActivity::class.java)
            this@LoginActivity.startActivity(myIntent)
        }
        binding.button.setOnClickListener {
            val username = binding.editTextTextUsername.text.toString()
            val password = binding.editTextTextPassword.text.toString()

            when {
                username.isEmpty() -> {
                    binding.editTextTextUsername.error = getString(R.string.enter_username)
                }
                password.isEmpty() -> {
                    binding.editTextTextPassword.error = getString(R.string.enter_password)
                }
                else -> {
                    viewModel.login(username, password).observe(this) { result ->
                        if (result != null) {
                            when (result) {
                                is Result.Loading -> {
                                    binding.progressBar.visibility = View.VISIBLE
                                }
                                is Result.Success -> {
                                    runBlocking {
                                        dataStoreManager.setUser(result.data.credentials)
                                        val myIntent =
                                            Intent(this@LoginActivity, MainActivity::class.java)
                                        this@LoginActivity.startActivity(myIntent)
                                        binding.progressBar.visibility = View.INVISIBLE
                                        finish()
                                    }
                                }
                                is Result.Error -> {
                                    binding.progressBar.visibility = View.INVISIBLE
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