package com.apps.yuxco.yournal.activities

import android.app.Activity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.apps.yuxco.yournal.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener

import kotlinx.android.synthetic.main.activity_note.*
import kotlinx.android.synthetic.main.content_note.*
import shared.Dyalog
import shared.Gloval

/*
 * NoteActivity.kt
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

class NoteActivity : Activity() {

    private lateinit var extras: Bundle
    private lateinit var note: DatabaseReference
    private var type = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note)
        setActionBar(toolbar)
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setDisplayShowHomeEnabled(true)
        toolbar.setNavigationOnClickListener {
            finish()
        }

        extras = intent.extras!!
        type = extras.getInt("code")

        if(type == Gloval.EDIT_CODE) {
            title = getString(R.string.title_activity_note_edit)

            note = Gloval.mRef.child("users").child(Gloval.mUser?.uid!!).child(extras.getString("id")!!)

            note.addListenerForSingleValueEvent(object: ValueEventListener {
                override fun onDataChange(p0: DataSnapshot) {
                    note_title.setText(p0.child("title").value.toString())
                    note_body.setText(p0.child("body").value.toString())
                }

                override fun onCancelled(p0: DatabaseError) {
                    Dyalog.show(this@NoteActivity, "Error", p0.message, Dyalog.EXIT)
                }
            })
        } else {
            title = getString(R.string.title_activity_note_add)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_note, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.action_ok -> {
                if(type == Gloval.EDIT_CODE) {
                    note.child("title").setValue(note_title.text.toString())
                    note.child("body").setValue(note_body.text.toString())
                } else if(type == Gloval.ADD_CODE) {
                    val post = Gloval.mRef.child("users").child(Gloval.mUser?.uid!!).push()
                    post.child("title").setValue(note_title.text.toString())
                    post.child("date").setValue(Gloval.getTimestamp())
                    post.child("body").setValue(note_body.text.toString())
                }

                finish()
            }
            else -> return super.onOptionsItemSelected(item)
        }

        return false
    }

}
