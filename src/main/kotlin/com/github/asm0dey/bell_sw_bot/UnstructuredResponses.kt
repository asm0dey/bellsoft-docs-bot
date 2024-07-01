// To parse the JSON, install kotlin's serialization plugin and do:
//
// val json                  = Json { allowStructuredMapKeys = true }
// val unstructuredResponses = json.parse(UnstructuredResponses.serializer(), jsonString)

package com.github.asm0dey.bell_sw_bot

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.springframework.ai.document.Document
import java.util.UUID

typealias UnstructuredResponses = List<UnstructuredResponse>

@Serializable
data class UnstructuredResponse(
    val type: String,

    @SerialName("element_id")
    val elementID: String,

    val text: String,
    val metadata: Metadata
)

class UnstructuredResponseDocumentAdapter(response: UnstructuredResponse) :
    Document(UUID.nameUUIDFromBytes(response.elementID.toByteArray()).toString(), response.text, response.metadata.asMap()) {
    override fun toString(): String = "UnstructuredResponseDocumentAdapter() ${super.toString()}"
}

@Serializable
data class Metadata(
    @SerialName("category_depth")
    val categoryDepth: Long? = null,

    val languages: List<String>,
    @SerialName("link_texts")
    val linkTexts: List<String>? = null,
    @SerialName("link_urls")
    val linkUrls: List<String>? = null,
    val filename: String,
    val filetype: String,

    @SerialName("parent_id")
    val parentID: String? = null,

    @SerialName("emphasized_text_contents")
    val emphasizedTextContents: List<String>? = null,

    @SerialName("emphasized_text_tags")
    val emphasizedTextTags: List<String>? = null,

    @SerialName("orig_elements")
    val origElements: String? = null,

    @SerialName("is_continuation")
    val isContinuation: Boolean? = null,
) {
    @Suppress("UNCHECKED_CAST")
    fun asMap(): Map<String, Any> = mapOf(
        "categoryDepth" to categoryDepth,
        "languages" to languages,
        "filename" to filename,
        "filetype" to filetype,
        "parentID" to parentID,
        "isContinuation" to isContinuation,
    ).filterValues { it != null } as Map<String, Any>
}
