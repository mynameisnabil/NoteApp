package com.example.noteapp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import com.example.noteapp.Application
import com.example.noteapp.adapter.NoteListAdapter
import com.example.noteapp.databinding.FragmentNoteBinding
import com.example.noteapp.viewmodel.NoteViewModel
import com.example.noteapp.viewmodel.NoteViewModelFactory
import java.util.Date

class NoteFragment : Fragment() {
    private var _binding: FragmentNoteBinding? = null
    private val binding get() = _binding!!

    // Don't forget to put 'name' attribute (application) in AndroidManifest.xml
    private val viewModel: NoteViewModel by activityViewModels {
        NoteViewModelFactory(
            (activity?.application as Application).database.noteDao()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Notes list
        val adapter = NoteListAdapter { selectedNoted ->
            val action = NoteFragmentDirections.actionNotesFragmentToAddNoteFragment(selectedNoted.id)
            this.findNavController().navigate(action)
        }
        binding.notelist.adapter = adapter
        viewModel.allNote.observe(this.viewLifecycleOwner) { notes ->
            notes.let {
                adapter.submitList(it.sortedByDescending { note -> Date(note.timestamp) }) {
                    binding.notelist.scrollToPosition(0)
                }
            }
        }
        binding.notelist.layoutManager = LinearLayoutManager(this.context)

        // Add note button
        binding.addNote.setOnClickListener {
            val action = NoteFragmentDirections.actionNotesFragmentToAddNoteFragment(-1);
            this.findNavController().navigate(action)

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
