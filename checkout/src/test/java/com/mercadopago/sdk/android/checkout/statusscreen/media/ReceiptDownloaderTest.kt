package com.mercadopago.sdk.android.checkout.statusscreen.media

import kotlinx.coroutines.test.runTest
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.tls.HandshakeCertificates
import okhttp3.tls.HeldCertificate
import org.junit.After
import org.junit.Before
import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

private const val STALE_FILE_AGE_MILLIS = 2 * 24 * 60 * 60 * 1000L
private const val OVERSIZED_BODY_LENGTH = 6 * 1024 * 1024

internal class ReceiptDownloaderTest {
    private lateinit var server: MockWebServer
    private lateinit var cacheDir: File
    private lateinit var downloader: ReceiptDownloader

    @Before
    fun setUp() {
        val serverCertificate = HeldCertificate.Builder()
            .addSubjectAlternativeName("localhost")
            .build()
        val serverCertificates = HandshakeCertificates.Builder()
            .heldCertificate(serverCertificate)
            .build()
        val clientCertificates = HandshakeCertificates.Builder()
            .addTrustedCertificate(serverCertificate.certificate)
            .build()
        server = MockWebServer().apply {
            useHttps(serverCertificates.sslSocketFactory(), false)
            start()
        }
        val trustingClient = OkHttpClient.Builder()
            .sslSocketFactory(clientCertificates.sslSocketFactory(), clientCertificates.trustManager)
            .followRedirects(false)
            .followSslRedirects(false)
            .build()
        cacheDir = File.createTempFile("cache", "").apply {
            delete()
            mkdirs()
        }
        downloader = ReceiptDownloader(cacheDir = cacheDir, client = trustingClient, validateUrl = { it })
    }

    @After
    fun tearDown() {
        server.shutdown()
        cacheDir.deleteRecursively()
    }

    @Test
    fun `given a valid pdf response then the file is downloaded to the private cache`() = runTest {
        // Given
        server.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setHeader("Content-Type", "application/pdf")
                .setBody("%PDF-1.4 fake receipt"),
        )

        // When
        val file = downloader.download(server.url("/receipt.pdf").toString())

        // Then
        assertNotNull(file)
        assertTrue(file.exists())
        assertTrue(file.path.startsWith(cacheDir.path))
    }

    @Test
    fun `given a non-https url then the download is rejected without a network call`() = runTest {
        // Given
        val defaultValidationDownloader = ReceiptDownloader(cacheDir = cacheDir)
        val url = "http://example.com/receipt.pdf"

        // When
        val file = defaultValidationDownloader.download(url)

        // Then
        assertNull(file)
        assertEquals(0, server.requestCount)
    }

    @Test
    fun `given a redirect response then the download is rejected`() = runTest {
        // Given
        server.enqueue(
            MockResponse()
                .setResponseCode(302)
                .setHeader("Location", "https://example.com/other.pdf"),
        )

        // When
        val file = downloader.download(server.url("/receipt.pdf").toString())

        // Then
        assertNull(file)
    }

    @Test
    fun `given a non-pdf content type then the download is rejected`() = runTest {
        // Given
        server.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setHeader("Content-Type", "text/html")
                .setBody("<html></html>"),
        )

        // When
        val file = downloader.download(server.url("/receipt.pdf").toString())

        // Then
        assertNull(file)
    }

    @Test
    fun `given a body that exceeds the byte cap while streaming then the partial file is deleted`() = runTest {
        // Given
        val oversized = "a".repeat(OVERSIZED_BODY_LENGTH)
        server.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setHeader("Content-Type", "application/pdf")
                .setBody(oversized),
        )

        // When
        val file = downloader.download(server.url("/receipt.pdf").toString())

        // Then
        assertNull(file)
        assertTrue(receiptDir().listFiles()?.isEmpty() != false)
    }

    @Test
    fun `given files older than the ttl then sweepStale deletes them but keeps fresh ones`() {
        // Given
        val staleFile = File(receiptDir(), "receipt_old.pdf").apply {
            createNewFile()
            setLastModified(System.currentTimeMillis() - STALE_FILE_AGE_MILLIS)
        }
        val freshFile = File(receiptDir(), "receipt_new.pdf").apply { createNewFile() }

        // When
        downloader.sweepStale()

        // Then
        assertFalse(staleFile.exists())
        assertTrue(freshFile.exists())
    }

    private fun receiptDir(): File = File(cacheDir, ReceiptDownloader.RECEIPT_CACHE_DIR_NAME).apply { mkdirs() }
}
