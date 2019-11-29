# jutils
Библиотека с utility слассами и классами для логгирования, доступа к mysql, redis 

# Заливка изменений
После изменений необходимо задеплоить артефакт в artifactory, который у нас хостится в GitHub, репозитории jutils, ветка mvn-repo.

https://github.com/AspiraLimited/jutils/tree/mvn-repo

Чтобы maven смог залить артефакты на GitHub необходимо прописать в ~/.m2/settings.xml свои креденшеналы на дрступ к github:

Шаблон ~/.m2/settings.xml:

```
<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0"
      xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
      xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0
                          https://maven.apache.org/xsd/settings-1.0.0.xsd">
      <localRepository/>
      <interactiveMode/>
      <offline/>
      <pluginGroups/>
<servers>
  <server>
    <id>github</id>
    <password>....</password>
  </server>
</servers>
      <mirrors/>
      <proxies/>
      <profiles/>
      <activeProfiles/>
</settings>
```

Не нужно писать свой пароль в файле, нужно сгнерировать Personal access token вот здесь:
https://github.com/settings/tokens