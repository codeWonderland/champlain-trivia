package us.cyosp.codewonderland.champlaintrivia.model

class Quiz(val mName: String) {

    private var mScore = 0
    private var mCurrentQuestion = 0
    private val mQuestions = mutableListOf<Question>()

    fun addQuestion(question: Question) {
        mQuestions.add(question)
    }

    fun submitAnswer(answer: String) : Boolean {
        val result = mQuestions[mCurrentQuestion].checkAnswer(answer)

        if (result) mScore++

        return result
    }

    fun nextQuestion() : Question {
        this.mCurrentQuestion =
                (this.mCurrentQuestion + 1) % this.mQuestions.size

        return getQuestion()
    }

    fun onLastQuestion() : Boolean {
        return mCurrentQuestion == mQuestions.size - 1
    }

    fun checkScore() : Int {
        return mScore
    }

    fun getQuestion() : Question {
        return this.mQuestions[this.mCurrentQuestion]
    }
}