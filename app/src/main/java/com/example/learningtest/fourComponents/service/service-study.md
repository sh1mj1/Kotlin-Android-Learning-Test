# Service

서비스는 UI 가 없는 백그라운드 작업을 수행하는 컴포넌트.
앱이 꺼져 있어도 계속 동작할 수 있고, 사용자와 직접 상호작용하지 않음.

크게 아래처럼 분류할 수 있다.

* BoundService: 클라이언트가 서비스에 붙어서 직접 호출하거나 통신할 수 있는 서비스.
* Unbound(Started) Service: 클라이언트와의 직접 연결없이 독립적으로 실행되는 서비스 (`startService()` 로 시작됨.)
    * Foreground Service: 중요한 작업을 수행 중임을 알리기 위해 알림을 표시하면서 실행되는 서비스
    * Background Service: 사용자에게 직접적으로 표시되지 않고 앱이 백그라운드 상태일 때 동작하는 서비스.

## Bound Service

클라이언트가 서비스에 붙어서 직접 호출하거나 통신할 수 있는 서비스.

즉, 앱의 컴포넌트(Activity, Fragment 등)가 `bindService()` 로 서비스에 연결(bind)하고, 연결된 동안에만 살아있는 서비스.
모든 클라이언트가 unbind 되면 자동으로 종료된다.

백그라운드에서도 유지시키고 싶다면 `startService()` 로 Unbound(Started) Service 로도 사용 가능. (바운드 서비스이면서 백그라운드 서비스)

### Bound Service 예시

* GPS 센서 데이터를 실시간으로 받기
* 음악 앱에서 현재 재생 상태 요청(표시)

### Bound Service 샘플 만들어보기

[SimpleBoundMusicService.kt](bound/SimpleBoundMusicService.kt),
[SimpleBoundMusicActivity.kt](bound/SimpleBoundMusicActivity.kt) 파일 참고.

* 바운드 방식: `bindService()` + LocalBinder
* 통신 방식: 서비스와 액티비티가 같은 앱, 같은 프로세스에서 동작하므로 직접 메서드 호출할 수 있다.
* 인터페이스 제공: Binder 를 상속한 LocalBinder 클래스에서 `getService()`로 서비스 인스턴스 전달한다.
* 액티비티에서의 사용: `onServiceConnected()` 에서 서비스 인스턴스를 받아 직접 메서드(`play()`, `pause()`, `progress`) 호출한다.
* 서비스 수명: 모든 클라이언트(여기서는 [SimpleBoundMusicActivity.kt](bound/SimpleBoundMusicActivity.kt) 가 언바인드 되면 자동
  종료된다.)

1. 액티비티에서 `ServiceConnection` 객체를 만든다. 여기서 서비스가 연결될 때 서비스를 초기화한다.
2. 액티비티가 onStart 될 때 `bindService`를 통해 서비스를 연결한다.
3. 서비스 연결 후, `observeServiceState()` 를 통해 상태 수집을 시작한다.
4. Compose UI 가 서비스 상태를 바탕으로 화면 갱신
5. 액티비티의 `onStop()` 에서 `unbindService()` 로 연결 해제한다. 
   이 경우, 모든 액티비티가 unbind 된 것이므로 서비스가 종료된다.

> Service는 음악 재생 로직을 CoroutineScope로 실행

## UnBounded(Started) Service

클라이언트와의 직접적인 연결 없이 `startService()` | `startForegroundService()` 로 독립적으로 실행되는 서비스.
명시적으로 `stopSelf()` | `stopService()` 를 해야 종료된다.

UI 없이 독립적으로 작업을 수행하는 경우에 사용된다.

### ForegroundService

중요한 작업을 수행 중임을 알리기 위해 알림을 표시하면서 실행되는 서비스.
일반적으로 `startForeground()` 로 시작, 수동으로 중단해서 종료된다.

#### ForegroundService 예시

* 푸시 알림에 progress 를 보여주면서 백그라운드 다운로드.
* GPS/위치 추적, 음악 재생, 통화 미러링(Zoom 등)

> 백그라운드로 웹툰 다운로드하고 다운로드 중에 progress 를 푸시 알림에 보여주면서 다운로드 완료 시 푸시 알림을 누르면
> 저장소 페이지로 이동하는 경우는 포그라운드 서비스 + `Notification" + `Intent` 조합 으로 구현

### Background Service

사용자에게 전혀 표시되지 않고 앱이 백그라운드 상태일 때 동작하는 서비스.

안드로이드 버전이 업그레이드되면서 제한이 많이 생김.
그래서 최근에는 대안으로 WorkManager, Foreground Service + BroadcastReceiver 등을 사용한다.

### 음악 재생 앱 예시

음악 재생 앱같은 경우, UI(Activity)와 직접 소통도 하고, 앱이 종료되어도 음악은 계속 재생되어야 한다.
그러므로 Bound Service & Foreground Service 를 사용한다.

* Bound Service: UI 에서 음악의 재생 상태, progress 등 정보를 가져오고 조작해야 함.
* Foreground Service: 음악이 재생되는 동안, 앱이 꺼져도 시스템이 서비스를 죽이지 않도록 하기 위함.

## Service 의 Lifecycle

서비스는 Service 추상 클래스를 상속받고 `onBind()` 메서드를 오버라이드 해야 한다.

* `onCreate()`: 서비스가 처음 생성될 때 1회 호출됨.
    * 초기화 작업 수행(예: 스레드 생성, 리스너 등록 등)
* `onStartCommand()`: 다른 컴포넌트가 `startService()` 호출 시 실행
    * 백그라운드 작업 시작. 작업 완료 후 `steopSelf()` 또는 `stopService()` 호출해야 함.
* `onBind()`: bindService() 호출 시 실행
    * 바인딩 방식으로 서비스에 접근하는 컴포넌트에 통신용 `IBinder` 객체 제공.
* `onDestroy()`: 서비스가 종료될 때 호출됨
    * 리소스 해제, 스레드 종료, 리스너 제거 등 정리 작업 수행.

## 📘 실습 정리: Bound Service & Foreground Service 예제

이 프로젝트에서는 안드로이드 서비스의 구조와 활용을 학습하기 위해 다음과 같은 실습을 진행하였다:


### 포그라운드 + 바운드 서비스 (Foreground + Bound Service)

[ForegroundBoundMusicActivity.kt](foregroundboud/ForegroundBoundMusicActivity.kt), 
[ForegroundBoundService.kt](foregroundboud/ForegroundBoundService.kt) 파일

- `MediaPlayer`를 직접 제어하고, 음악 재생을 관리
- `startForeground()`와 알림(Notification)을 통해 포그라운드 상태 유지
- `MediaSessionCompat`를 통해 시스템과의 통합 제공 (알림 제어, 재생 상태 공유 등)
- Compose UI에서 `StateFlow` 기반으로 서비스 상태를 구독하여 UI에 반영
- 알림 채널(NotificationChannel)은 액티비티에서 동적으로 생성하여 최신 권한 정책 대응
