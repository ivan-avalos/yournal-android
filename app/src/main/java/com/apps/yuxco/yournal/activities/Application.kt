package com.apps.yuxco.yournal.activities

import android.app.Application
import android.content.res.Resources
import android.graphics.Color
import com.apps.yuxco.yournal.R
import sakout.mehdi.StateViews.StateViewsBuilder

/*
 * Application.kt
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

class Application: Application() {
    override fun onCreate() {
        super.onCreate()
        /* Status page */
        StateViewsBuilder.init(this) .setIconColor(Color.parseColor("#D2D5DA"))
                .addState("STATE_NO_WIFI",
                        getString(R.string.error_wifi),
                        getString(R.string.error_wifi_message),
                        this.getDrawable(R.drawable.ic_server_error),
                        getString(R.string.confirm_cancel))
                .setButtonBackgroundColor(Color.parseColor("#317DED"))
                .iconSize = resources.getDimensionPixelSize(R.dimen.state_views_icon_size)
    }
}