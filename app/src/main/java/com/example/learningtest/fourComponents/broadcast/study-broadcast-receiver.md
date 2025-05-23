# Broadcast Receiver (브로드캐스트 리시버)

시스템이나 앱에서 발생한 이벤트를 수신학고 짧은 작업을 수행하는 컴포넌트입니다.
예를 들어 부팅 완료, 배터리 부족, 네트워크 변경 등 다양한 시스템 이벤트를 감지할 수 있습니다.
정적(Manifest 등록)과 동적(Java/Kotlin 코드 등록)으로 등록되며 UI 없이 빠르게 실행됩니다.

## 사용되는 상황:

* 시스템이 부팅 완료했을 때 앱에서 자동으로 무언가 시작하고 싶을 때
* 배터리가 부족할 때(`BATTERY_LOW`)
* 인터넷 연결이 바뀌었을 때 (`CONNECTIVITY_CHANGE`)
* 앱 간 커뮤니케이션이 필요할 때 (ex: 알람 앱이 알람 울렸다고 전송)

> 액티비티, 서비스도 앱 간 커뮤니케이션이 당연히 가능합니다.
> 브로드캐스트 리시버는 브로드캐시트 이벤트 전달 중심으로 커뮤니케이션하는 것입니다.
> 여러 앱이 동시에 이벤트 수신도 가능하며, UI 없이 짧게 실행됩니다.

브로드캐스트 리시버는 다른 앱이나 시스템(OS)이 보내는 브로드캐스트 인텐트를 수신해야 동작합니다.
OS 는 기본적으로 네트워크 변경, 부팅 완료, 배터리 부족 등 다양한 이벤트가 발생하면 자동으로 브로드캐스트를 보냅니다.
앱은 해당 이벤트에 맞는 인텐트 필터로 리시버를 등록해서 이를 감지하고 처리할 수 있습니다.

## 종류

1. 정적 등록(Manifest 에 선언 -> 앱이 꺼져 있어도 수신 가능. Android 8.0 이후 제한 있음)
2. 동적 등록(코드에서 `registerReceiver` -> 앱 실행 중일 때만 수신 가능)

## 네트워크 상태 예시 (동적으로 Broadcast 등록)

네트워크 상태 변경 브로드 캐스트는 암시적 브로드캐스트입니다. 
그래서 Manifest 정적 등록이 제한되어 있습니다. 
수신 대상이 여러 앱이기 때문입니다.

[NetworkChangeReceiver.kt](dynamic/NetworkChangeReceiver.kt)

이 리시버는 네트워크 상태 변경 브로드캐스트를 수신합니다.
네트워크 연결 여부 판단 로직은 콜백을 통해 [NetworkActivity.kt](dynamic/NetworkActivity.kt) 가 로직을 처리합니다.
액티비티에서 `CONNECTIVITY_ACTION`(네트워크 상태 변경 브로드 캐스트)이 발생하면, 이 객체에게 전달합니다.
그리고 다시 콜백을 통해 액티비티가 연결 여부를 판단합니다.

### 동작 순서

1. `IntentFilter(ConnectivityMananger.CONNECTIVITY_ACTION)` 로 브로드캐스트 수신을 요청합니다.
2. 시스템이 네트워크 상태 변경 이벤트 발생 시 `CONNECTIVITY_ACTION` 로 브로드캐스트합니다.
3. 등록된 `NetworkChangeReceiver` 의 `onReceive()` 가 호출됩니다.
4. 그 안에서 정의한 `onNetworkChanged()` 콜백이 실행됩니다.

### 플랫폼 아키텍처에서의 동작

1. App Layer: `NetworkActivity` 에서 `registerReceiver()` 로 네트워크 상태 변경 감지를 시스템에 요청합니다.
    * 이 작업은 내부적으로 Binder IPC 를 통해 Android 시스템 서비스에 등록됩니다.
2. Framework Layer: Android 시스템이 네트워크 상태가 바뀌는 것을 감지합니다.
    * 이 이벤트는 `ConnectivityService` -> `ActivityManagerService`를 통해
      앱이 등록한 브로드캐스트 리시버로 브로드캐스트 인텐트를 전달합니다.
3. `ActivityManagerService` 는 앱의 프로세스가 살아있는지 확인하고 해당 앱의 프로세스가 살아있으면 직접 `onReceive()`를 콜백으로 호출합니다.

## 자동 알림 예시(정적으로 Broadcast 를 Manifest에 등록)

알람 트리거는 명시적 브로드캐스트입니다.
수신 대상이 특정 앱의 특정 리시버입니다.
그래서 Manifest 에 정적 등록이 가능합니다.

| 컴포넌트                           | 역할                                |
|--------------------------------|-----------------------------------|
| `AlarmManger`                  | 특정 시간에 알람 예약                      |
| `PendingIntent.getBroadcast()` | 알람이 울릴 때 실행할 BroadcastReceiver 등록 |
| `BroadcastReceiver`            | 실제 알림을 처리                         |
| `NotificationManager`          | 푸시 알림 생성 및 표시                     |
| `POST_NOTIFICATIONS` 권한        | Android 13+ 에서 알림 허용 받기 위해 필요     |

### 동작 순서:

1. 알람 예약 (`AlarmManager.setExact`)
2. 지정 시간 도달
3. 시스템이 인텐트를 브로드캐스트
4. Manifest에 등록된 `BroadcastReceiver` 호출
5. `onReceive()` 실행

### 구현 설명

1. `AlarmActivity`:
    1. 권한 확인
    2. `PendingIntent` 알림이 울릴 시 호출될 리시버를 지정
    3. `AlarmManager` 로 특정 시간에 알람 예약
2. `NotificationReceiver` 에서 알람 트리거가 발생했을 때 `onReceive`호출
    1. `NotificationManager`: 시스템에서 알림을 띄우기 위한 매니저
    2. `NotificationChannel`: 고유한 채널 생성. 사용자 설정에서 이 채널을 통해 알림 관리
    3. `PendingIntent`: 알림 클릭 시 실행될 Intent를 담는 PendingIntent 생성. 시스템이 앱 대신 실행해줌.
    4. 알림 구성 후 `notify()`로 사용자에게 보여줌.

### 플랫폼 아키텍처에서의 동작

```text
App Layer: AlarmActivity → AlarmManager.setExact()

        ↓ IPC

Framework Layer: AlarmManagerService → 알람 큐 등록

        ↓ 시간 도달

System Server: Broadcast Intent 발송 → ActivityManager가 앱의 BroadcastReceiver 호출

        ↓

App Process: NotificationReceiver.onReceive() 호출 → NotificationManager.notify()

        ↓

User Action: 사용자가 알림 클릭 → AlarmActivity 실행
```
