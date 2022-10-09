## Build/Deploy to -> http://www.risker.shop:8080/board/main
# springbootBoard
## Toy 1
Springboot를 활용 게시판 제작

## 게시판 게시글 데이터베이스 설계 (v2)
* view (BIGINT -> INT)
![화면 캡처 2022-10-09 171057](https://user-images.githubusercontent.com/71891870/194748276-895c3351-c1b1-440f-bbb7-0a51d6970cb5.png)

## 회원정보 데이터베이스 설계 (v1)
![화면 캡처 2022-10-08 234527](https://user-images.githubusercontent.com/71891870/194715915-67bf5b1f-4629-485a-822a-f36f65ddc0a4.png)

## 구현할 기능
* 게시글 CRUD, 페이징 (✔)
* 로그인기능 / ID 혹은 닉네임 페이지 상단에 표시 (✔)
* OAuth 활용해보기(Google, Naver) ( )
* 개인정보 수정 기능 ( ) 
* 우측 상단에 날씨 띄우기 / 날씨 API ( )
* 추후 생각나는 대로

## 이해안됬던것 참고 사이트
### Controller, Service, Repository, Entity(Domain)
![화면 캡처 2022-09-26 210026](https://user-images.githubusercontent.com/71891870/192283019-aeb5a466-a65a-4b99-8ac5-e46b2cf6fd6d.png)
* https://velog.io/@seungho1216/Spring-BootController-Service-Repository%EC%97%90-%EB%8C%80%ED%95%98%EC%97%AC
* https://azderica.github.io/00-java-repositorys/
* https://dev-coco.tistory.com/114?category=1032063

## TroubleShooting 회고
### (게시글 상세 페이지)
* 특정 게시글을 불러오는 기능에서 JPARepository의 findById()를 사용하는데 해당 함수 반환값이 Optional임
* -> (해결) 이 optional의 반환값을 사용하려면 뒤에 .get()을 붙여줘야한다고 함 (이것 때문에 엄청 고생함)

### (게시글 상세 페이지2)
* 특정 게시글을 조회한 값이 controller까지 잘 옴 / view로 넘기면 model 전체랑 PK인 number은 잘 나오는데 (데이터는 넘어갔다는 소리) 나머지가 출력이 안됨
* -> 오류상으로는 해당 필드가 없다고 함 -> (해결) json 다루는 것 처럼 불러와야함 (board.writer 이런 식으로 / model이름.개별field이름)
* 근데 number는 왜 출력이 됬는지? -> 해당 controller 매핑에서 number가 넘어와서 number 그대로 그냥 써도 됬었음 ( @GetMapping("/board/content/{number}") )

### (페이징 기능)
* 다음/이전 페이지 이동 버튼만 구현
* 블록 숫자 버튼구현 할려고 하는데 (숫자 버튼 클릭하면 해당 페이지로 이동) mustache에서는 로직을 짤 수가 없어 보류
* JPARepository에서 지원하는 pageable 덕분에 전체 글 수, 블록 수 등 페이지 관련 정보는 있는데 이걸 활용해 view(/board/main)에서 로직을 짤수가 없음
* -> MVC 패턴의 역할 분리를 확실히 할 순있지만 페이징 기능을 위해서 추후 thymeleaf로 변경해야 될듯 (thymeleaf문법으로 view에서 로직을 짤 수 있음)

### (Spring Security 로그인 기능 구현 중)
* Spring Security를 활용해서 로그인 기능을 구현하고 있는데 BCryptPasswordEncoder가 Bean으로 등록되지 않았다는 에러가 뜸
* Consider defining a bean of type 'org.springframework.security.crypto.password.PasswordEncoder' in your configuration.
* 방법은 SecurityConfig 클래스에 BCryptPasswordEncoder 메서드에 @Bean 어노테이션을 추가 하면 된다는 난 이미 되어있다
* 혹시 몰라 메인메서드에 BCryptPasswordEncoder 메서드를 추가하고 @Bean을 했는데 에러가 없어지긴 했는데 구글링해본바로는 이건 다른 케이스고 내코드 같은 경우는 config 파일에 @Bean이 되어있으면 오류가 안나는게 정상인것 같다 (뭔가 이상함)
* 계속 원인 찾는중... (2022.10.05)
* 해결 -> Spring Security 클래스가 spring 패키지 밖에 있었음 ㅋㅋ, 패키지 안으로 옮기니깐 바로 실행됨
* 구글링 하다가 spring이 특정 Bean을 못찾는다는 오류일 수가 있다해서 메인 클래스에 ComponentScan 경로를 직접 설정하는 어노테이션을 추가했는데, 경로를 추가하다가 문제의 config 파일이 있는 패키지가 자동완성이 안되는 걸 보고 발견함 
* 정리 : 맨 처음 오류 확인했을때 메인메서드에 @Bean 추가해야 한다는 방법과 내 코드의 경우는 그와 다른 케이스라고 했는데 정리하자면
* -> 내 프로젝트에서 Spring Security에 대한 설정을 configuration/SecurityConfig.class 파일에 따로 했는데 여기서 Bean 등록을 메인클래스에 해버리면 해당 config 파일을 무시하고 디폴트 설정으로 이뤄진 또다른 Spring Security를 사용한다는 소리이다 

### (Spring Security 로그인 기능 구현 중2)
* 회원가입 잘됨, DB에도 잘 들어가고 password도 해싱되서 잘 들어감
* 근데 로그인을 하면 Spring Security에서 설정한, 로그인 성공 시 넘어가게한 페이지로 안넘어감 (config 파일에서의 에러는 아닌듯 하다)
* Controller에서 로그인 성공 시 세션에 사용자 정보를 추가하는 로직이 있는데 혹시나 해서 해당 변수를 찍어보니 null값 이었다 (로그인 성공해도 세션 활성화가 안됬던 것)
* 계속 해결 중 ... (2022.10.06)
* 해결 -> 일단 로그인 프로세스 흐름을 잘못이해함 / 일단 로그인 시도 후 성공/실패 결과에 따라 이동되는 페이지를 controller에서 직접 쓰는게 아니라 Spring Security 설정에서 하는 거였음, 로그인 성공/실패를 따지는 로직은 Spring Security에 내장된 경로로 명시 (loginProcessingUrl(), logoutUrl() 등)
* 처음 문제가 로그인 성공시 넘어가도록 설정한 페이지로 안넘어간다고 했는데 원인은 Spring Security에서 설정한 접근 허용한 경로가 /auth/** 인데 로그인 성공 유무 판단 로직 경로에서 앞에 /auth를 안붙임 그리고 세션을 설정하는 코드를 이동되는 페이지의 Controller 메서드에 안하고 로그인 페이지로 이동하는 메서드에 넣어놨었음 (2022.10.07)
* 로그인 기능 구현 후, 글 작성 시 임시로 넣은 작성자 필드를 세션의 usernickname으로 변경

### (Spring Security Login Failure Handler 기능 구현중)
* 로그인 실패 시 실패 원인을 alert하며 로그인 페이지로 리다이렉트 하는 기능 구현중
* Spring Security 설정 파일에서 failureHandler() 메서드를 활용하고 AuthenticationFailureHandler 인터페이스의 구현체인 SimpleUrlAuthenticationFailureHandler를 상속해서 구현중이다 (setDefaultFailureUrl()를 사용하기 위해)
* 해당 구현체를 상속받는 로그인 실패 핸들러 클래스를 생성해서 예외처리를 하는데 onAuthenticationFailure()라는 메서드를 오버라이딩 한다
* 그런데 @Override 어노테이션에서 오버라이딩할 메서드가 없다고 오류가 뜬다 -> SimpleUrlAuthenticationFailureHandler를 인식을 못하는거 같은데 뭔가 내가또 잘못한데 있는것 같다... 계속 원인 찾는중(2022.10.09)
* 해결 -> 라이브러리 임포트를 잘못했었다, AuthenticationException 메서드를 사용하려면 (import org.springframework.security.core.AuthenticationException)을 해야되는데 (import javax.naming.AuthenticationException)를 임포트 했던 것이었다
* 인텔리제이의 자동완성을 적극 활용하다보니 이런 단점도 있는것 같다. 자동완성으로 임포트할 라이브러리를 선택할때 라이브러리 이름을 잘 봐야할것 같다
* 두 라이브러리 이름이 똑같아서 원인을 찾기 힘들었던것 같다
* 이번 오류는 뭔가 인텔리제이 오류나 공백 오류 같은 진짜 오류일 것 같았는데 아니었다... 역시 컴퓨터는 거짓말을 하지 않는다.
