package com.amanda.task.ui.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.amanda.task.R
import com.amanda.task.databinding.FragmentLoginBinding
import com.amanda.task.databinding.FragmentRegisterBinding
import com.amanda.task.databinding.FragmentSplashBinding
import com.amanda.task.ui.util.showBottomSheet

class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListener()
    }

    private fun initListener(){
        binding.buttonLogin.setOnClickListener {
            validateData()
        }
        binding.btnRegister.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
        binding.btnRecover.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_recoverAccountFragment)
        }
    }
    private fun validateData(){
        val email = binding.inputEmail.text.toString().trim()
        val senha = binding.inputPassword.text.toString().trim()
        if (email.isNotBlank()){
            if (senha.isNotBlank()){
                findNavController().navigate(R.id.action_global_homeFragment)
            } else{
                showBottomSheet(message = R.string.password_empty)
            }
        } else{
            showBottomSheet(message = R.string.email_empty)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }
}