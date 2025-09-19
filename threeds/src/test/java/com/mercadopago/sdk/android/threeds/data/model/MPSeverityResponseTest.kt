package com.mercadopago.sdk.android.threeds.data.model

import org.junit.Test
import org.junit.Assert.*

class MPSeverityResponseTest {

    @Test
    fun `MPSeverityResponse should have all expected values`() {
        // Arrange
        val expectedSeverities = listOf(
            MPSeverityResponse.LOW,
            MPSeverityResponse.MEDIUM,
            MPSeverityResponse.HIGH,
            MPSeverityResponse.NONE
        )

        // Act
        val allValues = MPSeverityResponse.values().toList()

        // Assert
        assertEquals(4, allValues.size)
        expectedSeverities.forEach { expectedSeverity ->
            assertTrue("Missing severity: $expectedSeverity", allValues.contains(expectedSeverity))
        }
    }

    @Test
    fun `MPSeverityResponse should have correct ordinal values`() {
        // Act & Assert
        assertEquals(0, MPSeverityResponse.LOW.ordinal)
        assertEquals(1, MPSeverityResponse.MEDIUM.ordinal)
        assertEquals(2, MPSeverityResponse.HIGH.ordinal)
        assertEquals(3, MPSeverityResponse.NONE.ordinal)
    }

    @Test
    fun `getWaningByGrade should return LOW for grade 0`() {
        // Act
        val actualSeverity = MPSeverityResponse.getWaningByGrade(0)

        // Assert
        assertEquals(MPSeverityResponse.LOW, actualSeverity)
    }

    @Test
    fun `getWaningByGrade should return MEDIUM for grade 1`() {
        // Act
        val actualSeverity = MPSeverityResponse.getWaningByGrade(1)

        // Assert
        assertEquals(MPSeverityResponse.MEDIUM, actualSeverity)
    }

    @Test
    fun `getWaningByGrade should return HIGH for grade 2`() {
        // Act
        val actualSeverity = MPSeverityResponse.getWaningByGrade(2)

        // Assert
        assertEquals(MPSeverityResponse.HIGH, actualSeverity)
    }

    @Test
    fun `getWaningByGrade should return NONE for grade 3`() {
        // Act
        val actualSeverity = MPSeverityResponse.getWaningByGrade(3)

        // Assert
        assertEquals(MPSeverityResponse.NONE, actualSeverity)
    }

    @Test
    fun `getWaningByGrade should return NONE for invalid grades`() {
        // Arrange
        val invalidGrades = listOf(-1, 4, 5, 10, 100, -100)

        // Act & Assert
        invalidGrades.forEach { grade ->
            val actualSeverity = MPSeverityResponse.getWaningByGrade(grade)
            assertEquals("Failed for grade: $grade", MPSeverityResponse.NONE, actualSeverity)
        }
    }

    @Test
    fun `getWaningByGrade should handle all valid grades correctly`() {
        // Arrange
        val gradeToSeverityMap = mapOf(
            0 to MPSeverityResponse.LOW,
            1 to MPSeverityResponse.MEDIUM,
            2 to MPSeverityResponse.HIGH,
            3 to MPSeverityResponse.NONE
        )

        // Act & Assert
        gradeToSeverityMap.forEach { (grade, expectedSeverity) ->
            val actualSeverity = MPSeverityResponse.getWaningByGrade(grade)
            assertEquals("Failed mapping for grade $grade", expectedSeverity, actualSeverity)
        }
    }

    @Test
    fun `getWaningByGrade should be consistent with enum ordinals`() {
        // Act & Assert
        assertEquals(MPSeverityResponse.LOW, MPSeverityResponse.getWaningByGrade(MPSeverityResponse.LOW.ordinal))
        assertEquals(MPSeverityResponse.MEDIUM, MPSeverityResponse.getWaningByGrade(MPSeverityResponse.MEDIUM.ordinal))
        assertEquals(MPSeverityResponse.HIGH, MPSeverityResponse.getWaningByGrade(MPSeverityResponse.HIGH.ordinal))
        assertEquals(MPSeverityResponse.NONE, MPSeverityResponse.getWaningByGrade(MPSeverityResponse.NONE.ordinal))
    }

    @Test
    fun `MPSeverityResponse valueOf should work correctly`() {
        // Act & Assert
        assertEquals(MPSeverityResponse.LOW, MPSeverityResponse.valueOf("LOW"))
        assertEquals(MPSeverityResponse.MEDIUM, MPSeverityResponse.valueOf("MEDIUM"))
        assertEquals(MPSeverityResponse.HIGH, MPSeverityResponse.valueOf("HIGH"))
        assertEquals(MPSeverityResponse.NONE, MPSeverityResponse.valueOf("NONE"))
    }

    @Test(expected = IllegalArgumentException::class)
    fun `MPSeverityResponse valueOf should throw exception for invalid value`() {
        // Act
        MPSeverityResponse.valueOf("INVALID")
    }

    @Test
    fun `MPSeverityResponse should have correct string representation`() {
        // Act & Assert
        assertEquals("LOW", MPSeverityResponse.LOW.toString())
        assertEquals("MEDIUM", MPSeverityResponse.MEDIUM.toString())
        assertEquals("HIGH", MPSeverityResponse.HIGH.toString())
        assertEquals("NONE", MPSeverityResponse.NONE.toString())
    }

    @Test
    fun `enum constructor should accept grade parameter`() {
        // This test verifies that the enum constructor accepts a grade parameter
        // We can't directly test the constructor, but we can verify the enum values exist
        // and the getWaningByGrade method works with the expected grades

        // Act & Assert
        assertNotNull(MPSeverityResponse.LOW)
        assertNotNull(MPSeverityResponse.MEDIUM)
        assertNotNull(MPSeverityResponse.HIGH)
        assertNotNull(MPSeverityResponse.NONE)

        // Verify that getWaningByGrade works as expected (implying constructor worked)
        assertEquals(MPSeverityResponse.LOW, MPSeverityResponse.getWaningByGrade(0))
        assertEquals(MPSeverityResponse.MEDIUM, MPSeverityResponse.getWaningByGrade(1))
        assertEquals(MPSeverityResponse.HIGH, MPSeverityResponse.getWaningByGrade(2))
        assertEquals(MPSeverityResponse.NONE, MPSeverityResponse.getWaningByGrade(3))
    }
}
