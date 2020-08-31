package io.eberlein.btinformer.objects

import android.location.Location
import io.paperdb.Book
import io.paperdb.Paper
import java.sql.Timestamp

class GPSLogEntry(
    val timestamp: Timestamp,
    val device: Device,
    val filter: Filter,
    val location: Location
): DBObject() {
    companion object {
        private const val bookName = "gpsLogEntries"

        private fun book(): Book {
            return Paper.book(bookName)
        }

        fun getAll(): ArrayList<GPSLogEntry> {
            val r = ArrayList<GPSLogEntry>()
            for(k in book().allKeys) r.add(book().read(k))
            return r
        }
    }
}