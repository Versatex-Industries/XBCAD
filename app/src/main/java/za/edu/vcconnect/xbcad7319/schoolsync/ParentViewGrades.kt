package za.edu.vcconnect.xbcad7319.schoolsync

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import okhttp3.*
import org.json.JSONArray
import java.io.IOException

class ParentViewGrades : AppCompatActivity() {

    private lateinit var spinnerChildren: Spinner
    private lateinit var gradesListContainer: LinearLayout

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_grades)

        spinnerChildren = findViewById(R.id.spinnerChildren)
        gradesListContainer = findViewById(R.id.gradesListContainer)

        // Fetch and populate children
        fetchChildren()

        // Set listener for selected child
        spinnerChildren.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                fetchGrades(spinnerChildren.selectedItem.toString())
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun fetchChildren() {
        // Simulated backend call to fetch children
        val children = listOf("John Doe", "Jane Doe") // Replace with API call result
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, children)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerChildren.adapter = adapter
    }

    private fun fetchGrades(childName: String) {
        // Simulated API call to fetch grades
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("https://your-backend-api.com/grades?childName=$childName") // Replace with your actual API URL
            .get()
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    Toast.makeText(this@ParentViewGrades, "Failed to load grades: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                val jsonResponse = JSONArray(response.body?.string())
                runOnUiThread {
                    displayGrades(jsonResponse)
                }
            }
        })
    }

    private fun displayGrades(gradesData: JSONArray) {
        gradesListContainer.removeAllViews()

        for (i in 0 until gradesData.length()) {
            val grade = gradesData.getJSONObject(i)
            val subject = grade.getString("subject")
            val mark = grade.getInt("mark")

            val gradeView = LinearLayout(this).apply {
                orientation = LinearLayout.HORIZONTAL
                setPadding(8, 8, 8, 8)
            }

            val subjectTextView = TextView(this).apply {
                text = subject
                layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
                textSize = 16f
            }

            val markTextView = TextView(this).apply {
                text = "$mark%"
                textSize = 16f
                layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            }

            gradeView.addView(subjectTextView)
            gradeView.addView(markTextView)
            gradesListContainer.addView(gradeView)
        }
    }
}