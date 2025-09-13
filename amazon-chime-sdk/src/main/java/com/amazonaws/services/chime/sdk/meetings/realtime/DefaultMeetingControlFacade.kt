/*
 * Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
 * SPDX-License-Identifier: Apache-2.0
 */

package com.amazonaws.services.chime.sdk.meetings.realtime

import com.amazonaws.services.chime.sdk.meetings.utils.logger.Logger

class DefaultMeetingControlFacade(
    logger: Logger,
    realtimeController: RealtimeControllerFacade,
    meetingCreatorId: String,
    currentAttendeeId: String
) : MeetingControlFacade {

    private val controller = MeetingControlController(
        logger,
        realtimeController,
        meetingCreatorId,
        currentAttendeeId
    )

    override fun muteAttendeeAudio(attendeeId: String) {
        controller.controlAttendeeAudio(attendeeId, MeetingControlAction.MUTE_AUDIO)
    }

    override fun unmuteAttendeeAudio(attendeeId: String) {
        controller.controlAttendeeAudio(attendeeId, MeetingControlAction.UNMUTE_AUDIO)
    }

    override fun muteAttendeeSpeaker(attendeeId: String) {
        controller.controlAttendeeAudio(attendeeId, MeetingControlAction.MUTE_SPEAKER)
    }

    override fun unmuteAttendeeSpeaker(attendeeId: String) {
        controller.controlAttendeeAudio(attendeeId, MeetingControlAction.UNMUTE_SPEAKER)
    }

    override fun setAudioManager(audioManager: android.media.AudioManager) {
        controller.setAudioManager(audioManager)
    }

    override fun destroy() {
        controller.destroy()
    }
}