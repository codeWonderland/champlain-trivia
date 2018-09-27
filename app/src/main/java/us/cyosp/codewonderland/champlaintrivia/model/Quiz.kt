package us.cyosp.codewonderland.champlaintrivia.model

class Quiz(val mName: String) {

    private var mScore = 0
    private var mCurrentQuestion = 0
    private val mQuestions = mutableListOf<Question>()

    fun addQuestion(question: Question) {
        mQuestions.add(question)
    }

    fun checkQuestion(answer: String) : Boolean {
        val result = mQuestions[mCurrentQuestion].checkAnswer(answer)

        if (result) mScore++

        return result
    }

    fun checkScore() : Int {
        return mScore
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