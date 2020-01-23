package by.integrated.user.handler

import by.integrated.user.repository.Repository
import by.integrated.user.router.Router.Companion.PATH
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.awaitBody
import org.springframework.web.reactive.function.server.bodyAndAwait
import org.springframework.web.reactive.function.server.bodyValueAndAwait
import org.springframework.web.reactive.function.server.buildAndAwait
import java.net.URI

@Service
class Handler(private val repository: Repository) {

    companion object {
        const val PATH_VARIABLE = "id"
    }

    suspend fun all(request: ServerRequest) = ServerResponse.ok().bodyAndAwait(repository.all())

    suspend fun create(request: ServerRequest) =
        repository.create(request.awaitBody())?.let {
            ServerResponse.created(URI.create("${PATH}/$it")).buildAndAwait()
        }
            ?: ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).buildAndAwait()

    suspend fun one(request: ServerRequest) =
        repository.one(request.pathVariable(PATH_VARIABLE).toLong())?.let { ServerResponse.ok().bodyValueAndAwait(it) }
            ?: ServerResponse.notFound().buildAndAwait()

    suspend fun update(request: ServerRequest): ServerResponse {
        val id = request.pathVariable(PATH_VARIABLE).toLong()
        return repository.one(id)?.let {
            repository.update(id, request.awaitBody()).run { ServerResponse.noContent().buildAndAwait() }
        } ?: ServerResponse.notFound().buildAndAwait()
    }

    suspend fun delete(request: ServerRequest) =
        repository.one(request.pathVariable(PATH_VARIABLE).toLong())?.let {
            repository.delete(
                request.pathVariable(
                    PATH_VARIABLE
                ).toLong()
            ).let { ServerResponse.noContent().buildAndAwait() }
        } ?: ServerResponse.notFound().buildAndAwait()
}

