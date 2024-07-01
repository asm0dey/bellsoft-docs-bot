package com.github.asm0dey.bell_sw_bot

import io.ktor.client.*
import io.ktor.client.engine.java.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.http.*
import io.ktor.http.ContentType.Application.Json
import io.ktor.http.ContentType.MultiPart.FormData
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile
import java.math.BigDecimal

@Component
class UnstructuredClient(val unstructuredConfig: UnstructuredConfig) {
    private val client = HttpClient(Java) {
        install(ContentNegotiation) {
            this.json()
            json(Json {
                allowStructuredMapKeys = true
                ignoreUnknownKeys = true
            })
        }
    }

    suspend fun req(
        file: MultipartFile,
        coordinates: Boolean? = null,
        encoding: String? = null,
        extractImageBlockTypes: List<String>? = null,
        gzUncompressedContentType: String? = null,
        hiResModelName: String? = null,
        includePageBreaks: Boolean? = null,
        languages: List<String>? = null,
        ocrLanguages: List<String>? = null,
        outputFormat: String? = null,
        pdfInferTableStructure: Boolean? = null,
        skipInferTableTypes: List<String>? = null,
        startingPageNumber: Int? = null,
        strategy: String? = null,
        uniqueElementIds: Boolean? = null,
        xmlKeepTags: Boolean? = null,
        chunkingStrategy: String? = null,
        combineUnderNChars: Int? = null,
        includeOrigElements: Boolean? = null,
        maxCharacters: Int? = null,
        multipageSections: Boolean? = null,
        newAfterNChars: Int? = null,
        overlap: Int? = null,
        overlapAll: Boolean? = null,
        similarityThreshold: BigDecimal? = null
    ) = client
        .submitFormWithBinaryData(
            url = unstructuredConfig.endpoint,
            formData = formData {
                file.apply {
                    append("files", file.bytes, Headers.build {
                        append(HttpHeaders.ContentDisposition, "filename=\"${file.originalFilename}\"")
                    })
                }
                coordinates?.apply { append("coordinates", coordinates.toString()) }
                encoding?.apply { append("encoding", encoding) }
                extractImageBlockTypes?.forEach { append("extract_image_block_types", it) }
                gzUncompressedContentType?.apply {
                    append(
                        "gz_uncompressed_content_type",
                        gzUncompressedContentType
                    )
                }
                hiResModelName?.apply { append("hi_res_model_name", hiResModelName) }
                includePageBreaks?.apply { append("include_page_breaks", includePageBreaks.toString()) }
                languages?.forEach { append("languages", it) }
                ocrLanguages?.forEach { append("ocr_languages", it) }
                outputFormat?.apply { append("output_format", outputFormat) }
                pdfInferTableStructure?.apply {
                    append(
                        "pdf_infer_table_structure",
                        pdfInferTableStructure.toString()
                    )
                }
                skipInferTableTypes?.forEach { append("skip_infer_table_types", it) }
                startingPageNumber?.apply { append("starting_page_number", startingPageNumber.toString()) }
                strategy?.apply { append("strategy", strategy) }
                uniqueElementIds?.apply { append("unique_element_ids", uniqueElementIds.toString()) }
                xmlKeepTags?.apply { append("xml_keep_tags", xmlKeepTags.toString()) }
                chunkingStrategy?.apply { append("chunking_strategy", chunkingStrategy) }
                combineUnderNChars?.apply { append("combine_under_n_chars", combineUnderNChars.toString()) }
                includeOrigElements?.apply { append("include_orig_elements", includeOrigElements.toString()) }
                maxCharacters?.apply { append("max_characters", maxCharacters.toString()) }
                multipageSections?.apply { append("multipage_sections", multipageSections.toString()) }
                newAfterNChars?.apply { append("new_after_n_chars", newAfterNChars.toString()) }
                overlap?.apply { append("overlap", overlap.toString()) }
                overlapAll?.apply { append("overlap_all", overlapAll.toString()) }
                similarityThreshold
                    ?.apply { append("similarity_threshold", similarityThreshold) }
            }
        ) {
            accept(Json)
            contentType(FormData)
            header("unstructured-api-key", unstructuredConfig.key)
        }
}