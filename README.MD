# Бэкенд-часть проекта RouteNavigator
## Описание проекта
Проект представляет собой клиент-серверное приложение для маркетплейса пассажирских перевозок.  

Бэкенд-часть написана на Java, с использованием известного фреймворка `Spring` (`Spring Boot`).

## Другие части проекта
Фронтенд: https://github.com/greenem-official/RouteNavigator-Frontend  
Конфигурация для Docker: https://github.com/greenem-official/RouteNavigator-Compose  

## Архитектура базы данных
В качестве базы данных используются сразу две на выбор - `PostgreSQL` (по умолчанию) и `SQLite`. Совместимость реализуется с помощью объектно-реляционного подхода к хранению данных в базе данных - с использованием `JPA` (Java Persistence API), где `Hibernate` выступает в качестве реализации JPA.

Используется несколько объектов-сущностей, хранимых в соотвутствующих таблицах базы данных. Операции с ними выполняются с помощью **репозиториев** из `Spring Data JPA`, которые автоматически реализуют базовые операции и методы для работы с данными, а также разрешают писать собственные SQL-запросы в объектно-реляционном формате колонок таблиц базы данных.  

Поверх интерфейсов-репозиториев есть **сервисы** для более развёрнутой логики работы с этим типом объектов.

## API
Обработка запросов с клиента (фронтенда) выполняется с помощью `Rest API` запросов, обрабатываемых с помощью средств `Spring` в классе проекта `MainRestController`.

## Авторизация
Авторизация реализована хранением хешей паролей, а также генерацией токенов с ограниченным временем действия. В данной реализации в качестве самого токена выступает объект типа `UUID`, логика генерации и валидации реализована самостоятельно.  

Генерация и валидация хешей паролей сделана с использованием готовой реализации алгоритма `BCrypt` из `Spring Security`.

## Данные базы данных
Тестовые данные сгенерированы с помощью кода в этом самом проекте. При переключении значения `DO_IMPORT` на `true` в исходном коде класса `TestDataImporter`, этот класс произведёт сброс всех известных ему таблиц базы данных и автоматически заполнит их заданными в нём данными. **Рейсы** генерируются без чётких исходных данных, на базе собственного простого алгоритма -  с помощью заданного временного диапазона и простой информации о популярности разрешённых для генерации локаций.  

Все эти данные далее можно легко скачать в SQL файл, чтобы положить в конфигурацию Docker-контейнера. `SQLite` версия же всегда находится уже готовой к использованию прямо в готовом .jar файле.

## Особенности сборки и запуска
**Рекомендация:**
> Для простого запуска в режиме "production" лучше использовать Docker конфигурацию

### Запуск готового образа
Конфигурация для Docker: https://github.com/greenem-official/RouteNavigator-Compose

### Сборка и запуск в "Dev" режиме

> **Обязательно использовать Java SDK 21 для корректной работы**

1.1. Если вы хотите иметь уже готовые тестовые данные бд, разкомментируйте `docker-entrypoint-initdb.d/init.sql` в `dev_env/compose.yml`  
1.2. Если вы хотите сгенерировать их вручную (**рекомендуется, т.к. готовая версия может устареть**), можно установить `DO_IMPORT = true` в классе `TestDataImporter` (всего лишь на один запуск).  

2. Проверьте путь к `Gradle JVM`, указанный в `gradle.properties`.  
3. Убедитесь, что у вас установлен Docker
4. Один раз запустите `dev_env/createDevEnv.{bat|sh}` для создания dev-окружения, а именно dev базы данных.  
5. Запустите главный метод класса `MarketplaceApplication`. Проверьте, что профиль Spring стоит на нужном значении, например, `postgresql`.  

<br/>
Если ваша IDE не поддерживает конфигурации запуска Spring Boot приложений, укажите JVM аргумент

> -Dspring.profiles.active=postgresql

Или как аргументы командной строки:

> --spring.profiles.active=postgresql

Либо запустите без аргументов, но проверьте, что `resources/application.properties` уже имеет те же настройки, что и нужный профиль.

### Сборка проекта в образ Docker
Для сборки образа `Docker` можно использовать скрипт `deployScript.{bat|sh}`.  

## Тесты
Для запуска тестов можно использовать скрипт `runTests.{bat|sh}`.

Примечание: активная конфигурация Spring (по умолчанию в коде это "postgres") должна иметь корректные данные подключения к базе данных dev-окружения
