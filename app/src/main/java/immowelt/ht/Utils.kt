package immowelt.ht

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.support.annotation.DrawableRes
import android.support.graphics.drawable.VectorDrawableCompat
import android.support.v4.graphics.drawable.DrawableCompat
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory

object Utils {
    fun getBitmapDescriptor(cxt: Context, @DrawableRes id: Int): BitmapDescriptor {
        return BitmapDescriptorFactory.fromBitmap(
            getBitmap(
                VectorDrawableCompat.create(
                    cxt.getResources(),
                    id,
                    null
                )!!
            )
        )
    }

    fun getBitmap(vectorDrawable: VectorDrawableCompat): Bitmap {
        val bitmap = Bitmap.createBitmap(
            vectorDrawable.intrinsicWidth,
            vectorDrawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        vectorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight())
        vectorDrawable.draw(canvas)
        return bitmap
    }

    fun setTint(drawable: Drawable, color: Int): Drawable {
        val newDrawable = DrawableCompat.wrap(drawable)
        DrawableCompat.setTint(newDrawable, color)
        return newDrawable
    }
}