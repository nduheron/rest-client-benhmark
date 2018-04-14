# Rest client benchmark

Comparaison des clients [Restemplate](https://spring.io/guides/gs/consuming-rest/) et [WebClient](http://www.baeldung.com/spring-5-webclient)

[![Build Status](https://travis-ci.org/nduheron/rest-client-benhmark.svg?branch=master)](https://travis-ci.org/nduheron/rest-client-benhmark)

## Scénario de test

Le service effectue 1 requête à une API pour obtenir une liste d'identifiants utilisateurs (10 utilisateurs). Puis, pour chaque identifiant appelle l'API pour obtenir les infos de l'utilisateur.

## Résultat

### 1 utilisateur appelle le service continuellement pendant 30 secondes

|Benchmark		|  Count	| Average	| 90 percentille	| Unit	|
|:-------		|:------:	|:------:	|:------------:		|:----:	|
|RestTemplate	|444		|135,275	|144,835			| ms/op	|
|WebClient		|1532		|39,149		|46,006				| ms/op	|


### 20 utilisateurs appellent le service simultanément

|Benchmark		|  Count	| Average	| 90 percentille	| Unit	|
|:-------		|:------:	|:------:	|:------------:		|:----:	|
|RestTemplate	|415		|423,509	|881,852			| ms/op	|
|WebClient		|450		|428,633	|527,539			| ms/op	|


## Composants techniques utilisés

* JDK 8
* [Maven](https://maven.apache.org/) 3.3.9
* [Jackson](https://github.com/FasterXML/jackson-docs) 2.9.4
* [Spring web](https://spring.io/guides/gs/consuming-rest/) 5.0.4
* [Httpclient](https://hc.apache.org/) 4.5.5
* [Spring webflux](http://www.baeldung.com/spring-5-webclient) 5.0.4
* [Wiremock](http://wiremock.org/) 2.16.0
* [jmh](http://openjdk.java.net/projects/code-tools/jmh/) 1.20
