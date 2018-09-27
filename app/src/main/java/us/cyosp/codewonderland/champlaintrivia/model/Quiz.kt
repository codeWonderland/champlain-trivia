package us.cyosp.codewonderland.champlaintrivia.model

class Quiz(val mName: String,
           val mImg: String) {

    private var mScore = 0
    private var mCurrentQuestion = 0
    private val mQuestions = mutableListOf<Question>()

    fun addQuestion(question: Question) {
        mQuestions.add(question)
    }

    fun checkQuestion(answer: String) : Boolean {
        return mQuestions[mCurrentQuestion].checkAnswer(answer)
    }

    private fun getQuestion() : Question {
        return this.mQuestions[this.mCurrentQuestion]
    }

    fun nextQuestion() : Question {
        this.mCurrentQuestion =
                (this.mCurrentQuestion + 1) % this.mQuestions.size

        return getQuestion()
    }
}