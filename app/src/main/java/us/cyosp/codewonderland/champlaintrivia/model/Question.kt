package us.cyosp.codewonderland.champlaintrivia.model

class Question(val mType: String) {

    private val mAnswers = mutableListOf<Answer>()
    private var mPrompt = ""

    private data class Answer(val mValue: String, val mCorrect: Boolean)

    fun setPrompt(prompt: String) {
        this.mPrompt = prompt
    }

    fun getPrompt() : String {
        return mPrompt
    }

    fun addAnswer(value: String, correct: Boolean) {
        mAnswers.add(Answer(value, correct))
    }

    fun getAnswers(): List<String> {
        return mAnswers.map {
            answer ->
                answer.mValue
        }
    }

    fun checkAnswer(answer: String): Boolean {

        for (option in mAnswers) {
            if (option.mValue == answer) {
                return option.mCorrect
            }
        }

        return false
    }
}