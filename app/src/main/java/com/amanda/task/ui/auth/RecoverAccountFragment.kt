package com.amanda.task.ui.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import com.amanda.task.R
import com.amanda.task.databinding.FragmentRecoverAccountBinding
import com.amanda.task.databinding.FragmentRegisterBinding
import com.amanda.task.databinding.FragmentSplashBinding
import com.amanda.task.ui.BaseFragment
import com.amanda.task.ui.util.FirebaseHelper
import com.amanda.task.ui.util.initToolbar
import com.amanda.task.ui.util.showBottomSheet
import com.google.android.material.internal.ViewUtils.hideKeyboard
import com.google.firebase.auth.FirebaseAuth

class RecoverAccountFragment : BaseFragment() {
    private var _binding: FragmentRecoverAccountBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecoverAccountBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar(binding.toolbar)
        initListener()
        auth = FirebaseAuth.getInstance()
    }
    private fun initListener(){
        binding.buttonEnviar.setOnClickListener {
            validateData()
        }
    }
    private fun validateData(){
        val email = binding.inputEmail.text.toString().trim()

        if (email.isNotBlank()){
            hideKeyboard()
            binding.progressBar.isVisible = true
            recoverAccountUser(email)
        }else{
            showBottomSheet(message = getString(R.string.email_empty))
        }
    }
    private fun recoverAccountUser(email: String){
        try{
            auth.sendPasswordResetEmail(email)
                .addOnCompleteListener { task ->
                    binding.progressBar.isVisible = false
                    if (task.isSuccessful){
                        showBottomSheet(message = getString(R.string.text_email_sent_to_recover_account))
                    }else{
                        showBottomSheet(message = getString(FirebaseHelper.validError(task.exception?.message.toString())))
                    }
                }
        }catch ( e: Exception){
            Toast.makeText(requireContext(), e.message.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }
}