/*
 * Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
 * SPDX-License-Identifier: Apache-2.0
 */

package com.amazonaws.services.chime.sdkdemo.data

data class RemoteControlMessage(
    val type: String,
    val targetAttendeeId: String,
    val action: String,
    val senderAttendeeId: String? = null
) {
    companion object {
        const val TYPE_AUDIO_CONTROL = "audio_control"
        const val TYPE_SPEAKER_CONTROL = "speaker_control"
        const val ACTION_MUTE = "mute"
        const val ACTION_UNMUTE = "unmute"
        const val ACTION_DISABLE_SPEAKER = "disable_speaker"
        const val ACTION_ENABLE_SPEAKER = "enable_speaker"
    }
    
    override fun toString(): String {
        return "RemoteControlMessage(type='$type', targetAttendeeId='$targetAttendeeId', action='$action')"
    }
}