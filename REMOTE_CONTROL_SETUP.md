# Android 앱 원격 제어 설정 가이드

이 가이드는 웹 앱의 organizer가 Android 앱 사용자의 마이크와 스피커를 원격으로 제어할 수 있도록 하는 기능을 설명합니다.

## 수정된 파일들

### Android 앱 수정사항

1. **RemoteControlMessage.kt** - 원격 제어 메시지 데이터 클래스
   - 스피커 제어 액션 추가
   - 발신자 정보 추가

2. **RemoteControlManager.kt** - 원격 제어 관리자
   - 스피커 제어 기능 추가
   - 마이크/스피커 상태 변경 콜백 추가

3. **MeetingFragment.kt** - 미팅 프래그먼트
   - UI 업데이트 콜백 추가
   - refreshMuteStatus() 함수 추가

## 웹 앱 통합

### 1. JavaScript 클래스 추가

`web-remote-control-example.js` 파일의 `AndroidRemoteController` 클래스를 웹 앱에 통합하세요.

```javascript
const remoteController = new AndroidRemoteController(meetingSession);

// Android 사용자 마이크 음소거
remoteController.muteAndroidUser('android-attendee-id');

// Android 사용자 마이크 음소거 해제
remoteController.unmuteAndroidUser('android-attendee-id');

// Android 사용자 스피커 비활성화
remoteController.disableAndroidUserSpeaker('android-attendee-id');

// Android 사용자 스피커 활성화
remoteController.enableAndroidUserSpeaker('android-attendee-id');
```

### 2. UI 컴포넌트 추가

`web-remote-control-ui-example.html` 파일을 참고하여 웹 앱에 원격 제어 UI를 추가하세요.

## 작동 원리

1. **웹 앱에서 명령 전송**: organizer가 웹 앱에서 원격 제어 버튼을 클릭
2. **데이터 메시지 전송**: `remote_control` 토픽으로 JSON 메시지 전송
3. **Android 앱에서 수신**: Android 앱이 메시지를 수신하고 파싱
4. **액션 실행**: 대상 attendeeId가 일치하면 해당 액션 실행
5. **UI 업데이트**: Android 앱의 UI가 자동으로 업데이트

## 메시지 형식

### 마이크 제어
```json
{
  "type": "audio_control",
  "targetAttendeeId": "target-attendee-id",
  "action": "mute" | "unmute",
  "senderAttendeeId": "sender-attendee-id"
}
```

### 스피커 제어
```json
{
  "type": "speaker_control",
  "targetAttendeeId": "target-attendee-id", 
  "action": "disable_speaker" | "enable_speaker",
  "senderAttendeeId": "sender-attendee-id"
}
```

## 테스트 방법

1. Android 앱을 빌드하고 실행
2. 웹 앱에서 같은 미팅에 참여
3. 웹 앱에서 원격 제어 기능 테스트
4. Android 앱에서 마이크/스피커 상태 변경 확인

## 주의사항

- 원격 제어는 `remote_control` 토픽을 통해 작동합니다
- 대상 attendeeId가 정확해야 합니다
- 네트워크 지연으로 인해 약간의 딜레이가 있을 수 있습니다
- 스피커 제어는 사용 가능한 오디오 디바이스에 따라 다르게 작동할 수 있습니다

## 보안 고려사항

- 실제 프로덕션 환경에서는 organizer 권한 확인 로직을 구현해야 합니다
- 메시지 검증 및 인증 메커니즘을 추가하는 것을 권장합니다
