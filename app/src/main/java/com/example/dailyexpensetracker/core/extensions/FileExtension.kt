package com.example.dailyexpensetracker.core.extensions

import android.app.DownloadManager
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import com.example.dailyexpensetracker.data.local.ExpenseSummary
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

fun Context.sharePdf(file: File) {
    val uri = FileProvider.getUriForFile(
        this,
        "${this.packageName}.provider",
        file
    )

    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "application/pdf"
        putExtra(Intent.EXTRA_STREAM, uri)
        putExtra(Intent.EXTRA_SUBJECT, "Expense Report PDF")
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }

    this.startActivity(Intent.createChooser(intent, "Share PDF Report"))
}

fun Context.startDownload(url: String, fileName: String) {
    val request = DownloadManager.Request(url.toUri()).apply {
        setTitle(fileName)
        setDescription("Downloading file...")
        setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)
        setAllowedOverMetered(true)
        setAllowedOverRoaming(true)
    }

    val downloadManager = this.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
    downloadManager.enqueue(request)
}

fun Context.saveFileToDownloads(file: File, fileName: String, mimeType: String="application/pdf") {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        val resolver = this.contentResolver
        val contentValues = ContentValues().apply {
            put(MediaStore.Downloads.DISPLAY_NAME, fileName)
            put(MediaStore.Downloads.MIME_TYPE, mimeType)
            put(MediaStore.Downloads.IS_PENDING, 1)
        }

        val collection = MediaStore.Downloads.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
        val itemUri = resolver.insert(collection, contentValues)

        itemUri?.let { uri ->
            resolver.openOutputStream(uri)?.use { outputStream ->
                file.inputStream().use { input -> input.copyTo(outputStream) }
            }
            contentValues.clear()
            contentValues.put(MediaStore.Downloads.IS_PENDING, 0)
            resolver.update(uri, contentValues, null, null)
            this.toast("Saved to Downloads")
        } ?: run {
            this.toast( "Failed to save file")
        }

    } else {
        // For Android 9 and below (API < 29)
        val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val destFile = File(downloadsDir, fileName)

        try {
            file.inputStream().use { input ->
                FileOutputStream(destFile).use { output ->
                    input.copyTo(output)
                }
            }
            this.toast("Saved to Downloads")
        } catch (e: IOException) {
            e.printStackTrace()
            this.toast( "Failed to save file")
        }
    }
}

fun Context.generatePdfReport(data: List<ExpenseSummary>): File {
    val document = PdfDocument()
    val pageInfo = PdfDocument.PageInfo.Builder(300, 600, 1).create()
    val page = document.startPage(pageInfo)

    val canvas = page.canvas
    val paint = Paint()
    paint.textSize = 12f

    var y = 25
    // Draw report title and header
    canvas.drawText("Expense Report (Last 7 Days)", 10f, y.toFloat(), paint)
    y += 20
    canvas.drawText("Date      | Category       | Amount", 10f, y.toFloat(), paint)
    y += 15

    // Iterate over data and draw each entry line in the PDF
    data.forEach {
        val line = "${it.date.take(10)} | ${it.type.take(12)} | ₹${it.totalAmount}"
        y += 15
        canvas.drawText(line, 10f, y.toFloat(), paint)
    }

    document.finishPage(page)

    // Save the PDF file in app cache directory
    val file = File(this.cacheDir, "last_7_days_expense_report.pdf")
    file.outputStream().use {
        document.writeTo(it)
    }

    document.close()
    return file
}