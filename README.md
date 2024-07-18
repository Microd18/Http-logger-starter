# Spring Boot Starter для логирования HTTP запросов

### Описание проекта:
Spring Boot Starter, который предоставляет возможность логировать HTTP запросы в вашем приложении на базе Spring Boot.
Spring Boot Starter логирует все входящие и исходящие HTTP запросы и ответы вашего приложения.
Логирование включает в себя метод запроса, URL, заголовки запроса и ответа, код ответа и время обработки запроса.

### Запуск стартера:
- Клонируйте репозиторий https://github.com/Microd18/Http-logger-starter.git.
- Запустите команду ```mvn clean install```, после чего стартер будет сохранен в локальный Maven репозиторий.
- Теперь можно подключать этот стартер как зависимость, указав его в файле pom.xml соответствующего проекта.
```
<dependency>
   <groupId>com.example</groupId>
   <artifactId>http-logger-starter</artifactId>
   <version>0.0.1-SNAPSHOT</version>
</dependency>
```
- По умолчанию логирование всегда включено.
- Для отключения логирования в файле ```application.properties``` необходимо прописать ```request-logger.enabled=false```
- Также в стартере есть контроллер для демонстрации работы.

URL: http://localhost:8080

Тестовые методы - ```GET /test/hello``` ```POST /test/user```

### Технологии, используемые в проекте:
```
- Java 17
- Spring Boot
- SpringDoc
- Maven
- Lombok
```
