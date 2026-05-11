package com.amanda.task.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.amanda.task.R
import com.amanda.task.data.model.Task
import com.amanda.task.databinding.FragmentDoneBinding
import androidx.recyclerview.widget.LinearLayoutManager
import com.amanda.task.ui.adapter.TaskAdapter
import com.amanda.task.data.model.Status


class DoneFragment : Fragment() {
    private var _binding: FragmentDoneBinding? = null
    private val binding get() = _binding!!

    private lateinit var taskAdapter: TaskAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDoneBinding.inflate(inflater,container, false)
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
    private fun getTask(){
        val taskList =  listOf(
            Task("20", "Criar tela de splash", Status.DONE),
            Task("21", "Configurar navigation", Status.DONE),
            Task("22", "Criar RecyclerView", Status.DONE),
            Task("23", "Implementar adapter", Status.DONE),
            Task("24", "Adicionar dependências", Status.DONE),
        )
        taskAdapter.submitList(taskList)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

