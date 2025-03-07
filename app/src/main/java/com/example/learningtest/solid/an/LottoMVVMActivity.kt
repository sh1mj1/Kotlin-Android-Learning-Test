package com.example.learningtest.solid.an

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.learningtest.databinding.ActivityMvvmLottoBinding

class LottoMVVMActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMvvmLottoBinding
    private val viewModel: LottoViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMvvmLottoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this
    }
}
