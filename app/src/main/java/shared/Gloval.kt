package shared

import adapters.Note
import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by ivanalejandro on 12/29/17.
 */
object Gloval {

    val mAuth = FirebaseAuth.getInstance()
    var mUser = mAuth.currentUser
    lateinit var mDB: FirebaseDatabase
    lateinit var mRef: DatabaseReference

    val RC_SIGN_IN = 123
    val ADD_CODE = 1
    val EDIT_CODE = 2

    @SuppressLint("SimpleDateFormat")
    fun getTimestamp(): String {

        val c = Calendar.getInstance()
        return SimpleDateFormat("HH:mm dd/MM/yyyy").format(c.time)
    }

    fun deleteNote(context: Context, noteId: String, callback: (()->(Unit))? = null) {
        Dyalog.showConfirm(context, DialogInterface.OnClickListener { dialog, _ ->
            mRef.child("users").child(mUser?.uid!!).orderByKey()
                    .equalTo(noteId).addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(p0: DataSnapshot) {
                            if (p0.hasChildren())
                                p0.children.iterator().next().ref.removeValue()
                        }

                        override fun onCancelled(p0: DatabaseError) {
                            Dyalog.show(context, "Error", p0.message, Dyalog.EXIT)
                        }
                    })
            dialog.dismiss()
        }.also {
            callback?.invoke()
        }, Dyalog.DISMISS)
    }
}