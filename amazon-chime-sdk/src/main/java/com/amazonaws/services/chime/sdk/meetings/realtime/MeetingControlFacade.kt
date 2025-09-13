/*
 * Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
 * SPDX-License-Identifier: Apache-2.0
 */

package com.amazonaws.services.chime.sdk.meetings.realtime

interface MeetingControlFacade {
    /**
     * Mute audio for a specific attendee (meeting creator only)
     */
    fun muteAttendeeAudio(attendeeId: String)

    /**
     * Unmute audio for a specific attendee (meeting creator only)
     */
    fun unmuteAttendeeAudio(attendeeId: String)

    /**
     * Mute speaker for a specific attendee (meeting creator only)
     */
    fun muteAttendeeSpeaker(attendeeId: String)

    /**
     * Unmute speaker for a specific attendee (meeting creator only)
     */
    fun unmuteAttendeeSpeaker(attendeeId: String)

    /**
     * Set the audio manager for speaker control
     */
    fun setAudioManager(audioManager: android.media.AudioManager)

    /**
     * Clean up resources
     */
    fun destroy()
}