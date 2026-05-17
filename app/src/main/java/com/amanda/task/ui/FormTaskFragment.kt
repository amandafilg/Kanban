package com.amanda.task.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.firebase.Firebase
import com.amanda.task.R
import com.amanda.task.TaskViewModel
import com.amanda.task.data.model.Task
import com.amanda.task.databinding.FragmentFormTaskBinding
import com.amanda.task.databinding.FragmentRegisterBinding
import com.amanda.task.ui.util.initToolbar
import com.amanda.task.ui.util.showBottomSheet
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database
import com.amanda.task.data.model.Status
import com.amanda.task.ui.util.FirebaseHelper
import kotlin.getValue


class FormTaskFragment : BaseFragment() {
    private var _binding: FragmentFormTaskBinding? = null
    private val binding get() = _binding!!

    private lateinit var task: Task

    private var newTask: Boolean = true

    private var status: Status = Status.TODO

    private val viewModel: TaskViewModel by activityViewModels()

    private val args: FormTaskFragmentArgs by navArgs()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View {
        _binding = FragmentFormTaskBinding.inflate(inflater,container,false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar(binding.toolbar)

        getArgs()
        initListener()
    }

    private fun getArgs(){
        args.task.let{
            if (it != null){
                this.task = it
                configTask()
            }
        }
    }

    private fun configTask(){
        newTask = false
        status = task.status
        binding.textToolbar.setText(R.string.text_toolbar_update_form_task_fragment)
        binding.editTextDescricao.setText(task.description)
        setStatus()
    }
    private fun setStatus(){
        val id = when (task.status){
            Status.TODO -> R.id.rbTodo
            Status.DOING -> R.id.rbDoing
            else -> R.id.rbDone
        }
        binding.radioGroup.check(id)
    }
    private fun initListener(){
        binding.buttonSave.setOnClickListener {
            validateData()
        }

        binding.radioGroup.setOnCheckedChangeListener { _, id-> status =
            when(id){
                R.id.rbTodo -> Status.TODO
                R.id.rbDoing -> Status.DOING
                else -> Status.DONE
            }
        }
    }
    private fun validateData(){
        val description = binding.editTextDescricao.text.toString().trim()
        if (description.isNotBlank()){
            hideKeyboard()
            binding.progressBar.isVisible = true

            if(newTask) task = Task()

            task.description = description
            task.status = status

            saveTask()
        }else{
            showBottomSheet(message = getString(R.string.description_empty_form_task_fragment))
        }
    }

    private fun saveTask() {
        val userId = FirebaseHelper.getIdUser()
        if (userId == null){
            showBottomSheet(message = getString(R.string.error_generic))
            return
        }
        binding.progressBar.isVisible = true

        FirebaseHelper.getDatabase()
            .child("task")
            .child(userId)
            .child(task.id)
            .setValue(task)
            .addOnCompleteListener { result ->

                binding.progressBar.isVisible = false

                if (result.isSuccessful) {
                    Toast.makeText(
                        requireContext(),
                        R.string.text_save_sucess_form_task_fragment,
                        Toast.LENGTH_SHORT).show()

                    if (newTask) {
                        findNavController().popBackStack()
                    } else {
                        Toast.makeText(
                            requireContext(),
                            R.string.text_update_sucess_form_task_fragment,
                            Toast.LENGTH_SHORT
                        ).show()

                        viewModel.setUpdateTask(task)
                        binding.progressBar.isVisible = false

                    }
                } else {
                    binding.progressBar.isVisible = false
                    showBottomSheet(message = getString(R.string.error_generic))
                }
            }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}