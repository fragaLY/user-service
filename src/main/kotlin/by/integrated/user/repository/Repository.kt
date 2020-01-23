package by.integrated.user.repository

import by.integrated.user.domain.User
import kotlinx.coroutines.reactive.awaitFirstOrNull
import kotlinx.coroutines.reactive.awaitSingle
import org.springframework.data.r2dbc.core.DatabaseClient
import org.springframework.data.r2dbc.core.asType
import org.springframework.data.r2dbc.core.awaitOneOrNull
import org.springframework.data.r2dbc.core.flow
import org.springframework.data.r2dbc.core.into
import org.springframework.data.r2dbc.query.Criteria
import org.springframework.data.r2dbc.query.Update
import org.springframework.stereotype.Repository

@Repository
class Repository(private val client: DatabaseClient) {

    companion object {
        const val TABLE = "USER"
        const val ID = "ID"
        const val NAME = "NAME"
        const val EMAIL = "EMAIL"
        const val PHONE = "PHONE"
        const val PASSWORD = "PASSWORD"
    }

    suspend fun all() =
        client
            .select()
            .from(TABLE)
            .asType<User>()
            .fetch()
            .flow()

    suspend fun one(id: Long) =
        client
            .select()
            .from(TABLE)
            .matching(Criteria.where(ID).`is`(id))
            .asType<User>()
            .fetch()
            .awaitOneOrNull()

    suspend fun create(user: User) =
        client.insert()
            .into<User>()
            .table(TABLE)
            .using(user)
            .map { row, _ -> row.get(ID, Long::class.javaObjectType)?.toLong() }
            .awaitOneOrNull()

    suspend fun update(id: Long, user: User) =
        client
            .update()
            .table(TABLE)
            .using(
                Update.update(NAME, user.name)
                    .set(EMAIL, user.email)
                    .set(PHONE, user.phone)
                    .set(PASSWORD, user.password)
            )
            .matching(Criteria.where(ID).`is`(id))
            .fetch()
            .rowsUpdated()
            .awaitFirstOrNull()

    suspend fun delete(id: Long): Int =
        client
            .delete()
            .from(TABLE)
            .matching(Criteria.where(ID).`is`(id))
            .fetch()
            .rowsUpdated()
            .awaitSingle()
}