package com.mercadopago.sdk.android.example.presentation.coremethods

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.mercadopago.sdk.android.example.R
import com.mercadopago.sdk.android.example.databinding.ActivityPaymentExampleScreenBinding

class PaymentExampleScreenXML : AppCompatActivity() {

    private val binding by lazy { ActivityPaymentExampleScreenBinding.inflate(layoutInflater) }
    private val viewModel: PaymentScreenViewModel by lazy { PaymentScreenViewModel() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        setupFullScreen()
        setupView()
    }

    private fun setupFullScreen() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun setupView() {
        binding.cardNumberTextField.onEvent = { event ->
            viewModel.onCardNumberEvent(event)
        }

        binding.expirationTextField.onEvent = { event ->
            viewModel.onExpirationDateEvent(event)
        }

        binding.securityTextField.onEvent = { event ->
            viewModel.onSecurityCodeEvent(event)
        }

        binding.paymentButton.setOnClickListener {
            viewModel.generateToken(
                cardNumberState = binding.cardNumberTextField.state,
                expirationDateState = binding.expirationTextField.state,
                securityCodeState = binding.securityTextField.state
            )
        }
    }
}
