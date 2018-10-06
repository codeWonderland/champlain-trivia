package us.cyosp.codewonderland.champlaintrivia.controller

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.TextView
import org.json.JSONException
import org.json.JSONObject
import us.cyosp.codewonderland.champlaintrivia.R
import us.cyosp.codewonderland.champlaintrivia.model.Score
import java.io.File
import java.lang.Exception
import java.nio.charset.Charset

class QuizRecords : AppCompatActivity() {

    companion object {
        const val EXTRA_USERNAME: String =
                "us.cyosp.codewonderland.champlaintrivia.username"

        const val EXTRA_SCORE: String =
                "us.cyosp.codewonderland.champlaintrivia.score"

        const val EXTRA_NEW_PERSON: String =
                "us.cyosp.codewonderland.champlaintrivia.new_person"

        const val EXTRA_QUIZ_NAME: String =
                "us.cyosp.codewonderland.champlaintrivia.quiz_name"

        fun newIntent(packageContext: Context): android.content.Intent {
            val intent = Intent(packageContext, QuizRecords::class.java)
            intent.putExtra(QuizRecords.EXTRA_NEW_PERSON, false)
            return intent
        }

        fun newIntent(packageContext: Context,
                      score: Score): android.content.Intent {
            val intent = Intent(packageContext, QuizRecords::class.java)

            intent.putExtra(QuizRecords.EXTRA_NEW_PERSON, true)
            intent.putExtra(QuizRecords.EXTRA_USERNAME, score.username)
            intent.putExtra(QuizRecords.EXTRA_SCORE, score.score)
            intent.putExtra(QuizRecords.EXTRA_QUIZ_NAME, score.cat)

            return intent
        }
    }

    private val mScores = arrayListOf<Score>()
    private val mRecordsFile = "quiz_results.json"
    private var mQuizName: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.quiz_records)

        val newPerson: Boolean =
                this.intent!!.extras!!.getBoolean(QuizRecords.EXTRA_NEW_PERSON)

        val homeButton: Button = findViewById(R.id.home_button)

        val results = getResults()

        initScores(results)

        if (newPerson) {
            val username = this.intent!!.extras!!.getString(QuizRecords.EXTRA_USERNAME)
            val score = this.intent!!.extras!!.getInt(QuizRecords.EXTRA_SCORE)
            mQuizName = this.intent!!.extras!!.getString(QuizRecords.EXTRA_QUIZ_NAME)

            if (username != null && mQuizName != null) {
                mScores.add(Score(username, score, mQuizName!!))

                saveScores()

            }

        }

        displayScores()

        homeButton.setOnClickListener {
            setResult(Activity.RESULT_OK)
            this.finish()
        }

    }

    private fun displayScores() {
        val scores = mScores.asSequence()
                .filter { mQuizName == null || it.cat == mQuizName}
                .sortedByDescending { it.score }.toList()
                .take(5)

        findViewById<TextView>(R.id.username1).text = scores[0].username
        findViewById<TextView>(R.id.username2).text = scores[1].username
        findViewById<TextView>(R.id.username3).text = scores[2].username
        findViewById<TextView>(R.id.username4).text = scores[3].username
        findViewById<TextView>(R.id.username5).text = scores[4].username

        findViewById<TextView>(R.id.score1).text = scores[0].score.toString()
        findViewById<TextView>(R.id.score2).text = scores[1].score.toString()
        findViewById<TextView>(R.id.score3).text = scores[2].score.toString()
        findViewById<TextView>(R.id.score4).text = scores[3].score.toString()
        findViewById<TextView>(R.id.score5).text = scores[4].score.toString()
    }

    private fun saveScores() {
        val scores = mScores.map {
            score -> score.toString()
        }

        val output = "{ \"results\" : $scores }"

        this.openFileOutput(mRecordsFile, Context.MODE_PRIVATE).use {
            it.write(output.toByteArray())
        }
    }

    private fun initScores(results: String?) {

        if (results != null) {
            try {
                /*
                I MODIFIED THIS CODE
                Author: GrIsHu
                Source: https://stackoverflow.com/questions/19945411/android-java-how-can-i-parse-a-local-json-file-from-assets-folder-into-a-listvi
                 */
                val resultsObj = JSONObject(results)
                val resultsArr = resultsObj.getJSONArray("results")

                // Iterate over every result object
                for (i in 0..resultsArr.length()) {
                    // Grab the current result
                    val result: JSONObject = resultsArr.getJSONObject(i)

                    // Add the result to mScores
                    mScores.add(
                            // cast values to Score
                            Score(
                                    result.getString("name"),
                                    result.getInt("score"),
                                    result.getString("cat")
                            )
                    )
                }

            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }
    }

    private fun getResults(): String? {
        /* I MODIFIED THIS CODE
        Author: GrIsHu
        Source: https://stackoverflow.com/questions/19945411/android-java-how-can-i-parse-a-local-json-file-from-assets-folder-into-a-listvi
         */
        val json: String?

        val file = File(this.filesDir, mRecordsFile)

        val inputStream = if (file.exists()) {
            this.openFileInput(mRecordsFile)

        } else {
            assets.open(mRecordsFile)
        }

        try {
            val size = inputStream.available()
            val buffer = ByteArray(size)

            inputStream.read(buffer)
            inputStream.close()
            json = String(buffer, Charset.defaultCharset())

        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }

        return json
    }}