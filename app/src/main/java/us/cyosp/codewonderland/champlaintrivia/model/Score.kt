package us.cyosp.codewonderland.champlaintrivia.model

class Score(val username: String, val score: Int, val cat: String) {
    override fun toString() : String {
        return "{" +
                "  \"name\" : \"$username\"," +
                "  \"score\" : $score," +
                "  \"cat\" : \"$cat\"" +
                "}"
    }
}