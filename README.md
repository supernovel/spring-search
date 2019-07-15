# spring-search
https://www.baeldung.com/rest-api-query-search-or-operation 에 대한 수정본

```
String search = "firstName:john AND lastName:doe AND createdAt>=2019-07-01T01:00"
myRepo.findAll(GenericSpecification.of(search));
```
