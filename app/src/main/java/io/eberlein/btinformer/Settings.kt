package io.eberlein.btinformer

import io.paperdb.Paper

class Settings(
    var gpsLogging: Boolean,
    var scanTime: Int
) {
    constructor(): this(false, 10000)

    fun save(){
        Paper.book("Settings").write("General", this)
    }

    companion object {
        fun get(): Settings {
            return Paper.book("Settings").read("General")
        }
    }
}