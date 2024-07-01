package com.github.asm0dey.bell_sw_bot

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("unstructured")
class UnstructuredConfig(val key: String, val endpoint: String)