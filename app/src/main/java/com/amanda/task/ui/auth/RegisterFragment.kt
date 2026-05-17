package com.amanda.task.ui.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.amanda.task.R
import com.amanda.task.databinding.FragmentRegisterBinding
import com.amanda.task.databinding.FragmentSplashBinding
import com.amanda.task.ui.BaseFragment
import com.amanda.task.ui.util.FirebaseHelper
import com.amanda.task.ui.util.initToolbar
import com.amanda.task.ui.util.showBottomSheet
import com.google.firebase.auth.FirebaseAuth

class RegisterFragment : BaseFragment() {
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    //Variável que armazena a API de autenticação com o Firebase.
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar(binding.toolbar)
        initListener()
        auth = FirebaseAuth.getInstance()
    }
    private fun initListener(){
        binding.buttonRegister.setOnClickListener{
            validateData()
        }
    }
    private fun validateData(){
        val email = binding.inputEmail.text.toString().trim()
        val senha = binding.inputPassword.text.toString().trim()
        if (email.isNotBlank()){
            if (senha.isNotBlank()){
                hideKeyboard()
                Toast.makeText(requireContext(),"Tudo OK!", Toast.LENGTH_SHORT).show()
                binding.progressBar.isVisible = true
                registerUser(email, senha)
            } else{
                showBottomSheet(message = getString(R.string.password_empty_register_fragment))
            }
        } else{
            showBottomSheet(message = getString(R.string.email_empty_register_fragment))
        }
    }
    private fun registerUser(email:String, password:String){
        try{
            val auth = FirebaseAuth.getInstance()
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener{ task ->
                    if (task.isSuccessful){
                        //Encaminha para tela HOME.
                        findNavController().navigate(R.id.action_global_homeFragment)
                    }else{
                        binding.progressBar.isVisible = false
                        showBottomSheet(message = getString(FirebaseHelper.validError(task.exception?.message.toString())))
                    }

                }

        } catch(e: Exception){
            Toast.makeText(requireContext(), e.message.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }
}