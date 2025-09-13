/*
 * Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
 * SPDX-License-Identifier: Apache-2.0
 */

package com.amazonaws.services.chime.sdk.meetings.realtime

import com.amazonaws.services.chime.sdk.meetings.realtime.datamessage.DataMessage
import com.amazonaws.services.chime.sdk.meetings.realtime.datamessage.DataMessageObserver
import com.amazonaws.services.chime.sdk.meetings.utils.logger.Logger
import com.google.gson.Gson

class MeetingControlController(
    private val logger: Logger,
    private val realtimeController: RealtimeControllerFacade,
    private val meetingCreatorId: String,
    private val currentAttendeeId: String
) : DataMessageObserver {

    companion object {
        private const val MEETING_CONTROL_TOPIC = "meeting-control"
    }

    private val gson = Gson()
    private var audioManager: android.media.AudioManager? = null

    init {
        setupDataMessageListener()
    }

    private fun setupDataMessageListener() {
        realtimeController.addRealtimeDataMessageObserver(MEETING_CONTROL_TOPIC, this)
    }

    override fun onDataMessageReceived(dataMessage: DataMessage) {
        try {
            val controlMessage = dataMessage.fromJson(MeetingControlMessage::class.java)
            
            // Only process messages targeted to this attendee
            if (controlMessage.targetAttendeeId != currentAttendeeId) {
                return
            }

            // Only allow meeting creator to send control commands
            if (controlMessage.senderAttendeeId != meetingCreatorId) {
                logger.warn("MeetingControl", "Ignoring control message from non-creator attendee")
                return
            }

            executeControlAction(controlMessage)
        } catch (error: Exception) {
            logger.error("MeetingControl", "Failed to process meeting control message: ${error.message}")
        }
    }

    private fun executeControlAction(message: MeetingControlMessage) {
        when (message.action) {
            MeetingControlAction.MUTE_AUDIO -> {
                realtimeController.realtimeLocalMute()
                logger.info("MeetingControl", "Audio muted by meeting creator")
            }
            
            MeetingControlAction.UNMUTE_AUDIO -> {
                realtimeController.realtimeLocalUnmute()
                logger.info("MeetingControl", "Audio unmuted by meeting creator")
            }
            
            MeetingControlAction.MUTE_SPEAKER -> {
                audioManager?.let { am ->
                    am.adjustStreamVolume(android.media.AudioManager.STREAM_MUSIC, 
                                        android.media.AudioManager.ADJUST_MUTE, 0)
                    logger.info("MeetingControl", "Speaker muted by meeting creator")
                }
            }
            
            MeetingControlAction.UNMUTE_SPEAKER -> {
                audioManager?.let { am ->
                    am.adjustStreamVolume(android.media.AudioManager.STREAM_MUSIC, 
                                        android.media.AudioManager.ADJUST_UNMUTE, 0)
                    logger.info("MeetingControl", "Speaker unmuted by meeting creator")
                }
            }
        }
    }

    // For meeting creator to control other attendees
    fun controlAttendeeAudio(targetAttendeeId: String, action: MeetingControlAction) {
        if (currentAttendeeId != meetingCreatorId) {
            logger.warn("MeetingControl", "Only meeting creator can control other attendees")
            return
        }

        val message = MeetingControlMessage(
            action = action,
            targetAttendeeId = targetAttendeeId,
            senderAttendeeId = currentAttendeeId,
            timestamp = System.currentTimeMillis()
        )

        realtimeController.realtimeSendDataMessage(
            MEETING_CONTROL_TOPIC,
            gson.toJson(message)
        )
    }

    fun setAudioManager(audioManager: android.media.AudioManager) {
        this.audioManager = audioManager
    }

    fun destroy() {
        realtimeController.removeRealtimeDataMessageObserverFromTopic(MEETING_CONTROL_TOPIC)
    }
}