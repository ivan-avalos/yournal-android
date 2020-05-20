package adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.apps.yuxco.yournal.R
import com.daimajia.swipe.SwipeLayout
import java.util.*

/*
 * NotesAdapter.kt
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

class NotesAdapter(private val editCallback: (swipeLay: SwipeLayout, note: Note)->(Unit),
                   private val deleteCallback: (swipeLay: SwipeLayout, note: Note)->(Unit),
                   private val viewCallback: (swipeLay: SwipeLayout, note: Note)->(Unit)):
                   RecyclerView.Adapter<NotesAdapter.NotesViewHolder>() {

    private val dataset = ArrayList<Note>()

    override fun getItemCount() = dataset.size

    override fun onBindViewHolder(holder: NotesViewHolder, position: Int) {
        val rdataset = dataset.asReversed()[position]
        holder.tvTitle.text = rdataset.title
        holder.tvDate.text = rdataset.date
        holder.swipeLay.showMode = SwipeLayout.ShowMode.LayDown
        holder.btnEdit.setOnClickListener {
            editCallback(holder.swipeLay, rdataset)
        }
        holder.btnDelete.setOnClickListener {
            deleteCallback(holder.swipeLay, rdataset)
        }

        holder.btnOpen.setOnClickListener { holder.swipeLay.open() }

        holder.swipeLay.addSwipeListener(object : SwipeLayout.SwipeListener {
            override fun onStartOpen(layout: SwipeLayout?) {}

            override fun onStartClose(layout: SwipeLayout?) {}

            override fun onHandRelease(layout: SwipeLayout?, xvel: Float, yvel: Float) {}

            override fun onOpen(layout: SwipeLayout?) {
                holder.btnOpen.setOnClickListener {
                    holder.swipeLay.close()
                }
            }

            override fun onClose(layout: SwipeLayout?) {
                holder.btnOpen.setOnClickListener {
                    holder.swipeLay.open()
                }
            }

            override fun onUpdate(layout: SwipeLayout?, leftOffset: Int, topOffset: Int) {}
        })

        holder.surface_wrapper.setOnClickListener {
            holder.swipeLay.close()

            viewCallback(holder.swipeLay, rdataset)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            NotesViewHolder(LayoutInflater
                    .from(parent.context)
                    .inflate(R.layout.item, parent, false))

    fun addNote(note: Note) {
        dataset.add(note)
        notifyDataSetChanged()
    }

    fun clearData() {
        dataset.clear()
        notifyDataSetChanged()
    }

    class NotesViewHolder(v: View): RecyclerView.ViewHolder(v) {
        val swipeLay = v.findViewById(R.id.swipeLay) as SwipeLayout
        val surface_wrapper = v.findViewById(R.id.surface_wrapper) as RelativeLayout
        val btnEdit = v.findViewById(R.id.btnEdit) as ImageButton
        val btnDelete = v.findViewById(R.id.btnDelete) as ImageButton
        val tvTitle = v.findViewById(R.id.tvTitle) as TextView
        val tvDate = v.findViewById(R.id.tvDate) as TextView
        val btnOpen = v.findViewById(R.id.btnOpen) as ImageButton
    }
}