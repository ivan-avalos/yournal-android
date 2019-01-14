package shared

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.support.annotation.RequiresApi
import android.view.LayoutInflater
import com.apps.yuxco.yournal.R

/**
 * Created by ivanalejandro on 12/29/17.
 */
object Dyalog {

    fun show(ctx: Context, title: String, message: String, listener: DialogInterface.OnClickListener) {

        AlertDialog.Builder(ctx)
                .setTitle(title)
                .setMessage(message)
                .setNeutralButton("OK", listener)
                .create().show()
    }

    fun showAboutDialog(ctx: Activity) {

        AlertDialog.Builder(ctx)
                .setTitle(ctx.getString(R.string.about_title))
                .setView(ctx.layoutInflater.inflate(R.layout.about, null))
                .setNeutralButton("OK", DISMISS)
                .setPositiveButton(ctx.getString(R.string.about_website)) { dialog, _ ->
                    ctx.startActivity(Intent (
                            Intent.ACTION_VIEW,
                            Uri.parse("https://yournal.robotronica.mx")
                    ))
                    dialog.dismiss()
                }
                .create().show()
    }

    fun showConfirm(ctx: Context, okl: DialogInterface.OnClickListener,
                    cancell: DialogInterface.OnClickListener) {

        AlertDialog.Builder(ctx)
                .setTitle(ctx.getString(R.string.confirm_title))
                .setMessage(ctx.getString(R.string.confirm_message))
                .setNegativeButton(ctx.getString(R.string.confirm_delete), okl)
                .setPositiveButton(ctx.getString(R.string.confirm_cancel), cancell)
                .create().show()
    }

    val DISMISS = DialogInterface.OnClickListener { dialogInterface, _ ->
        dialogInterface.dismiss()
    }

    val EXIT = DialogInterface.OnClickListener { _, _ ->
        System.exit(-1)
    }
}