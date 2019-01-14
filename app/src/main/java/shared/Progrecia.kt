package shared

import android.app.ProgressDialog
import android.content.Context

/**
 * Created by ivanalejandro on 12/29/17.
 */
class Progrecia(ctx: Context, message: String) {

    private val progress = ProgressDialog(ctx)

    init {
        progress.setMessage(message)
        progress.isIndeterminate = true
    }

    fun show() {
        progress.show()
    }

    fun dismiss() {
        progress.dismiss()
    }
}