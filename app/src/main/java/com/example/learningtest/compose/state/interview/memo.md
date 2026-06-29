- LaunchedEffect: 실행된 부수효과 - 컴포저블이 컴포지션될 때 자동으로 코루틴 런치 실행.
  - 컴포저블의 생명주기에 따라 코루틴이 취소된다.
  - 키가 바뀌면 기존 코루틴 취소 후 새 코루틴 런치.
  - 리컴포지션이 일어나도 다시 실행되지 않음.
- rememberCoroutineScope: 코루틴 스코프를 기억해둔다. 
  - 컴포저블 함수이지만, 리턴 타입이 코루틴 스코프. 
  - Job 을 저장해두고, 이 코루틴 스코프에서 실행시킬 수 있음. 원할 때 취소시킬 수도 있음.
  - 컴포지션 종료시 코루틴 스코프가 자동으로 취소된다. 
  
- rememberUpdatedState
- DisposableEffect
- SideEffect
- produceState
- derivedStateOf
- snapshotFlow


















- LaunchedEffect
- rememberCoroutineScope
- rememberUpdatedState
- DisposableEffect
- SideEffect
- produceState
- derivedStateOf
- snapshotFlow