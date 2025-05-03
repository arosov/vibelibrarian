package ovh.devcraft.vibe.books

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform