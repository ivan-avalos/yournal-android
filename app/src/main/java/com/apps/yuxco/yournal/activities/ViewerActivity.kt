package com.apps.yuxco.yournal.activities

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.apps.yuxco.yournal.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener

import kotlinx.android.synthetic.main.activity_viewer.*
import kotlinx.android.synthetic.main.content_viewer.*
import shared.Dyalog
import shared.Gloval

/*
 * ViewerActivity
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

class ViewerActivity : AppCompatActivity() {

    private lateinit var extras: Bundle
    private lateinit var note: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_viewer)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        toolbar.setNavigationOnClickListener {
            finish()
        }
        extras = intent.extras!!

        note = Gloval.mRef.child("users").child(Gloval.mUser?.uid!!).child(extras.getString("id")!!)

        note.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                title = p0.child("title").value.toString()
                viewer_title.text = p0.child("title").value.toString()
                viewer_date.text = p0.child("date").value.toString()
                viewer_body.text = p0.child("body").value.toString()
            }

            override fun onCancelled(p0: DatabaseError) {}
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_viewer, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId) {
            R.id.action_edit -> {
                val intent = Intent(this, NoteActivity::class.java)
                intent.putExtra("code", Gloval.EDIT_CODE)
                intent.putExtra("id", extras.getString("id"))
                startActivity(intent)
            }
            R.id.action_delete -> {
                Dyalog.showConfirm(this@ViewerActivity, DialogInterface.OnClickListener{ dialog, _ ->
                    finish()

                    note.addListenerForSingleValueEvent(object: ValueEventListener {
                        override fun onDataChange(p0: DataSnapshot) {
                            p0.ref.removeValue()
                        }

                        override fun onCancelled(p0: DatabaseError) {
                            Dyalog.show(this@ViewerActivity, "Error", p0.message, Dyalog.EXIT)
                        }
                    })
                }, DialogInterface.OnClickListener { dialog, _ ->
                    dialog.dismiss()
                })
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
