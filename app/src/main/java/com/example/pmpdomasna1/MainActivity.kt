package com.example.pmpdomasna1

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var searchQuery: EditText
    private lateinit var tagQuery: EditText
    private lateinit var saveButton: Button
    private lateinit var clearTagsButton: Button
    private lateinit var taggedList: ListView

    private val tags = mutableListOf<String>()
    private var filteredTags = mutableListOf<String>()
    private lateinit var adapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        searchQuery = findViewById(R.id.searchQuery)
        tagQuery = findViewById(R.id.tagQuery)
        saveButton = findViewById(R.id.saveButton)
        clearTagsButton = findViewById(R.id.clearTagsButton)
        taggedList = findViewById(R.id.taggedList)

        filteredTags.addAll(tags)
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, filteredTags)
        taggedList.adapter = adapter

        saveButton.setOnClickListener {
            val tag = tagQuery.text.toString().trim()
            if (tag.isNotEmpty()) {
                tags.add(tag)
                filterTags(searchQuery.text.toString())
                tagQuery.text.clear()
            } else {
                Toast.makeText(this, "Enter a valid tag", Toast.LENGTH_SHORT).show()
            }
        }

        clearTagsButton.setOnClickListener {
            tags.clear()
            filterTags("")
        }

        taggedList.setOnItemClickListener { _, _, position, _ ->
            showEditDialog(position)
        }

        searchQuery.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterTags(s.toString()) // Filter the list as the user types
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun showEditDialog(position: Int) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Edit Tag")

        val input = EditText(this)
        input.setText(filteredTags[position])
        builder.setView(input)

        builder.setPositiveButton("Save") { _, _ ->
            val newTag = input.text.toString().trim()
            val originalIndex = tags.indexOf(filteredTags[position])
            if (originalIndex != -1) {
                tags[originalIndex] = newTag
                filterTags(searchQuery.text.toString())
            }
        }

        builder.setNegativeButton("Cancel", null)
        builder.show()
    }

    private fun filterTags(query: String) {
        filteredTags.clear()
        if (query.isEmpty()) {
            filteredTags.addAll(tags) // Show all if query is empty
        } else {
            filteredTags.addAll(tags.filter { it.contains(query, ignoreCase = true) })
        }
        adapter.notifyDataSetChanged()
    }
}
