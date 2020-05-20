package com.apps.yuxco.yournal.activities

import adapters.Note
import adapters.NotesAdapter
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import com.apps.yuxco.yournal.R
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import shared.Dyalog
import shared.Gloval
import shared.Gloval.mUser
import kotlin.system.exitProcess

/*
 * MainActivity.kt
 *
 * Copyright 2019 Iván Ávalos <ivan.avalos.diaz@hotmail.com>
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301, USA.
 *
 *
 */

class MainActivity: Activity() {
    lateinit var adapter: NotesAdapter
    lateinit var lmanager: LinearLayoutManager

    /* Authentication UI */
    private val providers = listOf(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build())

    private fun setFirebaseDataListener () {
        status_page.displayLoadingState()
        Gloval.mRef.child("users").child(mUser?.uid!!)
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(p0: DataSnapshot) {
                        adapter.clearData()

                        p0.children.forEach {
                            adapter.addNote(Note(
                                    id = it.key!!,
                                    title = it.child("title").value.toString(),
                                    date = it.child("date").value.toString(),
                                    body = it.child("body").value.toString()
                            ))
                        }

                        status_page.hideStates()
                    }

                    override fun onCancelled(p0: DatabaseError) { status_page.hideStates() }
                })
    }

    override fun onResume() {
        super.onResume()

        if (mUser != null)
            setFirebaseDataListener()
    }

    override fun onRestart() {
        super.onRestart()

        if (mUser != null)
            setFirebaseDataListener()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setActionBar(toolbar)

        /* Check WiFi connection */
        checkWiFi()

        /* Database */
        Gloval.mDB = FirebaseDatabase.getInstance()
        Gloval.mRef = Gloval.mDB.reference

        /* RecyclerView */
        adapter = NotesAdapter(editCallback = { _, note ->
            val intent = Intent(this, NoteActivity::class.java)
            intent.putExtra("code", Gloval.EDIT_CODE)
            intent.putExtra("id", note.id)
            startActivity(intent)
        }, deleteCallback = { swipeLay, note ->
            Gloval.deleteNote(this, note.id)
            swipeLay.close()
        }, viewCallback = { _, note ->
            val intent = Intent(this, ViewerActivity::class.java)
            intent.putExtra("id", note.id)
            startActivity(intent)
        })
        lmanager = LinearLayoutManager(this)

        rvNotes.adapter = adapter
        rvNotes.layoutManager = lmanager

        if(mUser == null) loadLogin()
        else setFirebaseDataListener()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.action_add -> {
                val intent = Intent(this, NoteActivity::class.java)
                intent.putExtra("code", Gloval.ADD_CODE)
                startActivity(intent)
            }
            R.id.action_logout -> {
                AuthUI.getInstance()
                        .signOut(this)
                        .addOnCompleteListener {
                            adapter.clearData()
                            loadLogin()
                        }
            }
            R.id.action_about -> {
                Dyalog.showAboutDialog(this)
            }
            else -> return super.onOptionsItemSelected(item)
        }

        return false
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode == RESULT_OK) {
            when(requestCode) {
                Gloval.RC_SIGN_IN -> {
                    mUser = FirebaseAuth.getInstance().currentUser
                }
            }
        }
    }

    private fun checkWiFi() {
        status_page.setOnStateButtonClicked {
            exitProcess(0)
        }

        val connManager = getSystemService(Context.CONNECTIVITY_SERVICE)
        val isConnected = if (connManager is ConnectivityManager) {
            val netinfo: NetworkInfo? = connManager.activeNetworkInfo
            netinfo?.isConnected ?: false
        } else false
        if (!isConnected) {
            status_page.displayState("STATE_NO_WIFI")
            actionBar?.hide()
        }
    }

    private fun loadLogin() {
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setLogo(R.drawable.yournal_full)
                        .setTheme(R.style.AppTheme_NoActionBar)
                        .setAvailableProviders(providers)
                        .build(),
                Gloval.RC_SIGN_IN)
    }
}