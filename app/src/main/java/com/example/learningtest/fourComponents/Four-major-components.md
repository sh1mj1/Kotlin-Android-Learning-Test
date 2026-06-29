# 안드로이드 4대 구성요소

![Four-major-android-app-components.png](Four-major-android-app-components.png)

각 컴포넌트는 시스템이나 사용자가 앱에 진입할 수 있는 진입점이다.
일부 컴포넌트는 실행되기 위해서 다른 컴포넌트의 도움을 필요로 하는 경우가 있다.

앱 컴포넌트는 Android 앱을 구성하는 기본 빌딩 블록이다.
컴포넌트 종류마다 목적과 생명 주기가 다르며, 생명 주기는 컴포넌트가 생성되고 제거되는 방식을 정의한다.

## 구성요소

* [Activity](activity/study-activity.md)
    * 사용자 인터페이스가 있는 단일 화면을 나타낸다.
    * A single screen with a user interface.
* [Service](service/study-service.md)
    * 다양한 이유로 어떤 작업을 백그라운드에서 계속 실행하기 위한 컴포넌트이다.
    * 사용자 인터페이스를 제공하지 않는다.
* [Broadcast receiver](broadcast/study-broadcast-receiver.md)
    * 시스템 혹은 특정 앱은 외부에서 앱으로 이벤트를 전달할 수 있다.
    * 이 때 앱이 시스템 전체 브로드캐스트 알림에 응답할 수 있도록 하는 컴포넌트이다.
* [Content provider](contentprovider/study-content-provider.md)
    * 앱의 데이터를 다른 앱과 공유할 수 있도록 해주는 컴포넌트이다.
    * 외부 앱이 안전하게 자신의 앱 데이터에 접근할 수 있도록 하는 것이 주 목적이다.
    * 다음 위치에 저장할 수 있는 공유 앱 데이터 집합을 관리한다.
        * 파일 시스템
        * 같은 앱 또는 다른 앱의 SQLite 데이터베이스
        * 웹
        * 앱이 접근할 수 있는 기타 영구 저장소 위치

## Reference

* https://developer.android.com/guide/components/fundamentals
* Image: https://medium.com/@Abderraouf/understand-android-basics-part-1-application-activity-and-lifecycle-b559bb1e40e
