package us.cyosp.codewonderland.champlaintrivia.model

import org.json.JSONArray
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.FileReader

data class Score(val username: String, val score: Int)