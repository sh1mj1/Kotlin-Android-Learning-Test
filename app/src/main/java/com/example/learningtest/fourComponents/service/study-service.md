# Service

서비스는 백그라운드에서 오래 실행되는 작업을 처리하기 위한 컴포넌트입니다.
UI 없이 동작하며, 음악 재생, 파일 다운로드 등에 주로 사용됩니다.
앱이 종료되어도 계속 동작할 수 있고 사용자와 직접 상호작용하지 않습니다.

크게 아래처럼 분류할 수 있습니다:

* BoundService: 클라이언트가 서비스에 붙어서 직접 호출하거나 통신할 수 있는 서비스
* Started(Unbound) Service: 클라이언트와의 직접 연결 없이 독립적으로 실행되는 서비스(`startService()`)
    * Foreground Service: 중요한 작업을 수행 중임을 알리기 위해 알림을 표시하면서 실행되는 서비스
    * Background Service: 사용자에게 직접적으로 표시되지 않고, 앱이 백그라운드 상태일 때 동작하는 서비스

## Bound Service

다른 앱 컴포넌트(액티비티)와 상호작용할 수 있도록 바인딩된 상태에서 실행되는 서비스입니다.
클라이언트는 `bindService()` 로 서비스에 연결하고,
`IBinder` 인터페이스를 통해 메서드를 직접 호출할 수 있습니다.
즉, 앱의 컴포넌트(Activity, Fragment 등)가 `bindService()` 로 서비스에 연결(bind)하고,
연결된 동안에만 살아있는 서비스입니다.
여러 클라이언트가 동시에 바인딩될 수 있으며, 바인딩된 모든 클라이언트가 unbind 되면 자동으로 종료됩니다.

백그라운드에서도 유지시키고 싶다면 `bindService()` 와 `startService()` 를 함께 호출하여
Started(Unbound) 서비스이면서 Bound Service 로 사용할 수 있다.

### Bound Service 예시

* 음악 앱에서 현재 재생 상태가 액티비티에 표시
* GPS 센서 데이터를 실시간으로 받아서 액티비티에 표시

### Bound Service 샘플 만들어보기

[SimpleBoundMusicService.kt](bound/SimpleBoundMusicService.kt),
[SimpleBoundMusicActivity.kt](bound/SimpleBoundMusicActivity.kt) 파일 참고.

* 바운드 방식: `bindService()` + Binder 를 구현한 클래스(`LocalBinder`)
* 통신 방식: 서비스와 액티비티가 같은 앱, 같은 프로세스에서 동작하므로 직접 메서드를 호출할 수 있습니다.
* 인터페이스 제공: Binder 를 상속한 `LocalBinder` 를 통해 서비스 객체에 접근 혹은 서비스 객체의 메서드를 호출합니다.
* 액티비티에서의 사용: `onServiceConnected()` 에서 서비스의 인스턴스를 받아서 직접 메서드를 호출합니다.
* 서비스 수명: 모든 클라이언트(여기서는 [SimpleBoundMusicActivity.kt](bound/SimpleBoundMusicActivity.kt)) 가 언바인드되면
  자동으로 종료됩니다.

서비스와 액티비티가 동일 프로세스에 있다면, 굳이 `Messenger`, `AIDL` 같은 IPC 계층을 사용할 필요가 없습니다.
Binder 객체는 Client-Server 모델에서 바로 메서드를 호출할 수 있는 인터페이스를 제공합니다.
그래서 JNI 나 커널의 Binder Driver를 거치지 않습니다.

Binder 는 안드로이드의 핵심 아키텍처 구성 요소로, 시스템 서비스들과도 동일한 방식으로 통신하게 합니다.
즉, 개발자로 하여금 같은 앱 내에서도 `Binder` 를 사용하도록 하여, 개발자에게 일관된 패턴을 제공합니다.

1. 액티비티에서 `ServiceConnection` 객체를 생성합니다. 여기서 서비스가 연결될 때 서비스를 초기화합니다.
2. 액티비티가 `onStart` 될 때 `bindService`를 통해 서비스를 연결합니다.
3. 서비스를 연결한 후, `observeServiceState` 를 통해 상태 수집을 시작합니다.
4. Compose UI 가 서비스 상태를 바탕으로 화면을 갱신합니다.
5. 액티비티의 `onStop` 에서 `unbindService`로 연결 해제합니다. 이 경우 모든 액티비티가 unbind 된 것이므로 서비스가 종료됩니다.

## UnBounded(Started) Service

클라이언트와의 직접적인 연결 없이 `startService()` 혹은 `startForegroundService()` 로 독립적으로 실행되는 서비스입니다.
명시적으로 `stopSelf()` 혹은 `stopService()` 를 해야 종료됩니다.

UI 없이 독립적으로 작업을 수행하는 경우에 사용됩니다.

### ForegroundService

`ForegroundService` 는 사용자에게 알림을 표시하며 실행되는 서비스입니다.
시스템에 의해 강제 종료되지 않도록 보장됩니다. (종료되어도 재실행 flag 들을 설정할 수 있습니다.)
`startForegroundService()` 호출로 시작되며, 명시적으로 중단해야 종료됩니다.

#### ForegroundService 예시

* 푸시 알림에 progress 를 보여주면서 백그라운드 다운로드.
* GPS/위치 추적, 음악 재생, 통화 미러링(Zoom 등)

### Background Service

`BackgroundService` 는 사용자에게 표시되지 않고 앱이 백그라운드 상태일 때 동작하는 서비스입니다.
위치 추적, 데이터 동기화같은 작업을 조용히 처리하지만, 안드로이드 버전이 올라가면서 제약이 많아졌습니다.
최근에는 `WorkManager` 나 `ForegroundService` 와 다른 컴포넌트의 조합으로 대체되는 경우가 많습니다.

### 음악 재생 앱 예시

음악 재생 앱 같은 경우, UI(Activity) 와 직접 소통도 하고, 앱이 종료되어도 음악은 계속 재생되어야 합니다.
그러므로 `BoundService` 와 `ForegroundService`를 사용합니다.

* Bound Service: Ui 에서 음악의 재생 상태, progress 등 정보를 가져오고 조작해야 합니다.
* Foreground Service: 음악이 재생되는 동안, 앱이 꺼져도 시스템이 서비스를 죽이지 않도록 하기 위함입니다.

## Service 의 Lifecycle

서비스는 `Service` 추상 클래스를 상속받고 `onBind()` 메서드를 오버라이드 해야 합니다.

* `onCreate()`: 서비스가 처음 생성될 때 1회 호출됨.
    * 초기화 작업 수행(예: 스레드 생성, 리스너 등록 등)
* `onStartCommand()`: 다른 컴포넌트가 `startService()`호출 시마다 실행됩니다.
    * 백그라운드 작업을 시작합니다. (워커 스레드에서 작업을 해야 합니다.)
    * 반환값(`START_STICKY`) 등은 시스템이 서비스가 죽었을 때 재시작 정책에 영향을 줍니다.
    * 작업 완료 후 `stopSelf()` 또는 `stopService()`를 호출해야 합니다.
* `onBind()`: `bindService()` 를 호출 시에 실행됩니다.
    * 바인딩 방식으로 서비스에 접근하는 컴포넌트에 통신용 `IBinder` 객체를 제공합니다.
* `onUnbind()`: 마지막 바인딩 해제시 호출됩니다.
* `onDestroy()`: 서비스가 종료될 때 호출됩니다.

## 📘 실습 정리: Bound Service & Foreground Service 예제

이 프로젝트에서는 안드로이드 서비스의 구조와 활용을 학습하기 위해 다음과 같은 실습을 진행하였습니다

### 포그라운드 + 바운드 서비스 (Foreground + Bound Service)

[ForegroundBoundMusicActivity.kt](foregroundboud/ForegroundBoundMusicActivity.kt),
[ForegroundBoundService.kt](foregroundboud/ForegroundBoundService.kt) 파일

- `MediaPlayer`를 직접 제어하고, 음악 재생을 관리
- `startForeground()`와 알림(Notification)을 통해 포그라운드 상태 유지
- `MediaSessionCompat`를 통해 시스템과의 통합 제공 (알림 제어, 재생 상태 공유 등)
- Compose UI에서 `StateFlow` 기반으로 서비스 상태를 구독하여 UI에 반영
- 알림 채널(NotificationChannel)은 액티비티에서 동적으로 생성하여 최신 권한 정책 대응
