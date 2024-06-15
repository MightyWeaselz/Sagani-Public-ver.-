package service

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class TileFactoryTest {
    @Test
    fun getOriginalTiles() {
        val originalTiles = TileFactory.getOriginalTiles()
        assertEquals(72, originalTiles.size)
    }
}