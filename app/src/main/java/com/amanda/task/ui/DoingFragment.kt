package com.amanda.task.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.amanda.task.R
import com.amanda.task.data.model.Task
import com.amanda.task.databinding.FragmentDoingBinding
import androidx.recyclerview.widget.LinearLayoutManager
import com.amanda.task.ui.adapter.TaskAdapter
import com.amanda.task.data.model.Status

class DoingFragment : Fragment() {
    private var _binding: FragmentDoingBinding? = null
    private val binding get() = _binding!!
    private lateinit var taskAdapter: TaskAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDoingBinding.inflate(inflater,container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerViewTask()
        getTask()
    }
    private fun initRecyclerViewTask() {
        taskAdapter = TaskAdapter(requireContext()){task, option -> optionSelected(task,option)}
        with (binding.recyclerViewTask) {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            adapter = taskAdapter

        }
    }

    private fun optionSelected(task: Task, option:Int){
        when (option){

            TaskAdapter.SELECT_REMOVE -> {
                Toast.makeText(requireContext(), "Removendo ${task.description}", Toast.LENGTH_SHORT).show()
            }

            TaskAdapter.SELECT_EDIT -> {
                Toast.makeText(requireContext(), "Editando ${task.description}", Toast.LENGTH_SHORT).show()
            }

            TaskAdapter.SELECT_DETAILS -> {
                Toast.makeText(requireContext(), "Detalhes ${task.description}", Toast.LENGTH_SHORT).show()
            }

            TaskAdapter.SELECT_NEXT -> {
                Toast.makeText(requireContext(), "Próximo", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getTask() {
        val taskList = listOf(
            Task("10", "Implementar tela de cadastro", Status.DOING),
            Task("11", "Corrigir bug no login", Status.DOING),
            Task("12", "Testar integração com API", Status.DOING),
            Task("13", "Melhorar layout da home", Status.DOING),
            Task("14", "Organizar estrutura do projeto", Status.DOING),
        )
        taskAdapter.submitList(taskList)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

