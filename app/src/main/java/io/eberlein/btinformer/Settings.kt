package io.eberlein.btinformer

import io.paperdb.Book
import io.paperdb.Paper

class Settings(
    var gpsLogging: Boolean,
    var scanTime: Int
): DBObject() {
    companion object {
        private const val bookName: String = "settings"
        private const val settingId: String = "00000000-0000-0000-0000-000000000000"

        private fun book(): Book {
            return Paper.book(bookName)
        }

        fun getOrCreate(): Settings {
            var r = book().read<Settings>(settingId)
            if(r == null) r = Settings(false, 10000)
            book().write(settingId, r)
            return r
        }
    }
}