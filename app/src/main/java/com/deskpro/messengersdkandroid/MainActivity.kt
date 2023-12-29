package com.deskpro.messengersdkandroid

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.deskpro.messenger.DeskPro
import com.deskpro.messenger.data.MessengerConfig
import com.deskpro.messengersdkandroid.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var messenger: DeskPro
    private val messengerConfig =
        MessengerConfig("http://178.62.74.147:3001/d/", "B0ED0B34-8507-4248-8738-0B0D80A6F7E9")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        /**
         * New messanger instance creation
         */
        messenger = DeskPro(messengerConfig)

        messenger.initialize(applicationContext)

        initListeners()
    }

    override fun onClick(v: View?) {
        binding.apply {
            when (v) {
                btnTest -> {
                    onTestPressed()
                }

                btnOpenMessenger -> {
                    onOpenMessengerPressed()
                }
            }
        }
    }

    private fun initListeners() {
        binding.btnTest.setOnClickListener(this)
        binding.btnOpenMessenger.setOnClickListener(this)
    }

    private fun onTestPressed() {
        val result = messenger.test()
        binding.tvMessage.text = result
    }

    private fun onOpenMessengerPressed() {
        messenger.present().show()
    }
}
