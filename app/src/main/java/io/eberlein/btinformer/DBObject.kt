package io.eberlein.btinformer

import io.paperdb.Book
import io.paperdb.Paper
import java.util.*

open class DBObject (
    var id: String
) {
    constructor(): this(UUID.randomUUID().toString())

    companion object {
        private const val bookName = "dbobject"

        fun book(): Book {
            return Paper.book(bookName)
        }
    }

    open fun save(){
        book().write(id, this)
    }

    fun delete(){
        book().delete(id)
    }
}