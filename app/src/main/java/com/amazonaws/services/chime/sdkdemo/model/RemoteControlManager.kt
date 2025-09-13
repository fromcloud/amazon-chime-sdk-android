package com.amazonaws.services.chime.sdkdemo.model

import com.amazonaws.services.chime.sdk.meetings.audiovideo.AudioVideoFacade
import com.amazonaws.services.chime.sdk.meetings.device.MediaDevice
import com.amazonaws.services.chime.sdk.meetings.device.MediaDeviceType
import com.amazonaws.services.chime.sdk.meetings.session.MeetingSessionCredentials
import com.amazonaws.services.chime.sdkdemo.data.RemoteControlMessage
import com.google.gson.Gson

class RemoteControlManager(
    private val audioVideo: AudioVideoFacade,
    private val credentials: MeetingSessionCredentials,
    private val gson: Gson,
    private val onMuteStateChanged: (Boolean) -> Unit = {},
    private val onSpeakerStateChanged: (Boolean) -> Unit = {}
) {
    companion object {
        private const val REMOTE_CONTROL_TOPIC = "remote_control"
        private const val DATA_MESSAGE_LIFETIME_MS = 300000
    }

    private var isSpeakerDisabled = false

    fun isMeetingCreator(): Boolean {
        // 간단한 구현: 모든 사용자가 원격 제어 권한을 가짐
        // 실제 구현에서는 서버에서 권한을 확인하거나 특정 조건을 체크
        return true
    }

    fun sendMuteCommand(targetAttendeeId: String) {
        val message = RemoteControlMessage(
            type = RemoteControlMessage.TYPE_AUDIO_CONTROL,
            targetAttendeeId = targetAttendeeId,
            action = RemoteControlMessage.ACTION_MUTE,
            senderAttendeeId = credentials.attendeeId
        )
        
        val messageJson = gson.toJson(message)
        println("[RemoteControl] Sending mute command: $messageJson")
        
        audioVideo.realtimeSendDataMessage(
            REMOTE_CONTROL_TOPIC,
            messageJson,
            DATA_MESSAGE_LIFETIME_MS
        )
    }

    fun sendUnmuteCommand(targetAttendeeId: String) {
        val message = RemoteControlMessage(
            type = RemoteControlMessage.TYPE_AUDIO_CONTROL,
            targetAttendeeId = targetAttendeeId,
            action = RemoteControlMessage.ACTION_UNMUTE,
            senderAttendeeId = credentials.attendeeId
        )
        
        val messageJson = gson.toJson(message)
        println("[RemoteControl] Sending unmute command: $messageJson")
        
        audioVideo.realtimeSendDataMessage(
            REMOTE_CONTROL_TOPIC,
            messageJson,
            DATA_MESSAGE_LIFETIME_MS
        )
    }

    fun sendDisableSpeakerCommand(targetAttendeeId: String) {
        val message = RemoteControlMessage(
            type = RemoteControlMessage.TYPE_SPEAKER_CONTROL,
            targetAttendeeId = targetAttendeeId,
            action = RemoteControlMessage.ACTION_DISABLE_SPEAKER,
            senderAttendeeId = credentials.attendeeId
        )
        
        val messageJson = gson.toJson(message)
        println("[RemoteControl] Sending disable speaker command: $messageJson")
        
        audioVideo.realtimeSendDataMessage(
            REMOTE_CONTROL_TOPIC,
            messageJson,
            DATA_MESSAGE_LIFETIME_MS
        )
    }

    fun sendEnableSpeakerCommand(targetAttendeeId: String) {
        val message = RemoteControlMessage(
            type = RemoteControlMessage.TYPE_SPEAKER_CONTROL,
            targetAttendeeId = targetAttendeeId,
            action = RemoteControlMessage.ACTION_ENABLE_SPEAKER,
            senderAttendeeId = credentials.attendeeId
        )
        
        val messageJson = gson.toJson(message)
        println("[RemoteControl] Sending enable speaker command: $messageJson")
        
        audioVideo.realtimeSendDataMessage(
            REMOTE_CONTROL_TOPIC,
            messageJson,
            DATA_MESSAGE_LIFETIME_MS
        )
    }

    fun handleRemoteControlMessage(messageText: String): Boolean {
        return try {
            val message = gson.fromJson(messageText, RemoteControlMessage::class.java)
            
            println("[RemoteControl] Received message: type=${message.type}, target=${message.targetAttendeeId}, action=${message.action}")
            println("[RemoteControl] Current attendeeId: ${credentials.attendeeId}")
            
            if (message.targetAttendeeId != credentials.attendeeId) {
                println("[RemoteControl] Message not for this attendee")
                return false
            }

            when (message.type) {
                RemoteControlMessage.TYPE_AUDIO_CONTROL -> {
                    when (message.action) {
                        RemoteControlMessage.ACTION_MUTE -> {
                            println("[RemoteControl] Executing mute command")
                            val success = audioVideo.realtimeLocalMute()
                            if (success) onMuteStateChanged(true)
                            println("[RemoteControl] Mute result: $success")
                            success
                        }
                        RemoteControlMessage.ACTION_UNMUTE -> {
                            println("[RemoteControl] Executing unmute command")
                            val success = audioVideo.realtimeLocalUnmute()
                            if (success) onMuteStateChanged(false)
                            println("[RemoteControl] Unmute result: $success")
                            success
                        }
                        else -> {
                            println("[RemoteControl] Unknown audio action: ${message.action}")
                            false
                        }
                    }
                }
                RemoteControlMessage.TYPE_SPEAKER_CONTROL -> {
                    when (message.action) {
                        RemoteControlMessage.ACTION_DISABLE_SPEAKER -> {
                            println("[RemoteControl] Executing disable speaker command")
                            val success = disableSpeaker()
                            if (success) onSpeakerStateChanged(true)
                            println("[RemoteControl] Disable speaker result: $success")
                            success
                        }
                        RemoteControlMessage.ACTION_ENABLE_SPEAKER -> {
                            println("[RemoteControl] Executing enable speaker command")
                            val success = enableSpeaker()
                            if (success) onSpeakerStateChanged(false)
                            println("[RemoteControl] Enable speaker result: $success")
                            success
                        }
                        else -> {
                            println("[RemoteControl] Unknown speaker action: ${message.action}")
                            false
                        }
                    }
                }
                else -> {
                    println("[RemoteControl] Unknown message type: ${message.type}")
                    false
                }
            }
        } catch (e: Exception) {
            println("[RemoteControl] Error handling message: ${e.message}")
            false
        }
    }

    private fun disableSpeaker(): Boolean {
        return try {
            val devices = audioVideo.listAudioDevices()
            val nonSpeakerDevice = devices.find { 
                it.type != MediaDeviceType.AUDIO_BUILTIN_SPEAKER && 
                it.type != MediaDeviceType.AUDIO_BLUETOOTH 
            }
            
            if (nonSpeakerDevice != null) {
                audioVideo.chooseAudioDevice(nonSpeakerDevice)
                isSpeakerDisabled = true
                true
            } else {
                // 다른 디바이스가 없으면 볼륨을 0으로 설정하는 방법 등을 고려
                false
            }
        } catch (e: Exception) {
            println("[RemoteControl] Error disabling speaker: ${e.message}")
            false
        }
    }

    private fun enableSpeaker(): Boolean {
        return try {
            val devices = audioVideo.listAudioDevices()
            val speakerDevice = devices.find { 
                it.type == MediaDeviceType.AUDIO_BUILTIN_SPEAKER 
            }
            
            if (speakerDevice != null) {
                audioVideo.chooseAudioDevice(speakerDevice)
                isSpeakerDisabled = false
                true
            } else {
                false
            }
        } catch (e: Exception) {
            println("[RemoteControl] Error enabling speaker: ${e.message}")
            false
        }
    }
}