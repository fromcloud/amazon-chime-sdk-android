// Web App Remote Control Example for Amazon Chime SDK
// This code should be integrated into your web application

class AndroidRemoteController {
    constructor(meetingSession) {
        this.meetingSession = meetingSession;
        this.REMOTE_CONTROL_TOPIC = 'remote_control';
        this.DATA_MESSAGE_LIFETIME_MS = 300000;
    }

    // Mute Android app user's microphone
    muteAndroidUser(targetAttendeeId) {
        const message = {
            type: 'audio_control',
            targetAttendeeId: targetAttendeeId,
            action: 'mute',
            senderAttendeeId: this.meetingSession.configuration.credentials.attendeeId
        };
        
        console.log('[WebRemoteControl] Sending mute command:', message);
        
        this.meetingSession.audioVideo.realtimeSendDataMessage(
            this.REMOTE_CONTROL_TOPIC,
            JSON.stringify(message),
            this.DATA_MESSAGE_LIFETIME_MS
        );
    }

    // Unmute Android app user's microphone
    unmuteAndroidUser(targetAttendeeId) {
        const message = {
            type: 'audio_control',
            targetAttendeeId: targetAttendeeId,
            action: 'unmute',
            senderAttendeeId: this.meetingSession.configuration.credentials.attendeeId
        };
        
        console.log('[WebRemoteControl] Sending unmute command:', message);
        
        this.meetingSession.audioVideo.realtimeSendDataMessage(
            this.REMOTE_CONTROL_TOPIC,
            JSON.stringify(message),
            this.DATA_MESSAGE_LIFETIME_MS
        );
    }

    // Disable Android app user's speaker
    disableAndroidUserSpeaker(targetAttendeeId) {
        const message = {
            type: 'speaker_control',
            targetAttendeeId: targetAttendeeId,
            action: 'disable_speaker',
            senderAttendeeId: this.meetingSession.configuration.credentials.attendeeId
        };
        
        console.log('[WebRemoteControl] Sending disable speaker command:', message);
        
        this.meetingSession.audioVideo.realtimeSendDataMessage(
            this.REMOTE_CONTROL_TOPIC,
            JSON.stringify(message),
            this.DATA_MESSAGE_LIFETIME_MS
        );
    }

    // Enable Android app user's speaker
    enableAndroidUserSpeaker(targetAttendeeId) {
        const message = {
            type: 'speaker_control',
            targetAttendeeId: targetAttendeeId,
            action: 'enable_speaker',
            senderAttendeeId: this.meetingSession.configuration.credentials.attendeeId
        };
        
        console.log('[WebRemoteControl] Sending enable speaker command:', message);
        
        this.meetingSession.audioVideo.realtimeSendDataMessage(
            this.REMOTE_CONTROL_TOPIC,
            JSON.stringify(message),
            this.DATA_MESSAGE_LIFETIME_MS
        );
    }
}

// Usage example:
// const remoteController = new AndroidRemoteController(meetingSession);
// remoteController.muteAndroidUser('android-attendee-id');
// remoteController.disableAndroidUserSpeaker('android-attendee-id');
