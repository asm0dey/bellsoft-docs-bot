package com.github.asm0dey.bell_sw_bot

import io.ktor.client.call.*
import org.springframework.ai.chat.client.ChatClient
import org.springframework.ai.chat.messages.UserMessage
import org.springframework.ai.chat.model.ChatModel
import org.springframework.ai.chat.prompt.Prompt
import org.springframework.ai.chat.prompt.PromptTemplate
import org.springframework.ai.chat.prompt.SystemPromptTemplate
import org.springframework.ai.vectorstore.SearchRequest
import org.springframework.ai.vectorstore.VectorStore
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import java.util.*

@RestController
class BotController(val vectorStore: VectorStore, chatModel: ChatModel, val unstructuredClient: UnstructuredClient) {
    private val chatClient = ChatClient.create(chatModel)


    @GetMapping("/search")
    fun query(@RequestParam q: String): String {
        val query = SearchRequest.query(q).withTopK(3)
        val docs = vectorStore.similaritySearch(query)
        val information = docs.joinToString("\n---||---\n") {
            """TITLE: ${it.metadata["filename"]}
            |BODY:
            |${it.content}
        """.trimMargin()
        }
        val systemPromptTemplate = SystemPromptTemplate(
            """You are a helpful assistant.

You find information in data provided below.
The format of data is following:
---
TITLE: title of the source post
BODY:
text of the document or its part
---
The delimiter between documents is "---||---"
If you have to reference a document, reference it by name.
Always reference a document where you found the information.
If there is no information, you  answer with a message
"Sorry, I do not have such an information".

Use the following information to answer the question:
---
{information}"""
        )
        val systemMessage = systemPromptTemplate.createMessage(
            mapOf("information" to information)
        )
        val userMessagePromptTemplate =
            PromptTemplate(""""{question}"""")
        val model = mapOf("question" to q)

        val userMessage = UserMessage(userMessagePromptTemplate.create(model).getContents())
        val prompt = Prompt(listOf(systemMessage, userMessage))
        val response = chatClient.prompt(prompt).call().content()
        return response
    }

    @PostMapping("/")
    suspend fun storeDocument(@RequestParam file: MultipartFile): ResponseEntity<Unit> {
        val responses: UnstructuredResponses = unstructuredClient.req(
            file = file,
            combineUnderNChars = 500,
            maxCharacters = 8192,
            includeOrigElements = false,
            chunkingStrategy = "basic",
        )
            .body()

        val docs = responses.map(::UnstructuredResponseDocumentAdapter)
        vectorStore.add(docs)
        return ResponseEntity.of(Optional.of(Unit))
    }
}