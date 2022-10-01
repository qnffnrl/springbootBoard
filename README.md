# Build/Deploy to -> http://www.risker.shop:8080/board/main

# springbootBoard
## Toy 1
Springboot를 활용 게시판 제작

## 게시판 게시글 데이터베이스 설계 (v1)
![화면 캡처 2022-09-26 205356](https://user-images.githubusercontent.com/71891870/192281667-92061028-4c22-4be5-9412-dfc373958a61.png)

## 구현할 기능
* 게시글 CRUD, 페이징 (✔)
* 로그인기능 OAuth 활용해보기(Google, Naver) / ID 혹은 닉네임 페이지 상단에 표시 ( )
* 개인정보 수정 기능 ( ) 
* 우측 상단에 날씨 띄우기 / 날씨 API ( )
* 추후 생각나는 대로

## 이해안됬던것 참고 사이트
### Controller, Service, Repository, Entity(Domain)
![화면 캡처 2022-09-26 210026](https://user-images.githubusercontent.com/71891870/192283019-aeb5a466-a65a-4b99-8ac5-e46b2cf6fd6d.png)
* https://velog.io/@seungho1216/Spring-BootController-Service-Repository%EC%97%90-%EB%8C%80%ED%95%98%EC%97%AC
* https://azderica.github.io/00-java-repositorys/

## TroubleShooting 
### (게시글 상세 페이지)
* 특정 게시글을 불러오는 기능에서 JPARepository의 findById()를 사용하는데 해당 함수 반환값이 Optional임
* -> (해결) 이 optional의 반환값을 사용하려면 뒤에 .get()을 붙여줘야한다고 함 (이것 때문에 엄청 고생함)
### (게시글 상세 페이지2)
* 특정 게시글을 조회한 값이 controller까지 잘 옴 / view로 넘기면 model 전체랑 PK인 number은 잘 나오는데 (데이터는 넘어갔다는 소리) 나머지가 출력이 안됨
* -> 오류상으로는 해당 필드가 없다고 함 -> (해결) json 다루는 것 처럼 불러와야함 (board.writer 이런 식으로 / model이름.개별field이름)
* 근데 number는 왜 출력이 됬는지? -> 해당 controller 매핑에서 number가 넘어와서 number 그대로 그냥 써도 됬었음 ( @GetMapping("/board/content/{number}") )
