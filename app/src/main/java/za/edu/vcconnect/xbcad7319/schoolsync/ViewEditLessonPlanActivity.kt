package za.edu.vcconnect.xbcad7319.schoolsync

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.view.Gravity
import android.widget.*
import android.widget.ImageButton
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class ViewEditLessonPlanActivity : AppCompatActivity() {

    private lateinit var tableLayout: TableLayout

    // Sample data for the lesson plan
    private var lessonPlan = arrayOf(
        arrayOf("Monday", "Reading - Themes", "Spanish - Verbs", "Geography - North America", "Math - Problem-solving", "Physical Education"),
        arrayOf("Tuesday", "Library - Book Sale", "Arts - Painting", "Math - Long Division", "Music - Instruments", ""),
        arrayOf("Wednesday", "Reading - Character Analysis", "Spanish - Verbs", "Geography - North America", "Math - Problem-solving", "Physical Education"),
        arrayOf("Thursday", "Civic Studies - Socialization", "Arts - Painting", "Math - Long Division", "Music - Instruments", ""),
        arrayOf("Friday", "Reading - Reflections", "Spanish - Verbs", "Geography - North America", "Math - Problem-solving", "Physical Education")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_edit_lesson_plan)

        val backButton = findViewById<ImageButton>(R.id.BckBtn)
        tableLayout = findViewById(R.id.tableLayout)

        // Populate the table with sample data
        populateTable()

        // Add/Edit Lesson Plan Button
        val addLessonButton: Button = findViewById(R.id.addLessonButton)
        addLessonButton.setOnClickListener {
            Toast.makeText(this, "Add/Edit functionality can be expanded here.", Toast.LENGTH_SHORT).show()
        }

        backButton.setOnClickListener {
            backButton.setOnClickListener {
                val intent = Intent(this, DashboardActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun populateTable() {
        // Create the header row
        val headerRow = TableRow(this)
        headerRow.gravity = Gravity.CENTER
        val headers = arrayOf("Day", "Subject/Activity 1", "Subject/Activity 2", "Subject/Activity 3", "Subject/Activity 4", "Edit")
        for (header in headers) {
            val textView = TextView(this).apply {
                text = header
                gravity = Gravity.CENTER
                setPadding(8, 8, 8, 8)
                textSize = 14f
            }
            headerRow.addView(textView)
        }
        tableLayout.addView(headerRow)

        // Create data rows
        for (i in lessonPlan.indices) {
            val row = TableRow(this)

            for (j in lessonPlan[i].indices) {
                val textView = TextView(this).apply {
                    text = lessonPlan[i][j]
                    gravity = Gravity.CENTER
                    setPadding(8, 8, 8, 8)
                }
                row.addView(textView)
            }

            // Add "Edit" button
            val editButton = Button(this).apply {
                text = "Edit"
                setOnClickListener {
                    showEditDialog(i)
                }
            }
            row.addView(editButton)

            tableLayout.addView(row)
        }
    }

    private fun showEditDialog(rowIndex: Int) {
        // Create a dialog to edit the lesson plan for the selected row
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Edit Lesson Plan for ${lessonPlan[rowIndex][0]}")

        // Create a layout for the dialog
        val dialogLayout = TableLayout(this)
        val editTexts = mutableListOf<EditText>()

        for (i in 1 until lessonPlan[rowIndex].size) {
            val editText = EditText(this).apply {
                hint = "Edit ${lessonPlan[rowIndex][i]}"
                setText(lessonPlan[rowIndex][i])
                inputType = InputType.TYPE_CLASS_TEXT
            }
            dialogLayout.addView(editText)
            editTexts.add(editText)
        }

        builder.setView(dialogLayout)

        // Save Button
        builder.setPositiveButton("Save") { _, _ ->
            for (i in editTexts.indices) {
                lessonPlan[rowIndex][i + 1] = editTexts[i].text.toString()
            }
            // Refresh the table
            tableLayout.removeAllViews()
            populateTable()
            Toast.makeText(this, "Lesson Plan Updated!", Toast.LENGTH_SHORT).show()
        }

        // Cancel Button
        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.dismiss()
        }

        builder.create().show()
    }


}