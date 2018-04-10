# SimpleCalculator  
[![CircleCI](https://circleci.com/gh/xfl03/SimpleCalculator/tree/master.svg?style=svg)](https://circleci.com/gh/xfl03/SimpleCalculator/tree/master)  
Simple Calculator in Kotlin  
## Feature
- Support BigInteger & BigDecimal
- Support Elementary arithmetic & Bracket
- Support Integer only mode
- Could auto fix right bracket
- Show reason and position for syntax error
- Print out fraction & decimal
- Print mixed fraction if needed 

## Technology Stack
- Kotlin (JVM)
- JUnit 5
- Gradle 4
- CircleCI 2

## Build
````
.\gradlew clean build
````

## Run
````
java -jar .\build\libs\SimpleCalculator-1.1-SNAPSHOT.jar -m
````
### Argument
`-d` `--debug` DebugMode  
`-f` `--fix` Auto Fix Right Bracket in the end  
`-m` `--mixed` Use Mixed Fraction  
`-i` `--int` Integer Only  
`-nd` `--no-dec` No decimal out print  