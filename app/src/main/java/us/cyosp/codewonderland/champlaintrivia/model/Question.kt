package us.cyosp.codewonderland.champlaintrivia.model

class Question(val mType: String) {

    private val mAnswers = mutableListOf<Answer>()
    private var mPrompt = ""

    private data class Answer(val mValue: String, val mCorrect: Boolean)

    fun setPrompt(prompt: String) {
        this.mPrompt = prompt
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

    fun checkAnswer(value: String): Boolean {
        return mAnswers.fold(false) {
            acc, cur ->
            return if (cur.mValue == value) {
                cur.mCorrect
            } else {
                acc
            }
        }
    }
}