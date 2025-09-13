/*
 * Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
 * SPDX-License-Identifier: Apache-2.0
 */

package com.amazonaws.services.chime.sdk.meetings.realtime

import com.google.gson.annotations.SerializedName

enum class MeetingControlAction {
    @SerializedName("MUTE_AUDIO")
    MUTE_AUDIO,
    
    @SerializedName("UNMUTE_AUDIO")
    UNMUTE_AUDIO,
    
    @SerializedName("MUTE_SPEAKER")
    MUTE_SPEAKER,
    
    @SerializedName("UNMUTE_SPEAKER")
    UNMUTE_SPEAKER
}

data class MeetingControlMessage(
    @SerializedName("action")
    val action: MeetingControlAction,
    
    @SerializedName("targetAttendeeId")
    val targetAttendeeId: String,
    
    @SerializedName("senderAttendeeId")
    val senderAttendeeId: String,
    
    @SerializedName("timestamp")
    val timestamp: Long
)