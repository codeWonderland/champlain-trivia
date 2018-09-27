package us.cyosp.codewonderland.champlaintrivia.controller

import android.util.Log
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import org.xmlpull.v1.XmlPullParserFactory
import us.cyosp.codewonderland.champlaintrivia.model.Question
import us.cyosp.codewonderland.champlaintrivia.model.Quiz
import java.io.IOException
import java.io.InputStream

class QuizManager(private val mDataStream: InputStream,
                  private val mSendQuizzes: (List<String>) -> Unit) {
    private val mQuizzes = mutableMapOf<String, Quiz>()

    private fun parseXML() {
        /* Based off of xml parsing tutorial.

        https://antonioleiva.com/functional-programming-android-kotlin-lambdas/
         */
        val parserFactory: XmlPullParserFactory?

        try {
            parserFactory = XmlPullParserFactory.newInstance()
            val parser = parserFactory!!.newPullParser()

            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
            parser.setInput(mDataStream, null)

            processParsing(parser)

        } catch (e: IOException) {
            Log.d("QuizManager", e.message)
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
                            this.mQuizzes[quiz.mName] = quiz
                            question = null

                        }
                        val quizName = parser.getAttributeValue(null, "name")
                        val quizImg = parser.getAttributeValue(null, "img")
                        quiz = Quiz(quizName, quizImg)

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
    }

    init {
        parseXML()

        this.mSendQuizzes(mQuizzes.keys.toList())
    }
}