import com.muslima.yya.data.remote.WsMessage
import com.muslima.yya.domain.model.Student
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

fun main() {
    val student = Student("1", "John", "Doe", "123", 20, "M", "1")
    val msg: WsMessage = WsMessage.Join(student, "sample_quiz_id")
    val text = Json.encodeToString(msg)
    println(text)
}
