package com.example.noteapp.fragment

import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.noteapp.Application
import com.example.noteapp.databinding.FragmentAddNoteBinding
import com.example.noteapp.model.Note
import com.example.noteapp.viewmodel.NoteViewModel
import com.example.noteapp.viewmodel.NoteViewModelFactory
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


class AddNoteFragment : Fragment() {

    private var _binding: FragmentAddNoteBinding? = null
    private val binding get() = _binding!!

    private val navigationArgs: AddNoteFragmentArgs by navArgs()

    private val viewModel: NoteViewModel by activityViewModels {
        NoteViewModelFactory(
            (activity?.application as Application).database.noteDao()
        )
    }

    lateinit var note: Note

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun isEntryValid(): Boolean {
        return viewModel.isEntryValid(
            binding.AddNoteTitle.text.toString(),
            binding.AddNoteContent.text.toString()
        )
    }

    private fun Date.toString(format: String, locale: Locale = Locale.getDefault()): String {
        val formatter = SimpleDateFormat(format, locale)
        return formatter.format(this)
    }

    private fun getCurrentDateTime(): Date {
        return Calendar.getInstance().time
    }

    private fun getDateInString(): String {
        val date = getCurrentDateTime()
        return date.toString("yyyy/MM/dd HH:mm:ss")
    }

    private fun addNewNote() {
        if (isEntryValid()) {
            viewModel.addNewNote(
                binding.AddNoteTitle.text.toString(),
                binding.AddNoteContent.text.toString(),
                getDateInString()
            )

            val action = AddNoteFragmentDirections.actionAddNoteFragmentToNotesFragment()
            findNavController().navigate(action)

            binding.status.visibility = View.GONE
        } else {
            binding.status.visibility = View.VISIBLE
        }
    }

    private fun updateNote() {
        if (isEntryValid()) {
            viewModel.updateNote(
                this.navigationArgs.noteId,
                this.binding.AddNoteTitle.text.toString(),
                this.binding.AddNoteContent.text.toString(),
                getDateInString()
            )

            val action = AddNoteFragmentDirections.actionAddNoteFragmentToNotesFragment()
            findNavController().navigate(action)
        } else {
            binding.status.visibility = View.VISIBLE
        }
    }

    private fun bind(note: Note) {
        binding.apply {
            AddNoteTitle.setText(note.title, TextView.BufferType.SPANNABLE)
            AddNoteContent.setText(note.content, TextView.BufferType.SPANNABLE)

            binding.SaveButton.setOnClickListener { updateNote() }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val id = navigationArgs.noteId
        if (id != -1) {
            viewModel.getSpecificNote(id).observe(viewLifecycleOwner) { selectedNote ->
                note = selectedNote
                bind(note)
            }

            // Delete note
            binding.DeleteButton.visibility = View.VISIBLE
            binding.DeleteButton.setOnClickListener { showConfirmationDeleteDialog() }
        } else {
            binding.SaveButton.setOnClickListener { addNewNote() }
        }

        binding.CancelButton.setOnClickListener {
            val action = AddNoteFragmentDirections.actionAddNoteFragmentToNotesFragment()
            this.findNavController().navigate(action)
        }
    }

    private fun showConfirmationDeleteDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Delete Note")
            .setMessage("Are you sure you want to delete this note?")
            .setCancelable(false)
            .setNegativeButton(("NO")) { _, _ -> }
            .setPositiveButton(("YES")) { _, _ ->
                deleteNote()
            }
            .show()
    }

    private fun deleteNote() {
        viewModel.delete(note)
        findNavController().navigateUp()
    }

    override fun onDestroyView() {
        super.onDestroyView()

        val inputMethodManager =
            requireActivity().getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(requireActivity().currentFocus?.windowToken, 0)

        _binding = null
    }
}