package feature_products

import core.client
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

object OpenAiRepository {


    suspend fun request(products: List<Product>): Result<Receipt> = runCatching {
        val content = client.post {
            url("/v1/chat/completions")
            setBody(
                Request(
                    model = "gpt-4o",
                    responseFormat = Request.Format("json_object"),
                    messages = listOf(
                        Request.Message(
                            role = "system",
                            content = "You are a Chef designed to output receipt in JSON from provided products. Do not numerate ingredients and instructions." +
                                    "Json object must have " +
                                    "field name with type string, " +
                                    "field ingredients that is a list of type string, " +
                                    "field instructions that is a list of type string"
                        ),
                        Request.Message(
                            role = "user",
                            content = products.joinToString(separator = ", ")
                        )
                    )
                )
            )
        }.body<Response>().choices.first().message.content
        Json.decodeFromString<Receipt>(content)
    }
}

@Serializable
data class Request(
    @SerialName("model") val model: String,
    @SerialName("response_format") val responseFormat: Format,
    @SerialName("messages") val messages: List<Message>,
) {

    @Serializable
    data class Format(@SerialName("type") val type: String)

    @Serializable
    data class Message(
        @SerialName("role") val role: String,
        @SerialName("content") val content: String,
    )
}

@Serializable
data class Response(@SerialName("choices") val choices: List<Choice>) {

    @Serializable
    data class Choice(@SerialName("message") val message: Message) {

        @Serializable
        data class Message(
            @SerialName("role") val role: String,
            @SerialName("content") val content: String,
        )
    }
}

@Serializable
data class Receipt(
    @SerialName("name") val name: String,
    @SerialName("ingredients") val ingredients: List<String>,
    @SerialName("instructions") val instructions: List<String>
)