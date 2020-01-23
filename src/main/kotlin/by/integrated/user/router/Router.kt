package by.integrated.user.router

import by.integrated.user.handler.Handler
import org.springframework.context.annotation.Bean
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.coRouter

@Component
class Router(private val handler: Handler) {

    companion object {
        const val PATH = "/api/users"
        const val BLANK = ""
        const val ID_PATTERN = "/{id}"
    }

    @Bean
    fun coRouter() = coRouter {

        PATH.nest {
            GET(BLANK).and(accept(MediaType.APPLICATION_JSON)).invoke(handler::all)
            GET(ID_PATTERN).and(accept(MediaType.APPLICATION_JSON)).invoke(handler::one)
            POST(BLANK).and(contentType(MediaType.APPLICATION_JSON)).invoke(handler::create)
            PUT(ID_PATTERN).and(contentType(MediaType.APPLICATION_JSON)).invoke(handler::update)
            DELETE(ID_PATTERN, handler::delete)
        }
    }
}