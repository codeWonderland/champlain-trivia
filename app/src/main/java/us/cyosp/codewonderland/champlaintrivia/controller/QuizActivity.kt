package us.cyosp.codewonderland.champlaintrivia.controller

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import us.cyosp.codewonderland.champlaintrivia.R
import us.cyosp.codewonderland.champlaintrivia.model.Question
import us.cyosp.codewonderland.champlaintrivia.model.Quiz
import java.lang.Exception
import us.cyosp.codewonderland.champlaintrivia.util.BeatBox


class QuizActivity : AppCompatActivity() {

    // Modified version of class from chapter 21
    private data class Sound(val mName: String, val mPath: String, var mSoundId: Int)

    private val TAG: String = "QuizActivity"
    private val REQUEST_CODE_RESULTS = 0

    private var mQuizIndex = -1
    private var mQuiz: Quiz? = null
    private var mPrompt: TextView? = null
    private var mAnswers = ArrayList<Button>()
    private var mNext: Button? = null
    private var mHint: Button? = null
    private var mQuestion: Question? = null
    private var mBeatBox: BeatBox? = null

    companion object {
        const val EXTRA_QUIZ_INDEX =
                "us.cyosp.codewonderland.champlaintrivia.quiz_data"

        fun newIntent(packageContext: Context, quizIndex: Int): android.content.Intent {
            val intent = Intent(packageContext, QuizActivity::class.java)
            intent.putExtra(EXTRA_QUIZ_INDEX, quizIndex)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.quiz)

        // grab the parts of our layout we need to modify
        mPrompt = findViewById(R.id.question)
        mAnswers.add(findViewById(R.id.answer0))
        mAnswers.add(findViewById(R.id.answer1))
        mAnswers.add(findViewById(R.id.answer2))
        mAnswers.add(findViewById(R.id.answer3))
        mNext = findViewById(R.id.nextButton)
        mHint = findViewById(R.id.hintButton)

        // set up quiz
        mQuizIndex = this.intent!!.extras!!.getInt(QuizActivity.EXTRA_QUIZ_INDEX)

        mQuiz = QuizSelection.sQuizzes[mQuizIndex]
        mQuestion = mQuiz!!.getQuestion()

        setQuestion()

        // add functionality to next button
        setNextListener()

        // add sound functionality
        mBeatBox = BeatBox(this)

    }

    override fun onActivityResult(requestCode: Int,
                                  resultCode: Int,
                                  data: android.content.Intent?) {
        if (resultCode != Activity.RESULT_OK) {
            return
        }

        if (requestCode == this.REQUEST_CODE_RESULTS) {
            setResult(Activity.RESULT_OK)
            this.finish()
        }
    }

    private fun setQuestion() {
        mPrompt!!.text = mQuestion!!.getPrompt()

        val answers = mQuestion!!.getAnswers()

        // update answer buttons with current options
        for (i in answers.indices) {
            if (mQuestion!!.mType == "text") {
                mAnswers[i].text = answers[i]
            } else {
                try {  // TODO: Test this
                    mAnswers[i].setBackgroundResource(
                            resources.getIdentifier(answers[i], "String", packageName)
                    )
                } catch (e: Exception) {

                }
            }
        }

        // add event listeners to answer buttons
        for (answer in mAnswers) {
            setAnswerListener(answer)
        }

        // determine a random wrong answer to remove for hint
        var wrongAnswer: Button? = null

        for (answer in mAnswers.shuffled()) {

            if (!mQuestion!!.checkAnswer(answer.text.toString())) {
                wrongAnswer = answer
                break
            }
        }

        // make hint visible and setup listener
        mHint!!.visibility = View.VISIBLE
        mHint!!.setOnClickListener {

            // remove incorrect option and make hint button disappear
            wrongAnswer?.visibility = View.GONE
            mHint!!.visibility = View.GONE

            mHint!!.setOnClickListener(null)
        }
    }

    private fun setAnswerListener(answer: Button) {
        answer.setOnClickListener {
            for (button in mAnswers) {
                if (button.text != answer.text) {
                    button.setBackgroundColor(Color.GRAY)
                }

                clearListener(button)
            }

            val result = mQuiz!!.submitAnswer(answer.text.toString())

            Toast.makeText(this, result.toString(), Toast.LENGTH_SHORT).show()
            playSound(result)

            if (mQuiz!!.isLastQuestion()) {
                mNext!!.setText(R.string.submit_button)
            }

            mHint!!.visibility = View.GONE
            mNext!!.visibility = View.VISIBLE
        }
    }

    private fun setNextListener() {
        mNext!!.setOnClickListener {
            /*
            determine if we are on the last question
            then do appropriate setup and scene change
             */
            mNext!!.visibility = View.GONE

            if (mQuiz!!.isLastQuestion()) {
                /*
                create intent with score and start
                results activity with intent
                 */

                val intent = QuizResults.newIntent(this, mQuiz!!.getScore())
                intent.putExtra(QuizActivity.EXTRA_QUIZ_INDEX, mQuizIndex)
                startActivityForResult(intent, REQUEST_CODE_RESULTS)

            } else {
                /*
                if not the last question we do
                some setup then reset the stage
                */
                mQuestion = mQuiz!!.nextQuestion()

                for (answer in mAnswers) {
                    /*
                    return answer buttons to
                    blue and make them visible
                     */
                    answer.setBackgroundColor(Color.parseColor("#236192"))
                    answer.visibility = View.VISIBLE
                }

                setQuestion()
            }
        }
    }

    private fun clearListener(button: Button) {
        button.setOnClickListener(null)
    }

    private fun playSound(correct: Boolean) {
        val path = if (correct) {
            "sounds/success.mp3"

        } else {
            "sounds/failure.mp3"
        }

        for (sound in mBeatBox!!.sounds) {
            if (sound.assetPath == path) {
                mBeatBox!!.play(sound)
            }
        }
    }
}