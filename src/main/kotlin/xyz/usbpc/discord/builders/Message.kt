package xyz.usbpc.discord.builders

import java.time.ZonedDateTime

fun message(init: LegoMessage.() -> Unit) : LegoMessage {
    val legoMessage = LegoMessage()
    legoMessage.init()
    return legoMessage
}
class LegoMessage {
    var content: String? = null
    var embed: LegoEmbed? = null
    fun embed(init: LegoEmbed.() -> Unit) {
        val embed = LegoEmbed()
        embed.init()
        this.embed = embed
    }
    fun toJson() = buildString {
        append('{')
        if (content != null) {
            append("\"content\":\"$content\"")
        }
        if (content != null && embed != null) {
            append(',')
        }
        if (embed != null) {
            append(embed!!.toJson())
        }
        append('}')
    }
}
class LegoEmbed {
    var title: String? = null
    var description: String? = null
    var url: String? = null
    var timestamp: ZonedDateTime? = null
    var color: Int = 0
    val fields = mutableListOf<Field>()
    var author: Author? = null
    var footer: Footer? = null
    class Field {
        var name = ""
        var value = ""
        var inline = false
        fun toJson(): String = buildString {
            append('{')
            append("\"name\":")
            append("\"$name\"")
            append(',')
            append("\"value\":")
            append("\"$value\"")
            if (inline) {
                append(',')
                append("\"inline\":true")
            }
            append('}')
        }
    }
    fun field(init: Field.() -> Unit) {
        val field = Field()
        field.init()
        fields.add(field)
    }
    class Author {
        lateinit var name: String
        var url: String? = null
        var iconUrl: String? = null
        fun toJson() = buildString {
            append("\"author\":")
            append('{')
            append("\"name\":\"$name\"")
            if (url != null) {
                append(',')
                append("\"url\":\"$url\"")
            }
            if (iconUrl != null) {
                append(',')
                append("\"icon_url\":\"$iconUrl\"")
            }
            append('}')
        }
    }
    fun author(init: Author.() -> Unit) {
        val author = Author()
        author.init()
        this.author = author
    }
    class Footer {
        lateinit var text: String
        var iconUrl: String? = null
        fun toJson() = buildString {
            append("\"footer\":")
            append('{')
            append("\"text\":\"$text\"")
            if (iconUrl != null) {
                append(',')
                append("\"icon_url\":\"$iconUrl\"")
            }
            append('}')
        }
    }
    fun footer(init: Footer.() -> Unit) {
        val footer = Footer()
        footer.init()
        this.footer = footer
    }
    fun toJson(): String = buildString {
        append("\"embed\":")
        append('{')
        if (title != null) {
            append("\"title\":\"$title\"").append(',')
        }
        if (description != null) {
            append("\"description\":\"$description\"").append(',')
        }
        if (url != null) {
            append("\"url\":\"$url\"").append(',')
        }
        append("\"color\": $color").append(',')
        if (timestamp != null) {
            append("\"timestamp\":${timestamp!!.toLocalDateTime()}").append(',')
        }
        if (footer != null) {
            append(footer!!.toJson()).append(',')
        }
        if (author != null) {
            append(author!!.toJson()).append(',')
        }
        if (fields.isNotEmpty()) {
            append("\"fields\":").append('[')
            for (field in fields) {
                append(field.toJson()).append(',')
            }
            setCharAt(this.length - 1, ']')
        }
        append('}')
    }
}



fun main(args: Array<String>) {
    val msg = message {
        content = "This is a nice message!"
        embed {
            title = "This is an embed"
            description = "**This is nice**"
            field {
                name = "First field"
                value = "this is a nice field!"
            }
            footer {
                text = "That was not a rhyme"
            }
        }
    }
    println(msg.toJson())
}