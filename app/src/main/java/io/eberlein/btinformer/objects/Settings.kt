package io.eberlein.btinformer.objects

import io.paperdb.Book
import io.paperdb.Paper
import io.paperdb.PaperDbException

class Settings(
    var gpsLogging: Boolean,
    var scanTime: Int
): DBObject() {
    companion object {
        private const val bookName: String = "settings"
        private const val settingId: String = "settings"

        private fun book(): Book {
            return Paper.book(bookName)
        }

        fun getOrCreate(): Settings {
            var r = book().read<Settings>(settingId)
            if (r == null) {
                r = Settings(false, 10000)
                r.save()
            }
            return r
        }
    }

    override fun save(){
        book().write(id, this)
    }
}