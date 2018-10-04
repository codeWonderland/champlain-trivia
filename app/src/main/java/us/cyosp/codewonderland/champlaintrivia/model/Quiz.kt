package us.cyosp.codewonderland.champlaintrivia.model

class Quiz(val mName: String) {

    private var mScore = 0
    private var mCurrentQuestion = 0
    private val mQuestions = mutableListOf<Question>()

    fun addQuestion(question: Question) {
        mQuestions.add(question)
    }

    fun submitAnswer(answer: String) {
        val result = mQuestions[mCurrentQuestion].checkAnswer(answer)

        if (result) mScore++
    }

    fun nextQuestion(answer: String) : Question {
        this.mCurrentQuestion =
                (this.mCurrentQuestion + 1) % this.mQuestions.size

        return getQuestion()
    }

    fun checkScore() : Int {
        return mScore
    }

    fun getQuestion() : Question {
        return this.mQuestions[this.mCurrentQuestion]
    }
}