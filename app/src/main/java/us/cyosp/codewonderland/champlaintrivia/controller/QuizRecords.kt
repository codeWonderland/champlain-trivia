package us.cyosp.codewonderland.champlaintrivia.controller

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import us.cyosp.codewonderland.champlaintrivia.model.Score

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

    private var mNewPerson = false
    private var mScore: Score? = null

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)

        mNewPerson = this.intent!!.extras!!.getBoolean(QuizRecords.Intent.EXTRA_NEW_PERSON)

        if (mNewPerson) {
            val username = this.intent!!.extras!!.getString(QuizRecords.Intent.EXTRA_USERNAME)
            val score = this.intent!!.extras!!.getInt(QuizRecords.Intent.EXTRA_SCORE)

            if (username != null) {
                mScore = Score(username, score)

            }
        }
    }
}