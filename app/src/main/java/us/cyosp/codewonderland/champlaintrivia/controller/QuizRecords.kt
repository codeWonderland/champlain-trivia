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
import java.io.IOException
import java.nio.charset.Charset

class QuizRecords : AppCompatActivity() {

    object Intent {
        const val EXTRA_USERNAME: String =
                "us.cyosp.codewonderland.champlaintrivia.username"

        const val EXTRA_SCORE: String =
                "us.cyosp.codewonderland.champlaintrivia.score"

        const val EXTRA_NEW_PERSON: String =
                "us.cyosp.codewonderland.champlaintrivia.new_person"

        fun newIntent(packageContext: Context): android.content.Intent {
            val intent = Intent(packageContext, QuizRecords::class.java)
            intent.putExtra(QuizRecords.Intent.EXTRA_NEW_PERSON, false)
            return intent
        }

        fun newIntent(packageContext: Context, score: Score): android.content.Intent {
            val intent = Intent(packageContext, QuizRecords::class.java)
            intent.putExtra(QuizRecords.Intent.EXTRA_NEW_PERSON, true)
            intent.putExtra(QuizRecords.Intent.EXTRA_USERNAME, score.username)
            intent.putExtra(QuizRecords.Intent.EXTRA_SCORE, score.score)
            return intent
        }
    }

    private val mScores = arrayListOf<Score>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.quiz_records)

        val newPerson: Boolean =
                this.intent!!.extras!!.getBoolean(QuizRecords.Intent.EXTRA_NEW_PERSON)

        val homeButton: Button = findViewById(R.id.home_button)

        initScores()

        if (newPerson) {
            val username = this.intent!!.extras!!.getString(QuizRecords.Intent.EXTRA_USERNAME)
            val score = this.intent!!.extras!!.getInt(QuizRecords.Intent.EXTRA_SCORE)

            if (username != null) {
                mScores.add(Score(username, score))

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
        val sortedScores = mScores.sortedByDescending { it.score }
        val topScores = sortedScores.take(5)

        findViewById<TextView>(R.id.username1).text = topScores[0].username
        findViewById<TextView>(R.id.username2).text = topScores[1].username
        findViewById<TextView>(R.id.username3).text = topScores[2].username
        findViewById<TextView>(R.id.username4).text = topScores[3].username
        findViewById<TextView>(R.id.username5).text = topScores[4].username

        findViewById<TextView>(R.id.score1).text = topScores[0].score.toString()
        findViewById<TextView>(R.id.score2).text = topScores[1].score.toString()
        findViewById<TextView>(R.id.score3).text = topScores[2].score.toString()
        findViewById<TextView>(R.id.score4).text = topScores[3].score.toString()
        findViewById<TextView>(R.id.score5).text = topScores[4].score.toString()
    }

    private fun saveScores() {
        // TODO: Implement this
    }

    private fun initScores() {

        val results = openResults()

        if (results != null) {
            try {
                /*
                I MODIFIED THIS CODE
                Author: GrIsHu
                Source: https://stackoverflow.com/questions/19945411/android-java-how-can-i-parse-a-local-json-file-from-assets-folder-into-a-listvi
                 */
                val resultsObj = JSONObject(results)
                val resultsArr = resultsObj.getJSONArray("results")

                for (i in 0..resultsArr.length()) {
                    val result: JSONObject = resultsArr.getJSONObject(i)

                    mScores.add(
                            Score(
                                    result.getString("name"),
                                    result.getInt("score")
                            )
                    )
                }

            } catch (e: JSONException) {
                e.printStackTrace()

            }
        }
    }

    private fun openResults(): String? {
        /* I MODIFIED THIS CODE
        Author: GrIsHu
        Source: https://stackoverflow.com/questions/19945411/android-java-how-can-i-parse-a-local-json-file-from-assets-folder-into-a-listvi
         */
        val json: String?

        try {
            val inputStream = assets.open("quiz_results.json")
            val size = inputStream.available()
            val buffer = ByteArray(size)

            inputStream.read(buffer)
            inputStream.close()
            json = String(buffer, Charset.defaultCharset())

        } catch (ex: IOException) {
            ex.printStackTrace()
            return null
        }

        return json
    }}