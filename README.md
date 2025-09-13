# Amazon Chime SDK for Android
[Amazon Chime SDK 프로젝트 보드](https://aws.github.io/amazon-chime-sdk-js/modules/projectboard.html)

> 참고: SDK 소스 코드로 빌드하는 경우, `development` 브랜치에는 공개적으로 사용 가능한 Chime 미디어 라이브러리와 빌드되지 않거나 [공개 릴리스](https://github.com/aws/amazon-chime-sdk-android/releases)만큼 안정적이지 않을 수 있는 최신 변경 사항이 포함되어 있습니다.

## Amazon Chime을 기반으로 한 영상 통화, 음성 통화 및 화면 공유 애플리케이션 구축

### 🎯 원격 제어 기능 (Remote Control Features)

이 프로젝트는 **Real Time Data Message**를 활용하여 회의 참석자의 마이크와 스피커를 원격으로 제어하는 기능을 포함합니다.

**주요 기능:**
- 📢 **마이크 원격 제어**: 다른 참석자의 마이크 음소거/해제
- 🔊 **스피커 원격 제어**: 다른 참석자의 스피커 비활성화/활성화
- 🔄 **실시간 동기화**: 제어 명령이 즉시 대상 사용자에게 적용
- 👥 **참석자 목록 통합**: Roster 화면에서 클릭으로 간편한 제어

**구현 방식:**
- `"remote_control"` 토픽을 통한 Real Time Data Message 전송
- JSON 형태의 제어 명령 (`RemoteControlMessage`)
- 자동 UI 업데이트 및 사용자 알림

Android용 Amazon Chime SDK는 Amazon Chime 서비스의 회의를 지원하는 동일한 인프라 서비스를 사용하여 Android 애플리케이션에 협업 음성 통화, 영상 통화 및 화면 공유 보기 기능을 쉽게 추가할 수 있게 해줍니다.

이 Android용 Amazon Chime SDK는 AWS 계정에서 생성한 회의 세션 리소스에 연결하여 작동합니다. SDK에는 회의 세션 구성, 오디오 장치 나열 및 선택, 비디오 장치 전환, 화면 공유 보기 시작 및 중지, 볼륨 변경과 같은 미디어 이벤트 발생 시 콜백 수신, 오디오 음소거 및 비디오 타일 바인딩과 같은 회의 기능 관리 등 Android 애플리케이션에서 사용자 정의 통화 및 협업 환경을 구축하는 데 필요한 모든 것이 포함되어 있습니다.

또한 커뮤니티 요청과 그 상태를 확인할 수 있는 [Amazon Chime SDK 프로젝트 보드](https://aws.github.io/amazon-chime-sdk-js/modules/projectboard.html)도 있습니다.

시작하려면 다음 리소스를 참조하세요:

* [Amazon Chime](https://aws.amazon.com/chime)
* [Amazon Chime 개발자 가이드](https://docs.aws.amazon.com/chime/latest/dg/what-is-chime.html)
* [Amazon Chime SDK API 참조](http://docs.aws.amazon.com/chime/latest/APIReference/Welcome.html)
* [SDK 문서](https://aws.github.io/amazon-chime-sdk-android/amazon-chime-sdk/)

다음 가이드를 검토하세요:

* [API 개요](guides/api_overview.md)
* [시작하기](guides/getting_started.md)
* [자주 묻는 질문 (FAQ)](#자주-묻는-질문)
* [사용자 정의 비디오 소스, 프로세서 및 싱크](guides/custom_video.md)
* [활성 화자 기반 정책을 사용한 비디오 페이지네이션](guides/video_pagination.md)
* [콘텐츠 공유](guides/content_share.md)
* [회의 이벤트](guides/meeting_events.md)
* [이벤트 수집](guides/event_ingestion.md)
* [원격 비디오 구독 구성](guides/configuring_remote_video_subscription.md)
* [배경 비디오 필터](guides/background_video_filters.md)

## 설정

> 참고: 데모 애플리케이션만 실행하려면 [데모 앱 실행](#데모-앱-실행)으로 건너뛰세요

Amazon Chime SDK를 통합하려면 메인 SDK, 미디어 SDK 중 하나, 그리고 선택적으로 머신러닝 모듈을 포함해야 합니다.

### SDK 모듈

| 모듈                | 아티팩트                                            | 설명                                                    |
|------------------------|-----------------------------------------------------|--------------------------------------------------------------- |
| 메인 SDK (필수)    | `amazon-chime-sdk`                                  | 회의 제어, 플랫폼 통합 및 최상위 API.     |
| 미디어 SDK (하나 선택) | `amazon-chime-sdk-media`                            | ARM 장치용 전체 기능.                                 |
|                        | `amazon-chime-sdk-media-no-video-codecs`            | ARM 장치용 오디오 전용, 더 작은 크기.                      |
|                        | `amazon-chime-sdk-media-x86-stub`                   | ARM 장치용 전체 기능, x86 스텁 지원.            |
|                        | `amazon-chime-sdk-media-no-video-codecs-x86-stub`   | ARM 장치용 오디오 전용, x86 스텁 지원, 더 작은 크기. |
| ML SDK (선택사항)      | `amazon-chime-sdk-machine-learning`                 | 배경 흐림 및 교체 기능 활성화.                       |

> ⚠️ **x86 스텁 변형에 대한 참고사항**:  
> 이러한 아티팩트는 **ARM 장치를 완전히 지원**하지만, 범용 APK 또는 앱 번들 빌드를 지원하기 위해 **스텁된 x86/x86_64 코드**를 포함합니다.  
> x86 코드에서는 오디오/비디오 기능을 사용할 수 없으므로 **x86 에뮬레이터나 장치에서 실행하기 위한 것이 아닙니다**.

### 옵션 1: Maven Central을 통한 설치 (권장)
메인 SDK는 기본적으로 전체 미디어 SDK(`amazon-chime-sdk-media`)와 ML SDK(`amazon-chime-sdk-machine-learning`)를 포함합니다.  
다른 미디어 구현(예: 오디오 전용 또는 x86 호환)을 사용하려면 기본값을 **제외**하고 원하는 것을 명시적으로 추가해야 합니다.

#### 사용 예시
> `<version>`을 최신 [릴리스](https://github.com/aws/amazon-chime-sdk-android/releases/latest)의 최신 버전으로 교체하세요.

##### 전체 기능 (ARM)

```groovy
implementation 'software.aws.chimesdk:amazon-chime-sdk:<version>'
```

##### 오디오 전용 (ARM)
```groovy
implementation('software.aws.chimesdk:amazon-chime-sdk:<version>') {
    exclude group: 'software.aws.chimesdk', module: 'amazon-chime-sdk-media'
    exclude group: 'software.aws.chimesdk', module: 'amazon-chime-sdk-machine-learning'
}
implementation 'software.aws.chimesdk:amazon-chime-sdk-media-no-video-codecs:<media_version>'
```

##### 전체 기능 (ARM) + x86 스텁
```groovy
implementation('software.aws.chimesdk:amazon-chime-sdk:<version>') {
    exclude group: 'software.aws.chimesdk', module: 'amazon-chime-sdk-media'
}
implementation 'software.aws.chimesdk:amazon-chime-sdk-media-x86-stub:<media_version>'
```

##### 오디오 전용 (ARM) + x86 스텁
```groovy
implementation('software.aws.chimesdk:amazon-chime-sdk:<version>') {
    exclude group: 'software.aws.chimesdk', module: 'amazon-chime-sdk-media'
    exclude group: 'software.aws.chimesdk', module: 'amazon-chime-sdk-machine-learning'
}
implementation 'software.aws.chimesdk:amazon-chime-sdk-media-no-video-codecs-x86-stub:<media_version>'
```

### 옵션 2: AAR 파일 직접 사용
SDK 바이너리를 자체 프로젝트에 포함하려면 다음 단계를 따르세요.

#### 1단계: 바이너리 다운로드

최신 [릴리스](https://github.com/aws/amazon-chime-sdk-android/releases/latest)에서 필요한 `.aar` 파일을 다운로드하세요:

- `amazon-chime-sdk`
- 필요한 미디어 SDK 변형
- 선택적으로 `amazon-chime-sdk-machine-learning`

> **참고:** 다른 릴리스의 바이너리를 혼합하지 마세요.

`.aar` 파일을 앱의 `libs` 디렉토리에 복사하세요.

#### 2단계: Gradle 구성 업데이트

**프로젝트 수준** `build.gradle`에 추가:

```groovy
allprojects {
   repositories {
      flatDir {
        dirs 'libs'
      }
      //...
   }
}
```

**앱 수준** build.gradle에 추가:

```groovy
dependencies {
    implementation(name: 'amazon-chime-sdk', ext: 'aar')
    implementation(name: '<chosen-media-aar-name>', ext: 'aar')

    // 선택사항
    implementation(name: 'amazon-chime-sdk-machine-learning', ext: 'aar')
}
```

<chosen-media-aar-name>을 다운로드한 미디어 SDK 변형으로 교체하세요:
- amazon-chime-sdk-media
- amazon-chime-sdk-media-no-video-codecs
- amazon-chime-sdk-media-x86-stub
- amazon-chime-sdk-media-no-video-codecs-x86-stub

### Java 호환성
Java 17 호환성을 위해 `app/build.gradle` 내부에 추가하세요:

```groovy
android {
    compileOptions {
        sourceCompatibility JavaVersion.VERSION_17
        targetCompatibility JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}
```

## 데모 앱 실행

데모 앱을 실행하려면 다음 단계를 따르세요:

### 1단계: 서버 배포

[amazon-chime-sdk-js](https://github.com/aws/amazon-chime-sdk-js) 저장소의 `demos/serverless` 디렉토리에 있는 서버리스 데모를 배포하세요. 자세한 지침은 [여기](https://github.com/aws/amazon-chime-sdk-js/tree/main/demos/serverless)를 참조하세요.

### 2단계: 앱 실행

1. Android Studio에서 프로젝트를 엽니다
2. 앱을 빌드하고 실행합니다
3. 서버 URL을 입력하고 회의에 참가합니다

## 자주 묻는 질문

### Amazon Chime SDK는 무엇인가요?

Amazon Chime SDK는 개발자가 자신의 애플리케이션에 실시간 음성, 비디오, 화면 공유 및 메시징 기능을 추가할 수 있게 해주는 서비스입니다.

### 이 SDK는 무료인가요?

Amazon Chime SDK는 사용한 만큼 지불하는 요금제입니다. 자세한 요금 정보는 [Amazon Chime SDK 요금 페이지](https://aws.amazon.com/chime/chime-sdk/pricing/)를 참조하세요.

### 어떤 Android 버전이 지원되나요?

Amazon Chime SDK for Android는 API 레벨 21 (Android 5.0) 이상을 지원합니다.

### 동시에 몇 명의 참가자를 지원하나요?

Amazon Chime SDK는 최대 250명의 참가자가 참여하는 회의를 지원합니다. 비디오는 최대 25개의 타일을 동시에 표시할 수 있습니다.

### 네트워크 요구사항은 무엇인가요?

- 음성 통화: 최소 50kbps 업로드/다운로드
- 비디오 통화: 최소 1Mbps 업로드/다운로드
- 화면 공유: 추가로 1-2Mbps 업로드

### 보안은 어떻게 보장되나요?

Amazon Chime SDK는 전송 중 및 저장 시 암호화를 제공합니다. 모든 미디어는 TLS 1.2를 사용하여 암호화되며, 회의 데이터는 AWS의 보안 인프라에 저장됩니다.

## 기여하기

이 프로젝트에 기여하고 싶으시다면 [CONTRIBUTING.md](CONTRIBUTING.md)를 참조하세요.

## 라이선스

이 라이브러리는 Apache 2.0 라이선스에 따라 라이선스가 부여됩니다. 자세한 내용은 LICENSE 파일을 참조하세요.

## 지원

- [GitHub Issues](https://github.com/aws/amazon-chime-sdk-android/issues)를 통해 버그 리포트나 기능 요청을 제출하세요
- [AWS 개발자 포럼](https://forums.aws.amazon.com/forum.jspa?forumID=357)에서 질문하세요
- [AWS Support](https://aws.amazon.com/support/)를 통해 기술 지원을 받으세요
