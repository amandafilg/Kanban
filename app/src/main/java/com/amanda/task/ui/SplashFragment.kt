package com.amanda.task.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.amanda.task.R
import com.amanda.task.databinding.FragmentSplashBinding
import com.google.firebase.auth.FirebaseAuth

class SplashFragment : Fragment() {

    private var _binding: FragmentSplashBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSplashBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Handler(Looper.getMainLooper()).postDelayed({checkAuth()},3000)
        auth = FirebaseAuth.getInstance()

    }

    private fun checkAuth(){
        try{
            val currentUser = auth.currentUser

            if (currentUser != null){
                //homeFragment
                findNavController().navigate(R.id.action_splashFragment3_to_homeFragment)

            }else{
                //permanecer na tela de login
                findNavController().navigate(R.id.action_splashFragment3_to_authentication)

            }

        }catch (e: Exception){
            Toast.makeText(requireContext(),e.message.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }
}