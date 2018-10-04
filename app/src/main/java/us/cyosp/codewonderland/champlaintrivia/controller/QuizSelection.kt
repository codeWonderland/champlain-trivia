package us.cyosp.codewonderland.champlaintrivia.controller

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import us.cyosp.codewonderland.champlaintrivia.R
import us.cyosp.codewonderland.champlaintrivia.model.Question
import us.cyosp.codewonderland.champlaintrivia.model.Quiz
import java.io.IOException



class QuizSelection : AppCompatActivity() {

    object Data {
        val Quizzes = ArrayList<Quiz>()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.quiz_selection)

        parseXML()
    }

    private fun parseXML() {
        /* Based off of xml parsing tutorial.

        https://antonioleiva.com/functional-programming-android-kotlin-lambdas/
         */
        val dataStream = assets.open("quiz_data.xml")
        val parserFactory: XmlPullParserFactory?

        try {
            parserFactory = XmlPullParserFactory.newInstance()
            val parser = parserFactory!!.newPullParser()

            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
            parser.setInput(dataStream, null)

            processParsing(parser)

        } catch (e: IOException) {
            Log.d("QuizSelection", e.message)
        }

    }

    private fun processParsing(parser: XmlPullParser) {
        /* Based off of xml parsing tutorial.

        https://antonioleiva.com/functional-programming-android-kotlin-lambdas/
         */
        var quiz: Quiz? = null
        var question: Question? = null
        var eventType = parser.eventType

        while (eventType != XmlPullParser.END_DOCUMENT) {
            var eltName: String

            if (eventType == XmlPullParser.START_TAG) {
                eltName = parser.name

                when (eltName) {
                    "quiz" -> {
                        if (quiz != null) {
                            QuizSelection.Data.Quizzes.add(quiz)
                            question = null

                        }
                        val quizName = parser.getAttributeValue(null, "name")
                        quiz = Quiz(quizName)

                    }
                    "question" -> {
                        if (question != null) {
                            quiz!!.addQuestion(question)
                        }

                        val questionType = parser.getAttributeValue(null, "type")
                        question = Question(questionType)

                    }
                    "prompt" -> {
                        val prompt = parser.nextText()
                        question!!.setPrompt(prompt)
                    }
                    "answer" -> {
                        val correct = parser.getAttributeValue(null, "correct")
                        val answer = parser.nextText()

                        question!!.addAnswer(answer, correct!!.toBoolean())

                    }
                }
            }

            eventType = parser.next()
        }

        if (quiz != null) {
            QuizSelection.Data.Quizzes.add(quiz)
        }

        displayQuizzes()
    }

    private fun displayQuizzes() {
        val quiz0: Button = findViewById(R.id.quiz0)
        val quiz1: Button = findViewById(R.id.quiz1)
        val quiz2: Button = findViewById(R.id.quiz2)

        quiz0.text = QuizSelection.Data.Quizzes[0].mName
        quiz1.text = QuizSelection.Data.Quizzes[1].mName
        quiz2.text = QuizSelection.Data.Quizzes[2].mName

        attachQuiz(quiz0, 0)
        attachQuiz(quiz1, 1)
        attachQuiz(quiz2, 2)
    }

    private fun attachQuiz(button: Button, quizIndex: Int) {
        button.setOnClickListener {
            val intent = QuizActivity.Intent.newIntent(this, quizIndex)
            startActivity(intent)
        }
    }
}