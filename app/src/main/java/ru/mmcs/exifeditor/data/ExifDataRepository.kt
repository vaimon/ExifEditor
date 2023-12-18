package ru.mmcs.exifeditor.data

import android.content.Context
import android.net.Uri
import androidx.exifinterface.media.ExifInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ExifDataRepository(val applicationContext: Context) : ExifRepository {
    override suspend fun getExifData(uri: Uri) : Map<String, String> {
        val input = applicationContext.contentResolver.openInputStream(uri)
        val exifInterface = ExifInterface(input!!)
        val exifData = mutableMapOf<String,String>()
        EXIF_TAGS.forEach { tag ->
            exifInterface.getAttribute(tag)?.let{
                exifData.put(tag, it)
            }
        }
        withContext(Dispatchers.IO) {
            input.close()
        }
        return exifData
    }

    companion object{
        private val EXIF_TAGS = arrayOf(
            ExifInterface.TAG_APERTURE_VALUE,
            ExifInterface.TAG_BRIGHTNESS_VALUE,
            ExifInterface.TAG_CAMERA_OWNER_NAME,
            ExifInterface.TAG_ARTIST,
            ExifInterface.TAG_BITS_PER_SAMPLE,
            ExifInterface.TAG_COLOR_SPACE,
            ExifInterface.TAG_CONTRAST,
            ExifInterface.TAG_DATETIME,
            ExifInterface.TAG_DIGITAL_ZOOM_RATIO,
            ExifInterface.TAG_EXPOSURE_TIME,
            ExifInterface.TAG_FLASH,
            ExifInterface.TAG_FLASH_ENERGY,
            ExifInterface.TAG_FOCAL_LENGTH,
            ExifInterface.TAG_F_NUMBER,
            ExifInterface.TAG_GPS_ALTITUDE,
            ExifInterface.TAG_GPS_LATITUDE,
            ExifInterface.TAG_GPS_LONGITUDE,
            ExifInterface.TAG_IMAGE_LENGTH,
            ExifInterface.TAG_IMAGE_WIDTH,
            ExifInterface.TAG_IMAGE_UNIQUE_ID,
            ExifInterface.TAG_ISO_SPEED,
            ExifInterface.TAG_LENS_MAKE,
            ExifInterface.TAG_LENS_MODEL,
            ExifInterface.TAG_LENS_SPECIFICATION,
            ExifInterface.TAG_LIGHT_SOURCE,
            ExifInterface.TAG_MAKE,
            ExifInterface.TAG_MAKER_NOTE,
            ExifInterface.TAG_MODEL,
            ExifInterface.TAG_ORIENTATION,
            ExifInterface.TAG_SATURATION,
            ExifInterface.TAG_SHARPNESS,
            ExifInterface.TAG_SHUTTER_SPEED_VALUE,
            ExifInterface.TAG_SOFTWARE,
        )
    }
}