package com.example.legocatalog.util

import com.example.legocatalog.legoset.data.LegoSet

object DomainObjectFactory {

    fun createLegoSet() = LegoSet("id_123", "Fantastic Car", "imageUrl", 31)

    fun createLegoSets(count: Int): List<LegoSet> {
        return (0 until count).map {
            createLegoSet()
        }
    }

}
