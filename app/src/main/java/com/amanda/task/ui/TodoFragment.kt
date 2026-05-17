package com.amanda.task.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.amanda.task.R
import com.amanda.task.databinding.FragmentTodoBinding
import com.amanda.task.ui.adapter.TaskAdapter
import com.amanda.task.data.model.Task
import com.amanda.task.data.model.Status
import android.widget.Toast
import androidx.core.view.isVisible
import com.amanda.task.ui.util.showBottomSheet
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database
import com.google.firebase.database.ValueEventListener
import com.amanda.task.TaskViewModel
import androidx.fragment.app.activityViewModels
import com.amanda.task.ui.util.FirebaseHelper


class TodoFragment : Fragment() {
    private var _binding: FragmentTodoBinding? = null
    private val binding get() = _binding!!
    private lateinit var taskAdapter: TaskAdapter

    private val viewModel: TaskViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTodoBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListeners()
        initRecyclerViewTask()
        getTask()
    }

    private fun initListeners(){
        binding.floatingActionButton.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToFormTaskFragment(null)
            findNavController().navigate(action)
        }

        observerViewModel()
    }

    private fun observerViewModel(){
        viewModel.taskUpdate.observe(viewLifecycleOwner){ updateTask ->
            if(updateTask.status == Status.TODO){
                val oldList = taskAdapter.currentList
                val newList = oldList.toMutableList().apply {
                    find { it.id == updateTask.id}?.description = updateTask.description
                }

                val position = newList.indexOfFirst{ it.id == updateTask.id}
                taskAdapter.submitList(newList)
                taskAdapter.notifyItemChanged(position)
            }
        }
    }

    private fun initRecyclerViewTask(){
        taskAdapter = TaskAdapter(requireContext()){task, option ->
            optionSelected(task,option)
        }

        with (binding.recyclerViewTask) {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            adapter = taskAdapter

        }
    }
    private fun optionSelected(task: Task, option:Int){
        when (option){

            TaskAdapter.SELECT_REMOVE -> {
                showBottomSheet(titleDialog = R.string.text_title_dialog_delete,
                    message = getString(R.string.text_message_dialog_delete),
                    titleButton = R.string.text_button_dialog_confirm,
                    onClick = {
                        deleteTask(task)
                    }
                )
            }

            TaskAdapter.SELECT_EDIT -> {
                val action = HomeFragmentDirections.actionHomeFragmentToFormTaskFragment(task)
                findNavController().navigate(action)
                Toast.makeText(requireContext(), "Editando ${task.description}", Toast.LENGTH_SHORT).show()
            }

            TaskAdapter.SELECT_DETAILS -> {
                Toast.makeText(requireContext(), "Detalhes ${task.description}", Toast.LENGTH_SHORT).show()
            }

            TaskAdapter.SELECT_NEXT -> {
                task.status = Status.DOING
                updateTask(task)
            }
        }
    }

    private fun getTask() {
        FirebaseHelper.getDatabase()
            .child("tasks")
            .child(FirebaseHelper.getIdUser())
            .addValueEventListener(object: ValueEventListener{
                override fun onDataChange(p0: DataSnapshot) {
                    val taskList = mutableListOf<Task>()

                    for (ds in p0.children){
                        val task = ds.getValue(Task::class.java) as Task
                        taskList.add(task)

                        if(task.status == Status.TODO){
                            taskList.add(task)
                        }
                    }
                    binding.progressBar.isVisible = false
                    listEmpty(taskList)

                    taskList.reverse()
                    taskAdapter.submitList(taskList)
                }

                override fun onCancelled(p0: DatabaseError) {
                    Toast.makeText(requireContext(), R.string.error_generic, Toast.LENGTH_SHORT).show()
                }
            })
    }
    private fun deleteTask(task:Task){
        FirebaseHelper.getDatabase()
            .child("task")
            .child(FirebaseHelper.getIdUser())
            .child(task.id)
            .removeValue().addOnCompleteListener{ result ->
                if(result.isSuccessful){
                    Toast.makeText(requireContext(), R.string.text_delete_sucess_task, Toast.LENGTH_SHORT).show()

                }else{
                    Toast.makeText(requireContext(), R.string.error_generic, Toast.LENGTH_SHORT).show()
                }
            }

    }
    private fun listEmpty(taskList: List<Task>){
        binding.textInfo.text = if (taskList.isEmpty()){
            getString(R.string.text_list_task_empty)
        }else{
            ""
        }
    }

    private fun updateTask(task: Task){

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

