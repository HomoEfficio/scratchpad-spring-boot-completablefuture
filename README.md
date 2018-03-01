# SpringBoot에서 CompletableFuture 테스트

CompletableFuture + AsyncRestTemplate + ThreadPoolTaskExecutor 로 SpringBoot에서의 비동기 프로그래밍 테스트

## 테스트 구성

```
LoadTester -- HTTP --> SpringBootCompletableFutureApplication
                                                          └-- HTTP --> RemoteService1
                                                          └-- HTTP --> RemoteService2
```

## 테스트 방법

1. RemoteServer1, RemoteServer2 실행
  - RemoteService2 에서 에러 유발 테스트 가능

1. SpringBootCompletableFutureApplication 실행
  - ExecutorConfig에서 ThreadPoolTaskExecutor의 설정을 바꿔가며 테스트 가능

1. JDK_HOME/bin 의 jmc 실행
  - SpringBootCompletableFutureApplication의 MBean 실행
  - 생성되는 Thread 현황 확인 가능

1. LoadTester 실행
  - Sync/Async 비교 테스트 가능

## 참고 자료

토비의 봄 TV - https://www.youtube.com/channel/UCcqH2RV1-9ebRBhmN_uaSNg

- 8회 스프링 리액티브 프로그래밍(4) 자바와 스프링의 비동기 기술
- 10회 스프링 리액티브 프로그래밍(6) AsyncRestTemlate의 콜백 헬과 중복 작업 문제
- 11회 스프링 리액티브 프로그래밍(7) CompletableFuture


